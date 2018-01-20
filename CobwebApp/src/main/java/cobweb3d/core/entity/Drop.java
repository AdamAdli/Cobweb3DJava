package cobweb3d.core.entity;

import cobweb3d.core.Updatable;
import cobweb3d.core.agent.Agent;

/**
 * Contains methods
 */
public interface Drop extends Updatable {

    boolean canStep(Agent agent);

    void onStep(Agent agent);

    void prepareRemove();
}
