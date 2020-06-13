/*
 * Created on Jan 20, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.FormattedNumber;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.CargoLogic;
import jo.ttg.utils.URIUtils;;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CargoTableModel extends AbstractTableModel
{
    public static final int       COL_ORIGIN      = 0;
    public static final int       COL_DESTINATION = 1;
    public static final int       COL_TYPE        = 3;
    public static final int       COL_NAME        = 2;
    public static final int       COL_WARNINGS    = 4;
    public static final int       COL_LEGALITY    = 5;
    public static final int       COL_BESTBY      = 6;
    public static final int       COL_SIZE        = 7;
    public static final int       COL_TOBUY       = 8;
    public static final int       COL_TOSELL      = 9;

    private static final String[] mColumnNames    = { "Origin", "Destination",
            "Name", "Type", "Warnings", "Legality", "Best By", "Size", "To Buy",
            "To Sell", };

    private List<CargoBean>       mCargos;
    private String                mAt;
    private DateBean              mNow;
    private IGenScheme            mScheme;
    private int[]                 mColumns;

    public CargoTableModel()
    {
        mCargos = new ArrayList<>();
        mColumns = new int[mColumnNames.length];
        for (int i = 0; i < mColumns.length; i++)
            mColumns[i] = i;
    }

    public void setCargos(List<CargoBean> newList)
    {
        mCargos.clear();
        if (newList != null)
            mCargos.addAll(newList);
        fireTableStructureChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return mColumns.length;
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return mCargos.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col)
    {
        CargoBean cargo = mCargos.get(row);
        switch (mColumns[col])
        {
            case COL_ORIGIN:
                return URIUtils.extractName(cargo.getOrigin());
            case COL_DESTINATION:
                if (cargo.getDestination() != null)
                    return URIUtils.extractName(cargo.getDestination());
                return "-";
            case COL_TYPE:
                return CargoLogic.getTypeText(cargo);
            case COL_NAME:
                return cargo.getName();
            case COL_WARNINGS:
                return cargo.getWarnings();
            case COL_LEGALITY:
                if (cargo.getLegality() == 0)
                    return "";
                return new Integer(cargo.getLegality());
            case COL_BESTBY:
                return CargoLogic.getBestByText(cargo);
            case COL_SIZE:
            {
                double v = cargo.getQuantity();
                return new FormattedNumber(v, FormatUtils.sTons(v));
            }
            case COL_TOBUY:
            {
                double v = CargoLogic.purchasePrice(cargo, mScheme);
                return new FormattedNumber(v, FormatUtils.sCurrency(v));
            }
            case COL_TOSELL:
            {
                double v = CargoLogic.salePrice(cargo, mAt, mNow, mScheme);
                return new FormattedNumber(v, FormatUtils.sCurrency(v));
            }
        }
        return null;
    }

    public Class<?> getColumnClass(int col)
    {
        switch (mColumns[col])
        {
            case COL_ORIGIN:
                return String.class;
            case COL_DESTINATION:
                return String.class;
            case COL_TYPE:
                return String.class;
            case COL_NAME:
                return String.class;
            case COL_WARNINGS:
                return String.class;
            case COL_LEGALITY:
                return Integer.class;
            case COL_BESTBY:
                return String.class;
            case COL_SIZE:
                return FormattedNumber.class;
            case COL_TOBUY:
                return FormattedNumber.class;
            case COL_TOSELL:
                return FormattedNumber.class;
        }
        return super.getColumnClass(col);
    }

    /**
     * @return
     */
    public String getAt()
    {
        return mAt;
    }

    /**
     * @return
     */
    public DateBean getNow()
    {
        return mNow;
    }

    /**
     * @param bean
     */
    public void setAt(String uri)
    {
        mAt = uri;
        fireTableStructureChanged();
    }

    /**
     * @param bean
     */
    public void setNow(DateBean bean)
    {
        mNow = bean;
        fireTableStructureChanged();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getColumnName(int)
     */
    public String getColumnName(int col)
    {
        return mColumnNames[mColumns[col]];
    }

    /**
     * @return
     */
    public List<CargoBean> getCargos()
    {
        return mCargos;
    }

    public IGenScheme getScheme()
    {
        return mScheme;
    }

    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

    public void removeColumn(int colType)
    {
        for (int i = 0; i < mColumns.length; i++)
            if (mColumns[i] == colType)
            {
                int[] newCols = new int[mColumns.length - 1];
                System.arraycopy(mColumns, 0, newCols, 0, i);
                System.arraycopy(mColumns, i + 1, newCols, i,
                        mColumns.length - i - 1);
                mColumns = newCols;
                fireTableStructureChanged();
                return;
            }
    }

    public void addColumn(int colType)
    {
        int[] newCols = new int[mColumns.length + 1];
        System.arraycopy(mColumns, 0, newCols, 0, mColumns.length);
        newCols[mColumns.length] = colType;
        mColumns = newCols;
        fireTableStructureChanged();
    }

    protected int[] getColumns()
    {
        return mColumns;
    }
}
