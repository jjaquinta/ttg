/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body.ext;

import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodySpecialBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodySpaceportHandler implements IBodySpecialHandler
{
    public boolean isHandlerFor(BodySpecialBean body)
    {
        return body.getSubType() == BodySpecialBean.ST_SPACEPORT;
    }

	public BodySpaceportHandler()
	{
		super();
	}

	public int getType()
    {
        return T_NAME|T_NOTES|T_DOCKING|T_FUEL|T_REPAIR;
    }

	public String getNotes(BodySpecialBean body)
    {
		StringBuffer sb = new StringBuffer();
		UPPPorBean port = (UPPPorBean)body.getSpecialInfo();
		sb.append(" "+port.getValueDescription()+".");
        return sb.toString().trim();
    }

	public String getDocking(BodySpecialBean body)
    {
		UPPPorBean port = (UPPPorBean)body.getSpecialInfo();
		if (port.isCanDock())
		    return "You may dock here.";
	    else
	        return "Docking unavailable here.";
    }

	public String getFuel(BodySpecialBean body)
    {
		UPPPorBean port = (UPPPorBean)body.getSpecialInfo();
		if (port.getHasFuel())
			if (port.getHasRefinedFuel())
		        return "Refined and Unrefined fuel available here.";
        	else
        	    return "Unrefined fuel available here.";
	    else
	        return "No fuel available here.";
    }

	public String getRepair(BodySpecialBean body)
    {
		UPPPorBean port = (UPPPorBean)body.getSpecialInfo();
		if (port.isExcellent())
		    return "Ship may be outfitted for new hulls and components here.";
		else if (port.isGood())
		    return "Ship may be outfitted for components here.";
		else
		    return "No repair facilities are available.";
    }
}
