package jo.ttg.ship0.logic;

import jo.ttg.beans.RandBean;
import jo.ttg.logic.RandLogic;
import jo.ttg.ship0.beans.T0ShipDesign;

public class T0GenerateLogic
{
    public static T0ShipDesign generateShip(String template)
    {
        T0ShipDesign design = new T0ShipDesign();
        design.fromString(template);
        return design;
    }
    /**
    mode = chance in 6 to have hardpoint occupied
    */
   public static void generateHardpoints(T0ShipDesign ship, RandBean r, int mode)
   {
       for (int h = 0; h < ship.getTurretSize().length; h++)
       {
           if (RandLogic.D(r, 1) > mode)
               continue;
           if (RandLogic.D(r, 1) <= mode)
           {
               if (RandLogic.D(r, 1) <= mode)
                   ship.getTurretSize()[h] = (3);
               else
                   ship.getTurretSize()[h] = (2);
           }
           else
               ship.getTurretSize()[h] = (1);
           int roll = RandLogic.D(r, 1);
           if (roll <= 2)
               ship.getMissiles()[h] = (ship.getTurretSize()[h]);
           else if (roll == 3)
               ship.getBeamLasers()[h] = (ship.getTurretSize()[h]);
           else if (roll == 4)
               ship.getSandcasters()[h] = (ship.getTurretSize()[h]);
           else
               ship.getPulseLasers()[h] = (ship.getTurretSize()[h]);
       }
   }

}
