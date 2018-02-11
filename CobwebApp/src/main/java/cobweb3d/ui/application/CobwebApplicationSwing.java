package cobweb3d.ui.application;

import cobweb3d.ThreadSimulationRunner;
import cobweb3d.impl.Simulation;
import cobweb3d.impl.SimulationConfig;
import cobweb3d.io.Cobweb3Serializer;
import cobweb3d.ui.UpdatableUI;
import cobweb3d.ui.exceptions.UserInputException;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class CobwebApplicationSwing extends CobwebApplicationSwingBase {
    public ThreadSimulationRunner simRunner;

    public CobwebApplicationSwing() {
        simRunner = new ThreadSimulationRunner(new Simulation());
    }

    public CobwebApplicationSwing(ThreadSimulationRunner simulationRunner) {
        simRunner = simulationRunner;
    }

    @Override
    public Component add(Component comp) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(comp);
    }

    @Override
    public Component add(String name, Component comp) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(name, comp);
    }

    @Override
    public Component add(Component comp, int index) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        return super.add(comp, index);
    }

    @Override
    public void add(@NotNull Component comp, Object constraints) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        super.add(comp, constraints);
    }

    @Override
    public void add(Component comp, Object constraints, int index) {
        if (comp instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) comp);
        super.add(comp, constraints, index);
    }

    @Override
    public void add(PopupMenu popup) {
        if (popup instanceof UpdatableUI) simRunner.addUIComponent((UpdatableUI) popup);
        super.add(popup);
    }

    private void updateDynamicUI() {
        // TODO:
        // if (simStatePanel != null) simStatePanel.simulationChanged();
        validate();
    }

    /**
     * Load simulation config.
     *
     * @param config       simulation configuration
     * @param continuation load this as a continuation of the current simulation?
     */
    @Override
    public File openFile(SimulationConfig config, boolean continuation) {
        File file = super.openFile(config, continuation);
        // TODO more organized way to deal with loading simulation configurations
        // TODO create new simRunner when starting new simulation, reuse when modifying
        if (simRunner.isRunning())
            simRunner.stop();
        if (!continuation) {
            simRunner.getSimulation().resetTime();
            simRunner.setLog(null);
        }
        simRunner.getSimulation().load(config);
        updateDynamicUI();
        return file;
    }

    public void saveSimulation(String savePath) {
        File sf = new File(savePath);
        if (sf.isHidden() || (sf.exists() && !sf.canWrite())) {
            JOptionPane.showMessageDialog(
                    this,
                    "Caution:  File \"" + savePath + "\" is NOT allowed to be written to.", "Warning",
                    JOptionPane.WARNING_MESSAGE);
        } else {
            try (OutputStream file = new FileOutputStream(sf)) {
                Cobweb3Serializer serializer = new Cobweb3Serializer();
                serializer.saveConfig(simRunner.getSimulation().simulationConfig, file);
            } catch (IOException ex) {
                throw new UserInputException("Save failed", ex);
            }
        }
    }

    /**
     * Copies the current simulation data being used to a temporary file, which
     * can be modified and saved by the user.
     */
    protected void openCurrentData() {
        String currentData = CURRENT_DATA_FILE_NAME;
        File cf = new File(currentData);
        cf.deleteOnExit();
        try {
            Cobweb3Serializer serializer = new Cobweb3Serializer();
            FileOutputStream outStream = new FileOutputStream(cf);
            serializer.saveConfig(simRunner.getSimulation().simulationConfig, outStream);
            outStream.close();
        } catch (IOException ex) {
            throw new UserInputException("Cannot open config file", ex);
        }
    }

    /**
     * Opens the simulation settings window with the current simulation file
     * data.  The user can modify and save the file here.  If the user tries
     * to overwrite data found in the default data file, a dialog box will be
     * created to tell the user the proper way to create new default data.
     */
    protected void openCurrentFile() {
        if (CURRENT_DATA_FILE_NAME.equals(currentFile)) {
            throw new UserInputException("File not currently saved, use \"Modify Current Data\" instead");
        }
    }
}
