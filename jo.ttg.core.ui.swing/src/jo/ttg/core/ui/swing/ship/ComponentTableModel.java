package jo.ttg.core.ui.swing.ship;

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
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.logic.ComponentLogic;
import jo.util.utils.PCSBeanUtils;

public class ComponentTableModel extends ComponentBaseModel implements
        PropertyChangeListener, TableModel
{
    private static final String[] mColumnNames = { "Name", "Section", "Volume",
            "Power", "Weight", "Price", "TechLevel", "ControlPoints", };

    private ShipBean              mShip;
    private List<TableModelListener>             mListeners;

    public ComponentTableModel(ShipBean ship)
    {
        super(ship.getComponents());
        mShip = ship;
        mListeners = new ArrayList<>();
        //mShip.addPropertyChangeListener("components", this);
        PCSBeanUtils.listen(mShip, "components", (ov,nv) -> updateComponents());
    }

    public String getColumnName(int col)
    {
        return mColumnNames[col];
    }

    public int getRowCount()
    {
        return mComponents.size();
    }

    public int getColumnCount()
    {
        return 8;
    }

    public Object getValueAt(int row, int col)
    {
        ShipComponent obj = getElementAt(row);
        return getColumnValue(obj, col);
    }
    
    private Object getColumnValue(ShipComponent comp, int col)
    {
        switch (col)
        {
            case 0:
                return ComponentLogic.getName(comp);
            case 1:
                return ShipComponent.mSectionDescriptions[ComponentLogic.getSection(comp)];
            case 2:
                return FormatUtils.sVolume(ComponentLogic.getVolume(comp, null));
            case 3:
                return FormatUtils.sPower(ComponentLogic.getPower(comp, null));
            case 4:
                return FormatUtils.sWeight(ComponentLogic.getWeight(comp, null));
            case 5:
                return FormatUtils.sCurrency(ComponentLogic.getPrice(comp, null));
            case 6:
                return String.valueOf(ComponentLogic.getTechLevel(comp));
            case 7:
                return String.valueOf(ComponentLogic.getControlPoints(comp));
        }
        return null;
        
    }

    public void propertyChange(PropertyChangeEvent arg0)
    {
        fireTableStructureChanged(getRowCount());
        updateComponents();
    }

    public void sortByColumn(int column, boolean ascending)
    {
        Collections.sort(mComponents, new CompSorter(column, ascending));
    }

    public class CompSorter implements Comparator<ShipComponent>
    {
        private int     mCol;
        private boolean mDir;

        public CompSorter(int col, boolean dir)
        {
            mCol = col;
            mDir = dir;
        }

        /**
         *  
         */

        public int compare(ShipComponent comp1, ShipComponent comp2)
        {
            int ret = 0;
            switch (mCol)
            {
                case 0:
                    ret = comp1.getName().compareTo(comp2.getName());
                    break;
                case 1:
                    ret = sgn(comp1.getSection() - comp2.getSection());
                    break;
                case 2:
                    ret = sgn(comp1.getVolume() - comp2.getVolume());
                    break;
                case 3:
                    ret = sgn(comp1.getPower() - comp2.getPower());
                    break;
                case 4:
                    ret = sgn(comp1.getWeight() - comp2.getWeight());
                    break;
                case 5:
                    ret = sgn(comp1.getPrice() - comp2.getPrice());
                    break;
                case 6:
                    ret = sgn(comp1.getTechLevel() - comp2.getTechLevel());
                    break;
                case 7:
                    ret = sgn(comp1.getControlPoints()
                            - comp2.getControlPoints());
                    break;
            }
            if (!mDir)
                ret = -ret;
            return ret;
        }

        private int sgn(double d)
        {
            if (d < 0)
                return -1;
            else if (d > 0)
                return 1;
            else
                return 0;
        }

    }

    protected void updateComponents()
    {
        int rows = getRowCount();
        super.updateComponents();
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
     * @see javax.swing.table.TableModel#addTableModelListener(javax.swing.event.TableModelListener)
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
     * @see javax.swing.table.TableModel#removeTableModelListener(javax.swing.event.TableModelListener)
     */
    public void removeTableModelListener(TableModelListener l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }
}