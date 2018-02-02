package cobweb3d;


import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.javafx.FXSimulationRenderer;
import cobweb3d.ui.AppContext;
import cobweb3d.ui.exceptions.UserInputException;
import cobweb3d.ui.util.FileDialogUtil;
import javafx.application.Platform;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.logging.Logger;

/**
 * This class consists of methods to allow the user to use the Cobweb simulation tool.  It
 * implements all necessary methods defined by the UIClient class, and makes use of the JFrame
 * class.
 */
public class CobwebApplication extends JFrame implements AppContext {

    private static final String WINDOW_TITLE = "COBWEB 3D";

    /**
     * Filename of current simulation config
     */
    private String currentFile;

    public static final String CONFIG_FILE_EXTENSION = ".xml";

    private static final String TEMPORARY_FILE_EXTENSION = ".cwtemp";

    static final String INITIAL_OR_NEW_INPUT_FILE_NAME =
            "initial_or_new_input_(reserved)" + CONFIG_FILE_EXTENSION;

    public static final String DEFAULT_DATA_FILE_NAME = "default_data_(reserved)";

    private static final String CURRENT_DATA_FILE_NAME =
            "current_data_(reserved)" + TEMPORARY_FILE_EXTENSION;

    private final Logger logger = Logger.getLogger("COBWEB3D");

    public ThreadSimulationRunner simRunner = new ThreadSimulationRunner(new Simulation());

    private ISimulationRenderer simulationRenderer;

    public CobwebApplication() {
        super(WINDOW_TITLE);

        logger.info("Welcome to Cobweb 3D");
        logger.info("JVM Memory: " + Runtime.getRuntime().maxMemory() / 1024 + "KB");

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                quitApplication();
            }
        });

        setLayout(new BorderLayout());
        setSize(580, 650);
        setJMenuBar(makeMenuBar());
        //add(makeMenuBar(), BorderLayout.AFTER_LINE_ENDS);
        setLocationRelativeTo(null);
        logger.info("Initializing simulation renderer: " + FXSimulationRenderer.class.getSimpleName());
        simulationRenderer = new FXSimulationRenderer(simRunner);
        add(simulationRenderer.getBackbuffer(), BorderLayout.CENTER);

        setVisible(true);
    }

    public void quitApplication() {
        getContentPane().removeAll();
        Platform.exit();
        dispose();
        System.exit(0);
    }

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
            if (path != null && !path.isEmpty()) {
                saveSimulation(path);
            }
            //   openFileDialog();
        }
    };

    /**
     * Load simulation config.
     *
     * @param config       simulation configuration
     * @param continuation load this as a continuation of the current simulation?
     */
    public void openFile(SimulationConfig config, boolean continuation) {
        // TODO more organized way to deal with loading simulation configurations
        // TODO create new simRunner when starting new simulation, reuse when modifying
        if (simRunner.isRunning())
            simRunner.stop();

        if (!continuation) {
            simRunner.getSimulation().resetTime();
            simRunner.setLog(null);
        }
        simRunner.getSimulation().load(config);

        File file = new File(config.fileName);
        if (file.exists()) {
            try {
                currentFile = file.getCanonicalPath();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            if (file.isHidden() || !file.canWrite()) {
                JOptionPane.showMessageDialog(this,
                        "Caution:  The initial data file \"" + currentFile
                                + "\" is NOT allowed to be modified.\n"
                                + "\n                  Any modification of this data file will be neither implemented nor saved.");
            }
        }

        updateDynamicUI();

        setTitle(WINDOW_TITLE + " - " + file.getName());

        // simulatorUI null only when called from constructor.
        // TODO: create UI in such a way as to avoid this check
        if (simulationRenderer != null) {
            simulationRenderer.bindSimulation(simRunner.getSimulation());
            simulationRenderer.update(true);
        }
    }

    public void pauseUI() {
        simRunner.stop();
    }

    public void saveSimulation(String savePath) {
        File sf = new File(savePath);
        if (sf.isHidden() || (sf.exists() && !sf.canWrite())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Caution:  File \"" + savePath + "\" is NOT allowed to be written to.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try (OutputStream file = new FileOutputStream(sf)) {
                Cobweb3Serializer serializer = new Cobweb3Serializer();
                serializer.saveConfig(simRunner.getSimulation().simulationConfig, file);
            } catch (IOException ex) {
                throw new UserInputException("Save failed", ex);
            }
        }
    }

    private void updateDynamicUI() {
        // TODO:

        validate();
    }

  //  @Override
    public void showOpenFileDialog() {

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
        JToggleButton playButton = new JToggleButton("Pause", false);
        playButton.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.DESELECTED) {
                playButton.setText("Pause");
            } else if (event.getStateChange() == ItemEvent.SELECTED) {
                playButton.setText("Play");
            }
        });
      //  jMenuBar.add(playButton);
        return jMenuBar;
    }
}
