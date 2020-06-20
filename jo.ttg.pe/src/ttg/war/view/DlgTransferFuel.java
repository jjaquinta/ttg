/*
 * Created on Apr 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.ShipInst;
import ttg.war.logic.IconLogic;
import ttg.war.logic.ShipLogic;
import ttg.war.view.info.ShipInstInfoPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DlgTransferFuel extends JDialog
{
	private WarPanel		mPanel;
	private ShipInst		mShip1;
	private ShipInst		mShip2;
	
	private ShipInstInfoPanel	mInfo1;
	private ShipInstInfoPanel	mInfo2;
	private JButton			mF;
	private JButton			mFF;
	private JButton			mFFF;
	private JButton			mR;
	private JButton			mRR;
	private JButton			mRRR;
	private JButton			mOK;
	private JButton			mHelp;
	
	/**
	 *
	 */

	public DlgTransferFuel(Frame frame, WarPanel panel, ShipInst ship1, ShipInst ship2)
	{
		super(frame, "Transfer Fuel", true);
		mPanel = panel;
		mShip1 = ship1;
		mShip2 = ship2;
		initInstantiate();
		initLink();
		initLayout();
	}
	
	private void initInstantiate()
	{
		mInfo1 = new ShipInstInfoPanel(mPanel, null);
		mInfo1.setObject(mShip1);
		mInfo1.setShortForm(true);
		mInfo2 = new ShipInstInfoPanel(mPanel, null);
		mInfo2.setObject(mShip2);
		mInfo2.setShortForm(true);
		mOK = new WarButton("Done", IconLogic.mButtonDone);
		mF = new WarButton(null, IconLogic.mButtonForward);
		mFF = new WarButton(null, IconLogic.mButtonFastForward);
		mFFF = new WarButton(null, IconLogic.mButtonForwardToEnd);
		mR = new WarButton(null, IconLogic.mButtonReverse);
		mRR = new WarButton(null, IconLogic.mButtonFastReverse);
		mRRR = new WarButton(null, IconLogic.mButtonReverseToEnd);
		mHelp = new WarButton(null, IconLogic.mButtonHelp);
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK());
		ListenerUtils.listen(mHelp, (ev) -> doHelp());
		ListenerUtils.listen(mF, (ev) -> doTransfer(mShip1, mShip2, ShipLogic.FUEL_ONE));
		ListenerUtils.listen(mFF, (ev) -> doTransfer(mShip1, mShip2, ShipLogic.FUEL_TENTH));
		ListenerUtils.listen(mFFF, (ev) -> doTransfer(mShip1, mShip2, ShipLogic.FUEL_ALL));
		ListenerUtils.listen(mR, (ev) -> doTransfer(mShip2, mShip1, ShipLogic.FUEL_ONE));
		ListenerUtils.listen(mRR, (ev) -> doTransfer(mShip2, mShip1, ShipLogic.FUEL_TENTH));
		ListenerUtils.listen(mRRR, (ev) -> doTransfer(mShip2, mShip1, ShipLogic.FUEL_ALL));
	}

	private void initLayout()
	{
		JPanel p1 = new JPanel();
		p1.add(mRRR);
		p1.add(mRR);
		p1.add(mR);
		p1.add(mOK);
		p1.add(mF);
		p1.add(mFF);
		p1.add(mFFF);
		JPanel p2 = new JPanel();
		p2.setLayout(new BorderLayout());
		p2.add("Center", p1);
		p2.add("East", mHelp);
		
		getContentPane().add("West", mInfo1);
		getContentPane().add("East", mInfo2);
		getContentPane().add("South", p2);
		
		setSize(500, 600);
	}

	private void doOK()
	{
		dispose();
	}

	private void doHelp()
	{
		mPanel.doHelp("DialogFuelTransfer.htm");
	}

	private void doTransfer(ShipInst from, ShipInst to, int mode)
	{
		ShipLogic.transferFuel(from, to, mode);
		mInfo1.setObject(mShip1);
		mInfo2.setObject(mShip2);
	}
}
