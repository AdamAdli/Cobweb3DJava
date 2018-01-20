package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;

public interface UpdateMutator extends AgentMutator {

    void onUpdate(Agent agent);
}
