package jo.ttg.gen.tm;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.IGenSector;
import jo.ttg.logic.uni.UniverseLogic;

class TMGenSector implements IGenSector
{
    TMGenScheme   mScheme;

    public TMGenSector(TMGenScheme scheme)
    {
        mScheme = scheme;
    }

    public SectorBean generateSector(OrdBean ords)
    {
    	return UniverseLogic.findSector(mScheme.mUniverse, ords);
   	}
}