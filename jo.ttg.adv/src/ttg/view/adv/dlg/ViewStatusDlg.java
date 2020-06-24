/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.Game;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewStatusDlg extends JDialog
{
	private Game		mGame;
	
	private JList	mStatus;
	private JButton	mOK;
	
	/**
	 *
	 */

	public ViewStatusDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setTitle("Status History");
	    mStatus = new JList();
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		updateLists();
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK());
	}

	private void initLayout()
	{
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mStatus));
		pack();
	}
	
	private void updateLists()
	{
	    mStatus.setListData(mGame.getStatusHistory().toArray());
	}

	protected void doOK()
	{
		dispose();
	}
}
