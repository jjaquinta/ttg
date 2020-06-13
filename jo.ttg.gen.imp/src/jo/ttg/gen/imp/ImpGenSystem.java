package jo.ttg.gen.imp;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenSystem;
import jo.ttg.logic.RandLogic;
import jo.util.beans.Cache;

public class ImpGenSystem extends ImpGenSystemBase implements IGenSystem
{
    private Cache	mCache;
    private ImpGenWorld mGenWorld;
    private ImpGenGiant mGenGiant;
    private ImpGenToids mGenToids;
    private ImpGenStar mGenStar;

    public ImpGenSystem(ImpGenScheme _scheme)
    {
        super(_scheme, null);
        mCache = new Cache();
        mGenWorld = new ImpGenWorld(getScheme(), this);
        mGenGiant = new ImpGenGiant(getScheme(), this);
        mGenToids = new ImpGenToids(getScheme(), this);
        mGenStar = new ImpGenStar(getScheme(), this);
    }

    protected SystemBean generateSystemFromCache(OrdBean ords)
    {
        return (SystemBean)mCache.getFromCache(ords);
    }

    public SystemBean generateSystem(OrdBean ords)
    {
        SystemBean sys = generateSystemFromCache(ords);
        if (sys != null)
            return sys;
        MainWorldBean mw = getScheme().getGeneratorMainWorld().generateMainWorld(ords);
        if (mw == null)
        	return null;
        sys = newSystemBean();

        long localSeed = getScheme().getXYZSeed(ords, ImpGenScheme.R_LOCAL);
        //long subSeed   = getScheme().getXYZSeed(ords, GenScheme.R_SUBSECTOR);
        //long secSeed   = getScheme().getXYZSeed(ords, GenScheme.R_SECTOR);
        RandBean r = new RandBean();
        RandLogic.setMagic(r, localSeed, RandBean.DETAIL_MAGIC);
        sys.setSeed(r.getSeed());

        sys.setName(mw.getName());
        sys.setOrds(ords);
        BodyBean root = generateRoot(sys, mw, r);
        sys.setSystemRoot(root);
        
        mCache.addToCache(ords, sys);
        return sys;
    }

    public SystemBean newSystemBean()
    {
        return new SystemBean();
    }

    BodyBean generateRoot(SystemBean sys, MainWorldBean mw, RandBean r)
    {
        BodyStarBean star = mGenStar.generateStar(null, 0, mw, r);
        // make names unique
        BodyBean[] b = star.getAllSatelites().toArray(new BodyBean[0]);
        for (int i = 0; i < b.length - 1; i++)
        {
            String iName = b[i].getName();
            for (int j = i + 1; j < b.length; j++)
            {
                if (iName.equals(b[j].getName()))
                {
                    generateName(b[j], mw, r);
                }
            }
            if (b[i].getSeed() == 0)
                b[i].setSeed(r.getSeed());
            b[i].setSystem(sys);
        }
        if (b[b.length - 1].getSeed() == 0)
            b[b.length - 1].setSeed(r.getSeed());
        b[b.length - 1].setSystem(sys);
        return star;
    }

    public ImpGenGiant getGenGiant()
    {
        return mGenGiant;
    }
    public void setGenGiant(ImpGenGiant genGiant)
    {
        mGenGiant = genGiant;
    }
    public ImpGenStar getGenStar()
    {
        return mGenStar;
    }
    public void setGenStar(ImpGenStar genStar)
    {
        mGenStar = genStar;
    }
    public ImpGenToids getGenToids()
    {
        return mGenToids;
    }
    public void setGenToids(ImpGenToids genToids)
    {
        mGenToids = genToids;
    }
    public ImpGenWorld getGenWorld()
    {
        return mGenWorld;
    }
    public void setGenWorld(ImpGenWorld genWorld)
    {
        mGenWorld = genWorld;
    }
}