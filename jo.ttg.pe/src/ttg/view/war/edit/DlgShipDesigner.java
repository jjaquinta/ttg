/*
 * Created on Apr 7, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.edit;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.io.IOException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.Game;
import ttg.beans.war.Ship;
import ttg.logic.war.DefaultGame;
import ttg.logic.war.IconLogic;
import ttg.logic.war.ShipLogic;
import ttg.view.war.WarButton;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DlgShipDesigner extends JDialog
{
	private Ship		mShip;
	private boolean		mAccepted;
	private boolean		mEclipse;
	private int			mMaxCost;
	private int			mConstRate;
	private Game		mGame;
	
	private JTextField	mName;
	private JSpinner	mAttack;
	private JSpinner	mDefense;
	private JSpinner	mJump;
	private JSpinner	mCapacityJump;
	private JSpinner	mCapacityHanger;
	private JLabel		mSize;
	private JLabel		mCapacity;
	private JLabel		mFuel1;
	private JLabel		mRange;
	private JLabel		mCost;
	private JLabel		mTime;
	private JButton		mLoad;
	private JButton		mSave;
	private JButton		mOK;
	private JButton		mCancel;
	
	/**
	 *
	 */

	public DlgShipDesigner(Dialog dlg)
	{
		super(dlg, "Pocket Empires - Ship Designer", true);
		initInstantiate();
		initLink();
		initLayout();
	}

	public DlgShipDesigner(Frame dlg)
	{
		super(dlg, "Pocket Empires - Ship Designer", true);
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mShip = new Ship();
		mName = new JTextField();
		mAttack = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
		mDefense = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));
		mJump = new JSpinner(new SpinnerNumberModel(1, 0, 6, 1));
		mCapacityJump = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
		mCapacityHanger = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
		mSize = new JLabel();
		mCapacity = new JLabel();
		mFuel1 = new JLabel();
		mRange = new JLabel();
		mCost = new JLabel();
		mTime = new JLabel();
		mLoad = new WarButton(IconLogic.mButtonLoad);
		mLoad.setToolTipText("Load ship from Library");
		mLoad.setVisible(false);
		mSave = new WarButton(IconLogic.mButtonSave);
		mSave.setToolTipText("Save ship to Library");
		mSave.setVisible(false);
		mOK = new WarButton(IconLogic.mButtonDone);
		mOK.setToolTipText("Accept Change");
		mCancel = new WarButton(IconLogic.mButtonReset);
		mCancel.setToolTipText("Abort change");
	}

	private void initLink()
	{
		ChangeListener l = new ChangeListener() {
            @Override
			public void stateChanged(ChangeEvent ev)
			{
				if (!mEclipse)
					doUIChange();
			}
		};
		mAttack.addChangeListener(l);
		mDefense.addChangeListener(l);
		mJump.addChangeListener(l);
		mCapacityHanger.addChangeListener(l);
		mCapacityJump.addChangeListener(l);
		ListenerUtils.listen(mOK, (ev) -> doOK());
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		ListenerUtils.listen(mLoad, (ev) -> doLoad());
		ListenerUtils.listen(mSave, (ev) -> doSave());
	}

	private void initLayout()
	{
		JPanel buttonBar = new JPanel();
		buttonBar.add(mLoad);
		buttonBar.add(mSave);
		buttonBar.add(mOK);
		buttonBar.add(mCancel);
		
		JPanel client = new JPanel();
		client.setLayout(new TableLayout("anchor=w"));
		client.add("1,+", new JLabel("Name:"));
		client.add("+,. fill=h", mName);
		client.add("1,+", new JLabel("Attack:"));
		client.add("+,. fill=h", mAttack);
		client.add("1,+", new JLabel("Defense:"));
		client.add("+,. fill=h", mDefense);
		client.add("1,+", new JLabel("Jump:"));
		client.add("+,. fill=h", mJump);
		client.add("1,+", new JLabel("Cap (j):"));
		client.add("+,. fill=h", mCapacityJump);
		client.add("1,+", new JLabel("Cap (h):"));
		client.add("+,. fill=h", mCapacityHanger);
		client.add("1,+", new JLabel("Size:"));
		client.add("+,. fill=h", mSize);
		client.add("1,+", new JLabel("Capacity:"));
		client.add("+,. fill=h", mCapacity);
		client.add("1,+", new JLabel("Jump-1:"));
		client.add("+,. fill=h", mFuel1);
		client.add("1,+", new JLabel("Range:"));
		client.add("+,. fill=h", mRange);
		client.add("1,+", new JLabel("Cost:"));
		client.add("+,. fill=h", mCost);
		client.add("1,+", new JLabel("Build Time:"));
		client.add("+,. fill=h", mTime);
		
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", client);
		setSize(256, 400);
	}
	
	private void doUIChange()
	{
		getShip(); // update ship
		int cap = ShipLogic.capacity(mShip);
		int fuel1 = ShipLogic.fuelForJump1(mShip);
		int cost = ShipLogic.cost(mShip);
		mSize.setText(String.valueOf(ShipLogic.size(mShip)));
		mCapacity.setText(String.valueOf(cap));
		mFuel1.setText(String.valueOf(fuel1));
		mRange.setText(ShipLogic.getJumpDescription(mShip, cap+fuel1));
		mCost.setText(String.valueOf(cost));
		if ((mMaxCost > 0) && (cost > mMaxCost))
			mCost.setForeground(Color.RED);
		else
			mCost.setForeground(null);
		if (mConstRate > 0)
		{
			int todo = (int)Math.ceil((double)cost/(double)mConstRate);
			mTime.setText(String.valueOf(todo));
		}
		else
			mTime.setText(null);
	}
	
	private void doOK()
	{
		doUIChange();
		mAccepted = true;
		dispose();
	}
	
	private void doCancel()
	{
		mAccepted = false;
		dispose();
	}
	
	private void doLoad()
	{
		DlgShipDesignSelect dlg = new DlgShipDesignSelect(null, mGame, null);
		dlg.setVisible(true);
		if (dlg.getShip() != null)
			setShip(dlg.getShip());
	}
	
	private void doSave()
	{
		ShipLogic.addUnique(mGame.getShipLibrary(), getShip());
	}
	
	/**
	 * @return
	 */
	public Ship getShip()
	{
		try
		{
			DefaultGame.parseShip(mShip, getLine());
		}
		catch (IOException e)
		{
		}
		return mShip;
	}

	/**
	 * @param ship
	 */
	public void setShip(Ship ship)
	{
		mShip = ship;
		mName.setText(mShip.getName());
		int cap = mShip.getCapacity();
		int fuel1 = ShipLogic.fuelForJump1(mShip);
		int capJ;		
		int capH;
		if (fuel1 == 0)
		{
			capJ = 0;
			capH = cap/100;
		}
		else
		{
			// see which fits with whole numbers
			if ((cap%fuel1) == 0)
			{
				capJ = cap/fuel1;
				capH = 0;
			}
			else if ((cap%100) == 0)
			{
				capJ = 0;
				capH = cap/100;
			}
			else
			{
				capJ = cap;
				capH = 0;
			}
		}
		mEclipse = true;
		mAttack.setValue(new Integer(mShip.getAttack()));
		mDefense.setValue(new Integer(mShip.getDefense()));
		int jump = mShip.getJump();
		if (jump > getJumpLimit())
			jump = getJumpLimit();
		mJump.setValue(new Integer(jump));
		mCapacityHanger.setValue(new Integer(capH));
		mCapacityJump.setValue(new Integer(capJ));
		mEclipse = false;
		doUIChange();
	}

	public void setLine(String line)
	{
		StringTokenizer st = new StringTokenizer(line, ",");
		mName.setText(st.nextToken());
		mEclipse = true;
		mAttack.setValue(new Integer(st.nextToken()));
		mDefense.setValue(new Integer(st.nextToken()));
		String cap = st.nextToken();
		int o = cap.indexOf(".");
		if (o < 0)
		{
			mCapacityHanger.setValue(new Integer(cap));
			mCapacityJump.setValue(new Integer(0));
		}
		else
		{
			mCapacityHanger.setValue(new Integer(cap.substring(0, o)));
			mCapacityJump.setValue(new Integer(cap.substring(o+1)));
		}
		mEclipse = false;
		doUIChange();
	}
	
	public String getLine()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(mName.getText());
		sb.append(",");
		sb.append(((Integer)mAttack.getValue()).intValue());
		sb.append(",");
		sb.append(((Integer)mDefense.getValue()).intValue());
		sb.append(",");
		sb.append(((Integer)mCapacityHanger.getValue()).intValue());
		sb.append(".");
		sb.append(((Integer)mCapacityJump.getValue()).intValue());
		sb.append(",");
		sb.append(((Integer)mJump.getValue()).intValue());
		return sb.toString();
	}
	/**
	 * @return
	 */
	public boolean isAccepted()
	{
		return mAccepted;
	}

    public void setMaxCost(int i)
    {
        mMaxCost = i;
    }

	public void setJumpLimit(int i)
	{
		((SpinnerNumberModel)mJump.getModel()).setMaximum(new Integer(i));
	}

	public int getJumpLimit()
	{
		return ((Integer)((SpinnerNumberModel)mJump.getModel()).getMaximum()).intValue();
	}

	/**
	 * @return
	 */
	public int getConstRate()
	{
		return mConstRate;
	}

	/**
	 * @param i
	 */
	public void setConstRate(int i)
	{
		mConstRate = i;
	}
	
	/**
	 * @return
	 */
	public Game getGame()
	{
		return mGame;
	}

	/**
	 * @param inst
	 */
	public void setGame(Game inst)
	{
		mGame = inst;
		mLoad.setVisible(true);
		mSave.setVisible(true);
	}

}
