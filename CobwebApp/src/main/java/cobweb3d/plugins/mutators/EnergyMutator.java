package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;
import cobweb3d.core.entity.Cause;

public interface EnergyMutator extends AgentMutator {

    void onEnergyChange(Agent agent, int delta, Cause cause);
}
