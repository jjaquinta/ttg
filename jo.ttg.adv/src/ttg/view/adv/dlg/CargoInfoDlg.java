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

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.AdvCargoBean;
import ttg.beans.adv.Game;
import ttg.view.adv.ctrl.AdvCargoStatsPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CargoInfoDlg extends JDialog
{
	private Game		mGame;
	private AdvCargoBean	mCargo;
	
	private AdvCargoStatsPanel	mCargoStats;
	private JButton			mOK;
	
	/**
	 *
	 */

	public CargoInfoDlg(JFrame parent, Game game, AdvCargoBean cargo)
	{
		super(parent);
		mGame = game;
		mCargo = cargo;
		initInstantiate();
		initLink();
		initLayout();
	}

	public CargoInfoDlg(JDialog parent, Game game, AdvCargoBean cargo)
	{
		super(parent);
		mGame = game;
		mCargo = cargo;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    if (mCargo.getClassification() == AdvCargoBean.CC_CARGO)
	        setTitle("Cargo Info");
        else
            setTitle("Freight Info");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCargoStats = new AdvCargoStatsPanel();
		mCargoStats.setScheme(mGame.getScheme());
		mCargoStats.setNow(mGame.getDate());
		mCargoStats.setAt(mGame.getShip().getLocation());
		mCargoStats.setCargo(mCargo);
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
		getContentPane().add("Center", mCargoStats);
		pack();
	}

	protected void doOK()
	{
		dispose();
	}
}
