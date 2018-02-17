package cobweb3d.impl.stats.excel;

import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.Simulation;
import cobweb3d.plugins.mutators.ExcelLoggingMutator;

import java.util.List;
import java.util.Set;

public class BaseStatsProvider {
    protected Simulation simulation;

    public BaseStatsProvider(Simulation simulation) {
        this.simulation = simulation;
    }

    public long countAgentEnergy() {
        long totalEnergy = 0;
        for (BaseAgent a : simulation.environment.getAgents()) totalEnergy += a.getEnergy();
        return totalEnergy;
    }

    public long getAgentCount() {
        return simulation.environment.getAgentCount();
    }

    public long countAgents(int agentType) {
        long count = 0;
        for (BaseAgent a : simulation.environment.getAgents()) if (a.getType() == agentType) count++;
        return count;
    }

    public long countAgentEnergy(int agentType) {
        long totalEnergy = 0;
        for (BaseAgent a : simulation.environment.getAgents())
            if (a.getType() == agentType) totalEnergy += a.getEnergy();
        return totalEnergy;
    }

    public int getAgentTypeCount() {
        return simulation.getAgentTypeCount();
    }

    public long getTime() {
        return simulation.getTime();
    }

    public Set<ExcelLoggingMutator> getLoggingPlugins() {
        return simulation.mutatorListener.getLoggingMutators();
    }

    public List<String> pluginStatsHeaderAgent() {
        return simulation.mutatorListener.logHeaderAgent();
    }

    public List<String> pluginStatsHeaderTotal() {
        return simulation.mutatorListener.logHeaderTotal();
    }

    public List<String> pluginStatsTotal() {
        return simulation.mutatorListener.logDataTotal();
    }

    public List<String> pluginStatsAgent(int type) {
        return simulation.mutatorListener.logDataAgent(type);
    }

    public List<BaseAgent> getAgents() {
        return simulation.getAgents();
    }
}
