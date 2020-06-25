/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.CrewBean;
import ttg.adv.beans.Game;
import ttg.adv.logic.CrewLogic;
import ttg.adv.view.ctrl.CrewStatsPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CrewInfoDlg extends JDialog
{
	private Game		mGame;
	private CrewBean	mCrew;
	
	private CrewStatsPanel	mCrewStats;
	private JButton			mOK;
	
	/**
	 *
	 */

	public CrewInfoDlg(JFrame parent, Game game, CrewBean character)
	{
		super(parent);
		mGame = game;
		mCrew = character;
		initInstantiate();
		initLink();
		initLayout();
	}

	public CrewInfoDlg(JDialog parent, Game game, CrewBean character)
	{
		super(parent);
		mGame = game;
		mCrew = character;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Crew Information");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));
		mCrewStats = new CrewStatsPanel();
		mCrewStats.setChar(mCrew);
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
		getContentPane().add("Center", mCrewStats);
		pack();
	}

	protected void doOK()
	{
	    int newJob = mCrewStats.getSelectedJob();
	    if (newJob != mCrew.getJob())
	        CrewLogic.assignJob(mGame.getShip(), mCrew, newJob);
		dispose();
	}
}
