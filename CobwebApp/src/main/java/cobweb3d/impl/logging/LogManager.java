package cobweb3d.impl.logging;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.ui.UpdatableUI;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Set;

public class LogManager implements UpdatableUI {
    private DataTable coreDataTable;
    private BaseStatsProvider baseStatsProvider;
    private int nextDataRow = 0;

    public LogManager(Simulation simulation) {
        coreDataTable = new DataTable("Tick", "Total Agent Count", "Total Agent Energy", "Average Agent Energy");
        baseStatsProvider = new BaseStatsProvider(simulation);
        writeHeaders();
    }

    public Set<DataLoggingMutator> getDataLoggingPlugins() {
        return baseStatsProvider.getDataLoggingPlugins();
    }

    public void writeHeaders() {
        coreDataTable = new DataTable("Tick", "Total Agent Count", "Total Agent Energy", "Average Agent Energy");
        for (int i = 0; i < baseStatsProvider.getAgentTypeCount(); i++) {
            coreDataTable.addColumn("Agent " + (i + 1) + " Count");
            coreDataTable.addColumn("Total Agent " + (i + 1) + " Energy");
            coreDataTable.addColumn("Average Agent " + (i + 1) + " Energy");
        }

        for (DataLoggingMutator plugin : getDataLoggingPlugins()) {
            plugin.getTableCount();
        }
    }

    public void writeLogEntry(long tick, DataTable.SmartLogRow logRow) {
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
        writeLogEntry(baseStatsProvider.getTime(), coreDataTable.getRow(nextDataRow));
        for (DataLoggingMutator plugin : getDataLoggingPlugins()) {
            plugin.logData(baseStatsProvider);
        }
        nextDataRow++;
    }

    public void saveLog(@NotNull File file, @NotNull SavingStrategy savingStrategy) {
        if (savingStrategy != null) savingStrategy.save(coreDataTable, getDataLoggingPlugins(), file);
        else System.err.println("Did not save log: null SavingStategy in LogManager.saveLog()!");
    }

    public String getLoggingStatus() {
        return "Logging to Memory";
    }

    // Updates EVERY frame even when update speed is so fast its async.
    @Override
    public boolean isReadyToUpdate() {
        return true;
    }
}
