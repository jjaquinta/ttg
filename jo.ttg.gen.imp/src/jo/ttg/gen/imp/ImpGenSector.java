package jo.ttg.gen.imp;


import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.IGenSector;
import jo.ttg.logic.RandLogic;

public class ImpGenSector implements IGenSector
{
    protected ImpGenScheme   mScheme;

    public ImpGenSector(ImpGenScheme _scheme)
    {
        mScheme = _scheme;
    }

    public SectorBean generateSector(OrdBean ords)
    {
        SectorBean sec = newSectorBean();
        OrdBean ub = new OrdBean(ords);
        mScheme.nearestSec(ub);
        OrdBean size = mScheme.getSectorSize();
        OrdBean lb = new OrdBean(ub.getX() + size.getX(), ub.getY() + size.getY(), ub.getZ() + size.getZ());
        sec.setUpperBound(ub);
        sec.setLowerBound(lb);
        RandBean r = new RandBean();
        RandLogic.setMagic(r, mScheme.getXYZSeed(ords, ImpGenScheme.R_SECTOR), RandBean.LANG_MAGIC);
        sec.setName(mScheme.getGeneratorLanguage().generatePlaceName(ub, lb, "", r));
        return sec;
    }

    public SectorBean newSectorBean()
    {
        return new OurSectorBean(mScheme);
    }

}