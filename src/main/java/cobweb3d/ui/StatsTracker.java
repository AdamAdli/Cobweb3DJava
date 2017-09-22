package cobweb3d.ui;

import cobweb3d.Simulation;
import cobweb3d.model.agent.Agent;
import org.cobweb.cobweb2.Simulation;
import org.cobweb.cobweb2.core.Environment;
import org.cobweb.cobweb2.core.agent.Agent;
import org.cobweb.cobweb2.core.location.Location;
import org.cobweb.cobweb2.impl.ComplexAgent;
import org.cobweb.cobweb2.plugins.pd.PDState;

import java.util.List;

public class StatsTracker {

	protected Simulation simulation;

	public StatsTracker(Simulation simulation) {
		this.simulation = simulation;
	}

	public long countAgentEnergy() {
		long totalEnergy = 0;
		for(Agent a : simulation.theEnvironment.getAgents()){
			ComplexAgent agent = (ComplexAgent) a;
			totalEnergy += agent.getEnergy();
		}
		return totalEnergy;
	}

	public long getAgentCount() {
		return simulation.theEnvironment.getAgentCount();
	}

	public long countAgents(int agentType) {
		long count = 0;
		for(Agent a : simulation.theEnvironment.getAgents()) {
			if (a.getType() == agentType)
				count++;
		}
		return count;
	}

	public long countAgentEnergy(int agentType) {
		long totalEnergy = 0;
		for(Agent a : simulation.theEnvironment.getAgents()) {
			ComplexAgent agent = (ComplexAgent) a;
			if (agent.getType() == agentType)
				totalEnergy += agent.getEnergy();
		}
		return totalEnergy;
	}

	public static class CoopCheaterCount {
		public int cooperators = 0;
		public int cheaters = 0;
	}

	protected void tallyPD(CoopCheaterCount coopCheaterCount, Agent a) {
		PDState pdState = ((ComplexAgent)a).getState(PDState.class);
		if (pdState == null)
			return;

		if (pdState.pdCheater) {
			coopCheaterCount.cheaters++;
		} else {
			coopCheaterCount.cooperators++;
		}
	}

	public CoopCheaterCount numAgentsStrat() {
		CoopCheaterCount coopCheaterCount= new CoopCheaterCount();
		for(Agent a : simulation.theEnvironment.getAgents()) {
			tallyPD(coopCheaterCount, a);
		}
		return coopCheaterCount;
	}

	public CoopCheaterCount numAgentsStrat(int agentType) {
		CoopCheaterCount coopCheaterCount= new CoopCheaterCount();
		for(Agent a : simulation.theEnvironment.getAgents()) {
			if (a.getType() == agentType)
				tallyPD(coopCheaterCount, a);
		}
		return coopCheaterCount;
	}

	public int getAgentTypeCount() {
		return simulation.getAgentTypeCount();
	}

	public long getTime() {
		return simulation.getTime();
	}

	public long countFoodTiles() {
		Environment e = simulation.theEnvironment;
		long foodCount = 0;
		for (int x = 0; x < e.topology.width; ++x) {
			for (int y = 0; y < e.topology.height; ++y) {
				Location currentPos = new Location(x, y);
				if (e.hasFood(currentPos))
					++foodCount;
			}
		}
		return foodCount;
	}

	public int countFoodTiles(int foodType) {
		Environment e = simulation.theEnvironment;
		int foodCount = 0;
		for (int x = 0; x < e.topology.width; ++x) {
			for (int y = 0; y < e.topology.height; ++y) {
				Location currentPos = new Location(x, y);
				if (e.hasFood(currentPos))
					if (e.getFoodType(currentPos) == foodType)
						++foodCount;
			}
		}
		return foodCount;
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

}
