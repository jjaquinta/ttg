/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CharTableModel extends AbstractTableModel
{
    public static final int       COL_NAME     = 0;
    public static final int       COL_TITLE    = 1;
    public static final int       COL_UPP      = 2;
    public static final int       COL_AGE      = 3;
    public static final int       COL_SKILL1   = 4;
    public static final int       COL_SKILL2   = 5;
    public static final int       COL_SALARY   = 6;

    private static final String[] mColumnNames = { "Name", "Title", "UPP",
            "Age", "Skill", "Skill", "Salary", };

    private List<CharBean>        mChars;
    private int[]                 mColumns;

    public CharTableModel()
    {
        mChars = new ArrayList<>();
        mColumns = new int[mColumnNames.length];
        for (int i = 0; i < mColumns.length; i++)
            mColumns[i] = i;
    }

    public void setChars(List<CharBean> newList)
    {
        mChars.clear();
        if (newList != null)
            mChars.addAll(newList);
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
        return mChars.size();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int col)
    {
        CharBean ch = mChars.get(row);
        switch (mColumns[col])
        {
            case COL_NAME:
                return ch.getName();
            case COL_TITLE:
                return ch.getTitle();
            case COL_UPP:
                return FormatUtils.int2upp(ch.getUpp());
            case COL_AGE:
                return new Integer(ch.getAge());
            case COL_SKILL1:
                return ch.getBestSkill();
            case COL_SKILL2:
                return ch.getSecondBestSkill();
            case COL_SALARY:
                return FormatUtils.sCurrency(ch.getSalary());
        }
        return null;
    }

    public Class<?> getColumnClass(int col)
    {
        switch (mColumns[col])
        {
            case COL_NAME:
                return String.class;
            case COL_TITLE:
                return String.class;
            case COL_UPP:
                return String.class;
            case COL_AGE:
                return Integer.class;
            case COL_SKILL1:
                return String.class;
            case COL_SKILL2:
                return String.class;
            case COL_SALARY:
                return String.class;
        }
        return super.getColumnClass(col);
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
    public List<CharBean> getChars()
    {
        return mChars;
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

    public int[] getColumns()
    {
        return mColumns;
    }

    public void setColumns(int[] columns)
    {
        mColumns = columns;
        fireTableStructureChanged();
    }
}
