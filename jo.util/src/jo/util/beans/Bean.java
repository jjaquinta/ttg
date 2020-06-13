//class: Bean
/**
 * The Bean class.
 * 
 * @author uoakeju
 * @version Jun 17, 2004
 */
package jo.util.beans;

import jo.util.utils.BeanUtils;

public abstract class Bean implements Cloneable
{
    private long mOID;

    //constructor: Bean
    protected Bean()
    {
        mOID = -1;
    }

    public Bean(Bean copy)
    {
        set(copy);
    }

    //method: getOID
    /**
     * @return
     */
    public long getOID()
    {
        return mOID;
    }

    //method: setOID
    /**
     * @param l
     */
    public void setOID(long l)
    {
        mOID = l;
    }

    //method: toString
    public String toString()
    {
        return "Bean: " + String.valueOf(getOID());
    }

    public void set(Bean b)
    {
        BeanUtils.set(this, b);
    }
    
    public Object clone() throws CloneNotSupportedException
    {
        Object ret = super.clone();
        ((Bean)ret).set(this);
        return ret;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return super.equals(obj);
        return BeanUtils.equals(this, obj);
    }

}