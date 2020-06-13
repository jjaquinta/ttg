package jo.ttg.beans.sub;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.URIBean;
import jo.util.beans.Bean;

public class LinksBean extends Bean implements URIBean
{
    public static final String SCHEME = "links://";
    
    private String         mURI;
    private OrdBean        mUpperBound;
    private OrdBean        mLowerBound;
    private List<LinkBean> mLinks;

    public LinksBean()
    {
        mLinks = new ArrayList<LinkBean>();
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public OrdBean getUpperBound()
    {
        return mUpperBound;
    }

    public void setUpperBound(OrdBean UpperBound)
    {
        mUpperBound = UpperBound;
    }

    public OrdBean getLowerBound()
    {
        return mLowerBound;
    }

    public void setLowerBound(OrdBean LowerBound)
    {
        mLowerBound = LowerBound;
    }

    public List<LinkBean> getLinks()
    {
        return mLinks;
    }

    public void setLinks(List<LinkBean> links)
    {
        mLinks = links;
    }
}
