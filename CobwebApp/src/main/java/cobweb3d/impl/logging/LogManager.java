package cobweb3d.impl.logging;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.logging.strategies.ExcelXSSFSavingStrategy;
import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.ui.UpdatableUI;

import java.io.File;

public class LogManager implements UpdatableUI {
    SmartDataTable coreDataTable;
    private BaseStatsProvider baseStatsProvider;
    private File file;
    private int autoSaveCounter = 100;

    private SavingStrategy savingStrategy = new ExcelXSSFSavingStrategy();

    public LogManager(Simulation simulation, File file) {
        this.file = file;
        coreDataTable = new SmartDataTable("Tick", "Total Agent Count", "Total Agent Energy", "Average Agent Energy");
        baseStatsProvider = new BaseStatsProvider(simulation);
        writeHeaders();
    }

    public void writeHeaders() {
        coreDataTable = new SmartDataTable("Tick", "Total Agent Count", "Total Agent Energy", "Average Agent Energy");
        for (int i = 0; i < baseStatsProvider.getAgentTypeCount(); i++) {
            coreDataTable.addColumn("Agent " + (i + 1) + " Count");
            coreDataTable.addColumn("Total Agent " + (i + 1) + " Energy");
            coreDataTable.addColumn("Average Agent " + (i + 1) + " Energy");
        }

        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            // Validate later?
            plugin.getTableCount();
        }
    }

    public void writeLogEntry(long tick, SmartDataTable.SmartLogRow logRow) {
        long agentCount = baseStatsProvider.getAgentCount(),
                totEnergy = baseStatsProvider.countAgentEnergy();

        logRow.putVal(0, tick);
        logRow.putVal(1, agentCount);
        logRow.putVal(2, totEnergy);
        logRow.putVal(3, ((float) totEnergy / (float) agentCount));

        for (int i = 0; i < baseStatsProvider.getAgentTypeCount(); i++) {
            long i_agentCount = baseStatsProvider.countAgents(i),
                    i_totEnergy = baseStatsProvider.countAgentEnergy(i);
            logRow.putVal(4 + (i * 3), i_agentCount);
            logRow.putVal(5 + (i * 3), i_totEnergy);
            logRow.putVal(6 + (i * 3), ((float) i_totEnergy / (float) i_agentCount));
        }
    }

    @Override
    public void update(boolean synchronous) {
        writeLogEntry(baseStatsProvider.getTime(), coreDataTable.getRow((int) baseStatsProvider.getTime()));
        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            plugin.logData(baseStatsProvider);
        }
        autoSaveCounter--;
        if (autoSaveCounter <= 0) {
            saveLog();
            autoSaveCounter = 100;
        }
    }

    public void saveLog() {
        if (savingStrategy != null) savingStrategy.save(coreDataTable, baseStatsProvider.getDataLoggingPlugins(), file);
    }

    public void dispose() {
    }

    @Override
    public boolean isReadyToUpdate() {
        return true;
    }


    @Override
    public void onStopped() {
        saveLog();
    }

    @Override
    public void onStarted() {
        // Nothing
    }

    public String getLogPath() {
        return file != null ? file.getPath() : "nowhere";
    }
}
