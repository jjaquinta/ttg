/*
 * Created on Jan 25, 2005
 *
 */
package jo.util.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jo
 *
 */
public class Cache
{
    private Map<Object,Object>		mIndex;
    private Map<Object,Object>		mReverseIndex;
    private List<Object>	mList;
    private int			mSize;
    
    public Cache(int size)
    {
        mSize = size;
        mIndex = new HashMap<Object, Object>(mSize + 1);
        mReverseIndex = new HashMap<Object, Object>(mSize + 1);
        mList = new ArrayList<Object>();
    }
    
    public Cache()
    {
        this(15);
    }
    
    public void addToCache(Object key, Object val)
    {
        if (mList.contains(val))
            return;
        mList.add(0, val);
        mIndex.put(key, val);
        mReverseIndex.put(val, key);
        if (mList.size() > mSize)
        {
            Object removeVal = mList.get(mSize);
            Object removeKey = mReverseIndex.get(removeVal);
            mList.remove(mSize);
            mIndex.remove(removeKey);
            mReverseIndex.remove(removeVal);
        }
    }
    
    public Object getFromCache(Object key)
    {
        return mIndex.get(key);
    }
}
