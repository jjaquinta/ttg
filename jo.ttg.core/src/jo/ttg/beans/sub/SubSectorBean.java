package jo.ttg.beans.sub;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.PropertiesBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.util.beans.Bean;

public class SubSectorBean extends Bean implements PropertiesBean, URIBean
{
    // utilities
    
    public String toString()
    {
        return mName;
    }
    
    // utilities
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof SectorBean)
            return getURI().equals(((SectorBean)obj).getURI());
        return super.equals(obj);
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

    // MainWorlds
    protected List<MainWorldBean> mMainWorlds;
    public MainWorldBean[] getMainWorlds()
    {
        return mMainWorlds.toArray(new MainWorldBean[0]);
    }
    public void setMainWorlds(MainWorldBean[] v)
    {
        mMainWorlds.clear();
        for (int i = 0; i < v.length; i++)
            mMainWorlds.add(v[i]);
    }
    public MainWorldBean getMainWorlds(int index)
    {
        return mMainWorlds.get(index);
    }
    public void setMainWorlds(int index, MainWorldBean v)
    {
        mMainWorlds.set(index, v);
    }
    public Iterator<MainWorldBean> getMainWorldsIterator()
    {
        return mMainWorlds.iterator();
    }

    // Links
    protected List<LinkBean> mLinks;
    public LinkBean[] getLinks()
    {
        return mLinks.toArray(new LinkBean[0]);
    }
    public void setLinks(LinkBean[] v)
    {
        mLinks.clear();
        for (int i = 0; i < v.length; i++)
            mLinks.add(v[i]);
    }
    public LinkBean getLinks(int index)
    {
        return mLinks.get(index);
    }
    public void setLinks(int index, LinkBean v)
    {
        mLinks.set(index, v);
    }
    public Iterator<LinkBean> getLinksIterator()
    {
        return mLinks.iterator();
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
    public SubSectorBean()
    {
        mName = new java.lang.String();
        mUpperBound = new OrdBean();
        mLowerBound = new OrdBean();
        mMainWorlds = new ArrayList<MainWorldBean>();
        mLinks = new ArrayList<LinkBean>();
        mProperties = new HashBean();
    }

    public java.lang.String getURI()
    {
        return "sub://"+getUpperBound();
    }
}
