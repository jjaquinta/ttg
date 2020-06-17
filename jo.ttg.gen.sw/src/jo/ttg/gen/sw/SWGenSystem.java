package jo.ttg.gen.sw;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.imp.ImpGenSystemEx;
import jo.ttg.gen.sw.data.SWSystemBean;

public class SWGenSystem extends ImpGenSystemEx
{
    public SWGenSystem(SWGenScheme scheme)
    {
        super(scheme);
    }

    @Override
    public SystemBean generateSystem(OrdBean ords)
    {
        return super.generateSystem(ords);
    }
    
    public SystemBean newSystemBean()
    {
        return new SWSystemBean();
    }
}
