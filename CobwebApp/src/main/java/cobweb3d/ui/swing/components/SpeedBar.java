package cobweb3d.ui.swing.components;

import cobweb3d.ThreadSimulationRunner;

import javax.swing.*;
import java.awt.*;
import java.awt.event.AdjustmentEvent;

public class SpeedBar extends JScrollBar implements
        java.awt.event.AdjustmentListener {
    public static final long serialVersionUID = 0xD5E78F1D65B18165L;
    private static final int SCROLLBAR_TICKS = 11;
    private final ThreadSimulationRunner scheduler;
    private final Dimension d = new Dimension(70, 18);
    private final Color original;

    public SpeedBar(ThreadSimulationRunner scheduler) {
        this.scheduler = scheduler;
        setOrientation(Scrollbar.HORIZONTAL);
        addAdjustmentListener(this);
        this.setValues(SCROLLBAR_TICKS - 1, 0, 0, SCROLLBAR_TICKS);
        this.setPreferredSize(d);
        original = this.getBackground();
    }

    @Override
    public void adjustmentValueChanged(AdjustmentEvent e) {
        int delay = 0;
        int d1 = SCROLLBAR_TICKS - getValue();
        if (d1 != 0) {
            delay = 1 << (d1 - 1);
        }
        if (delay == 0) {
            this.setBackground(Color.yellow);
        } else {
            this.setBackground(original);
        }
        scheduler.setDelay(delay);
    }
}