package jo.ttg.gen.sw.h;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONArray;

import jo.ttg.beans.sys.AnimalBean;

public class AnimalArrayHandler implements IFromJSONHandler
{
    private Class<?> mObjectClass = AnimalBean.class;
    private Class<?> mArrayClass = (new AnimalBean[0]).getClass();

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
        AnimalBean[] ret = new AnimalBean[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            ret[i] = (AnimalBean)FromJSONLogic.fromJSON(arr.get(i), mObjectClass);
        return ret;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        throw new IllegalStateException("Not handled");
    }

}
