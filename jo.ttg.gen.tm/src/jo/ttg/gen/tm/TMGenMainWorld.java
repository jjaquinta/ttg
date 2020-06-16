package jo.ttg.gen.tm;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.logic.uni.UniverseLogic;

public class TMGenMainWorld implements IGenMainWorld
{
    private TMGenScheme mScheme;
    
    public TMGenMainWorld(TMGenScheme scheme)
    {
        mScheme = scheme;
    }

    public MainWorldBean generateMainWorld(OrdBean ords)
    {
        return UniverseLogic.findMainWorld(mScheme.mUniverse, ords);
    }

}
