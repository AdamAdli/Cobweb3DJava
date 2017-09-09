package cobweb3d;


import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;

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
public class CobwebApplication extends JFrame implements GLEventListener {

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

    private final Logger logger = Logger.getLogger("COBWEB2");

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

        initializeOpenGL();
        setVisible(true);

    }

    private void initializeOpenGL() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glProfile);
        final GLCanvas glcanvas = new GLCanvas(glcapabilities);
        glcanvas.addGLEventListener(this);
        getContentPane().add(glcanvas, BorderLayout.CENTER);
    }

    int w = 100;
    int h = 100;

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

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glClear(GL.GL_COLOR_BUFFER_BIT);

        // draw a triangle filling the window
        gl2.glLoadIdentity();
        gl2.glBegin(GL.GL_TRIANGLES);
        gl2.glColor3f(1, 0, 0);
        gl2.glVertex2f(0, 0);
        gl2.glColor3f(0, 1, 0);
        gl2.glVertex2f(w, 0);
        gl2.glColor3f(0, 0, 1);
        gl2.glVertex2f(w / 2, h);
        gl2.glEnd();
    }

    /**
     * Called on window reshaped.
     */
    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl2 = glAutoDrawable.getGL().getGL2();
        gl2.glMatrixMode(GL2.GL_PROJECTION);
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL2.GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height);
    }
}
