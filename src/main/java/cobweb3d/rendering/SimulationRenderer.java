package cobweb3d.rendering;

import cobweb3d.BuildConfig;
import cobweb3d.SimulationRunner;
import cobweb3d.SimulationRunnerBase;
import cobweb3d.core.Simulation;
import cobweb3d.core.SimulationInterface;
import cobwebutil.math.Vector3F;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import sun.nio.cs.ext.MacHebrew;

import javax.swing.*;
import java.awt.*;
import java.nio.FloatBuffer;


import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;
import static com.jogamp.opengl.fixedfunc.GLLightingFunc.GL_SMOOTH;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_MODELVIEW;
import static com.jogamp.opengl.fixedfunc.GLMatrixFunc.GL_PROJECTION;

public class SimulationRenderer implements ISimulationRenderer, GLEventListener {

    private Simulation simulation;
    private FPSAnimator fpsAnimator;
    private GLCanvas glCanvas;
    private GLU glu;  // for the GL Utility
    static boolean once = false;

    private float fovY = 45.0f;
    private float fovX = 45.0f;
    private float fovYr = 45.0f;
    private float fovXr = 45.0f;

    GridRenderer mGridRenderer;

    public SimulationRenderer(SimulationRunnerBase simulationRunner) {
        initializeOpenGL();
        bindSimulation(simulationRunner.getSimulation());
        simulationRunner.addUIComponent(this);
    }

    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;
        this.mGridRenderer = new GridRenderer(simulation.environment);
    }

    @Override
    public Component getBackbuffer() {
        return glCanvas;
    }

    @Override
    public void update(boolean synchronous) {
       // if (fpsAnimator.isPaused()) fpsAnimator.start();
    }

    @Override
    public boolean isReadyToUpdate() {
        return glCanvas != null && fpsAnimator != null;
    }

    @Override
    public void onStopped() {
        fpsAnimator.stop();
    }

    @Override
    public void onStarted() {
    //    fpsAnimator.start();
    }

    private void initializeOpenGL() {
        GLProfile glProfile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities(glProfile);
        glCanvas = new GLCanvas(glcapabilities);
        glCanvas.addGLEventListener(this);

        fpsAnimator = new FPSAnimator(glCanvas, 61);
     //   fpsAnimator.start();
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        glu = new GLU();
        GL2 gl;
        if( !once ) {
            once = true;
            gl = tryDebugGL(drawable.getGL()).getGL2();
        } else {
            gl = drawable.getGL().getGL2();
        }
        gl.glClearColor(1.0f, 1.0f, 1.0f, 1.0f); // set background (clear) color
        gl.glClearDepth(1.0f);      // set clear depth value to farthest
        gl.glEnable(GL_DEPTH_TEST); // enables depth testing
        gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
        gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        fpsAnimator.stop();
    }

    int w = 100;
    int h = 100;

    /**
     * Called back by the animator to perform rendering.
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl2 = drawable.getGL().getGL2();
        gl2.glClear(GL_COLOR_BUFFER_BIT);

        // draw a triangle filling the window
       /* gl2.glLoadIdentity();
        gl2.glBegin(GL.GL_TRIANGLES);
        gl2.glColor3f(1, 0, 0);
        gl2.glVertex2f(0, 0);
        gl2.glColor3f(0, 1, 0);
        gl2.glVertex2f(w, 0);
        gl2.glColor3f(0, 0, 1);
        gl2.glVertex2f(w / 2, h);
        gl2.glEnd(); */

        GL2 gl = drawable.getGL().getGL2();  // Get the OpenGL 2 graphics context
        gl.glClearColor(0.93f, 0.93f, 0.93f, 1f);
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // Clear color and depth buffers
        gl.glLoadIdentity();  // Reset the model-view matrix

      //  float p = 5;
      //  float r = 8.66025403784f;
       /// float z = r/(float)(Math.sin(fovYr));//fovY*3.14/180));
      //  float z2 = r/(float)(Math.sin(fovXr));//fovX*3.14/180));

        Vector3F gridCenter = new Vector3F(((float) simulation.environment.width) / 2f, ((float) simulation.environment.height) / 2f,
                ((float) simulation.environment.depth) / 2f);
        float gridBoundingRadius = new Vector3F(simulation.environment.width, simulation.environment.height,
                simulation.environment.depth).length() / 2;
        float optimalDistanceY = ((float)simulation.environment.height/2) / (float) Math.tan(fovYr / 2);
        float optimalDistanceX = ((float)simulation.environment.width/2) / (float) Math.tan(fovXr / 2);
        //        float optimalDistanceX = (gridBoundingRadius * (0.5f + ((float)simulation.environment.width / (float)simulation.environment.height))) / (float) Math.tan(fovXr);

        //float optimalDistanceY = (simulation.environment.height) / (float) Math.sin(fovYr);
      //  float optimalDistanceX = (gridBoundingRadius * 2) / (float) Math.tan(fovXr);


        /*float optimalDistanceY = gridBoundingRadius / (float) Math.sin(fovYr);
        float optimalDistanceX = gridBoundingRadius / (float) Math.sin(fovXr);*/

        System.out.println("Yd: " + optimalDistanceY + " | Xd: " + optimalDistanceX + " | fovY half : " + fovY + " | fovX half: " + (fovX));

       // System.out.println("GridCenter: " + gridCenter.toString());
       // System.out.println("gridBoundingRadius: " + gridBoundingRadius);
       // float dis = (gridBoundingRadius * 2) / (float) Math.tan(fovXr % (3.14/4));
      //  System.out.println("fovX: " + fovX + " | calcFovX: " + Math.toDegrees(fovXr % (3.14/4)));
      //  System.out.println("dis: " + dis);

        glu.gluLookAt(gridCenter.x, gridCenter.y, -Math.max(optimalDistanceY, optimalDistanceX),
                gridCenter.x, gridCenter.y, gridCenter.z,
                0, 1, 0);

        // ----- Your OpenGL rendering code here (Render a white triangle for testing) -----
        // gl.glTranslatef(5.0f, 5.0f, -20.0f);

        mGridRenderer.draw(simulation.environment, gl);
        /*gl.glTranslatef(0.0f, 0.0f, -6.0f); // translate into the screen
         gl.glBegin(GL_TRIANGLES); // draw using triangles
        gl.glVertex3f(0.0f, 1.0f, 0.0f);
        gl.glVertex3f(-1.0f, -1.0f, 0.0f);
        gl.glVertex3f(1.0f, -1.0f, 0.0f);
        gl.glEnd(); */

    }

    /**
     * Call-back handler for window re-size event. Also called when the drawable is
     * first set to visible.
     */
    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        /*GL2 gl2 = drawable.getGL().getGL2();
        gl2.glMatrixMode(GL_PROJECTION);
        gl2.glLoadIdentity();

        // coordinate system origin at lower left with width and height same as the window
        GLU glu = new GLU();
        glu.gluOrtho2D(0.0f, width, 0.0f, height);

        gl2.glMatrixMode(GL_MODELVIEW);
        gl2.glLoadIdentity();

        gl2.glViewport(0, 0, width, height); */

        if (height == 0) height = 1;   // prevent divide by zero
        float aspect = ((float) width) / ((float) height);


        GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

        // Set the view port (display area) to cover the entire window.
        gl.glViewport(0, 0, width, height);

        // Setup perspective projection, with aspect ratio matches viewport
        gl.glMatrixMode(GL_PROJECTION);  // Choose projection matrix.
        gl.glLoadIdentity(); // Reset projection matrix.
        glu.gluPerspective(fovY, aspect, 0.1, 1000.0); // FoVy, aspect, zNear, zFar
        fovYr = (float) Math.toRadians(fovY);
        fovXr = 2 * (float) Math.atan(Math.tan(fovYr / 2) * aspect);//fovYr * aspect; //2 * (float) Math.atan(Math.tan(fovYr / 2) * aspect);
        fovX = (float) Math.toDegrees(fovXr);
      //  fovX = (float) (float) Math.toDegrees(Math.atan(Math.tan(Math.toRadians(fovY) ) * aspect));//fovY * aspect;//(float) Math.toDegrees(Math.atan(Math.tan(fovY) * aspect));

        // Enable the model-view transform
        gl.glMatrixMode(GL_MODELVIEW);
        gl.glLoadIdentity(); // reset
    }


    private static GL tryDebugGL(GL gl) {
        if(BuildConfig.DEBUG) {
            try {
                // Debug ..
                gl = gl.getContext().setGL( GLPipelineFactory.create("com.jogamp.opengl.Debug", null, gl, null) );
            } catch (Exception e) {e.printStackTrace();}
            try {
                // Trace ..
              //  gl = gl.getContext().setGL( GLPipelineFactory.create("com.jogamp.opengl.Trace", null, gl, new Object[] { System.err } ) );
            } catch (Exception e) {e.printStackTrace();}
        }
        return gl;
    }
}
