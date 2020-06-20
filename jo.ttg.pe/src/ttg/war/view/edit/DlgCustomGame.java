/*
 * Created on Apr 4, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.edit;

import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.logic.IconLogic;
import ttg.war.view.WarButton;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DlgCustomGame extends JDialog
{
	private JTextArea	mText;
	private JButton		mShip;
	private JButton		mCancel;
	private JButton		mOK;
	private boolean		mAccepted;
	
	/**
	 *
	 */

	public DlgCustomGame(Frame frame)
	{
		super(frame, "Customize Game", true);
		initInstantiate();
		initLink();
		initLayout();
	}
	
	private void initInstantiate()
	{
		mText = new JTextArea();
		mShip = new WarButton("Ship Designer", IconLogic.mButtonBuild);
		mShip.setToolTipText("Highlight a ship line and click to edit it");
		mOK = new WarButton("OK", IconLogic.mButtonDone);
		mCancel = new WarButton("Cancel", IconLogic.mButtonCancel);
	}

	private void initLink()
	{
		ListenerUtils.listen(mShip, (ev) -> doShip());
		ListenerUtils.listen(mOK, (ev) -> doOK());
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
	}

	private void initLayout()
	{
		JPanel p1 = new JPanel();
		p1.add(mShip);
		p1.add(mOK);
		p1.add(mCancel);
		getContentPane().add("North", new JLabel("Edit custom game:"));
		getContentPane().add("Center", new JScrollPane(mText));
		getContentPane().add("South", p1);
		
		setSize(640, 480);
	}

	private void doOK()
	{
		mAccepted = true;
		dispose();
	}
	
	private void doCancel()
	{
		mAccepted = false;
		dispose();
	}
	
	private void doShip()
	{
		DlgShipDesigner dlg = new DlgShipDesigner(this);
		int start = mText.getSelectionStart();
		int end = mText.getSelectionEnd();
		String txt = mText.getText();
		String line = txt.substring(start, end);
		String prefix = "";
		int off = line.indexOf("=");
		if (off > 0)
		{
			prefix = line.substring(0, off+1);
			line = line.substring(off+1).trim();
		}
		dlg.setLine(line);
		dlg.setVisible(true);
		if (dlg.isAccepted())
		{
			txt = txt.substring(0, start) + prefix+dlg.getLine() + txt.substring(end);
			mText.setText(txt);
			mText.setCaretPosition(start);
		}
	}
	
	public void setText(String txt)
	{
		mText.setText(txt);
	}
	
	public String getText()
	{
		return mText.getText();
	}
	/**
	 * @return
	 */
	public boolean isAccepted()
	{
		return mAccepted;
	}

}
