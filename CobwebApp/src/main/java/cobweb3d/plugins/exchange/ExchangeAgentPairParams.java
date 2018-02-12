package cobweb3d.plugins.exchange;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.core.params.ResizableParam;
import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;

public class ExchangeAgentPairParams implements ParameterSerializable, ResizableParam {
    private static final long serialVersionUID = 12L;

    @ConfXMLTag("type1")
    public int typeOne = 1;

    @ConfXMLTag("type2")
    public int typeTwo = 1;

    @ConfDisplayName("Quantity of x transferred")
    @ConfXMLTag("quantXTransfer")
    public int quantXTransfer = 0;

    @ConfDisplayName("Quantity of y transferred")
    @ConfXMLTag("quantYTransfer")
    public int quantYTransfer = 0;

    @ConfDisplayName("Dynamic Quantity")
    @ConfXMLTag("dynamicParams")
    public ExchangeAgentPairDynamicParams dynParams = new ExchangeAgentPairDynamicParams();

    //@Deprecated // for reflection use only!
    public ExchangeAgentPairParams() {
    }

    public ExchangeAgentPairParams(int typeOne, int typeTwo) {
        this.typeOne = typeOne;
        this.typeTwo = typeTwo;
    }

    @Override
    public void resize(AgentFoodCountable size) {
        // TODO:
    }
}
