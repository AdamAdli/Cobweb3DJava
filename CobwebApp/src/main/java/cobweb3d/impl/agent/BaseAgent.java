package cobweb3d.impl.agent;

import cobweb3d.core.SimulationInternals;
import cobweb3d.core.agent.Agent;
import cobweb3d.core.agent.Controller;
import cobweb3d.core.environment.Environment;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.impl.params.BaseAgentParams;

public class BaseAgent extends Agent {
    public BaseAgentParams params;

    protected transient SimulationInternals simulation;
    protected transient Environment environment;

    private Controller controller;

    private long birthTick;
    private long lastAsexTime = 0;

    public BaseAgent(SimulationInternals simulation, int type) {
        super(type);
        this.simulation = simulation;
        birthTick = simulation.getTime();
    }

    public void setParams(BaseAgentParams agentParams) {
        this.params = agentParams;
    }

    public long getAge() {
        return simulation.getTime() - birthTick;
    }

    @Override
    protected Agent createChildAsexual(LocationDirection location) {
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
    public void init(Environment env, LocationDirection pos, BaseAgentParams agentParams, Controller controller) {
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
        simulation.addAgent(this);
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
        if (canStep(destPos)) {
            onStepFreeTile(destPos);
        } else if (environment.hasAgent(destPos)) {
            onStepBump(environment.getAgent(destPos));
        } else {
            // Non-free tile rock, waste, etc.
        }
    }

    private void onStepFreeTile(LocationDirection destPos) {
        move(destPos);
    }

    private void onStepBump(Agent otherAgent) {

    }

    private boolean canStep(Location dest) {
        return !environment.hasAgent(dest);
    }
}
