package cobweb3d.plugins.transform;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.exchange.ExchangeState;
import cobweb3d.plugins.mutators.LoggingMutator;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.UpdateMutator;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class TransformationMutator extends StatefulMutatorBase<TransformationState> implements UpdateMutator,
        LoggingMutator {
    TransformationParams params;
    int i = 0;
    private SimulationTimeSpace simulation;

    public TransformationMutator() {
        super(TransformationState.class);
    }

    public void setParams(SimulationTimeSpace sim, TransformationParams transformationParams, int agentTypes) {
        this.simulation = sim;
        this.params = transformationParams;
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
        header.add("Transformations");
        return header;
    }

    @Override
    public Collection<String> logHeaderTotal() {
        List<String> header = new LinkedList<String>();
        header.add("Transformations");
        return header;
    }

    @Override
    protected boolean validState(TransformationState value) {
        return value != null;
    }

    @Override
    public void onUpdate(BaseAgent agent) {
        if (params.of(agent).enabled && getX(agent) >= params.of(agent).transformationX.getValue() && i >= 30) {
            setAgentState(agent, new TransformationState(getAgentState(agent), agent.getType()));
            agent.transformType(params.of(agent).destType);
            if (agent instanceof Agent) {
                ((Agent) agent).setParams(((Simulation) simulation).environment.agentParams[agent.getType()]);
            }
        }
        i++;
    }

    private float getX(BaseAgent agent) {
        if (agent instanceof Agent) {
            ExchangeState exchangeState = ((Agent) agent).getState(ExchangeState.class);
            if (exchangeState != null) {
                return exchangeState.x;
            }
        }
        return 0;
    }
}
