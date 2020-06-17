package jo.ttg.gen.util;


import java.io.File;
import java.io.IOException;

import org.json.simple.FromJSONLogic;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenCargo;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.gen.IGenPassengers;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.IGenSector;
import jo.ttg.gen.IGenSubSector;
import jo.ttg.gen.IGenSurface;
import jo.ttg.gen.IGenSystem;
import jo.ttg.gen.IGenUniverse;
import jo.ttg.gen.util.h.BlahArrayHandler;
import jo.ttg.gen.util.h.BodyHandler;
import jo.ttg.gen.util.h.SystemHandler;
import jo.util.utils.obj.StringUtils;

public class GenSchemePersistant implements IGenScheme
{
    static
    {
        FromJSONLogic.addHandler(new BlahArrayHandler());
        FromJSONLogic.addHandler(new SystemHandler());
        FromJSONLogic.addHandler(new BodyHandler());
        ToJSONLogic.addHandler(new BodyHandler());
    }
    private IGenScheme mRoot;
    private File    mDataDir;
    private GenSectorPersistant mGeneratorSector;
    private GenSubSectorPersistant mGeneratorSubSector;
    private GenMainWorldPersistant mGeneratorMainWorld;
    private GenSystemPersistant mGeneratorSystem;
    private GenSurfacePersistant mGeneratorSurface;
    
