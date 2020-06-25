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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.core.ui.swing.ship.ShipStatsPanel;
import jo.ttg.ship.beans.ShipStats;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.Game;
import ttg.adv.logic.ShipLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShipInfoDlg extends JDialog
{
	private Game			mGame;
	private ShipStats		mStats;
	
	private ShipStatsPanel	mShipStats;
	private JButton			mOK;
	private JButton			mRename;
	
	/**
	 *
	 */

	public ShipInfoDlg(JFrame parent, Game game, ShipStats stats)
	{
		super(parent);
		mGame = game;
		mStats = stats;
		initInstantiate();
		initLink();
		initLayout();
	}

	public ShipInfoDlg(JDialog parent, Game game, ShipStats stats)
	{
		super(parent);
		mGame = game;
		mStats = stats;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Ship Info");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mRename = new JButton("Rename");        
		mShipStats = new ShipStatsPanel();
		mShipStats.setDisplayErrors(false);
		mShipStats.setStats(mStats);
		if (mGame == null)
		    mRename.setVisible(false);
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
		ListenerUtils.listen(mRename, (e) -> doRename());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mRename);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", mShipStats);
		setSize(640, 480);
	}

	protected void doOK()
	{
		dispose();
	}
	
	protected void doRename()
	{
		String newName = (String)JOptionPane.showInputDialog(
				this, // parent component
				"New Ship Name", // message
				"Rename Ship", // title
				JOptionPane.PLAIN_MESSAGE, // message type
				null, // icon
				null, // selection values
				mGame.getShip().getName() // initial value
			);
	    if (newName == null)
	        return;
	    newName = newName.trim();
	    if (newName.length() == 0)
	        return;
	    if (newName.equals(mGame.getShip().getName()))
	        return;
	    ShipLogic.renameShip(mGame, newName);
	    mShipStats.setStats(mGame.getShip().getStats());
	}
	
	public void setDisplayErrors(boolean show)
	{
	    mShipStats.setDisplayErrors(show);
	}
}
