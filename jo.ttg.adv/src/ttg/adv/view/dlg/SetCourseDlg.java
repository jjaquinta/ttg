/*
 * Created on Jan 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.tree.TreePath;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.HexField;
import jo.ttg.core.ui.swing.ctrl.SystemList;
import jo.ttg.core.ui.swing.ctrl.SystemTree;
import jo.ttg.core.ui.swing.ctrl.SystemTreeNode;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import ttg.adv.beans.Game;
import ttg.adv.logic.ShipLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SetCourseDlg extends JDialog
{
	private Game	 		mGame;
	private LocationURI		mOrigin;
	private BodyBean		mSelectedBody;
	private BodyInfoDlg		mBodyInfoDlg;
	
	private HexField		mHexes;
	private SystemTree		mSystemTree;
	private SystemList		mSystemList;
	private BodyView		mDescription;
	private JLabel			mConsumption;
	private JButton 		mOK;
	private JButton 		mCancel;
	
	private DistCapabilities	mCaps;
	
	/**
	 *
	 */

	public SetCourseDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		setTitle("Set Course");
		mCancel = new JButton("Cancel", TTGIconUtils.getIcon("tb_cancel.gif"));        
		mOK = new JButton("Set Course", TTGIconUtils.getIcon("tb_save.gif"));
		mDescription = new BodyView();
		mConsumption = new JLabel();
		mHexes = new HexField(mGame.getScheme());
		mOrigin = new LocationURI(mGame.getShip().getDestination());
		OrdBean ul = new OrdBean(mOrigin.getOrds());
		//System.out.println("SetCourseDlg.initInstantiate, at="+ul.toString());
		ul.setX(ul.getX() - 4);
		ul.setY(ul.getY() - 5);
		//System.out.println("SetCourseDlg.initInstantiate, origin="+ul.toString());
		mHexes.setHexesHigh(10);
		mHexes.setHexesWide(8);
		mHexes.setOrigin(ul);
		mHexes.setForeColor(Color.YELLOW);
		mHexes.setBackground(Color.BLACK);
		mHexes.setFocusedColor(Color.WHITE);
		mHexes.setDisabledColor(Color.LIGHT_GRAY);
		mHexes.setFocus(mOrigin.getOrds());

		mSystemTree = new SystemTree(mGame.getScheme());
		mSystemTree.setOrigin(mOrigin.getOrds());
		mSystemList = new SystemList(mGame.getScheme());
		mSystemList.setFilter(SystemList.SPECIALS);
		mSystemList.setOrigin(mOrigin.getOrds());
		
		mCaps = ShipLogic.getCaps(mGame.getShip().getStats());
		BodyBean body = (BodyBean)SchemeLogic.getFromURI(mGame.getScheme(), mGame.getShip().getDestination());
		mSystemTree.setSelectedBody(body);
		mSystemList.setSelectedValue(body);
		setSelectedBody(body);
	}

	private void initLink()
	{
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		ListenerUtils.listen(mOK, (ev) -> doOK());
		mHexes.addTTGActionListener(new TTGActionListener() {
            public void actionPerformed(TTGActionEvent e)
            {
                if (e.getID() == TTGActionEvent.SELECTED)
                    doHexSelect(e.getObject());
            }
		});
		ListenerUtils.change(mSystemList, (e) -> doBodyTreeSelect());
	    MouseUtils.mouseClicked(mSystemTree, (ev) -> {
	    	            if (ev.getClickCount() == 2)
	    	                doBodyTreeClick();
	    	        });
		ListenerUtils.change(mSystemList, (e) -> doBodyListSelect());
		MouseUtils.mouseClicked(mSystemList, (ev) -> {
	    	            if (ev.getClickCount() == 2)
	    	                doBodyListClick();
	    	        });
	}

	private void initLayout()
	{
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mCancel);
		buttonBar.add(mDescription);
		buttonBar.add(mConsumption);
		
		JTabbedPane tabs = new JTabbedPane();
		tabs.add("Points of Interest", new JScrollPane(mSystemList));
		tabs.add("Full System", new JScrollPane(mSystemTree));
	    
		JPanel ctrlBar = new JPanel();
		ctrlBar.setLayout(new GridLayout(1, 2));
		ctrlBar.add(new JScrollPane(mHexes));
		ctrlBar.add(new JScrollPane(tabs));
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", ctrlBar);
		getContentPane().add("South", buttonBar);
		pack();
	}
	
	protected void doHexSelect(Object o)
	{
	    if (o instanceof MainWorldBean)
	    {
	        OrdBean ords = ((MainWorldBean)o).getOrds();
	        mSystemTree.setOrigin(ords);
	        mSystemList.setOrigin(ords);
			SystemBean sys = mGame.getScheme().getGeneratorSystem().generateSystem(ords);
			BodyBean mw = SystemLogic.findMainworld(sys);
			for (Iterator<BodyBean> i = mw.getSatelitesIterator(); i.hasNext(); )
			{
			    BodyBean b = i.next();
			    if ((b instanceof BodySpecialBean) && (((BodySpecialBean)b).getSubType() == BodySpecialBean.ST_STARPORT))
			    {
			        mw = b;
			        break;
			    }
			}
			mSystemTree.setSelectedBody(mw);
			mSystemList.setSelectedValue(mw);
			setSelectedBody(mw);
	    }
	}
	
	protected void doBodyListSelect()
	{
	    BodyBean body = (BodyBean)mSystemList.getSelectedValue();
	    if (body == null)
	    {
	        setSelectedBody(null);
	        mSystemTree.setSelectionRow(-1);
	    }
	    else
	    {
	        setSelectedBody(body);
	        mSystemTree.setSelectedBody(body);
	    }
	}
	
	protected void doBodyListClick()
	{
	    BodyBean body = (BodyBean)mSystemList.getSelectedValue();
	    if (body != null)
	    {
	        if (mBodyInfoDlg == null)
	            mBodyInfoDlg = new BodyInfoDlg(this, body);
	        else
	            mBodyInfoDlg.setBody(body);
	        mBodyInfoDlg.setVisible(true);
	    }
	}
	
	protected void doBodyTreeSelect()
	{
	    TreePath sel = mSystemTree.getSelectionPath();
	    if (sel == null)
	    {
	        setSelectedBody(null);
	        mSystemList.setSelectedValue(null);
	    }
	    else
	    {
	        BodyBean body = ((SystemTreeNode)sel.getLastPathComponent()).getBody();
	        setSelectedBody(body);
	        mSystemList.setSelectedValue(body);
	    }
	}
	
	protected void doBodyTreeClick()
	{
	    TreePath sel = mSystemTree.getSelectionPath();
	    if (sel != null)
	    {
		    BodyBean body = ((SystemTreeNode)sel.getLastPathComponent()).getBody();
	        BodyInfoDlg dlg = new BodyInfoDlg(this, body);
	        dlg.setModal(true);
	        dlg.setVisible(true);
	    }
	}
	
	private void setSelectedBody(BodyBean body)
	{
	    if ((body != null) && (mSelectedBody != null) && body.getURI().equals(mSelectedBody.getURI()))
	        return;
	    mSelectedBody = body;
	    mDescription.setBody(mSelectedBody);
        mConsumption.setText("");
	    if (mSelectedBody == null)
	        return;
        try
        {
    	    LocationURI uri = new LocationURI(mSelectedBody.getURI());
    	    if (mSelectedBody instanceof BodyGiantBean)
    	        uri.setParam("orbit", "1");
    	    else if (mSelectedBody instanceof BodyStarBean)
    	        uri.setParam("orbit", "100");
            List<DistTransition> trans = TraverseLogic.calcTraverse(mGame.getShip().getLocation(), uri.getURI(), mCaps, mGame.getScheme());
            DistConsumption cons = ConsumptionLogic.calcConsumption(trans, mCaps);
            mConsumption.setText(FormatUtils.formatElapsedTime(cons.getTime())+", "+Math.floor(cons.getFuel())+"t");
        }
        catch (TraverseException e)
        {
            mConsumption.setText("Too far");
        }
        if (mBodyInfoDlg != null)
            mBodyInfoDlg.setBody(body);
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doOK()
	{
	    LocationURI uri = new LocationURI(mSelectedBody.getURI());
	    if (mSelectedBody instanceof BodyGiantBean)
	        uri.setParam("orbit", "1");
	    else if (mSelectedBody instanceof BodyStarBean)
	        uri.setParam("orbit", "100");
		mGame.getShip().setDestination(uri.getURI());
		dispose();
	}
}
