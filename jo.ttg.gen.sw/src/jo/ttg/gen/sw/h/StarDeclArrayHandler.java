package jo.ttg.gen.sw.h;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONArray;

import jo.ttg.beans.mw.StarDeclBean;

public class StarDeclArrayHandler implements IFromJSONHandler
{
    private Class<?> mObjectClass = StarDeclBean.class;
    private Class<?> mArrayClass = (new StarDeclBean[0]).getClass();

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if ((json instanceof JSONArray) && hint.isAssignableFrom(mArrayClass))
            return true;
        return false;
    }

    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        JSONArray arr = (JSONArray)json;
        StarDeclBean[] ret = new StarDeclBean[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            ret[i] = (StarDeclBean)FromJSONLogic.fromJSON(arr.get(i), mObjectClass);
        return ret;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        throw new IllegalStateException("Not handled");
    }

}
