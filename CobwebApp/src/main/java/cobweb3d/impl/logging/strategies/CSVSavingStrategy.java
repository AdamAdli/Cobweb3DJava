package cobweb3d.impl.logging.strategies;

import cobweb3d.impl.logging.SavingStrategy;
import cobweb3d.impl.logging.SmartDataTable;
import cobweb3d.plugins.mutators.DataLoggingMutator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.*;

public class CSVSavingStrategy implements SavingStrategy {

    private File lastFile;
    private int lastSavedTick = 0;

    @Override
    public int save(SmartDataTable coreData, Collection<DataLoggingMutator> plugins, File file) {
        if (file == null) return 0;
        if (lastFile == null || (!lastFile.equals(file) && !lastFile.getAbsolutePath().equals(file.getAbsolutePath())))
            lastSavedTick = 0;
        try {
            PrintWriter printWriter = new PrintWriter(new FileOutputStream(file, true));
            saveDataToCSV(printWriter, coreData, plugins);
            printWriter.flush();
            printWriter.close();
        } catch (Exception ex) {
            System.err.println("Failed saving log to CSV.");
        }
        lastFile = file;
        return this.lastSavedTick;
    }

    public void saveDataToCSV(PrintWriter printWriter, SmartDataTable coreData, Collection<DataLoggingMutator> plugins) {
        if (lastSavedTick == 0) {
            Iterator<Map.Entry<Integer, String>> iterator = coreData.columnInts.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, String> pairs = iterator.next();
                printWriter.append(pairs.getValue());
                printWriter.append(',');
            }
            for (DataLoggingMutator plugin : plugins) {
                for (SmartDataTable smartDataTable : plugin.getTables()) {
                    iterator = smartDataTable.columnInts.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<Integer, String> pairs = iterator.next();
                        printWriter.append(pairs.getValue());
                        if (iterator.hasNext()) printWriter.append(',');
                    }
                }
            }
            printWriter.append("\r\n");
        }

        int maxRows = coreData.rowMap.size();
        for (DataLoggingMutator plugin : plugins) {
            for (SmartDataTable smartDataTable : plugin.getTables()) {
                int count = smartDataTable.rowMap.size();
                if (count > maxRows) maxRows = count;
            }
        }
        for (int r = lastSavedTick; r < maxRows; r++) {
            printRowToCSV(printWriter, coreData.rowMap.get(r));
            printWriter.append(',');

            Iterator<DataLoggingMutator> dataLoggingMutatorIterator = plugins.iterator();
            while (dataLoggingMutatorIterator.hasNext()) {
                Iterator<SmartDataTable> tableIterator = dataLoggingMutatorIterator.next().getTables().iterator();
                while (tableIterator.hasNext()) {
                    printRowToCSV(printWriter, tableIterator.next().rowMap.get(r));
                    if (tableIterator.hasNext() || dataLoggingMutatorIterator.hasNext()) printWriter.append(',');
                }
            }

            printWriter.append("\r\n");
        }
        this.lastSavedTick = maxRows;
    }

    public void printRowToCSV(PrintWriter printWriter, SmartDataTable.SmartLogRow row) {
        if (row == null) return;
        Set<Integer> col = new TreeSet<>(row.cells.keySet());
        int lastCol = 0;
        for (int i : col) {
            for (int j = 0; j < i - lastCol; j++)
                printWriter.append(',');
            printWriter.append(row.cells.get(i).value);
            lastCol = i;
        }
    }
}
