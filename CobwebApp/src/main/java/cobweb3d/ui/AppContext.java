package cobweb3d.ui;

import cobweb3d.impl.SimulationConfig;

public interface AppContext {

    void openFileDialog();

    void openFile(SimulationConfig config, boolean continuation);

    void quitApplication();
}
