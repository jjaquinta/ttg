package jo.util.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupMap<K,V>
{
    private Map<K, List<V>> mMap;
    
    public GroupMap()
    {
        mMap = new HashMap<K, List<V>>();
    }
    
    public void add(K key, V val)
    {
        get(key).add(val);
    }
    
    public void remove(K key, V val)
    {
        get(key).remove(val);
    }
    
    public List<V> get(K key)
    {
        List<V> vals = mMap.get(key);
        if (vals == null)
        {
            vals = new ArrayList<V>();
            mMap.put(key, vals);
        }
        return vals;
    }
    
    public Map<K, List<V>> getMap()
    {
        return mMap;
    }
}
