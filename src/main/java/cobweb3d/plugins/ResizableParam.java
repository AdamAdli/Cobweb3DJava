package cobweb3d.plugins;

import cobweb3d.model.agent.AgentFoodCountable;
import io.ParameterSerializable;

public interface ResizableParam extends ParameterSerializable {

	/**
	 * Updates configuration for the new number of agent types
	 * @param envParams used to retrieve agent type count
	 */
	void resize(AgentFoodCountable envParams);
}
