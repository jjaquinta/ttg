package jo.ttg.gen.sw;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenSector;
import jo.ttg.gen.sw.data.SWSectorBean;

public class SWGenSector extends ImpGenSector
{

    public SWGenSector(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public SectorBean generateSector(OrdBean ords)
    {
        File subFile = ((SWGenScheme)mScheme).getSectorFile(ords);
        if (!subFile.exists())
            return super.generateSector(ords);
        try
        {
            JSONObject json = JSONUtils.readJSON(subFile);
            SWSectorBean sub = (SWSectorBean)newSectorBean();
            sub.fromJSON(json);
            return sub;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return super.generateSector(ords);
        }
    }
    
    @Override
    public SectorBean newSectorBean()
    {
        return new SWSectorBean();
    }
    
    public void save(SectorBean sec)
    {
        try
        {
            File subFile = ((SWGenScheme)mScheme).getSectorFile(sec.getUpperBound());
            JSONObject json = ((SWSectorBean)sec).toJSON();
            JSONUtils.writeJSON(subFile, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void delete(OrdBean ords)
    {
        File subFile = ((SWGenScheme)mScheme).getSectorFile(ords);
        if (subFile.exists())
            subFile.delete();
    }

}
