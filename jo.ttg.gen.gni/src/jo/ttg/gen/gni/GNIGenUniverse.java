package jo.ttg.gen.gni;

import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.IGenUniverse;

public class GNIGenUniverse implements IGenUniverse
{
    private GNIGenScheme   	mScheme;

    public GNIGenUniverse(GNIGenScheme scheme)
    {
        mScheme = scheme;
    }

    public UniverseBean generateUniverse()
    {
        return mScheme.mUniverse;
    }
}