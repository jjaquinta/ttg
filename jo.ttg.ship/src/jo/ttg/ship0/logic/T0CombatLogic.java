package jo.ttg.ship0.logic;

import jo.ttg.beans.RandBean;
import jo.ttg.logic.RandLogic;
import jo.ttg.ship0.beans.T0ShipInstance;

public class T0CombatLogic
{
    public static String resolveHits(T0ShipInstance ship, RandBean r, int hits)
    {
        if (hits <= 0)
            return null;
        StringBuffer sb = new StringBuffer();
        while (hits-- > 0)
            switch (RandLogic.D(r, 2))
            {
                case 2:
                    if (ship.getPowNow() >= 'A')
                    {
                        ship.setPowNow(decrement(ship.getPowNow()));
                        sb.append("Power plant hit.\n");
                    }
                    break;
                case 3:
                    if (ship.getManNow() >= 'A')
                    {
                        ship.setManNow(decrement(ship.getManNow()));
                        sb.append("Maneuver drive hit.\n");
                    }
                    break;
                case 4:
                    if (ship.getJumpNow() >= 'A')
                    {
                        ship.setJumpNow(decrement(ship.getJumpNow()));
                        sb.append("Jump drive hit.\n");
                    }
                    break;
                case 5:
                    sb.append("Fuel hit.\n");
                    ship.setFuelHits(ship.getFuelHits()+1);
                    break;
                case 6:
                case 7:
                    sb.append("Hull hit.\n");
                    ship.setHullHits(ship.getHullHits()+1);
                    break;
                case 8:
                    ship.setHoldHits(ship.getHoldHits()+1);
                    sb.append("Hold hit.\n");
                    break;
                case 9:
                    ship.setCompHits(ship.getCompHits()+1);
                    sb.append("Computer hit.\n");
                    break;
                case 10:
                case 11:
                    int which = RandLogic.nextInt(r, ship.getTurretSize().length);
                    ship.getTurretDamage()[which] = true;
                    sb.append("Turret "+(which+1)+" hit.");
                    break;
                case 12:    // critical hit
                    switch (RandLogic.D(r, 1))
                    {
                        case 1:
                            if (ship.getPowNow() >= 'A')
                            {
                                ship.setPowNow(' ');
                                sb.append("Power plant destroyed!\n");
                            }
                            break;
                        case 2:
                            if (ship.getManNow() >= 'A')
                            {
                                ship.setManNow(' ');
                                sb.append("Maneuver drive destroyed!\n");
                            }
                            break;
                        case 3:
                            if (ship.getJumpNow() >= 'A')
                            {
                                ship.setJumpNow(' ');
                                sb.append("Jump drive destroyed!\n");
                            }
                            break;
                        case 4:
                            ship.setCrewHits(ship.getCrewHits()+1);
                            sb.append("Crew killed!\n");
                            break;
                        case 5:
                            ship.setCompHits(ship.getCompHits() + 1000);
                            sb.append("Computer destroyed!\n");
                            break;
                        case 6:
                            sb.append("Ship explodes!\n");
                            break;
                    }
                    break;
            }
        return sb.toString();
    }

    private static char decrement(char val)
    {
        val--;
        if (val == 'O')
            val--;
        else if (val == 'I')
            val--;
        return val;
    }
}
