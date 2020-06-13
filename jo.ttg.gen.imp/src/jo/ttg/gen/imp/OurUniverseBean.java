package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.uni.UniverseBean;

public class OurUniverseBean extends UniverseBean
{
    ImpGenScheme   scheme;

    public OurUniverseBean(ImpGenScheme _scheme)
    {
        super();
        scheme = _scheme;
    }

    public SectorBean findSector(OrdBean o)
    {
        OrdBean sec = new OrdBean(o);
        scheme.nearestSec(sec);
        return ensureSector(sec);
    }

    public List<SectorBean> findSectors(OrdBean upperBound, OrdBean lowerBound)
    {
        return ensureSectors(upperBound, lowerBound);
    }

    private List<SectorBean> ensureSectors(OrdBean upperBound, OrdBean lowerBound)
    {
        List<SectorBean> ret = new ArrayList<SectorBean>();
        OrdBean ub = new OrdBean(upperBound);
        scheme.nearestSec(ub);
        OrdBean lb = new OrdBean(lowerBound);
        scheme.nearestSec(lb);
        long lx = Math.min(ub.getX(), lb.getX());
        long ux = Math.max(ub.getX(), lb.getX());
        long ly = Math.min(ub.getY(), lb.getY());
        long uy = Math.max(ub.getY(), lb.getY());
        long lz = Math.min(ub.getZ(), lb.getZ());
        long uz = Math.max(ub.getZ(), lb.getZ());
        for (long x = lx; x < ux; x += 32)
            for (long y = ly; y < uy; y += 40)
                for (long z = lz; z < uz; z++)
                    ret.add(ensureSector(new OrdBean(x, y, z)));
        return ret;
    }

    private Map<OrdBean,SectorBean> sectorCache = new HashMap<OrdBean,SectorBean>();

    private SectorBean ensureSector(OrdBean o)
    {
        SectorBean sec = sectorCache.get(o);
        if (sec == null)
        {
            SectorBean[] oldUni = getSectors();
            SectorBean[] newUni;
            if (oldUni.length < 40)
            {
                newUni = new SectorBean[oldUni.length + 1];
                System.arraycopy(oldUni, 0, newUni, 1, oldUni.length);
            }
            else
            {
                newUni = new SectorBean[oldUni.length];
                System.arraycopy(oldUni, 0, newUni, 1, oldUni.length - 1);
            }
            sec = scheme.getGeneratorSector().generateSector(o);
            newUni[0] = sec;
            sectorCache.put(o, newUni[0]);
            setSectors(newUni);
        }
        return sec;
    }
}
