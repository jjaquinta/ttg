package jo.ttg.gen.sw.h;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONArray;

import jo.ttg.beans.sys.BodyBean;

public class BodyArrayHandler implements IFromJSONHandler
{
    private Class<?> mObjectClass = BodyBean.class;
    private Class<?> mArrayClass = (new BodyBean[0]).getClass();

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
        BodyBean[] ret = new BodyBean[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            ret[i] = (BodyBean)FromJSONLogic.fromJSON(arr.get(i), mObjectClass);
        return ret;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        throw new IllegalStateException("Not handled");
    }

}
