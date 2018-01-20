package cobweb3d.impl.params;

import io.ConfDisplayName;
import io.ConfXMLTag;
import io.ParameterSerializable;

public class EnvironmentParams implements ParameterSerializable {

    private static final long serialVersionUID = 2L;
    /**
     * Width of the grid.
     */
    @ConfDisplayName("Width")
    @ConfXMLTag("Width")
    public int width = 80;
    /**
     * Height of the grid.
     */
    @ConfDisplayName("Height")
    @ConfXMLTag("Height")
    public int height = 80;
    /**
     * Height of the grid.
     */
    @ConfDisplayName("Depth")
    @ConfXMLTag("Depth")
    public int depth = 80;
    /**
     * Enables the grid to wrap around at the edges.
     */
    @ConfDisplayName("Wrap edges")
    @ConfXMLTag("wrap")
    public boolean wrapMap = true;
    /**
     * Number of stones to randomly place
     */
    @ConfDisplayName("Random stones")
    @ConfXMLTag("randomStones")
    public int initialStones = 10;
}
