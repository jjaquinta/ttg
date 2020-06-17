package jo.ttg.gen.util.h;

import java.lang.reflect.Array;
import java.util.HashSet;
import java.util.Set;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONArray;

import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sys.AnimalBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.CityBean;
import jo.ttg.beans.sys.StatsGovBranchBean;

public class BlahArrayHandler implements IFromJSONHandler
{
    private Set<Class<?>> mObjectClasses = new HashSet<>();
    
    public BlahArrayHandler()
    {
        mObjectClasses.add(AnimalBean.class);
        mObjectClasses.add(CityBean.class);
        mObjectClasses.add(BodyBean.class);
        mObjectClasses.add(StarDeclBean.class);
        mObjectClasses.add(StatsGovBranchBean.class);
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONArray))
            return false;
        if (!hint.isArray())
            return false;
        for (Class<?> objectClass : mObjectClasses)
            if (hint.getComponentType().isAssignableFrom(objectClass))
                return true;
        return false;
    }

    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        JSONArray arr = (JSONArray)json;
        Object ret = Array.newInstance(hint.getComponentType(), arr.size());
        for (int i = 0; i < arr.size(); i++)
            Array.set(ret, i, FromJSONLogic.fromJSON(arr.get(i), hint.getComponentType()));
        return ret;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        throw new IllegalStateException("Not handled");
    }

}
