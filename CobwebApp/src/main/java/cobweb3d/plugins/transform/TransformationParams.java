package cobweb3d.plugins.transform;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class TransformationParams extends PerAgentParams<TransformationAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public TransformationParams(AgentFoodCountable size) {
        super(TransformationAgentParams.class, size);
    }

    @Override
    protected TransformationAgentParams newAgentParam() {
        return new TransformationAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable newSize) {
        this.size = newSize;
        super.resize(newSize);
    }
}
