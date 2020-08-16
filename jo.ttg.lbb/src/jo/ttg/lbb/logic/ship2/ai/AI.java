package jo.ttg.lbb.logic.ship2.ai;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.ttg.lbb.logic.ship2.CombatLogic;
import jo.ttg.lbb.logic.ship2.PhaseComputerReprogramLogic;
import jo.ttg.lbb.logic.ship2.PhaseLaserFireLogic;
import jo.ttg.lbb.logic.ship2.PhaseLogic;
import jo.ttg.lbb.logic.ship2.PhaseMovementLogic;
import jo.ttg.lbb.logic.ship2.PhaseOrdnanceLaunchLogic;
import jo.ttg.lbb.logic.ship2.Ship2Logic;
import jo.util.geom3d.Point3D;

public class AI implements PropertyChangeListener
{
	private Combat		mCombat;
	private CombatSide	mSide;
	private CombatSide	mOtherSide;
	
	private Ship2Instance	mTarget;
	
	public AI(Combat combat, CombatSide side)
	{
		mCombat = combat;
		mSide = side;
		for (CombatSide s : mCombat.getSides())
			if (s != mSide)
			{
				mOtherSide = s;
				break;
			}
		System.out.println("Side: "+mSide.getName());
		for (Ship2Instance ship : mSide.getShips())
		{
			System.out.println("  Ship: "+ship.getName());
			for (Ship2HardpointInstance hp : ship.getTurrets())
			{
				System.out.print("    "+hp.getName()+":");
				if (hp.getDesign().getBeamLasers() > 0)
					System.out.print(" "+hp.getDesign().getBeamLasers()+" beam lasers");
				if (hp.getDesign().getPulseLasers() > 0)
					System.out.print(" "+hp.getDesign().getPulseLasers()+" pulse lasers");
				if (hp.getDesign().getMissiles() > 0)
					System.out.print(" "+hp.getDesign().getMissiles()+" missles");
				if (hp.getDesign().getSandcasters() > 0)
					System.out.print(" "+hp.getDesign().getSandcasters()+" sandcasters");
				System.out.println();				
			}
		}
		mCombat.addPropertyChangeListener(this);
	}
	
	public void takeTurn()
	{
		if (CombatLogic.getSide(mCombat) != mSide)
			return; // not us
		confirmTarget();
		switch (mCombat.getPhase())
		{
			case Combat.PHASE_INTRUDER_MOVEMENT:
			case Combat.PHASE_NATIVE_MOVEMENT:
				move();
				break;
			case Combat.PHASE_INTRUDER_LASER_FIRE:
			case Combat.PHASE_NATIVE_LASER_FIRE:
				fire();
				break;
			case Combat.PHASE_NATIVE_LASER_RETURN_FIRE:
			case Combat.PHASE_INTRUDER_LASER_RETURN_FIRE:
				returnFire();
				break;
			case Combat.PHASE_INTRUDER_ORDANCE_LAUNCH:
			case Combat.PHASE_NATIVE_ORDANCE_LAUNCH:
				ordanceLaunch();
				break;
			case Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM:
			case Combat.PHASE_NATIVE_COMPUTER_REPROGRAM:
				computerReprogram();
				break;
		}
		PhaseLogic.nextPhase(mCombat);
	}
	
	private void confirmTarget()
	{
		if ((mTarget != null) && CombatLogic.isShipExists(mCombat, mTarget) && isThreat(mTarget))
			return;
		List<Ship2Instance> threats = new ArrayList<Ship2Instance>();
		for (Ship2Instance ship : mOtherSide.getShips())
			if (isThreat(ship))
				threats.add(ship);
		if (threats.size() == 0)
		{
			mTarget = null;
			return;
		}
		mTarget = threats.get(mCombat.getRND().nextInt(threats.size()));		
	}
	
	private boolean isThreat(Ship2Instance ship)
	{
		for (Ship2HardpointInstance hp : ship.getTurrets())
		{
			if (hp.isDamage())
				continue;
			if ((hp.getDesign().getMissiles() > 0) && (hp.getMissiles() > 0))
				return true;
			if ((hp.getDesign().getSandcasters() > 0) && (hp.getSand() > 0))
				return true;
			if ((hp.getDesign().getBeamLasers() > 0) && CombatLogic.isFuel(ship) && CombatLogic.isPower(ship))
				return true;
			if ((hp.getDesign().getPulseLasers() > 0) && CombatLogic.isFuel(ship) && CombatLogic.isPower(ship))
				return true;
		}
		return false;
	}

