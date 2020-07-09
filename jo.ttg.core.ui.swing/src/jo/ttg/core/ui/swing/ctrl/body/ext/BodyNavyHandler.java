/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body.ext;

import jo.ttg.beans.sys.BodySpecialBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodyNavyHandler implements IBodySpecialHandler
{
    public boolean isHandlerFor(BodySpecialBean body)
    {
        return body.getSubType() == BodySpecialBean.ST_NAVYBASE;
    }

	public BodyNavyHandler()
	{
		super();
	}

	public int getType()
    {
        return T_NAME|T_DOCKING|T_FUEL;
    }

	public String getNotes(BodySpecialBean body)
    {
        return "";
    }

	public String getDocking(BodySpecialBean body)
    {
	    return "You may dock here.";
    }

	public String getFuel(BodySpecialBean body)
    {
        return "Refined and Unrefined fuel available here.";
    }

	public String getRepair(BodySpecialBean body)
    {
	    return null;
    }
}
