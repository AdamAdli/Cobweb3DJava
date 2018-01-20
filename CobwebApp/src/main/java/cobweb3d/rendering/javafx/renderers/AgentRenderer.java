package cobweb3d.rendering.javafx.renderers;

import cobweb3d.core.agent.Agent;
import cobweb3d.core.location.Direction;
import cobweb3d.core.location.Location;
import cobweb3d.core.location.LocationDirection;
import cobweb3d.rendering.javafx.mesh.PyramidMesh;
import cobwebutil.math.TransformUtil;
import javafx.scene.Group;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class AgentRenderer extends Group {
    ArrayList<Agent> agents;
    private Map<Agent, PyramidMesh> meshCache;

    public AgentRenderer() {
        super();
        setCache(false);
        meshCache = new HashMap<>();
    }

    public void drawAgents(Collection<Agent> agentList) {
        // getChildren().clear();
        PyramidMesh pyramidMesh;
        if (agentList != null) {
            for (Agent agent : agentList) {
                if (meshCache.containsKey(agent)) {
                    pyramidMesh = meshCache.get(agent);
                    pyramidMesh.getTransforms().clear();
                } else {
                    pyramidMesh = new PyramidMesh(1, 1);
                    meshCache.put(agent, pyramidMesh);
                    getChildren().add(pyramidMesh);
                }
                LocationDirection direction = agent.getPosition();
                TransformUtil.TransformNode(pyramidMesh, new Vector3f(direction), 0.5f, 0.5f, 0.5f,
                        new Vector3f(direction.direction), PyramidMesh.UP);
            }
        }
    }

    public void drawTest() {
        if (agents == null) {
            agents = new ArrayList<>();
            Agent agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(2, 2, 0), Direction.xPos);
            agents.add(agent);
            agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(3, 2, 0), Direction.xNeg);
            agents.add(agent);

            agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(2, 1, 0), Direction.yPos);
            agents.add(agent);
            agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(3, 1, 0), Direction.yNeg);
            agents.add(agent);

            agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(2, 0, 0), Direction.zPos);
            agents.add(agent);
            agent = new SimpleAgent(0);
            agent.position = new LocationDirection(new Location(3, 0, 0), Direction.zNeg);
            agents.add(agent);
        }
        drawAgents(agents);
    }

    public static class SimpleAgent extends Agent {
        public SimpleAgent(int type) {
            super(type);
        }

        @Override
        protected Agent createChildAsexual(LocationDirection location) {
            return null;
        }
    }
}
