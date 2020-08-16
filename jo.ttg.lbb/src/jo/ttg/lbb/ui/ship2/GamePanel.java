package jo.ttg.lbb.ui.ship2;

import java.awt.CardLayout;
import java.awt.Container;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.logic.ship2.ai.AI;

@SuppressWarnings("serial")
public class GamePanel extends Container
{
	private Combat				mCombat;
	
	private CardLayout			mLayout;
	private ScenarioChoicePanel	mNoCombatPanel;
	private CombatPanel			mCombatPanel;
	
	public GamePanel()
	{
		mLayout = new CardLayout();
		setLayout(mLayout);
		
		mNoCombatPanel = new ScenarioChoicePanel(this);
		add(mNoCombatPanel, "nocombat");
		mCombatPanel = new CombatPanel(this);
		add(mCombatPanel, "combat");
	}

	public Combat getCombat()
	{
		return mCombat;
	}

	public void setCombat(Combat combat)
	{
		mCombat = combat;
		if (mCombat == null)
			mLayout.show(this, "nocombat");
		else
		{
			mLayout.show(this, "combat");
			new AI(mCombat, mCombat.getSides().get(1));
		}
		mCombatPanel.setCombat(mCombat);
	}
	
	
}
