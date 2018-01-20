package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;
import cobweb3d.core.agent.ControllerInput;

public interface ControllerInputMutator extends AgentMutator {

    /**
     * Controller has controlled an agent.
     *
     * @param agent  Agent in question.
     * @param cInput controller input state
     */
    void onControl(Agent agent, ControllerInput cInput);
}
