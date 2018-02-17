package cobweb3d.plugins.exchange;

import cobweb3d.plugins.states.AgentState;
import io.ConfXMLTag;

public class ExchangeState implements AgentState {

    @ConfXMLTag("x")
    public float x = 0;

    @ConfXMLTag("y")
    public float y = 0;

    @Deprecated // for reflection use only!

    public ExchangeState() {
    }

    public ExchangeState(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public ExchangeState(ExchangeAgentParams agentParams) {
        this(agentParams.initialX, agentParams.initialY);
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
