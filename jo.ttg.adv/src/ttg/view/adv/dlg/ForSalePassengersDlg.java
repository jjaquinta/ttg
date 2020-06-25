/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocationURI;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.utils.URIUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.ui.swing.utils.TableSorter;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.beans.adv.ShipInst;
import ttg.logic.adv.BuyLogic;
import ttg.logic.adv.ForSaleLogic;
import ttg.logic.adv.ForSaleReportLogic;
import ttg.logic.adv.PassengerLogic;
import ttg.view.adv.ctrl.PassengerTableModel;
import ttg.view.adv.ctrl.SelectedCapacityPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSalePassengersDlg extends JDialog
{
	private Game		mGame;
	private ShipInst	mShip;
	private Map<String,List<PassengerBean>>		mPassengersMap;
	private PassengerTableModel mPassengerModel;
	private TableSorter mPassengerSorter;
	
	private SelectedCapacityPanel	mCabins;
	private SelectedCapacityPanel	mBerths;
	private JTable		mPassengers;
	private JComboBox<String>	mDestinations;
	private JButton		mBuyPassengers;
	private JButton		mCancel;
	private JButton		mReport;
	
	/**
	 *
	 */

	public ForSalePassengersDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setTitle(URIUtils.extractName(mGame.getShip().getLocation())+" Departing Passengers");
	    mPassengersMap = new HashMap<>();
		mPassengerModel = new PassengerTableModel();
		mPassengerModel.removeColumn(PassengerTableModel.COL_TITLE);
		mPassengerModel.removeColumn(PassengerTableModel.COL_UPP);
		mPassengerModel.removeColumn(PassengerTableModel.COL_AGE);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SKILL1);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SKILL2);
		mPassengerModel.removeColumn(PassengerTableModel.COL_SALARY);
		mPassengerModel.removeColumn(PassengerTableModel.COL_ORIGIN);
		mPassengerModel.removeColumn(PassengerTableModel.COL_BORDED);
		mCancel = new JButton("Done", TTGIconUtils.getIcon("tb_cancel.gif"));        
		mPassengerSorter = new TableSorter(mPassengerModel);
		mPassengers = new JTable(mPassengerSorter);
		mPassengerSorter.addMouseListenerToHeaderInTable(mPassengers);
		mBuyPassengers = new JButton("Contract");
		mReport = new JButton("Report");
		mDestinations = new JComboBox<>();
		mCabins = new SelectedCapacityPanel("int");
		mCabins.setBorder(BorderFactory.createTitledBorder("Cabins"));
		mBerths = new SelectedCapacityPanel("int");
		mBerths.setBorder(BorderFactory.createTitledBorder("Berths"));
		updateLists();
		updateCapacity();
	}

	private void initLink()
	{
	    ListenerUtils.change(mPassengers, (ev) -> updateCapacity());
	    MouseUtils.mouseClicked(mPassengers, (ev) -> {
	            if (ev.getClickCount() == 2)
	                doTableClick();
	        });
		ListenerUtils.listen(mCancel, (e) -> doCancel());
		ListenerUtils.listen(mBuyPassengers, (e) -> doBuyPassengers());
		ListenerUtils.listen(mDestinations, (e) -> doNewDestination());
		ListenerUtils.listen(mReport, (e) -> doReport());
	}

    private void initLayout()
	{	    
		JPanel statusBar = new JPanel();
		statusBar.setLayout(new GridLayout(1, 2));
		statusBar.add(mCabins);
		statusBar.add(mBerths);
		
		JPanel buttonBar = new JPanel();
		buttonBar.add(mReport);
		buttonBar.add(new JLabel(" Destination:"));
		buttonBar.add(mDestinations);
		buttonBar.add(mBuyPassengers);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mPassengers));
		getContentPane().add("North", statusBar);
		pack();
	}
	
	private void updateLists()
	{
	    mPassengersMap.clear();
		mShip = mGame.getShip();
		if (mShip == null)
		{
			mPassengerModel.setChars(null);
			return;
		}
		//System.out.println("ForSale:Updating lists, here="+mShip.getLocation()+", there="+mShip.getDestination()+".");
		Object hereObj = SchemeLogic.getFromURI(mGame.getScheme(), mShip.getLocation());
		if (!(hereObj instanceof BodySpecialAdvBean))
		{
			mPassengerModel.setChars(null);
			return;
		}
		DateBean date = mGame.getDate();
	    BodySpecialAdvBean here = (BodySpecialAdvBean)hereObj;
	    List<MainWorldBean> worlds = SchemeLogic.getWorldsWithin(mGame.getScheme(), here.getSystem().getOrds(), mShip.getStats().getJump());
	    List<String> destinations = new ArrayList<>();
	    destinations.add("Show All");
	    LocationURI destinationURI = new LocationURI(mShip.getDestination());
	    String shipDestination = null;
	    for (MainWorldBean mw : worlds)
	    {
	        //System.out.println("ForSalePassengersDlg.updateLists, world="+mw.getName());
	        SystemBean thereSys = mGame.getScheme().getGeneratorSystem().generateSystem(mw.getOrds());
	        for (Iterator<BodyBean> j = thereSys.getSystemRoot().getAllSatelitesIterator(); j.hasNext(); )
	        {
	            BodyBean thereObj = j.next();
	            if (thereObj instanceof BodySpecialAdvBean)
	            {
	                BodySpecialAdvBean there = (BodySpecialAdvBean)thereObj;
	    	        //System.out.println("ForSalePassengersDlg.updateLists,   body="+there.getName());
	        		LocationURI thereURI = new LocationURI(there.getURI());
	                if (here.getURI().equals(thereURI.getURI()))
	                    continue;
	        		List<PassengerBean> pasengers = ForSaleLogic.genPassengersForSale(mGame, here, there, date);
	        		if (pasengers.size() == 0)
	        		    continue;	        		
	        		String name = there.getName();
	        		if (name.indexOf(there.getSystem().getName()) < 0)
	        		    name += " ("+there.getSystem().getName()+" System)";
	        		name += ", "+pasengers.size()+"";
	           		mPassengersMap.put(name, pasengers);
	        		destinations.add(name);
	        		if ((shipDestination == null) && destinationURI.equals(thereURI))
	        		    shipDestination = name; 
	            }
	        }
	    }
	    String oldDest = (String)mDestinations.getSelectedItem();
	    if ((oldDest == null) || !destinations.contains(oldDest))
	        if (shipDestination != null)
	            oldDest = shipDestination;
	        else
	            oldDest = "Show All";
	    mDestinations.setModel(new DefaultComboBoxModel<String>(destinations.toArray(new String[0])));
	    mDestinations.setSelectedItem(oldDest);
	    doNewDestination();
	}

	/**
     * 
     */
    protected void doNewDestination()
    {
        String name = (String)mDestinations.getSelectedItem();
        List<PassengerBean> passengers;
        if (name.equals("Show All"))
        {
            passengers = new ArrayList<>();
            for (List<PassengerBean> a : mPassengersMap.values())
                passengers.addAll(a);
        }
        else
        {
            passengers = mPassengersMap.get(name);
        }
		mPassengerModel.setPassengers(passengers);
    }

	protected void doCancel()
	{
		dispose();
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
	    html.append("<head><title>Passenger Trade Report</title></head>");
	    html.append("<body>");
	    html.append(ForSaleReportLogic.genIntraSystemPassengerReport(
	            mGame.getScheme(), sys, mGame.getDate()));
	    html.append("</body>");
	    html.append("</html>");
	    ReportDlg dlg = new ReportDlg(this, html.toString());
	    dlg.setModal(true);
	    dlg.setVisible(true);
	}

	protected void doBuyPassengers()
	{
		int[] sel = mPassengers.getSelectedRows();
		mPassengerSorter.mapRows(sel);
		List<CharBean> passengers = mPassengerModel.getChars();
		for (int i = 0; i < sel.length; i++)
			if (!BuyLogic.buyPassenger(mGame, mShip, (PassengerBean)passengers.get(sel[i])))
			    break;
		updateLists();
		updateCapacity();
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
	
	protected void updateCapacity()
	{
		int[] sel = mPassengers.getSelectedRows();
		mPassengerSorter.mapRows(sel);
		List<CharBean> passengers = mPassengerModel.getChars();
	    int cabins = 0;
	    int berths = 0;
		for (int i = 0; i < sel.length; i++)
		{
			if (((PassengerBean)passengers.get(sel[i])).getPassage() == PassengerBean.PASSAGE_LOW)
			    berths++;
			else
			    cabins++;
		}
		int cabinPassengers = PassengerLogic.totalCabins(mGame.getShip().getPassengers());
		int berthPassengers = PassengerLogic.totalBerths(mGame.getShip().getPassengers());
		mCabins.setQuanCapacity(mGame.getShip().getStats().getStaterooms()
		        - mGame.getShip().getCrew().size() - cabinPassengers);
		mCabins.setQuanSelected(cabins);
		mBerths.setQuanCapacity(mGame.getShip().getStats().getLowBerths()
		        - berthPassengers);
		mBerths.setQuanSelected(berths);
	}
}
