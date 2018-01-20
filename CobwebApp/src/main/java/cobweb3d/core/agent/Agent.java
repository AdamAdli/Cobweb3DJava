package cobweb3d.core.agent;


import cobweb3d.core.Updatable;
import cobweb3d.core.location.LocationDirection;

/**
 * Basic properties of an Agent
 */
public abstract class Agent implements Updatable {
    public LocationDirection position;
    private Integer id;
    private int type;
    private boolean alive = true;
    private int energy;

    public Agent(int type) {
        this.type = type;
    }

    public int id() {
        return id;
    }

    public void setId(Integer id) {
        if (this.id != null) throw new IllegalStateException("Agent id is already set for " + this.toString() + "!");
        else this.id = id;
    }

    public void die() {
        assert (isAlive());
        if (!isAlive())
            return;
        alive = false;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean enoughEnergy(int required) {
        return getEnergy() >= required;
    }

    /**
     * Changes the agent's energy level.
     *
     * @param delta Energy change delta, positive means agent gains energy, negative means it loses
     * @param cause Why the energy changed.
     */
    public void changeEnergy(int delta) {
        energy += delta;
    }

    /**
     * @return the location this Agent occupies.
     */
    public LocationDirection getPosition() {
        return position;
    }

    public boolean isAlive() {
        return alive;
    }

    public int getType() {
        return type;
    }

    protected abstract Agent createChildAsexual(LocationDirection location);
}
