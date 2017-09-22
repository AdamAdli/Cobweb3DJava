package cobweb3d.core;


import cobweb3d.core.plugins.StateParameter;
import cobweb3d.core.plugins.StatePluginSource;
import cobweb3d.model.agent.Agent;
import cobweb3d.model.agent.AgentListener;
import cobweb3d.model.agent.AgentSimilarityCalculator;

/**
 * Methods that only simulation components need access to.
 * UI and other external components should only use SimulationInterface!
 */
public interface SimulationInternals extends StatePluginSource, SimulationTimeSpace {

	public Agent newAgent(int type);

	public void addAgent(Agent agent);

	public StateParameter getStateParameter(String name);

	public AgentSimilarityCalculator getSimilarityCalculator();

	public AgentListener getAgentListener();
}
