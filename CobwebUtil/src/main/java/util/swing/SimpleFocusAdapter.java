package util.swing;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public abstract class SimpleFocusAdapter extends FocusAdapter {

    /**
     * Called when the focus is changed in any way.
     */
    public abstract void update();

    @Override
    public void focusGained(FocusEvent e) {
        update();
    }

    @Override
    public void focusLost(FocusEvent e) {
        update();
    }
}
