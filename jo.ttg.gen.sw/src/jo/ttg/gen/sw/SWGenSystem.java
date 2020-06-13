package jo.ttg.gen.sw;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.imp.ImpGenSystemEx;
import jo.ttg.gen.sw.data.SWSystemBean;

public class SWGenSystem extends ImpGenSystemEx
{
    public SWGenSystem(SWGenScheme scheme)
    {
        super(scheme);
    }

    @Override
    public SystemBean generateSystem(OrdBean ords)
    {
        File sysFile = ((SWGenScheme)mScheme).getSystemFile(ords);
        if (!sysFile.exists())
            return super.generateSystem(ords);
        try
        {
            JSONObject json = JSONUtils.readJSON(sysFile);
            SWSystemBean sys = (SWSystemBean)newSystemBean();
            sys.fromJSON(json);
            return sys;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return super.generateSystem(ords);
        }
    }
    
    public SystemBean newSystemBean()
    {
        return new SWSystemBean();
    }
    
    public void save(SystemBean sys)
    {
        try
        {
            File sysFile = ((SWGenScheme)mScheme).getSystemFile(sys.getOrds());
            JSONObject json = ((SWSystemBean)sys).toJSON();
            JSONUtils.writeJSON(sysFile, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void delete(OrdBean ords)
    {
        File mwFile = ((SWGenScheme)mScheme).getSystemFile(ords);
        if (mwFile.exists())
            mwFile.delete();
    }
}
