/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body;

import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IBodyPanelHandler
{
    public void getBodyPanels(Map<String,JPanel> map);
    public String handleBody(Map<String,JPanel> map, BodyBean b);
}
