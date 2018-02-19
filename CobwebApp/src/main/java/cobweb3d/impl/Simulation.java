package cobweb3d.impl;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.AgentSimilarityCalculator;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.MutatorListener;
import cobweb3d.plugins.StateParameter;
import cobweb3d.plugins.StatePlugin;
import cobweb3d.plugins.exchange.ExchangeMutator;
import cobweb3d.plugins.food.ConsumptionMutator;
import cobweb3d.plugins.reproduction.ReproductionMutator;
import cobweb3d.plugins.states.AgentState;
import cobweb3d.plugins.transform.TransformationMutator;
import cobweb3d.ui.SimulationInterface;
import util.RandomNoGenerator;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class Simulation implements SimulationInternals, SimulationInterface {

    public SimulationConfig simulationConfig;
    public BaseEnvironment environment;
    private RandomNoGenerator random;
    private long time = 0;
    private int mNextAgentId = 0;
    public MutatorListener mutatorListener = new MutatorListener();
    private volatile List<BaseAgent> mAgents;

    public Simulation() {
        environment = new BaseEnvironment(this);
        mAgents = new LinkedList<>();
    }

    private ReproductionMutator reproductionMutator;
    private ConsumptionMutator consumptionMutator;
    private ExchangeMutator exchangeMutator;
    private TransformationMutator transformationMutator;

    @Override
    public long getTime() {
        return time;
    }

    public void resetTime() {
        time = 0;
    }

    @Override
    public RandomNoGenerator getRandom() {
        return random;
    }

    @Override
    public Topology getTopology() {
        return environment != null ? environment.topology : null;
    }

    private Map<String, StateParameter> aiStateMap = new LinkedHashMap<String, StateParameter>();

    public synchronized Agent addAgent(Location location, int agentType) {
        if (environment.isOccupied(location)) return null;
        else return spawnAgent(location, agentType);
    }

    private List<StatePlugin> aiStatePlugins = new LinkedList<StatePlugin>();

    @Override
    public void registerAgent(BaseAgent agent) {
        mAgents.add(agent);
        agent.setId(mNextAgentId++);
    }

    public void loadNewAgents() {
        for (int i = 0; i < environment.agentParams.length; i++) {
            for (int j = 0; j < environment.agentParams[i].initialAgents; j++) {
                Location location;
                int tries = 0;
                do location = getTopology().getRandomLocation();
                while (tries++ < 100 & environment.isOccupied(location));
                if (tries < 100) spawnAgent(location, i);
            }
        }
    }

    @Override
    public int getAgentTypeCount() {
        return simulationConfig.getAgentTypes();
    }

    @Override
    public BaseAgent newAgent(int type) {
        return new Agent(this, type);
    }

    public void load(SimulationConfig simConfig) {
        random = simConfig.randomSeed == 0 ? new RandomNoGenerator() : new RandomNoGenerator(simConfig.randomSeed);
        simulationConfig = simConfig;
        environment.setParams(simConfig.envParams, simConfig.agentParams, simulationConfig.keepOldAgents);

        if (!simConfig.isContinuation()) {
            aiStatePlugins.clear();
            mutatorListener.clearMutators();
            mAgents.clear();
            mNextAgentId = 0;
            reproductionMutator = null;
            consumptionMutator = null;
            exchangeMutator = null;
            transformationMutator = null;
        }

        // TODO: ? time = 0;
        mutatorListener.clearMutators(); //TODO: IMPORTANT!
        if (reproductionMutator == null) {
            reproductionMutator = new ReproductionMutator();
            mutatorListener.addMutator(reproductionMutator);
        }
        if (consumptionMutator == null) {
            consumptionMutator = new ConsumptionMutator();
            mutatorListener.addMutator(consumptionMutator);
        }
        if (exchangeMutator == null) {
            exchangeMutator = new ExchangeMutator();
            mutatorListener.addMutator(exchangeMutator);
        }
        if (transformationMutator == null) {
            transformationMutator = new TransformationMutator();
            mutatorListener.addMutator(transformationMutator);
        }

        reproductionMutator.setParams(this, simConfig.reproductionParams, simConfig.getAgentTypes());
        consumptionMutator.setParams(this, simConfig.consumptionParams, simConfig.getAgentTypes());
        exchangeMutator.setParams(this, simConfig.exchangeParams, simConfig.getAgentTypes());
        transformationMutator.setParams(this, simConfig.transformationParams, simConfig.getAgentTypes());

        if (simConfig.spawnNewAgents) loadNewAgents();
    }

    private static final AtomicLong ticks = new AtomicLong();
    @Override
    public void step() {
        environment.update();
        synchronized (ticks) {
            for (BaseAgent agent : new LinkedList<>(mAgents)) {
                System.out.println("Update 1 - " + agent.id());
                agent.update();
                System.out.println("Update 2 - " + agent.id());
                mutatorListener.onUpdate(agent);
                if (!agent.isAlive()) mAgents.remove(agent);
                System.out.println("Update 3 - " + agent.id());
            }
        }
        time++;
        ticks.set(time);
    }

    public Agent spawnAgent(Location location, int agentType) {
        Agent agent = (Agent) newAgent(agentType);
        agent.init(environment, new LocationDirection(location, getTopology().getRandomDirection()),
                environment.agentParams[agentType], simulationConfig.controllerParams.createController(this, agentType));
        return agent;
    }

    @Override
    public StateParameter getStateParameter(String name) {
        return aiStateMap.get(name);
    }

    private void setupAIStatePlugins() {
        aiStateMap.clear();
        for (StatePlugin plugin : aiStatePlugins) {
            for (StateParameter param : plugin.getParameters()) {
                aiStateMap.put(param.getName(), param);
            }
        }
    }

    @Override
    public AgentSimilarityCalculator getSimilarityCalculator() {
        return null;
    }

    @Override
    public Set<String> getStatePluginKeys() {
        return aiStateMap.keySet();
    }

    @Override
    public AgentListener getAgentListener() {
        return mutatorListener;
    }

    /**
     * Checks whether given AgentState can be used in the current simulation configuration
     *
     * @param type  specific Class of AgentState
     * @param value value of AgentState
     * @return true if AgentState supported in current configuration
     */
    public <T extends AgentState> boolean supportsState(Class<T> type, T value) {
        return mutatorListener.supportsState(type, value);
    }

    public List<BaseAgent> getAgents() {
        return mAgents;
    }
}
