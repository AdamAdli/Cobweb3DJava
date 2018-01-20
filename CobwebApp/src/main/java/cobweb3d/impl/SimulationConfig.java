package cobweb3d.impl;

import cobweb3d.core.params.AgentFoodCountable;
import cobweb3d.impl.params.AgentParams;
import cobweb3d.impl.params.EnvironmentParams;
import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;

import java.lang.reflect.Field;

/**
 * Used to organize, modify, and access simulation parameters.
 */
public class SimulationConfig implements ParameterSerializable, AgentFoodCountable {

    private static final long serialVersionUID = 2L;
    public String fileName = "default simulation";
    /**
     * Random number generator seed for repeating the simulation exactly.
     */
    @ConfDisplayName("Random seed")
    @ConfXMLTag("randomSeed")
    public long randomSeed = 42;
    @ConfXMLTag("Environment")
    public EnvironmentParams envParams = new EnvironmentParams();
    @ConfXMLTag("Agents")
    public AgentParams agentParams = new AgentParams(this);
    private int agentTypeCount = 4;

    /**
     * Creates the default Cobweb simulation parameters.
     */
    public SimulationConfig() {
    }

    public int getAgentTypes() {
        return agentTypeCount;
    }

    /**
     * Number of Agent types.
     */
    @ConfDisplayName("Agent types")
    @ConfXMLTag("AgentTypeCount")
    public void setAgentTypes(int count) {
        this.agentTypeCount = count;
        agentCountChanged();
    }

    private void agentCountChanged() {
        // Stub.
    }

    public boolean isContinuation() {
        return false;
    }

    public <T> T getParam(Class<T> pt) {
        if (pt.isAssignableFrom(this.getClass())) {
            @SuppressWarnings("unchecked")
            T result = (T) this;
            return result;
        } else {
            for (Field f : this.getClass().getFields()) {
                if (pt.isAssignableFrom(f.getType())) {
                    try {
                        @SuppressWarnings("unchecked")
                        T result = (T) f.get(this);
                        return result;
                    } catch (IllegalAccessException ex) {
                        throw new RuntimeException("Could not get parameter " + f.getName(), ex);
                    }
                }
            }
        }
        return null;
    }
}
