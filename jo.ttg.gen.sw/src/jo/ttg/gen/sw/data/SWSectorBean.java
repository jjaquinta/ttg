package jo.ttg.gen.sw.data;

import org.json.simple.IJSONAble;
import org.json.simple.JSONObject;

import jo.ttg.beans.sec.SectorBean;

public class SWSectorBean extends SectorBean implements IJSONAble
{

    @Override
    public JSONObject toJSON()
    {
        throw new IllegalStateException("Not implemented yet");
    }

    @Override
    public void fromJSON(JSONObject o)
    {
        throw new IllegalStateException("Not implemented yet");
    }

}
