package jo.ttg.beans.uni;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.PropertiesBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.sec.SectorBean;
import jo.util.beans.Bean;

public class UniverseBean extends Bean implements PropertiesBean, URIBean
{
    // Name
    private String mName;
    public String getName()
    {
        return mName;
    }
    public void setName(String v)
    {
        mName = v;
    }

    // Sectors
    private List<SectorBean> mSectors;
    public SectorBean[] getSectors()
    {
        return mSectors.toArray(new SectorBean[0]);
    }
    public void setSectors(SectorBean[] v)
    {
        mSectors.clear();
        for (int i = 0; i < v.length; i++)
        	mSectors.add(v[i]);
    }
    public SectorBean getSectors(int index)
    {
        return mSectors.get(index);
    }
    public void setSectors(int index, SectorBean v)
    {
        mSectors.set(index, v);
    }
    public Iterator<SectorBean> getSectorsIterator()
    {
        return mSectors.iterator();
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
    public UniverseBean()
    {
        mName = new String();
        mSectors = new ArrayList<SectorBean>();
        mProperties = new HashBean();
    }

    public String getURI()
    {
        return "uni://";
    }
}
