/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.ctrl.body.BodyPanel;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodyInfoDlg extends JDialog
{
	private BodyBean	mBody;
	
	private BodyPanel	mBodyStats;
	private JButton		mOK;
	
	/**
	 *
	 */

	public BodyInfoDlg(JFrame parent, BodyBean body)
	{
		super(parent);
		initInstantiate();
		initLink();
		initLayout();
		setBody(body);
	}

	public BodyInfoDlg(JDialog parent, BodyBean body)
	{
		super(parent);
		initInstantiate();
		initLink();
		initLayout();
		setBody(body);
	}

	private void initInstantiate()
	{
	    mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mBodyStats = new BodyPanel();
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", mBodyStats);
		setSize(300, 400);
	}

	protected void doOK()
	{
		dispose();
	}
    public BodyBean getBody()
    {
        return mBody;
    }
    public void setBody(BodyBean body)
    {
        mBody = body;
	    if (mBody instanceof BodyWorldBean)
	        setTitle("World Info");
	    else if (mBody instanceof BodyStarBean)
	        setTitle("Star Info");
	    else if (mBody instanceof BodyGiantBean)
	        setTitle("Gas Giant Info");
	    else if (mBody instanceof BodyToidsBean)
	        setTitle("Planetoid Belt Info");
	    else if (mBody instanceof BodySpecialBean)
	        switch (((BodySpecialBean)mBody).getSubType())
	        {
	            case BodySpecialBean.ST_LABBASE:
	    	        setTitle("Lab Info");
	                break;
	            case BodySpecialBean.ST_LOCALBASE:
	    	        setTitle("Local Military Base Info");
	                break;
	            case BodySpecialBean.ST_NAVYBASE:
	    	        setTitle("Naval Base Info");
	                break;
	            case BodySpecialBean.ST_REFINERY:
	    	        setTitle("Refinery Info");
	                break;
	            case BodySpecialBean.ST_SCOUTBASE:
	    	        setTitle("Scout Base Info");
	                break;
	            case BodySpecialBean.ST_SPACEPORT:
	    	        setTitle("Spaceport Info");
	                break;
	            case BodySpecialBean.ST_STARPORT:
	    	        setTitle("Starport Info");
	                break;
	        }
        else
            setTitle("Unknown Info");
		mBodyStats.setBody(mBody);
    }
}
