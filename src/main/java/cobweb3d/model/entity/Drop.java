package cobweb3d.model.entity;

import cobweb3d.model.Updatable;
import cobweb3d.model.agent.Agent;

/**
 * Contains methods
 */
public interface Drop extends Updatable {

    boolean canStep(Agent agent);

    void onStep(Agent agent);

    void prepareRemove();
}
