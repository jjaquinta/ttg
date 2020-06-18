package ttg.view.war.info;

import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import jo.util.ui.swing.TableLayout;
import ttg.beans.war.ShipInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.ShipLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.ObjectButton;
import ttg.view.war.ShipTree;
import ttg.view.war.WarPanel;

public class ShipInstInfoPanel extends HelpPanel
{
    @SuppressWarnings("unused")
    private InfoPanel    mInfo;
    private ShipInst     mShip;

    private ObjectButton mName;
    private ObjectButton mSide;
    private JLabel       mAttack;
    private JLabel       mDefense;
    private JLabel       mCapacity;
    private JLabel       mSize;
    private JLabel       mJump;
    private JProgressBar mFuel;
    private JLabel       mFuelRequired;
    private JLabel       mDamaged;
    private ObjectButton mLocation;
    private ObjectButton mDestination;
    private JLabel       mContainedByLabel;
    private ObjectButton mContainedBy;
    private JLabel       mContainsLabel;
    private ShipTree     mContains;

    /**
     *
     */

    public ShipInstInfoPanel(WarPanel panel, InfoPanel info)
    {
        mPanel = panel;
        mInfo = info;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mName = new ObjectButton(mPanel);
        mSide = new ObjectButton(mPanel);
        mAttack = new JLabel();
        mDefense = new JLabel();
        mCapacity = new JLabel();
        mSize = new JLabel();
        mJump = new JLabel();
        mFuel = new JProgressBar(0, 1000);
        mFuel.setStringPainted(true);
        mFuelRequired = new JLabel();
        mDamaged = new JLabel();
        mLocation = new ObjectButton(mPanel);
        mDestination = new ObjectButton(mPanel);
        mContainedByLabel = new JLabel("Contained By:");
        mContainedBy = new ObjectButton(mPanel);
        mContains = new ShipTree(mPanel);
        mContains.setInfoOnClick(true);
        mContains.setInfoOnSelect(false);
        mContains.setShipsOnSelect(true);
        mContainsLabel = new JLabel("Contains:");
        mContains.setRootLabel("Contains:");
        mContains.setName("ShipInfo");
    }

    private void initLink()
    {
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Ship Info", "InfoShip.htm"));
        add("1,+", new JLabel("Ship:"));
        add("+,. fill=h", mName);
        add("1,+", new JLabel("Damage:"));
        add("+,. fill=h", mDamaged);
        add("1,+", new JLabel("Side:"));
        add("+,. fill=h", mSide);
        add("1,+", new JLabel("Attack:"));
        add("+,. fill=h", mAttack);
        add("1,+", new JLabel("Defense:"));
        add("+,. fill=h", mDefense);
        add("1,+", new JLabel("Jump:"));
        add("+,. fill=h", mJump);
        add("1,+", new JLabel("Fuel:"));
        add("+,. fill=h", mFuel);
        add("1,+", new JLabel("Jump-1:"));
        add("+,. fill=h", mFuelRequired);
        add("1,+", new JLabel("Location:"));
        add("+,. fill=h", mLocation);
        add("1,+", new JLabel("Destination:"));
        add("+,. fill=h", mDestination);
        add("1,+", new JLabel("Capacity:"));
        add("+,. fill=h", mCapacity);
        add("1,+", new JLabel("Size:"));
        add("+,. fill=h", mSize);
        add("1,+", mContainedByLabel);
        add("+,. fill=h", mContainedBy);
        add("1,+ 2x1 fill=h", mContainsLabel);
        add("1,+ 2x1 fill=hv weighty=20", mContains);
    }

    public void setObject(ShipInst obj)
    {
        mShip = obj;
        if (mShip == null)
        {
            mName.setObject(null);
            mSide.setObject(null);
            mAttack.setText(null);
            mDefense.setText(null);
            mCapacity.setText(null);
            mSize.setText(null);
            mJump.setText(null);
            mFuel.setString("");
            mFuel.setValue(0);
            mFuel.setMaximum(1);
            mFuelRequired.setText(null);
            mDamaged.setText(null);
            mLocation.setObject(null);
            mDestination.setObject(null);
            mContainedBy.setObject(null);
            mContains.done();
            mContains.init(new ArrayList<>());
        }
        else
        {
            mName.setObject(mShip);
            mSide.setObject(mShip.getSideInst());
            mAttack.setText(mShip.getShip().getAttack() + " ("
                    + (mShip.getShip().getAttack() / 2) + ")");
            mDefense.setText(mShip.getShip().getDefense() + " ("
                    + (mShip.getShip().getDefense() / 2) + ")");
            int cap = ShipLogic.capacity(mShip.getShip());
            if (cap > 0)
                mCapacity.setText(String.valueOf(
                        ShipLogic.additionalCapacity(mShip)) + " remaining of "
                        + String.valueOf(ShipLogic.capacity(mShip.getShip())));
            else
                mCapacity.setText("-");
            mSize.setText(String.valueOf(ShipLogic.size(mShip)));
            mJump.setText(ShipLogic.getJumpDescription(mShip));
            mFuel.setString(String.valueOf(
                    mShip.getFuel() + " of " + ShipLogic.fuelTankage(mShip)));
            mFuel.setMaximum(ShipLogic.fuelTankage(mShip));
            mFuel.setValue(mShip.getFuel());
            mFuelRequired
                    .setText(String.valueOf(ShipLogic.fuelForJump1(mShip)));
            mDamaged.setText(mShip.isDamaged() ? "Damaged!" : "");
            mLocation.setObject(mShip.getLocation());
            if (mShip.getSideInst() != mPanel.getSide())
                mDestination.setObject(null);
            else
                mDestination.setObject(mShip.getDestination());
            mContainedBy.setObject(mShip.getContainedBy());
            mContains.done();
            mContains.init(mShip);
        }
    }

    /**
     * 
     */
    protected void doSide()
    {
        if (mShip != null)
            mPanel.getInfoPanel().setObject(mShip.getSideInst());
    }

    /**
     * 
     */
    protected void doLocation()
    {
        WorldInst world = mShip.getLocation();
        if (world != null)
            mPanel.getInfoPanel().setObject(world);
    }

    /**
     * 
     */
    protected void doDestination()
    {
        WorldInst world = mShip.getDestination();
        if (mShip.getSideInst() != mPanel.getSide())
            world = null;
        if (world != null)
            mPanel.getInfoPanel().setObject(world);
    }

    /**
     * @param b
     */
    public void setShortForm(boolean b)
    {
        mContainedByLabel.setVisible(!b);
        mContainedBy.setVisible(!b);
        mContainsLabel.setVisible(!b);
        mContains.setVisible(!b);
    }
}
