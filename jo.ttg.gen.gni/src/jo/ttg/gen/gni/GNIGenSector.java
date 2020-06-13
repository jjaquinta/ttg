package jo.ttg.gen.gni;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.IGenSector;
import jo.ttg.logic.uni.UniverseLogic;

class GNIGenSector implements IGenSector
{
    GNIGenScheme   mScheme;

    public GNIGenSector(GNIGenScheme scheme)
    {
        mScheme = scheme;
    }

    public SectorBean generateSector(OrdBean ords)
    {
    	return UniverseLogic.findSector(mScheme.mUniverse, ords);
   	}
}