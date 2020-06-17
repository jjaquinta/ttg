package jo.ttg.gen.sw;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenSubSector;
import jo.ttg.gen.sw.data.SWSubSectorBean;

public class SWGenSubSector extends ImpGenSubSector
{

    public SWGenSubSector(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public SubSectorBean generateSubSector(OrdBean ords)
    {
        return super.generateSubSector(ords);
    }
    
    @Override
    public SubSectorBean newSubSectorBean()
    {
        return new SWSubSectorBean();
    }
}
