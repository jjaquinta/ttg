/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.beans.adv.ShipInst;
import ttg.logic.adv.PassengerLogic;
import ttg.view.adv.ctrl.PassengerTableModel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewPassengersDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private PassengerTableModel mPassengerModel;
	private TableSorter mPassengerSorter;
	
	private JTable		mPassengers;
	private JButton		mCancel;
	private JButton		mDisembark;
	
	/**
	 *
	 */

	public ViewPassengersDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setTitle(mGame.getShip().getName()+"'s Passengers");
		mPassengerModel = new PassengerTableModel();
		mPassengerModel.removeColumn(PassengerTableModel.COL_TITLE);
		mPassengerModel.removeColumn(PassengerTableModel.COL_UPP);
		mPassengerModel.removeColumn(PassengerTableModel.COL_AGE);
		//mPassengerModel.removeColumn(PassengerTableModel.COL_ORIGIN);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SKILL1);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SKILL2);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SALARY);
		mCancel = new JButton("Done", TTGIconUtils.getIcon("tb_cancel.gif"));        
		mDisembark = new JButton("Disembark");
		mPassengerSorter = new TableSorter(mPassengerModel);
		mPassengers = new JTable(mPassengerSorter);
		mPassengerSorter.addMouseListenerToHeaderInTable(mPassengers);
		updateLists();
	}

	private void initLink()
	{
	    MouseUtils.mouseClicked(mPassengers, (ev) -> {
	            if (ev.getClickCount() == 2)
	                doTableClick();
	        });
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		ListenerUtils.listen(mDisembark, (ev) -> doDisembark());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mDisembark);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mPassengers));
		pack();
	}
	
	private void updateLists()
	{
		mShip = mGame.getShip();
		if (mShip == null)
		{
			mPassengerModel.setChars(null);
			return;
		}
		mDisembark.setEnabled(mShip.isDocked());
		//System.out.println("ForSale:Updating lists, here="+mShip.getLocation()+", there="+mShip.getDestination()+".");
		Object hereObj = SchemeLogic.getFromURI(mGame.getScheme(), mShip.getLocation());
		if (!(hereObj instanceof BodySpecialAdvBean))
		{
			mPassengerModel.setChars(null);
			return;
		}
		mPassengerModel.setPassengers(mShip.getPassengers());
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doDisembark()
	{
		int[] sel = mPassengers.getSelectedRows();
		mPassengerSorter.mapRows(sel);
		List<CharBean> passengers = mPassengerModel.getChars();
		List<PassengerBean> disembarked = new ArrayList<>();
		for (int i = 0; i < sel.length; i++)
		    disembarked.add((PassengerBean)passengers.get(sel[i]));
		PassengerLogic.disembarkPassengers(mGame, disembarked);
		updateLists();
	}
	
	protected void doTableClick()
	{
	    int row = mPassengers.getSelectedRow();
	    if (row < 0)
	        return;
	    /*
	    PassengerBean cargo = (PassengerBean)mPassengerModel.getChars().get(mPassengerSorter.mapRow(row));
	    PassengerInfoDlg dlg = new PassengerInfoDlg(this, mGame, cargo);
	    dlg.setVisible(true);
	     */
	}
}
