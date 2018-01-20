package cobweb3d.plugins.states;

import io.ParameterSerializable;

public interface AgentState extends ParameterSerializable {

    boolean isTransient();
}
