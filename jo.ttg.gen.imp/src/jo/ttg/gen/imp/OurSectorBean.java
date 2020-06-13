package jo.ttg.gen.imp;


import java.util.Iterator;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.logic.OrdLogic;

public class OurSectorBean extends SectorBean
{
    ImpGenScheme   scheme;

    public OurSectorBean(ImpGenScheme _scheme)
    {
        super();
        scheme = _scheme;
    }

    boolean initSubs = false;

    synchronized protected void doInitSubs()
    {
        if (initSubs)
            return;
        IGenSubSector gsub = scheme.getGeneratorSubSector();
        SubSectorBean[] subs = new SubSectorBean[16];
        int o = 0;
        for (int x = 0; x < 32; x += 8)
            for (int y = 0; y < 40; y += 10)
            {
                OrdBean subOrds = new OrdBean(getUpperBound());
                OrdLogic.incr(subOrds, x, y, 0);
                subs[o] = gsub.generateSubSector(subOrds);
                //subs[o].setParent(this);
                o++;
            }
        this.setSubSectors(subs);
        initSubs = true;
    }

    public SubSectorBean[] getSubSectors()
    {
        doInitSubs();
        return super.getSubSectors();
    }
    public SubSectorBean getSubSectors(int index)
    {
        doInitSubs();
        return super.getSubSectors(index);
    }
    public Iterator<SubSectorBean> getSubSectorsIterator()
    {
        doInitSubs();
        return super.getSubSectorsIterator();
    }

}