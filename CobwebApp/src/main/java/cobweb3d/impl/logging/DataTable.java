package cobweb3d.impl.logging;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DataTable {
    LogRow headerRow;
    Map<Integer, LogRow> rowMap;

    public DataTable() {
        headerRow = new LogRow();
        rowMap = new LinkedHashMap<>();
    }

    public LogRow getRow(int tick) {
        if (rowMap.containsKey(tick)) return rowMap.get(tick);
        else {
            LogRow row = new LogRow();
            rowMap.put(tick, row);
            return row;
        }
    }

    public class LogRow {

        Map<Integer, Cell> cells = new HashMap<>();

        public LogRow() {

        }

        public void putVal(int columnIndex, String value) {
            if (!cells.containsKey(columnIndex)) cells.put(columnIndex, new Cell(value));
            else {
                Cell cell = cells.get(columnIndex);
                cell.value = value;
            }
        }

        public void putVal(int columnIndex, long value) {
            putVal(columnIndex, Long.toString(value));
        }

        public void putVal(int columnIndex, int value) {
            putVal(columnIndex, Integer.toString(value));
        }

        public void putVal(int columnIndex, float value) {
            putVal(columnIndex, Float.toString(value));
        }

        public class Cell {
            String value;

            public Cell(String value) {
                this.value = value;
            }
        }
    }
}
