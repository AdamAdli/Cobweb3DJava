package cobweb3d.ui.swing.components.simstate;

import cobweb3d.SimulationRunner;
import cobweb3d.ThreadSimulationRunner;
import cobweb3d.ui.UpdatableUI;
import util.swing.SimpleDocumentListener;
import util.swing.SimpleFocusAdapter;

import javax.swing.*;
import java.awt.*;

public class SimStatePanel extends JToolBar implements UpdatableUI {

    private static final long serialVersionUID = 1L;
    private PauseButton pauseButton;
    private StepButton stepButton;
    private SpeedBar speedBar;
    private JTextField tickField;
    private JLabel tickDisplay;
    private SimulationRunner simRunner;

    public SimStatePanel(SimulationRunner simulationRunner) {
        this();
        setScheduler(simulationRunner);
    }

    public SimStatePanel() {
        super();
        setFloatable(false);
        add(pauseButton = new PauseButton());
        add(stepButton = new StepButton());
        add(speedBar = new SpeedBar());
        add(tickDisplay = new JLabel());
        add(new JLabel("Stop at: "));
        add(tickField = new JTextField());

        pauseButton.setPreferredSize(new Dimension(pauseButton.getPreferredSize().width, 20));
        stepButton.setPreferredSize(new Dimension(stepButton.getPreferredSize().width, 20));

        speedBar.setPreferredSize(new Dimension(64, 20));
        speedBar.setMaximumSize(new Dimension(64, 20));

        tickDisplay.setPreferredSize(new Dimension(128, 20));

        tickField.setMinimumSize(new Dimension(128, 20));
        tickField.setPreferredSize(new Dimension(128, 20));
        tickField.setMaximumSize(new Dimension(128, 20));
        tickField.getDocument().addDocumentListener((SimpleDocumentListener)
                () -> simRunner.setAutoStopTime(Integer.parseInt(tickField.getText())));
        tickField.addFocusListener(new SimpleFocusAdapter(tickField::repaint));
    }

    public void setScheduler(SimulationRunner simulationRunner) {
        this.simRunner = simulationRunner;
        pauseButton.setScheduler(simulationRunner);
        stepButton.setScheduler(simulationRunner);
        if (simulationRunner instanceof ThreadSimulationRunner)
            speedBar.setScheduler((ThreadSimulationRunner) simulationRunner);
        else speedBar.setVisible(false);
    }

    @Override
    public void update(boolean synchronous) {
        if (simRunner != null) tickDisplay.setText("Tick: " + simRunner.getSimulation().getTime() + "  ");
    }

    @Override
    public void onStopped() {
        pauseButton.repaint();
        update(true);
    }

    @Override
    public void onStarted() {
        pauseButton.repaint();
    }
}
