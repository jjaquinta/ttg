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
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IconLogic;
import ttg.war.logic.WorldLogic;
import ttg.war.view.ObjectButton;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NewOwnerPanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private ObjectButton	mWorld;
	private ObjectButton	mOldSide;
	private ObjectButton	mNewSide;
	private JLabel			mPoints;
	private JButton			mOK;
	
	/**
	 *
	 */

	public NewOwnerPanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mWorld = new ObjectButton(mPanel);
		mOldSide = new ObjectButton(mPanel);
		mNewSide = new ObjectButton(mPanel);
		mPoints = new JLabel();
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
        ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", new JLabel("The system of:"));
		add("1,+ fill=h", mWorld);
		add("1,+ fill=h", new JLabel("has changed from:"));
		add("1,+ fill=h", mOldSide);
		add("1,+ fill=h", new JLabel("to:"));
		add("1,+ fill=h", mNewSide);
		add("1,+ fill=h", new JLabel("worth:"));
		add("1,+ fill=h", mPoints);
		add("1,+ fill=hv weighty=30", new JLabel(""));
		add("1,+ fill=h", mOK);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		if (mMessage == null)
		{
			mWorld.setObject(null);
			mOldSide.setObject(null);
			mNewSide.setObject(null);
			mPoints.setText(null);
			mOK.setEnabled(false);
		}
		else
		{
			WorldInst world = (WorldInst)mMessage.getArg1();
			SideInst oldSide = (SideInst)mMessage.getArg2();
			SideInst newSide = world.getSide();
			mWorld.setObject(world);
			mOldSide.setObject(oldSide);
			mNewSide.setObject(newSide);
			mPoints.setText(String.valueOf(WorldLogic.getHaveWorldValue(mPanel.getGame(), world)));
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
