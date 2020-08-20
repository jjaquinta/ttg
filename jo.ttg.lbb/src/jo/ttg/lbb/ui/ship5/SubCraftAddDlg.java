package jo.ttg.lbb.ui.ship5;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class SubCraftAddDlg extends JDialog
{
    private Ship5Design                       mShip;
    private Ship5Design.Ship5DesignSubCraft   mCraft;

    private JTextField                        mShipNameCtrl;
    private JSpinner                          mHullTonnageCtrl;
    private JSpinner                          mCostCtrl;
    private JSpinner                          mCrewCtrl;
    private JSpinner                          mQuantityCtrl;
    private JCheckBox                         mVehicleCtrl;
    private JButton                           mOK;
    private JButton                           mCancel;

    /**
     *
     */

    public SubCraftAddDlg(JFrame parent, Ship5Design ship)
    {
        super(parent, "Add Sub Craft", Dialog.ModalityType.DOCUMENT_MODAL);
        mShip = ship;
        mCraft = mShip.new Ship5DesignSubCraft();
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setTitle("Add Launch Tube");
        mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));
        mCancel = new JButton("Cancel");
        mShipNameCtrl = new JTextField(24);
        mHullTonnageCtrl = new JSpinner(new SpinnerNumberModel(mCraft.getQuantity(), 1, 100000, 1));
        mCostCtrl = new JSpinner(new SpinnerNumberModel(mCraft.getCrew(), 0, 10000000000.0, 1));
        mCrewCtrl = new JSpinner(new SpinnerNumberModel(mCraft.getCrew(), 0, 10000, 1));
        mVehicleCtrl = new JCheckBox("Vehicle");
        mQuantityCtrl = new JSpinner(new SpinnerNumberModel(mCraft.getQuantity(), 1, 100000, 1));
    }

    private void initLink()
    {
        ListenerUtils.listen(mOK, (e) -> doOK());
        ListenerUtils.listen(mCancel, (e) -> doCancel());
        BeanMapUtils.map(mShipNameCtrl, mCraft, "shipName");
        BeanMapUtils.map(mHullTonnageCtrl, mCraft, "hullTonnage");
        BeanMapUtils.map(mCostCtrl, mCraft, "cost");
        BeanMapUtils.map(mCrewCtrl, mCraft, "crew");
        BeanMapUtils.map(mVehicleCtrl, mCraft, "vehicle");
        BeanMapUtils.map(mQuantityCtrl, mCraft, "quantity");
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(mOK);
        buttonBar.add(mCancel);

        JPanel client = new JPanel();
        client.setLayout(new GridLayout(2, 2));
        client.add(new JLabel("Name:"));
        client.add(mShipNameCtrl);
        client.add(new JLabel("Tonnage:"));
        client.add(mHullTonnageCtrl);
        client.add(new JLabel("Cost:"));
        client.add(mCostCtrl);
        client.add(new JLabel("Crew:"));
        client.add(mCrewCtrl);
        client.add(new JLabel(""));
        client.add(mVehicleCtrl);
        client.add(new JLabel("Quantity:"));
        client.add(mQuantityCtrl);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("North",
                new JLabel("Select parameters for contained sub craft"));
        getContentPane().add("South", buttonBar);
        getContentPane().add("Center", client);
        setSize(640, 480);
    }

    protected void doOK()
    {
        mShip.getSubCraft().add(mCraft);
        mShip.fireMonotonicPropertyChange("subCraft", mShip.getLaunchTubes());
        BeanMapUtils.unmap(mShipNameCtrl, mCraft, "shipName");
        BeanMapUtils.unmap(mHullTonnageCtrl, mCraft, "hullTonnage");
        BeanMapUtils.unmap(mCostCtrl, mCraft, "cost");
        BeanMapUtils.unmap(mCrewCtrl, mCraft, "crew");
        BeanMapUtils.unmap(mVehicleCtrl, mCraft, "vehicle");
        BeanMapUtils.unmap(mQuantityCtrl, mCraft, "quantity");
        dispose();
    }

    protected void doCancel()
    {
        BeanMapUtils.unmap(mShipNameCtrl, mCraft, "shipName");
        BeanMapUtils.unmap(mHullTonnageCtrl, mCraft, "hullTonnage");
        BeanMapUtils.unmap(mCostCtrl, mCraft, "cost");
        BeanMapUtils.unmap(mCrewCtrl, mCraft, "crew");
        BeanMapUtils.unmap(mVehicleCtrl, mCraft, "vehicle");
        BeanMapUtils.unmap(mQuantityCtrl, mCraft, "quantity");
        dispose();
    }

}
