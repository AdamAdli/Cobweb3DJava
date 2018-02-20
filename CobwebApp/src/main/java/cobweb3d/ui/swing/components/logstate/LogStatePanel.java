package cobweb3d.ui.swing.components.logstate;

import cobweb3d.SimulationRunner;
import cobweb3d.ui.MenuItemComponent;
import cobweb3d.ui.UpdatableUI;
import util.swing.SimpleAction;
import util.swing.layout.WrapLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Collections;

public class LogStatePanel extends JToolBar implements UpdatableUI.UpdateableLoggingUI, MenuItemComponent {

    private static final long serialVersionUID = 1L;

    private static final int HEIGHT = 19;

    private JLabel logStatusDisplay;

    private SimulationRunner.LoggingSimulationRunner simRunner;
    private JCheckBoxMenuItem toggleLogCheckbox = new JCheckBoxMenuItem(toggleLogStatePanel);
    private Action toggleLogStatePanel = new SimpleAction("Show Logger State", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        setVisible(selected);
    });

    public LogStatePanel(SimulationRunner.LoggingSimulationRunner simulationRunner) {
        this();
        setScheduler(simulationRunner);
    }

    public LogStatePanel() {
        super();
        setLayout(new WrapLayout(FlowLayout.LEFT, 5, 0));
        setFloatable(false);

        add(logStatusDisplay = new JLabel());
        logStatusDisplay.getPreferredSize().height = HEIGHT;
        logStatusDisplay.setVerticalAlignment(JLabel.CENTER);
        logStatusDisplay.setVerticalTextPosition(JLabel.CENTER);
        logStatusDisplay.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));

        setPreferredSize(new Dimension(getPreferredSize().width, HEIGHT));
    }

    public void setScheduler(SimulationRunner.LoggingSimulationRunner simulationRunner) {
        this.simRunner = simulationRunner;
        if (simRunner != null && simRunner.isLogging()) onLogStarted();
        else onLogStopped();
    }

    @Override
    public void onLogStarted() {
        setVisible(true);
        if (simRunner != null && simRunner.isLogging()) logStatusDisplay.setText(simRunner.getLoggingStatus());
    }

    @Override
    public void onLogStopped() {
        setVisible(false);
        logStatusDisplay.setText("Not Logging.");
    }

    @Override
    public void setVisible(boolean visible) {
        boolean oldVisible = isVisible();
        super.setVisible(visible);
        firePropertyChange("visible", oldVisible, visible);
        toggleLogCheckbox.setState(visible);
    }

    @Override
    public Collection<Component> getJMenuItems() {
        toggleLogCheckbox.setState(isVisible());
        return Collections.singleton(toggleLogCheckbox);
    }
}
