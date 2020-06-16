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
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.sub.SubSectorLogic;
import jo.ttg.utils.DisplayUtils;
import jo.util.ui.swing.ctrl.HTMLLabel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class IMPSubSectorDetails extends JPanel
{
    private SubSectorBean mSubSector;
	
    private HTMLLabel     mClient;
	
	public IMPSubSectorDetails()
	{
		initInstantiate();
		initLink();
		initLayout();
		doUpdateFocusSubSector();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<table width='100%' align='top'>"
            + "<tr><th><font size='+2'>SubSector:</font></th><td colspan='3'><font size='+2'>[[Name]]</font></td></tr>"
            + "<tr><th>Number of Worlds:</th><td colspan='3'><font size='+2'>[[NumWorlds]]</font></td></tr>"
            + "<tr><th>Population:</th><td colspan='3'><font size='+2'>[[PopWorlds]]</font></td></tr>"
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
        RuntimeLogic.listen("focusSubSector", (ov,nv) -> doUpdateFocusSubSector());
	}

    private void doUpdateFocusSubSector()
    {
        setSubSector(RuntimeLogic.getInstance().getFocusSubSector());
    }

    public SubSectorBean getSubSector()
    {
        return mSubSector;
    }

    public void setSubSector(SubSectorBean subsector)
    {
        mSubSector = subsector;
        if (mSubSector == null)
            return;
        mClient.replaceBean(mSubSector);
        MainWorldBean[] mws = SubSectorLogic.getMainWorlds(mSubSector);
        mClient.setSubstitution("NumWorlds", String.valueOf(mws.length));
        mClient.setSubstitution("PopWorlds", DisplayUtils.formatPopulation(MainWorldLogic.getPopulation(mws)));
        mClient.updateText();
        doLayout();
    }
}
