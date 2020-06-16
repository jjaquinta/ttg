/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.sw.ui.sys;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.core.ui.swing.ctrl.body.BodyPanel;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.util.ui.swing.ctrl.HTMLLabel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SWWorldDetails extends JPanel
{
    private BodyBean     mWorld;
	
    private HTMLLabel   mClient;
    private BodyPanel   mView;
	
	public SWWorldDetails()
	{
		initInstantiate();
		initLink();
		initLayout();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<table width='100%' align='top'>"
            + "<tr><th>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>"
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
        mView = new BodyPanel();
        doUpdateCursorWorld();
	}
	
	private void initLayout()
	{
		setLayout(new BorderLayout());
        //add("Center", mClient);
        add("Center", mView);
	}
	private void initLink()
	{
        RuntimeLogic.listen("cursorWorld", (ov,nv) -> doUpdateCursorWorld());
	}

    private void doUpdateCursorWorld()
    {
        setWorld(RuntimeLogic.getInstance().getCursorWorld());
    }

    public BodyBean getWorld()
    {
        return mWorld;
    }

    public void setWorld(BodyBean world)
    {
        mWorld = world;
        mClient.replaceBean(mWorld);
        mClient.updateText();
        mView.setBody(mWorld);
        doLayout();
    }
}
