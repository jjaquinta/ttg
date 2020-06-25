/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.ctrl;

import jo.ttg.beans.mw.UPPPorBean;
import ttg.adv.beans.BodySpecialAdvBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodyStarportPanel extends BodyBasePanel
{
	public BodyStarportPanel()
	{
		super();
	}

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getType()
     */
    protected int getType()
    {
        return T_NAME|T_NOTES|T_DOCKING|T_FUEL|T_REPAIR;
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getNotes()
     */
    protected String getNotes()
    {
		StringBuffer sb = new StringBuffer();
        BodySpecialAdvBean body = getBody(); 
		UPPPorBean port = (UPPPorBean)body.getSpecialInfo();
		sb.append(" "+port.getValueDescription()+".");
		sb.append(" "+getDemandProduction());
        return sb.toString().trim();
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getDocking()
     */
    protected String getDocking()
    {
		UPPPorBean port = (UPPPorBean)getBody().getSpecialInfo();
		if (port.isCanDock())
		    return "You may dock here.";
	    else
	        return "Docking unavailable here.";
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getFuel()
     */
    protected String getFuel()
    {
		UPPPorBean port = (UPPPorBean)getBody().getSpecialInfo();
		if (port.getHasFuel())
			if (port.getHasRefinedFuel())
		        return "Refined and Unrefined fuel available here.";
        	else
        	    return "Unrefined fuel available here.";
	    else
	        return "No fuel available here.";
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getRepair()
     */
    protected String getRepair()
    {
		UPPPorBean port = (UPPPorBean)getBody().getSpecialInfo();
		if (port.isExcellent())
		    return "Ship may be outfitted for new hulls and components here.";
		else if (port.isGood())
		    return "Ship may be outfitted for components here.";
		else
		    return "No repair facilities are available.";
    }
}
