/*
 * Created on Sep 25, 2005
 *
 */
package jo.ttg.utils;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.LocationURI;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.LocationURILogic;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.utils.FormatUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class DisplayUtils extends FormatUtils
{

    /**
     * Format number as distance.
     * @param Dist in AU
     * @return String
     */
    public static String formatDistance(double dist)
    {
        if (dist < 0)
            return "-"+formatDistance(-dist);
        if (dist >= 173.144633)
            return DoubleUtils.format(dist/173.144633, -1, 1) + "Light Days";
        if (dist >= 10)
            return DoubleUtils.format(dist/7.21435969, -1, 1) + "Light Hours";
        if (dist >= 0.2)
            return DoubleUtils.format(dist, -1, 1) + "AU";
        dist = ConvUtils.convAUToKm(dist);
        if (dist >= 200000.0)
            return DoubleUtils.format(dist / 1000000.0, -1, 1) + "MKm";
        if (dist > .5)
            return DoubleUtils.format(dist, -1, 0) + "Km";
        return DoubleUtils.format(dist * 1000.0, -1, 0) + "m";
    }


    public static String formatPower(double d)
    {
        String ret = String.valueOf(d);
        int o = ret.indexOf(".");
        if (o < 0)
            ret += ".000";
        else if (ret.length() - o > 4)
            ret = ret.substring(0, o+4);
        else if (ret.length() - o == 3)
            ret += "0";
        else if (ret.length() - o == 2)
            ret += "00";
        else if (ret.length() - o == 1)
            ret += "000";
        return ret+"MW";
    }

    /**
     * Format number as weight.
     * @param d mass in Solar Masses
     * @return String
     */
    public static String formatWeight(double d)
    {
        if (d >= .5)
            return DoubleUtils.format(d, -1, 3)+" SM";
        double kg = ConvUtils.convSMToKg(d);
        double jm = ConvUtils.convKGToJM(kg);
        if (jm >= .5)
            return DoubleUtils.format(jm, -1, 3)+" JM";
        double em = ConvUtils.convKGToJM(kg);
        if (em >= .5)
            return DoubleUtils.format(em, -1, 3)+" EM";
        double lm = ConvUtils.convKGToJM(kg);
        if (lm >= .5)
            return DoubleUtils.format(lm, -1, 3)+" LM";
        if (kg > 500)
            return DoubleUtils.format(ConvUtils.convKgToMT(kg), -1, 3)+" mt";
        return DoubleUtils.format(kg, -1, 3)+" kg";
    }

    /**
     * Format number as tons.
     * @param t
     * @return String
     */
    public static String formatTons(double t)
    {
        return String.valueOf((int)Math.floor(t+.5)) + "t";
    }

    /**
     * @param needed
     * @return
     */
    public static String formatVolume(double vol)
    {
        if (vol > 13.5)
            return formatTons((int)(vol/13.5));
        if (vol > 1.0)
            return DoubleUtils.format(vol, 8, 1).trim()+"kl";
        return DoubleUtils.format(vol*1000, 8, 1).trim()+"l";
    }

    public static String formatElapsedTime(DateBean date)
    {
        if (date == null)
            return "";
        StringBuffer ret = new StringBuffer();
        if (date.getYear() > 0)
            ret.append(date.getYear()+"y ");
        if (date.getDay() > 0)
            ret.append(date.getDay()+"d ");
        if ((date.getHour() > 0) || (date.getMinute() > 0))
            ret.append(formatTime(date));
        return ret.toString();
    }

    public static String formatDateTime(DateBean date)
    {
        if (date == null)
            return "";
        return formatDate(date)+" "+formatTime(date);
    }

    public static String formatDate(DateBean date)
    {
        if (date == null)
            return "";
        return IntegerUtils.format(date.getYear(), -4)+"-"+
        IntegerUtils.format(date.getDay(), -3);
    }

    public static String formatTime(DateBean date)
    {
        if (date == null)
            return "";
        return IntegerUtils.format(date.getHour(), -2)+":"+
        FormatUtils.prefix(String.valueOf(date.getMinute()), 2, "0");
    }

    public static String formatTime(int minutes)
    {
        return formatTime(new DateBean(minutes));
    }

    public static String formatAngle(double d)
    {
        String ret = String.valueOf(d);
        int o = ret.indexOf(".");
        if (o < 0)
            ret += ".00";
        else if (ret.length() - o > 3)
            ret = ret.substring(0, o+3);
        else if (ret.length() - o == 2)
            ret += "0";
        else if (ret.length() - o == 1)
            ret += "00";
        ret += "\u00b0";
        return ret;
    }
    
    public static String formatPopulation(double pop)
    {
        StringBuffer ret = new StringBuffer();
        while (pop >= 1000)
        {
            double remainder = pop - Math.floor(pop/1000)*1000;
            ret.insert(0, ","+FormatUtils.prefix(String.valueOf((long)remainder%1000L), 3, "0"));
            pop = Math.floor(pop/1000);
        }
        ret.insert(0, String.valueOf((long)pop));
        return ret.toString();
    }
    /**
     * Format number as hex string.
     * @param Hex
     * @return String
     */
    public static String formatHex(int hex)
    {
        return String.valueOf(int2upp(hex));
    }


    /**
     * Convert an int to a UPP character.
     * @param i integer value
     * @return char character value
     */
    public static char int2upp(int i)
    {
        if (i == -1)
            return 'R';
        else if (i == -2)
            return 'S';
        if ((i < 0) || (i >= DisplayUtils.uppLookup.length()))
            return '?';
        return DisplayUtils.uppLookup.charAt(i);
    }

    public static String int2upp(int[] i)
    {
        StringBuffer ret = new StringBuffer();
        for (int j = 0; j < i.length; j++)
            ret.append(int2upp(i[j]));
        return ret.toString();
    }

    /**
     * Convert hex char to int.
     * @param c
     * @return int
     */
    public static int upp2int(char c)
    {
        return DisplayUtils.uppLookup.indexOf(c);
    }


    // global hex conversion functions
    public static String uppLookup = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    /**
     * Format number as currency.
     * @param currency
     * @return String
     */
    public static String formatCurrency(double currency)
    {
        String minus;
        if (currency < 0)
        {
            minus = "-";
            currency = -currency;
        }
        else
            minus = "";
        if (currency >= 1000000000000.0)
            return minus + DoubleUtils.format(currency / 1000000000000.0, -1, 1) + "TCr";
        if (currency >= 1000000000.0)
            return minus + DoubleUtils.format(currency / 1000000000.0, -1, 1) + "BCr";
        if (currency >= 1000000.0)
            return minus + DoubleUtils.format(currency / 1000000.0, -1, 1) + "MCr";
        if (currency >= 1000.0)
            return minus + DoubleUtils.format(currency / 1000.0, -1, 1) + "KCr";
        if (currency >= 0.01)
            return minus + DoubleUtils.format(currency, -1, 2) + "Cr";
        return "0Cr";
    }

    /**
     * Format number as currency with all decimals.
     * @param currency
     * @return String
     */
    public static String formatCurrencyFull(double currency)
    {
        String minus;
        if (currency < 0)
        {
            minus = "-";
            currency = -currency;
        }
        else
            minus = "";
        if (currency >= 0.01)
            return minus + formatCommaNumber((long)currency) + "Cr";
        return "0Cr";
    }

    public static String formatCommaNumber(long num)
    {
    	String ret = String.valueOf(num);
    	for (int i = ret.length() - 3; i > 0; i -= 3)
    		ret = ret.substring(0, i) + "," + ret.substring(i);
    	return ret;
    }
    
    public static String formatURI(LocationURI uri, IGenScheme scheme)
    {
        OrdBean ords = uri.getOrds();
        if (uri.getType() == LocationURI.MAINWORLD)
        {
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(ords);
            if (mw == null)
                return "Hex "+OrdLogic.getShortNum(ords);
            else
                return mw.getName()+" ("+OrdLogic.getShortNum(ords)+")";
        }
        else if (uri.getType() == LocationURI.BODY)
        {
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(ords);
            String primary = uri.getPath();
            int o = primary.lastIndexOf("/");
            if (o >= 0)
                primary = primary.substring(o+1);
            String orbit = uri.getParam("orbit");
            if (orbit == null)
                return "At "+primary+" in the "+mw.getName()+" system.";
            else if (orbit.equals("0"))
                return "On "+primary+" in the "+mw.getName()+" system.";
            else
            {
                BodyBean b = (BodyBean)SchemeLogic.getFromURI(scheme, LocationURILogic.getURI(uri));
                String desc;
                if (b instanceof BodyStarBean)
                {
                	double oval = Double.parseDouble(orbit);
                	double r = oval*b.getDiameter();
                	double oNum = BodyBean.convRadiusToOrbit(r);
                	desc = "orbit #"+DoubleUtils.format(oNum, 0, 1)+" around ";
                }
                else if ((b instanceof BodyWorldBean) || (b instanceof BodyGiantBean))
                {
                	double oval = Double.parseDouble(orbit);
                	desc = DoubleUtils.format(oval, 0, 1)+" diameters around ";
                }
                else
                    desc = "";
                return "At "+desc+primary+" in the "+mw.getName()+" system.";
            }
        }
        else if (uri.getType() == LocationURI.SYSTEM)
        {
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(ords);
        	String destSys = uri.getParam("destSys");
        	if (destSys == null)
                return "In the "+mw.getName()+" system.";
        	OrdBean destOrds = OrdLogic.parseString(destSys);
            MainWorldBean dest = scheme.getGeneratorMainWorld().generateMainWorld(destOrds);
            String destPath = uri.getParam("destPath");
            double timeLeft = DoubleUtils.parseDouble(uri.getParam("timeLeft"))/60.0;
            String time;
            if (timeLeft < 36)
            	time = DoubleUtils.format(timeLeft, -1, 1)+" hours";
            else
            	time = DoubleUtils.format(timeLeft/24.0, -1, 1)+" days";
            if (destPath == null)
            {
	            return "In jumpspace between "+mw.getName()+" and "+dest.getName()+" "+time+" out.";
            }
            else
            {
	            int o = destPath.lastIndexOf('/');
	            if (o >= 0)
	            	destPath = destPath.substring(o+1);
	            return "In jumpspace between "+mw.getName()+" and "+dest.getName()+" "+time+" hours out from "+destPath+".";
            }
        }
        return uri.toString();
    }


    public static String formatURI(String uri, IGenScheme scheme)
    {
        if (StringUtils.isTrivial(uri))
            return "";
        return formatURI(LocationURILogic.fromURI(uri), scheme);
    }

    public static String formatLocation(LocBean loc)
    {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        sb.append(FormatUtils.formatDouble(loc.getX(), 2));
        sb.append(',');
        sb.append(FormatUtils.formatDouble(loc.getY(), 2));
        sb.append(',');
        sb.append(FormatUtils.formatDouble(loc.getZ(), 2));
        sb.append(']');
        return sb.toString();
    }

    public static String formatLocation2D(LocBean loc)
    {
        StringBuffer sb = new StringBuffer();
        sb.append('[');
        sb.append(FormatUtils.formatDouble(loc.getX(), 2));
        sb.append(',');
        sb.append(FormatUtils.formatDouble(loc.getY(), 2));
        sb.append(']');
        return sb.toString();
    }
}
