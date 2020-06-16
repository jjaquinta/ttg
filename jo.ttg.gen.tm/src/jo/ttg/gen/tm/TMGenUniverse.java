package jo.ttg.gen.tm;

import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.IGenUniverse;

public class TMGenUniverse implements IGenUniverse
{
    private TMGenScheme   	mScheme;

    public TMGenUniverse(TMGenScheme scheme)
    {
        mScheme = scheme;
    }

    public UniverseBean generateUniverse()
    {
        TMUniverseBean universe = mScheme.mUniverse;
        return universe;
    }
}