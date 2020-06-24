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
import javax.swing.JPanel;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.ctrl.CharStatsPanel;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.Game;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CharInfoDlg extends JDialog
{
	private Game		mGame;
	private CharBean	mChar;
	
	private CharStatsPanel	mCharStats;
	private JButton			mOK;
	
	/**
	 *
	 */

	public CharInfoDlg(JFrame parent, Game game, CharBean character)
	{
		super(parent);
		mGame = game;
		mChar = character;
		initInstantiate();
		initLink();
		initLayout();
	}

	public CharInfoDlg(JDialog parent, Game game, CharBean character)
	{
		super(parent);
		mGame = game;
		mChar = character;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Personal Information");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCharStats = new CharStatsPanel();
		mCharStats.setChar(mChar);
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", mCharStats);
		pack();
	}

	protected void doOK()
	{
		dispose();
	}
}
