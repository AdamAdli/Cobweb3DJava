package cobweb3d.ui.stats;

import cobweb3d.impl.Simulation;

import java.util.List;

public class StatsTracker {

    protected Simulation simulation;

    public StatsTracker(Simulation simulation) {
        this.simulation = simulation;
    }

    public long countAgentEnergy() {
        long totalEnergy = 0;
		/*for(BaseAgent a : simulation.theEnvironment.getAgents()){
			ComplexAgent agent = (ComplexAgent) a;
			totalEnergy += agent.getEnergy();
		}*/
        return totalEnergy;
    }

    public long getAgentCount() {
        return 0;//simulation.theEnvironment.getAgentCount();
    }

    public long countAgents(int agentType) {
        long count = 0;
		/*for(BaseAgent a : simulation.theEnvironment.getAgents()) {
			if (a.getType() == agentType)
				count++;
		} */
        return count;
    }

    public long countAgentEnergy(int agentType) {
        long totalEnergy = 0;
	/*	for(BaseAgent a : simulation.theEnvironment.getAgents()) {
			ComplexAgent agent = (ComplexAgent) a;
			if (agent.getType() == agentType)
				totalEnergy += agent.getEnergy();
		} */
        return totalEnergy;
    }

    public int getAgentTypeCount() {
        return 0;//simulation.getAgentTypeCount();
    }

    public long getTime() {
        return 0;//simulation.getTime();
    }

    public long countFoodTiles() {
        // BaseEnvironment e = simulation.theEnvironment;
        long foodCount = 0;
		/* for (int x = 0; x < e.topology.width; ++x) {
			for (int y = 0; y < e.topology.height; ++y) {
				Location currentPos = new Location(x, y);
				if (e.hasFood(currentPos))
					++foodCount;
			}
		} */
        return foodCount;
    }

    public int countFoodTiles(int foodType) {
        // BaseEnvironment e = simulation.theEnvironment;
        int foodCount = 0;
		/* for (int x = 0; x < e.topology.width; ++x) {
			for (int y = 0; y < e.topology.height; ++y) {
				Location currentPos = new Location(x, y);
				if (e.hasFood(currentPos))
					if (e.getFoodType(currentPos) == foodType)
						++foodCount;
			}
		} */
        return foodCount;
    }

    public List<String> pluginStatsHeaderAgent() {
        return null;//simulation.mutatorListener.logHeaderAgent();
    }

    public List<String> pluginStatsHeaderTotal() {
        return null;//simulation.mutatorListener.logHeaderTotal();
    }

    public List<String> pluginStatsTotal() {
        return null;//simulation.mutatorListener.logDataTotal();
    }

    public List<String> pluginStatsAgent(int type) {
        return null;//simulation.mutatorListener.logDataAgent(type);
    }

    public static class CoopCheaterCount {
        public int cooperators = 0;
        public int cheaters = 0;
    }

}
