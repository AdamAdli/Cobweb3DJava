package cobweb3d.impl.params;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.PerAgentParams;

public class AgentParams extends PerAgentParams<BaseAgentParams> {

    private static final long serialVersionUID = 2L;
    private AgentFoodCountable size;

    public AgentParams(AgentFoodCountable size) {
        super(BaseAgentParams.class);
        this.size = size;
        resize(size);
    }

    @Override
    protected BaseAgentParams newAgentParam() {
        return new BaseAgentParams();
    }

    @Override
    public void resize(AgentFoodCountable envParams) {
        size = envParams;
        super.resize(size);

        for (BaseAgentParams complexAgentParams : agentParams) {
            complexAgentParams.resize(size);
        }
    }
}
