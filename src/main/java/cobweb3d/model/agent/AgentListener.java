package cobweb3d.model.agent;

import cobweb3d.model.entity.Cause;
import cobweb3d.model.location.LocationDirection;

public interface AgentListener extends ControllerListener {

    void onContact(Agent bumper, Agent bumpee);

    void onStep(Agent agent, LocationDirection from, LocationDirection to);

    void onSpawn(Agent agent, Agent parent1, Agent parent2);

    void onSpawn(Agent agent, Agent parent);

    void onSpawn(Agent agent);

    void onDeath(Agent agent);

    void onConsumeFood(Agent agent, int foodType);

    void onConsumeAgent(Agent agent, Agent food);

    void onEnergyChange(Agent agent, int delta, Cause cause);

    void onUpdate(Agent agent);
}
