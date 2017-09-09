package cobweb3d.model.agent

import cobweb3d.model.Updatable
import cobweb3d.model.entity.Cause
import cobweb3d.model.location.LocationDirection

/**
 * Basic properties of an agent
 */
abstract class Agent(val type: Int) : Updatable {

    var id: Int = 0

    var isAlive = true // Initializer required, not a nullable type.
        private set // The setter is private and has the default implementation.

    /**
     * @return the location this agent occupies.
     */
    var position: LocationDirection? = null
        protected set

    /**
     * Energy the agent can use to do things and can gain doing other things
     */
    var energy: Int = 0
        private set

    fun die() {
        assert(isAlive)
        if (!isAlive)
            return

        isAlive = false
    }

    fun enoughEnergy(required: Int): Boolean {
        return energy >= required
    }

    /**
     * Changes the agent's energy level.
     * @param delta Energy change delta, positive means agent gains energy, negative means it loses
     * @param cause Why the energy changed.
     */
    fun changeEnergy(delta: Int, cause: Cause) {
        energy += delta
    }

    protected abstract fun createChildAsexual(location: LocationDirection): Agent

    override fun update() {}
}
