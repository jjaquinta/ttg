package jo.ttg.lbb.ui.ship5;

import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.util.ui.swing.TableLayout;

public class Ship5EditPanel extends JPanel
{
    private Ship5Design               mShip;

    private Ship5EditBasicsPanel      mBasics;
    private Ship5EditEngineeringPanel mEnginering;
    private Ship5EditSpinalPanel      mSpinal;
    private Ship5EditBaysPanel        mBays;
    private Ship5EditTurretsPanel     mTurrets;
    private Ship5EditScreensPanel     mScreens;
    private Ship5EditSubCraftPanel    mSubCraft;
    private Ship5EditCrewPanel        mCrew;

    public Ship5EditPanel(Ship5Design ship)
    {
        mShip = ship;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mBasics = new Ship5EditBasicsPanel(mShip);
        mBasics.setBorder(new TitledBorder("Basics"));
        mEnginering = new Ship5EditEngineeringPanel(mShip);
        mEnginering.setBorder(new TitledBorder("Engineering"));
        mSpinal = new Ship5EditSpinalPanel(mShip);
        mSpinal.setBorder(new TitledBorder("Spinal Mount"));
        mBays = new Ship5EditBaysPanel(mShip);
        mBays.setBorder(new TitledBorder("Bays"));
        mTurrets = new Ship5EditTurretsPanel(mShip);
        mTurrets.setBorder(new TitledBorder("Turrets"));
        mScreens = new Ship5EditScreensPanel(mShip);
        mScreens.setBorder(new TitledBorder("Screens"));
        mSubCraft = new Ship5EditSubCraftPanel(mShip);
        mSubCraft.setBorder(new TitledBorder("Sub Craft"));
        mCrew = new Ship5EditCrewPanel(mShip);
        mCrew.setBorder(new TitledBorder("Crew"));
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        add("1,+ fill=h", mBasics);
        add("1,+ fill=h", mEnginering);
        add("1,+ fill=h", mSpinal);
        add("1,+ fill=h", mBays);
        add("1,+ fill=h", mTurrets);
        add("1,+ fill=h", mScreens);
        add("1,+ fill=hv", mSubCraft);
        add("1,+ fill=h", mCrew);
    }

    private void initLink()
    {
    }

    public Ship5Design getShip()
    {
        return mShip;
    }

    public void setShip(Ship5Design ship)
    {
        mShip.fromJSON(ship.toJSON());
    }
}
