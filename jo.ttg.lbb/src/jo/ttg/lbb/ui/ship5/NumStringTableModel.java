package jo.ttg.lbb.ui.ship5;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

class NumStringTableModel  implements PropertyChangeListener, TableModel
{
    private String[]                mColumnNames;
    private Map<String, ?>    mData;
    private Object[][]              mTuples;

    private List<TableModelListener> mListeners;

    public NumStringTableModel(String[] columnNames, Map<String,?> data)
    {
        mColumnNames = columnNames;
        mListeners = new ArrayList<>();
        setData(data);
    }

    public String getColumnName(int col)
    {
        return mColumnNames[col];
    }

    public int getRowCount()
    {
        return mTuples.length;
    }

    public int getColumnCount()
    {
        return mColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        return getColumnValue(mTuples[row], col);
    }
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Object[] getElementAt(int row)
    {
        if ((row < 0) || (row >= mTuples.length))
            return null;
        return mTuples[row];
    }

    private Object getColumnValue(Object[] comp, int col)
    {
        switch (col)
        {
            case 0:
                return comp[col].toString();
            case 1:
                return comp[col].toString();
        }
        return null;
    }

    public void propertyChange(PropertyChangeEvent arg0)
    {
        fireTableStructureChanged(getRowCount());
        updateItems();
    }

    public void sortByColumn(int column, boolean ascending)
    {
        Arrays.sort(mTuples, new ItemSorter(column, ascending));
    }

    public class ItemSorter implements Comparator<Object[]>
    {
        private int     mCol;
        private boolean mDir;

        public ItemSorter(int col, boolean dir)
        {
            mCol = col;
            mDir = dir;
        }

        /**
         *  
         */

        public int compare(Object[] comp1, Object[] comp2)
        {
            int ret = 0;
            Object v1 = comp1[mCol];
            Object v2 = comp2[mCol];
            if ((v1 instanceof Number))
                ret = (int)Math.signum(((Number)v1).doubleValue() - ((Number)v2).doubleValue());
            else
                ret = v1.toString().compareTo(v2.toString());
            if (!mDir)
                ret = -ret;
            return ret;
        }
    }

    protected void updateItems()
    {
        int rows = getRowCount();
        fireTableStructureChanged(rows);
    }

    private void fireTableStructureChanged(int rows)
    {
        if (mListeners == null)
            return;
        TableModelEvent e1 = new TableModelEvent(this, 0, rows,
                TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        TableModelEvent e2 = new TableModelEvent(this, 0, getRowCount(),
                TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        Object[] listeners = mListeners.toArray();
        for (int i = 0; i < listeners.length; i++)
        {
            ((TableModelListener)listeners[i]).tableChanged(e1);
            ((TableModelListener)listeners[i]).tableChanged(e2);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
    {
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnClass(int)
     */
    public Class<?> getColumnClass(int columnIndex)
    {
        if (mTuples.length > 0)
            return mTuples[0][columnIndex].getClass();
        return String.class;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object aValue, int rowIndex, int columnIndex)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableModel#addTableModelListener(javax.swing.event.
     * TableModelListener)
     */
    public void addTableModelListener(TableModelListener l)
    {
        synchronized (mListeners)
        {
            mListeners.add(l);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.
     * TableModelListener)
     */
    public void removeTableModelListener(TableModelListener l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }

    public Map<String, ?> getData()
    {
        return mData;
    }

    public void setData(Map<String, ?> data)
    {
        mData = data;
        mTuples = new Object[mData.size()][2];
        int idx = 0;
        for (String key : mData.keySet())
        {
            mTuples[idx][1] = key;
            mTuples[idx][0] = mData.get(key);
            idx++;
        }
        updateItems();
    }
}