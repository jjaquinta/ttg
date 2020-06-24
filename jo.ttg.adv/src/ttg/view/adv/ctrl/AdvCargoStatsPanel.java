/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.adv.ctrl;

import javax.swing.JLabel;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.ctrl.CargoStatsPanel;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import ttg.beans.adv.AdvCargoBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AdvCargoStatsPanel extends CargoStatsPanel
{
    private JLabel	mDeliveryDate;

	public AdvCargoStatsPanel()
	{
	    super();
	}

	/**
	 * 
	 */
	protected void initInstantiate()
	{
	    super.initInstantiate();
	    mDeliveryDate = new JLabel();
	}


	/**
	 * 
	 */
	protected void initLink()
	{
	    super.initLink();
	}

	/**
	 * 
	 */
	protected void initLayout()
	{
	    super.initLayout();
		add("1,+", new JLabel("Delivery By:"));
		add("+,. fill=h", mDeliveryDate);
	}

	/**
	 * @param stats
	 */
	public void setCargo(CargoBean cargo)
	{
	    super.setCargo(cargo);	    
		if (cargo == null)
		{
			mDeliveryDate.setText("");
		}
		else
		{
		    DateBean d = ((AdvCargoBean)cargo).getDelivered();
		    if (d.getMinutes() > 0)
		        mDeliveryDate.setText(FormatUtils.formatDateTime(d));
		    else
		        mDeliveryDate.setText("-");
		}
	}
}
