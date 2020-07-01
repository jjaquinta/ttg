package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;

import jo.ttg.deckplans.logic.RuntimeLogic;
import jo.ttg.deckplans.logic.ScanLogic;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.obj.IntegerUtils;

public class ScanViewer extends JPanel
{
    private PlanItemTableModel mPlanItemTableModel;

    private JComboBox<String>  mConfiguration;
    private JSpinner           mVolume;
    private JScrollPane        mScroller;
    private JTable             mClient;

    public ScanViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        mPlanItemTableModel = new PlanItemTableModel(scan);
        mClient = new JTable(mPlanItemTableModel);
        mClient.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mScroller = new JScrollPane(mClient);
        mConfiguration = new JComboBox<>(new DefaultComboBoxModel<>(Hull.hullDescription));
        mConfiguration.setSelectedIndex(scan.getConfiguration());
        mVolume = new JSpinner(new SpinnerNumberModel(scan.getVolume(), 1350, 1000000, 100));
    }

    private void initLayout()
    {
        JPanel data = new JPanel();
        data.setLayout(new TableLayout());
        data.add("1,+", new JLabel("Configuration:"));
        data.add("+,. fill=h", mConfiguration);
        data.add("+,.", new JLabel("Volume (m\u00b3):"));
        data.add("+,. fill=h", mVolume);
        
        setLayout(new BorderLayout());
        add("North", data);
        add("Center", mScroller);
    }

    private void initLink()
    {
        RuntimeLogic.listen("shipScan.configuration", (ov,nv) -> doDataUpdateConfiguration());
        RuntimeLogic.listen("shipScan.volume", (ov,nv) -> doDataUpdateVolume());
        ListenerUtils.listen(mConfiguration, (ev) -> doUIUpdateConfiguration());
        ListenerUtils.change(mVolume, (ev) -> doUIUpdateVolume());
    }
    
    private void doDataUpdateConfiguration()
    {
        int newValue = RuntimeLogic.getInstance().getShipScan().getConfiguration();
        if (mConfiguration.getSelectedIndex() != newValue)
            mConfiguration.setSelectedIndex(newValue);
    }
    
    private void doDataUpdateVolume()
    {
        int newValue = RuntimeLogic.getInstance().getShipScan().getVolume();
        if (!mVolume.getValue().equals(newValue))
            mVolume.setValue(newValue);
    }
    
    private void doUIUpdateConfiguration()
    {
        ScanLogic.setConfiguration(mConfiguration.getSelectedIndex());
    }
    
    private void doUIUpdateVolume()
    {
        ScanLogic.setVolume(IntegerUtils.parseInt(mVolume.getValue()));
    }
}
