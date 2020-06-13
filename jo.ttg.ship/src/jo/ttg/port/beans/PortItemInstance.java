package jo.ttg.port.beans;

import java.util.ArrayList;
import java.util.List;

public class PortItemInstance
{
    private PortItemBean    mItem;
    private int             mInstance = 1;
    
    // constructors
    public PortItemInstance()
    {
    }
    
    public PortItemInstance(PortItemBean item)
    {
        mItem = item;
    }
    
    public PortItemInstance(PortItemBean item, int instance)
    {
        mItem = item;
        mInstance = instance;
    }
    
    // utilities
    
    public static List<List<PortItemInstance>> split(List<PortItemInstance> items, int parts)
    {
        List<List<PortItemInstance>> ret = new ArrayList<List<PortItemInstance>>();
        int itemsPerPart = items.size()/parts;
        int o = 0;
        for (int i = 0; i < parts; i++)
        {
            List<PortItemInstance> r = new ArrayList<>();
            if (i + 1 == parts)
                itemsPerPart = items.size() - o;
            for (int j = 0; j < itemsPerPart; j++)
                r.add(items.get(o++));
            ret.add(r);
        }
        return ret;
    }
    
    // getters and setters
    public PortItemBean getItem()
    {
        return mItem;
    }
    public void setItem(PortItemBean item)
    {
        mItem = item;
    }
    public int getInstance()
    {
        return mInstance;
    }
    public void setInstance(int instance)
    {
        mInstance = instance;
    }
}
