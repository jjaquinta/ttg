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
import ttg.beans.war.SideInst;
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
public class SideMessagePanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private ObjectButton	mSide;
	private JLabel			mText;
	private JButton			mOK;
	
	/**
	 *
	 */

	public SideMessagePanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mSide = new ObjectButton(mPanel);
		mText = new JLabel();
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
        ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", mSide);
		add("1,+ fill=h", mText);
		add("1,+ fill=hv weighty=30", new JLabel(""));
		add("1,+ fill=h", mOK);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		if (mMessage == null)
		{
			mSide.setObject(null);
			mText.setText(null);
			mOK.setEnabled(false);
		}
		else
		{
			SideInst side = (SideInst)mMessage.getArg1();
			mSide.setObject(side);
			if (mMessage.getID() == PlayerMessage.YOULOSE)
				mText.setText("You Lose!");
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
