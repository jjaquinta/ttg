package jo.ttg.deckplans.ui.scan;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.deckplans.logic.LibraryLogic;

public class LibEntryTableModel implements PropertyChangeListener, TableModel
{
    private static final String[]    mColumnNames = { "Title", "Tech",
            "Volume", "Man", "Jump", };

    private List<TableModelListener> mListeners;

    public LibEntryTableModel()
    {
        mListeners = new ArrayList<>();
    }

    public String getColumnName(int col)
    {
        return mColumnNames[col];
    }

    public int getRowCount()
    {
        return LibraryLogic.getLibraryEntries().size();
    }

    public int getColumnCount()
    {
        return mColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        LibEntryBean obj = getElementAt(row);
        return getColumnValue(obj, col);
    }
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public LibEntryBean getElementAt(int row)
    {
        if ((row < 0) || (row >= LibraryLogic.getLibraryEntries().size()))
            return null;
        return LibraryLogic.getLibraryEntries().get(row);
    }

    private Object getColumnValue(LibEntryBean comp, int col)
    {
        switch (col)
        {
            case 0:
                return comp.getTitle();
            case 1:
                return comp.getTechLevel();
            case 2:
                return FormatUtils.sVolume(comp.getDisplacement());
            case 3:
                return comp.getManeuver();
            case 4:
                return comp.getJump();
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
        Collections.sort(LibraryLogic.getLibraryEntries(), new ItemSorter(column, ascending));
    }

    public class ItemSorter implements Comparator<LibEntryBean>
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

        public int compare(LibEntryBean comp1, LibEntryBean comp2)
        {
            int ret = 0;
            switch (mCol)
            {
                case 0:
                    ret = comp1.getTitle().compareTo(comp2.getTitle());
                    break;
                case 1:
                    ret = comp1.getTechLevel() - comp2.getTechLevel();
                    break;
                case 2:
                    ret = comp1.getDisplacement() - comp2.getDisplacement();
                    break;
                case 3:
                    ret = comp1.getManeuver() - comp2.getManeuver();
                    break;
                case 4:
                    ret = comp1.getJump() - comp2.getJump();
                    break;
            }
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
}