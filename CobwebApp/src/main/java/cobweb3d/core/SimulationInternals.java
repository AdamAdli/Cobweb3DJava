package cobweb3d.core;

import cobweb3d.core.agent.AgentListener;
import cobweb3d.core.agent.AgentSimilarityCalculator;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.plugins.StateParameter;
import cobweb3d.plugins.StatePluginSource;

/**
 * Methods that only simulation components need access to.
 * UI and other external components should only use SimulationInterface!
 */
public interface SimulationInternals extends StatePluginSource, SimulationTimeSpace {
    BaseAgent newAgent(int type);

    void registerAgent(BaseAgent agent);

    StateParameter getStateParameter(String name);

    AgentSimilarityCalculator getSimilarityCalculator();

    AgentListener getAgentListener();
}
