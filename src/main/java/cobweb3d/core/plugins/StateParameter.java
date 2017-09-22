package cobweb3d.core.plugins;

import cobweb3d.model.agent.Agent;

public interface StateParameter {

	String getName();

	double getValue(Agent agent);
}
