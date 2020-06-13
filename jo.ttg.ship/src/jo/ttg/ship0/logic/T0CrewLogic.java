package jo.ttg.ship0.logic;

import jo.ttg.ship0.beans.T0ShipDesign;

public class T0CrewLogic
{

    public static int neededPosition(T0ShipDesign ship, int p)
    {
        switch (p)
        {
            case T0ShipDesign.P_PILOT:
                return 1;
            case T0ShipDesign.P_NAV:
                return (ship.getSize() > 200) ? 1 : 0;
            case T0ShipDesign.P_ENGINEER:
                return (T0ShipLogic.jumpMass(ship) + T0ShipLogic.manMass(ship))/35;
            case T0ShipDesign.P_STEWARD:
                return ship.getCabins()/8;
            case T0ShipDesign.P_MEDIC:
                return ((ship.getSize() > 200) ? 1 : 0)
                    + (ship.getCabins() + ship.getBerths())/120;
            case T0ShipDesign.P_GUNNER:
                return 0;
        }
        return 0;
    }

}
