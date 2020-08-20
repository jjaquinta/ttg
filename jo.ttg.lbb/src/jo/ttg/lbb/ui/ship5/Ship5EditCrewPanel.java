package jo.ttg.lbb.ui.ship5;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.map.BeanMapUtils;

public class Ship5EditCrewPanel extends JPanel
{
    private Ship5Design mShip;
    
    private JSpinner mCrewTroops;
    private JSpinner mStaterooms;
    private JSpinner mLowBerths;
    private JSpinner mEmergencyLowBerths;

    public Ship5EditCrewPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mCrewTroops = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        mStaterooms = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        mLowBerths = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
        mEmergencyLowBerths = new JSpinner(new SpinnerNumberModel(0, 0, 10000, 1));
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w,ipadw=4,ipadh=2"));
        add("1,+ anchor=e", new JLabel("Ship Troops:"));
        add("+,.", mCrewTroops);
        add("1,+ anchor=e", new JLabel("Staterooms:"));
        add("+,.", mStaterooms);
        add("+,. anchor=e", new JLabel("Low Berths:"));
        add("+,.", mLowBerths);
        add("+,. anchor=e", new JLabel("Emergency Low Berths:"));
        add("+,.", mEmergencyLowBerths);
    }

    private void initLink()
    {
        BeanMapUtils.map(mCrewTroops, mShip, "crewTroops");
        BeanMapUtils.map(mStaterooms, mShip, "staterooms");
        BeanMapUtils.map(mLowBerths, mShip, "lowBerths");
        BeanMapUtils.map(mEmergencyLowBerths, mShip, "emergencyLowBerths");
    }
}
