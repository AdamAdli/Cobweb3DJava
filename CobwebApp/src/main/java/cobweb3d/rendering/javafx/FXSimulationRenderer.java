package cobweb3d.rendering.javafx;

import cobweb3d.SimulationRunnerBase;
import cobweb3d.impl.Simulation;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.javafx.renderers.AgentRenderer;
import cobweb3d.rendering.javafx.renderers.GridRenderer;
import cobwebutil.math.MaterialColor;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.awt.*;

public class FXSimulationRenderer implements ISimulationRenderer {
    private JFXPanel jfxPanel;

    private Simulation simulation;

    private Parent mainLayout;

    private SimCamera camera;

    private SubScene renderScene;

    private Group rootGroup;
    private GridRenderer gridRenderer;
    private final ChangeListener<Number> resizeListener = (observable, oldValue, newValue) ->
            resizeRendering((int) renderScene.getParent().getScene().getWidth(),
                    (int) renderScene.getParent().getScene().getHeight());
    private AgentRenderer agentRenderer;

    public FXSimulationRenderer(SimulationRunnerBase simulationRunner) {
        bindSimulation(simulationRunner.getSimulation());
        simulationRunner.addUIComponent(this);
        initJavaFX();
    }

    private void initJavaFX() {
        jfxPanel = new JFXPanel();
        mainLayout = new BorderPane(initRenderScene());
        BorderPane layout = (BorderPane) mainLayout;
        layout.widthProperty().addListener(resizeListener);
        layout.heightProperty().addListener(resizeListener);
        jfxPanel.setScene(new Scene(layout, MaterialColor.grey_100.asJFXColor()));
    }

    private SubScene initRenderScene() {
        gridRenderer = new GridRenderer(simulation.environment);
        agentRenderer = new AgentRenderer();
        rootGroup = new Group(gridRenderer, agentRenderer);
        camera = new SimCamera();

        renderScene = new SubScene(rootGroup, 200, 200, true, SceneAntialiasing.BALANCED);
        renderScene.setFill(Color.WHITE);
        renderScene.setCamera(camera);
        return renderScene;
    }

    @Override
    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;
        if (jfxPanel == null) return;
        Platform.runLater(() -> {
            if (rootGroup != null) {
                if (gridRenderer != null) {
                    gridRenderer.generateGeometry(simulation.environment);
                    gridRenderer.focusCamera(camera);
                }
            }
        });

    }

    @Override
    public Component getBackbuffer() {
        return jfxPanel;
    }

    @Override
    public void update(boolean synchronous) {
        if (jfxPanel == null) return;
        Platform.runLater(() -> {
            if (rootGroup != null) {
                if (simulation != null && simulation.environment != null)
                    agentRenderer.drawAgents(simulation.environment.getAgents());
            }
        });
    }

    @Override
    public void onStopped() {
    }

    @Override
    public void onStarted() {
    }

    private void resizeRendering(int width, int height) {
        renderScene.setWidth(width);
        renderScene.setHeight(height);
        if (height == 0) height = 1;   // prevent divide by zero
        camera.adjustForResolution(width, height);
        gridRenderer.focusCamera(camera);
    }
}
