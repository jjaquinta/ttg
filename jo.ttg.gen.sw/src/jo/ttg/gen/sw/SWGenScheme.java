package jo.ttg.gen.sw;


import java.io.File;

import org.json.simple.FromJSONLogic;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ImpGenCargo;
import jo.ttg.gen.imp.ImpGenLanguage;
import jo.ttg.gen.imp.ImpGenPassengers;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenSurface;
import jo.ttg.gen.sw.h.BlahArrayHandler;
import jo.ttg.gen.sw.h.BodyHandler;
import jo.ttg.gen.sw.h.SystemHandler;
import jo.ttg.logic.RandLogic;
import jo.util.utils.obj.StringUtils;

public class SWGenScheme extends ImpGenScheme
{
    private File    mDataDir;
    
    public SWGenScheme(File dataDir)
    {
        FromJSONLogic.addHandler(new BlahArrayHandler());
        FromJSONLogic.addHandler(new SystemHandler());
        FromJSONLogic.addHandler(new BodyHandler());
        ToJSONLogic.addHandler(new BodyHandler());
        mDataDir = dataDir;
        mSectorSize = new OrdBean(16, 16, 16);
        mSubSectorSize = new OrdBean(8, 8, 8);
        mGeneratorUniverse = new SWGenUniverse(this);
        mGeneratorSector = new SWGenSector(this);
        mGeneratorSubSector = new SWGenSubSector(this);
        mGeneratorMainWorld = new SWGenMainWorld(this);
        mGeneratorSystem = new SWGenSystem(this);
        mGeneratorSurface = new ImpGenSurface(this);
        mGeneratorPassengers = new ImpGenPassengers(this);
        mGeneratorCargo = new ImpGenCargo(this);
        mGeneratorLanguage = new ImpGenLanguage(this);
    }

    protected String toFileOrd(long x)
    {
        String s = Integer.toHexString((int)x);
        if (s.length() > 4)
            s = s.substring(s.length() - 4);
        else
            s = StringUtils.zeroPrefix(s, 4);
        return s;
    }
    
    protected String toFileName(OrdBean o)
    {
        return toFileOrd(o.getX())+toFileOrd(o.getY())+toFileOrd(o.getZ());
    }
    
    protected File getSectorFile(OrdBean o)
    {
        File f = new File(mDataDir, "SEC"+toFileName(o)+".json");
        return f;
    }
    
    protected File getSubSectorFile(OrdBean o)
    {
        File f = new File(mDataDir, "SUB"+toFileName(o)+".json");
        return f;
    }
    
    protected File getMainWorldFile(OrdBean o)
    {
        File f = new File(mDataDir, "MW"+toFileName(o)+".json");
        return f;
    }
    
    protected File getSystemFile(OrdBean o)
    {
        File f = new File(mDataDir, "SYS"+toFileName(o)+".json");
        return f;
    }

    public boolean exists(OrdBean o)
    {
        File f = getMainWorldFile(o);
        if (f.exists())
            return f.length() > 0;
        else
        {
            RandBean r = new RandBean();
            int d;
            d = getDensity(o);
            RandLogic.setMagic(r, getXYZSeed(o, R_LOCAL), RandBean.EXIST_MAGIC);
            return (RandLogic.rand(r) % 100) < d;
        }
    }
    protected int getDensity(OrdBean ords)
    {
        double x2 = (double) ords.getX() / (8.0 * 4.0) * Math.PI;
        double x3 = (double) ords.getX() / (8.0 * 2.0) * Math.PI;
        double y2 = (double) ords.getY() / (8.0 * 4.0) * Math.PI;
        double y3 = (double) ords.getY() / (8.0 * 2.0) * Math.PI;
        double z2 = (double) ords.getY() / (8.0 * 4.0) * Math.PI;
        double z3 = (double) ords.getX() / (8.0 * 2.0) * Math.PI;
        double tot =
                + Math.sin(x2)
                + Math.cos(x3)
                + Math.sin(y2)
                + Math.cos(y3)
                + Math.sin(z2)
                + Math.cos(z3);
        tot = (tot / 12.0) / 2 + 0.25;
        return (int) (tot * 100);
    }

    public long getXYZSeed(OrdBean ords, int r)
    {
        long ltmp;

        ords = new OrdBean(ords);
        if (r == R_SUBSECTOR)
        {
            nearestSub(ords);
        }
        else if (r == R_SECTOR)
        {
            nearestSec(ords);
        }
        ltmp = make_seed(ords.getX(), ords.getY(), ords.getZ());
        ltmp += r;
        return ltmp;
    }

    private long make_seed(long x, long y, long z)
    {
        // hash bits together
        long ret;
        ret = 0L;
        if ((x & 0x00000001L) != 0) ret |= 0x01;
        if ((y & 0x00000001L) != 0) ret |= 0x02;
        if ((z & 0x00000001L) != 0) ret |= 0x04;
        if ((x & 0x00000002L) != 0) ret |= 0x08;
        if ((y & 0x00000002L) != 0) ret |= 0x10;
        if ((z & 0x00000002L) != 0) ret |= 0x20;
        if ((x & 0x00000004L) != 0) ret |= 0x40;
        if ((y & 0x00000004L) != 0) ret |= 0x80;
        if ((z & 0x00000004L) != 0) ret |= 0x100;
        if ((x & 0x00000008L) != 0) ret |= 0x200;
        if ((y & 0x00000008L) != 0) ret |= 0x400;
        if ((z & 0x00000008L) != 0) ret |= 0x800;
        if ((x & 0x00000010L) != 0) ret |= 0x1000;
        if ((y & 0x00000010L) != 0) ret |= 0x2000;
        if ((z & 0x00000010L) != 0) ret |= 0x4000;
        if ((x & 0x00000020L) != 0) ret |= 0x8000;
        if ((y & 0x00000020L) != 0) ret |= 0x10000;
        if ((z & 0x00000020L) != 0) ret |= 0x20000;
        if ((x & 0x00000040L) != 0) ret |= 0x40000;
        if ((y & 0x00000040L) != 0) ret |= 0x80000;
        if ((z & 0x00000040L) != 0) ret |= 0x100000;
        if ((x & 0x00000080L) != 0) ret |= 0x200000;
        if ((y & 0x00000080L) != 0) ret |= 0x400000;
        if ((z & 0x00000080L) != 0) ret |= 0x800000;
        if ((x & 0x00000100L) != 0) ret |= 0x1000000;
        if ((y & 0x00000100L) != 0) ret |= 0x2000000;
        if ((z & 0x00000100L) != 0) ret |= 0x4000000;
        if ((x & 0x00000200L) != 0) ret |= 0x8000000;
        if ((y & 0x00000200L) != 0) ret |= 0x10000000;
        if ((z & 0x00000200L) != 0) ret |= 0x20000000;
        if ((x & 0x00000400L) != 0) ret |= 0x40000000;
        if ((y & 0x00000400L) != 0) ret |= 0x80000000;
        return ret;
    }

    public void save(SectorBean sec)
    {
        ((SWGenSector)mGeneratorSector).save(sec);
    }
    
    public void save(SubSectorBean sub)
    {
        ((SWGenSubSector)mGeneratorSubSector).save(sub);
    }
    
    public void save(MainWorldBean mw)
    {
        ((SWGenMainWorld)mGeneratorMainWorld).save(mw);
    }
}