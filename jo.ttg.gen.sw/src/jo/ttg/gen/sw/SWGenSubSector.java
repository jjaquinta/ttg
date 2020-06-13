package jo.ttg.gen.sw;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.gen.imp.ImpGenSubSector;
import jo.ttg.gen.sw.data.SWSubSectorBean;

public class SWGenSubSector extends ImpGenSubSector
{

    public SWGenSubSector(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public SubSectorBean generateSubSector(OrdBean ords)
    {
        File subFile = ((SWGenScheme)mScheme).getSubSectorFile(ords);
        if (!subFile.exists())
            return super.generateSubSector(ords);
        try
        {
            JSONObject json = JSONUtils.readJSON(subFile);
            SWSubSectorBean sub = (SWSubSectorBean)newSubSectorBean();
            sub.fromJSON(json);
            return sub;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return super.generateSubSector(ords);
        }
    }
    
    @Override
    public SubSectorBean newSubSectorBean()
    {
        return new SWSubSectorBean();
    }
    
    public void save(SubSectorBean sub)
    {
        try
        {
            File subFile = ((SWGenScheme)mScheme).getSubSectorFile(sub.getUpperBound());
            JSONObject json = ((SWSubSectorBean)sub).toJSON();
            JSONUtils.writeJSON(subFile, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public void delete(OrdBean ords)
    {
        File subFile = ((SWGenScheme)mScheme).getSubSectorFile(ords);
        if (subFile.exists())
            subFile.delete();
    }
}
