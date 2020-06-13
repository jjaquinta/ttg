package jo.ttg.gen.imp;


import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.ttg.gen.IGenUniverse;
import jo.ttg.logic.RandLogic;

public class ImpGenUniverse implements IGenUniverse
{
    ImpGenScheme   scheme;

    public ImpGenUniverse(ImpGenScheme _scheme)
    {
        scheme = _scheme;
    }

    public UniverseBean generateUniverse()
    {
        UniverseBean uni = newUniverseBean();
        RandBean r = new RandBean();
        RandLogic.setMagic(r, scheme.getXYZSeed(new OrdBean(), ImpGenScheme.R_UNIVERSE), RandBean.LANG_MAGIC);
        uni.setName(scheme.getGeneratorLanguage().generatePlaceName(null, null, "", r));
        return uni;
    }

    public UniverseBean newUniverseBean()
    {
        return new OurUniverseBean(scheme);
    }
}