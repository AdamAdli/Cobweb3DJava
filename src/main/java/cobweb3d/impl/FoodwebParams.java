/**
 *
 */
package cobweb3d.impl;

import cobweb3d.model.agent.AgentFoodCountable;
import cobweb3d.plugins.ResizableParam;
import io.ConfDisplayName;
import io.ConfList;
import io.ConfSquishParent;

import java.util.Arrays;

/**
 * Food web parameters for an agent.
 */
public class FoodwebParams implements ResizableParam {

	private static final long serialVersionUID = 1380425322335531943L;

	/**
	 * If this agent can eat Agent type 1 canEatAgents[0] is true.
	 */
	@ConfDisplayName("Agent")
	@ConfSquishParent
	@ConfList(indexName = "agent", startAtOne = true)
	public boolean[] canEatAgent = new boolean[0];

	/**
	 * If this agent can eat Food type 1 canEatFood[0] is true.
	 */
	@ConfDisplayName("Food")
	@ConfSquishParent
	@ConfList(indexName = "food", startAtOne = true)
	public boolean[] canEatFood = new boolean[0];

	public FoodwebParams(AgentFoodCountable env) {
		resize(env);
	}

	@Override
	public void resize(AgentFoodCountable envParams) {
		canEatAgent = Arrays.copyOf(canEatAgent, envParams.getAgentTypes());

		int oldSize = canEatFood.length;
		canEatFood  = Arrays.copyOf(canEatFood, envParams.getAgentTypes());
		// agents can eat all food by default
		for (int i = oldSize; i < canEatFood.length; i++) {
			canEatFood [i] = true;
		}
	}

}
