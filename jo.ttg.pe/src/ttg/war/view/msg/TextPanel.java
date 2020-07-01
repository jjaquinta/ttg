/*
 * Created on Mar 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.msg;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.FileOpenUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.PlayerMessage;
import ttg.war.logic.GameLogic;
import ttg.war.logic.IconLogic;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TextPanel extends JPanel
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
    private JButton     mSave;
	private JButton		mOK;
	private JLabel		mWhy;
	
	/**
	 *
	 */

	public TextPanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mOK = new WarButton("OK", IconLogic.mButtonDone);
        mSave = new WarButton("Save...", IconLogic.mButtonSave);
		mWhy = new JLabel();
	}

	private void initLink()
	{
        ListenerUtils.listen(mOK, (ev) -> doOK());
        ListenerUtils.listen(mSave, (ev) -> doSave());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", mWhy);
		add("1,+ fill=hv weighty=30", new JLabel(""));
        add("1,+ fill=h", mSave);
		add("1,+ fill=h", mOK);
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		if (mMessage == null)
		{
			mWhy.setText(null);
			mOK.setEnabled(false);
            mSave.setVisible(false);
		}
		else
		{
			mWhy.setText((String)mMessage.getArg1());
			mOK.setEnabled(true);
            mSave.setVisible(mMessage.getID() == PlayerMessage.ENDOFTURN);
		}
	}

	/**
	 * 
	 */
	protected void doOK()
	{
		mPanel.setMode(WarPanel.DONE);
	}
	
	public void doSave()
	{
        File f = FileOpenUtils.selectFile(this, "Save", "TTG Pocket Empires", ".pe.json");
        if (f == null)
            return;
        GameLogic.save(mPanel.getGame(), f);
	}
}
