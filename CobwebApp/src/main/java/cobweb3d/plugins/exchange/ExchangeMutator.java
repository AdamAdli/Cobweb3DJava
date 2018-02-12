package cobweb3d.plugins.exchange;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.core.location.Location;
import cobweb3d.plugins.mutators.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ExchangeMutator extends StatefulMutatorBase<ExchangeState> implements ContactMutator,
        StepMutator, LoggingMutator, SpawnMutator {
    ExchangeParams params;

    private SimulationTimeSpace simulation;

    public ExchangeMutator() {
        super(ExchangeState.class);
    }

    public void setParams(SimulationTimeSpace sim, ExchangeParams exchangeParams, int agentTypes) {
        this.simulation = sim;
        this.params = exchangeParams;
    }

    @Override
    public Collection<String> logDataAgent(int agentType) {
        List<String> l = new LinkedList<String>();

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
        header.add("Exchanges");
        return header;
    }

    @Override
    public Collection<String> logHeaderTotal() {
        List<String> header = new LinkedList<String>();
        header.add("Exchanges");
        return header;
    }

    private void tryExchange(BaseAgent agent, BaseAgent other) {
        ExchangeAgentPairParams pairParams = params.getPairParams(agent.getType(), other.getType());
        if (pairParams.quantXTransfer != 0 || pairParams.quantYTransfer != 0) {
            System.out.println("Exchange!");
            ExchangeState agentState = getAgentState(agent);
            ExchangeState otherState = getAgentState(other);
            System.out.println(params.of(agent).calculateU(agentState));
            System.out.println(params.of(agent).calculateU(otherState));
        }
    }

    @Override
    protected boolean validState(ExchangeState value) {
        return value != null;
    }

    @Override
    public void onSpawn(BaseAgent agent) {
        setAgentState(agent, new ExchangeState(params.of(agent)));
    }

    @Override
    public void onContact(BaseAgent bumper, BaseAgent bumpee) {
        tryExchange(bumper, bumpee);
    }

    @Override
    public void onStep(BaseAgent agent, Location from, Location to) {

    }
}
