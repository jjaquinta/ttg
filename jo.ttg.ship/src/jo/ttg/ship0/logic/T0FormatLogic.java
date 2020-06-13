package jo.ttg.ship0.logic;

import jo.ttg.ship0.beans.T0ShipDesign;
import jo.ttg.ship0.beans.T0ShipInstance;
import jo.ttg.utils.DisplayUtils;

public class T0FormatLogic
{

    // string functions
    public static String sHoldSize(T0ShipDesign ship)
    {
        return DisplayUtils.formatTons(ship.getHoldSize());
    }

    public static String sCabins(T0ShipDesign ship)
    {
        return Integer.toString(ship.getCabins());
    }

    public static String sBerths(T0ShipDesign ship)
    {
        return Integer.toString(ship.getBerths());
    }

    public static String sFuelSize(T0ShipDesign ship)
    {
        return DisplayUtils.formatTons(ship.getFuelSize());
    }

    public static String sSize(T0ShipDesign ship)
    {
        return DisplayUtils.formatTons(ship.getSize());
    }

    private static String sCalculateNumber(T0ShipDesign ship, char i)
    {
        return String.valueOf(i) + "(" + Double.toString(T0ShipLogic.calculateNumber(ship, i))
                + ")";
    }

    public static String sJumpDrive(T0ShipDesign ship)
    {
        return sCalculateNumber(ship, ship.getJumpType());
    }

    public static String sManDrive(T0ShipDesign ship)
    {
        return sCalculateNumber(ship, ship.getManType());
    }

    public static String sPowPlant(T0ShipDesign ship)
    {
        return sCalculateNumber(ship, ship.getPowType());
    }

    public static String sHardpoint(T0ShipDesign ship, int n)
    {
        if ((n < 0) || (n >= ship.getTurretSize().length))
            return "";
        StringBuffer sb = new StringBuffer();
        int o;
        if ((ship.getTurretSize()[n]) != 0)
        {
            sb.append("H ");
            o = (ship.getTurretSize()[n]);
            if (o == 1)
                sb.append("T1 ");
            else if (o == 2)
                sb.append("T2 ");
            else if (o == 3)
                sb.append("T3 ");
            if (o != 0)
            {
                o = (ship.getMissiles()[n]);
                if (o == 1)
                    sb.append("M");
                else if (o == 2)
                    sb.append("MM");
                else if (o == 3)
                    sb.append("MMM");
                o = (ship.getSandcasters()[n]);
                if (o == 1)
                    sb.append("S");
                else if (o == 2)
                    sb.append("SS");
                else if (o == 3)
                    sb.append("SSS");
                o = (ship.getPulseLasers()[n]);
                if (o == 1)
                    sb.append("P");
                else if (o == 2)
                    sb.append("PP");
                else if (o == 3)
                    sb.append("PPP");
                o = (ship.getBeamLasers()[n]);
                if (o == 1)
                    sb.append("B");
                else if (o == 2)
                    sb.append("BB");
                else if (o == 3)
                    sb.append("BBB");
            }
        }
        return sb.toString();
    }

    // string functions
    public static String sJumpNow(T0ShipInstance ship) { return sCalculateNumber(ship, ship.getJumpNow()); }
    public static String sManNow(T0ShipInstance ship) { return sCalculateNumber(ship, ship.getManNow()); }
    public static String sPowNow(T0ShipInstance ship) { return sCalculateNumber(ship, ship.getPowNow()); }

}
