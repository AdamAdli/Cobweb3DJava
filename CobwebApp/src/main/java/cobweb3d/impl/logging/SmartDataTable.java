package cobweb3d.impl.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class SmartDataTable {
    public SortedMap<Integer, SmartLogRow> rowMap = new TreeMap<>();
    // Map<String, Integer> columns = new HashMap<>();
    public SortedMap<Integer, String> columnInts = new TreeMap<>();

    public SmartDataTable(String... columnNames) {
        rowMap = new TreeMap<>();
        addColumns(columnNames);
    }

    public void addColumns(String... columnNames) {
        int prevSize = columnInts.size();
        for (int i = 0; i < columnNames.length; i++) {
            // this.columns.put(columnNames[i], i + columns.size());
            this.columnInts.put(i + prevSize, columnNames[i]);
            //columnArrList.add(columnNames[i]);
        }
    }

    public void addColumn(String columnName) {
        // this.columns.put(columnName, columns.size());
        this.columnInts.put(columnInts.size(), columnName);
    }

    public SmartLogRow getRow(int tick) {
        if (rowMap.containsKey(tick)) return rowMap.get(tick);
        else {
            SmartLogRow row = new SmartLogRow(null);
            rowMap.put(tick, row);
            return row;
        }
    }

    public class SmartLogRow {
        public Map<String, Integer> columns = new HashMap<>();
        public Map<Integer, Cell> cells = new HashMap<>();

        public SmartLogRow(Map<String, Integer> columns) {
            this.columns = columns;
        }

        public void putVal(String column, String value) {
            if (!cells.containsKey(column)) cells.put(this.columns.get(column), new Cell(value));
            else {
                Cell cell = cells.get(column);
                cell.value = value;
            }
        }

        public void putVal(String columnIndex, long value) {
            putVal(columnIndex, Long.toString(value));
        }

        public void putVal(String columnIndex, int value) {
            putVal(columnIndex, Integer.toString(value));
        }

        public void putVal(String columnIndex, float value) {
            putVal(columnIndex, Float.toString(value));
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
            public String value;

            public Cell(String value) {
                this.value = value;
            }
        }
    }
}