/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.adv.beans.CrewBean;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;
import ttg.adv.logic.SellLogic;
import ttg.adv.view.ctrl.CrewTableModel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewCrewDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private CrewTableModel	mCrewModel;
	private TableSorter mCrewSorter;
	
	private JTable	mCrew;
	private JButton	mFireCrew;
	private JButton	mCancel;
	
	/**
	 *
	 */

	public ViewCrewDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mShip = mGame.getShip();
	    setTitle(mShip.getName()+"'s Crew");
		mCrewModel = new CrewTableModel();
		mCancel = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCrewSorter = new TableSorter(mCrewModel);
		mCrew = new JTable(mCrewSorter);
		mCrewSorter.addMouseListenerToHeaderInTable(mCrew);
		mFireCrew = new JButton("Fire");
		updateLists();
	}

	private void initLink()
	{
	    MouseUtils.mouseClicked(mCrew, (ev) -> {
	    	            if (ev.getClickCount() == 2)
	    	                doTableClick();
	    	        });
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		ListenerUtils.listen(mFireCrew, (ev) -> doFireCrew());
	}

	private void initLayout()
	{
		JPanel buttonBar = new JPanel();
		buttonBar.add(mFireCrew);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mCrew));
		pack();
	}
	
	private void updateLists()
	{
		//System.out.println("ForSale:Updating lists.");
		mShip = mGame.getShip();
		if (mShip == null)
		{
			mCrewModel.setChars(null);
			return;
		}
		mCrewModel.setCrew(mShip.getCrew());
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doFireCrew()
	{
		int[] sel = mCrew.getSelectedRows();
		mCrewSorter.mapRows(sel);
		List<CharBean> crews = mCrewModel.getChars();
		for (int i = 0; i < sel.length; i++)
			SellLogic.fireCrew(mGame, mShip, (CrewBean)crews.get(sel[i]));
		updateLists();
	}
	
	protected void doTableClick()
	{
	    int row = mCrew.getSelectedRow();
	    if (row < 0)
	        return;
	    CrewBean crew = (CrewBean)mCrewModel.getChars().get(mCrewSorter.mapRow(row));
	    CrewInfoDlg dlg = new CrewInfoDlg(this, mGame, crew);
	    dlg.setModal(true);
	    dlg.setVisible(true);
	    mCrew.repaint();
	}
}
