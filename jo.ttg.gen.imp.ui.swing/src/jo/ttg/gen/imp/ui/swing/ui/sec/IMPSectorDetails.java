/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.imp.ui.swing.ui.sec;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.utils.DisplayUtils;
import jo.util.ui.swing.ctrl.HTMLLabel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IMPSectorDetails extends JPanel
{
    private SectorBean     mSector;
	
    private HTMLLabel   mClient;
	
	public IMPSectorDetails()
	{
		initInstantiate();
		initLink();
		initLayout();
		doUpdateFocusSector();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<table width='100%' align='top'>"
            + "<tr><th><font size='+2'>Sector:</font></th><td colspan='3'><font size='+2'>[[Name]]</font></td></tr>"
            + "<tr><th>Number of Worlds:</th><td colspan='3'><font size='+2'>[[NumWorlds]]</font></td></tr>"
            + "<tr><th>Population:</th><td colspan='3'><font size='+2'>[[PopWorlds]]</font></td></tr>"
            + "<tr><th colspan='4'>Subectors:</th></tr>"
            + "<tr><td>[[SubName0]]</td><td>[[SubName1]]</td><td>[[SubName2]]</td><td>[[SubName3]]</td></tr>"
            + "<tr><td>[[SubName4]]</td><td>[[SubName5]]</td><td>[[SubName6]]</td><td>[[SubName7]]</td></tr>"
            + "<tr><td>[[SubName8]]</td><td>[[SubName9]]</td><td>[[SubName10]]</td><td>[[SubName11]]</td></tr>"
            + "<tr><td>[[SubName12]]</td><td>[[SubName13]]</td><td>[[SubName14]]</td><td>[[SubName15]]</td></tr>"
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
        RuntimeLogic.listen("focusSector", (ov,nv) -> doUpdateFocusSector());
	}

    private void doUpdateFocusSector()
    {
        setSector(RuntimeLogic.getInstance().getFocusSector());
    }

    public SectorBean getSector()
    {
        return mSector;
    }

    public void setSector(SectorBean sector)
    {
        mSector = sector;
        if (mSector == null)
            return;
        mClient.replaceBean(mSector);
        MainWorldBean[] mws = SectorLogic.getMainWorlds(mSector);
        mClient.setSubstitution("NumWorlds", String.valueOf(mws.length));
        mClient.setSubstitution("PopWorlds", DisplayUtils.formatPopulation(MainWorldLogic.getPopulation(mws)));
        SubSectorBean[] subs = mSector.getSubSectors();
        for (int i = 0; i < subs.length; i++)
            mClient.setSubstitution("SubName"+i, subs[i].getName());
        mClient.updateText();
        doLayout();
    }
}
