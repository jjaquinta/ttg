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
import ttg.beans.war.WorldInst;
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
public class ShipMessagePanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private JLabel		 mText;
	private ObjectButton mShip;
	private ObjectButton mWorld;
	private JButton		 mOK;
	
	/**
	 *
	 */

	public ShipMessagePanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mText = new JLabel();
		mShip = new ObjectButton(mPanel);
		mWorld = new ObjectButton(mPanel);
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
        ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", mText);
		add("1,+ fill=h", mShip);
		add("1,+ fill=h", new JLabel("at:"));
		add("1,+ fill=h", mWorld);
		add("1,+ fill=hv weighty=30", new JLabel(""));
		add("1,+ fill=h", mOK);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		if (mMessage == null)
		{
			mText.setText(null);
			mShip.setObject(null);
			mWorld.setObject(null);
			mOK.setEnabled(false);
		}
		else
		{
			if (mMessage.getID() == PlayerMessage.SHIPDAMAGED)
				mText.setText("Ship damaged:");
			else if (mMessage.getID() == PlayerMessage.SHIPDESTROYED)
				mText.setText("Ship destroyed:");
			else if (mMessage.getID() == PlayerMessage.SHIPMISSED)
				mText.setText("Attack missed ship:");
			else if (mMessage.getID() == PlayerMessage.DIDREPAIR)
				mText.setText("Reparied ship:");
			else if (mMessage.getID() == PlayerMessage.CANTREPAIR)
				mText.setText("Can't repair ship:");
			ShipInst ship = (ShipInst)mMessage.getArg1();
			WorldInst world = ship.getLocation();
			mShip.setObject(ship);
			mWorld.setObject(world);
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
