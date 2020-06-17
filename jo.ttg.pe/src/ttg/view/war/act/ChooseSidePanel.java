package ttg.view.war.act;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.SideInst;
import ttg.logic.war.IconLogic;
import ttg.logic.war.PhaseLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.WarButton;
import ttg.view.war.WarPanel;
import ttg.view.war.ai.ComputerPlayer;
import ttg.view.war.ai.GUIPlayer;
import ttg.view.war.ai.ObserverPlayer;

public class ChooseSidePanel extends HelpPanel
{
	private ButtonGroup	mGroup;
	
	private JRadioButton[]	mSides;
	private JButton			mOK;
	
	/**
	 *
	 */

	public ChooseSidePanel(WarPanel panel)
	{
		mPanel = panel;
	}
	
	public void init()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mGroup = new ButtonGroup();
		mSides = new JRadioButton[mPanel.getGame().getSides().size()];
		for (int i = 0; i < mSides.length; i++)
		{
			SideInst side = (SideInst)mPanel.getGame().getSides().get(i);
			mSides[i] = new JRadioButton(side.getSide().getName());
			mSides[i].setIcon(IconLogic.getSideIcon(side, false));
			mSides[i].setSelectedIcon(IconLogic.getSideIcon(side, true));
			mGroup.add(mSides[i]);
		}
		mSides[0].setSelected(true);
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK(ev.getModifiers()));
		for (int i = 0; i < mSides.length; i++)
		    ListenerUtils.listen(mSides[i], (ev) -> doSide());
	}

    private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", makeTitle("Pick side:", "ActionChooseSide.htm"));
		for (int i = 0; i < mSides.length; i++)
			add("1,+ fill=h", mSides[i]);
		add("1,+ fill=hv weighty=30", new JLabel(""));
		add("1,+ fill=h", mOK);
	}

	protected void doOK(int mods)
	{
		for (int i = 0; i < mSides.length; i++)
		{
			SideInst side = (SideInst)mPanel.getGame().getSides().get(i);
			if (mSides[i].isSelected())
			{
				if ((mods&ActionEvent.CTRL_MASK) != 0)
					side.setPlayer(new ObserverPlayer(mPanel, mPanel.getGame(), side));
				else
					side.setPlayer(new GUIPlayer(mPanel));
				mPanel.setSide(side);
			}
			else
				side.setPlayer(new ComputerPlayer(mPanel.getGame(), side));
		}
		PhaseLogic.start(mPanel.getGame());
	}
	
	protected void doSide()
	{
		for (int i = 0; i < mSides.length; i++)
			if (mSides[i].isSelected())
			{
				mPanel.getInfoPanel().setObject(mPanel.getGame().getSides().get(i));
				return;
			}
		mPanel.getInfoPanel().setObject(null);
	}
}
