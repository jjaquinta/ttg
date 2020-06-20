package ttg.war.view.info;

import javax.swing.JLabel;

import jo.util.ui.swing.TableLayout;
import ttg.war.beans.Ship;
import ttg.war.logic.ShipLogic;
import ttg.war.view.HelpPanel;
import ttg.war.view.ObjectButton;
import ttg.war.view.WarPanel;

public class ShipInfoPanel extends HelpPanel
{
    @SuppressWarnings("unused")
    private InfoPanel    mInfo;
    private Ship         mShip;

    private ObjectButton mName;
    private JLabel       mAttack;
    private JLabel       mDefense;
    private JLabel       mCapacity;
    private JLabel       mSize;
    private JLabel       mJump;
    private JLabel       mFuelRequired;
    private JLabel       mCost;

    /**
     *
     */

    public ShipInfoPanel(WarPanel panel, InfoPanel info)
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
        mAttack = new JLabel();
        mDefense = new JLabel();
        mCapacity = new JLabel();
        mSize = new JLabel();
        mJump = new JLabel();
        mFuelRequired = new JLabel();
        mCost = new JLabel();
    }

    private void initLink()
    {
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Design Info", "InfoShip.htm"));
        add("1,+", new JLabel("Ship:"));
        add("+,. fill=h", mName);
        add("1,+", new JLabel("Attack:"));
        add("+,. fill=h", mAttack);
        add("1,+", new JLabel("Defense:"));
        add("+,. fill=h", mDefense);
        add("1,+", new JLabel("Jump:"));
        add("+,. fill=h", mJump);
        add("1,+", new JLabel("Jump-1:"));
        add("+,. fill=h", mFuelRequired);
        add("1,+", new JLabel("Capacity:"));
        add("+,. fill=h", mCapacity);
        add("1,+", new JLabel("Size:"));
        add("+,. fill=h", mSize);
        add("1,+", new JLabel("Cost:"));
        add("+,. fill=h", mCost);
    }

    public void setObject(Ship obj)
    {
        mShip = obj;
        if (mShip == null)
        {
            mName.setObject(null);
            mAttack.setText(null);
            mDefense.setText(null);
            mCapacity.setText(null);
            mSize.setText(null);
            mJump.setText(null);
            mFuelRequired.setText(null);
            mCost.setText(null);
        }
        else
        {
            mName.setObject(mShip);
            mAttack.setText(
                    mShip.getAttack() + " (" + (mShip.getAttack() / 2) + ")");
            mDefense.setText(
                    mShip.getDefense() + " (" + (mShip.getDefense() / 2) + ")");
            int cap = ShipLogic.capacity(mShip);
            if (cap > 0)
                mCapacity.setText(String.valueOf(mShip));
            else
                mCapacity.setText("-");
            mSize.setText(String.valueOf(ShipLogic.size(mShip)));
            mJump.setText(ShipLogic.getJumpDescription(mShip,
                    ShipLogic.capacity(mShip)));
            mFuelRequired
                    .setText(String.valueOf(ShipLogic.fuelForJump1(mShip)));
            mSize.setText(String.valueOf(ShipLogic.cost(mShip)));
        }
    }
}
