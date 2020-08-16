package jo.ttg.lbb.test;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.logic.ship2.CombatLogic;
import jo.ttg.lbb.logic.ship2.PhaseLogic;
import jo.ttg.lbb.logic.ship2.ScenarioLogic;
import jo.ttg.lbb.logic.ship2.ai.AI;

public class Ship2Test implements Runnable
{
	private String[]	mArgs;
	
	public Ship2Test(String[] args)
	{
		mArgs = args;
	}
	
	public void run()
	{
		ScenarioLogic.loadBuiltIn();
		Combat combat;
		if (mArgs.length > 0)
			combat = CombatLogic.newCombat(mArgs[0], "Test Combat");
		else
			combat = CombatLogic.newCombat("test", "Test Combat");
		AI ai1 = new AI(combat, combat.getSides().get(0));
		AI ai2 = new AI(combat, combat.getSides().get(1));
		while (combat.getTurn() < 16)
		{
			System.out.println("Turn "+combat.getTurn()+", "+Combat.PHASE_NAMES[combat.getPhase()]+":");
			int numMessages = combat.getMessages().size();
			ai1.takeTurn();
			ai2.takeTurn();
			PhaseLogic.nextPhase(combat);
			for (int i = numMessages; i < combat.getMessages().size(); i++)
				System.out.println("  "+combat.getMessages().get(i));
		}
	}
	
	public static void main(String[] argv)
	{
		Ship2Test app = new Ship2Test(argv);
		app.run();
	}
}
