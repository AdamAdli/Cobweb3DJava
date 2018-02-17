package cobweb3d.impl.stats.excel;

import cobweb3d.impl.Simulation;
import cobweb3d.plugins.mutators.ExcelLoggingMutator;
import cobweb3d.ui.UpdatableUI;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelLogger implements UpdatableUI {
    public static final int AUTO_SAVE_TIMER = 100;
    public static final int HEADER_ROW = 0;
    public static final int FIRST_DATA_ROW = 1;
    private final BaseStatsProvider statsProvider;
    int nextDataRow = 1;
    XSSFWorkbook workbook;
    XSSFSheet sheet;
    Map<ExcelLoggingMutator, XSSFSheet> pluginSheets;
    //private FileOutputStream fileOutputStream;
    private File file;
    private int autoSaveCounter = AUTO_SAVE_TIMER;

    public ExcelLogger(Simulation simulation, File file) {
        statsProvider = new BaseStatsProvider(simulation);
        this.file = file;
        initializeWorkbook();
    }

    private void initializeWorkbook() {
        workbook = new XSSFWorkbook();
        pluginSheets = new HashMap<>();
        sheet = workbook.createSheet("COBWEB Base");
        writeHeaders(sheet.createRow(HEADER_ROW));
        nextDataRow = 1;
    }

    public void writeHeaders(XSSFRow headingRow) {
        XSSFCell tickCell = headingRow.createCell(0);
        tickCell.setCellValue("Tick");
        XSSFCell agentCountCell = headingRow.createCell(1);
        agentCountCell.setCellValue("Total Agent Count");
        XSSFCell totalAgentEnergyCell = headingRow.createCell(2);
        totalAgentEnergyCell.setCellValue("Total Agent Energy");
        XSSFCell averageAgentEnergyCell = headingRow.createCell(3);
        averageAgentEnergyCell.setCellValue("Average Agent Energy");

        for (int i = 1; i <= statsProvider.getAgentTypeCount(); i++) {
            XSSFCell i_agentCountCell = headingRow.createCell(4 + ((i - 1) * 3));
            i_agentCountCell.setCellValue("Agent " + i + " Count");
            XSSFCell i_totalAgentEnergyCell = headingRow.createCell(5 + ((i - 1) * 3));
            i_totalAgentEnergyCell.setCellValue("Total Agent " + i + " Energy");
            XSSFCell i_averageAgentEnergyCell = headingRow.createCell(6 + ((i - 1) * 3));
            i_averageAgentEnergyCell.setCellValue("Average Agent " + i + " Energy");
        }

        for (ExcelLoggingMutator excelPlugin : statsProvider.getLoggingPlugins()) {
            XSSFSheet sheet = workbook.getSheet(excelPlugin.getName());
            if (sheet == null) sheet = workbook.createSheet(excelPlugin.getName());
            excelPlugin.setWorksheet(sheet);
        }
    }

    public void writeLogEntry(XSSFRow dataRow) {
        long time = statsProvider.getTime(), agentCount = statsProvider.getAgentCount(),
                totEnergy = statsProvider.countAgentEnergy();

        XSSFCell tickCell = dataRow.createCell(0);
        tickCell.setCellValue(time);
        XSSFCell agentCountCell = dataRow.createCell(1);
        agentCountCell.setCellValue(agentCount);
        XSSFCell totalAgentEnergyCell = dataRow.createCell(2);
        totalAgentEnergyCell.setCellValue(totEnergy);
        XSSFCell averageAgentEnergyCell = dataRow.createCell(3);
        averageAgentEnergyCell.setCellValue(((float) totEnergy / (float) agentCount));

        for (int i = 0; i < statsProvider.getAgentTypeCount(); i++) {
            long i_agentCount = statsProvider.countAgents(i),
                    i_totEnergy = statsProvider.countAgentEnergy(i);

            XSSFCell i_agentCountCell = dataRow.createCell(4 + (i * 3));
            i_agentCountCell.setCellValue(i_agentCount);
            XSSFCell i_totalAgentEnergyCell = dataRow.createCell(5 + (i * 3));
            i_totalAgentEnergyCell.setCellValue(i_totEnergy);
            XSSFCell i_averageAgentEnergyCell = dataRow.createCell(6 + (i * 3));
            i_averageAgentEnergyCell.setCellValue(((float) i_totEnergy / (float) i_agentCount));
        }

        for (ExcelLoggingMutator excelPlugin : statsProvider.getLoggingPlugins()) {
            XSSFSheet sheet = workbook.getSheet(excelPlugin.getName());
            if (sheet == null) {
                sheet = workbook.createSheet(excelPlugin.getName());
                excelPlugin.setWorksheet(sheet);
            }
            excelPlugin.logData(statsProvider);
        }
    }

    public void saveLog() {
    }

    public void dispose() throws IOException {
        if (workbook != null) workbook.close();
    }

    @Override
    public void update(boolean synchronous) {
        if (sheet == null) initializeWorkbook();
        if (sheet != null) {
            if (statsProvider.getTime() == 0) return;
            writeLogEntry(sheet.createRow(nextDataRow));
            nextDataRow++;
            autoSaveCounter--;
            if (autoSaveCounter <= 0) {

                saveLog();
                autoSaveCounter = AUTO_SAVE_TIMER;

            }
        }
    }


    @Override
    public boolean isReadyToUpdate() {
        return true;
    }


    @Override
    public void onStopped() {
        //logStream.flush();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            workbook.write(fileOutputStream);
            workbook.close();
            fileOutputStream.close();
            workbook = new XSSFWorkbook(new FileInputStream(file));
            sheet = workbook.getSheet("COBWEB Base");
        } catch (Exception ex) {

        }
    }

    @Override
    public void onStarted() {
        // Nothing
    }
}
