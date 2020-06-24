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
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.ctrl.CargoTableModel;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.CargoLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.utils.URIUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.beans.adv.AdvCargoBean;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;
import ttg.logic.adv.BuyLogic;
import ttg.logic.adv.ForSaleLogic;
import ttg.logic.adv.ForSaleReportLogic;
import ttg.view.adv.ctrl.AdvCargoTableModel;
import ttg.view.adv.ctrl.SelectedCapacityPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSaleCargoDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private AdvCargoTableModel	mCargoModel;
	private TableSorter	mCargoSorter;
	
	private JTable	mCargo;
	private SelectedCapacityPanel	mTons;
	private SelectedCapacityPanel	mDosh;
	private JButton	mBuyCargo;
	private JButton	mCancel;
	private JButton	mReport;
	
	/**
	 *
	 */

	public ForSaleCargoDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setTitle(URIUtils.extractName(mGame.getShip().getLocation())+" Cargo Market");
		mCargoModel = new AdvCargoTableModel();
		mCargoModel.setScheme(mGame.getScheme());
		mCargoModel.removeColumn(CargoTableModel.COL_ORIGIN);
		mCargoModel.removeColumn(CargoTableModel.COL_DESTINATION);
		mCargoModel.removeColumn(CargoTableModel.COL_TOSELL);
		mCargoModel.removeColumn(AdvCargoTableModel.COL_DELIVERED);
		mCargoModel.removeColumn(AdvCargoTableModel.COL_LEGALITY);
		mCancel = new JButton("Done", TTGIconUtils.getIcon("tb_cancel.gif"));        
		mCargoSorter = new TableSorter(mCargoModel);
		mCargo = new JTable(mCargoSorter);
		mCargoSorter.addMouseListenerToHeaderInTable(mCargo);
		mBuyCargo = new JButton("Buy");
		mReport = new JButton("Report");
		mTons = new SelectedCapacityPanel("tons");
		mTons.setBorder(BorderFactory.createTitledBorder("Volume"));
		mDosh = new SelectedCapacityPanel("currency");
		mDosh.setBorder(BorderFactory.createTitledBorder("Cost"));
		updateLists();
		updateCapacity();
	}

	private void initLink()
	{
        ListenerUtils.change(mCargo, (e) -> updateCapacity());
        MouseUtils.mouseClicked(mCargo, (ev) -> {
	    	            if (ev.getClickCount() == 2)
	    	                doTableClick();
	    	        });
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		ListenerUtils.listen(mBuyCargo, (ev) -> doBuyCargo());
		ListenerUtils.listen(mReport, (ev) -> doReport());
	}

	private void initLayout()
	{
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 2));
		statusBar.add(mTons);
		statusBar.add(mDosh);
		
		JPanel buttonBar = new JPanel();
		buttonBar.add(mReport);
		buttonBar.add(mBuyCargo);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mCargo));
		getContentPane().add("North", statusBar);
		setSize(640, 480);
	}
	
	private void updateLists()
	{
		//System.out.println("ForSale:Updating lists.");
		mShip = mGame.getShip();
		if (mShip == null)
		{
			mCargoModel.setCargos(null);
			return;
		}
		Object hereObj = SchemeLogic.getFromURI(mGame.getScheme(), mShip.getLocation());
		if (!(hereObj instanceof BodySpecialAdvBean))
		{
			mCargoModel.setCargos(null);
		}
		else
		{
		    BodySpecialAdvBean here = (BodySpecialAdvBean)hereObj;
			DateBean date = mGame.getDate();
			List<CargoBean> cargos = ForSaleLogic.genCargosForSale(mGame, here, date);
			mCargoModel.setCargos(cargos);
		}
	}

	protected void doCancel()
	{
		dispose();
	}

	protected void doBuyCargo()
	{
		int[] sel = mCargo.getSelectedRows();
		mCargoSorter.mapRows(sel);
		List<CargoBean> cargos = mCargoModel.getCargos();
		for (int i = 0; i < sel.length; i++)
			if (!BuyLogic.buyCargo(mGame, mShip, (AdvCargoBean)cargos.get(sel[i])))
			    break;
		updateLists();
		updateCapacity();
	}
	
	protected void doReport()
	{
	    SystemBean sys;
	    Object loc = SchemeLogic.getFromURI(mGame.getScheme(), mGame.getShip().getLocation());
	    if (loc instanceof BodyBean)
	        sys = ((BodyBean)loc).getSystem();
	    else if (loc instanceof SystemBean)
	        sys = (SystemBean)loc;
	    else
	        return;
	    StringBuffer html = new StringBuffer();
	    html.append("<html>");
	    html.append("<head><title>Cargo Trade Report</title></head>");
	    html.append("<body>");
	    html.append(ForSaleReportLogic.genIntraSystemCargoReport(
	            mGame.getScheme(), sys, mGame.getDate()));
	    html.append("</body>");
	    html.append("</html>");
	    ReportDlg dlg = new ReportDlg(this, html.toString());
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void doTableClick()
	{
	    int row = mCargo.getSelectedRow();
	    if (row < 0)
	        return;
	    AdvCargoBean cargo = (AdvCargoBean)mCargoModel.getCargos().get(mCargoSorter.mapRow(row));
	    CargoInfoDlg dlg = new CargoInfoDlg(this, mGame, cargo);
	    dlg.show();
	}
	
	protected void updateCapacity()
	{
	    int tons = 0;
	    double dosh = 0.0;
		int[] sel = mCargo.getSelectedRows();
		mCargoSorter.mapRows(sel);
		List<CargoBean> cargos = mCargoModel.getCargos();
		for (int i = 0; i < sel.length; i++)
		{
			tons += ((AdvCargoBean)cargos.get(sel[i])).getQuantity();
			dosh += ((AdvCargoBean)cargos.get(sel[i])).getPurchasePrice();
		}
		mTons.setQuanCapacity(mGame.getShip().getStats().getCargo()/13.5 - CargoLogic.totalTons(mGame.getShip().getCargo()));
		mTons.setQuanSelected(tons);
		mDosh.setQuanCapacity(mGame.getAccounts().getCash());
		mDosh.setQuanSelected(dosh);
	}
}
