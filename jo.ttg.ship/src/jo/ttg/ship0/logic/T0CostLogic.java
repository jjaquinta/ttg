package jo.ttg.ship0.logic;

import jo.ttg.ship0.beans.T0ShipDesign;

public class T0CostLogic
{
    public static double calculateCost(T0ShipDesign ship)
    {
        double ret = 0;
        ret += hullPrice(ship.getSize()); // hull
        ret += ship.getCabins()*500000.0;
        ret += ship.getBerths()*50000.0;
        ret += jumpPrice(ship);
        ret += powPrice(ship);
        ret += manPrice(ship);
        ret += compPrice(ship);
        for (int i = 0; i < ship.getTurretSize().length; i++)
            ret += hardpointPrice(ship, i);
        return ret;
    }
    public static int hardpointPrice(T0ShipDesign ship, int hardpoint)
    {
        int ret = 0;
        if (ship.getTurretSize()[hardpoint] != 0)
        {
            ret += 100000;
            int o = ship.getTurretSize()[hardpoint];
            if (o == 1)
                ret += 200000;
            else if (o == 2)
                ret += 500000;
            else if (o == 3)
                ret += 1000000;
            ret += 750000*ship.getMissiles()[hardpoint];
            ret += 250000*ship.getSandcasters()[hardpoint];
            ret += 500000*ship.getBeamLasers()[hardpoint];
            ret += 1000000*ship.getPulseLasers()[hardpoint];
        }
        return ret;
    }
    public static double jumpPrice(T0ShipDesign ship)
    {
        return T0ShipLogic.jumpPrice(ship.getJumpType());
    }

    public static double manPrice(T0ShipDesign ship)
    {
        return manPrice(ship.getManType());
    }

   public static double powPrice(T0ShipDesign ship)
    {
        return powPrice(ship.getPowType());
    }
   public static double compPrice(T0ShipDesign ship)
   {
       return compPrice(ship.getComp());
   }
   public static double manPrice(char Let)
   {
       int off = T0ShipLogic.letOff(Let);
       if (off < 0)
           return 0;
       return (4+4*off)*1000000.0;
   }
   public static double powPrice(char Let)
   {
       int off = T0ShipLogic.letOff(Let);
       if (off < 0)
           return 0;
       return (1+off)*8000000.0;
   }
   public static double compPrice(int comp)
   {
       switch (comp)
       {
           case T0ShipDesign.S_COMP1   : return  2000000.0;
           case T0ShipDesign.S_COMP1BIS: return  4000000.0;
           case T0ShipDesign.S_COMP2   : return  9000000.0;
           case T0ShipDesign.S_COMP2BIS: return 18000000.0;
           case T0ShipDesign.S_COMP3   : return 18000000.0;
           case T0ShipDesign.S_COMP4   : return 30000000.0;
           case T0ShipDesign.S_COMP5   : return 45000000.0;
           case T0ShipDesign.S_COMP6   : return 55000000.0;
           case T0ShipDesign.S_COMP7   : return 80000000.0;
       }
       return 0.0;
   }
   public static double hullPrice(int hull)
   {
       switch (hull)
       {
           case  100: return   2000000.0;
           case  200: return   8000000.0;
           case  400: return  16000000.0;
           case  600: return  48000000.0;
           case  800: return  80000000.0;
           case 1000: return 100000000.0;
       }
       return hull*100000.0;
   }

}
