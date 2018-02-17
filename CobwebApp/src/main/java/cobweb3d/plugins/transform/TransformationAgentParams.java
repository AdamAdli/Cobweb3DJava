package cobweb3d.plugins.transform;

import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;
import util.MutatableInt;

public class TransformationAgentParams implements ParameterSerializable {
    private static final long serialVersionUID = 12L;

    @ConfDisplayName("Transformation Enabled")
    @ConfXMLTag("enabled")
    public boolean enabled = false;

    /**
     * Chance that bumping into another agent will result in sexual breeding.
     */
    @ConfDisplayName("Destination Type")
    @ConfXMLTag("transformTo")
    public int destType = 1;

    /**
     * Chance that bumping into another agent will result in sexual breeding.
     */
    @ConfDisplayName("x Threshold")
    @ConfXMLTag("transformationXThreshold")
    public MutatableInt transformationX = new MutatableInt(100);

    //@Deprecated // for reflection use only!
    public TransformationAgentParams() {
    }
}