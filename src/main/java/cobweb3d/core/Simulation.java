package cobweb3d.core;

import cobweb3d.model.agent.Agent;
import cobweb3d.model.environment.Environment;

import java.util.LinkedList;
import java.util.List;

public class Simulation implements SimulationInterface {

    private long mTime = 0;

    public Environment environment;
    private List<Agent> mAgents = new LinkedList<>();
    private int mNextAgentId = 0;

    public Simulation() {
        environment = new Environment();
    }

    @Override
    public void step() {
        for (Agent agent : mAgents) {
            agent.update();

            if (!agent.isAlive()) mAgents.remove(agent);
        }

        mTime++;
    }

    @Override
    public long getTime() {
        return mTime;
    }

    public void resetTime() {
        mTime = 0;
    }

    public void addAgent(Agent agent) {
        mAgents.add(agent);
        agent.id = mNextAgentId++;
    }

    @Override
    public int getAgentTypeCount() {
        return 0;
    }
}
