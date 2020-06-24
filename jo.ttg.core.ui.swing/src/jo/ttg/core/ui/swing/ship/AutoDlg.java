/*
 * Created on Jan 8, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ship;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.logic.AutoGen;
import jo.util.ui.swing.utils.ListenerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AutoDlg extends JDialog
{
	private ShipBean		mShip;

	private JTextField		mTechLevel;
	private JTextField		mTonnage;
	private JTextField		mJump;
	private JTextField		mManeuver;
	private JTextField		mPassengers;
	private JTextField		mBerths;
	private JComboBox<String>		mType;
	private JButton mOK;
	private JButton mCancel;

	public AutoDlg(JFrame parent, ShipBean ship)
	{
		super(parent, "Auto Generate Ship", true);
		mShip = ship;
		initInstantiate();
		initLink();
		initLayout();
	}

	public AutoDlg(JDialog parent, ShipBean ship)
	{
		super(parent, "Auto Generate Ship", true);
		mShip = ship;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		// controls
		mTechLevel = new JTextField("12", 4);
		mTonnage = new JTextField("100", 4);
		mJump = new JTextField("2", 4);
		mManeuver = new JTextField("1.5", 4);
		mPassengers = new JTextField("8", 4);
		mBerths = new JTextField("12", 4);
		mType = new JComboBox<>();
		mType.addItem("Merchant");
		mType.addItem("Scout");
		mType.addItem("Military");
		mType.setSelectedIndex(0);
		mOK = new JButton("OK");
		mCancel = new JButton("Cancel");
	}
	private void initLayout()
	{
		JPanel buttonBar1 = new JPanel();
		buttonBar1.add(mOK);
		buttonBar1.add(mCancel);
		
		JPanel p = new JPanel();
		p.setLayout(new GridLayout(7, 2));
		p.add(new JLabel("TechLevel:"));
		p.add(mTechLevel);
		p.add(new JLabel("Tons:"));
		p.add(mTonnage);
		p.add(new JLabel("Jump:"));
		p.add(mJump);
		p.add(new JLabel("Maneuver:"));
		p.add(mManeuver);
		p.add(new JLabel("Passengers:"));
		p.add(mPassengers);
		p.add(new JLabel("Berths:"));
		p.add(mBerths);
		p.add(new JLabel("Type:"));
		p.add(mType);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", p);
		getContentPane().add("South", buttonBar1);
		setSize(480, 300);
	}
	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK());
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
	}

	/**
	 * 
	 */
	protected void doCancel()
	{
		dispose();
	}

	/**
	 * 
	 */
	protected void doOK()
	{
		dispose();
		int tl = FormatUtils.atoi(mTechLevel.getText());
		int tons = FormatUtils.atoi(mTonnage.getText());
		int jump = FormatUtils.atoi(mJump.getText());
		double man = FormatUtils.atod(mManeuver.getText());
		int pass = FormatUtils.atoi(mPassengers.getText());
		int berths = FormatUtils.atoi(mBerths.getText());
		int type = mType.getSelectedIndex();
		AutoGen.genMerchant(mShip, tl, tons, jump, man, pass, berths, type);
	}
}
