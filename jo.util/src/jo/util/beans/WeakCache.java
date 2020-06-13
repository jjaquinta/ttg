package jo.util.beans;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeakCache<KeyType,ObjectType>
{
    private Map<KeyType, Object>    mStore;
    private Map<KeyType, Long>      mAccess;
    private long                    mTimeout;
    
    public WeakCache()
    {
        mStore = new HashMap<KeyType, Object>();
        mAccess = new HashMap<KeyType, Long>();
        mTimeout = 0;
    }
    
    public void setParameters(Map<Object,Object> params)
    {
    }
    
    public void put(KeyType key, ObjectType val)
    {
        synchronized (this)
        {
            if (mTimeout < 0)
                mStore.put((KeyType)key, (new WeakReference<ObjectType>((ObjectType)val))); // always weak reference
            else
                mStore.put(key, val);
            mAccess.put(key, System.currentTimeMillis());
        }
    }

    @SuppressWarnings("unchecked")
    public ObjectType get(KeyType key)
    {
        synchronized(this)
        {
            if (!mStore.containsKey(key))
                return null;
            Object v = mStore.get(key);
            if (v instanceof WeakReference<?>)
            {
                WeakReference<ObjectType> wr = (WeakReference<ObjectType>)v;
                ObjectType val = wr.get();
                if (val != null)
                {
                    mAccess.put(key, System.currentTimeMillis());
                    if (mTimeout >= 0)
                        mStore.put(key, val); // promote to stong ref
                    return val;
                }
                else
                {
                    remove(key);
                    return null;
                }
            }
            else
            {
                mAccess.put(key, System.currentTimeMillis());
                return (ObjectType)v;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public Collection<ObjectType> getAll()
    {
        Collection<ObjectType> all = new ArrayList<ObjectType>();
        Object[] keys;
        synchronized(this)
        {
            keys = mStore.keySet().toArray();
        }
        for (Object k : keys)
        {
            ObjectType o = get((KeyType)k);
            if (o != null)
                all.add(o);
        }
        return all;
    }
    
    public void remove(KeyType key)
    {
        synchronized (this)
        {
            mStore.remove(key);
            mAccess.remove(key);
        }
    }

    public long getTimeout()
    {
        return mTimeout;
    }

    public void setTimeout(long timeout)
    {
        if ((mTimeout <= 0) && (timeout > 0))
        {
            Thread job = new Thread(new CacheJob(), "Cache Maintainer");
            job.start();
        }
        mTimeout = timeout;
    }

    public Map<KeyType, Object> getStore()
    {
        return mStore;
    }

    public void setStore(Map<KeyType, Object> store)
    {
        mStore = store;
    }
    
    class CacheJob implements Runnable
    {
        @Override
        public void run()
        {
            while (mTimeout > 0)
            {
                try
                {
                    Thread.sleep(mTimeout);
                }
                catch (InterruptedException e)
                {
                }
                expireReferences();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void expireReferences()
    {
        Object[] keys = mStore.keySet().toArray();
        long expiryDate = System.currentTimeMillis() - getTimeout();
        for (Object k : keys)
            synchronized (WeakCache.this)
            {
                if (!mStore.containsKey(k))
                    continue;
                Object v = mStore.get(k);
                if (v instanceof WeakReference<?>)
                {
                    WeakReference<ObjectType> wr = (WeakReference<ObjectType>)v;
                    ObjectType val = wr.get();
                    if (val == null)
                        mStore.remove(k); // remove expired reference
                }
                else
                {
                    Long access = mAccess.get(k);
                    if ((access == null) || (access < expiryDate))
                    {
                        mStore.put((KeyType)k, (new WeakReference<ObjectType>(
                                (ObjectType)v))); // demote to weak reference
                        mAccess.remove(k);
                    }
                }
            }
    }

    protected void cleanCacheHarsh()
    {
        System.out.println("HARSH CLEANUP!");
        Object[] keys = mStore.keySet().toArray();
        for (Object key : keys)
        {
            Object v = mStore.get(key);
            if (v instanceof WeakReference<?>)
            {
                if (!mAccess.containsKey(key))
                    mStore.remove(key);
            }
        }
        System.gc();
    }
    
    public String dump(List<Object> contents)
    {
        StringBuffer sb = new StringBuffer();
        synchronized (WeakCache.this)
        {
            sb.append("<b>Timeout</b>="+mTimeout+"</br>");
            sb.append("<table>");
            sb.append("<tr><td><b>Key</b></td><td><b>Reference</b></td><td><b>Access</b></td></tr>");
            Date d = new Date();
            for (KeyType key : mStore.keySet())
            {
                sb.append("<tr><td>");
                sb.append(key.toString());
                sb.append("</td><td>");
                Object v = mStore.get(key);
                if (v instanceof WeakReference<?>)
                    sb.append("weak");
                else
                    sb.append("strong");
                sb.append("</td><td>");
                if (mAccess.containsKey(key))
                {
                    d.setTime(mAccess.get(key));
                    sb.append(d.toString());
                }
                else
                    sb.append("No last access");
                sb.append("</td></tr>");
                if (contents != null)
                    contents.add(v);
            }
            sb.append("</table>");
        }
        return sb.toString();
    }
}

