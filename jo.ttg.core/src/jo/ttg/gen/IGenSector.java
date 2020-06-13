package jo.ttg.gen;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;

public interface IGenSector
{
    public SectorBean generateSector(OrdBean ords);
}