package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.BaseAgent;

public interface ConsumptionMutator extends AgentMutator {

    void onConsumeAgent(BaseAgent agent, BaseAgent food);

    void onConsumeFood(BaseAgent agent, int foodType);
}
