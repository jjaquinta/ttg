package jo.ttg.beans;

import jo.util.beans.Bean;

public class OrdBean extends Bean implements URIBean
{
    // X
    private long mX;
    public long getX()
    {
        return mX;
    }
    public void setX(long v)
    {
        mX = v;
    }

    // Y
    private long mY;
    public long getY()
    {
        return mY;
    }
    public void setY(long v)
    {
        mY = v;
    }

    // Z
    private long mZ;
    public long getZ()
    {
        return mZ;
    }
    public void setZ(long v)
    {
        mZ = v;
    }


    // constructor
    public OrdBean()
    {
        mX = 0;
        mY = 0;
        mZ = 0;
    }

    /**
     * Construct from ords.
     * @param _x
     * @param _y
     * @param _z
     */
    public OrdBean(long _x, long _y, long _z)
    {
        mX = _x;
        mY = _y;
        mZ = _z;
    }

    /**
     * Copy constructor
     * @param o
     */
    public OrdBean(OrdBean o)
    {
        this(o.getX(), o.getY(), o.getZ());
    }

    // utils

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone()
    {
        return new OrdBean(getX(), getY(), getZ());
    }
    /**
     * Method equals.
     * @param _x
     * @param _y
     * @param _z
     * @return boolean
     */
    public boolean equals(long _x, long _y, long _z)
    {
        return (mX == _x) && (mY == _y) && (mZ == _z);
    }
    /**
     * Method equals.
     * @param o
     * @return boolean
     */
    public boolean equals(Object o)
    {
        if (this == o)
            return true;
        if (!(o instanceof OrdBean))
            return false;
    	OrdBean ord = (OrdBean)o;
        boolean ret = equals(ord.mX, ord.mY, ord.mZ);
        return ret;
    }
    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode()
    {
        long ret =
            (((mX & 0xfff) << 0) | ((mY & 0xfff) << 12) | ((mZ & 0xff) << 24));
        return (int) ret;
    }
    /**
     * Set by index.
     * @param i
     * @param v
     * @return OrdBean
     */
    public OrdBean set(int i, long v)
    {
        if (i == 0)
            mX = v;
        else if (i == 1)
            mY = v;
        else
            mZ = v;
        return this;
    }
    /**
     * Set by ords.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public OrdBean set(long _x, long _y, long _z)
    {
        mX = _x;
        mY = _y;
        mZ = _z;
        return this;
    }
    /**
     * Set by ord.
     * @param o
     * @return OrdBean
     */
    public OrdBean set(OrdBean o)
    {
        return set(o.mX, o.mY, o.mZ);
    }
    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[" + mX + "," + mY + "," + mZ + "]";
    }
    public String toURIString()
    {
        return mX + "," + mY + "," + mZ;
    }
    public String getURI()
    {
        return "ord://"+toURIString();
    }
}
