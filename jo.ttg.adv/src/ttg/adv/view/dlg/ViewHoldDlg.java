/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.CargoLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.adv.beans.AdvCargoBean;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;
import ttg.adv.logic.SellLogic;
import ttg.adv.view.ctrl.AdvCargoTableModel;
import ttg.adv.view.ctrl.SelectedCapacityPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ViewHoldDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private AdvCargoTableModel	mHoldModel;
	private TableSorter mCargoSorter;
	
	private JTable	mHold;
	private SelectedCapacityPanel	mTons;
	private SelectedCapacityPanel	mDosh;
	private JButton	mSellCargo;
	private JButton	mDitchCargo;
	private JButton	mCancel;
	
	/**
	 *
	 */

	public ViewHoldDlg(JFrame parent, Game game)
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
	    setTitle(mShip.getName()+"'s Hold");
		mHoldModel = new AdvCargoTableModel();
		mHoldModel.setScheme(mGame.getScheme());
		mHoldModel.setNow(mGame.getDate());
		mHoldModel.removeColumn(AdvCargoTableModel.COL_LEGALITY);
		mCancel = new JButton("Done", TTGIconUtils.getIcon("tb_cancel.gif"));        
		mCargoSorter = new TableSorter(mHoldModel);
		mHold = new JTable(mCargoSorter);
		mCargoSorter.addMouseListenerToHeaderInTable(mHold);
		mSellCargo = new JButton("Sell");
		mSellCargo.setEnabled(mShip.isDocked());
		mDitchCargo = new JButton("Ditch");
		mTons = new SelectedCapacityPanel("tons");
		mTons.setBorder(BorderFactory.createTitledBorder("Volume"));
		mDosh = new SelectedCapacityPanel("currency");
		mDosh.setBorder(BorderFactory.createTitledBorder("Cost"));
		updateLists();
		updateCapacity();
	}

	private void initLink()
	{
	    ListenerUtils.change(mHold, (ev) -> updateCapacity());
	    MouseUtils.mouseClicked(mHold, (ev) -> {
	    	            if (ev.getClickCount() == 2)
	    	                doTableClick();
	    	        });
		ListenerUtils.listen(mCancel, (e) -> doCancel());
		ListenerUtils.listen(mSellCargo, (e) -> doSellCargo());
		ListenerUtils.listen(mDitchCargo, (e) -> doDitchCargo());
	}

	private void initLayout()
	{
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 2));
		statusBar.add(mTons);
		statusBar.add(mDosh);
		
		JPanel buttonBar = new JPanel();
		buttonBar.add(mSellCargo);
		buttonBar.add(mDitchCargo);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mHold));
		getContentPane().add("North", statusBar);
		setSize(640, 480);
	}
	
	private void updateLists()
	{
		//System.out.println("ForSale:Updating lists.");
		mShip = mGame.getShip();
		if (mShip == null)
		{
			mHoldModel.setCargos(null);
			return;
		}
		mHoldModel.setAt(mShip.getLocation());
		mHoldModel.setCargos(mShip.getCargo());
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doSellCargo()
	{
		int[] sel = mHold.getSelectedRows();
		mCargoSorter.mapRows(sel);
		List<CargoBean> cargos = mHoldModel.getCargos();
		for (int i = 0; i < sel.length; i++)
			SellLogic.sellCargo(mGame, mShip, (AdvCargoBean)cargos.get(sel[i]));
		updateLists();
		updateCapacity();
	}

	protected void doDitchCargo()
	{
		int[] sel = mHold.getSelectedRows();
		mCargoSorter.mapRows(sel);
		List<CargoBean> cargos = mHoldModel.getCargos();
		for (int i = 0; i < sel.length; i++)
			SellLogic.ditchCargo(mGame, mShip, (AdvCargoBean)cargos.get(sel[i]));
		updateLists();
		updateCapacity();
	}
	
	protected void doTableClick()
	{
	    int row = mHold.getSelectedRow();
	    if (row < 0)
	        return;
	    AdvCargoBean cargo = (AdvCargoBean)mHoldModel.getCargos().get(mCargoSorter.mapRow(row));
	    CargoInfoDlg dlg = new CargoInfoDlg(this, mGame, cargo);
	    dlg.setVisible(true);
	}
	
	protected void updateCapacity()
	{
	    int tons = 0;
	    double dosh = 0.0;
		int[] sel = mHold.getSelectedRows();
		mCargoSorter.mapRows(sel);
		List<CargoBean> cargos = mHoldModel.getCargos();
		for (int i = 0; i < sel.length; i++)
		{
			tons += ((AdvCargoBean)cargos.get(sel[i])).getQuantity();
			dosh += ((AdvCargoBean)cargos.get(sel[i])).getPurchasePrice();
		}
		mTons.setQuanCapacity(mGame.getShip().getStats().getCargo()/13.5 - CargoLogic.totalTons(mGame.getShip().getCargo()));
		mTons.setQuanSelected(-tons);
		mDosh.setQuanCapacity(mGame.getAccounts().getCash());
		mDosh.setQuanSelected(-dosh);
	}
}
