package cobweb3d;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.logging.AutoSavingLogManager;
import cobweb3d.impl.logging.LogManager;
import cobweb3d.impl.stats.StatsLogger;
import cobweb3d.impl.stats.excel.ExcelLogger;
import cobweb3d.ui.UpdatableUI;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

public class SimulationRunnerBase implements SimulationRunner, SimulationRunner.LoggingSimulationRunner {

    protected Simulation simulation;

    protected volatile boolean running = false;

    private long tickAutoStop = 0;

    private ExcelLogger statsLogger = null;
    private LogManager logManager = null;
    private Set<UpdatableUI> uiComponents = new HashSet<>();

    public SimulationRunnerBase(Simulation simulation) {
        this.simulation = simulation;
    }

    @Override
    public void step() {
        if (isRunning()) {
            stop();
        } else {
            simulation.step();
            updateUI(true);
        }
    }

    @Override
    public void run() {
        if (running)
            throw new IllegalStateException("Already running!");

        running = true;
        notifyStarted();

        System.out.println(String.format(
				"Running '%1$s' for %2$d steps. Log: %3$s",
				simulation.simulationConfig.fileName,
				getAutoStopTime(),
                logManager == null ? "No" : "Yes"));

        long increment = getAutoStopTime() / 100;
        if (increment > 1000)
            increment = 1000;
        if (increment == 0)
            increment = 1000;

        while (isRunning()) {
            simulation.step();
            updateUI(true);

            long time = getSimulation().getTime();
            if (time % increment == 0) {
                if (getAutoStopTime() != 0) {
                    System.out.println(String.format(
                            "Step: %1$d / %2$d (%3$d%%)",
                            time,
                            getAutoStopTime(),
                            100 * time / getAutoStopTime()
                    ));
                } else {
                    System.out.println(String.format(
                            "Step: %1$d",
                            time
                    ));
                }
            }

            // Stop at target time
            if (getAutoStopTime() != 0 && simulation.getTime() >= getAutoStopTime()) {
                stop();
            }
        }
        System.out.println("Done!");
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop() {
        running = false;

        notifyStopped();
    }

    protected void notifyStarted() {
        for (UpdatableUI updatableUI : new LinkedList<UpdatableUI>(uiComponents)) {
            updatableUI.onStarted();
        }
    }

    protected void notifyStopped() {
        for (UpdatableUI updatableUI : new LinkedList<UpdatableUI>(uiComponents)) {
            updatableUI.onStopped();
        }
    }

    /**
     * Sets log Writer for the simulation.
     * null to disable.
     *
     * @param writer where to write the log.
     * @see StatsLogger
     */
    public void setLog(Writer writer) {
        /* TODO
        if (statsLogger != null) {
            statsLogger.dispose();
            removeUIComponent(statsLogger);
        }

        if (writer != null) {
            statsLogger = new StatsLogger(writer, simulation);
            addUIComponent(statsLogger);
        }*/
    }

    public void setLogManager(String path) {
        if (logManager != null) {
            if (logManager instanceof AutoSavingLogManager) ((AutoSavingLogManager) logManager).saveLog();
            removeUIComponent(logManager);
            logManager = null;
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStopped();
                }
            }
        }

        if (path != null) {
            logManager = new AutoSavingLogManager(simulation, new File(path));
            addUIComponent(logManager);
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStarted();
                }
            }
        }
    }

    public void setExcelLog(String path) {
        if (statsLogger != null) {
            try {
                statsLogger.saveLog();
                statsLogger.dispose();
            } catch (IOException ex) {

            }
            removeUIComponent(statsLogger);
            statsLogger = null;
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStopped();
                }
            }
        }

        if (path != null) {
            statsLogger = new ExcelLogger(simulation, new File(path));
            addUIComponent(statsLogger);
            for (UpdatableUI loggingUI : uiComponents) {
                if (loggingUI instanceof UpdatableUI.UpdateableLoggingUI) {
                    ((UpdatableUI.UpdateableLoggingUI) loggingUI).onLogStarted();
                }
            }
        }
    }

    /* TODO
    /**
	 * Writes simulation report to writer.
	 * @param writer where to write report.
	 * @see AgentReporter

	public void report(Writer writer) {
		if (simulation != null) {
			try {
				AgentReporter.report(writer, simulation);
				writer.flush();
				writer.close();
			} catch (IOException ex) {
				throw new UserInputException("Cannot save report file", ex);
			}
		}
	}
    */

    protected void updateUI(boolean synchronous) {
        for (UpdatableUI client : new LinkedList<UpdatableUI>(uiComponents)) {
            if (synchronous || client.isReadyToUpdate()) {
                client.update(synchronous);
            }
        }
    }

    @Override
    public void addUIComponent(UpdatableUI ui) {
        uiComponents.add(ui);
        ui.update(true);
    }

    @Override
    public void removeUIComponent(UpdatableUI ui) {
        uiComponents.remove(ui);
    }

    @Override
    public Simulation getSimulation() {
        return simulation;
    }

    public long getAutoStopTime() {
        return tickAutoStop;
    }

    @Override
    public void setAutoStopTime(long t) {
        tickAutoStop = t;
    }

    @Override
    public boolean isLogging() {
        return logManager != null;//statsLogger != null;
    }

    @Override
    public String getLoggingStatus() {
        return logManager != null ? logManager.getLoggingStatus() : "Not Logging";
    }
}
