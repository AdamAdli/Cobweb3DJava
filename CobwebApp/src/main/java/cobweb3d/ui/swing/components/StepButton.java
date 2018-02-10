package cobweb3d.ui.swing.components;


import cobweb3d.SimulationRunner;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * This class represents the button responsible for advancing
 * the application by one time tick.
 *
 * @author skinawy
 */
public class StepButton extends JButton implements ActionListener {
    public static final long serialVersionUID = 0xD4B844C0AA5E3991L;
    private SimulationRunner scheduler;

    public StepButton(SimulationRunner scheduler) {
        super("Step");
        setScheduler(scheduler);
        addActionListener(this);
    }

    public void setScheduler(SimulationRunner scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        scheduler.step();
    }
}