package cobweb3d.ui.util;

import cobweb3d.impl.params.AgentParams;
import util.ArrayUtilities;
import util.swing.ColorLookup;

import java.awt.*;

public class TypeColorEnumeration implements ColorLookup {

    private Color[] colors = new Color[1];

    public TypeColorEnumeration(AgentParams[] agentParams) {
        ArrayUtilities.resizeArray(colors, agentParams.length);
        for (int i = 0; i < agentParams.length; i++) {
            colors[i] = Color.decode(agentParams[i].color);
        }
    }

    @Override
    public Color getColor(int index, int num) {
        // generates any number of colors, num bound is ignored.
        Color c = colors[index % colors.length];
        while (index >= colors.length) {
            index -= colors.length;
            c = c.darker();
        }
        return c;
    }
}
