/*
 * Created on Feb 6, 2005
 *
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.beans.LocBean;
import jo.ttg.core.ui.swing.ctrl.Bogey;
import jo.ttg.core.ui.swing.ctrl.BogeyPanel;
import ttg.beans.adv.Game;
import ttg.beans.adv.ShipInst;

/**
 * @author Jo
 *
 */
public class ShipCombatDlg extends JDialog
{
    @SuppressWarnings("unused")
    private Game                 mGame;
    private List<ShipInst>       mGoodGuys;
    private List<ShipInst>       mBadGuys;
    private Map<ShipInst, Bogey> mBogeys;

    private BogeyPanel           mClient;

    /**
     *
     */

    public ShipCombatDlg(JFrame parent, Game game, List<ShipInst> goodGuys,
            List<ShipInst> badGuys)
    {
        super(parent);
        mGame = game;
        mGoodGuys = goodGuys;
        mBadGuys = badGuys;
        initInstantiate();
        initLink();
        initLayout();
    }

    public ShipCombatDlg(JDialog parent, Game game, List<ShipInst> goodGuys,
            List<ShipInst> badGuys)
    {
        super(parent);
        mGame = game;
        mGoodGuys = goodGuys;
        mBadGuys = badGuys;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setTitle("Ship Combat");
        mClient = new BogeyPanel();

        mBogeys = new HashMap<>();
        double dy = 0;
        for (ShipInst ship : mGoodGuys)
        {
            Bogey b = new Bogey();
            b.setColor(Color.GREEN);
            b.setSprite(3);
            b.setLocation(new LocBean(-3, dy, 0));
            mBogeys.put(ship, b);
            dy++;
            mClient.addBogey(b);
        }
        dy = 0;
        for (ShipInst ship : mBadGuys)
        {
            Bogey b = new Bogey();
            b.setColor(Color.RED);
            b.setSprite(1);
            b.setLocation(new LocBean(-3, dy, 0));
            mBogeys.put(ship, b);
            dy++;
            mClient.addBogey(b);
        }
    }

    private void initLink()
    {
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(new JLabel("..."));

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("South", buttonBar);
        getContentPane().add("Center", mClient);
        setSize(640, 480);
    }

}
