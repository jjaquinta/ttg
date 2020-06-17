/*
 * Created on Apr 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.edit;

import java.awt.Frame;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.Game;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.logic.war.IconLogic;
import ttg.view.war.ShipRenderer;
import ttg.view.war.WarButton;
import ttg.view.war.info.ShipInfoPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DlgShipDesignSelect extends JDialog
{
	private Game		mGame;
	private Ship			mShip;
	
	private JComboBox		mShips;
	private ShipInfoPanel	mInfo;
	private WarButton		mOK;
	private WarButton		mCancel;
	
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
		mShips = new JComboBox(mGame.getShipLibrary().toArray());
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
		mShips.addItemListener( new ItemListener() {
            @Override
			public void itemStateChanged(ItemEvent ev) { doShipChoose(); }
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
