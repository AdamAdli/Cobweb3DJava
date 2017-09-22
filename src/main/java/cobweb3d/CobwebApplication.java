package cobweb3d;


import cobweb3d.core.Simulation;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.SimulationRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * This class consists of methods to allow the user to use the Cobweb simulation tool.  It
 * implements all necessary methods defined by the UIClient class, and makes use of the JFrame
 * class.
 */
public class CobwebApplication extends JFrame {

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
        setLayout(new BorderLayout());

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

        // Center window on screen
        setLocationRelativeTo(null);

        simulationRenderer = new SimulationRenderer(simRunner);
        getContentPane().add(simulationRenderer.getBackbuffer(), BorderLayout.CENTER);
        setVisible(true);
    }

    public void quitApplication() {
        dispose();
        System.exit(0);
    }

    /**
     * Creates the main menu bar, which contains all options to allow the user to modify the
     * simulation, save the simulation, etc.
     *
     * @return The menu bar object.
     */
    private JMenuBar makeMenuBar() {
        JMenu fileMenu = new JMenu("File");

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
        return jMenuBar;
    }
}
