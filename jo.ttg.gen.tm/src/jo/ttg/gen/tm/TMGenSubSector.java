package jo.ttg.gen.tm;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.logic.uni.UniverseLogic;

class TMGenSubSector implements IGenSubSector
{
    TMGenScheme   mScheme;

    public TMGenSubSector(TMGenScheme scheme)
    {
        mScheme = scheme;
    }

    public SubSectorBean generateSubSector(OrdBean ords)
    {
    	return UniverseLogic.findSubSector(mScheme.mUniverse, ords);
    }

    @Override
    public SubSectorBean newSubSectorBean()
    {
        return new SubSectorBean();
    }
}