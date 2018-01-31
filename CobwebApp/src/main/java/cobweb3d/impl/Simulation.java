package cobweb3d.impl;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.AgentSimilarityCalculator;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.ControllerInput;
import cobweb3d.core.entity.Cause;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.Agent;
import cobweb3d.impl.ai.SimpleController;
import cobweb3d.plugins.StateParameter;
import cobweb3d.ui.SimulationInterface;
import util.RandomNoGenerator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements SimulationInternals, SimulationInterface {

    public SimulationConfig simulationConfig;
    public BaseEnvironment environment;
    private RandomNoGenerator random;
    private long time = 0;
    private int mNextAgentId = 0;
    private List<BaseAgent> mAgents;

    public Simulation() {
        environment = new BaseEnvironment(this);
        mAgents = new LinkedList<>();
    }

    public void load(SimulationConfig simConfig) {
        random = simConfig.randomSeed == 0 ? new RandomNoGenerator() : new RandomNoGenerator(simConfig.randomSeed);
        simulationConfig = simConfig;
        environment.setParams(simConfig.envParams, simConfig.agentParams, false);
        loadNewAgents();
    }

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

    @Override
    public void step() {
        environment.update();
        synchronized (environment) {
            for (BaseAgent agent : new LinkedList<>(mAgents)) {
                agent.update();

                if (!agent.isAlive()) mAgents.remove(agent);
            }
        }
        time++;
    }

    public synchronized Agent addAgent(Location location, int agentType) {
        if (environment.isOccupied(location)) return null;
        else return spawnAgent(location, agentType);
    }

    public Agent spawnAgent(Location location, int agentType) {
        Agent agent = (Agent) newAgent(agentType);
        agent.init(environment, new LocationDirection(location, getTopology().getRandomDirection()),
                environment.agentParams[agentType], new SimpleController());
        return agent;
    }

    @Override
    public void registerAgent(BaseAgent agent) {
        mAgents.add(agent);
        agent.setId(mNextAgentId++);
    }

    public void loadNewAgents() {
        mAgents.clear(); // TODO: clear renderer..
        mNextAgentId = 0;
        environment.clearAgents();
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
        return 0;
    }

    @Override
    public BaseAgent newAgent(int type) {
        return new Agent(this, type);
    }

    @Override
    public StateParameter getStateParameter(String name) {
        return null;
    }

    @Override
    public AgentSimilarityCalculator getSimilarityCalculator() {
        return null;
    }

    @Override
    public AgentListener getAgentListener() {
        return new AgentListener() {
            @Override
            public void onContact(BaseAgent bumper, BaseAgent bumpee) {

            }

            @Override
            public void onStep(BaseAgent agent, LocationDirection from, LocationDirection to) {

            }

            @Override
            public void onSpawn(BaseAgent agent, BaseAgent parent1, BaseAgent parent2) {

            }

            @Override
            public void onSpawn(BaseAgent agent, BaseAgent parent) {

            }

            @Override
            public void onSpawn(BaseAgent agent) {

            }

            @Override
            public void onDeath(BaseAgent agent) {

            }

            @Override
            public void onConsumeFood(BaseAgent agent, int foodType) {

            }

            @Override
            public void onConsumeAgent(BaseAgent agent, BaseAgent food) {

            }

            @Override
            public void onEnergyChange(BaseAgent agent, int delta, Cause cause) {

            }

            @Override
            public void onUpdate(BaseAgent agent) {

            }

            @Override
            public void beforeControl(BaseAgent agent, ControllerInput cInput) {

            }
        };
    }

    @Override
    public Collection<String> getStatePluginKeys() {
        return null;
    }
}
