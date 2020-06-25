/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view;

import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.core.ui.swing.ctrl.body.IBodyPanelHandler;
import ttg.adv.beans.BodySpecialAdvBean;
import ttg.adv.view.ctrl.BodyLabPanel;
import ttg.adv.view.ctrl.BodyLocalPanel;
import ttg.adv.view.ctrl.BodyNavyPanel;
import ttg.adv.view.ctrl.BodyRefineryPanel;
import ttg.adv.view.ctrl.BodyScoutPanel;
import ttg.adv.view.ctrl.BodySpaceportPanel;
import ttg.adv.view.ctrl.BodyStarportPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvBodyPanelHander implements IBodyPanelHandler
{

    /* (non-Javadoc)
     * @see ttg.view.ctrl.BodyPanelHandler#getBodyPanels(java.util.HashMap)
     */
    public void getBodyPanels(Map<String,JPanel> map)
    {
		map.put("adv.starport", new BodyStarportPanel());
		map.put("adv.spaceport", new BodySpaceportPanel());
		map.put("adv.navy", new BodyNavyPanel());
		map.put("adv.local", new BodyLocalPanel());
		map.put("adv.scout", new BodyScoutPanel());
		map.put("adv.lab", new BodyLabPanel());
		map.put("adv.refinery", new BodyRefineryPanel());
    }

    /* (non-Javadoc)
     * @see ttg.view.ctrl.BodyPanelHandler#handleBody(java.util.HashMap, ttg.beans.sys.BodyBean)
     */
    public String handleBody(Map<String,JPanel> map, BodyBean b)
    {
        if (!(b instanceof BodySpecialAdvBean))
        	return null;
        BodySpecialAdvBean sp = (BodySpecialAdvBean)b;
		switch (sp.getSubType())
		{
		    case BodySpecialBean.ST_STARPORT:
				((BodyStarportPanel)map.get("adv.starport")).setBody(sp);
		    	return "adv.starport";
		    case BodySpecialBean.ST_SPACEPORT:
				((BodySpaceportPanel)map.get("adv.spaceport")).setBody(sp);
				return "adv.spaceport";
		    case BodySpecialBean.ST_LOCALBASE:
		        ((BodyLocalPanel)map.get("adv.local")).setBody(sp);
				return "adv.local";
		    case BodySpecialBean.ST_NAVYBASE:
				((BodyNavyPanel)map.get("adv.navy")).setBody(sp);
				return "adv.navy";
		    case BodySpecialBean.ST_SCOUTBASE:
				((BodyScoutPanel)map.get("adv.scout")).setBody(sp);
				return "adv.scout";
		    case BodySpecialBean.ST_LABBASE:
				((BodyLabPanel)map.get("adv.lab")).setBody(sp);
				return "adv.lab";
		    case BodySpecialBean.ST_REFINERY:
				((BodyRefineryPanel)map.get("adv.refinery")).setBody(sp);
				return "adv.refinery";
		}
		return null;
    }

}
