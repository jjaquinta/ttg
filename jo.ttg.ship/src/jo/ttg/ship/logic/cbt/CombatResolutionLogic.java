package jo.ttg.ship.logic.cbt;

import jo.ttg.ship.beans.cbt.CombatBean;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;

public class CombatResolutionLogic
{
    public static boolean activeParticipant(CombatShipBean ship)
    {
        if (CombatShipLogic.hasFlown(ship))
            return false;
        if (ship.getDamageFuel() >= 10)
            return false;
        if (ship.getDamagePowerPlant() >= 10)
            return false;
        if (ship.getDamageManeuver() < CombatShipLogic.getManeuver(ship))
            return true;
        if (ship.getWeapons().size() > 0)
            return true;
        return false;
    }
    
    public static boolean activeParticipant(CombatSideBean side)
    {
        for (CombatShipBean ship : side.getShips())
            if (activeParticipant(ship))
                return true;
        return false;
    }
    
    public static boolean activeParticipant(CombatBean combat)
    {
        for (CombatSideBean side : combat.getSides())
            if (activeParticipant(side))
                return true;
        return true;
    }
    
    public static CombatSideBean getWinningSide(CombatBean combat)
    {
        CombatSideBean winner = null;
        for (CombatSideBean side : combat.getSides())
            if (activeParticipant(side))
            {
                if (winner != null)
                    return null;
                winner = side;
            }
        return winner;
    }
    
    public static boolean hasFlown(CombatSideBean side)
    {
        for (CombatShipBean ship : side.getShips())
            if (!CombatShipLogic.hasFlown(ship))
                return false;
        return true;
    }
}
