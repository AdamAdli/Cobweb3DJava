package cobweb3d.plugins.mutators;

import cobweb3d.core.agent.Agent;

/**
 * Modifies agents when they are born.
 * Note: the agent's location is initialized AFTER spawn and removed BEFORE death!
 * Use StepMutator if you want initial and final locations
 */
public interface SpawnMutator extends AgentMutator {

    /**
     * Agent died.
     *
     * @param agent Agent that died.
     */
    void onDeath(Agent agent);

    /**
     * Agent spawned by user.
     *
     * @param agent Agent spawned.
     */
    void onSpawn(Agent agent);

    /**
     * Agent produced asexually
     *
     * @param agent  Agent produced.
     * @param parent Asexual parent.
     */
    void onSpawn(Agent agent, Agent parent);

    /**
     * Agent produced sexually.
     *
     * @param agent   Agent produced.
     * @param parent1 First parent.
     * @param parent2 Second parent.
     */
    void onSpawn(Agent agent, Agent parent1, Agent parent2);
}
