//class: Bean
/**
 * The Bean class.
 * 
 * @author uoakeju
 * @version Jun 17, 2004
 */
package jo.util.beans;

public abstract class DataBean extends Bean
{
    private Object[]    mData;
    
    public void setData(String data)
    {
        if (mData == null)
            mData = new Object[1];
        mData[0] = data;
    }
    
    public Object getData()
    {
        if ((mData == null) || (mData.length == 0))
            return null;
        return mData[0];
    }
    
    public void setData(String key, Object val)
    {
        if (mData == null)
        {
            mData = new Object[3];
            mData[1] = key;
            mData[2] = val;
        }
        else
        {
            for (int i = 1; i < mData.length; i += 2)
                if (key.equals(mData[i]))
                {
                    mData[i + 1] = val;
                    return;
                }
            Object[] newData = new Object[mData.length + 2];
            System.arraycopy(mData, 0, newData, 0, mData.length);
            newData[newData.length - 2] = key;
            newData[newData.length - 1] = val;
            mData = newData;
        }
    }
    
    public Object getData(String key)
    {
        if (mData == null)
            return null;
        for (int i = 1; i < mData.length; i++)
            if (key.equals(mData[i]))
                return mData[i+1];
        return null;
    }
}