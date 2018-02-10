package cobweb3d.ui.swing.components;

import cobweb3d.SimulationRunner;
import cobwebutil.math.MaterialColor;

import javax.swing.*;
import java.awt.*;

public class PauseButton extends JButton implements java.awt.event.ActionListener {
    public static final long serialVersionUID = 0xE55CC6E3B8B5824DL;
    private SimulationRunner scheduler;
    private boolean myRunning = false;

    public PauseButton(SimulationRunner scheduler) {
        super("Start");
        this.scheduler = scheduler;
        addActionListener(this);
        setBackground(MaterialColor.green_300.asAWTColor());
        setPreferredSize(new Dimension(63, 26));
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (scheduler.isRunning()) {
            scheduler.stop();
        } else {
            scheduler.run();
        }
        repaint();
    }

    public void setScheduler(SimulationRunner scheduler) {
        this.scheduler = scheduler;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        boolean running = scheduler.isRunning();
        if (running != myRunning) {
            myRunning = running;
            if (myRunning) {
                setText("Stop");
                setBackground(MaterialColor.red_300.asAWTColor());
            } else {
                setText("Start");
                setBackground(MaterialColor.green_300.asAWTColor());
            }
        }
        super.paintComponent(g);
    }

}