package cobweb3d.impl.environment;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.params.BaseEnvironmentParams;
import cobweb3d.impl.agent.Agent;
import cobweb3d.impl.params.AgentParams;
import cobweb3d.impl.params.BaseAgentParams;
import cobweb3d.plugins.mutators.EnvironmentMutator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class Environment extends BaseEnvironment {
    public AgentParams[] agentParams;
    private Map<Class<? extends EnvironmentMutator>, EnvironmentMutator> plugins = new LinkedHashMap<>();

    public Environment(SimulationInternals simulation) {
        super(simulation);
    }

    public synchronized void setParams(BaseEnvironmentParams envParams, BaseAgentParams agentParams, boolean keepOldAgents) throws IllegalArgumentException {
        this.agentParams = agentParams.agentParams;
        super.setParams(envParams, keepOldAgents);
        if (keepOldAgents) {
            killOffgridAgents();
            loadOldAgents();
            // TODO: Load old agents, set them in the flagArray.
        } else agentTable.clear();
    }

    @Override
    public synchronized void setParams(BaseEnvironmentParams envParams, boolean keepOldAgents) throws IllegalArgumentException {
        throw new RuntimeException("Environment requires agent params!");
    }

    public void loadNew() {
        for (EnvironmentMutator plugin : plugins.values()) {
            plugin.loadNew();
        }
    }

    @Override
    public void update() {
        super.update();

        /*
        TODO: Plugins for environment?
        for (Updatable plugin : plugins.values()) {
            plugin.update();
        }         */
    }

    public <T extends EnvironmentMutator> void addPlugin(T plugin) {
        plugins.put(plugin.getClass(), plugin);
    }

    @SuppressWarnings("unchecked")
    public <T extends EnvironmentMutator> T getPlugin(Class<T> type) {
        return (T) plugins.get(type);
    }

    /**
     * Searches through each location to find every old agent.  Each agent that is found
     * is added to the scheduler if the scheduler is new.  Agents that are off the new
     * environment are removed from the environment.
     */
    private void loadOldAgents() {
        // Add in-bounds old agents to the new scheduler and update new
        // constants
        // TODO: a way to keep old parameters for old agents?
        for (int x = 0; x < topology.width; ++x) {
            for (int y = 0; y < topology.height; ++y) {
                for (int z = 0; z < topology.depth; ++z) {
                    Location currentPos = new Location(x, y, z);
                    BaseAgent agent = getAgent(currentPos);
                    if (agent instanceof Agent) ((Agent) agent).setParams(agentParams[agent.getType()]);
                }
            }
        }
    }

    public Collection<Location> getEmptyNearLocations(Location position) {
        Collection<Location> result = new ArrayList<>(8);
        Direction direction = new Direction(0, 0, 0);
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    Location location = topology.getAdjacent(position, (Direction) direction.set(x, y, z));
                    if (location != null && testFlag(location, (byte) 0)) {
                        result.add(location);
                    }
                }
            }
        }
        return result;
    }
}
