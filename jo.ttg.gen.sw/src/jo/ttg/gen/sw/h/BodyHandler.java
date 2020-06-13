package jo.ttg.gen.sw.h;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import jo.ttg.beans.sys.BodyBean;

public class BodyHandler implements IFromJSONHandler, IToJSONHandler
{
    private Set<String> mSkips = new HashSet<>();
    
    public BodyHandler()
    {
        mSkips.add("primary");
        mSkips.add("system");
    }
    
    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if ((json instanceof JSONObject) && hint.isAssignableFrom(BodyBean.class))
            return true;
        return false;
    }

    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        try
        {
            String className = ((JSONObject)json).getString(BeanHandler.BEAN_CLASS);
            Class<?> objClass = Class.forName(className);
            Object bean = objClass.newInstance();
            BeanHandler.doFromJSONInto(json, bean);
            return bean;
        }
        catch (InstantiationException | IllegalAccessException | ClassNotFoundException e)
        {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        BeanHandler.doFromJSONInto(json, bean);
    }

    @Override
    public boolean isHandler(Object o)
    {
        if (o instanceof BodyBean)
            return true;
        return false;
    }

    @Override
    public Object toJSON(Object o)
    {
        return BeanHandler.doToJSON(o, mSkips);
    }
}
