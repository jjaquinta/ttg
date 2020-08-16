package jo.ttg.lbb.logic.ship2;

import jo.ttg.lbb.data.ship2.Combat;

public class PhaseLogic
{
	public static void nextPhase(Combat combat)
	{
		switch (combat.getPhase())
		{
			case Combat.PHASE_INTRUDER_MOVEMENT:
				PhaseMovementLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_INTRUDER_LASER_FIRE);
				PhaseLaserFireLogic.startPhase(combat);
				break;
			case Combat.PHASE_INTRUDER_LASER_FIRE:
				PhaseLaserFireLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_NATIVE_LASER_RETURN_FIRE);
				PhaseLaserReturnFireLogic.startPhase(combat);
				break;
			case Combat.PHASE_NATIVE_LASER_RETURN_FIRE:
				PhaseLaserReturnFireLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_INTRUDER_ORDANCE_LAUNCH);
				PhaseOrdnanceLaunchLogic.startPhase(combat);
				break;
			case Combat.PHASE_INTRUDER_ORDANCE_LAUNCH:
				PhaseOrdnanceLaunchLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM);
				PhaseComputerReprogramLogic.startPhase(combat);
				break;
			case Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM:
				PhaseComputerReprogramLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_NATIVE_MOVEMENT);
				PhaseMovementLogic.startPhase(combat);
				break;
			case Combat.PHASE_NATIVE_MOVEMENT:
				PhaseMovementLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_NATIVE_LASER_FIRE);
				PhaseLaserFireLogic.startPhase(combat);
				break;
			case Combat.PHASE_NATIVE_LASER_FIRE:
				PhaseLaserFireLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_INTRUDER_LASER_RETURN_FIRE);
				PhaseLaserReturnFireLogic.startPhase(combat);
				break;
			case Combat.PHASE_INTRUDER_LASER_RETURN_FIRE:
				PhaseLaserReturnFireLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_NATIVE_ORDANCE_LAUNCH);
				PhaseOrdnanceLaunchLogic.startPhase(combat);
				break;
			case Combat.PHASE_NATIVE_ORDANCE_LAUNCH:
				PhaseOrdnanceLaunchLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_NATIVE_COMPUTER_REPROGRAM);
				PhaseComputerReprogramLogic.startPhase(combat);
				break;
			case Combat.PHASE_NATIVE_COMPUTER_REPROGRAM:
				PhaseComputerReprogramLogic.endPhase(combat);
				combat.setPhase(Combat.PHASE_INTERPHASE);
				PhaseGameTurnLogic.startPhase(combat);
				break;
			case Combat.PHASE_INTERPHASE:
				PhaseGameTurnLogic.endPhase(combat);
				// TODO: combat over?
				combat.setTurn(combat.getTurn() + 1);
				combat.setPhase(Combat.PHASE_INTRUDER_MOVEMENT);
				PhaseMovementLogic.startPhase(combat);
				break;
		}
	}

	public static void kickOff(Combat combat)
	{
		PhaseMovementLogic.startPhase(combat);
	}
}
