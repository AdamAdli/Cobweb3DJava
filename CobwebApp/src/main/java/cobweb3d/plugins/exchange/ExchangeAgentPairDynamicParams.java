package cobweb3d.plugins.exchange;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;

public class ExchangeAgentPairDynamicParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Enabled")
    @ConfXMLTag("enabled")
    public boolean enabled = false;

    @ConfDisplayName("Lower bound")
    @ConfXMLTag("lowerBound")
    public int lowerBound = 0;

    @ConfDisplayName("Upper bound")
    @ConfXMLTag("upperBound")
    public int upperBound = 0;

    @ConfDisplayName("Increment")
    @ConfXMLTag("increment")
    public int increment = 0;

    //@Deprecated // for reflection use only!
    public ExchangeAgentPairDynamicParams() {
    }

    @Override
    public void resize(AgentFoodCountable size) {
        // TODO:
    }
}
