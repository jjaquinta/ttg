package jo.ttg.lbb.ui.ship5;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.utils.PCSBeanUtils;

class LaunchTubeTableModel  implements PropertyChangeListener, TableModel
{
    private static final String[]    COLUMN_NAMES = { "Tonnage", "Quantity", };

    private List<TableModelListener> mListeners;
    private Ship5Design              mShip;

    public LaunchTubeTableModel(Ship5Design ship)
    {
        mShip = ship;
        mListeners = new ArrayList<>();
        PCSBeanUtils.listen(ship, "launchTubes", (ov,nv) -> updateItems());
    }

    public String getColumnName(int col)
    {
        return COLUMN_NAMES[col];
    }

    public int getRowCount()
    {
        return mShip.getLaunchTubes().size();
    }

    public int getColumnCount()
    {
        return COLUMN_NAMES.length;
    }

    public Object getValueAt(int row, int col)
    {
        Ship5Design.Ship5DesignLaunchTube obj = getElementAt(row);
        return getColumnValue(obj, col);
    }
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public Ship5Design.Ship5DesignLaunchTube getElementAt(int row)
    {
        if ((row < 0) || (row >= mShip.getLaunchTubes().size()))
            return null;
        return mShip.getLaunchTubes().get(row);
    }

    private Object getColumnValue(Ship5Design.Ship5DesignLaunchTube comp, int col)
    {
        switch (col)
        {
            case 0:
                return comp.getCapacity();
            case 1:
                return comp.getQuantity();
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
        Collections.sort(mShip.getLaunchTubes(), new ItemSorter(column, ascending));
    }

    public class ItemSorter implements Comparator<Ship5Design.Ship5DesignLaunchTube>
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

        public int compare(Ship5Design.Ship5DesignLaunchTube comp1, Ship5Design.Ship5DesignLaunchTube comp2)
        {
            int ret = 0;
            switch (mCol)
            {
                case 0:
                    ret = comp1.getCapacity() - comp2.getCapacity();
                    break;
                case 1:
                    ret = comp1.getQuantity() - comp2.getQuantity();
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