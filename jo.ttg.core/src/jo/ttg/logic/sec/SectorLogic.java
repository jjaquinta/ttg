/*
 * Created on Sep 25, 2005
 *
 */
package jo.ttg.logic.sec;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.sub.SubSectorLogic;
import jo.ttg.logic.uni.UniverseLogic;

public class SectorLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static SectorBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith("sec://"))
            return null;
        String sOrd = uri.substring(6);
        if (sOrd.indexOf(",") >= 0)
        {
            OrdBean o = OrdLogic.parseString(sOrd);
            return scheme.getGeneratorSector().generateSector(o);
        }
        else
        {
            return UniverseLogic.findSector(scheme.getGeneratorUniverse().generateUniverse(), sOrd);
        }
    }

    public static SectorBean getFromOrds(OrdBean o)
    {
        return getFromOrds(SchemeLogic.getDefaultScheme(), o);
    }

    public static SectorBean getFromOrds(IGenScheme scheme, OrdBean o)
    {
        return scheme.getGeneratorSector().generateSector(o);
    }

    // util

    public static SubSectorBean findSubSector(SectorBean sec, OrdBean o)
    {
        for (SubSectorBean s : sec.getSubSectors())
        {
            if (OrdLogic.within(o, s.getUpperBound(), s.getLowerBound()))
                return s;
        }
        return null;
    }

    public static SubSectorBean findSubSector(SectorBean sec, String name)
    {
        for (SubSectorBean s : sec.getSubSectors())
        {
            if (s.getName().equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public static MainWorldBean findMainWorld(SectorBean sec, OrdBean o)
    {
        try
        {
            return SubSectorLogic.findMainWorld(findSubSector(sec, o), o);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    public static MainWorldBean findMainWorld(SectorBean sec, String name)
    {
        for (SubSectorBean s : sec.getSubSectors())
        {
            MainWorldBean mw = SubSectorLogic.findMainWorld(s, name);
            if (mw != null)
                return mw;
        }
        return null;
    }

    public static List<SubSectorBean> findSubSectors(SectorBean sec, OrdBean upperBound, OrdBean lowerBound)
    {
        List<SubSectorBean> ret = new ArrayList<SubSectorBean>();
        for (SubSectorBean s : sec.getSubSectors())
        {
            if (OrdLogic.intersects(s.getUpperBound(), s.getLowerBound(), upperBound, lowerBound))
                ret.add(s);
        }
        return ret;
    }

    public static List<MainWorldBean> findMainWorlds(SectorBean sec, OrdBean upperBound, OrdBean lowerBound)
    {
        List<MainWorldBean> ret = new ArrayList<MainWorldBean>();
        for (SubSectorBean s : findSubSectors(sec, upperBound, lowerBound))
        {
            List<MainWorldBean> sw = SubSectorLogic.findMainWorlds(s, upperBound, lowerBound);
            ret.addAll(0, sw);
        }
        return ret;
    }
    
    public static MainWorldBean[] getMainWorlds(SectorBean sector)
    {
        List<MainWorldBean> mws = new ArrayList<MainWorldBean>();
        for (SubSectorBean sub : sector.getSubSectors())
            for (MainWorldBean mw : sub.getMainWorlds())
                mws.add(mw);
        return mws.toArray(new MainWorldBean[0]);
    }

    public static double getPopulation(SectorBean sector)
    {
        return MainWorldLogic.getPopulation(getMainWorlds(sector));
    }
}
