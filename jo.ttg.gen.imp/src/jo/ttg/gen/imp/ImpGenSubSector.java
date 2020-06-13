package jo.ttg.gen.imp;


import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.logic.RandLogic;

public class ImpGenSubSector implements IGenSubSector
{
    protected ImpGenScheme   mScheme;

    public ImpGenSubSector(ImpGenScheme _scheme)
    {
        mScheme = _scheme;
    }

    public SubSectorBean generateSubSector(OrdBean ords)
    {
        SubSectorBean sub = newSubSectorBean();
        OrdBean ub = new OrdBean(ords);
        mScheme.nearestSub(ub);
        OrdBean size = mScheme.getSubSectorSize();
        OrdBean lb = new OrdBean(ub.getX() + size.getX(), ub.getY() + size.getY(), ub.getZ() + size.getZ());
        sub.setUpperBound(ub);
        sub.setLowerBound(lb);
        RandBean r = new RandBean();
        RandLogic.setMagic(r, mScheme.getXYZSeed(ords, ImpGenScheme.R_SUBSECTOR), RandBean.LANG_MAGIC);
        sub.setName(mScheme.getGeneratorLanguage().generatePlaceName(ub, lb, "", r));
        return sub;
    }

    public SubSectorBean newSubSectorBean()
    {
        return new OurSubSectorBean(mScheme);
    }
}