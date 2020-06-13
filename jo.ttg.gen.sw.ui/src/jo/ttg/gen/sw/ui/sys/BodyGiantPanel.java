/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.sw.ui.sys;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyGiantBean;
import jo.util.ui.swing.ctrl.HTMLPane;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyGiantPanel extends JPanel
{
	BodyGiantBean	mGiant;	
	   
    private HTMLPane  mClient;
	
	public BodyGiantPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<body style='font-family: Sans-Serif;'>"
            + "<table width='100%' align='top'>"
            + "<tr><th>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>"
            + "<tr><th>Type:</th><td colspan='2'>[[TypeDesc]]</td></tr>"
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
	public void setBody(BodyGiantBean bean)
	{
		mGiant = bean;
        mClient.replaceBean(mGiant);
		if (mGiant.getSize() == BodyGiantBean.GS_S)
	        mClient.setSubstitution("TypeDesc", "Small");
		else
		    mClient.setSubstitution("TypeDesc", "Large");
        mClient.updateText();
        doLayout();
	}
}
