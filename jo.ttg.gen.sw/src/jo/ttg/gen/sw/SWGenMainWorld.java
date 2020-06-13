package jo.ttg.gen.sw;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

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
        File mwFile = ((SWGenScheme)mScheme).getMainWorldFile(ords);
        if (!mwFile.exists())
        {
            SWMainWorldBean mw = (SWMainWorldBean)super.generateMainWorld(ords, force);
            long localSeed = mScheme.getXYZSeed(ords, ImpGenScheme.R_LOCAL);
            //long subSeed   = scheme.getXYZSeed(ords, GenScheme.R_SUBSECTOR);
            //long secSeed   = scheme.getXYZSeed(ords, GenScheme.R_SECTOR);
            RandBean r = new RandBean();
            RandLogic.setMagic(r, localSeed, SW_MAGIC);
            generateExtras(mw, r);
            return mw;
        }
        if (mwFile.length() <= 2)
            return null;
        try
        {
            JSONObject json = JSONUtils.readJSON(mwFile);
            SWMainWorldBean mw = (SWMainWorldBean)newMainWorldBean();
            mw.fromJSON(json);
            return mw;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return super.generateMainWorld(ords);
        }
    }
    
    private void generateExtras(SWMainWorldBean mw, RandBean r)
    {
        OrdBean o = mw.getOrds();
        Point3D fine = new Point3D(o.getX() + RandLogic.rnd(r), o.getY() + RandLogic.rnd(r), o.getZ() + RandLogic.rnd(r));
        mw.setOrdsFine(fine);
    }
    
    public void save(MainWorldBean mw)
    {
        try
        {
            File mwFile = ((SWGenScheme)mScheme).getMainWorldFile(mw.getOrds());
            JSONObject json = ((SWMainWorldBean)mw).toJSON();
            JSONUtils.writeJSON(mwFile, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void erase(OrdBean ords)
    {
        try
        {
            File mwFile = ((SWGenScheme)mScheme).getMainWorldFile(ords);
            JSONObject json = new JSONObject();
            JSONUtils.writeJSON(mwFile, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public MainWorldBean insert(OrdBean ords)
    {
        MainWorldBean mw = generateMainWorld(ords, true);
        save(mw);
        return mw;
    }
    
    public void delete(OrdBean ords)
    {
        File mwFile = ((SWGenScheme)mScheme).getMainWorldFile(ords);
        if (mwFile.exists())
            mwFile.delete();
    }

    public MainWorldBean newMainWorldBean()
    {
        return new SWMainWorldBean();
    }
}
