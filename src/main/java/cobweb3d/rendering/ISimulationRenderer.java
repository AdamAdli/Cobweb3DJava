package cobweb3d.rendering;

import cobweb3d.core.Simulation;
import cobweb3d.ui.UpdatableUI;

import javax.swing.*;
import java.awt.*;

public interface ISimulationRenderer extends UpdatableUI {

    void bindSimulation(Simulation simulation);

    Component getBackbuffer();
}
