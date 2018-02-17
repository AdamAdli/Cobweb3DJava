package cobweb3d.impl.logging;

import cobweb3d.impl.Simulation;
import cobweb3d.impl.stats.StatsLogger;
import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.impl.stats.excel.ExcelLogger;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import cobweb3d.ui.UpdatableUI;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class LogManager implements UpdatableUI {
    public Map<Class, DataTable> pluginDataTables = new HashMap<>();
    DataTable coreDataTable;
    private String logFilePath = null;
    private StatsLogger statsLogger;
    private ExcelLogger excelLogger;
    private BaseStatsProvider baseStatsProvider;

    public LogManager(Simulation simulation) {
        pluginDataTables = new HashMap<>();
        coreDataTable = new DataTable();
        baseStatsProvider = new BaseStatsProvider(simulation);
    }

    public void setLog(String path) {
        logFilePath = path;
    }

    public void writeHeaders(DataTable.LogRow headerRow) {
        headerRow.putVal(0, "Tick");
        headerRow.putVal(1, "Total Agent Count");
        headerRow.putVal(2, "Total Agent Energy");
        headerRow.putVal(3, "Average Agent Energy");

        for (int i = 1; i <= baseStatsProvider.getAgentTypeCount(); i++) {
            headerRow.putVal(4 + ((i - 1) * 3), "Agent " + i + " Count");
            headerRow.putVal(5 + ((i - 1) * 3), "Total Agent " + i + " Energy");
            headerRow.putVal(6 + ((i - 1) * 3), "Average Agent " + i + " Energy");
        }

        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            if (!pluginDataTables.containsKey(plugin.getClass())) {
                DataTable table = new DataTable();
                pluginDataTables.put(plugin.getClass(), table);
                plugin.setTable(table);
            }
        }
    }

    public void writeLogEntry(long tick, DataTable.LogRow logRow) {
        long agentCount = baseStatsProvider.getAgentCount(),
                totEnergy = baseStatsProvider.countAgentEnergy();

        logRow.putVal(0, tick);
        logRow.putVal(1, agentCount);
        logRow.putVal(2, totEnergy);
        logRow.putVal(3, ((float) totEnergy / (float) agentCount));

        for (int i = 1; i <= baseStatsProvider.getAgentTypeCount(); i++) {
            long i_agentCount = baseStatsProvider.countAgents(i),
                    i_totEnergy = baseStatsProvider.countAgentEnergy(i);
            logRow.putVal(4 + ((i - 1) * 3), i_agentCount);
            logRow.putVal(5 + ((i - 1) * 3), i_totEnergy);
            logRow.putVal(6 + ((i - 1) * 3), ((float) i_totEnergy / (float) i_agentCount));
        }
    }

    @Override
    public void update(boolean synchronous) {
        writeLogEntry(baseStatsProvider.getTime(), coreDataTable.getRow((int) baseStatsProvider.getTime()));
        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            if (!pluginDataTables.containsKey(plugin.getClass())) {
                DataTable table = new DataTable();
                pluginDataTables.put(plugin.getClass(), table);
                plugin.logData(baseStatsProvider);
            }
            plugin.logData(baseStatsProvider);
        }
    }

    public void saveData() {
        if (logFilePath != null) {

        }
    }

    public int writeRow(PrintWriter printWriter, DataTable.LogRow row, int offset) {
        Set<Integer> col = new TreeSet<>(row.cells.keySet());
        int lastCol = offset;
        for (int i : col) {
            for (int j = 0; j < (i + offset) - lastCol; j++)
                printWriter.write(',');
            printWriter.write(row.cells.get(i).value);
            lastCol = i + offset;
        }
        return lastCol;
    }

    public void saveData(PrintWriter printWriter) {
        int offset = writeRow(printWriter, coreDataTable.headerRow, 0);
        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            offset = writeRow(printWriter, pluginDataTables.get(plugin.getClass()).headerRow, offset);
        }


        Set<Integer> col = new TreeSet<>(coreDataTable.headerRow.cells.keySet());
        int lastCol = 0;
        for (int i : col) {
            for (int j = 0; j < i - lastCol; j++)
                printWriter.write(',');
            printWriter.write(coreDataTable.headerRow.cells.get(i).value);
            lastCol = i;
        }
        for (DataLoggingMutator plugin : baseStatsProvider.getDataLoggingPlugins()) {
            saveData(printWriter, pluginDataTables.get(plugin.getClass()), lastCol);
        }

    }

    public void saveData(PrintWriter printWriter, DataTable table, int offset) {
        Set<Integer> col = new TreeSet<>(table.headerRow.cells.keySet());
        int lastCol = offset;
        for (int i : col) {
            for (int j = 0; j < (i + offset) - lastCol; j++)
                printWriter.write(',');
            printWriter.write(table.headerRow.cells.get(i).value);
            lastCol = i + offset;
        }

    }

    @Override
    public void onStopped() {

    }

    @Override
    public void onStarted() {

    }
}
