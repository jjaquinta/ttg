package jo.ttg.gen;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.SystemBean;

public interface IGenSystem
{
    public SystemBean generateSystem(OrdBean ords);
    public SystemBean newSystemBean();
}