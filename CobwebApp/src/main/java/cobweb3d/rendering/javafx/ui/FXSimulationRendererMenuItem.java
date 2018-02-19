package cobweb3d.rendering.javafx.ui;

import cobweb3d.rendering.ISimulationRendererMenuItem;
import cobweb3d.rendering.javafx.FXSimulationRenderer;
import util.swing.SimpleAction;

import javax.swing.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class FXSimulationRendererMenuItem implements ISimulationRendererMenuItem {

    private FXSimulationRenderer fxSimulationRenderer;
    private List<JMenuItem> menuItemList;
    private JCheckBoxMenuItem toonShadingJMenuItem;
    private JCheckBoxMenuItem outlineRenderingJMenuItem;
    private Action toggleToonshading = new SimpleAction("Toon Shading", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (fxSimulationRenderer != null) fxSimulationRenderer.setToonRendering(selected);
    });
    private Action toggleOutlines = new SimpleAction("Outline Rendering", e -> {
        boolean selected = ((AbstractButton) e.getSource()).isSelected();
        if (fxSimulationRenderer != null) fxSimulationRenderer.setOutlineRendering(selected);
    });

    public FXSimulationRendererMenuItem(FXSimulationRenderer simulationRenderer) {
        this.fxSimulationRenderer = simulationRenderer;
    }

    private void buildMenuItems() {
        menuItemList = new LinkedList<>();
        toonShadingJMenuItem = new JCheckBoxMenuItem(toggleToonshading);
        toonShadingJMenuItem.setState(fxSimulationRenderer == null || fxSimulationRenderer.toonRendering());
        outlineRenderingJMenuItem = new JCheckBoxMenuItem(toggleOutlines);
        outlineRenderingJMenuItem.setState(fxSimulationRenderer == null || fxSimulationRenderer.outlineRendering());
        menuItemList.add(toonShadingJMenuItem);
        menuItemList.add(outlineRenderingJMenuItem);
    }

    @Override
    public Collection<JMenuItem> getJMenuItems() {
        if (menuItemList == null) buildMenuItems();
        return menuItemList;
    }
}
