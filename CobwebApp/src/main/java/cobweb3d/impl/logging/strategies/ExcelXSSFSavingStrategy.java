package cobweb3d.impl.logging.strategies;

import cobweb3d.impl.logging.SavingStrategy;
import cobweb3d.impl.logging.SmartDataTable;
import cobweb3d.plugins.mutators.DataLoggingMutator;
import org.apache.poi.EmptyFileException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Collection;

public class ExcelXSSFSavingStrategy implements SavingStrategy {
    XSSFWorkbook workbook;
    private File lastFile;
    private int lastSavedTick = 0;

    @Override
    public int save(SmartDataTable coreData, Collection<DataLoggingMutator> plugins, File file) {
        if (file == null) return 0;
        if (lastFile == null || (!lastFile.equals(file) && !lastFile.getAbsolutePath().equals(file.getAbsolutePath())))
            lastSavedTick = 0;
        try {
            try {
                workbook = new XSSFWorkbook(new FileInputStream(file));
            } catch (EmptyFileException ex2) {
                workbook = new XSSFWorkbook();
            }
            saveDataToExcelSheets(coreData, plugins);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();

        } catch (Exception ex) {
            System.err.println("Failed saving log to XLSL.");
        }
        lastFile = file;
        return this.lastSavedTick;
    }

    public void saveDataToExcelSheets(SmartDataTable coreData, Collection<DataLoggingMutator> plugins) {
        saveDataToExcelSheet("COBWEB base", coreData);
        for (DataLoggingMutator plugin : plugins) {
            int i = 1;
            for (SmartDataTable smartDataTable : plugin.getTables()) {
                if (i >= 2) saveDataToExcelSheet(plugin.getName() + " " + i, smartDataTable);
                else saveDataToExcelSheet(plugin.getName(), smartDataTable);
                i++;
            }
        }
    }

    public void saveDataToExcelSheet(String title, SmartDataTable table) {
        XSSFSheet sheet = workbook.getSheet(title);
        if (sheet == null) sheet = workbook.createSheet(title);
        XSSFRow headingRow = sheet.getRow(0);
        if (headingRow == null) headingRow = sheet.createRow(0);
        for (int i : table.columnInts.keySet()) {
            headingRow.createCell(i).setCellValue(table.columnInts.get(i));
        }

        for (int r = 0; r < table.rowMap.size(); r++) {
            XSSFRow row = sheet.createRow(1 + r);
            SmartDataTable.SmartLogRow logRow = table.rowMap.get(r);
            for (int c : logRow.cells.keySet()) {
                row.createCell(c).setCellValue(logRow.cells.get(c).value);
            }
        }
    }
}