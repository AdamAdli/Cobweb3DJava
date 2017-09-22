package cobweb3d.rendering;

import cobweb3d.core.Simulation;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GLDrawable;
import com.jogamp.opengl.util.GLBuffers;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;


public class GridRenderer {
    FloatBuffer floatBuffer; //= GLBuffers.newDirectFloatBuffer(vertexData)
    ShortBuffer elementBuffer; //= GLBuffers.newDirectShortBuffer(elementData);
    public GridRenderer(Simulation simulation, GL3 gl3) {
        float[] verticies = new float[];
        for (int x = 0; x < simulation.environment.width; x++) {
            for (int y = 0; y < simulation.environment.height; y++) {
                for (int z = 0; z < simulation.environment.depth; z++) {

                }
            }
        }

    }
}
