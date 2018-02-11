package cobweb3d.plugins.food;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.mutators.ContactMutator;
import cobweb3d.plugins.mutators.LoggingMutator;
import cobweb3d.plugins.states.AgentState;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ConsumptionMutator implements ContactMutator, LoggingMutator {

    ConsumptionParams params;

    private SimulationTimeSpace simulation;

    public ConsumptionMutator() {
    }

    public void setParams(SimulationTimeSpace sim, ConsumptionParams reproductionParams, int agentTypes) {
        this.simulation = sim;
        this.params = reproductionParams;
    }

    @Override
    public Collection<String> logDataAgent(int agentType) {
        List<String> l = new LinkedList<String>();
        // l.add(Integer.toString(birthCounts[agentType]));
        return l;
    }

    @Override
    public Collection<String> logDataTotal() {
        List<String> l = new LinkedList<String>();
        int sum = 0;
        l.add(Integer.toString(sum));
        return l;
    }

    @Override
    public Collection<String> logHeadersAgent() {
        List<String> header = new LinkedList<String>();
        header.add("Consumptions");
        return header;
    }

    @Override
    public Collection<String> logHeaderTotal() {
        List<String> header = new LinkedList<String>();
        header.add("Consumptions");
        return header;
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }

    private boolean canEat(BaseAgent eater, BaseAgent food) {
        if (eater == null || food == null) return false;
        else return params.of(eater).canEat[food.getType()];
    }

    private void eat(BaseAgent eater, BaseAgent food) {
        int gain = food.getEnergy(); // TODO: later multiple by factor ?)
        eater.changeEnergy((int) (gain * params.of(eater).energyMultipler[food.getType()]), new Agent.EatAgentCause());
        // TODO: figure out a way for plugins to broadcast events for other plugins to listen to.
        // simulation.getAgentListener().onConsumeAgent(this, otherAgent);
        food.die();
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        if (canEat(bumper, bumpee)) eat(bumper, bumpee);
    }
}
