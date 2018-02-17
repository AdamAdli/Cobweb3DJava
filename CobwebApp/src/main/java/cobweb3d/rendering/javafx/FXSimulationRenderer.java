package cobweb3d.rendering.javafx;

import cobweb3d.SimulationRunnerBase;
import cobweb3d.impl.Simulation;
import cobweb3d.rendering.ISimulationRenderer;
import cobweb3d.rendering.javafx.renderers.GridRenderer;
import cobweb3d.rendering.javafx.renderers.UncachedAgentRenderer;
import cobwebutil.MaterialColor;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.embed.swing.JFXPanel;
import javafx.scene.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.logging.Logger;

public class FXSimulationRenderer implements ISimulationRenderer {
    private JFXPanel jfxPanel;

    private Simulation simulation;
    private final Logger logger = Logger.getLogger("FXSimulationRenderer");

    private Parent mainLayout;
    private SimCamera camera;
    private SubScene renderScene;
    private Group rootGroup;
    private GridRenderer gridRenderer;
    private final ChangeListener<Number> resizeListener = (observable, oldValue, newValue) -> resizeRendering((int) renderScene.getParent().getScene().getWidth(),
            (int) renderScene.getParent().getScene().getHeight());
    private UncachedAgentRenderer agentRenderer;

    AnimationTimer animationTimer;

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
        logger.info("Initialized JavaFX");

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (rootGroup != null) {
                    // TODO: FIX FUNCTIONALITY! POOR PERFORMANCE WITH THIS FIX.
                    rootGroup.getChildren().remove(agentRenderer);
                    agentRenderer = new UncachedAgentRenderer();
                    rootGroup.getChildren().add(agentRenderer);
                    if (simulation != null && simulation.environment != null)
                        agentRenderer.drawAgents(new ArrayList<>(simulation.getAgents())); // TODO: Check concurrency.
                }
            }
        };
    }

    private SubScene initRenderScene() {
        gridRenderer = new GridRenderer(simulation.environment);
        agentRenderer = new UncachedAgentRenderer();
        rootGroup = new Group(gridRenderer, agentRenderer);
        camera = new SimCamera();

        renderScene = new SubScene(rootGroup, 200, 200, true, SceneAntialiasing.BALANCED);
        renderScene.setFill(Color.WHITE);
        renderScene.setCamera(camera);
        return renderScene;
    }

    @Override
    public void refreshSimulation() {
        if (jfxPanel == null) return;
        animationTimer.stop(); // TODO: Should contol the animationTimer?
        Platform.runLater(() -> {
            if (rootGroup != null) {
                if (gridRenderer != null) {
                    gridRenderer.generateGeometry(simulation.environment);
                    gridRenderer.focusCamera(camera);
                }
                if (agentRenderer != null) agentRenderer.clearCache();
                animationTimer.start();
            }
        });
    }

    @Override
    public void bindSimulation(Simulation simulation) {
        this.simulation = simulation;
        if (jfxPanel == null) return;
        animationTimer.stop();
        Platform.runLater(() -> {
            if (rootGroup != null) {
                if (gridRenderer != null) {
                    gridRenderer.generateGeometry(simulation.environment);
                    gridRenderer.focusCamera(camera);
                }
                animationTimer.start();
            }
        });
    }

    @Override
    public Component getBackbuffer() {
        return jfxPanel;
    }

    @Override
    synchronized public void update(boolean synchronous) {
        if (jfxPanel == null) return;
    }

    @Override
    public void onStopped() {
        // TODO: Test animationTimer.stop();
    }

    @Override
    public void onStarted() {
        animationTimer.start();
    }

    private void resizeRendering(int width, int height) {
        renderScene.setWidth(width);
        renderScene.setHeight(height);
        if (height == 0) height = 1;   // prevent divide by zero
        camera.adjustForResolution(width, height);
        gridRenderer.focusCamera(camera);
    }
}