    public GenSchemePersistant(File dataDir, IGenScheme root)
    {
        mRoot = root;
        mDataDir = dataDir;
        mGeneratorSector = new GenSectorPersistant(getGeneratorSector());
        mGeneratorSubSector = new GenSubSectorPersistant(getGeneratorSubSector());
        mGeneratorMainWorld = new GenMainWorldPersistant(getGeneratorMainWorld());
        mGeneratorSystem = new GenSystemPersistant(getGeneratorSystem());
        mGeneratorSurface = new GenSurfacePersistant(getGeneratorSurface());
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
    
    protected File getSurfaceFile(String uri)
    {
        String name = uri.replace('/', '_');
        int o = name.indexOf(':');
        if (o > 0)
            name = name.substring(o+1); // strip body:
        File f = new File(mDataDir, "SURF"+name+".json");
        return f;
    }

    public void save(SectorBean sec)
    {
        mGeneratorSector.save(sec);
    }

    public void deleteSector(OrdBean ords)
    {
        mGeneratorSector.delete(ords);
    }
    
    public void save(SubSectorBean sub)
    {
        mGeneratorSubSector.save(sub);
    }
    
    public void deleteSubSector(OrdBean ords)
    {
        mGeneratorSubSector.delete(ords);
    }
    
    public void save(MainWorldBean mw)
    {
        mGeneratorMainWorld.save(mw);
    }
    
    public void deleteMainWorld(OrdBean ords)
    {
        mGeneratorMainWorld.delete(ords);
    }
    
    public void insertMainWorld(OrdBean ords)
    {
        mGeneratorMainWorld.insert(ords);
    }
    
    public void save(SystemBean sys)
    {
        mGeneratorSystem.save(sys);
    }

    @Override
    public OrdBean getSectorSize()
    {
        return mRoot.getSectorSize();
    }

    @Override
    public IGenUniverse getGeneratorUniverse()
    {
        return mRoot.getGeneratorUniverse();
    }

    @Override
    public IGenSector getGeneratorSector()
    {
        return mGeneratorSector;
    }

    @Override
    public IGenSubSector getGeneratorSubSector()
    {
        return mRoot.getGeneratorSubSector();
    }

    @Override
    public IGenMainWorld getGeneratorMainWorld()
    {
        return mGeneratorMainWorld;
    }

    @Override
    public IGenSystem getGeneratorSystem()
    {
        return mGeneratorSystem;
    }

    @Override
    public IGenSurface getGeneratorSurface()
    {
        return mGeneratorSurface;
    }

    @Override
    public IGenPassengers getGeneratorPassengers()
    {
        return mRoot.getGeneratorPassengers();
    }

    @Override
    public IGenCargo getGeneratorCargo()
    {
        return mRoot.getGeneratorCargo();
    }

    @Override
    public IGenLanguage getGeneratorLanguage()
    {
        return mRoot.getGeneratorLanguage();
    }

    @Override
    public void nearestSec(OrdBean o)
    {
        mRoot.nearestSec(o);
    }

    @Override
    public void nearestSub(OrdBean o)
    {
        mRoot.nearestSub(o);
    }

    @Override
    public double distance(OrdBean o1, OrdBean o2)
    {
        return mRoot.distance(o1, o2);
    }
    
    class GenSectorPersistant implements IGenSector
    {
        private IGenSector mRoot;
        
        public GenSectorPersistant(IGenSector root)
        {
            mRoot = root;
        }

        public SectorBean generateSector(OrdBean ords)
        {
            File secFile = getSectorFile(ords);
            if (!secFile.exists())
                return mRoot.generateSector(ords);
            try
            {
                JSONObject json = JSONUtils.readJSON(secFile);
                SectorBean sec = (SectorBean)newSectorBean();
                FromJSONLogic.fromJSONInto(json, sec);
                return sec;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return mRoot.generateSector(ords);
            }
        }
        
        @Override
        public SectorBean newSectorBean()
        {
            return mRoot.newSectorBean();
        }
        
        public void save(SectorBean sec)
        {
            try
            {
                File secFile = getSectorFile(sec.getUpperBound());
                JSONObject json = (JSONObject)ToJSONLogic.toJSON(sec);
                JSONUtils.writeJSON(secFile, json);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        public void delete(OrdBean ords)
        {
            File secFile = getSectorFile(ords);
            if (secFile.exists())
                secFile.delete();
        }
        
    }
    
    class GenSubSectorPersistant implements IGenSubSector
    {
        private IGenSubSector mRoot;
        
        public GenSubSectorPersistant(IGenSubSector root)
        {
            mRoot = root;
        }

        public SubSectorBean generateSubSector(OrdBean ords)
        {
            File subFile = getSubSectorFile(ords);
            if (!subFile.exists())
                return mRoot.generateSubSector(ords);
            try
            {
                JSONObject json = JSONUtils.readJSON(subFile);
                SubSectorBean sub = (SubSectorBean)newSubSectorBean();
                FromJSONLogic.fromJSONInto(json, sub);
                return sub;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return mRoot.generateSubSector(ords);
            }
        }
        
        @Override
        public SubSectorBean newSubSectorBean()
        {
            return mRoot.newSubSectorBean();
        }
        
        public void save(SubSectorBean sub)
        {
            try
            {
                File subFile = getSubSectorFile(sub.getUpperBound());
                JSONObject json = (JSONObject)ToJSONLogic.toJSON(sub);
                JSONUtils.writeJSON(subFile, json);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        public void delete(OrdBean ords)
        {
            File subFile = getSubSectorFile(ords);
            if (subFile.exists())
                subFile.delete();
        }
        
    }
    
    class GenMainWorldPersistant implements IGenMainWorld
    {
        private IGenMainWorld mRoot;
        
        public GenMainWorldPersistant(IGenMainWorld root)
        {
            mRoot = root;
        }

        public MainWorldBean generateMainWorld(OrdBean ords)
        {
            File mwFile = getMainWorldFile(ords);
            if (!mwFile.exists())
                return mRoot.generateMainWorld(ords);
            try
            {
                JSONObject json = JSONUtils.readJSON(mwFile);
                MainWorldBean sub = (MainWorldBean)newMainWorldBean();
                FromJSONLogic.fromJSONInto(json, sub);
                return sub;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return mRoot.generateMainWorld(ords);
            }
        }
        
        @Override
        public MainWorldBean newMainWorldBean()
        {
            return mRoot.newMainWorldBean();
        }
        
        public void save(MainWorldBean mw)
        {
            try
            {
                File mwFile = getMainWorldFile(mw.getOrds());
                JSONObject json = (JSONObject)ToJSONLogic.toJSON(mw);
                JSONUtils.writeJSON(mwFile, json);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        public void delete(OrdBean ords)
        {
            File mwFile = getMainWorldFile(ords);
            if (mwFile.exists())
                mwFile.delete();
        }
        
        public void insert(OrdBean ords)
        {
            MainWorldBean mw = newMainWorldBean();
            mw.setOrds(ords);
            save(mw);
        }
        
    }
    
    class GenSystemPersistant implements IGenSystem
    {
        private IGenSystem mRoot;
        
        public GenSystemPersistant(IGenSystem root)
        {
            mRoot = root;
        }

        public SystemBean generateSystem(OrdBean ords)
        {
            File sysFile = getSystemFile(ords);
            if (!sysFile.exists())
                return mRoot.generateSystem(ords);
            try
            {
                JSONObject json = JSONUtils.readJSON(sysFile);
                SystemBean sys = (SystemBean)newSystemBean();
                FromJSONLogic.fromJSONInto(json, sys);
                setSystem(sys, null, sys.getSystemRoot());
                return sys;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return mRoot.generateSystem(ords);
            }
        }
        
        private void setSystem(SystemBean sys, BodyBean parent, BodyBean body)
        {
            body.setSystem(sys);
            body.setPrimary(parent);
            for (BodyBean c : body.getSatelites())
                setSystem(sys, body, c);
        }
        
        @Override
        public SystemBean newSystemBean()
        {
            return mRoot.newSystemBean();
        }
        
        public void save(SystemBean sys)
        {
            try
            {
                File mwFile = getSystemFile(sys.getOrds());
                JSONObject json = (JSONObject)ToJSONLogic.toJSON(sys);
                JSONUtils.writeJSON(mwFile, json);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        public void delete(OrdBean ords)
        {
            File mwFile = getSystemFile(ords);
            if (mwFile.exists())
                mwFile.delete();
        }        
    }
    
    class GenSurfacePersistant implements IGenSurface
    {
        private IGenSurface mRoot;
        
        public GenSurfacePersistant(IGenSurface root)
        {
            mRoot = root;
        }

        public SurfaceBean generateSurface(BodyWorldBean body)
        {
            File surfFile = getSurfaceFile(body.getURI());
            if (!surfFile.exists())
                return mRoot.generateSurface(body);
            try
            {
                JSONObject json = JSONUtils.readJSON(surfFile);
                SurfaceBean surf = (SurfaceBean)newSurfaceBean();
                FromJSONLogic.fromJSONInto(json, surf);
                surf.setBody(body);
                return surf;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return mRoot.generateSurface(body);
            }
        }
        
        @Override
        public SurfaceBean newSurfaceBean()
        {
            return mRoot.newSurfaceBean();
        }
        
        public void save(SurfaceBean surf)
        {
            try
            {
                File mwFile = getSurfaceFile(surf.getBody().getURI());
                JSONObject json = (JSONObject)ToJSONLogic.toJSON(surf);
                JSONUtils.writeJSON(mwFile, json);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        
        public void delete(BodyWorldBean world)
        {
            File surfFile = getSurfaceFile(world.getURI());
            if (surfFile.exists())
                surfFile.delete();
        }        
    }
}