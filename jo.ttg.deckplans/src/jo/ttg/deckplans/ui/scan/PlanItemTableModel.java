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
import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.utils.PCSBeanUtils;

public class PlanItemTableModel implements PropertyChangeListener, TableModel
{
    private static final String[]    mColumnNames = { "Type", "Number",
            "Volume", "Notes", };

    private ShipScanBean             mScan;
    private List<TableModelListener> mListeners;

    public PlanItemTableModel(ShipScanBean scan)
    {
        mScan = scan;
        mListeners = new ArrayList<>();
        // mShip.addPropertyChangeListener("components", this);
        PCSBeanUtils.listen(mScan, "items",
                (ov, nv) -> updateItems());
    }

    public String getColumnName(int col)
    {
        return mColumnNames[col];
    }

    public int getRowCount()
    {
        return mScan.getItems().size();
    }

    public int getColumnCount()
    {
        return mColumnNames.length;
    }

    public Object getValueAt(int row, int col)
    {
        PlanItem obj = getElementAt(row);
        return getColumnValue(obj, col);
    }
    /* (non-Javadoc)
     * @see javax.swing.ListModel#getElementAt(int)
     */
    public PlanItem getElementAt(int row)
    {
        if ((row < 0) || (row >= mScan.getItems().size()))
            return null;
        return mScan.getItems().get(row);
    }

    private Object getColumnValue(PlanItem comp, int col)
    {
        switch (col)
        {
            case 0:
                return ShipSquareBean.NAMES[comp.getType()];
            case 1:
                return comp.getNumber()+"x";
            case 2:
                return FormatUtils.sVolume(comp.getVolume());
            case 3:
                return comp.getNotes();
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
        Collections.sort(mScan.getItems(), new ItemSorter(column, ascending));
    }

    public class ItemSorter implements Comparator<PlanItem>
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

        public int compare(PlanItem comp1, PlanItem comp2)
        {
            int ret = 0;
            switch (mCol)
            {
                case 0:
                    ret = comp1.getType() - comp2.getType();
                    break;
                case 1:
                    ret = comp1.getNumber() - comp2.getNumber();
                    break;
                case 2:
                    ret = (int)Math.signum(comp1.getVolume() - comp2.getVolume());
                    break;
                case 3:
                    ret = comp1.getNotes().compareTo(comp2.getNotes());
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