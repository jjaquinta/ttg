/*
 * Created on Jan 23, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.adv.ctrl;

import java.util.List;

import jo.ttg.core.ui.swing.ctrl.CharTableModel;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.utils.URIUtils;
import ttg.beans.adv.PassengerBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PassengerTableModel extends CharTableModel
{
    public static final int COL_PASSAGE = 7;
    public static final int COL_ORIGIN = 8;
    public static final int COL_DESTINATION = 9;
    public static final int COL_BORDED = 10;

    private static final String[] mColumnNames = 
	{
		"Passage", "Origin", "Destination", "Borded",
	};
	
	public PassengerTableModel()
	{
	    super();
	    addColumn(COL_PASSAGE);
	    addColumn(COL_ORIGIN);
	    addColumn(COL_DESTINATION);
	    addColumn(COL_BORDED);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int col)
	{
	    PassengerBean ch = (PassengerBean)getChars().get(row);
		switch (getColumns()[col])
		{
			case COL_PASSAGE:
			    switch (ch.getPassage())
			    {
			        case PassengerBean.PASSAGE_HIGH:
			            return "High";
			        case PassengerBean.PASSAGE_MIDDLE:
			            return "Middle";
			        case PassengerBean.PASSAGE_LOW:
			            return "Low";
			        default:
			            return "-";
			    }
	        case COL_ORIGIN:
	            return URIUtils.extractName(ch.getOrigin());
	        case COL_DESTINATION:
	            return URIUtils.extractName(ch.getDestination());
	        case COL_BORDED:
	            return FormatUtils.formatDate(ch.getBoarded());
		}
		return super.getValueAt(row, col);
	}

    public Class<?> getColumnClass(int col)
    {
		switch (getColumns()[col])
		{
			case COL_PASSAGE:
				return String.class;
			case COL_ORIGIN:
				return String.class;
			case COL_DESTINATION:
				return String.class;
			case COL_BORDED:
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
	    if (colOff >= COL_PASSAGE)
	        return mColumnNames[colOff - COL_PASSAGE];
	    else
	        return super.getColumnName(col);
	}

    public void setPassengers(List<PassengerBean> newList)
    {
        mChars.clear();
        if (newList != null)
            mChars.addAll(newList);
        fireTableStructureChanged();
    }
}
