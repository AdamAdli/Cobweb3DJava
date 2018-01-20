package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;

/**
 * Modifies agents when one makes contact with another.
 */
public interface ContactMutator extends AgentMutator {

    /**
     * Event called when an agent makes contact with another.
     *
     * @param bumper Agent that moved to make contact.
     * @param bumpee Agent that got bumped into by the other.
     */
    void onContact(Agent bumper, Agent bumpee);
}
