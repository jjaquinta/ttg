package ttg.war.view.act;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IconLogic;
import ttg.war.view.HelpPanel;
import ttg.war.view.ObjectButton;
import ttg.war.view.ShipRenderer;
import ttg.war.view.WarButton;
import ttg.war.view.WarPanel;

public class RepairShipsPanel extends HelpPanel
        implements ListSelectionListener, PropertyChangeListener
{
    private WorldInst       mWorldInst;

    private ObjectButton    mWorld;
    private JList<ShipInst> mShips;
    private JLabel          mNumber;
    private JButton         mSet;
    private JButton         mReset;
    private JButton         mDone;
    private boolean         mEclipse;
    private int             mPicked;
    private int             mMax;

    /**
     *
     */

    public RepairShipsPanel(WarPanel panel)
    {
        mPanel = panel;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mWorld = new ObjectButton(mPanel);
        mShips = new JList<ShipInst>();
        mNumber = new JLabel();
        mShips.setCellRenderer(new ShipRenderer(IconLogic.SEL_TODO));
        mSet = new WarButton("Set", IconLogic.mButtonSet);
        mReset = new WarButton("Reset", IconLogic.mButtonReset);
        mDone = new WarButton("Done", IconLogic.mButtonDone);
    }

    private void initLink()
    {
        ListenerUtils.listen(mSet, (ev) -> doSet());
        ListenerUtils.listen(mReset, (ev) -> doReset());
        ListenerUtils.listen(mDone, (ev) -> doDone());
        mShips.addListSelectionListener(this);
    }

    private void initLayout()
    {
        setLayout(new TableLayout("anchor=w"));
        add("1,+ 2x1 fill=h", makeTitle("Repair Ships:", "ActionRepair.htm"));
        add("1,+", new JLabel("World:"));
        add("+,. fill=h", mWorld);
        add("1,+ 2x1 fill=hv weighty=30", new JScrollPane(mShips));
        add("2,+ fill=h", mNumber);
        add("2,+ fill=h", mSet);
        add("2,+ fill=h", mReset);
        add("2,+ fill=h", mDone);
    }

    public void init()
    {
        mPicked = 0;
        @SuppressWarnings("unchecked")
        List<ShipInst> ships = (List<ShipInst>)mPanel.getArg1();
        for (ShipInst ship : ships)
        {
            ship.setToDo(true);
            mWorldInst = ship.getLocation();
        }
        mShips.setListData(ships.toArray(new ShipInst[0]));
        mWorld.setObject(mWorldInst);
        int port = mWorldInst.getWorld().getPopulatedStats().getUPP().getPort()
                .getValue();
        if (port == 'A')
        {
            mNumber.setText("(can pick 2)");
            mMax = 2;
        }
        else
        {
            mNumber.setText("(pick 1)");
            mMax = 1;
        }
        mPanel.getInfoPanel().getPCS().addPropertyChangeListener("ships", this);
    }

    protected void doSet()
    {
        List<ShipInst> ships = mShips.getSelectedValuesList();
        if (ships.size() == 0)
        {
            mPanel.getGame().setStatus("No ship selected");
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (ShipInst ship : ships)
        {
            ship.setToDo(false);
            if (sb.length() > 0)
                sb.append(", ");
            sb.append(ship.getShip().getName());
        }
        sb.append(" set to repair.");
        mPanel.getGame().setStatus(sb.toString());
        mShips.repaint();
        mPanel.getInfoPanel().setObject(mShips.getSelectedValue());
        updatePicked();
    }

    protected void doReset()
    {
        List<ShipInst> ships = mPanel.getSide().getShips();
        for (ShipInst ship : ships)
        {
            ship.setToDo(true);
        }
        updatePicked();
    }

    protected void doDone()
    {
        mPanel.getInfoPanel().getPCS().removePropertyChangeListener("ships",
                this);
        mPanel.setMode(WarPanel.DONE);
    }

    private void updatePicked()
    {
        mPicked = 0;
        @SuppressWarnings("unchecked")
        List<ShipInst> ships = (List<ShipInst>)mPanel.getArg1();
        for (ShipInst ship : ships)
        {
            if (!ship.isToDo())
                mPicked++;
        }
        mDone.setEnabled(mPicked <= mMax);
    }

    public void valueChanged(ListSelectionEvent e)
    {
        ShipInst primaryShip = (ShipInst)mShips.getSelectedValue();
        if (!mEclipse && (primaryShip != null))
            mPanel.getInfoPanel().setObject(primaryShip);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        Object[] ships = (Object[])ev.getNewValue();
        mEclipse = true;
        int[] idx = new int[ships.length];
        for (int i = 0; i < ships.length; i++)
            idx[i] = mPanel.getSide().getShips().indexOf(ships[i]);
        mShips.setSelectedIndices(idx);
        mEclipse = false;
    }
}
