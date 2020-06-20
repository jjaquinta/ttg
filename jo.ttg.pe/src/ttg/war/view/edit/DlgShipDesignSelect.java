/*
 * Created on Apr 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.edit;

import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.Game;
import ttg.war.beans.Ship;
import ttg.war.beans.ShipInst;
import ttg.war.logic.IconLogic;
import ttg.war.view.ShipRenderer;
import ttg.war.view.WarButton;
import ttg.war.view.info.ShipInfoPanel;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DlgShipDesignSelect extends JDialog
{
    private Game            mGame;
    private Ship            mShip;

    private JComboBox<Ship> mShips;
    private ShipInfoPanel   mInfo;
    private WarButton       mOK;
    private WarButton       mCancel;

    /**
     *
     */

    public DlgShipDesignSelect(Frame frame, Game game, ShipInst ship)
    {
        super(frame, "Ship Design Library", true);
        mGame = game;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mShip = null;
        mShips = new JComboBox<>(mGame.getShipLibrary().toArray(new Ship[0]));
        mShips.setRenderer(new ShipRenderer(IconLogic.SEL_DONT));
        mInfo = new ShipInfoPanel(null, null);
        mInfo.setObject(mShip);
        mOK = new WarButton("Done", IconLogic.mButtonDone);
        mCancel = new WarButton("Cancel", IconLogic.mButtonCancel);
    }

    private void initLink()
    {
        ListenerUtils.listen(mOK, (ev) -> doOK());
        ListenerUtils.listen(mCancel, (ev) -> doCancel());
        mShips.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ev)
            {
                doShipChoose();
            }
        });
    }

    private void initLayout()
    {
        JPanel p1 = new JPanel();
        p1.add(mOK);
        p1.add(mCancel);

        getContentPane().add("North", new JScrollPane(mShips));
        getContentPane().add("Center", mInfo);
        getContentPane().add("South", p1);

        setSize(256, 400);
    }

    private void doOK()
    {
        dispose();
    }

    private void doCancel()
    {
        mShip = null;
        dispose();
    }

    private void doShipChoose()
    {
        setShip((Ship)mShips.getSelectedItem());
    }

    /**
     * @return
     */
    public Ship getShip()
    {
        return mShip;
    }

    /**
     * @param ship
     */
    public void setShip(Ship ship)
    {
        mShip = ship;
        mInfo.setObject(mShip);
    }

}
