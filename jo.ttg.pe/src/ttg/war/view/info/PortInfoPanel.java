package ttg.war.view.info;

import java.awt.Frame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import jo.ttg.logic.mw.UPPLogic;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.Ship;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IconLogic;
import ttg.war.logic.ShipLogic;
import ttg.war.logic.WorldLogic;
import ttg.war.view.HelpPanel;
import ttg.war.view.ObjectButton;
import ttg.war.view.ShipRenderer;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;
import ttg.war.view.edit.DlgShipDesigner;

public class PortInfoPanel extends HelpPanel
{
    private InfoPanel    mInfo;
    private WorldInst    mWorld;

    private ObjectButton mName;
    private JLabel       mUPP;
    private ObjectButton mSide;
    private JList<Ship>  mBuilding;
    private WarButton    mMoveUp;
    private WarButton    mMoveDown;
    private WarButton    mEdit;
    private WarButton    mAdd;
    private WarButton    mRemove;
    private JLabel       mConstRate;
    private JLabel       mTurnsLeft;
    private JProgressBar mCompleted;

    /**
     *
     */

    public PortInfoPanel(WarPanel panel, InfoPanel info)
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
        mUPP = new JLabel();
        mSide = new ObjectButton(mPanel);
        mBuilding = new JList<>();
        mBuilding.setCellRenderer(new ShipRenderer(IconLogic.SEL_DONT));
        mMoveUp = new WarButton(IconLogic.mButtonUp);
        mMoveUp.setToolTipText("Move selected ship up production list");
        mMoveDown = new WarButton(IconLogic.mButtonDown);
        mMoveDown.setToolTipText("Move selected ship down production list");
        mEdit = new WarButton(IconLogic.mButtonEdit);
        mEdit.setToolTipText("Edit ship design");
        mAdd = new WarButton(IconLogic.mButtonAdd);
        mAdd.setToolTipText("Add a new ships design to the production list");
        mRemove = new WarButton(IconLogic.mButtonSubtract);
        mRemove.setToolTipText("Remove a ships from the production list");
        mConstRate = new JLabel();
        mCompleted = new JProgressBar(0, 10000);
        mCompleted.setStringPainted(true);
        mTurnsLeft = new JLabel();
    }

    private void initLink()
    {
        ListenerUtils.listen(mAdd, (e) -> doBuild());
        ListenerUtils.listen(mEdit, (e) -> doEdit());
        ListenerUtils.listen(mRemove, (e) -> doScrap());
        ListenerUtils.listen(mMoveUp, (e) -> doMoveUp());
        ListenerUtils.listen(mMoveDown, (e) -> doMoveDown());
        mBuilding.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent ev)
            {
                if (ev.getClickCount() == 2)
                    doActivateShip();
                else if (ev.getClickCount() == 1)
                    doSelectShip();
            }
        });
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(mMoveUp);
        buttonBar.add(mMoveDown);
        buttonBar.add(mEdit);
        buttonBar.add(mAdd);
        buttonBar.add(mRemove);

        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Starport Info", "InfoPort.htm"));
        add("1,+", new JLabel("World:"));
        add("+,. fill=h", mName);
        add("1,+", new JLabel("UPP:"));
        add("+,. fill=h", mUPP);
        add("1,+", new JLabel("Side:"));
        add("+,. fill=h", mSide);
        add("1,+", new JLabel("Construction:"));
        add("+,. fill=h", mConstRate);
        add("1,+ 2x1", new JLabel("Building:"));
        add("1,+ 2x1 weighty=20 fill=hv", mBuilding);
        add("1,+ 2x1 fill=h", buttonBar);
        add("1,+", new JLabel("Completed:"));
        add("+,. fill=h", mCompleted);
        add("1,+", new JLabel("Turns Left:"));
        add("+,. fill=h", mTurnsLeft);
    }

    private void setCanBuild(boolean canWe)
    {
        mMoveUp.setVisible(canWe);
        mMoveDown.setVisible(canWe);
        mEdit.setVisible(canWe);
        mAdd.setVisible(canWe);
        mRemove.setVisible(canWe);
    }

    public void setObject(WorldInst obj)
    {
        mWorld = obj;
        if (mWorld == null)
        {
            mName.setObject(null);
            mUPP.setText(null);
            mSide.setObject(null);
            mConstRate.setText(null);
            mBuilding.setListData(new Ship[0]);
            mCompleted.setValue(0);
            mCompleted.setString("");
            mTurnsLeft.setText(null);
            setCanBuild(false);
        }
        else
        {
            mName.setObject(mWorld);
            mSide.setObject(mWorld.getSide());
            if (mWorld.getWorld() != null)
            {
                mUPP.setText(UPPLogic.getUPPDesc(
                        mWorld.getWorld().getPopulatedStats().getUPP()));
                int constPerTurn = WorldLogic
                        .getConstructionPerTurn(mPanel.getGame(), mWorld);
                mConstRate.setText(String.valueOf(constPerTurn) + " / turn");
                if ((mWorld.getSide() == mPanel.getSide())
                        && mPanel.getGame().getGame().isAllowConstruction())
                {
                    setCanBuild(true);
                    mBuilding.setListData(
                            mWorld.getUnderConstruction().toArray(new Ship[0]));
                }
                else
                {
                    mBuilding.setListData(new Ship[0]);
                    mCompleted.setValue(0);
                    mCompleted.setString("");
                    mTurnsLeft.setText(null);
                    setCanBuild(false);
                }
            }
            else
            {
                mUPP.setText(null);
                mConstRate.setText(null);
                mBuilding.setListData(new Ship[0]);
            }
        }
    }

    private void doSelectShip()
    {
        Ship design = mBuilding.getSelectedValue();
        if (design == null)
        {
            mCompleted.setValue(0);
            mCompleted.setString("");
            mTurnsLeft.setText(null);
        }
        else
        {
            int cost = ShipLogic.cost(design);
            int done = mWorld.getConstructionDone();
            mCompleted.setMaximum(cost);
            mCompleted.setValue(done);
            mCompleted.setString(done + " of " + cost);
            int constPerTurn = WorldLogic
                    .getConstructionPerTurn(mPanel.getGame(), mWorld);
            int left = (int)(Math
                    .ceil((double)(cost - done) / (double)constPerTurn));
            mTurnsLeft.setText(String.valueOf(left));
        }
    }

    private void doActivateShip()
    {
        Ship design = (Ship)mBuilding.getSelectedValue();
        if (design != null)
            mInfo.setObject(design);
    }

    private void doBuild()
    {
        if (mWorld == null)
            return;
        if (mWorld.getWorld() == null)
            return;
        SideInst side = mPanel.getSide();
        if (side != mWorld.getSide())
            return;
        int port = mWorld.getWorld().getPopulatedStats().getUPP().getPort()
                .getValue();
        if ((port != 'A') && (port != 'B'))
            return;
        Ship design = new Ship();
        Ship sel = (Ship)mBuilding.getSelectedValue();
        if (sel != null)
            ShipLogic.copy(sel, design);
        DlgShipDesigner dlg = new DlgShipDesigner(
                (Frame)SwingUtilities.getRoot(this));
        dlg.setShip(design);
        dlg.setJumpLimit(WorldLogic.getMaxJumpConstruction(mWorld));
        dlg.setConstRate(
                WorldLogic.getConstructionPerTurn(mPanel.getGame(), mWorld));
        dlg.setGame(mPanel.getGame().getGame());
        dlg.setVisible(true);
        if (dlg.isAccepted())
        {
            mWorld.getUnderConstruction().add(dlg.getShip());
            setObject(mWorld); // reset UI
        }
    }

    private void doEdit()
    {
        if (mWorld == null)
            return;
        if (mWorld.getWorld() == null)
            return;
        SideInst side = mPanel.getSide();
        if (side != mWorld.getSide())
            return;
        int port = mWorld.getWorld().getPopulatedStats().getUPP().getPort()
                .getValue();
        if ((port != 'A') && (port != 'B'))
            return;
        Ship design = (Ship)mBuilding.getSelectedValue();
        if (design == null)
            return;
        int idx = mBuilding.getSelectedIndex();
        if ((idx == 0) && (mWorld.getConstructionDone() > 0))
        {
            int yesno = JOptionPane.showConfirmDialog(null,
                    "Changing your ship design will reset all current progress. Are you sure?",
                    "Change Production", JOptionPane.YES_NO_OPTION);
            if (yesno != 0)
                return;
        }
        DlgShipDesigner dlg = new DlgShipDesigner(
                (Frame)SwingUtilities.getRoot(this));
        dlg.setShip(design);
        dlg.setJumpLimit(WorldLogic.getMaxJumpConstruction(mWorld));
        dlg.setConstRate(
                WorldLogic.getConstructionPerTurn(mPanel.getGame(), mWorld));
        dlg.setGame(mPanel.getGame().getGame());
        dlg.setVisible(true);
        if (dlg.isAccepted())
        {
            if (idx == 0)
                mWorld.setConstructionDone(0);
            setObject(mWorld); // reset UI
            mBuilding.setSelectedIndex(idx);
        }
    }

    /**
     * 
     */
    protected void doMoveDown()
    {
        List<Ship> ships = mWorld.getUnderConstruction();
        int idx = mBuilding.getSelectedIndex();
        if ((idx < 0) || (idx == ships.size() - 1))
            return;
        if ((idx == 0) && (mWorld.getConstructionDone() > 0))
        {
            int yesno = JOptionPane.showConfirmDialog(null,
                    "Moving this ship design will reset all current progress. Are you sure?",
                    "Change Production", JOptionPane.YES_NO_OPTION);
            if (yesno != 0)
                return;
        }
        Ship ship = ships.get(idx);
        ships.remove(idx);
        ships.add(idx + 1, ship);
        setObject(mWorld); // reset UI
        mBuilding.setSelectedIndex(idx + 1);
    }

    /**
     * 
     */
    protected void doMoveUp()
    {
        List<Ship> ships = mWorld.getUnderConstruction();
        int idx = mBuilding.getSelectedIndex();
        if ((idx < 0) || (idx == 0))
            return;
        if ((idx == 1) && (mWorld.getConstructionDone() > 0))
        {
            int yesno = JOptionPane.showConfirmDialog(null,
                    "Moving this ship design will reset all current progress. Are you sure?",
                    "Change Production", JOptionPane.YES_NO_OPTION);
            if (yesno != 0)
                return;
        }
        Ship ship = ships.get(idx);
        ships.remove(idx);
        ships.add(idx - 1, ship);
        setObject(mWorld); // reset UI
        mBuilding.setSelectedIndex(idx - 1);
    }

    /**
     * 
     */
    protected void doScrap()
    {
        if (mWorld == null)
            return;
        if (mWorld.getWorld() == null)
            return;
        SideInst side = mPanel.getSide();
        if (side != mWorld.getSide())
            return;
        int port = mWorld.getWorld().getPopulatedStats().getUPP().getPort()
                .getValue();
        if ((port != 'A') && (port != 'B'))
            return;
        Ship design = (Ship)mBuilding.getSelectedValue();
        if (design == null)
            return;
        int idx = mBuilding.getSelectedIndex();
        if ((idx == 0) && (mWorld.getConstructionDone() > 0))
        {
            int yesno = JOptionPane.showConfirmDialog(null,
                    "Deleting this ship design will reset all current progress. Are you sure?",
                    "Change Production", JOptionPane.YES_NO_OPTION);
            if (yesno != 0)
                return;
        }
        if (idx == 0)
            mWorld.setConstructionDone(0);
        mWorld.getUnderConstruction().remove(idx);
        setObject(mWorld); // reset UI
    }
}
