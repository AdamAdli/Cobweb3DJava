package cobweb3d.plugins.mutators;

import cobweb3d.impl.logging.SmartDataTable;
import cobweb3d.impl.stats.excel.BaseStatsProvider;

import java.util.Collection;

public interface DataLoggingMutator extends AgentMutator {
    String getName();

    int getTableCount();

    void logData(BaseStatsProvider statsProvider);

    Collection<SmartDataTable> getTables();
}
