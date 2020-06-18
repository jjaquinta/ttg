package ttg.view.war.act;

import java.awt.Frame;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.IconLogic;
import ttg.logic.war.ShipLogic;
import ttg.logic.war.SideLogic;
import ttg.logic.war.WorldLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.ShipTree;
import ttg.view.war.WarButton;
import ttg.view.war.WarPanel;
import ttg.view.war.WarTTGActionEvent;
import ttg.view.war.edit.DlgShipDesigner;

public class SetupShipsPanel extends HelpPanel implements TTGActionListener
{
    private ShipTree mShips;
    private JButton  mScrap;
    private JButton  mBuild;
    private JButton  mSet;
    private JButton  mReset;
    private JButton  mDone;

    /**
     *
     */

    public SetupShipsPanel(WarPanel panel)
    {
        mPanel = panel;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mShips = new ShipTree(mPanel);
        mShips.setName("SetupShips");
        mShips.setRootLabel("Ships:");
        mScrap = new WarButton("Scrap", IconLogic.mButtonScrap);
        mBuild = new WarButton("Build", IconLogic.mButtonBuild);
        mSet = new WarButton("Set", IconLogic.mButtonSet);
        mReset = new WarButton("Reset", IconLogic.mButtonReset);
        mDone = new WarButton("Done", IconLogic.mButtonDone);
    }

    private void initLink()
    {
        ListenerUtils.listen(mScrap, (ev) -> doScrap());
        ListenerUtils.listen(mBuild, (ev) -> doBuild());
        ListenerUtils.listen(mSet, (ev) -> doSet());
        ListenerUtils.listen(mReset, (ev) -> doReset());
        ListenerUtils.listen(mDone, (ev) -> doDone());
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Setup Ships:", "ActionSetup.htm"));
        add("1,+ 2x1 fill=hv weighty=30", mShips);
        add("2,+ fill=h", mScrap);
        add("2,+ fill=h", mBuild);
        add("2,+ fill=h", mSet);
        add("2,+ fill=h", mReset);
        add("2,+ fill=h", mDone);
    }

    public void init()
    {
        List<ShipInst> ships = mPanel.getSide().getShips();
        for (ShipInst ship : ships)
            ship.setToDo(true);
        mShips.init(ships);
        boolean build = mPanel.getGame().getGame()
                .isAllowFleetReconfiguration();
        mScrap.setVisible(build);
        mBuild.setVisible(build);
        mDone.setEnabled(false);
        mPanel.getStarPanel().addTTGActionListener(this);
    }

    protected void doSet()
    {
        OrdBean ord = mPanel.getStarPanel().getFocus();
        if (ord == null)
        {
            mPanel.getGame().setStatus("No world selected");
            return;
        }
        ShipInst[] ships = mShips.getSelectedShips();
        WorldInst world = WorldLogic.getWorld(mPanel.getGame(), ord);
        setUpon(world, ships);
    }

    private void setUpon(WorldInst world, ShipInst[] ships)
    {
        if (world == null)
        {
            mPanel.getGame().setStatus("No world selected");
            return;
        }
        if (world.getSide() != mPanel.getSide())
        {
            mPanel.getGame().setStatus("Can't setup on this world");
            return;
        }
        if (ships.length == 0)
        {
            mPanel.getGame().setStatus("Select some ships first");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ships.length; i++)
        {
            if (ships[i] == null)
                continue;
            ShipLogic.setDestination(mPanel.getGame(), ships[i], world);
            ShipLogic.setToDoAll(ships[i], false);
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(ships[i].getShip().getName());
        }
        sb.append(" placed at " + world.getWorld().getName());
        mPanel.getGame().setStatus(sb.toString());
        mShips.repaint();
        mPanel.getStarPanel().setFocus(world.getOrds());
        // mPanel.getStarPanel().repaint();
        mPanel.getInfoPanel().setObject(world);
        boolean done = true;
        for (ShipInst ship : mPanel.getSide().getShips())
            if (ship.isToDo())
            {
                done = false;
                break;
            }
        mDone.setEnabled(done);
    }

    protected void doReset()
    {
        List<ShipInst> ships = mPanel.getSide().getShips();
        for (ShipInst ship : ships)
        {
            ShipLogic.setDestination(mPanel.getGame(), ship, null);
            ship.setToDo(true);
        }
        mDone.setEnabled(false);
        mShips.repaint();
        mPanel.getStarPanel().repaint();
    }

    protected void doDone()
    {
        mPanel.getStarPanel().removeTTGActionListener(this);
        mShips.done();
        mPanel.setMode(WarPanel.DONE);
    }

    protected void doScrap()
    {
        ShipInst[] ships = mShips.getSelectedShips();
        if (ships.length == 0)
        {
            mPanel.getGame().setStatus("Select some ships first");
            return;
        }
        for (int i = 0; i < ships.length; i++)
            ShipLogic.scrap(mPanel.getGame(), ships[i]);
        mShips.init(mPanel.getSide().getShips());
    }

    protected void doBuild()
    {
        Ship design = new Ship();
        ShipInst[] ships = mShips.getSelectedShips();
        if (ships.length > 0)
            ShipLogic.copy(ships[0].getShip(), design);
        int dosh = mPanel.getSide().getResources();
        DlgShipDesigner dlg = new DlgShipDesigner(
                (Frame)SwingUtilities.getRoot(this));
        dlg.setShip(design);
        dlg.setMaxCost(dosh);
        dlg.setGame(mPanel.getGame().getGame());
        dlg.setVisible(true);
        if (!dlg.isAccepted())
            return;
        design = dlg.getShip();
        int cost = ShipLogic.cost(design);
        if (cost > dosh)
        {
            mPanel.getGame().setStatus("Too expensive! Costs " + cost
                    + " and you only have " + dosh + ".");
            return;
        }
        ShipLogic.buy(mPanel.getGame(), mPanel.getSide(), design);
        SideLogic.addResources(mPanel.getSide(), -cost);
        mShips.init(mPanel.getSide().getShips());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ttg.view.ctrl.TTGActionListener#actionPerformed(ttg.view.ctrl.
     * TTGActionEvent)
     */
    public void actionPerformed(TTGActionEvent e)
    {
        if (e.getID() == WarTTGActionEvent.DROPPED)
        {
            Object obj = ((WarTTGActionEvent)e).getActor();
            // DebugLogic.debug("obj="+obj.getClass());
            String str;
            if (obj instanceof InputStream)
            {
                StringBuffer sb = new StringBuffer();
                try
                {
                    InputStream is = (InputStream)obj;
                    for (;;)
                    {
                        int ch = is.read();
                        if (ch == -1)
                            break;
                        sb.append((char)ch);
                    }
                    is.close();
                }
                catch (IOException ex)
                {
                }
                str = sb.toString();
            }
            else
                str = obj.toString();
            StringTokenizer st = new StringTokenizer(str, "\r\n");
            ShipInst[] ships = new ShipInst[st.countTokens()];
            for (int i = 0; i < ships.length; i++)
                ships[i] = ShipLogic.getShip(mPanel.getGame(), st.nextToken());
            obj = e.getObject();
            WorldInst world = null;
            if (obj instanceof MainWorldBean)
                world = WorldLogic.getWorld(mPanel.getGame(),
                        (MainWorldBean)obj);
            else if (obj instanceof OrdBean)
                world = WorldLogic.getWorld(mPanel.getGame(), (OrdBean)obj);
            setUpon(world, ships);
        }
    }
}
