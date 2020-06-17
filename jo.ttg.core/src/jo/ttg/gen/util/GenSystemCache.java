package jo.ttg.gen.util;

import java.util.HashMap;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenSystem;
import jo.util.utils.obj.WeakValueHashMap;

public class GenSystemCache implements IGenSystem
{
    private IGenSystem              mRootGenerator;
    private Map<OrdBean,SystemBean> mFixedCache;
    private Map<OrdBean,SystemBean> mTransientCache;
    
    public GenSystemCache(IGenSystem rootGenerator)
    {
        mRootGenerator = rootGenerator;
        mFixedCache = new HashMap<OrdBean,SystemBean>();
        mTransientCache = new WeakValueHashMap<OrdBean,SystemBean>();
    }

    public SystemBean generateSystem(OrdBean ords)
    {
        if (mFixedCache.containsKey(ords))
            return mFixedCache.get(ords);
        if (mTransientCache.containsKey(ords))
            return mTransientCache.get(ords);
        SystemBean ret = mRootGenerator.generateSystem(ords);
        mTransientCache.put(ords, ret);
        return ret;
    }

    public void setSystem(SystemBean bean)
    {
        mFixedCache.put(bean.getOrds(), bean);
    }

    @Override
    public SystemBean newSystemBean()
    {
        return mRootGenerator.newSystemBean();
    }
}
