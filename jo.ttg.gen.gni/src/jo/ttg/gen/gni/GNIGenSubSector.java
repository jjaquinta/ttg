package jo.ttg.gen.gni;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.logic.uni.UniverseLogic;

class GNIGenSubSector implements IGenSubSector
{
    GNIGenScheme   mScheme;

    public GNIGenSubSector(GNIGenScheme scheme)
    {
        mScheme = scheme;
    }

    public SubSectorBean generateSubSector(OrdBean ords)
    {
    	return UniverseLogic.findSubSector(mScheme.mUniverse, ords);
    }
}