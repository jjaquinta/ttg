package ttg.war.view.act;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.ttg.logic.OrdLogic;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IconLogic;
import ttg.war.logic.ShipLogic;
import ttg.war.logic.WorldLogic;
import ttg.war.view.HelpPanel;
import ttg.war.view.ShipNode;
import ttg.war.view.ShipTree;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;
import ttg.war.view.WarTTGActionEvent;
import ttg.war.view.WorldRenderer;

public class MoveShipsPanel extends HelpPanel implements ListSelectionListener,
        TTGActionListener, TreeSelectionListener
{
    private ShipTree         mShipTree;
    private JList<WorldInst> mWorlds;
    private JButton          mSet;
    private JButton          mReset;
    private JButton          mDone;

    private boolean          mEclipse;

    /**
     *
     */

    public MoveShipsPanel(WarPanel panel)
    {
        mPanel = panel;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mShipTree = new ShipTree(mPanel);
        mShipTree.setRootLabel("Move Ships:");
        mShipTree.setName("MoveShips");
        mWorlds = new JList<WorldInst>();
        mWorlds.setCellRenderer(new WorldRenderer(mPanel));
        mWorlds.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mSet = new WarButton("Set", IconLogic.mButtonSet);
        mReset = new WarButton("Reset", IconLogic.mButtonReset);
        mDone = new WarButton("Done", IconLogic.mButtonDone);
    }

    private void initLink()
    {
        ListenerUtils.listen(mSet, (ev) -> doSet());
        ListenerUtils.listen(mReset, (ev) -> doReset());
        ListenerUtils.listen(mDone, (ev) -> doDone());
        mShipTree.addTreeSelectionListener(this);
        mWorlds.addListSelectionListener(this);
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Move Ships:", "ActionMove.htm"));
        add("1,+ 2x1 fill=hv weighty=30", mShipTree);
        add("1,+ 2x1 fill=h", new JLabel("Destinations:"));
        add("1,+ 2x1 fill=hv weighty=30", new JScrollPane(mWorlds));
        add("2,+ fill=h", mSet);
        add("2,+ fill=h", mReset);
        add("2,+ fill=h", mDone);
    }

    public void init()
    {
        List<ShipInst> ships = mPanel.getSide().getShips();
        for (ShipInst ship : ships)
            ship.setToDo(true);
        mPanel.getStarPanel().addTTGActionListener(this);
        mShipTree.init(ships);
    }

    protected void doSet()
    {
        WorldInst world = (WorldInst)mWorlds.getSelectedValue();
        ShipInst[] ships = mShipTree.getSelectedShips();
        setUpon(world, ships);
    }

    private void setUpon(WorldInst world, ShipInst[] ships)
    {
        if (world == null)
        {
            mPanel.getGame().setStatus("No world selected");
            return;
        }
        if (ships.length == 0)
        {
            mPanel.getGame().setStatus("No ship selected");
            return;
        }
        StringBuffer msg = new StringBuffer();
        StringBuffer err = new StringBuffer();
        for (int i = 0; i < ships.length; i++)
        {
            if (ships[i] == null)
                System.out.println("QUACK: ship["+i+"] is null");
            if (ships[i].getSideInst() == mPanel.getSide())
            {
                String errMsg = ShipLogic.validateMove(mPanel.getGame(),
                        ships[i], world);
                if (errMsg == null)
                {
                    ShipLogic.setDestination(mPanel.getGame(), ships[i], world);
                    ships[i].setToDo(false);
                    if (msg.length() > 0)
                        msg.append(", ");
                    msg.append(ships[i].getShip().getName());
                }
                else
                {
                    if (err.length() > 0)
                        err.append(", ");
                    err.append(ships[i].getShip().getName());
                    err.append(" ");
                    err.append(errMsg);
                }
            }
        }
        if ((msg.length() == 0) && (err.length() == 0))
            return; // nothing to move
        if (msg.length() > 0)
        {
            if (world.getWorld() != null)
                msg.append(" moved to " + world.getWorld().getName());
            else
                msg.append(
                        " moved to " + OrdLogic.getShortNum(world.getOrds()));
        }
        if (err.length() > 0)
        {
            if (msg.length() > 0)
                msg.append(". ");
            msg.append(err.toString());
        }
        mPanel.getGame().setStatus(msg.toString());
        mShipTree.repaint();
        mPanel.getStarPanel().setFocus(world.getOrds());
        // mPanel.getStarPanel().repaint();
        mPanel.getInfoPanel().setObject(world);
    }

    protected void doReset()
    {
        List<ShipInst> ships = mPanel.getSide().getShips();
        for (ShipInst ship : ships)
        {
            ShipLogic.setDestination(mPanel.getGame(), ship, null);
            ship.setToDo(true);
        }
        mShipTree.repaint();
    }

    protected void doDone()
    {
        mPanel.getStarPanel().removeTTGActionListener(this);
        mPanel.setMode(WarPanel.DONE);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        if (!mEclipse && (mWorlds.getSelectedValue() != null))
            mPanel.getInfoPanel().setObject(mWorlds.getSelectedValue());
    }

    public void valueChanged(TreeSelectionEvent ev)
    {
        TreePath path = ev.getNewLeadSelectionPath();
        if (path == null)
            mWorlds.setListData(new WorldInst[0]);
        else
        {
            ShipNode node = (ShipNode)path.getLastPathComponent();
            ShipInst primaryShip = node.getShip();
            ShipInst[] ships = mShipTree.getSelectedShips();
            Set<WorldInst> worlds = new HashSet<>();
            for (int i = 0; i < ships.length; i++)
            {
                List<WorldInst> validWorlds = ShipLogic
                        .validDestinations(mPanel.getGame(), ships[i]);
                if (i == 0)
                    worlds.addAll(validWorlds);
                else
                    worlds.retainAll(validWorlds);
            }
            mWorlds.setListData(worlds.toArray(new WorldInst[0]));
            mWorlds.setSelectedValue(primaryShip.getDestination(), true);
        }
    }

    public void actionPerformed(TTGActionEvent e)
    {
        switch (e.getID())
        {
            case TTGActionEvent.SELECTED:
                doHexSelected(e.getObject());
                break;
            case WarTTGActionEvent.DROPPED:
                doDrop((WarTTGActionEvent)e);
                break;
        }
    }

    /**
     * @param bean
     */
    private void doHexSelected(Object obj)
    {
        WorldInst world = WorldLogic.getWorld(mPanel.getGame(), obj);
        if (world == null)
        {
            mWorlds.setSelectedValue(null, false);
            return;
        }
        StringBuffer errors = new StringBuffer();
        ShipInst[] ships = mShipTree.getSelectedShips();
        for (int i = 0; i < ships.length; i++)
        {
            String error = ShipLogic.validateMove(mPanel.getGame(), ships[i],
                    world);
            if (error != null)
            {
                if (errors.length() > 0)
                    errors.append(", ");
                errors.append(ships[i].getShip().getName());
                errors.append(": ");
                errors.append(error);
            }
        }
        if (errors.length() == 0)
        {
            mWorlds.setSelectedValue(world, true);
        }
        else
        {
            mWorlds.setSelectedValue(null, false);
            mPanel.getGame().setStatus(errors.toString());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see ttg.view.ctrl.TTGActionListener#actionPerformed(ttg.view.ctrl.
     * TTGActionEvent)
     */
    private void doDrop(WarTTGActionEvent e)
    {
        Object obj = e.getActor();
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
            catch (IOException e1)
            {
            }
            str = sb.toString();
        }
        else
            str = obj.toString();
        StringTokenizer st = new StringTokenizer(str, "\r\n");
        List<ShipInst> ships = new ArrayList<>(st.countTokens());
        while (st.hasMoreTokens())
        {
            ShipInst ship = ShipLogic.getShip(mPanel.getGame(), st.nextToken());
            if (ship != null)
                ships.add(ship);
        }
        WorldInst world = WorldLogic.getWorld(mPanel.getGame(), e.getObject());
        setUpon(world, ships.toArray(new ShipInst[0]));
    }
}
