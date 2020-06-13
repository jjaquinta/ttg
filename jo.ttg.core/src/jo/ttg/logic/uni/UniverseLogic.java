/*
 * Created on Sep 25, 2005
 *
 */
package jo.ttg.logic.uni;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sub.SubSectorLogic;

public class UniverseLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static UniverseBean getFromURI(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith("uni://"))
            return null;
        return scheme.getGeneratorUniverse().generateUniverse();
    }
    // util

    public static SectorBean findSector(UniverseBean uni, OrdBean o)
    {
        for (SectorBean s : uni.getSectors())
        {
            if (OrdLogic.within(o, s.getUpperBound(), s.getLowerBound()))
                return s;
        }
        return null;
    }

    public static SectorBean findSector(UniverseBean uni, String name)
    {
        for (SectorBean s : uni.getSectors())
        {
            String secName = s.getName();
            if (secName.equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public static SubSectorBean findSubSector(UniverseBean uni, OrdBean o)
    {
        try
        {
            return SectorLogic.findSubSector(findSector(uni, o), o);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    public static MainWorldBean findMainWorld(UniverseBean uni, OrdBean o)
    {
        try
        {
            return SubSectorLogic.findMainWorld(SectorLogic.findSubSector(findSector(uni, o), o), o);
        }
        catch (NullPointerException e)
        {
            return null;
        }
    }

    public static List<SectorBean> findSectors(UniverseBean uni, OrdBean upperBound, OrdBean lowerBound)
    {
        List<SectorBean> ret = new ArrayList<SectorBean>();
        for (SectorBean s : uni.getSectors())
        {
            if (OrdLogic.intersects(s.getUpperBound(), s.getLowerBound(), upperBound, lowerBound))
                ret.add(s);
        }
        return ret;
    }

    public static List<SubSectorBean> findSubSectors(UniverseBean uni, OrdBean upperBound, OrdBean lowerBound)
    {
        List<SubSectorBean> ret = new ArrayList<SubSectorBean>();
        for (SectorBean s : findSectors(uni, upperBound, lowerBound))
        {
            List<SubSectorBean> sw = SectorLogic.findSubSectors(s, upperBound, lowerBound);
            ret.addAll(0, sw);
        }
        return ret;
    }

    public static List<MainWorldBean> findMainWorlds(UniverseBean uni, OrdBean upperBound, OrdBean lowerBound)
    {
        List<MainWorldBean> ret = new ArrayList<MainWorldBean>();
        for (SubSectorBean s : findSubSectors(uni, upperBound, lowerBound))
        {
            List<MainWorldBean> sw = SubSectorLogic.findMainWorlds(s, upperBound, lowerBound);
            ret.addAll(0, sw);
        }
        return ret;
    }
    
    public static Object findByURI(UniverseBean uni, String uri)
    {
        if (uri.startsWith("sec://"))
        {
            return findSector(uni, OrdLogic.parseString(uri.substring(6)));
        }
        else if (uri.startsWith("sub://"))
        {
            return findSubSector(uni, OrdLogic.parseString(uri.substring(6)));
        }
        else if (uri.startsWith("mw://"))
        {
            return findMainWorld(uni, OrdLogic.parseString(uri.substring(5)));
        }
        else if (uri.startsWith("uni://"))
        {
            return uni;
        }
        return null;
    }


}
