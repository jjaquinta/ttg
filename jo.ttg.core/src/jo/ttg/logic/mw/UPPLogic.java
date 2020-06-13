/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.mw;


import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.mw.UPPBean;
import jo.ttg.utils.DisplayUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.StringUtils;

public class UPPLogic
{
    // trade code bitfields
    private final static int F_AG = (1 << (UPPBean.TC_AG));
    private final static int F_AS = (1 << (UPPBean.TC_AS));
    private final static int F_BA = (1 << (UPPBean.TC_BA));
    private final static int F_DE = (1 << (UPPBean.TC_DE));
    private final static int F_FL = (1 << (UPPBean.TC_FL));
    private final static int F_HI = (1 << (UPPBean.TC_HI));
    private final static int F_IC = (1 << (UPPBean.TC_IC));
    private final static int F_IN = (1 << (UPPBean.TC_IN));
    private final static int F_LO = (1 << (UPPBean.TC_LO));
    private final static int F_NA = (1 << (UPPBean.TC_NA));
    private final static int F_NI = (1 << (UPPBean.TC_NI));
    private final static int F_PO = (1 << (UPPBean.TC_PO));
    private final static int F_RI = (1 << (UPPBean.TC_RI));
    private final static int F_VA = (1 << (UPPBean.TC_VA));
    private final static int F_WA = (1 << (UPPBean.TC_WA));

    public static void updateTradeCodes(UPPBean upp)
    {
        long tradeCodes = 0;
        if ((upp.getAtmos().getValue() >= 4)
            && (upp.getAtmos().getValue() <= 9)
            && (upp.getHydro().getValue() >= 4)
            && (upp.getHydro().getValue() <= 8)
            && (upp.getPop().getValue() >= 5)
            && (upp.getPop().getValue() <= 7))
            tradeCodes |= F_AG;
        if ((upp.getSize().getValue() == 0) && (upp.getAtmos().getValue() == 0) && (upp.getHydro().getValue() == 0))
            tradeCodes |= F_AS;
        if ((upp.getPop().getValue() == 0) && (upp.getGov().getValue() == 0) && (upp.getLaw().getValue() == 0))
            tradeCodes |= F_BA;
        if ((upp.getAtmos().getValue() >= 2) && (upp.getHydro().getValue() == 0))
            tradeCodes |= F_DE;
        if ((upp.getAtmos().getValue() >= 10) && (upp.getHydro().getValue() >= 1))
            tradeCodes |= F_FL;
        if (upp.getPop().getValue() >= 9)
            tradeCodes |= F_HI;
        if ((upp.getAtmos().getValue() <= 1) && (upp.getHydro().getValue() >= 1))
            tradeCodes |= F_IC;
        if (((upp.getAtmos().getValue() == 2)
            || (upp.getAtmos().getValue() == 4)
            || (upp.getAtmos().getValue() == 7)
            || (upp.getAtmos().getValue() == 9))
            && (upp.getPop().getValue() >= 9))
            tradeCodes |= F_IN;
        if (upp.getPop().getValue() <= 4)
            tradeCodes |= F_LO;
        if ((upp.getAtmos().getValue() <= 3) && (upp.getHydro().getValue() <= 3) && (upp.getPop().getValue() >= 6))
            tradeCodes |= F_NA;
        if (upp.getPop().getValue() <= 6)
            tradeCodes |= F_NI;
        if ((upp.getAtmos().getValue() >= 3) && (upp.getAtmos().getValue() <= 5) && (upp.getHydro().getValue() <= 3))
            tradeCodes |= F_PO;
        if (((upp.getAtmos().getValue() == 6) || (upp.getAtmos().getValue() == 8))
            && (upp.getPop().getValue() >= 6)
            && (upp.getPop().getValue() <= 8)
            && (upp.getGov().getValue() >= 4)
            && (upp.getGov().getValue() <= 9))
            tradeCodes |= F_RI;
        if ((upp.getAtmos().getValue() == 0) && ((tradeCodes & F_AS) != 0))
            tradeCodes |= F_VA;
        if (upp.getHydro().getValue() >= 10)
            tradeCodes |= F_WA;
        upp.setTradeCodes(tradeCodes);
    }

    public static String getTradeCodeDesc(UPPBean upp, int n)
    {
        if ((upp != null) && !upp.isTradeCode(n))
            return "";
        return getTradeCodeDesc(n);
    }

