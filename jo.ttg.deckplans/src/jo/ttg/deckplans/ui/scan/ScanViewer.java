package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
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
import jo.util.geom3d.Point3D;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;

public class ScanViewer extends JPanel
{
    private PlanItemTableModel mPlanItemTableModel;

    private JComboBox<String>  mConfiguration;
    private JSpinner           mAspectX;
    private JSpinner           mAspectY;
    private JSpinner           mAspectZ;
    private JComboBox<String>  mOrientation;
    private JSpinner           mVolume;
    private JLabel             mProfile;
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
        mOrientation = new JComboBox<>(new DefaultComboBoxModel<>(ShipScanBean.orientationDescription));
        mOrientation.setSelectedIndex(scan.getOrientation());
        mAspectX = new JSpinner(new SpinnerNumberModel(scan.getAspectRatio().x, 1, 100, 1));
        mAspectY = new JSpinner(new SpinnerNumberModel(scan.getAspectRatio().y, 1, 100, 1));
        mAspectZ = new JSpinner(new SpinnerNumberModel(scan.getAspectRatio().z, 1, 100, 1));
        mVolume = new JSpinner(new SpinnerNumberModel(scan.getVolume(), 1350, 1000000, 100));
        mProfile = new JLabel();
    }

    private void initLayout()
    {
        JPanel data = new JPanel();
        data.setLayout(new TableLayout());
        data.add("1,+", new JLabel("Configuration:"));
        data.add("+,. fill=h", mConfiguration);
        data.add("+,.", new JLabel("Aspect Ratio:"));
        data.add("+,. fill=h", mAspectX);
        data.add("+,. fill=h", mAspectY);
        data.add("+,. fill=h", mAspectZ);
        data.add("+,.", new JLabel("Orientation:"));
        data.add("+,. fill=h", mOrientation);
        data.add("+,.", new JLabel("Volume (m\u00b3):"));
        data.add("+,. fill=h", mVolume);
        data.add("+,. fill=hv", mProfile);
        
        setLayout(new BorderLayout());
        add("North", data);
        add("Center", mScroller);
    }

    private void initLink()
    {
        RuntimeLogic.listen("shipScan.configuration", (ov,nv) -> doDataUpdateConfiguration());
        RuntimeLogic.listen("shipScan.orientation", (ov,nv) -> doDataUpdateOrientation());
        RuntimeLogic.listen("shipScan.aspectRatio", (ov,nv) -> doDataUpdateAspect());
        RuntimeLogic.listen("shipScan.volume", (ov,nv) -> doDataUpdateVolume());
        ListenerUtils.listen(mConfiguration, (ev) -> doUIUpdateConfiguration());
        ListenerUtils.listen(mOrientation, (ev) -> doUIUpdateOrientation());
        ListenerUtils.change(mAspectX, (ev) -> doUIUpdateAspect());
        ListenerUtils.change(mAspectY, (ev) -> doUIUpdateAspect());
        ListenerUtils.change(mAspectZ, (ev) -> doUIUpdateAspect());
        ListenerUtils.change(mVolume, (ev) -> doUIUpdateVolume());
    }
    
    private void doDataUpdateConfiguration()
    {
        int newValue = RuntimeLogic.getInstance().getShipScan().getConfiguration();
        if (mConfiguration.getSelectedIndex() != newValue)
            mConfiguration.setSelectedIndex(newValue);
        updateProfile();
    }
    
    private void doDataUpdateOrientation()
    {
        int newValue = RuntimeLogic.getInstance().getShipScan().getOrientation();
        if (mOrientation.getSelectedIndex() != newValue)
            mOrientation.setSelectedIndex(newValue);
        updateProfile();
    }
    
    private void doDataUpdateAspect()
    {
        Point3D newValue = RuntimeLogic.getInstance().getShipScan().getAspectRatio();
        if (!mAspectX.getValue().equals(newValue.x))
            mAspectX.setValue(newValue.x);
        if (!mAspectY.getValue().equals(newValue.y))
            mAspectY.setValue(newValue.y);
        if (!mAspectZ.getValue().equals(newValue.z))
            mAspectZ.setValue(newValue.z);
        updateProfile();
    }
    
    private void doDataUpdateVolume()
    {
        int newValue = RuntimeLogic.getInstance().getShipScan().getVolume();
        if (!mVolume.getValue().equals(newValue))
            mVolume.setValue(newValue);
        updateProfile();
    }
    
    private void updateProfile()
    {
        BufferedImage img = ScanLogic.scanToProfile(RuntimeLogic.getInstance().getShipScan());
        ImageIcon icon = new ImageIcon(img);
        mProfile.setIcon(icon);
    }
    
    private void doUIUpdateConfiguration()
    {
        ScanLogic.setConfiguration(mConfiguration.getSelectedIndex());
    }
    
    private void doUIUpdateOrientation()
    {
        ScanLogic.setOrientation(mOrientation.getSelectedIndex());
    }
    
    private void doUIUpdateAspect()
    {
        ScanLogic.setAspectRatio(DoubleUtils.parseDouble(mAspectX.getValue()), DoubleUtils.parseDouble(mAspectY.getValue()), DoubleUtils.parseDouble(mAspectZ.getValue()));
    }
    
    private void doUIUpdateVolume()
    {
        ScanLogic.setVolume(IntegerUtils.parseInt(mVolume.getValue()));
    }
}
