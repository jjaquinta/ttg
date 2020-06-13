package jo.ttg.gen.util;

import java.util.HashMap;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenMainWorld;
import jo.util.utils.obj.WeakValueHashMap;

public class GenMainWorldCache implements IGenMainWorld
{
    private IGenMainWorld                 mRootGenerator;
    private Map<OrdBean,MainWorldBean>    mFixedCache;
    private Map<OrdBean,MainWorldBean>    mTransientCache;
    
    public GenMainWorldCache(IGenMainWorld rootGenerator)
    {
        mRootGenerator = rootGenerator;
        mFixedCache = new HashMap<OrdBean,MainWorldBean>();
        mTransientCache = new WeakValueHashMap<OrdBean,MainWorldBean>();
    }

    public MainWorldBean generateMainWorld(OrdBean ords)
    {
        MainWorldBean ret;
        if (mFixedCache.containsKey(ords))
        {
            ret = mFixedCache.get(ords);
            //System.out.println("<<GenMW from fixed "+MainWorldLogic.getExportLine(ret)+">>");
        }
        else if (mTransientCache.containsKey(ords))
        {
            ret = mTransientCache.get(ords);
            //System.out.println("<<GenMW from transient "+MainWorldLogic.getExportLine(ret)+">>");
        }
        else
        {
            ret = mRootGenerator.generateMainWorld(ords);
            mTransientCache.put(ords, ret);
            //System.out.println("<<GenMW generated "+MainWorldLogic.getExportLine(ret)+">>");
        }
        return ret;
    }

    public void setMainWorld(MainWorldBean bean)
    {
        mFixedCache.put(bean.getOrds(), bean);
    }

}