    public static String getTradeCodeDesc(int n)
    {
        switch (n)
        {
            case UPPBean.TC_AG :
                return "Ag";
            case UPPBean.TC_AS :
                return "As";
            case UPPBean.TC_BA :
                return "Ba";
            case UPPBean.TC_DE :
                return "De";
            case UPPBean.TC_FL :
                return "Fl";
            case UPPBean.TC_HI :
                return "Hi";
            case UPPBean.TC_IC :
                return "Ic";
            case UPPBean.TC_IN :
                return "In";
            case UPPBean.TC_LO :
                return "Lo";
            case UPPBean.TC_NA :
                return "Na";
            case UPPBean.TC_NI :
                return "Ni";
            case UPPBean.TC_PO :
                return "Po";
            case UPPBean.TC_RI :
                return "Ri";
            case UPPBean.TC_VA :
                return "Va";
            case UPPBean.TC_WA :
                return "Wa";
        }
        return "";
    }

    public static String getTradeCodeDescLong(UPPBean upp, int n)
    {
        if ((upp != null) && !upp.isTradeCode(n))
            return "";
        switch (n)
        {
            case UPPBean.TC_AG :
                return "Agricultural";
            case UPPBean.TC_AS :
                return "Asteroid";
            case UPPBean.TC_BA :
                return "Barren";
            case UPPBean.TC_DE :
                return "Desert";
            case UPPBean.TC_FL :
                return "Fluid oceans";
            case UPPBean.TC_HI :
                return "Hi pop";
            case UPPBean.TC_IC :
                return "Ice capped";
            case UPPBean.TC_IN :
                return "Industrial";
            case UPPBean.TC_LO :
                return "Low pop";
            case UPPBean.TC_NA :
                return "Non-agricultural";
            case UPPBean.TC_NI :
                return "Non-industrial";
            case UPPBean.TC_PO :
                return "Poor";
            case UPPBean.TC_RI :
                return "Rich";
            case UPPBean.TC_VA :
                return "Vaccuum";
            case UPPBean.TC_WA :
                return "Water world";
        }
        return "";
    }
    public static String getTradeCodesDesc(UPPBean upp)
    {
        StringBuffer tcbuf = new StringBuffer();

        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_AG));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_AS));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_BA));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_DE));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_FL));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_HI));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_IC));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_IN));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_LO));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_NA));
        if (!upp.isLo()) // Lo implies Ni
            tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_NI));
        if (!upp.isBa()) // Ba implies Po
            tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_PO));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_RI));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_VA));
        tcbuf.append(getTradeCodeDesc(upp, UPPBean.TC_WA));
        return tcbuf.toString();
    }
    public static String getTradeCodesDescLong(UPPBean upp)
    {
        List<String> codes = new ArrayList<String>();
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_AG));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_AS));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_BA));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_DE));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_FL));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_HI));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_IC));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_IN));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_LO));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_NA));
        if (!upp.isLo()) // Lo implies Ni
            codes.add(getTradeCodeDescLong(upp, UPPBean.TC_NI));
        if (!upp.isBa()) // Ba implies Po
            codes.add(getTradeCodeDescLong(upp, UPPBean.TC_PO));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_RI));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_VA));
        codes.add(getTradeCodeDescLong(upp, UPPBean.TC_WA));
        StringBuffer tcbuf = new StringBuffer();
        for (String code : codes)
            if (!StringUtils.isTrivial(code))
            {
                if (tcbuf.length() > 0)
                    tcbuf.append(", ");
                tcbuf.append(code);
            }
        return tcbuf.toString();
    }
    public static String getUPPDesc(UPPBean upp)
    {
        StringBuffer ret = new StringBuffer();
        ret.append((char)       (upp.getPort().getValue()));
        ret.append(DisplayUtils.int2upp(upp.getSize() .getValue()));
        ret.append(DisplayUtils.int2upp(upp.getAtmos().getValue()));
        ret.append(DisplayUtils.int2upp(upp.getHydro().getValue()));
        ret.append(DisplayUtils.int2upp(upp.getPop()  .getValue()));
        ret.append(DisplayUtils.int2upp(upp.getGov()  .getValue()));
        ret.append(DisplayUtils.int2upp(upp.getLaw()  .getValue()));
        ret.append('-');
        ret.append(DisplayUtils.int2upp(upp.getTech() .getValue()));
        return ret.toString();
    }

    public static double getTradeCostMod(UPPBean upp)
    {
        double ret;

        ret = 4000.0;
        if (upp.isAg())
            ret -= 1000.0;
        if (upp.isAs())
            ret -= 1000.0;
        if (upp.isBa())
            ret += 1000.0;
        if (upp.isDe())
            ret += 1000.0;
        if (upp.isFl())
            ret += 1000.0;
        if (upp.isHi())
            ret -= 1000.0;
        if (upp.isIn())
            ret -= 1000.0;
        if (upp.isLo())
            ret += 1000.0;
        if (upp.isNa())
            ret += 1000.0;
        if (upp.isNi())
            ret += 1000.0;
        if (upp.isPo())
            ret -= 1000.0;
        if (upp.isRi())
            ret += 1000.0;
        if (upp.isVa())
            ret += 1000.0;
        return ret / 4000.0;
    }
    public static double getTradeMod(UPPBean from, UPPBean to)
    {
        double n, d;

        n = getTradePriceMod(from, to) * getXRate(to);
        d = getTradeCostMod(from) * getXRate(from);
        if (DoubleUtils.equals(d, 0.0))
            return 0;
        return n / d;
    }
    public static double getTradePriceMod(UPPBean from, UPPBean to)
    {
        int c;

        if (to.isBa())
        {
            return 1.0;
        }
        c = 4;
        if (to.isAg())
        {
            if (from.isAg())
                c++;
            if (from.isBa())
                c++;
            if (from.isIn())
                c++;
            if (from.isRi())
                c++;
        }
        if (to.isAs())
        {
            if (from.isAg())
                c++;
            if (from.isAs())
                c++;
            if (from.isIn())
                c++;
            if (from.isNa())
                c++;
            if (from.isVa())
                c++;
        }
        if (to.isDe())
        {
            if (from.isAg())
                c++;
            if (from.isDe())
                c++;
            if (from.isIn())
                c++;
            if (from.isNa())
                c++;
            if (from.isRi())
                c++;
        }
        if (to.isFl())
        {
            if (from.isFl())
                c++;
            if (from.isIn())
                c++;
        }
        if (to.isHi())
        {
            if (from.isAg())
                c++;
            if (from.isHi())
                c++;
            if (from.isIn())
                c++;
            if (from.isRi())
                c++;
        }
        if (to.isIn())
        {
            if (from.isAg())
                c++;
            if (from.isAs())
                c++;
            if (from.isBa())
                c++;
            if (from.isFl())
                c++;
            if (from.isIc())
                c++;
            if (from.isIn())
                c++;
            if (from.isLo())
                c++;
            if (from.isNi())
                c++;
            if (from.isRi())
                c++;
            if (from.isVa())
                c++;
            if (from.isWa())
                c++;
        }
        if (to.isLo())
        {
            if (from.isAg())
                c++;
            if (from.isHi())
                c++;
        }
        if (to.isNa())
        {
            if (from.isAg())
                c++;
            if (from.isAs())
                c++;
            if (from.isDe())
                c++;
        }
        if (to.isNi())
        {
            if (from.isIn())
                c++;
            if (from.isNi())
                c--;
        }
        if (to.isPo())
        {
            if (from.isIn())
                c++;
            if (from.isPo())
                c--;
        }
        if (to.isRi())
        {
            if (from.isAg())
                c++;
            if (from.isAs())
                c++;
            if (from.isHi())
                c++;
            if (from.isIn())
                c++;
            if (from.isLo())
                c++;
            if (from.isRi())
                c++;
            if (from.isWa())
                c++;
        }
        if (to.isVa())
        {
            if (from.isAs())
                c++;
            if (from.isIn())
                c++;
            if (from.isNa())
                c++;
            if (from.isVa())
                c++;
        }
        if (to.isWa())
        {
            if (from.isIn())
                c++;
            if (from.isWa())
                c++;
        }
        return (double) c * 1000.0 / 4000.0;
    }
    public static int[] getDestinationPreferences(UPPBean from)
    {
        int[] ret = new int[UPPBean.TRADE_CODES.length];
        if (from.isAg())
            ret[UPPBean.TC_AG]++;
        if (from.isBa())
            ret[UPPBean.TC_AG]++;
        if (from.isIn())
            ret[UPPBean.TC_AG]++;
        if (from.isRi())
            ret[UPPBean.TC_AG]++;
        if (from.isAg())
            ret[UPPBean.TC_AS]++;
        if (from.isAs())
            ret[UPPBean.TC_AS]++;
        if (from.isIn())
            ret[UPPBean.TC_AS]++;
        if (from.isNa())
            ret[UPPBean.TC_AS]++;
        if (from.isVa())
            ret[UPPBean.TC_AS]++;
        if (from.isAg())
            ret[UPPBean.TC_DE]++;
        if (from.isDe())
            ret[UPPBean.TC_DE]++;
        if (from.isIn())
            ret[UPPBean.TC_DE]++;
        if (from.isNa())
            ret[UPPBean.TC_DE]++;
        if (from.isRi())
            ret[UPPBean.TC_DE]++;
        if (from.isFl())
            ret[UPPBean.TC_FL]++;
        if (from.isIn())
            ret[UPPBean.TC_FL]++;
        if (from.isAg())
            ret[UPPBean.TC_HI]++;
        if (from.isHi())
            ret[UPPBean.TC_HI]++;
        if (from.isIn())
            ret[UPPBean.TC_HI]++;
        if (from.isRi())
            ret[UPPBean.TC_HI]++;
        if (from.isAg())
            ret[UPPBean.TC_IN]++;
        if (from.isAs())
            ret[UPPBean.TC_IN]++;
        if (from.isBa())
            ret[UPPBean.TC_IN]++;
        if (from.isFl())
            ret[UPPBean.TC_IN]++;
        if (from.isIc())
            ret[UPPBean.TC_IN]++;
        if (from.isIn())
            ret[UPPBean.TC_IN]++;
        if (from.isLo())
            ret[UPPBean.TC_IN]++;
        if (from.isNi())
            ret[UPPBean.TC_IN]++;
        if (from.isRi())
            ret[UPPBean.TC_IN]++;
        if (from.isVa())
            ret[UPPBean.TC_IN]++;
        if (from.isWa())
            ret[UPPBean.TC_IN]++;
        if (from.isAg())
            ret[UPPBean.TC_LO]++;
        if (from.isHi())
            ret[UPPBean.TC_LO]++;
        if (from.isAg())
            ret[UPPBean.TC_NA]++;
        if (from.isAs())
            ret[UPPBean.TC_NA]++;
        if (from.isDe())
            ret[UPPBean.TC_NA]++;
        if (from.isIn())
            ret[UPPBean.TC_NI]++;
        if (from.isNi())
            ret[UPPBean.TC_NI]--;
        if (from.isIn())
            ret[UPPBean.TC_PO]++;
        if (from.isPo())
            ret[UPPBean.TC_PO]--;
        if (from.isAg())
            ret[UPPBean.TC_RI]++;
        if (from.isAs())
            ret[UPPBean.TC_RI]++;
        if (from.isHi())
            ret[UPPBean.TC_RI]++;
        if (from.isIn())
            ret[UPPBean.TC_RI]++;
        if (from.isLo())
            ret[UPPBean.TC_RI]++;
        if (from.isRi())
            ret[UPPBean.TC_RI]++;
        if (from.isWa())
            ret[UPPBean.TC_RI]++;
        if (from.isAs())
            ret[UPPBean.TC_VA]++;
        if (from.isIn())
            ret[UPPBean.TC_VA]++;
        if (from.isNa())
            ret[UPPBean.TC_VA]++;
        if (from.isVa())
            ret[UPPBean.TC_VA]++;
        if (from.isIn())
            ret[UPPBean.TC_WA]++;
        if (from.isWa())
            ret[UPPBean.TC_WA]++;
        return ret;
    }
    public static double getXRate(UPPBean upp) // # of imp credits in on local credit
    {
        int t;

        t = upp.getTech().getValue();
        switch (upp.getPort().getValue())
        {
            case 'A' :
                return 0.25 + 0.05 * t;
            case 'B' :
                return 0.20 + 0.05 * t;
            case 'C' :
            case 'F' :
                if (t >= 5)
                    return 0.15 + 0.05 * t;
                return -0.1 + 0.1 * t;
            case 'D' :
            case 'G' :
                if (t >= 5)
                    return 0.10 + 0.05 * t;
                if (t == 4)
                    return 0.25;
                if (t == 3)
                    return 0.15;
                if (t == 2)
                    return 0.05;
                if (t == 1)
                    return 0.01;
                return 0.0;
            case 'E' :
            case 'H' :
                if (t >= 5)
                    return 0.05 + 0.05 * t;
                if (t == 4)
                    return 0.20;
                if (t == 3)
                    return 0.05;
                return 0.0;
            default :
                if (t >= 7)
                    return -0.05 + 0.05 * t;
                if (t == 6)
                    return 0.20;
                if (t == 5)
                    return 0.10;
                return 0.0;
        }
    }

    public static int getResourceMod(UPPBean upp)
    {
        int mod = 0;
        if (upp.isAg())
            mod++;
        if (upp.isDe())
            mod--;
        if (upp.isFl())
            mod--;
        if (upp.isHi())
            mod++;
        if (upp.isIc())
            mod--;
        if (upp.isIn())
            mod += 2;
        if (upp.isNa())
            mod--;
        if (upp.isRi())
            mod++;
        if (upp.isVa())
            mod--;
        if (upp.getPort().getValue() == 'A')
            mod += 2;
        else if (upp.getPort().getValue() == 'B')
            mod += 1;
        return mod;
    }

    public static int getInfrastructureMod(UPPBean upp)
    {
        int mod = 0;
        if (upp.isAs())
            mod--;
        if (upp.isHi())
            mod++;
        if (upp.isIn())
            mod += 2;
        if (upp.isLo())
            mod--;
        if (upp.isNi())
            mod--;
        if (upp.isPo())
            mod -= 2;
        if (upp.isRi())
            mod += 2;
        if (upp.isWa())
            mod--;
        if (upp.getPort().getValue() == 'A')
            mod += 4;
        else if (upp.getPort().getValue() == 'B')
            mod += 3;
        else if (upp.getPort().getValue() == 'C')
            mod += 2;
        else if (upp.getPort().getValue() == 'D')
            mod += 1;
        return mod;
    }

    public static int cultureMod(UPPBean upp)
    {
        int mod = 0;
        if (upp.isAg())
            mod--;
        if (upp.isAs())
            mod++;
        if (upp.isDe())
            mod++;
        if (upp.isFl())
            mod++;
        if (upp.isIc())
            mod++;
        if (upp.isNa())
            mod--;
        if (upp.isNi())
            mod--;
        if (upp.isPo())
            mod++;
        if (upp.isRi())
            mod++;
        if (upp.isVa() && !upp.isAs())
            mod++;
        return mod;
    }

    public static double convLocalToImperial(UPPBean upp, double amnt)
    {
        return amnt*getXRate(upp);
    }
    public static double convImperialToLocal(UPPBean upp, double amnt)
    {
        return amnt/getXRate(upp);
    }

    public static int parseTradeCode(String tc)
    {
        if (tc.equalsIgnoreCase("Ag"))
            return UPPBean.TC_AG;
        if (tc.equalsIgnoreCase("As"))
            return UPPBean.TC_AS;
        if (tc.equalsIgnoreCase("Ba"))
            return UPPBean.TC_BA;
        if (tc.equalsIgnoreCase("De"))
            return UPPBean.TC_DE;
        if (tc.equalsIgnoreCase("Fl"))
            return UPPBean.TC_FL;
        if (tc.equalsIgnoreCase("Hi"))
            return UPPBean.TC_HI;
        if (tc.equalsIgnoreCase("Ic"))
            return UPPBean.TC_IC;
        if (tc.equalsIgnoreCase("In"))
            return UPPBean.TC_IN;
        if (tc.equalsIgnoreCase("Lo"))
            return UPPBean.TC_LO;
        if (tc.equalsIgnoreCase("Na"))
            return UPPBean.TC_NA;
        if (tc.equalsIgnoreCase("Ni"))
            return UPPBean.TC_NI;
        if (tc.equalsIgnoreCase("Po"))
            return UPPBean.TC_PO;
        if (tc.equalsIgnoreCase("Ri"))
            return UPPBean.TC_RI;
        if (tc.equalsIgnoreCase("Va"))
            return UPPBean.TC_VA;
        if (tc.equalsIgnoreCase("Wa"))
            return UPPBean.TC_WA;
        return 0;
    }

}
