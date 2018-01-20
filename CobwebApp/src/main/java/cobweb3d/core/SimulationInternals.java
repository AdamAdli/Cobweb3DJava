package cobweb3d.core;

import cobweb3d.core.agent.Agent;
import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.AgentSimilarityCalculator;
import cobweb3d.plugins.StateParameter;
import cobweb3d.plugins.StatePluginSource;

/**
 * Methods that only simulation components need access to.
 * UI and other external components should only use SimulationInterface!
 */
public interface SimulationInternals extends StatePluginSource, SimulationTimeSpace {
    Agent newAgent(int type);

    void addAgent(Agent agent);

    StateParameter getStateParameter(String name);

    AgentSimilarityCalculator getSimilarityCalculator();

    AgentListener getAgentListener();
}
