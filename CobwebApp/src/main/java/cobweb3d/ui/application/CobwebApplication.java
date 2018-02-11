package cobweb3d.ui.application;


import cobweb3d.ThreadSimulationRunner;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.javafx.FXSimulationRenderer;
import cobweb3d.ui.exceptions.UserInputException;
import cobweb3d.ui.swing.components.simstate.SimStatePanel;
import cobweb3d.ui.util.FileDialogUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This class consists of methods to allow the user to use the Cobweb simulation tool.  It
 * implements all necessary methods defined by the UIClient class, and makes use of the JFrame
 * class.
 */
public class CobwebApplication extends CobwebApplicationSwing {

    SimStatePanel simStatePanel;
    private ISimulationRenderer simulationRenderer;
    private Action openSimulationAct = new AbstractAction("Open") {
        @Override
        public void actionPerformed(ActionEvent e) {
            pauseUI();
            try {
                File file = FileDialogUtil.openFile(CobwebApplication.this, "Open Simulation Configuration", "*.xml");
                if (file != null) openFile(Cobweb3Serializer.loadConfig(new FileInputStream(file)), false);
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(CobwebApplication.this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    };
    private Action saveSimulationAct = new AbstractAction("Save") {
        @Override
        public void actionPerformed(ActionEvent e) {
            pauseUI();
            String path = FileDialogUtil.saveFile(CobwebApplication.this, "Save Simulation Configuration", "*.xml");
            if (path != null && !path.isEmpty()) saveSimulation(path);
        }
    };
    private Action setLog = new AbstractAction("Log") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            pauseUI();
            String path = FileDialogUtil.saveFile(CobwebApplication.this, "Choose a file to save log to", "*");
            if (path != null && !path.isEmpty()) {
                try {
                    simRunner.setLog(new FileWriter(path, false));
                } catch (IOException ex) {
                    throw new UserInputException("Can't create log file!", ex);
                }
            }
        }
    };
    private Action modifySimulation = new AbstractAction("Modify Simulation") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            pauseUI();
            openCurrentData();
        }
    };
    private Action modifySimulationFile = new AbstractAction("Modify Simulation File") {
        private static final long serialVersionUID = 1L;

        @Override
        public void actionPerformed(ActionEvent e) {
            pauseUI();
            openCurrentFile();
        }
    };

    public CobwebApplication() {
        super(new ThreadSimulationRunner(new Simulation()));

        setLayout(new BorderLayout());
        setJMenuBar(makeMenuBar());

        simStatePanel = new SimStatePanel(simRunner);
        add(simStatePanel, BorderLayout.NORTH);


        logInfo("Initializing simulation renderer: " + FXSimulationRenderer.class.getSimpleName());
        simulationRenderer = new FXSimulationRenderer(simRunner);
        add(simulationRenderer.getBackbuffer(), BorderLayout.CENTER);
    }

    public void pauseUI() {
        simRunner.stop();
    }

    private void updateDynamicUI() {
        // TODO:

        validate();
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
        fileMenu.add(new JMenuItem(saveSimulationAct));
        fileMenu.add(new JSeparator());
        // fileMenu.add(new JMenuItem(setLogAction));
        // fileMenu.add(new JMenuItem(quitAction));
        JMenu editMenu = new JMenu("Edit");

        JMenu projectMenu = new JMenu("Project");

        JMenu viewMenu = new JMenu("View");

        JMenu dataMenu = new JMenu("Data");

        JMenu helpMenu = new JMenu("Help");

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
        /* TODO: SimulationConfigEditor editor = SimulationConfigEditor.show(this, currentData, true, displaySettings);
        if (editor.isOK()) {
            openFile(editor.getConfig(), editor.isContinuation());
        }*/
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
        /* TODO: SimulationConfigEditor editor = SimulationConfigEditor.show(this, currentFile, true, displaySettings);
        if (editor.isOK()) {
            openFile(editor.getConfig(), editor.isContinuation());
        }*/
    }
}
