/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body;


import java.awt.BorderLayout;

import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.util.ui.swing.ctrl.HTMLPane;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HTMLBodyToidsPanel extends HTMLPopulatedStatsPanel
{
    private HTMLPane   mClient;
	
	public HTMLBodyToidsPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<body style='font-family: Sans-Serif;'>"
            + "<table width='100%' align='top'>"
            + "<tr><th>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>"
            + "<tr><th>Rock Size:</th><td colspan='2'>[[RockSize]]</td></tr>"
            + "<tr><th>Width:</th><td colspan='2'>[[Width]]</td></tr>"
            + "<tr><th>Nickel/Iron:</th><td colspan='2'>[[Nickel]]</td></tr>"
            + "<tr><th>Mixed:</th><td colspan='2'>[[Mixed]]</td></tr>"
            + "<tr><th>Carbonaceous:</th><td colspan='2'>[[Carbon]]</td></tr>"
            + POPULATED_STATS_TEMPLATE
            + "</table>"
            + "<h3>Notes:</h3>"
            + "<p>[[notes]]</p>"
            + "</body>"
            + "</html>";

	private void initInstantiate()
	{
        mClient = new HTMLPane();
        mClient.setTemplate(TEMPLATE);
	}
	private void initLayout()
	{
        setLayout(new BorderLayout());
        add("Center", mClient);
	}
	private void initLink()
	{
	}
	
	/**
	 * @param bean
	 */
	public void setBody(BodyToidsBean toids)
	{
        mClient.replaceBean(toids);
        mClient.setSubstitution("RockSize", FormatUtils.sDistance(toids.getMaxDiam()));
        mClient.setSubstitution("Width", FormatUtils.sDistance(toids.getWidth()));
        mClient.setSubstitution("Nickel", FormatUtils.sPC(toids.getNZone()*100));
        mClient.setSubstitution("Mixed", FormatUtils.sPC(toids.getMZone()*100));
        mClient.setSubstitution("Carbon", FormatUtils.sPC(toids.getCZone()*100));
        mClient.updateText();
        doLayout();
	}
}
