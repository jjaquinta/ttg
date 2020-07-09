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
public interface IBodySpecialHandler
{
    public static final int T_NAME    = 0x01;
    public static final int T_NOTES   = 0x02;
    public static final int T_DOCKING = 0x04;
    public static final int T_FUEL    = 0x08;
    public static final int T_REPAIR  = 0x10;

    public boolean isHandlerFor(BodySpecialBean body);
    public int getType();
    public String getNotes(BodySpecialBean body);
    public String getDocking(BodySpecialBean body);
    public String getFuel(BodySpecialBean body);
    public String getRepair(BodySpecialBean body);
}