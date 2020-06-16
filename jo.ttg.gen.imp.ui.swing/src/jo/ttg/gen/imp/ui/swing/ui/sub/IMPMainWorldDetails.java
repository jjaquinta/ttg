/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.imp.ui.swing.ui.sub;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.util.ui.swing.ctrl.HTMLLabel;
import jo.util.utils.obj.StringUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IMPMainWorldDetails extends JPanel
{
    private MainWorldBean     mWorld;
	
    private HTMLLabel   mClient;
	
	public IMPMainWorldDetails()
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

    public MainWorldBean getWorld()
    {
        return mWorld;
    }

    public void setWorld(MainWorldBean world)
    {
        mWorld = world;
        if (mWorld == null)
            return;
        mClient.replaceBean(mWorld);
        String loc = StringUtils.zeroPrefix(mWorld.getOrds().getX()%32 + 1, 2)
                + StringUtils.zeroPrefix(mWorld.getOrds().getY()%40 + 1, 2);
        mClient.setSubstitution("OrdsFine", loc);
        mClient.setSubstitution("NAME", mWorld.getName());
        mClient.updateText();
        doLayout();
    }
}
