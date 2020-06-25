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
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.ctrl.CargoTableModel;
import jo.ttg.core.ui.swing.logic.FormatUtils;
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
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ForSaleFreightDlg extends JDialog
{
    private Game                         mGame;
    private ShipInst                     mShip;
    private Map<String, List<CargoBean>> mFreightMap;
    private AdvCargoTableModel           mFreightModel;
    private TableSorter                  mFreightSorter;

    private JTable                       mFreight;
    private JComboBox<String>            mDestinations;
    private JButton                      mBuyFreight;
    private JButton                      mReport;
    private JButton                      mCancel;
    private SelectedCapacityPanel        mTons;
    private SelectedCapacityPanel        mDosh;

    /**
     *
     */

    public ForSaleFreightDlg(JFrame parent, Game game)
    {
        super(parent);
        mGame = game;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setTitle(URIUtils.extractName(mGame.getShip().getLocation())
                + " Freight Yard");
        mFreightMap = new HashMap<>();
        mFreightModel = new AdvCargoTableModel();
        mFreightModel.setScheme(mGame.getScheme());
        mFreightModel.removeColumn(CargoTableModel.COL_ORIGIN);
        mFreightModel.removeColumn(CargoTableModel.COL_TOSELL);
        mFreightModel.removeColumn(AdvCargoTableModel.COL_DELIVERED);
        mFreightModel.removeColumn(AdvCargoTableModel.COL_TYPE);
        mFreightModel.removeColumn(AdvCargoTableModel.COL_LEGALITY);
        mCancel = new JButton("Done", TTGIconUtils.getIcon("tb_cancel.gif"));
        mFreightSorter = new TableSorter(mFreightModel);
        mFreight = new JTable(mFreightSorter);
        mFreightSorter.addMouseListenerToHeaderInTable(mFreight);
        mBuyFreight = new JButton("Buy");
        mDestinations = new JComboBox<>();
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
        ListenerUtils.change(mFreight, (ev) -> updateCapacity());
        MouseUtils.mouseClicked(mFreight, (ev) -> {
            if (ev.getClickCount() == 2)
                doTableClick();
        });
        ListenerUtils.listen(mCancel, (ev) -> doCancel());
        ListenerUtils.listen(mBuyFreight, (ev) -> doBuyFreight());
        ListenerUtils.listen(mDestinations, (ev) -> doNewDestination());
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
        buttonBar.add(new JLabel(" Destination:"));
        buttonBar.add(mDestinations);
        buttonBar.add(mBuyFreight);
        buttonBar.add(mCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("South", buttonBar);
        getContentPane().add("Center", new JScrollPane(mFreight));
        getContentPane().add("North", statusBar);
        setSize(720, 540);
    }

    private void updateLists()
    {
        mFreightMap.clear();
        mShip = mGame.getShip();
        if (mShip == null)
        {
            mFreightModel.setCargos(null);
            return;
        }
        Object hereObj = SchemeLogic.getFromURI(mGame.getScheme(),
                mShip.getLocation());
        if (!(hereObj instanceof BodySpecialAdvBean))
        {
            mFreightModel.setCargos(null);
            return;
        }
        // System.out.println("ForSale:Updating lists,
        // here="+mShip.getLocation()+", there="+mShip.getDestination()+".");
        DateBean date = mGame.getDate();
        BodySpecialAdvBean here = (BodySpecialAdvBean)hereObj;
        List<MainWorldBean> worlds = SchemeLogic.getWorldsWithin(
                mGame.getScheme(), here.getSystem().getOrds(),
                mShip.getStats().getJump());
        // System.out.println("ForSale:Updating lists,
        // #worlds="+worlds.size()+".");
        List<String> destinations = new ArrayList<>();
        destinations.add("Show All");
        LocationURI destinationURI = new LocationURI(mShip.getDestination());
        String shipDestination = null;
        for (MainWorldBean mw : worlds)
        {
            SystemBean thereSys = mGame.getScheme().getGeneratorSystem()
                    .generateSystem(mw.getOrds());
            for (Iterator<BodyBean> j = thereSys.getSystemRoot()
                    .getAllSatelitesIterator(); j.hasNext();)
            {
                BodyBean thereObj = j.next();
                if (thereObj instanceof BodySpecialAdvBean)
                {
                    BodySpecialAdvBean there = (BodySpecialAdvBean)thereObj;
                    LocationURI thereURI = new LocationURI(there.getURI());
                    if (here.getURI().equals(there.getURI()))
                        continue;
                    List<CargoBean> freights = ForSaleLogic
                            .genFreightForSale(mGame, here, there, date);
                    if (freights.size() == 0)
                        continue;
                    String name = there.getName();
                    if (name.indexOf(there.getSystem().getName()) < 0)
                        name += " (" + there.getSystem().getName() + " System)";
                    name += ", "
                            + FormatUtils.sTons(CargoLogic.totalTons(freights))
                            + "";
                    mFreightMap.put(name, freights);
                    destinations.add(name);
                    if ((shipDestination == null)
                            && (destinationURI.equals(thereURI)))
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
        mDestinations
                .setModel(new DefaultComboBoxModel<String>(destinations.toArray(new String[0])));
        mDestinations.setSelectedItem(oldDest);
        // System.out.println("ForSale:Updating lists, Done.");
        doNewDestination();
    }

    /**
     * 
     */
    protected void doNewDestination()
    {
        String name = (String)mDestinations.getSelectedItem();
        List<CargoBean> freights;
        if (name.equals("Show All"))
        {
            freights = new ArrayList<>();
            for (List<CargoBean> a : mFreightMap.values())
                freights.addAll(a);
        }
        else
        {
            freights = mFreightMap.get(name);
        }
        mFreightModel.setCargos(freights);
    }

    protected void doCancel()
    {
        dispose();
    }

    protected void doReport()
    {
        SystemBean sys;
        Object loc = SchemeLogic.getFromURI(mGame.getScheme(),
                mGame.getShip().getLocation());
        if (loc instanceof BodyBean)
            sys = ((BodyBean)loc).getSystem();
        else if (loc instanceof SystemBean)
            sys = (SystemBean)loc;
        else
            return;
        StringBuffer html = new StringBuffer();
        html.append("<html>");
        html.append("<head><title>Freight Trade Report</title></head>");
        html.append("<body>");
        html.append(ForSaleReportLogic.genIntraSystemFreightReport(
                mGame.getScheme(), sys, mGame.getDate()));
        html.append("</body>");
        html.append("</html>");
        ReportDlg dlg = new ReportDlg(this, html.toString());
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    protected void doBuyFreight()
    {
        int[] sel = mFreight.getSelectedRows();
        mFreightSorter.mapRows(sel);
        List<CargoBean> freights = mFreightModel.getCargos();
        for (int i = 0; i < sel.length; i++)
            if (!BuyLogic.buyFreight(mGame, mShip,
                    (AdvCargoBean)freights.get(sel[i])))
                break;
        updateLists();
        updateCapacity();
    }

    protected void doTableClick()
    {
        int row = mFreight.getSelectedRow();
        if (row < 0)
            return;
        AdvCargoBean cargo = (AdvCargoBean)mFreightModel.getCargos()
                .get(mFreightSorter.mapRow(row));
        CargoInfoDlg dlg = new CargoInfoDlg(this, mGame, cargo);
        dlg.setVisible(true);
    }

    protected void updateCapacity()
    {
        int tons = 0;
        double dosh = 0.0;
        int[] sel = mFreight.getSelectedRows();
        mFreightSorter.mapRows(sel);
        List<CargoBean> cargos = mFreightModel.getCargos();
        for (int i = 0; i < sel.length; i++)
        {
            tons += ((AdvCargoBean)cargos.get(sel[i])).getQuantity();
            dosh += ((AdvCargoBean)cargos.get(sel[i])).getPurchasePrice();
        }
        mTons.setQuanCapacity(mGame.getShip().getStats().getCargo() / 13.5
                - CargoLogic.totalTons(mGame.getShip().getCargo()));
        mTons.setQuanSelected(tons);
        mDosh.setQuanCapacity(mGame.getAccounts().getCash());
        mDosh.setQuanSelected(dosh);
    }
}
