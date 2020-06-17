/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.msg;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.PlayerMessage;
import ttg.beans.war.ShipInst;
import ttg.logic.war.IconLogic;
import ttg.view.war.ObjectButton;
import ttg.view.war.WarButton;
import ttg.view.war.WarPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NewShipPanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private ObjectButton	mShip;
	private ObjectButton	mLocation;
	private JButton		mOK;
	
	/**
	 *
	 */

	public NewShipPanel(WarPanel panel)
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
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
        ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", new JLabel("New ship:"));
		add("1,+ fill=h", mShip);
		add("1,+ fill=h", new JLabel("created at:"));
		add("1,+ fill=h", mLocation);
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
			mOK.setEnabled(false);
		}
		else
		{
			ShipInst ship = (ShipInst)mMessage.getArg1();
			mShip.setObject(ship);
			mLocation.setObject(ship.getLocation());
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
