package jo.ttg.gen.sw;

import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenUniverse;

public class SWGenUniverse extends ImpGenUniverse
{
    public SWGenUniverse(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public UniverseBean generateUniverse()
    {
        UniverseBean uni = newUniverseBean();
        uni.setName("Star Wars");
        return uni;
    }
}