	private void move()
	{
		if (mOtherSide.getShips().size() == 0)
			return;
		for (Ship2Instance ship : mSide.getShips())
		{
			int g = Ship2Logic.getG(ship)*100;
			Point3D delta;
			if (mTarget != null)
				delta = mTarget.getLocation().sub(ship.getLocation().add(ship.getVelocity()));
			else
				delta = ship.getVelocity().mult(-1);
			if (delta.mag() > g)
				delta.setMag(g);
			PhaseMovementLogic.setAcceleration(mCombat, ship, delta.x, delta.y, delta.z);			
		}
	}

	private void fire()
	{
		if (mTarget == null)
			return;
        for (Ship2Instance ship : PhaseLaserFireLogic.whoCanSetTarget(mCombat))
            if (PhaseLaserFireLogic.canSetTarget(mCombat, ship))
                PhaseLaserFireLogic.setTarget(mCombat, ship, mTarget);		
	}

	private void returnFire()
	{
        for (Ship2Instance ship : PhaseLaserFireLogic.whoCanSetTarget(mCombat))
            if (PhaseLaserFireLogic.canSetTarget(mCombat, ship))
                PhaseLaserFireLogic.setTarget(mCombat, ship, mTarget);      
	}

	private void ordanceLaunch()
	{
		if (mTarget == null)
			return;
        for (Ship2Instance ship : mSide.getShips())
        {
        	if (PhaseOrdnanceLaunchLogic.canSetTarget(mCombat, ship))
        		PhaseOrdnanceLaunchLogic.setTarget(mCombat, ship, mTarget);
        	if (PhaseOrdnanceLaunchLogic.canFireSand(mCombat, ship))
        		PhaseOrdnanceLaunchLogic.setFireSand(mCombat, ship);
            for (Ship2HardpointInstance hp : ship.getTurrets())
            {
            	if (hp.getTarget() != null)
            		continue;
            	if (hp.isFireSand())
            		continue;
            	if (PhaseOrdnanceLaunchLogic.canReload(mCombat, ship, hp))
            		PhaseOrdnanceLaunchLogic.setReload(mCombat, ship, hp);
            }
        }
	}

	private void computerReprogram()
	{
        for (Ship2Instance ship : mSide.getShips())
        {
			List<String> desired = new ArrayList<String>();
			Set<String> present = new HashSet<String>();
			present.addAll(ship.getLoadedPrograms());
			present.addAll(ship.getStoredPrograms());
			int space = Ship2Logic.getComputerLoadedCapacity(ship.getDesign());
			addForLasers(ship, present, desired, space - desired.size());
			addForMissiles(ship, present, desired, space - desired.size());
			addForSand(ship, present, desired, space - desired.size());
			addForLaserReturn(ship, present, desired, space - desired.size());
			addLasersBonus(ship, present, desired, space - desired.size());
			addLasersEvade(ship, present, desired, space - desired.size());
			addEvade(ship, present, desired, space - desired.size());
			for (String program : ship.getLoadedPrograms().toArray(new String[0]))
				if (!desired.contains(program))
					PhaseComputerReprogramLogic.saveProgram(mCombat, ship, program);
			for (String program : desired)
				if (!ship.getLoadedPrograms().contains(program))
					PhaseComputerReprogramLogic.loadProgram(mCombat, ship, program);
        }		
	}

	private void addForLasers(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
	}
	
	private void addForMissiles(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (space < 2)
			return;
		if (!present.contains(Ship2Instance.PROGRAM_LAUNCH) || !present.contains(Ship2Instance.PROGRAM_TARGET))
			return;
		if (!isAnyMissiles(ship))
			return;
		desired.add(Ship2Instance.PROGRAM_LAUNCH);
		desired.add(Ship2Instance.PROGRAM_TARGET);
	}
	
	private void addForSand(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (space < 1)
			return;
		if (!present.contains(Ship2Instance.PROGRAM_LAUNCH) || desired.contains(Ship2Instance.PROGRAM_LAUNCH))
			return;
		if (!isAnySand(ship))
			return;
		desired.add(Ship2Instance.PROGRAM_LAUNCH);
	}
	
	private void addForLaserReturn(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (!present.contains(Ship2Instance.PROGRAM_RETURN_FIRE) || !present.contains(Ship2Instance.PROGRAM_TARGET))
			return;
		if (desired.contains(Ship2Instance.PROGRAM_RETURN_FIRE) && desired.contains(Ship2Instance.PROGRAM_TARGET))
			return;
		int spaceNeeded = 2;
		if (desired.contains(Ship2Instance.PROGRAM_TARGET))
			spaceNeeded--;
		if (desired.contains(Ship2Instance.PROGRAM_RETURN_FIRE))
			spaceNeeded--;
		if (space < spaceNeeded)
			return;
		if (howManyLasers(ship) == 0)
			return;
		if (!desired.contains(Ship2Instance.PROGRAM_TARGET))
			desired.add(Ship2Instance.PROGRAM_TARGET);
		if (!desired.contains(Ship2Instance.PROGRAM_RETURN_FIRE))
			desired.add(Ship2Instance.PROGRAM_RETURN_FIRE);
	}
	
