package cobweb3d.plugins.mutators;

import cobweb3d.impl.logging.DataTable;
import cobweb3d.impl.stats.excel.BaseStatsProvider;

public interface DataLoggingMutator extends AgentMutator {
    String getName();

    void setTable(DataTable table);

    void logData(BaseStatsProvider statsProvider);
}
