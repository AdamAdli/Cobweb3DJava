package cobweb3d.ui;

import cobweb3d.core.Simulation;
import cobweb3d.core.SimulationConfig;

public interface AppContext {

    void openFileDialog();
    void openFile(SimulationConfig config, boolean continuation);
    void quitApplication();
}
