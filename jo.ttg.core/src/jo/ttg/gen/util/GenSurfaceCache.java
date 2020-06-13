package jo.ttg.gen.util;

import java.util.HashMap;
import java.util.Map;

import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.gen.IGenSurface;
import jo.util.utils.obj.WeakValueHashMap;

public class GenSurfaceCache implements IGenSurface
{
    private IGenSurface                     mRootGenerator;
    private Map<BodyWorldBean, SurfaceBean> mFixedCache;
    private Map<BodyWorldBean, SurfaceBean> mTransientCache;

    public GenSurfaceCache(IGenSurface rootGenerator)
    {
        mRootGenerator = rootGenerator;
        mFixedCache = new HashMap<>();
        mTransientCache = new WeakValueHashMap<>();
    }

    public SurfaceBean generateSurface(BodyWorldBean bwb)
    {
        if (mFixedCache.containsKey(bwb))
            return mFixedCache.get(bwb);
        if (mTransientCache.containsKey(bwb))
            return mTransientCache.get(bwb);
        SurfaceBean ret = mRootGenerator.generateSurface(bwb);
        mTransientCache.put(bwb, ret);
        return ret;
    }

    public void setSurface(BodyWorldBean bwb, SurfaceBean bean)
    {
        mFixedCache.put(bwb, bean);
    }
}
