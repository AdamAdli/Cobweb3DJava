package cobweb3d.ui.application;


import cobweb3d.ThreadSimulationRunner;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.javafx.FXSimulationRenderer;
import cobweb3d.ui.swing.components.simstate.SimStatePanel;
import cobweb3d.ui.swing.config.SimulationConfigEditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * This class consists of methods to allow the user to use the Cobweb simulation tool.  It
 * implements all necessary methods defined by the UIClient class, and makes use of the JFrame
 * class.
 */
public class CobwebApplication extends CobwebApplicationSwing {

    SimStatePanel simStatePanel;
    private ISimulationRenderer simulationRenderer;

    public CobwebApplication() {
        super(new ThreadSimulationRunner(new Simulation()));
        setLayout(new BorderLayout());
        setJMenuBar(makeMenuBar());

        simStatePanel = new SimStatePanel(simRunner);
        simStatePanel.setBackground(Color.WHITE);
        add(simStatePanel, BorderLayout.NORTH);

        logInfo("Initializing simulation renderer: " + FXSimulationRenderer.class.getSimpleName());
        simulationRenderer = new FXSimulationRenderer(simRunner);
        add(simulationRenderer.getBackbuffer(), BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Creates the main menu bar, which contains all options to allow the user to modify the
     * simulation, save the simulation, etc.
     *
     * @return The menu bar object.
     */
    private JMenuBar makeMenuBar() {
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(new JMenuItem(openSimulationAct));
        //fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(createNewDataAct));
        fileMenu.add(new JMenuItem(modifySimulationFileAct));
        fileMenu.add(new JMenuItem(retrieveDefaultDataAct));
        fileMenu.add(new JMenuItem(modifySimulationAct));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(loadPopulationAct));
        fileMenu.add(new JMenuItem(savePopulationAct));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(saveSimulationAct));
        fileMenu.add(new JMenuItem(setLogAct));
        fileMenu.add(new JMenuItem(reportData));
        fileMenu.add(new JSeparator());
        fileMenu.add(new JMenuItem(quitAct));

        JMenu editMenu = new JMenu("Edit");

        JMenu projectMenu = new JMenu("Project");

        JMenu viewMenu = new JMenu("View");

        JMenu dataMenu = new JMenu("Data");

        JMenu helpMenu = new JMenu("Help");
        helpMenu.add(new JMenuItem(openAboutAct));
        helpMenu.add(new JMenuItem(openCreditsAct));

        JMenuBar jMenuBar = new JMenuBar();
        jMenuBar.add(fileMenu);
        jMenuBar.add(editMenu);
        jMenuBar.add(projectMenu);
        jMenuBar.add(viewMenu);
        jMenuBar.add(dataMenu);
        jMenuBar.add(helpMenu);

        //  jMenuBar.add(new PauseButton(simRunner));
        //  jMenuBar.add(new StepButton(simRunner));
        // simStatePanel.setPreferredSize(new Dimension(200, 20));
        // jMenuBar.add(simStatePanel);
        return jMenuBar;
    }

    @Override
    public void updateDynamicUI() {
        // TODO:

        super.updateDynamicUI();
    }

    /**
     * Load simulation config.
     *
     * @param config       simulation configuration
     * @param continuation load this as a continuation of the current simulation?
     */
    @Override
    public File openFile(SimulationConfig config, boolean continuation) {
        File file = super.openFile(config, continuation);
        // TODO more organized way to deal with loading simulation configurations
        // TODO create new simRunner when starting new simulation, reuse when modifying
        if (simulationRenderer != null) {
            simulationRenderer.bindSimulation(simRunner.getSimulation());
            simulationRenderer.update(true);
        }
        return file;
    }

    /**
     * Copies the current simulation data being used to a temporary file, which
     * can be modified and saved by the user.
     */
    @Override
    protected void openCurrentData() {
        super.openCurrentData();
        SimulationConfigEditor editor = SimulationConfigEditor.show(this, CURRENT_DATA_FILE_NAME, true);
        if (editor.isOK()) openFile(editor.getConfig(), editor.isContinuation());
    }

    /**
     * Opens the simulation settings window with the current simulation file
     * data.  The user can modify and save the file here.  If the user tries
     * to overwrite data found in the default data file, a dialog box will be
     * created to tell the user the proper way to create new default data.
     */
    @Override
    protected void openCurrentFile() {
        super.openCurrentData();
        SimulationConfigEditor editor = SimulationConfigEditor.show(this, currentFile, true);
        if (editor.isOK()) openFile(editor.getConfig(), editor.isContinuation());
    }
}
