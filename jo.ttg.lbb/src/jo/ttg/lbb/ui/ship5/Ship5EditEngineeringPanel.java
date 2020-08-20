package jo.ttg.lbb.ui.ship5;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class Ship5EditEngineeringPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JSpinner    mDriveManeuver;
    private JSpinner    mDriveJump;
    private JSpinner    mDrivePower;
    private JSpinner    mFuelTankage;
    private JCheckBox   mFuelScoops;
    private JCheckBox   mFuelPurification;

    public Ship5EditEngineeringPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mDriveManeuver = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
        mDriveJump = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
        mDrivePower = new JSpinner(new SpinnerNumberModel(0, 0, 6, 1));
        mFuelTankage = new JSpinner(new SpinnerNumberModel(100, 0, 100000, 10));
        mFuelScoops = new JCheckBox("Fuel Scoops");
        mFuelPurification = new JCheckBox("Fuel Purification");
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w,ipadw=4,ipadh=2"));
        add("1,+ anchor=e", new JLabel("Maneuver Drive:"));
        add("+,.", mDriveManeuver);
        add("+,. anchor=e", new JLabel("Jump Drive:"));
        add("+,.", mDriveJump);
        add("+,. anchor=e", new JLabel("Power Plant:"));
        add("+,.", mDrivePower);
        add("1,+ anchor=e", new JLabel("Fuel Tankage:"));
        add("+,.", mFuelTankage);
        add("+,.,2,1", mFuelScoops);
        add("+,.,2,1", mFuelPurification);
    }

    private void initLink()
    {
        BeanMapUtils.map(mDriveManeuver, mShip, "maneuverDriveNumber");
        BeanMapUtils.map(mDriveJump, mShip, "jumpDriveNumber");
        BeanMapUtils.map(mDrivePower, mShip, "powerPlantNumber");
        BeanMapUtils.map(mFuelTankage, mShip, "fuelTankage");
        BeanMapUtils.map(mFuelScoops, mShip, "fuelScoops");
        BeanMapUtils.map(mFuelPurification, mShip, "fuelPurification");
    }
}
