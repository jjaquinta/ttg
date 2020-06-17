package jo.ttg.gen;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;

public interface IGenMainWorld
{
    public MainWorldBean generateMainWorld(OrdBean ords);
    public MainWorldBean newMainWorldBean();
}