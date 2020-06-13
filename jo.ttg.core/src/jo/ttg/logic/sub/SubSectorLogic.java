/*
 * Created on Sep 25, 2005
 *
 */
package jo.ttg.logic.sub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.LinkBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.uni.UniverseLogic;

public class SubSectorLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static SubSectorBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith("sub://"))
            return null;
        String sOrd = uri.substring(6);
        if (sOrd.indexOf(",") >= 0)
        {
            OrdBean o = OrdLogic.parseString(sOrd);
            return scheme.getGeneratorSubSector().generateSubSector(o);
        }
        else
        {
            int idx = sOrd.indexOf("/");
            if (idx < 0)
                return null;
            SectorBean sec = UniverseLogic.findSector(scheme.getGeneratorUniverse().generateUniverse(), sOrd.substring(0, idx));
            if (sec == null)
                return null;
            return SectorLogic.findSubSector(sec, sOrd.substring(idx+1));
        }
    }

    public static SubSectorBean getFromOrds(OrdBean o)
    {
        return getFromOrds(null, o);
    }

    public static SubSectorBean getFromOrds(IGenScheme scheme, OrdBean o)
    {
        if (scheme == null)
            scheme = SchemeLogic.getDefaultScheme();
        return scheme.getGeneratorSubSector().generateSubSector(o);
    }

    // util

    public static MainWorldBean findMainWorld(SubSectorBean sub, OrdBean o)
    {
        for (MainWorldBean s : sub.getMainWorlds())
        {
            if (o.equals(s.getOrds()))
                return s;
        }
        return null;
    }

    public static MainWorldBean findMainWorld(SubSectorBean sub, String name)
    {
        for (MainWorldBean s : sub.getMainWorlds())
        {
            if (s.getName().equalsIgnoreCase(name))
                return s;
        }
        return null;
    }

    public static List<MainWorldBean> findMainWorlds(SubSectorBean sub, OrdBean upperBound, OrdBean lowerBound)
    {
        List<MainWorldBean> ret = new ArrayList<MainWorldBean>();
        for (MainWorldBean s : sub.getMainWorlds())
        {
            if (OrdLogic.within(s.getOrds(), upperBound, lowerBound))
                ret.add(s);
        }
        return ret;
    }

    public static List<SubSectorBean> findSubSectors(IGenScheme scheme, OrdBean upperBound, OrdBean lowerBound)
    {
        
        List<SubSectorBean> ret = new ArrayList<SubSectorBean>();
        SubSectorBean ub = getFromOrds(scheme, upperBound);
        SubSectorBean lb = getFromOrds(scheme, lowerBound);
        long dx = ub.getLowerBound().getX() - ub.getUpperBound().getX();
        long dy = ub.getLowerBound().getY() - ub.getUpperBound().getY();
        for (Iterator<OrdBean> i = OrdLogic.getIterator2D(ub.getUpperBound(), lb.getLowerBound(), dx, dy); i.hasNext(); )
        {
            SubSectorBean sub = getFromOrds(scheme, i.next());
            ret.add(sub);
        }
        return ret;
    }
    
    public static String getUpperBoundDesc(SubSectorBean sub)
    {
        return OrdLogic.getShortNum(sub.getUpperBound());
    }

    public static MainWorldBean[] getMainWorlds(SubSectorBean sub)
    {
        return sub.getMainWorlds();
    }


    public static double getPopulation(SubSectorBean subSector)
    {
        return MainWorldLogic.getPopulation(subSector.getMainWorlds());
    }

    public static List<LinkBean> getLinks(IGenScheme scheme, OrdBean ords)
    {
        SubSectorBean sub = getFromOrds(scheme, ords);
        List<LinkBean> links = new ArrayList<LinkBean>();
        for (LinkBean link : sub.getLinks())
            if (link.getOrigin().equals(ords) || link.getDestination().equals(ords))
                links.add(link);
        return links;
    }

    public static List<LinkBean> getLinks(IGenScheme scheme, OrdBean upperBound, OrdBean lowerBound)
    {
        List<LinkBean> links = new ArrayList<LinkBean>();
        for (SubSectorBean sub : findSubSectors(scheme, upperBound, lowerBound))
            for (LinkBean link : sub.getLinks())
                if (OrdLogic.isWithin(link.getOrigin(), upperBound, lowerBound)
                        || OrdLogic.isWithin(link.getDestination(), upperBound, lowerBound))
                            links.add(link);
        return links;
    }

}
