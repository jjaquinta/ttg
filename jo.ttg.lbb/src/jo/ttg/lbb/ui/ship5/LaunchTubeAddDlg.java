package jo.ttg.lbb.ui.ship5;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class LaunchTubeAddDlg extends JDialog
{
    private Ship5Design                       mShip;
    private Ship5Design.Ship5DesignLaunchTube mTube;

    private JSpinner                          mCapacityCtrl;
    private JSpinner                          mQuantityCtrl;
    private JButton                           mOK;
    private JButton                           mCancel;

    /**
     *
     */

    public LaunchTubeAddDlg(JFrame parent, Ship5Design ship)
    {
        super(parent, "Add Launch Tube", Dialog.ModalityType.DOCUMENT_MODAL);
        mShip = ship;
        mTube = mShip.new Ship5DesignLaunchTube();
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setTitle("Add Launch Tube");
        mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));
        mCancel = new JButton("Cancel");
        mCapacityCtrl = new JSpinner(new SpinnerNumberModel(mTube.getCapacity(), 1, 100000, 100));
        mQuantityCtrl = new JSpinner(new SpinnerNumberModel(mTube.getQuantity(), 1, 100000, 1));
    }

    private void initLink()
    {
        ListenerUtils.listen(mOK, (e) -> doOK());
        ListenerUtils.listen(mCancel, (e) -> doCancel());
        BeanMapUtils.map(mCapacityCtrl, mTube, "capacity");
        BeanMapUtils.map(mQuantityCtrl, mTube, "quantity");
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(mOK);
        buttonBar.add(mCancel);

        JPanel client = new JPanel();
        client.setLayout(new GridLayout(2, 2));
        client.add(new JLabel("Capacity:"));
        client.add(mCapacityCtrl);
        client.add(new JLabel("Quantity:"));
        client.add(mQuantityCtrl);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("North",
                new JLabel("Select parameters for your launch tube"));
        getContentPane().add("South", buttonBar);
        getContentPane().add("Center", client);
        setSize(640, 480);
    }

    protected void doOK()
    {
        mShip.getLaunchTubes().add(mTube);
        mShip.fireMonotonicPropertyChange("launchTubes", mShip.getLaunchTubes());
        BeanMapUtils.unmap(mCapacityCtrl, mTube, "capacity");
        BeanMapUtils.unmap(mQuantityCtrl, mTube, "quantity");
        dispose();
    }

    protected void doCancel()
    {
        BeanMapUtils.unmap(mCapacityCtrl, mTube, "capacity");
        BeanMapUtils.unmap(mQuantityCtrl, mTube, "quantity");
        dispose();
    }

}
