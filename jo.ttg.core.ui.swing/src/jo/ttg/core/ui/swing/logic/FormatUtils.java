package jo.ttg.core.ui.swing.logic;
/**
 * Created on Aug 13, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */

import java.util.StringTokenizer;
import java.util.Vector;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.UPPDigitBean;
import jo.ttg.beans.mw.UPPPorBean;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class FormatUtils
{
    // global hex conversion functions
    static private String uppLookup = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    public static final int D_CRITICAL = 1;
    public static final int D_ERROR = 2;
    public static final int D_WARN = 3;
    public static final int D_INFO = 4;
    public static final int D_TRACE = 5;

    static private int debugLevel = 5;

    /**
     * Print debugging information.
     * Pass in one of D_CRITICAL..D_TRACE
     * @param lvl Debug level
     * @param msg Message
     */
    public static void debug(int lvl, String msg)
    {
        if (lvl <= debugLevel)
        {
            if (msg.endsWith("*"))
                System.out.print(msg.substring(0, msg.length()));
            else
                System.out.println(msg);
        }
    }

    /**
     * Print trace debug message.
     * @param msg Message
     */
    public static void debug(String msg)
    {
        debug(D_TRACE, msg);
    }

    /**
     * his is the general bailout function
     * @param fmt Message
     */
    public static void abort(String fmt)
    {
        debug(D_CRITICAL, fmt);
        System.exit(-1);
    }

    /**
     * Assert a condition.
     * @param val asserted or not
     * @param msg assertion failure message
     */
    public static void assertValue(boolean val, String msg)
    {
        if (!val)
        {
            debug(D_ERROR, msg);
            Throwable t = new Throwable(msg);
            t.fillInStackTrace();
            t.printStackTrace(System.out);
        }
        //System.exit(-1);
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
        if ((i < 0) || (i >= uppLookup.length()))
            return '?';
        return uppLookup.charAt(i);
    }
    
    public static String sUpp(int i)
    {
        return String.valueOf(int2upp(i));
    }
    
    public static String sUpp(UPPDigitBean dig)
    {
        if (dig instanceof UPPPorBean)
        {
            int v = dig.getValue();
            if (v < 0)
                return "?";
            else
                return String.valueOf((char)v);
        }
        else
            return String.valueOf(int2upp(dig.getValue()));
    }

    public static String int2upp(int[] is)
    {
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < is.length; i++)
            ret.append(int2upp(is[i]));
        return ret.toString();
    }

    /**
     * Calculate Log base 10.
     * @param v value
     * @return double log
     */
    public static double log10(double v)
    {
        return Math.log(v) / 2.302585092994;
    }

    /**
     * Print informational message.
     * @param msg
     */
    public static void message(String msg)
    {
        debug(D_INFO, msg);
    }

    /**
     * Get string representation of an array of numbers.
     * @param Num
     * @return String
     */
    public static String sNum(double Num[])
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < Num.length; i++)
        {
            if (i > 0)
                sb.append(",");
            sb.append(sNum(Num[i], -1, 2));
        }
        return sb.toString();
    }

    /**
     * Format a number as a string.
     * @param Num
     * @param siz
     * @param dpts
     * @return String
     */
    public static String sNum(double Num, int siz, int dpts)
    {
        //System.out.print("sNum("+String.valueOf(Num/7)+", "+String.valueOf(siz)+", "+String.valueOf(dpts)+")=");
        String s = FormatUtils.sNum(Num);
        String exp;
        int o;
        o = s.indexOf('E');
        if (o >= 0)
        {
            exp = s.substring(o);
            s = s.substring(0, o);
        }
        else
            exp = null;
        o = s.indexOf('.');
        if (o == 0)
            s = s.substring(0, o);
        else if (o > 0)
        {
            if (s.length() >= o + dpts + 1)
                s = s.substring(0, o + dpts + 1);
        }
        if (exp != null)
            s += exp;
        if (siz > 0)
        {
            s = "            " + s;
            s = s.substring(s.length() - siz);
        }
        else if (siz < -1)
        {
            s = s + "            ";
            s = s.substring(0, -siz);
        }
        //System.out.println(s);
        return s;
    }
    /**
     * Convert long to string.
     * @param Num
     * @return String
     */
    public static String sNum(long Num)
    {
        return Long.toString(Num);
    }
    /**
     * Convert long to string with justification.
     * @param Num
     * @param len
     * @return String
     */
    public static String sNum(long Num, int len)
    {
        return sStr(sNum(Num), len);
    }
    /**
     * Split up string.
     * StringTokenizer merges spearators. This does not.
     * @param buf
     * @param sep
     * @return String[]
     */
    public static String[] split(String buf, char sep)
    {
        Vector<String> v = new Vector<String>();
        StringBuffer acc = new StringBuffer();
        char[] c = buf.toCharArray();

        for (int i = 0; i < c.length; i++)
            if (c[i] == sep)
            {
                v.addElement(acc.toString());
                acc.setLength(0);
            }
            else
                acc.append(c[i]);
        v.addElement(acc.toString());
        String[] ret = new String[v.size()];
        v.copyInto(ret);
        //debug("sep="+sep+" splits '"+buf+"' into "+ret.length+" tokens.");        
        return ret;
    }
    /**
     * Justify string.
     * @param str
     * @param wid
     * @return String
     */
    public static String sStr(String str, int wid)
    {
        return sStr(str, wid, " ");
    }
    /**
     * Justify string.
     * @param str
     * @param wid
     * @return String
     */
    public static String sStr(String str, int wid, String pad)
    {
        StringBuffer ret = new StringBuffer(str);
        int len = str.length();
        if (wid < 0)
        {
            wid = -wid;
            if (len > wid)
                return str.substring(len - wid);
            for (int i = wid - len; i > 0; i--)
                ret.insert(0, pad);
        }
        else
        {
            if (len > wid)
                return str.substring(0, wid);
            for (int i = wid - len; i > 0; i--)
                ret.append(pad);
        }
        return ret.toString();
    }
    /**
     * Format number as currency.
     * @param currency
     * @return String
     */
    public static String sCurrency(double currency)
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
            return minus + sNum(currency / 1000000000000.0, -1, 1) + "TCr";
        if (currency >= 1000000000.0)
            return minus + sNum(currency / 1000000000.0, -1, 1) + "BCr";
        if (currency >= 1000000.0)
            return minus + sNum(currency / 1000000.0, -1, 1) + "MCr";
        if (currency >= 1000.0)
            return minus + sNum(currency / 1000.0, -1, 1) + "KCr";
        if (currency >= 0.01)
            return minus + sNum(currency, -1, 2) + "Cr";
        return "0Cr";
    }
    /**
     * Format number as resource unit.
     * @param resource
     * @return String
     */
    public static String sRU(double resource)
    {
        return sNum(resource, -1, 1) + "RU";
    }
    /**
     * Format number as percentage.
     * @param pc
     * @return String
     */
    public static String sPC(double pc)
    {
        return sNum(pc, -1, 0) + "%";
    }
    /**
     * Format number as tons.
     * @param t
     * @return String
     */
    public static String sTons(double t)
    {
        return sNum((int)Math.floor(t+.5)) + "t";
    }
    /**
     * Format number as eccentricity.
     * @param Eccen
     * @return String
     */
    public static String sEccen(double eccen)
    {
        return FormatUtils.sNum(eccen);
    }
    /**
     * Format number as density.
     * @param Density
     * @return String
     */
    public static String sDensity(double density)
    {
        return FormatUtils.sNum(density);
    }
    /**
     * Format number as distance.
     * @param Dist
     * @return String
     */
    public static String sDistance(double dist)
    {
        if (dist >= 0.2)
            return FormatUtils.sNum(dist) + "AU";
        dist = convAUToKm(dist);
        if (dist >= 200000.0)
            return FormatUtils.sNum(dist / 1000000.0) + "MKm";
        if (dist > .5)
            return FormatUtils.sNum(dist) + "Km";
        return FormatUtils.sNum(dist * 1000.0) + "m";
    }
    /**
     * Format number as distance.
     * @param Dist
     * @return String
     */
    public static String sDistance(double Dist[])
    {
        double dist = 0;
        for (int i = 0; i < Dist.length; i++)
            if (Dist[i] < 0)
            {
                if (-Dist[i] > dist)
                    dist = -Dist[i];
            }
            else if (Dist[i] > dist)
                dist = Dist[i];
        if (dist >= 0.2)
            return sNum(Dist) + "AU";
        double dDist[] = new double[Dist.length];
        for (int i = 0; i < Dist.length; i++)
            dDist[i] = convAUToKm(Dist[i]);
        dist = convAUToKm(dist);
        if (dist >= 200000.0)
        {
            mult(dDist, 1 / 1000000.0);
            return sNum(dDist) + "MKm";
        }
        if (dist > .5)
        {
            return sNum(dDist) + "Km";
        }
        mult(dDist, 1000.0);
        return sNum(dDist) + "m";
    }
    /**
     * Format number as time.
     * @param Days
     * @return String
     */
    public static String sDays(double days)
    {
        if (days < 0.9 / 24.0)
            return sNum(days * 24.0 * 60.0, 4, 1) + " m";
        if (days < 23.9 / 24.0)
            return sNum(days * 24.0, 4, 1) + " h";
        if (days < 7.0)
            return sNum(days, 4, 1) + " d";
        if (days < 28.0)
        {
            return sNum(days / 7, 4, 1) + " wk";
        }
        if (days < 28.0 * 11.0)
            return sNum(days / 28, 4, 1) + " mnth";
        return sNum(days / 365.25, 4, 1) + " yrs";
    }
    /**
     * Format number as Gs.
     * @param g
     * @return String
     */
    public static String sGrav(double g)
    {
        return sNum(g, 4, 1) + "G";
    }
    /**
     * Format number as mass.
     * @param Mass
     * @return String
     */
    public static String sMass(double mass)
    {
        double tons;

        tons = (double) convSMToMT(mass);
        if (tons >= 1000000.0)
            return sNum(mass, -1, 2) + "SM";
        if (tons >= 200000.0)
            return FormatUtils.sNum(tons / 1000000.0) + "Mt";
        if (tons >= 1.0)
            return FormatUtils.sNum(tons) + "t";
        return FormatUtils.sNum(tons * 1000.0) + "kg";
    }
    /**
     * Format number as hex string.
     * @param Hex
     * @return String
     */
    public static String sHex(int hex)
    {
        return String.valueOf(int2upp(hex));
    }
    /**
     * Format number as hours.
     * @param Hours
     * @return String
     */
    public static String sHours(double hours)
    {
        return sDays(convHoursToDays(hours));
    }
    /**
     * Format number as tilt.
     * @param tilt
     * @return String
     */
    public static String sTilt(int tilt)
    {
        return sNum(tilt) + "\u00b0";
    }
    /**
     * Format number as temperature.
     * @param Temp
     * @return String
     */
    public static String sTemp(double Temp)
    {
        if ((Temp < 273.0 + 500.0) && (Temp > 273.0 - 100.0))
            return sNum(Temp - 273.0, -1, 1) + "\u00b0C";
        return sNum(Temp, -1, 1) + "\u00b0K";
    }

    /**
     * Convert hex char to int.
     * @param c
     * @return int
     */
    public static int upp2int(char c)
    {
        return uppLookup.indexOf(c);
    }
    /**
     * Convert ascii to integer.
     * @param buf
     * @return int
     */
    public static int atoi(String buf)
    {
        return (int) atol(buf);
    }
    /**
     * Convert object to integer.
     * @param o
     * @return int
     */
    public static int atoi(Object o)
    {
        if (o instanceof Number)
            return ((Number) o).intValue();
        return atoi(o.toString());
    }
    /**
     * Convert ascii to long.
     * @param o
     * @return int
     */
    public static long atol(String buf)
    {
        if (buf == null)
            return 0;
        long neg = 1;
        long ret = 0;
        char str[] = buf.toCharArray();
        int i;
        // skip till first number
        for (i = 0; i < str.length; i++)
            if (str[i] == '-')
                neg = -1;
            else if ((str[i] != ' ') && (str[i] != '+'))
                break;
        // parse digits
        for (; i < str.length; i++)
            if ((str[i] < '0') || (str[i] > '9'))
                break;
            else
            {
                ret *= 10;
                ret += str[i] - '0';
            }
        return ret * neg;
    }
    /**
     * Convert object to long.
     * @param o
     * @return long
     */
    public static long atol(Object o)
    {
        if (o instanceof Number)
            return ((Number) o).longValue();
        return atol(o.toString());
    }
    /**
     * Convert string to double.
     * @param buf
     * @return double
     */
    public static double atod(String buf)
    {
        try
        {
            return Double.valueOf(buf).doubleValue();
        }
        catch (Exception e)
        {
            return 0;
        }
    }
    /**
     * Convert object to double.
     * @param o
     * @return double
     */
    public static double atod(Object o)
    {
        if (o instanceof Number)
            return ((Number) o).doubleValue();
        return atod(o.toString());
    }
    /**
     * Convert string to double array.
     * @param buf
     * @return double[]
     */
    public static double[] atoddd(String buf)
    {
        if (buf == null)
            return null;
        StringTokenizer st = new StringTokenizer(buf, ",");
        double ret[] = new double[st.countTokens()];
        for (int i = 0; i < ret.length; i++)
            ret[i] = atod(st.nextToken());
        return ret;
    }
    /**
     * Convert integer to string.
     * @param i
     * @return String
     */
    public static String itoa(int i)
    {
        return sNum(i);
    }
    /**
     * Convert long to string.
     * @param l
     * @return String
     */
    public static String ltoa(long l)
    {
        return sNum(l);
    }
    /**
     * Convert double to string.
     * @param d
     * @return String
     */
    public static String dtoa(double d)
    {
        return FormatUtils.sNum(d);
    }
    /**
     * Multiple an array of double by a value.
     * @param v
     * @param scalar
     */
    public static void mult(double v[], double scalar)
    {
        for (int i = 0; i < v.length; i++)
            v[i] *= scalar;
    }
    /**
     * Calculate magnitude of an array.
     * @param v
     * @return double
     */
    public static double mag(double v[])
    {
        double ret = 0;
        for (int i = 0; i < v.length; i++)
            ret += v[i] * v[i];
        return Math.sqrt(ret);
    }
    /**
     * Subtract two arrays.
     * @param v1
     * @param v2
     * @return double[]
     */
    public static double[] diff(double v1[], double v2[])
    {
        double ret[] = new double[v1.length];
        for (int i = 0; i < ret.length; i++)
            ret[i] = v2[i] - v1[i];
        return ret;
    }
    /**
     * Increment array by another.
     * @param v1
     * @param v2
     * @return double[]
     */
    public static double[] incr(double v1[], double v2[])
    {
        for (int i = 0; i < v1.length; i++)
            v1[i] += v2[i];
        return v1;
    }
    /**
     * Calculate distance between points.
     * @param v1
     * @param v2
     * @return double
     */
    public static double dist(double v1[], double v2[])
    {
        return mag(diff(v1, v2));
    }
    /**
     * Set one array with another.
     * @param v1
     * @param v2
     * @return double[]
     */
    public static double[] set(double v1[], double v2[])
    {
        System.arraycopy(v2, 0, v1, 0, v1.length);
        return v1;
    }
    /**
     * Set array with minimum of the two.
     * @param min
     * @param v
     * @return double[]
     */
    public static double[] setMin(double min[], double v[])
    {
        for (int i = 0; i < v.length; i++)
            if (v[i] < min[i])
                min[i] = v[i];
        return min;
    }
    /**
     * Set array to maximum of the two.
     * @param min
     * @param v
     * @return double[]
     */
    public static double[] setMax(double min[], double v[])
    {
        for (int i = 0; i < v.length; i++)
            if (v[i] > min[i])
                min[i] = v[i];
        return min;
    }
    /**
     * Duplicate an array.
     * @param arr
     * @return int[]
     */
    public static int[] arraydup(int[] arr)
    {
        int[] ret = new int[arr.length];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        return ret;
    }

    // conversion routines
    // MT = metric tons, SM = solar masses, Kg = Kilo Grams

    /**
     * Convert Astronomical Units to Kilometers.
     * @param AU
     * @return double
     */
    public static double convAUToKm(double AU)
    {
        return (double) (149600000.0 * AU);
    }
    /**
     * Convert Astronomical Unots to Meters.
     * @param AU
     * @return double
     */
    public static double convAUToM(double AU)
    {
        return (double) (convAUToKm(AU) * 1000.0);
    }
    /**
     * Convert Astronomical Units to Miles.
     * @param AU
     * @return double
     */
    public static double convAUToMiles(double AU)
    {
        return convKmToMiles(convAUToKm(AU));
    }
    /**
     * Convert days to hours.
     * @param days
     * @return double
     */
    public static double convDaysToHours(double days)
    {
        return days * 24;
    }
    /**
     * Convert days to seconds.
     * @param days
     * @return double
     */
    public static double convDaysToSeconds(double days)
    {
        return convHoursToSeconds(convDaysToHours(days));
    }
    /**
     * Convert Hours To Days.
     * @param hours
     * @return double
     */
    public static double convHoursToDays(double hours)
    {
        return hours / 24;
    }
    /**
     * Convert hours to seconds.
     * @param hours
     * @return double
     */
    public static double convHoursToSeconds(double hours)
    {
        return hours * 60 * 60;
    }
    /**
     * Convert kilograms to metric tons.
     * @param Kg
     * @return double
     */
    public static double convKgToMT(double Kg)
    {
        return (double) (Kg / 1000.0);
    }
    /**
     * Convert kilograms to solar masses.
     * @param Kg
     * @return double
     */
    public static double convKgToSM(double Kg)
    {
        return (double) convMTToSM(convKgToMT(Kg));
    }
    /**
     * Convert kilometers to astronomical units.
     * @param KM
     * @return double
     */
    public static double convKmToAU(double KM)
    {
        return (double) (KM / 149600000.0);
    }
    /**
     * Convert kilometers to miles.
     * @param KM
     * @return double
     */
    public static double convKmToMiles(double KM)
    {
        return (double) (KM / 1.6);
    }
    /**
     * Convert miles to Astronomical units.
     * @param Miles
     * @return double
     */
    public static double convMilesToAU(double Miles)
    {
        return convKmToAU(convMilesToKm(Miles));
    }
    /**
     * Convert miles to kilometers.
     * @param Miles
     * @return double
     */
    public static double convMilesToKm(double Miles)
    {
        return (double) (Miles * 1.6);
    }
    /**
     * Convert metric tons to kilograms.
     * @param MT
     * @return double
     */
    public static double convMTToKg(double MT)
    {
        return (double) (MT * 1000.0);
    }
    /**
     * Convert metric tons to solar masses.
     * @param MT
     * @return double
     */
    public static double convMTToSM(double MT)
    {
        return (double) (MT / 1.939e27);
    }
    /**
     * Convert seconds to days.
     * @param sec
     * @return double
     */
    public static double convSecondsToDays(double sec)
    {
        return convHoursToDays(convSecondsToHours(sec));
    }
    /**
     * Convert seconds to hours.
     * @param sec
     * @return double
     */
    public static double convSecondsToHours(double sec)
    {
        return sec / 60 / 60;
    }
    /**
     * Convert solar masses to kilograms.
     * @param SM
     * @return double
     */
    public static double convSMToKg(double SM)
    {
        return (double) convMTToKg(convSMToMT(SM));
    }
    /**
     * Solar masses to metric tons.
     * @param SM
     * @return double
     */
    public static double convSMToMT(double SM)
    {
        return (double) (SM * 1.939e27);
    }

    /**
     * Returns the debugLevel.
     * @return int
     */
    public static int getDebugLevel()
    {
        return debugLevel;
    }

    /**
     * Sets the debugLevel.
     * @param debugLevel The debugLevel to set
     */
    public static void setDebugLevel(int debugLevel)
    {
        FormatUtils.debugLevel = debugLevel;
    }
    
    public static String normalizePath(String path)
    {
        StringBuffer ret = new StringBuffer();
        char[] c = path.toCharArray();
        for (int i = 0; i < c.length; i++)
            if (c[i] == '\\')
                ret.append('/');
            else if (Character.isUpperCase(c[i]))
                ret.append(Character.toLowerCase(c[i]));
            else
                ret.append(c[i]);
        return ret.toString();
    }

    /**
     * Get string representation of a number.
     * @param Num
     * @return String
     */
    public static String sNum(double Num)
    {
        return Double.toString(Num);
    }

    public static String sPower(double d)
    {
        String ret = sNum(d);
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

    public static String sWeight(double d)
    {
        String ret = sNum(d);
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
        return ret;
    }

    /**
     * @param needed
     * @return
     */
    public static String sVolume(double vol)
    {
        if (vol > 13.5)
            return sTons((int)(vol/13.5));
        return sNum(vol, 8, 1).trim()+"kl";
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
        return FormatUtils.sNum(date.getYear(), -4)+"-"+
            FormatUtils.sNum(date.getDay(), -3);
    }

    public static String formatTime(DateBean date)
    {
        if (date == null)
            return "";
        return FormatUtils.sNum(date.getHour(), -2)+":"+
            FormatUtils.sStr(String.valueOf(date.getMinute()), -2, "0");
    }

    public static String sPopulation(double pop)
    {
        StringBuffer ret = new StringBuffer();
        while (pop >= 1000)
        {
            double remainder = pop - Math.floor(pop/1000)*1000;
            ret.insert(0, ","+sStr(ltoa(((long)remainder)%1000), 3, "0"));
            pop = Math.floor(pop/1000);
        }
        ret.insert(0, sNum((long)pop));
        return ret.toString();
    }
    
    public static String formatLongLat(double[] longlat)
    {
        StringBuffer ret = new StringBuffer();
        if (longlat[1] < 0)
            ret.append(String.valueOf(-(int)longlat[1])+"\u00b0S");
        else
            ret.append(String.valueOf((int)longlat[1])+"\u00b0N");
        ret.append(" ");
        if (longlat[0] > 180)
            ret.append(String.valueOf(360-(int)longlat[0])+"\u00b0W");
        else
            ret.append(String.valueOf((int)longlat[0])+"\u00b0E");
        return ret.toString();
    }
}
