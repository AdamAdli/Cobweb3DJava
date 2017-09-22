package cobweb3d.rendering;

import cobweb3d.SimulationRunner;
import cobweb3d.SimulationRunnerBase;
import cobweb3d.core.Simulation;
import cobweb3d.core.SimulationInterface;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;
import java.awt.*;

public class SimulationRenderer implements ISimulationRenderer, GLEventListener {

    private SimulationInterface simulation;
    private FPSAnimator fpsAnimator;
    private GLCanvas glCanvas;

    public SimulationRenderer(SimulationRunnerBase simulationRunner) {
        initializeOpenGL();
        bindSimulation(simulationRunner.getSimulation());
        simulationRunner.addUIComponent(this);
    }

    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;

    }

    @Override
    public Component getBackbuffer() {
        return glCanvas;
    }

    @Override
    public void update(boolean synchronous) {
        if (fpsAnimator.isPaused()) fpsAnimator.start();
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
        fpsAnimator.start();
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

    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        fpsAnimator.stop();
    }

    int w = 100;
    int h = 100;

    @Override
    public void display(GLAutoDrawable drawable) {
        w += 1;
        GL2 gl2 = drawable.getGL().getGL2();
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

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        GL2 gl2 = drawable.getGL().getGL2();
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
