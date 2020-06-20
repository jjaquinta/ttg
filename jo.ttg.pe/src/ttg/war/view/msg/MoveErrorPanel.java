/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.msg;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.PlayerMessage;
import ttg.war.beans.ShipInst;
import ttg.war.logic.IconLogic;
import ttg.war.view.ObjectButton;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MoveErrorPanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private ObjectButton	mShip;
	private ObjectButton	mLocation;
	private ObjectButton	mDestination;
	private JButton		mOK;
	private JLabel		mWhy;
	
	/**
	 *
	 */

	public MoveErrorPanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mShip = new ObjectButton(mPanel);
		mLocation = new ObjectButton(mPanel);
		mDestination = new ObjectButton(mPanel);
		mOK = new WarButton("OK", IconLogic.mButtonDone);
		mWhy = new JLabel();
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", new JLabel("Can't move ship:"));
		add("1,+ fill=h", mShip);
		add("1,+ fill=h", new JLabel("from location:"));
		add("1,+ fill=h", mLocation);
		add("1,+ fill=h", new JLabel("to destination:"));
		add("1,+ fill=h", mDestination);
		add("1,+ fill=h", mWhy);
		add("1,+ fill=hv weighty=30", new JLabel(""));
		add("1,+ fill=h", mOK);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		if (mMessage == null)
		{
			mShip.setObject(null);
			mLocation.setObject(null);
			mDestination.setObject(null);
			mWhy.setText(null);
			mOK.setEnabled(false);
		}
		else
		{
			ShipInst ship = (ShipInst)mMessage.getArg1();
			mShip.setObject(ship);
			mLocation.setObject(ship.getLocation());
			mDestination.setObject(ship.getDestination());
			mWhy.setText((String)mMessage.getArg2());
			mOK.setEnabled(true);
		}
	}

	/**
	 * 
	 */
	protected void doOK()
	{
		mPanel.setMode(WarPanel.DONE);
	}
}
