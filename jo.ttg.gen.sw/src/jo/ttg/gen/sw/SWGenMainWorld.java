package jo.ttg.gen.sw;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.imp.ImpGenMainWorld;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.logic.RandLogic;
import jo.util.geom3d.Point3D;

public class SWGenMainWorld extends ImpGenMainWorld
{
    public static final long SW_MAGIC = 32768L;

    public SWGenMainWorld(SWGenScheme _scheme)
    {
        super(_scheme);
    }

    public MainWorldBean generateMainWorld(OrdBean ords)
    {
        return generateMainWorld(ords, false);
    }
        
    public MainWorldBean generateMainWorld(OrdBean ords, boolean force)
    {
        if (!mScheme.exists(ords) && !force)
            return null;
        SWMainWorldBean mw = (SWMainWorldBean)super.generateMainWorld(ords, force);
        long localSeed = mScheme.getXYZSeed(ords, ImpGenScheme.R_LOCAL);
        //long subSeed   = scheme.getXYZSeed(ords, GenScheme.R_SUBSECTOR);
        //long secSeed   = scheme.getXYZSeed(ords, GenScheme.R_SECTOR);
        RandBean r = new RandBean();
        RandLogic.setMagic(r, localSeed, SW_MAGIC);
        generateExtras(mw, r);
        return mw;
    }
    
    private void generateExtras(SWMainWorldBean mw, RandBean r)
    {
        OrdBean o = mw.getOrds();
        Point3D fine = new Point3D(o.getX() + RandLogic.rnd(r), o.getY() + RandLogic.rnd(r), o.getZ() + RandLogic.rnd(r));
        mw.setOrdsFine(fine);
    }

    public MainWorldBean newMainWorldBean()
    {
        return new SWMainWorldBean();
    }
}
