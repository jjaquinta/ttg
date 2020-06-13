package jo.util.utils.obj;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WeakValueHashMap<K,V> implements Map<K,V>
{
    private Map<K,WeakReference<V>> mMap;
    
    public WeakValueHashMap()
    {
        mMap = new HashMap<K,WeakReference<V>>();
    }
    
    private void cleanup()
    {
        for (Iterator<K> i = mMap.keySet().iterator(); i.hasNext(); )
        {
            Object key = i.next();
            WeakReference<V> ref = mMap.get(key);
            if ((ref != null) && (ref.get() == null))
                i.remove();
        }
    }

    public void clear()
    {
        mMap.clear();
    }

    public boolean containsKey(Object key)
    {
        cleanup();
        return get(key) != null;
    }

    public boolean containsValue(Object value)
    {
        for (Iterator<WeakReference<V>> i = mMap.values().iterator(); i.hasNext(); )
        {
            WeakReference<V> ref = i.next();
            if (ref.get() == value)
                return true;
        }
        return false;
    }

    public Set<java.util.Map.Entry<K, V>> entrySet()
    {
        Map<K,V> map = new HashMap<K,V>();
        for (K k : mMap.keySet())
            map.put(k, get(k));
        return map.entrySet();
    }

    public V get(Object key)
    {
        WeakReference<V> ref = mMap.get(key);
        if (ref == null)
            return null;
        if (ref.get() != null)
            return ref.get();
        mMap.remove(key);
        return null;
    }

    public boolean isEmpty()
    {
        cleanup();
        return mMap.isEmpty();
    }

    public Set<K> keySet()
    {
        cleanup();
        return mMap.keySet();
    }

    public V put(K key, V value)
    {
        mMap.put(key, new WeakReference<V>(value));
        return value;
    }

    public void putAll(Map<? extends K, ? extends V> t)
    {
        for (Iterator<? extends K> i = t.keySet().iterator(); i.hasNext(); )
        {
            K key = i.next();
            V val = t.get(key);
            put(key, val);
        }
    }

    public V remove(Object key)
    {
        WeakReference<V> ref = mMap.remove(key);
        if (ref != null)
            return ref.get();
        return null;
    }

    public int size()
    {
        cleanup();
        return mMap.size();
    }

    public Collection<V> values()
    {
        List<V> ret = new ArrayList<V>();
        for (Iterator<WeakReference<V>> i = mMap.values().iterator(); i.hasNext(); )
        {
            WeakReference<V> ref = i.next();
            if (ref.get() != null)
                ret.add(ref.get());
        }
        return ret;
    }
}
