package cobweb3d.plugins.exchange;

import cobweb3d.plugins.exchange.utility.Calculation;
import cobweb3d.plugins.exchange.utility.UtilityFunctionParam;
import io.CloneHelper;
import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;

public class ExchangeAgentParams implements ParameterSerializable, Calculation {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Initial x")
    @ConfXMLTag("initialX")
    public int initialX = 0;

    @ConfDisplayName("Initial y")
    @ConfXMLTag("initialY")
    public int initialY = 0;

    @ConfDisplayName("Utility function")
    @ConfXMLTag("utilityFunction")
    public UtilityFunctionParam utilityFunctionParam = new UtilityFunctionParam();

    //@Deprecated // for reflection use only!
    public ExchangeAgentParams() {
    }

    @Override
    public ExchangeAgentParams clone() {
        try {
            ExchangeAgentParams copy = (ExchangeAgentParams) super.clone();
            copy.utilityFunctionParam = this.utilityFunctionParam.clone();
            CloneHelper.resetMutatable(copy);
            return copy;
        } catch (CloneNotSupportedException ex) {
            throw new RuntimeException(ex);
        }
    }

    public int calculateU(ExchangeState agentState) {
        if (agentState == null) return -1;
        return calculateU(agentState.x, agentState.y);
    }

    public int calculateU(int x, int y) {
        return calculateU(x, y, utilityFunctionParam.varA, utilityFunctionParam.varB);
    }

    @Override
    public int calculateU(int x, int y, float A, float B) {
        return utilityFunctionParam.formula.calculateU(x, y, A, B);
    }
}