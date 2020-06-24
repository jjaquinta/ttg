/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.adv.ctrl;

import jo.ttg.core.ui.swing.ctrl.CargoTableModel;
import jo.ttg.logic.DateLogic;
import ttg.beans.adv.AdvCargoBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AdvCargoTableModel extends CargoTableModel
{
    public static final int COL_DELIVERED = 10;

    private static final String[] mColumnNames = 
	{
		"Delivered",
	};
	
	public AdvCargoTableModel()
	{
	    super();
	    addColumn(COL_DELIVERED);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col)
	{
		AdvCargoBean ch = (AdvCargoBean)getCargos().get(row);
		switch (getColumns()[col])
		{
			case COL_DELIVERED:
			    if (DateLogic.earlierThan(ch.getDelivered(), getNow()))
			        return "Yes";
			    else
			        return "No";
		}
		return super.getValueAt(row, col);
	}

    public Class getColumnClass(int col)
    {
		switch (getColumns()[col])
		{
			case COL_DELIVERED:
				return String.class;
		}
        return super.getColumnClass(col);
    }

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnName(int)
	 */
	public String getColumnName(int col)
	{
	    int colOff = getColumns()[col];
	    if (colOff >= COL_DELIVERED)
	        return mColumnNames[colOff - COL_DELIVERED];
	    else
	        return super.getColumnName(col);
	}
}
