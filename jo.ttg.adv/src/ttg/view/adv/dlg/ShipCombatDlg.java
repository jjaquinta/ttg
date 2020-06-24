/*
 * Created on Feb 6, 2005
 *
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

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
    private Game		mGame;
    private ArrayList	mGoodGuys;
    private ArrayList	mBadGuys;
    private HashMap		mBogeys;
    
    private BogeyPanel	mClient;
	
	/**
	 *
	 */

	public ShipCombatDlg(JFrame parent, Game game, ArrayList goodGuys, ArrayList badGuys)
	{
		super(parent);
		mGame = game;
		mGoodGuys = goodGuys;
		mBadGuys = badGuys;
		initInstantiate();
		initLink();
		initLayout();
	}

	public ShipCombatDlg(JDialog parent, Game game, ArrayList goodGuys, ArrayList badGuys)
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

        mBogeys = new HashMap();
        double dy = 0;
        for (Iterator i = mGoodGuys.iterator(); i.hasNext(); )
        {
            ShipInst ship = (ShipInst)i.next();
            Bogey b = new Bogey();
            b.setColor(Color.GREEN);
            b.setSprite(3);
            b.setLocation(new LocBean(-3, dy, 0));
            mBogeys.put(ship, b);
            dy++;
            mClient.addBogey(b);
        }
        dy = 0;
        for (Iterator i = mBadGuys.iterator(); i.hasNext(); )
        {
            ShipInst ship = (ShipInst)i.next();
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
