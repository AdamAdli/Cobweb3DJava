package cobweb3d.plugins;

import cobweb3d.core.agent.Agent;

public interface StateParameter {
    String getName();

    double getValue(Agent agent);
}
