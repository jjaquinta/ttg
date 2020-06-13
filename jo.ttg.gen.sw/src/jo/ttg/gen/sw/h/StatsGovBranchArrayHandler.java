package jo.ttg.gen.sw.h;

import org.json.simple.FromJSONLogic;
import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONArray;

import jo.ttg.beans.sys.StatsGovBranchBean;

public class StatsGovBranchArrayHandler implements IFromJSONHandler
{
    private Class<?> mObjectClass = StatsGovBranchBean.class;
    private Class<?> mArrayClass = (new StatsGovBranchBean[0]).getClass();

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
        StatsGovBranchBean[] ret = new StatsGovBranchBean[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            ret[i] = (StatsGovBranchBean)FromJSONLogic.fromJSON(arr.get(i), mObjectClass);
        return ret;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        throw new IllegalStateException("Not handled");
    }

}
