package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;

public interface ConsumptionMutator extends AgentMutator {

    void onConsumeAgent(Agent agent, Agent food);

    void onConsumeFood(Agent agent, int foodType);
}
