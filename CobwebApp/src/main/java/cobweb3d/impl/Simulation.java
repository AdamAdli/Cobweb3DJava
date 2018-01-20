package cobweb3d.impl;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.Agent;
import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.AgentSimilarityCalculator;
import cobweb3d.core.agent.ControllerInput;
import cobweb3d.core.entity.Cause;
import cobweb3d.core.environment.Environment;
import cobweb3d.core.environment.Topology;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.agent.BaseAgent;
import cobweb3d.impl.ai.SimpleController;
import cobweb3d.plugins.StateParameter;
import cobweb3d.ui.SimulationInterface;
import util.RandomNoGenerator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Simulation implements SimulationInternals, SimulationInterface {

    public SimulationConfig simulationConfig;
    public Environment environment;
    private RandomNoGenerator random;
    private long time = 0;
    private int mNextAgentId = 0;
    private List<Agent> mAgents;

    public Simulation() {
        environment = new Environment(this);
        mAgents = new LinkedList<>();
    }

    public void load(SimulationConfig simConfig) {
        simulationConfig = simConfig;
        environment.setParams(simConfig.envParams, simConfig.agentParams);
        BaseAgent baseAgent = new BaseAgent(this, 0);
        baseAgent.init(environment, new LocationDirection(new Location(3, 3, 3), Direction.zPos), environment.agentParams[0], new SimpleController());
        BaseAgent baseAgent2 = new BaseAgent(this, 0);
        baseAgent2.init(environment, new LocationDirection(new Location(0, 4, 0), Direction.yNeg), environment.agentParams[0], new SimpleController());
        environment.setAgent(baseAgent.position, baseAgent);
        environment.setAgent(baseAgent2.position, baseAgent2);
        random = simConfig.randomSeed == 0 ? new RandomNoGenerator() : new RandomNoGenerator(simConfig.randomSeed);
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
            for (Agent agent : mAgents) {
                agent.update();

                if (!agent.isAlive()) mAgents.remove(agent);
            }
        }
        time++;
    }

    public void addAgent(Agent agent) {
        mAgents.add(agent);
        agent.setId(mNextAgentId++);
    }

    @Override
    public int getAgentTypeCount() {
        return 0;
    }

    @Override
    public Agent newAgent(int type) {
        return null;
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
            public void onContact(Agent bumper, Agent bumpee) {

            }

            @Override
            public void onStep(Agent agent, LocationDirection from, LocationDirection to) {

            }

            @Override
            public void onSpawn(Agent agent, Agent parent1, Agent parent2) {

            }

            @Override
            public void onSpawn(Agent agent, Agent parent) {

            }

            @Override
            public void onSpawn(Agent agent) {

            }

            @Override
            public void onDeath(Agent agent) {

            }

            @Override
            public void onConsumeFood(Agent agent, int foodType) {

            }

            @Override
            public void onConsumeAgent(Agent agent, Agent food) {

            }

            @Override
            public void onEnergyChange(Agent agent, int delta, Cause cause) {

            }

            @Override
            public void onUpdate(Agent agent) {

            }

            @Override
            public void beforeControl(Agent agent, ControllerInput cInput) {

            }
        };
    }

    @Override
    public Collection<String> getStatePluginKeys() {
        return null;
    }
}
