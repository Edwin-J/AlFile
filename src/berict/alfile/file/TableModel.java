package berict.alfile.file;

import berict.alfile.Main;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;


public class TableModel extends AbstractTableModel {
    private Object[][] data = new Object[0][3];

    public static ArrayList<FileTableItem> dataList = new ArrayList<>();

    // example from @link http://www.java2s.com/Code/Java/Swing-JFC/TablewithacustomTableModel.htm
    private String[] columnNames = {
            "Type",
            "Original File names",
            "Changed File names",
            "Location"
    };

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col) {
//        Main.log("getValueAt(" + row + ", " + col + ")");
        return data[row][col];
    }

    public void add(FileTableItem item) {
        dataList.add(item);
        updateFromDataList();
        Main.log("Added value " + item.getFile().getFullPath());
    }

    public void set(int row, FileTableItem item) {
        dataList.set(row, item);
        updateFromDataList();
    }

    public void remove(int row) {
        dataList.remove(row);
        updateFromDataList();
    }

    public void update() {
        updateFromDataList();
    }

    public int size() {
        return dataList.size();
    }

    public FileTableItem get(int row) {
        return dataList.get(row);
    }

    public boolean isModified(int row[]) {
        for (int i : row) {
            if (dataList.get(i).isModified()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDuplicate() {
        boolean duplicate = false;
        for (FileTableItem item : dataList) {
            if (item.hasDuplicate()) {
                duplicate = true;
                break;
            }
        }
        return duplicate;
    }

    public boolean hasDuplicate(int row[]) {
        boolean duplicate = false;
        for (int index : row) {
            if (dataList.get(index).hasDuplicate()) {
                duplicate = true;
                break;
            }
        }
        return duplicate;
    }

    public boolean hasDuplicate(int row) {
        return dataList.get(row).hasDuplicate();
    }

    public int search(String path) {
        for (int i = 0; i < dataList.size(); i++) {
            FileTableItem item = dataList.get(i);
            if (item.getFile().getFullPath().equals(path)) {
                return i;
            }
        }
        return -1;
    }

    private void updateFromDataList() {
        // update the data
        data = getData();
        fireTableDataChanged();
    }

    public int getModifiedCount() {
        int modifiedCount = 0;
        for (FileTableItem item : dataList) {
            if (item.isModified()) {
                modifiedCount++;
            }
        }
        return modifiedCount;
    }

    private Object[][] getData() {
        Object object[][] = new Object[dataList.size()][3];

        for (int i = 0; i < dataList.size(); i++) {
            FileTableItem item = dataList.get(i);
            object[i] = item.toObjects();
        }

        return object;
    }

    /*
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column == 2;
    }

    public void setValueAt(Object value, int row, int col) {
        Main.log("Setting value at " + row + "," + col
                    + " to " + value + " (an instance of "
                    + value.getClass() + ")");

        data[row][col] = value;
        fireTableCellUpdated(row, col);

        Main.log("New value of data:");
    }

}
