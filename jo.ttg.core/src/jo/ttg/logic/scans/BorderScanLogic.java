package jo.ttg.logic.scans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.scans.BorderScanBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.uni.UniverseLogic;
import jo.util.utils.IProgressMon;
import jo.util.utils.obj.StringUtils;

public class BorderScanLogic
{
    public static void scan(BorderScanBean data, IProgressMon pm)
    {
        if (data.getScope() instanceof SubSectorBean)
        {
            SubSectorBean sub = (SubSectorBean)data.getScope();
            pm.beginTask("Border Scan - "+sub.getName(), 1);
            doSubSector(pm, data, sub);
        }
        else if (data.getScope() instanceof SectorBean)
        {
            SectorBean sec = (SectorBean)data.getScope();
            pm.beginTask("Border Scan - "+sec.getName(), 16);
            doSector(pm, data, sec);
        }
        else
        {
            doUniverse(pm, data);
        }
    }

    private static void doUniverse(IProgressMon pm, BorderScanBean data)
    {
        UniverseBean uni = data.getScheme().getGeneratorUniverse().generateUniverse();
        OrdBean upperBound = new OrdBean(-10000, -10000, -10000);
        OrdBean lowerBound = new OrdBean(10000, 10000, 10000);
        List<SectorBean> sectors = UniverseLogic.findSectors(uni, upperBound, lowerBound);
        pm.beginTask("Border Scan - universe", sectors.size()*16);
        for (SectorBean sector : sectors)
        {            
            doSector(pm, data, sector);
            if (pm.isCanceled())
                break;
        }
    }

    private static void doSector(IProgressMon pm, BorderScanBean data, SectorBean sec)
    {
        for (SubSectorBean sub : sec.getSubSectors())
        {
            pm.setTaskName(sec.getName());
            doSubSector(pm, data, sub);
            if (pm.isCanceled())
                break;
        }
    }

    private static void doSubSector(IProgressMon pm, BorderScanBean data, SubSectorBean sub)
    {
        for (MainWorldBean mw : sub.getMainWorlds())
        {
            if (!isCandidate(mw, data.getNativeCriteria()))
                continue;
            String al = mw.getPopulatedStats().getAllegiance();
            List<MainWorldBean> nearby = SchemeLogic.getWorldsWithin(data.getScheme(), mw.getOrds(), data.getRadius());
            Map<String,List<MainWorldBean>> otherNations = new HashMap<String, List<MainWorldBean>>();
            for (MainWorldBean other : nearby)
                if (!al.equals(other.getPopulatedStats().getAllegiance()) && isCandidate(other, data.getAlienCriteria()))
                {
                    List<MainWorldBean> otherNationWorlds = otherNations.get(other.getPopulatedStats().getAllegiance());
                    if (otherNationWorlds == null)
                    {
                        otherNationWorlds = new ArrayList<MainWorldBean>();
                        otherNations.put(other.getPopulatedStats().getAllegiance(), otherNationWorlds);
                    }
                    otherNationWorlds.add(other);
                }
            if (otherNations.size() == 0)
                continue;
            List<MainWorldBean> borderBases = data.getBorderWorlds().get(al);
            if (borderBases == null)
            {
                borderBases = new ArrayList<MainWorldBean>();
                data.getBorderWorlds().put(al, borderBases);
            }
            borderBases.add(mw);
            data.getBorderWith().put(mw, otherNations);
            if (pm.isCanceled())
                break;
        }
        pm.worked(1);
    }

    private static boolean isCandidate(MainWorldBean mw, String criteria)
    {
        if (StringUtils.isTrivial(criteria))
            return true;
        if (BorderScanBean.CRITERIA_NAVAL_BASE.equals(criteria))
            return mw.getPopulatedStats().isNavalBase();
        throw new IllegalArgumentException("Unknown criteria '"+criteria+"'");
    }

}
