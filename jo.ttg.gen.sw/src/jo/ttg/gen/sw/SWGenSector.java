package jo.ttg.gen.sw;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenSector;
import jo.ttg.gen.sw.data.SWSectorBean;

public class SWGenSector extends ImpGenSector
{

    public SWGenSector(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public SectorBean generateSector(OrdBean ords)
    {
        return super.generateSector(ords);
    }
    
    @Override
    public SectorBean newSectorBean()
    {
        return new SWSectorBean();
    }
}
