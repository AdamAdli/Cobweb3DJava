package cobweb3d.impl.environment;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.Updatable;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.params.BaseAgentParams;
import cobweb3d.core.params.BaseEnvironmentParams;
import cobweb3d.plugins.mutators.EnvironmentMutator;

import java.util.LinkedHashMap;
import java.util.Map;

public class Environment extends BaseEnvironment {

    private Map<Class<? extends EnvironmentMutator>, EnvironmentMutator> plugins = new LinkedHashMap<>();

    public Environment(SimulationInternals simulation) {
        super(simulation);
    }

    @Override
    public synchronized void setParams(BaseEnvironmentParams envParams, BaseAgentParams agentParams, boolean keepOldAgents) throws IllegalArgumentException {
        super.setParams(envParams, agentParams, keepOldAgents);
        // TODO any thing?
    }

    public void loadNew() {
        for (EnvironmentMutator plugin : plugins.values()) {
            plugin.loadNew();
        }
    }

    @Override
    public void update() {
        super.update();

        for (Updatable plugin : plugins.values()) {
            plugin.update();
        }
    }

    public <T extends EnvironmentMutator> void addPlugin(T plugin) {
        plugins.put(plugin.getClass(), plugin);
    }

    @SuppressWarnings("unchecked")
    public <T extends EnvironmentMutator> T getPlugin(Class<T> type) {
        return (T) plugins.get(type);
    }
}
