/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.ctrl.CharTableModel;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.utils.URIUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.CrewBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;
import ttg.logic.adv.BuyLogic;
import ttg.logic.adv.ForSaleLogic;
import ttg.logic.adv.PassengerLogic;
import ttg.view.adv.ctrl.SelectedCapacityPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSaleCrewDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private CharTableModel	mCrewModel;
	private TableSorter mCrewSorter;
	
	private SelectedCapacityPanel	mCabins;
	private SelectedCapacityPanel	mBerths;
	private JTable	mCrew;
	private JButton	mBuyCrew;
	private JButton	mCancel;
	
	/**
	 *
	 */

	public ForSaleCrewDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setTitle(URIUtils.extractName(mGame.getShip().getLocation())+" EmploymentExchange");
		mCrewModel = new CharTableModel();
		mCrewModel.removeColumn(CharTableModel.COL_SALARY);
		mCancel = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCrewSorter = new TableSorter(mCrewModel);
		mCrew = new JTable(mCrewSorter);
		mCrewSorter.addMouseListenerToHeaderInTable(mCrew);
		mBuyCrew = new JButton("Hire");
		mCabins = new SelectedCapacityPanel("int");
		mCabins.setBorder(BorderFactory.createTitledBorder("Cabins"));
		mBerths = new SelectedCapacityPanel("int");
		mBerths.setBorder(BorderFactory.createTitledBorder("Berths"));
		updateLists();
		updateCapacity();
	}

	private void initLink()
	{
	    ListenerUtils.change(mCrew, (ev) -> updateCapacity());
	    MouseUtils.mouseClicked(mCrew, (ev) -> {
            if (ev.getClickCount() == 2)
                doTableClick();
        });
		ListenerUtils.listen(mCancel, (e) -> doCancel());
		ListenerUtils.listen(mBuyCrew, (e) -> doHireCrew());
	}

	private void initLayout()
	{
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 2));
		statusBar.add(mCabins);
		statusBar.add(mBerths);
		
		JPanel buttonBar = new JPanel();
		buttonBar.add(mBuyCrew);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mCrew));
		getContentPane().add("North", statusBar);
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
		Object hereObj = SchemeLogic.getFromURI(mGame.getScheme(), mShip.getLocation());
		if (!(hereObj instanceof BodySpecialAdvBean))
		{
			mCrewModel.setChars(null);
		}
		else
		{
		    BodySpecialAdvBean here = (BodySpecialAdvBean)hereObj;
			DateBean date = mGame.getDate();
			ArrayList crew = ForSaleLogic.genStaffForHire(mGame, here, date);
			mCrewModel.setChars(crew);
		}
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doHireCrew()
	{
		int[] sel = mCrew.getSelectedRows();
		mCrewSorter.mapRows(sel);
		List<CharBean> crew = mCrewModel.getChars();
		for (int i = 0; i < sel.length; i++)
			if (!BuyLogic.hireCrew(mGame, mShip, (CrewBean)crew.get(sel[i])))
			    break;
		updateLists();
		updateCapacity();
	}
	
	protected void doTableClick()
	{
	    int row = mCrew.getSelectedRow();
	    if (row < 0)
	        return;
	    CrewBean crew = (CrewBean)mCrewModel.getChars().get(mCrewSorter.mapRow(row));
	    CharInfoDlg dlg = new CharInfoDlg(this, mGame, crew);
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void updateCapacity()
	{
	    int crew = 0;
		int[] sel = mCrew.getSelectedRows();
		mCrewSorter.mapRows(sel);
		List<CharBean> crews = mCrewModel.getChars();
		for (int i = 0; i < sel.length; i++)
		{
			crew++;
		}
		int cabinPassengers = PassengerLogic.totalCabins(mGame.getShip().getPassengers());
		int berthPassengers = PassengerLogic.totalBerths(mGame.getShip().getPassengers());
		mCabins.setQuanCapacity(mGame.getShip().getStats().getStaterooms()
		        - mGame.getShip().getCrew().size() - cabinPassengers);
		mCabins.setQuanSelected(crew);
		mBerths.setQuanCapacity(mGame.getShip().getStats().getStaterooms()
		        - berthPassengers);
		mBerths.setQuanSelected(0);
	}
}
