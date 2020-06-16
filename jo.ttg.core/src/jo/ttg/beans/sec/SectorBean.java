package jo.ttg.beans.sec;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.PropertiesBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.util.beans.Bean;

public class SectorBean extends Bean implements PropertiesBean, URIBean
{
    public String toString()
    {
        return mName;
    }
    
    // Name
    private java.lang.String mName;
    public java.lang.String getName()
    {
        return mName;
    }
    public void setName(java.lang.String v)
    {
        mName = v;
    }

    // UpperBound
    private OrdBean mUpperBound;
    public OrdBean getUpperBound()
    {
        return mUpperBound;
    }
    public void setUpperBound(OrdBean v)
    {
        mUpperBound = v;
    }

    // LowerBound
    private OrdBean mLowerBound;
    public OrdBean getLowerBound()
    {
        return mLowerBound;
    }
    public void setLowerBound(OrdBean v)
    {
        mLowerBound = v;
    }

    // SubSectors
    private List<SubSectorBean> mSubSectors;
    
    public SubSectorBean[] getSubSectors()
    {
        return mSubSectors.toArray(new SubSectorBean[0]);
    }
    public void setSubSectors(SubSectorBean[] v)
    {
        mSubSectors.clear();
        for (int i = 0; i < v.length; i++)
            mSubSectors.add(v[i]);
    }
    public SubSectorBean getSubSectors(int index)
    {
        return mSubSectors.get(index);
    }
    public void setSubSectors(int index, SubSectorBean v)
    {
        mSubSectors.set(index, v);
    }
    public Iterator<SubSectorBean> getSubSectorsIterator()
    {
        return mSubSectors.iterator();
    }

    // Properties
    private HashBean mProperties;
    public HashBean getProperties()
    {
        return mProperties;
    }
    public void setProperties(HashBean v)
    {
        mProperties = v;
    }

    // constructor
    public SectorBean()
    {
        mName = new java.lang.String();
        mUpperBound = new OrdBean();
        mLowerBound = new OrdBean();
        mSubSectors = new ArrayList<SubSectorBean>();
        mProperties = new HashBean();
    }

    public java.lang.String getURI()
    {
        return "sec://"+getUpperBound();
    }
    
    // utilities
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SectorBean)
            return getURI().equals(((SectorBean)obj).getURI());
        return super.equals(obj);
    }
}
