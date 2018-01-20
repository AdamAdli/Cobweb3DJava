package cobweb3d.impl.agent;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.environment.BaseEnvironment;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.params.AgentParams;

public class Agent extends BaseAgent {
    public AgentParams params;

    protected transient SimulationInternals simulation;
    protected transient BaseEnvironment environment;

    private Controller controller;

    private long birthTick;
    private long lastAsexTime = 0;

    public Agent(SimulationInternals simulation, int type) {
        super(type);
        this.simulation = simulation;
        birthTick = simulation.getTime();
    }

    public void setParams(AgentParams agentParams) {
        this.params = agentParams;
    }

    public long getAge() {
        return simulation.getTime() - birthTick;
    }

    @Override
    protected BaseAgent createChildAsexual(LocationDirection location) {
        return null;
    }

   /* public boolean tryReproduceAsex() {
        if ((simulation.getTime() - lastAsexTime) < 0) return false;
        else {
            createChildAsexual()
        }
    } */

    @Override
    public void update() {
        if (!isAlive()) return;
        if (params.agingMode && getAge() >= params.agingLimit.getValue()) {
            die();
            return;
        }
        if (controller != null) controller.controlAgent(this, simulation.getAgentListener());
    }

    /**
     * Constructor with no parent agent; creates an agent using "immaculate conception" technique
     *
     * @param pos         spawn position
     * @param agentParams agent parameters
     */
    public void init(BaseEnvironment env, LocationDirection pos, AgentParams agentParams, Controller controller) {
        environment = env;
        setParams(agentParams);
        this.controller = controller;
        simulation.getAgentListener().onSpawn(this);

        initPosition(pos);

        changeEnergy(params.initEnergy.getValue());
    }

    private void initPosition(LocationDirection pos) {
        if (pos.direction.equals(Direction.NONE))
            pos = new LocationDirection(pos, simulation.getTopology().getRandomDirection());
        move(pos);
        simulation.registerAgent(this);
    }

    public void move(LocationDirection newPos) {
        LocationDirection oldPos = getPosition();
        if (oldPos != null) environment.setAgent(oldPos, null);
        if (newPos != null) environment.setAgent(newPos, this);
        simulation.getAgentListener().onStep(this, oldPos, newPos);
        position = newPos;
    }

    public void turnLeft() {
        position = environment.topology.getTurnLeftPosition(getPosition());//environment.topology.getTurnLeftPosition(getPosition());
    }

    public void turnRight() {
        position = environment.topology.getTurnRightPosition(getPosition());//environment.topology.getTurnRightPosition(getPosition());
    }

    public void turnUp() {
        position = environment.topology.getTurnUpPosition(getPosition());//environment.topology.getTurnUpPosition(getPosition());
    }

    public void turnDown() {
        position = environment.topology.getTurnDownPosition(getPosition());//
    }

    public void step() {
        LocationDirection destPos = environment.topology.getAdjacent(getPosition());
        if (!destPos.equals(getPosition())) {
            if (canStep(destPos)) {
                onStepFreeTile(destPos);
            } else if (environment.hasAgent(destPos)) {
                onStepAgentBump(environment.getAgent(destPos));
            } else {
                // Non-free tile rock, waste, etc.
            }
        }
    }

    private void onStepFreeTile(LocationDirection destPos) {
        move(destPos);
    }

    private void onStepAgentBump(BaseAgent otherAgent) {
        simulation.getAgentListener().onContact(this, otherAgent);
        changeEnergy(-params.stepAgentEnergy.getValue());
        if (canEat(otherAgent)) eat(otherAgent);
        if (!otherAgent.isAlive()) return;

        // If agents are the same type, try to breed.
        if (otherAgent instanceof Agent && otherAgent.getType() == getType()) {
            // TODO:
        }
    }

    private boolean canStep(Location dest) {
        return !environment.hasAgent(dest);
    }

    private boolean canEat(BaseAgent otherAgent) {
        return true;
    }

    private void eat(BaseAgent otherAgent) {
        int gain = otherAgent.getEnergy(); // TODO: later multiple by factor ?)
        changeEnergy(gain);
        simulation.getAgentListener().onConsumeAgent(this, otherAgent);
        otherAgent.die();
    }
}
