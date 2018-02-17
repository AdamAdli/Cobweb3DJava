package cobweb3d.plugins.exchange.log;

import cobweb3d.impl.stats.excel.BaseStatsProvider;
import cobweb3d.plugins.exchange.ExchangeParams;
import cobweb3d.plugins.mutators.ExcelLoggingMutator;
import cobweb3d.plugins.states.AgentState;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public class ExchangeLogger implements ExcelLoggingMutator {
    public static final int HEADER_ROW = 0;
    public static final int FIRST_DATA_ROW = 1;
    int nextDataRow = 1;
    XSSFSheet sheet;
    XSSFRow headingRow;

    ExchangeParams params;

    public ExchangeLogger(ExchangeParams params) {
        this.params = params;
    }

    @Override
    public String getName() {
        return "Exchange Data";
    }

    @Override
    public void setWorksheet(XSSFSheet worksheet) {
        sheet = worksheet;
        headingRow = sheet.createRow(HEADER_ROW);
        XSSFCell tickCell = headingRow.createCell(0);
        tickCell.setCellValue("Tick");

        XSSFCell totAgentCell = headingRow.createCell(1);
        totAgentCell.setCellValue("Total Agents");

        XSSFCell totXCell = headingRow.createCell(2);
        totXCell.setCellValue("Total X");

        XSSFCell totYCel = headingRow.createCell(3);
        totYCel.setCellValue("Total Y");

        XSSFCell totUtility = headingRow.createCell(4);
        totUtility.setCellValue("Total Utility");

        XSSFCell avgAgentUtility = headingRow.createCell(5);
        avgAgentUtility.setCellValue("Average Utility");

        int agentCount = params.agentParams.length;
        for (int i = 1; i <= agentCount; i++) {
            XSSFCell cell = headingRow.createCell(i + 5);
            cell.setCellValue("Agent " + i + " Average Utility");
        }
        nextDataRow = FIRST_DATA_ROW;
    }

    @Override
    public void logData(BaseStatsProvider statsProvider) {
        if (sheet == null) return;
        XSSFRow tickRow = sheet.createRow(nextDataRow);

        XSSFCell tickCell = tickRow.createCell(0);
        tickCell.setCellValue(statsProvider.getTime());

        long totAgentCount = statsProvider.getAgentCount();

        XSSFCell totAgentCell = tickRow.createCell(1);
        totAgentCell.setCellValue(totAgentCount);

        float totalX = ExchangeStatTracker.getTotalX(statsProvider);
        float totalY = ExchangeStatTracker.getTotalY(statsProvider);
        float totalU = ExchangeStatTracker.getTotalUtility(statsProvider, params);

        XSSFCell totXCell = tickRow.createCell(2);
        totXCell.setCellValue(totalX);

        XSSFCell totYCel = tickRow.createCell(3);
        totYCel.setCellValue(totalY);

        XSSFCell totUtility = tickRow.createCell(4);
        totUtility.setCellValue(totalU);

        XSSFCell avgAgentUtility = tickRow.createCell(5);
        avgAgentUtility.setCellValue((totalU / (float) totAgentCount));

        int agentCount = params.agentParams.length;
        for (int i = 0; i < agentCount; i++) {
            long typeAgentCount = statsProvider.countAgents(i);
            XSSFCell cell = tickRow.createCell(i + 6);
            if (typeAgentCount == 0) cell.setCellValue("0");
            else {
                float typeU = ExchangeStatTracker.getUtilityForAgent(statsProvider, params, i);
                cell.setCellValue(typeU / (float) typeAgentCount);
            }
        }
        nextDataRow++;
    }

    @Override
    public <T extends AgentState> boolean acceptsState(Class<T> type, T value) {
        return false;
    }
}
