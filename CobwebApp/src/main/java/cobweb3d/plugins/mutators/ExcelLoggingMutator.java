package cobweb3d.plugins.mutators;

import cobweb3d.impl.stats.excel.BaseStatsProvider;
import org.apache.poi.xssf.usermodel.XSSFSheet;

public interface ExcelLoggingMutator extends AgentMutator {
    String getName();

    void setWorksheet(XSSFSheet worksheet);

    void logData(BaseStatsProvider statsProvider);
}