	private void addLasersBonus(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (space < 1)
			return;
		if (!present.contains(Ship2Instance.PROGRAM_GUNNER_INTERACT) || desired.contains(Ship2Instance.PROGRAM_GUNNER_INTERACT))
			return;
		int turrets = howManyLasers(ship);
		if (turrets == 0)
			return;
		desired.add(Ship2Instance.PROGRAM_GUNNER_INTERACT);
		space--;
		if (space < 1)
			return;
		int targets = howManyOtherLasers();
		int multi = Math.max(turrets, targets);
		if (multi < 2)
			return;
		if ((multi >= 4) && present.contains(Ship2Instance.PROGRAM_MULTI_TARGET_4) && !desired.contains(Ship2Instance.PROGRAM_MULTI_TARGET_4))
		{
			desired.add(Ship2Instance.PROGRAM_MULTI_TARGET_4);
			return;
		}
		if ((multi >= 3) && present.contains(Ship2Instance.PROGRAM_MULTI_TARGET_3) && !desired.contains(Ship2Instance.PROGRAM_MULTI_TARGET_3))
		{
			desired.add(Ship2Instance.PROGRAM_MULTI_TARGET_3);
			return;
		}
		if ((multi >= 2) && present.contains(Ship2Instance.PROGRAM_MULTI_TARGET_2) && !desired.contains(Ship2Instance.PROGRAM_MULTI_TARGET_2))
		{
			desired.add(Ship2Instance.PROGRAM_MULTI_TARGET_2);
			return;
		}
	}
	
	private void addLasersEvade(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (space < 1)
			return;
		if (!present.contains(Ship2Instance.PROGRAM_AUTO_EVADE) || desired.contains(Ship2Instance.PROGRAM_AUTO_EVADE))
			return;
		if (howManyOtherLasers() == 0)
			return;
		desired.add(Ship2Instance.PROGRAM_AUTO_EVADE);
	}
	
	private void addEvade(Ship2Instance ship, Set<String> present, List<String> desired, int space)
	{
		if (space < 1)
			return;
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_6) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_6))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_6);
			return;
		}
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_5) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_5))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_5);
			return;
		}
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_4) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_4))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_4);
			return;
		}
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_3) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_3))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_3);
			return;
		}
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_2) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_2))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_2);
			return;
		}
		if (present.contains(Ship2Instance.PROGRAM_MAN_EVA_1) && !desired.contains(Ship2Instance.PROGRAM_MAN_EVA_1))
		{
			desired.add(Ship2Instance.PROGRAM_MAN_EVA_1);
			return;
		}
	}
	
	private boolean isAnyMissiles(Ship2Instance ship)
	{
		for (Ship2HardpointInstance hp : ship.getTurrets())
		{
			if (hp.isDamage())
				continue;
			if (hp.getDesign().getMissiles() == 0)
				continue;
			if (hp.getMissiles() == 0)
				continue;
			return true;
		}
		return false;
	}
	
	private boolean isAnySand(Ship2Instance ship)
	{
		for (Ship2HardpointInstance hp : ship.getTurrets())
		{
			if (hp.isDamage())
				continue;
			if (hp.getDesign().getSandcasters() == 0)
				continue;
			if (hp.getSand() == 0)
				continue;
			return true;
		}
		return false;
	}
	
	private int howManyLasers(Ship2Instance ship)
	{
		if (!CombatLogic.isFuel(ship))
			return 0;
		if (!CombatLogic.isPower(ship))
			return 0;
		int count = 0;
		for (Ship2HardpointInstance hp : ship.getTurrets())
		{
			if (hp.isDamage())
				continue;
			if ((hp.getDesign().getBeamLasers() == 0) && (hp.getDesign().getPulseLasers() == 0))
				continue;
			if (hp.getMissiles() == 0)
				continue;
			count++;
		}
		return count;
	}
	
	private int howManyOtherLasers()
	{
		int count = 0;
		for (Ship2Instance ship : mOtherSide.getShips())
			if (howManyLasers(ship) != 0)
				count++;
		return count;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		if ("phase".equals(evt.getPropertyName()))
			takeTurn();
	}
}
