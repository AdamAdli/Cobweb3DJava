package cobweb3d.plugins.transform;

import cobweb3d.plugins.states.AgentState;
import io.ConfList;
import io.ConfXMLTag;

import java.util.LinkedList;
import java.util.List;

public class TransformationState implements AgentState {
    // pregnancyPeriod is set value while pregPeriod constantly changes
    @ConfXMLTag("previousTypes")
    @ConfList(indexName = "PrevTypes", startAtOne = false)
    protected List<Integer> prevTypes = new LinkedList<>();

    @Deprecated // for reflection use only!
    public TransformationState() {
    }

    public TransformationState(TransformationState prevState, int curType) {
        if (prevState != null) prevTypes.addAll(prevState.prevTypes);
        prevTypes.add(curType);
    }

    @Override
    public boolean isTransient() {
        return false;
    }
}
