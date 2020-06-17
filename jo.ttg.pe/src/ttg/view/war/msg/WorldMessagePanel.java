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
public class WorldMessagePanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private JLabel		mText;
	private ObjectButton	mWorld;
	private JButton		mOK;
	
	/**
	 *
	 */

	public WorldMessagePanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mText = new JLabel();
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
			mWorld.setObject(null);
			mOK.setEnabled(false);
		}
		else
		{
			if (mMessage.getID() == PlayerMessage.COMBATSTART)
				mText.setText("Combat Started");
			else if (mMessage.getID() == PlayerMessage.COMBATEND)
				mText.setText("Combat Ended");
			WorldInst world = (WorldInst)mMessage.getArg1();
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
