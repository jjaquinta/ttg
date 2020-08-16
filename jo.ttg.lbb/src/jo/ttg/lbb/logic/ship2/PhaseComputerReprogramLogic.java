package jo.ttg.lbb.logic.ship2;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.Ship2Instance;

public class PhaseComputerReprogramLogic
{
	public static void startPhase(Combat combat)
	{
	}
	
	public static void loadProgram(Combat combat, Ship2Instance ship, String program)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM, Combat.PHASE_NATIVE_COMPUTER_REPROGRAM);
		CombatLogic.validateShip(combat, ship);
		CombatLogic.validateProgramStored(ship, program);
		ship.getStoredPrograms().remove(program);
		ship.getLoadedPrograms().add(program);
	}
	
	public static void saveProgram(Combat combat, Ship2Instance ship, String program)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM, Combat.PHASE_NATIVE_COMPUTER_REPROGRAM);
		CombatLogic.validateShip(combat, ship);
		CombatLogic.validateProgramLoaded(ship, program);
		ship.getLoadedPrograms().remove(program);
		ship.getStoredPrograms().add(program);
	}
	
	public static void endPhase(Combat combat)
	{
	}
}
