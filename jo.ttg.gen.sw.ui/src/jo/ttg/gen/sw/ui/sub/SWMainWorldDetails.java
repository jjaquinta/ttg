/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.sw.ui.sub;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.util.ui.swing.ctrl.HTMLLabel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SWMainWorldDetails extends JPanel
{
    private SWMainWorldBean     mWorld;
	
    private HTMLLabel   mClient;
	
	public SWMainWorldDetails()
	{
		initInstantiate();
		initLink();
		initLayout();
		doUpdateCursorMainWorld();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<table width='100%' align='top'>"
            + "<tr><th>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>"
            + "<tr><th>Location:</th><td colspan='2'>[[OrdsFineDesc]]</td></tr>"
            + "<tr><th>Port:</th><td>[[populatedStats.upp.port.getValueDigit()]]</td><td>[[populatedStats.upp.port.getValueDescription()]]</td></tr>"
            + "<tr><th>Size:</th><td>[[populatedStats.upp.size.getValueDigit()]]</td><td>[[populatedStats.upp.size.getValueDescription()]]</td></tr>"
            + "<tr><th>Atmosphere:</th><td>[[populatedStats.upp.atmos.getValueDigit()]]</td><td>[[populatedStats.upp.atmos.getValueDescription()]]</td></tr>"
            + "<tr><th>Hydrosphere:</th><td>[[populatedStats.upp.hydro.getValueDigit()]]</td><td>[[populatedStats.upp.hydro.getValueDescription()]]</td></tr>"
            + "<tr><th>Population:</th><td>[[populatedStats.upp.pop.getValueDigit()]]</td><td>[[populatedStats.upp.pop.getValueDescription()]]</td></tr>"
            + "<tr><th>Government:</th><td>[[populatedStats.upp.gov.getValueDigit()]]</td><td>[[populatedStats.upp.gov.getValueDescription()]]</td></tr>"
            + "<tr><th>Law Level:</th><td>[[populatedStats.upp.law.getValueDigit()]]</td><td>[[populatedStats.upp.law.getValueDescription()]]</td></tr>"
            + "<tr><th>Tech Level:</th><td>[[populatedStats.upp.tech.getValueDigit()]]</td><td>[[populatedStats.upp.tech.getValueDescription()]]</td></tr>"
            + "<tr><th>Bases:</th><td colspan='2'>[[populatedStats.getBasesDescLong()]]</td></tr>"
            + "<tr><th>Zone:</th><td colspan='2'>[[populatedStats.getTravelZoneDesc()]]</td></tr>"
            + "<tr><th>Alliegence:</th><td>[[populatedStats.allegiance]]</td><td>[[populatedStats.getAllegianceDesc()]]</td></tr>"
            + "</table>"
            + "<h3>Notes:</h3>"
            + "<p>[[notes]]</p>"
            + "</html>";

	private void initInstantiate()
	{
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
        mClient = new HTMLLabel();
        mClient.setTemplate(TEMPLATE);
	}
	
	private void initLayout()
	{
		setLayout(new BorderLayout());
        add("Center", mClient);
	}
	private void initLink()
	{
        RuntimeLogic.listen("cursorMainWorld", (ov,nv) -> doUpdateCursorMainWorld());
	}

    private void doUpdateCursorMainWorld()
    {
        setWorld(RuntimeLogic.getInstance().getCursorMainWorld());
    }

    public SWMainWorldBean getWorld()
    {
        return mWorld;
    }

    public void setWorld(SWMainWorldBean world)
    {
        mWorld = world;
        mClient.replaceBean(mWorld);
        mClient.updateText();
        doLayout();
    }
}
