package cobweb3d.plugins.transform;

import cobweb3d.core.SimulationTimeSpace;
import cobweb3d.core.agent.BaseAgent;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.agent.Agent;
import cobweb3d.plugins.exchange.ExchangeState;
import cobweb3d.plugins.mutators.StatefulMutatorBase;
import cobweb3d.plugins.mutators.UpdateMutator;

public class TransformationMutator extends StatefulMutatorBase<TransformationState, TransformationParams> implements UpdateMutator {
    TransformationParams params;
    private SimulationTimeSpace simulation;

    public TransformationMutator() {
        super(TransformationState.class);
    }

    @Override
    public void setParams(SimulationTimeSpace sim, TransformationParams transformationParams, int agentTypes) {
        this.simulation = sim;
        this.params = transformationParams;
    }

    @Override
    public boolean acceptsParam(Class<?> object) {
        return object.isAssignableFrom(TransformationParams.class);
    }

    @Override
    protected boolean validState(TransformationState value) {
        return value != null;
    }

    @Override
    public void onUpdate(BaseAgent agent) {
        if (params.of(agent).enabled && getX(agent) >= params.of(agent).transformationX.getValue()) {
            setAgentState(agent, new TransformationState(getAgentState(agent), agent.getType()));
            agent.transformType(params.of(agent).destType - 1);
            if (agent instanceof Agent) {
                ((Agent) agent).setParams(((Simulation) simulation).environment.agentParams[agent.getType()]);
            }
        }
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
