/*
 * Created on Feb 5, 2005
 *
 */
package jo.ttg.ship.logic.cbt;

import jo.ttg.logic.LocLogic;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;

/**
 * @author Jo
 *
 */
public class CombatShipLogic
{
    public static int getComputer(CombatShipBean ship)
    {
        int comp = ship.getShipStats().getComputerModel();
        comp -= ship.getDamageComputer();
        if (ship.isBridgeDestroyed())
            comp /= 2;
        return comp;
    }

    public static double getManeuver(CombatShipBean ship)
    {
        if (ship.getDamageFuel() >= 10)
            return 0;
        double man = ship.getShipStats().getManeuver();
        man -= ship.getDamageManeuver();
        return man;
    }
    
    public static double distToNearestEnemy(CombatShipBean ship)
    {
        double d = -1;
        for (CombatSideBean side : ship.getSide().getCombat().getSides())
        {
            if (side == ship.getSide())
                continue;
            for (CombatShipBean enemy : side.getShips())
            {
                if (enemy.getWeapons().size() == 0)
                    continue;
                double dist = LocLogic.dist(ship.getLocation(), enemy.getLocation());
                if ((d < 0) || (dist < d))
                    d = dist;
            }
        }
        if (d < 0)
            return 1000.0;
        else
            return d;
    }
    
    public static boolean hasFlown(CombatShipBean ship)
    {
        if (!ship.isFleeing())
            return false;
        if (distToNearestEnemy(ship) < 20.0)
            return false;
        return true;
    }
    
    public static boolean canFlee(CombatShipBean ship)
    {
        if (distToNearestEnemy(ship) < 20.0)
            return false;
        return true;
    }
    
    public static boolean canFlee(CombatSideBean side)
    {
        for (CombatShipBean ship : side.getShips())
            if (!canFlee(ship))
                return false; 
        return true;
    }
    
    public static void setFlee(CombatSideBean side, boolean fleeing)
    {
        for (CombatShipBean ship : side.getShips())
            ship.setFleeing(true);
    }
}
