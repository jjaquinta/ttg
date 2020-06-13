package jo.ttg.gen;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;

public interface IGenSubSector
{
    public SubSectorBean generateSubSector(OrdBean ords);
}