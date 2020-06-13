package jo.ttg.ship.beans.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.util.beans.PCSBean;
import jo.util.utils.obj.IntegerUtils;

public class ShipScanBean extends PCSBean
{
    private String      mURI;
    private Map<Object,Object>     mMetadata = new HashMap<>();
    private int mVolume;
    private int mConfiguration;
    private List<PlanItem> mItems = new ArrayList<PlanItem>();
    
    // constructors
    
    public ShipScanBean()
    {
    }

    public ShipScanBean(JSONObject json)
    {
        fromJSON(json);
    }
    
    // I/O

    @SuppressWarnings("unchecked")
    public JSONObject toJSON()
    {
        JSONObject md = new JSONObject();
        for (Object key : mMetadata.keySet())
        {
            Object val = mMetadata.get(key);
            md.put(key.toString(), val.toString());
        }
        JSONArray items = new JSONArray();
        for (PlanItem item : mItems)
            items.add(item.toJSON());

        JSONObject json = new JSONObject();
        if (mURI != null)
            json.put("uri", mURI);
        json.put("volume", mVolume);
        json.put("configuration", mConfiguration);
        if (md.size() > 0)
            json.put("metadata", md);
        if (items.size() > 0)
            json.put("items", items);
        return json;
    }
    
    public void fromJSON(JSONObject json)
    {
        mURI = json.getString("uri");
        if (json.containsKey("metadata"))
        {
            mMetadata = new HashMap<>();
            JSONObject md = (JSONObject)json.get("metadata");
            for (String key : md.keySet())
                mMetadata.put(key, md.get(key));
        }
        mVolume = IntegerUtils.parseInt(json.get("volume"));
        mConfiguration = IntegerUtils.parseInt(json.get("configuration"));
        if (json.containsKey("items"))
        {
            JSONArray items = (JSONArray)json.get("items");
            for (int i = 0; i < items.size(); i++)
                mItems.add(new PlanItem((JSONObject)items.get(i)));
        }
    }
    
    // getters and setters

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public Map<Object, Object> getMetadata()
    {
        return mMetadata;
    }

    public void setMetadata(Map<Object, Object> metadata)
    {
        mMetadata = metadata;
    }

    public int getVolume()
    {
        return mVolume;
    }

    public void setVolume(int volume)
    {
        mVolume = volume;
    }

    public int getConfiguration()
    {
        return mConfiguration;
    }

    public void setConfiguration(int configuration)
    {
        mConfiguration = configuration;
    }

    public List<PlanItem> getItems()
    {
        return mItems;
    }

    public void setItems(List<PlanItem> items)
    {
        mItems = items;
    }
}
