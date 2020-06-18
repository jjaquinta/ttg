package ttg.view.war.info;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.util.ui.swing.TableLayout;
import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.ShipLogic;
import ttg.logic.war.WorldLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.ObjectButton;
import ttg.view.war.ShipTree;
import ttg.view.war.WarPanel;
import ttg.view.war.WorldRenderer;

public class SideInfoPanel extends HelpPanel
{
    @SuppressWarnings("unused")
    private InfoPanel        mInfo;
    private SideInst         mSide;

    private ObjectButton     mName;
    private JLabel           mResources;
    private JLabel           mResGen;
    private JLabel           mPopulation;
    private JLabel           mMaxTech;
    private JLabel           mFleet;
    private JLabel           mVictoryPoints;
    private JList<WorldInst> mWorlds;
    private ShipTree         mShips;

    /**
     *
     */

    public SideInfoPanel(WarPanel panel, InfoPanel info)
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
        mResources = new JLabel();
        mResGen = new JLabel();
        mVictoryPoints = new JLabel();
        mPopulation = new JLabel();
        mMaxTech = new JLabel();
        mFleet = new JLabel();
        mWorlds = new JList<>();
        mWorlds.setCellRenderer(new WorldRenderer(mPanel));
        mShips = new ShipTree(mPanel);
        mShips.setInfoOnClick(true);
        mShips.setInfoOnSelect(false);
        mShips.setShipsOnSelect(true);
        mShips.setRootLabel("Ships:");
        mShips.setName("SideInfo");
    }

    private void initLink()
    {
        mWorlds.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ev)
            {
                if (ev.getClickCount() == 2)
                    doWorldAction();
            }
        });
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Empire Info", "InfoSide.htm"));
        add("1,+", new JLabel("Side:"));
        add("+,. fill=h", mName);
        add("1,+", new JLabel("Resources:"));
        add("+,. fill=h", mResources);
        add("1,+", new JLabel("Production:"));
        add("+,. fill=h", mResGen);
        add("1,+", new JLabel("Population:"));
        add("+,. fill=h", mPopulation);
        add("1,+", new JLabel("Max Tech:"));
        add("+,. fill=h", mMaxTech);
        add("1,+", new JLabel("Fleet:"));
        add("+,. fill=h", mFleet);
        add("1,+", new JLabel("Points:"));
        add("+,. fill=h", mVictoryPoints);
        add("1,+ 2x1 weighty=20 fill=hv", mShips);
        add("1,+", new JLabel("Worlds:"));
        add("1,+ 2x1 weighty=20 fill=hv", new JScrollPane(mWorlds));
    }

    public void setObject(SideInst obj)
    {
        mSide = obj;
        if (mSide == null)
        {
            mName.setObject(null);
            mResources.setText(null);
            mResGen.setText(null);
            mPopulation.setText(null);
            mMaxTech.setText(null);
            mFleet.setText(null);
            mVictoryPoints.setText(null);
            mShips.done();
            mShips.init(new ArrayList<>());
            mWorlds.setListData(new WorldInst[0]);
        }
        else
        {
            int resGen = 0;
            double pop = 0;
            int maxTech = 0;
            for (WorldInst world : mSide.getWorlds())
            {
                MainWorldBean mw = world.getWorld();
                if (mw == null)
                    continue;
                resGen += WorldLogic.getResourceGeneration(mPanel.getGame(),
                        world);
                pop += mw.getPopulatedStats().getUPP().getPop().getPopulation();
                maxTech = Math.max(maxTech,
                        mw.getPopulatedStats().getUPP().getTech().getValue());
            }
            int att = 0;
            int def = 0;
            int tons = 0;
            for (ShipInst ship : mSide.getShips())
            {
                att += ShipLogic.getAttack(ship);
                def += ShipLogic.getDefense(ship);
                tons += ShipLogic.cost(ship.getShip());
            }

            mName.setObject(mSide);
            mResources.setText(String.valueOf(mSide.getResources()));
            mResGen.setText(String.valueOf(resGen));
            mPopulation.setText(FormatUtils.sPopulation(pop));
            mMaxTech.setText(String.valueOf(maxTech));
            mFleet.setText("A" + att + " D" + def + " $" + tons);
            mVictoryPoints.setText(String.valueOf(mSide.getVictoryPoints()));
            mShips.done();
            if (mPanel.getGame().getGame().isAllowOmniscentSensors()
                    || (mPanel.getGame().getTurn() < 1)
                    || (mPanel.getSide() == mSide))
                mShips.init(mSide.getShips());
            else
                mShips.init(new ArrayList<>());
            mWorlds.setListData(mSide.getWorlds().toArray(new WorldInst[0]));
        }
    }

    /**
     * 
     */
    protected void doWorldAction()
    {
        WorldInst world = (WorldInst)mWorlds.getSelectedValue();
        if (world != null)
            mPanel.getInfoPanel().setObject(world);
    }
}
