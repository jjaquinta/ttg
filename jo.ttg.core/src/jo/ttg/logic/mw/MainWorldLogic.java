/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.mw;

import java.util.StringTokenizer;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sub.SubSectorLogic;
import jo.ttg.logic.uni.UniverseLogic;
import jo.ttg.utils.DisplayUtils;

public class MainWorldLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static MainWorldBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith("mw://"))
            return null;
        String sOrd = uri.substring(5);
        if (sOrd.indexOf(",") >= 0)
        {
            OrdBean o = OrdLogic.parseString(sOrd);
            return scheme.getGeneratorMainWorld().generateMainWorld(o);
        }
        else
        {
            StringTokenizer st = new StringTokenizer(sOrd, "/");
            if (!st.hasMoreTokens())
                return null;
            String secName = st.nextToken();
            SectorBean sec = UniverseLogic.findSector(scheme.getGeneratorUniverse().generateUniverse(), secName);
            if (sec == null)
                return null;
            if (!st.hasMoreTokens())
                return null;
            String subName = st.nextToken();
            SubSectorBean sub = SectorLogic.findSubSector(sec, subName);
            if (sub == null)
            {
                MainWorldBean mw = SectorLogic.findMainWorld(sec, subName);
                if (mw != null)
                    return mw;
                if (subName.length() != 4)
                    return null;
                try
                {
                    int dx = Integer.parseInt(subName.substring(0, 2)) - 1;
                    int dy = Integer.parseInt(subName.substring(2, 4)) - 1;
                    OrdBean o = new OrdBean(sec.getUpperBound().getX() + dx, sec.getUpperBound().getY() + dy, 0);
                    mw = SectorLogic.findMainWorld(sec, o);
                    if (mw != null)
                        return mw;
                }
                catch (NumberFormatException e)
                {
                }
                return null;
            }
            if (!st.hasMoreTokens())
                return null;
            return SubSectorLogic.findMainWorld(sub, st.nextToken());
        }
    }

    public static MainWorldBean getFromOrds(OrdBean o)
    {
        return getFromOrds(null, o);
    }

    public static MainWorldBean getFromOrds(IGenScheme scheme, OrdBean o)
    {
        if (scheme == null)
            scheme = SchemeLogic.getDefaultScheme();
        return scheme.getGeneratorMainWorld().generateMainWorld(o);
    }

    public static String getNameDesc(MainWorldBean mw)
    {
        if (mw.getPopulatedStats().getUPP().isLo())
            return mw.getName().toLowerCase();
        if (mw.getPopulatedStats().getUPP().isHi())
            return mw.getName().toUpperCase();
        return mw.getName();
    }

    public static String getExtraDesc(MainWorldBean mw)
    {
        return String.valueOf(
            (mw.getPopulatedStats().getTravelZone() == 'G')
                ? ' '
                : mw.getPopulatedStats().getTravelZone())
            + String.valueOf(mw.getPopDigit())
            + String.valueOf(mw.getNumBelts())
            + String.valueOf(mw.getNumGiants());
    }
    public static String getStarsDesc(MainWorldBean mw)
    {
        StringBuffer ret = new StringBuffer();

        for (StarDeclBean s : mw.getStars())
        {
            ret.append(' ');
            ret.append(s.getDesc());
        }
        return ret.toString();
    }

    public static String getExportLine(MainWorldBean mw)
    {
        if (mw == null)
            return "<null>";
        StringBuffer ret = new StringBuffer();
        ret.append(mw.getName());
        ret.append("              ");
        ret.setLength(14);
        ret.append("    ");
        ret.append(UPPLogic.getUPPDesc(mw.getPopulatedStats().getUPP()));
        ret.append(' ');
        ret.append(mw.getPopulatedStats().getBasesDesc());
        ret.append("   ");
        ret.setLength(31);
        ret.append(' ');
        ret.append(UPPLogic.getTradeCodesDesc(mw.getPopulatedStats().getUPP()));
        ret.append("               ");
        ret.setLength(47);
        ret.append(' ');
        ret.append((char) (mw.getPopulatedStats().getTravelZone()));
        ret.append("  ");
        ret.append(String.valueOf(mw.getPopDigit()));
        ret.append(String.valueOf(mw.getNumBelts()));
        ret.append(String.valueOf(mw.getNumGiants()));
        ret.append(' ');
        ret.append(mw.getPopulatedStats().getAllegiance());
        for (StarDeclBean s : mw.getStars())
        {
            ret.append(' ');
            ret.append(s.getDesc());
        }
        ret.insert(0, " ");
        ret.insert(0, OrdLogic.getShortNum(mw.getOrds()));
        return ret.toString();
    }
    public static boolean importLine(MainWorldBean mw, String Line)
    {
//        if (Line.indexOf("Dn-4457") >= 0)
//            System.out.println(Line);
        char line[] = Line.toCharArray();
        int uwpOff = findUWP(line);
        if (uwpOff < 0)
            return false;
        importName(mw, Line, line, uwpOff);
        importUWB(mw, line, uwpOff);
        StringBuffer bases = new StringBuffer();
        int tcOff = parseBases(mw, line, uwpOff, bases);
        int statsOff = findStats(line, tcOff);
        if (statsOff < 0)
            return true;
        if (line[statsOff - 2] == 'R')
            mw.getPopulatedStats().setTravelZone('R');
        else if (line[statsOff - 2] == 'A')
            mw.getPopulatedStats().setTravelZone('A');
        else
            mw.getPopulatedStats().setTravelZone('G');
        mw.setPopDigit((line[statsOff] - '0'));
        mw.setNumBelts((line[statsOff + 1] - '0'));
        mw.setNumGiants((line[statsOff + 2] - '0'));
        int allOff = statsOff + 3;
        // Alliegence
        while ((allOff < line.length) && Character.isWhitespace(line[allOff]))
            allOff++;
        if (allOff == line.length)
            return true;
        mw.getPopulatedStats().setAllegiance(Line.substring(allOff, allOff + 2));
        importBases(mw, bases.toString().toCharArray());
        String ln = Line.substring(allOff + 2).trim();
        // do stars
        //K5 VI M4 D M3
        for (int star = 0; star < 3; star++)
        {
            if (ln.length() == 0)
                break;
            StarDeclBean s = new StarDeclBean();
            ln = s.importStarType(ln);
            if (ln.length() == 0)
                break;
            ln = s.importStarClass(ln);
            if (!s.getIsSet())
                break;
            mw.addStars(s);
            if (ln.length() == 0)
                break;
        }
        return true;
    }

    private static void importBases(MainWorldBean mw, char[] line)
    {
        long bases = 0L;
        String al = mw.getPopulatedStats().getAllegiance();
        for (char c : line)
        {
            switch (c)
            {
                case 'A' :
                    bases |= PopulatedStatsBean.SCOUT_BASE;
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'B' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    bases |= PopulatedStatsBean.SCOUT_WAY;
                    break;
                case 'C' :
                    if (al.equals("Va"))
                        bases |= PopulatedStatsBean.CORSAIR_BASE;
                    else
                        bases |= PopulatedStatsBean.COLONY_BASE;
                    break;
                case 'D' :
                    bases |= PopulatedStatsBean.NAVAL_DEPOT;
                    break;
                case 'E' :
                    bases |= PopulatedStatsBean.E_BASE;
                    break;
                case 'F' :
                    if ((al.equals("In") || al.equals("Hi")))
                        bases |= PopulatedStatsBean.LOCAL_BASE|PopulatedStatsBean.NAVAL_BASE;
                    else
                        bases |= PopulatedStatsBean.FARM_BASE;
                    break;
                case 'G' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'H' :
                    bases |= PopulatedStatsBean.NAVAL_BASE|PopulatedStatsBean.CORSAIR_BASE;
                    break;
                case 'I' :
                    bases |= PopulatedStatsBean.I_BASE;
                    break;
                case 'J' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'K' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'L' :
                    if (al.equals("Hi"))
                        bases |= PopulatedStatsBean.NAVAL_BASE;
                    else
                        bases |= PopulatedStatsBean.LAB_BASE;
                    break;
                case 'M' :
                    bases |= PopulatedStatsBean.LOCAL_BASE;
                    break;
                case 'N' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'O' :
                    bases |= PopulatedStatsBean.NAVAL_OUTPOST;
                    break;
                case 'P' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case 'Q' :
                    bases |= PopulatedStatsBean.MILITARY_GARRISON;
                    break;
                case 'R' :
                    bases |= PopulatedStatsBean.CLAN_BASE;
                    break;
                case 'S' :
                    bases |= PopulatedStatsBean.SCOUT_BASE;
                    break;
                case 'T' :
                    bases |= PopulatedStatsBean.TLAUKHU_BASE;
                    break;
                case 'U' :
                    bases |= PopulatedStatsBean.CLAN_BASE|PopulatedStatsBean.TLAUKHU_BASE;
                    break;
                case 'W' :
                    bases |= PopulatedStatsBean.SCOUT_WAY;
                    break;
                case 'X' :
                    bases |= PopulatedStatsBean.RELAY_STATION;
                    break;
                case 'Y' :
                    bases |= PopulatedStatsBean.NAVAL_DEPOT;
                    break;
                case 'Z' :
                    bases |= PopulatedStatsBean.NAVAL_BASE;
                    break;
                case '#' :
                    bases |= PopulatedStatsBean.MINE_BASE;
                    break;
                case ':' :
                    bases |= PopulatedStatsBean.COLON_BASE;
                    break;
                case '0' :
                    bases |= PopulatedStatsBean.ZERO_BASE;
                    break;
                case '1' :
                    bases |= PopulatedStatsBean.ONE_BASE;
                    break;
                case '2' :
                    bases |= PopulatedStatsBean.TWO_BASE;
                    break;
                case '3' :
                    bases |= PopulatedStatsBean.THREE_BASE;
                    break;
                case '4' :
                    bases |= PopulatedStatsBean.FOUR_BASE;
                    break;
                case '5' :
                    bases |= PopulatedStatsBean.FIVE_BASE;
                    break;
                case '6' :
                    bases |= PopulatedStatsBean.SIX_BASE;
                    break;
                case '7' :
                    bases |= PopulatedStatsBean.SEVEN_BASE;
                    break;
                case '8' :
                    bases |= PopulatedStatsBean.EIGHT_BASE;
                    break;
                default:
                    System.out.println("UNKNOWN BASE: '"+c+"' ("+al+")");
                    break;
            }
        }
        mw.getPopulatedStats().setBases(bases);
    }


    private static int parseBases(MainWorldBean mw, char[] line, int uwpOff, StringBuffer bases)
    {
        int i = uwpOff + 10;
        while (Character.isWhitespace(line[i]) && (i < line.length))
            i++;
        if (i > uwpOff + 20)
        {   // too big a gap, no bases
            return i;
        }
        if (Character.isUpperCase(line[i])
            && Character.isLowerCase(line[i + 1]))
            return i; // trade code
        while (!Character.isWhitespace(line[i]) && (i < line.length))
        {
            bases.append(line[i]);
            i++;
        }
        return i;
    }

    private static void importUWB(MainWorldBean mw, char[] line, int uwpOff)
    {
        mw.getPopulatedStats().getUPP().getPort().setValue((int) (line[uwpOff + 1]));
        mw.getPopulatedStats().getUPP().getSize().setValue(DisplayUtils.upp2int(line[uwpOff + 2]));
        mw.getPopulatedStats().getUPP().getAtmos().setValue(DisplayUtils.upp2int(line[uwpOff + 3]));
        mw.getPopulatedStats().getUPP().getHydro().setValue(DisplayUtils.upp2int(line[uwpOff + 4]));
        mw.getPopulatedStats().getUPP().getPop().setValue(DisplayUtils.upp2int(line[uwpOff + 5]));
        mw.getPopulatedStats().getUPP().getGov().setValue(DisplayUtils.upp2int(line[uwpOff + 6]));
        mw.getPopulatedStats().getUPP().getLaw().setValue(DisplayUtils.upp2int(line[uwpOff + 7]));
        mw.getPopulatedStats().getUPP().getTech().setValue(DisplayUtils.upp2int(line[uwpOff + 9]));
        UPPLogic.updateTradeCodes(mw.getPopulatedStats().getUPP());
    }

    private static void importName(MainWorldBean mw, String Line, char[] line, int uwpOff)
    {
        int hexOff = findHex(line);
        if (hexOff < 0)
            mw.setName(Line.substring(0, uwpOff).trim());
        else if (hexOff == 0)
            mw.setName(Line.substring(4, uwpOff).trim());
        else
            mw.setName(Line.substring(0, hexOff).trim());
    }

    private static int findStats(char[] line, int tcOff)
    {
        for (int i = tcOff; i < line.length - 3; i++)
            if (Character.isDigit(line[i])
                && Character.isDigit(line[i + 1])
                && Character.isDigit(line[i + 2]))
                return i;
        return -1;
    }
    
    public static int findHex(char[] line)
    {
        // now look for <space><alnum*4><space>
        for (int i = 0; i < line.length - 4; i++)
            if (Character.isDigit(line[i])
                && Character.isDigit(line[i + 1])
                && Character.isDigit(line[i + 2])
                && Character.isDigit(line[i + 3]))
                {
                    if ((i == 0) || Character.isWhitespace(line[i-1]))
                        if ((i + 4 == line.length) || Character.isWhitespace(line[i+4]))
                            return i;
                }
        return -1;
    }
    
    private static int findUWP(char[] line)
    {
        // now look for <space><alnum*6>-<alnum>
        for (int i = 0; i < line.length - 10; i++)
            if (Character.isWhitespace(line[i])
                && Character.isLetterOrDigit(line[i + 1])
                && Character.isLetterOrDigit(line[i + 2])
                && Character.isLetterOrDigit(line[i + 3])
                && Character.isLetterOrDigit(line[i + 4])
                && Character.isLetterOrDigit(line[i + 5])
                && Character.isLetterOrDigit(line[i + 6])
                && Character.isLetterOrDigit(line[i + 7])
                && (line[i + 8] == '-')
                && Character.isLetterOrDigit(line[i + 9]))
                    return i;
        return -1;
    }

    /**
     * This returns the actual population
     * using the Pop digit and muiltiplier.
     */
    public static double getPopulation(MainWorldBean mw)
    {
        return Math.pow(10, mw.getPopulatedStats().getUPP().getPop().getValue())
            * mw.getPopDigit();
    }

    /**
     * This returns the actual population of all the worlds combined
     * using the Pop digit and muiltiplier.
     */
    public static double getPopulation(MainWorldBean[] mws)
    {
        double tot = 0;
        for (int i = 0; i < mws.length; i++)
            tot += getPopulation(mws[i]);
        return tot;
    }
    /**
     * This returns the TCS annual revenue.
     * If type is 0 then the peace value is
     * returned. If type is 1 then the war
     * value is returned.
     */
    public static double getTCSRevenue(MainWorldBean mw, int type)
    {
        return 500
            * getPopulation(mw)
            * mw.getPopulatedStats().getUPP().getGov().getGovMod(type)
            * UPPLogic.getXRate(mw.getPopulatedStats().getUPP());
    }
    /**
     * This returns the TCS tonnage capacity.
     * If type is 0 then the peace value is
     * returned. If type is 1 then the war
     * value is returned.
     */
    public static double getTCSShipTonnage(MainWorldBean mw, int type)
    {
        int prt = mw.getPopulatedStats().getUPP().getPort().getValue();
        if ((prt != 'A') && (prt != 'B'))
            return 0;
        return getPopulation(mw)
            * mw.getPopulatedStats().getUPP().getGov().getGovMod(type)
            / 100;
    }
    /**
     * This returns the TCS tonnage of ships
     * that can be produced.
     * If type is 0 then the peace value is
     * returned. If type is 1 then the war
     * value is returned.
     */
    public static int getTCSShipyardCapacity(MainWorldBean mw, int type)
    {
        if (mw.getPopulatedStats().getUPP().getPort().getValue() != 'A')
            return 0;
        return (int)
            (getPopulation(mw)
                * mw.getPopulatedStats().getUPP().getGov().getGovMod(type)
                / 1000);
    }
}
