package jo.ttg.lbb.ui.ship2;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Container;
import java.awt.List;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatMessage;
import jo.ttg.lbb.ui.util.TableLayout;

@SuppressWarnings("serial")
public class MessagePanel extends Container implements PropertyChangeListener, ItemListener
{
	private Combat	mCombat;
	private int		mLastMessage;
	private int		mVerbosity;
	
	private List		mMessages;
	private Checkbox	mHigh;
	private Checkbox	mMedium;
	private Checkbox	mLow;

	public MessagePanel()
	{
		mVerbosity = CombatMessage.MEDIUM;
		
		CheckboxGroup group = new CheckboxGroup();
		mMessages = new List();
		mHigh = new Checkbox("H", group, false);
		mMedium = new Checkbox("M", group, true);
		mLow = new Checkbox("L", group, false);
		
		setLayout(new TableLayout());
		add("1,1,1,4 fill=hv", mMessages);
		add("2,1", mHigh);
		add("2,2", mMedium);
		add("2,3", mLow);
		
		mHigh.addItemListener(this);
		mMedium.addItemListener(this);
		mLow.addItemListener(this);
	}
	
	private void updateMessages()
	{
		if (mLastMessage == 0)
			mMessages.removeAll();
		if (mCombat == null)
			return;
		while (mLastMessage < mCombat.getMessages().size())
		{
			CombatMessage msg = mCombat.getMessages().get(mLastMessage++);
			if (msg.getPriority() <= mVerbosity)
				mMessages.add(msg.getMessage());
		}
		if (mMessages.getItemCount() > 0)
			mMessages.select(mMessages.getItemCount() - 1);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		updateMessages();
	}

	@Override
	public void itemStateChanged(ItemEvent ev)
	{
		if (mHigh.getState())
			mVerbosity = CombatMessage.HIGH;
		else if (mMedium.getState())
			mVerbosity = CombatMessage.MEDIUM;
		else if (mLow.getState())
			mVerbosity = CombatMessage.LOW;
		mLastMessage = 0;
		updateMessages();
	}

	public Combat getCombat()
	{
		return mCombat;
	}

	public void setCombat(Combat combat)
	{
		if (mCombat != null)
			mCombat.removePropertyChangeListener(this);
		mCombat = combat;
		if (mCombat != null)
			mCombat.addPropertyChangeListener(this);
		mLastMessage = 0;
		updateMessages();
	}

}
