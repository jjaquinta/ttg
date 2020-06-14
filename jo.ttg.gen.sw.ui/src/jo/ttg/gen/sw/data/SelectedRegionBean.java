package jo.ttg.gen.sw.data;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.URIBean;

public class SelectedRegionBean implements URIBean
{
    private String                  mURI            = "";
    private List<SWMainWorldBean>   mWorldList      = new ArrayList<>();
    private List<SWMainWorldBean>   mInnerWorldList = new ArrayList<>();
    private List<SWMainWorldBean[]> mShortLinks     = new ArrayList<>();
    private List<SWMainWorldBean[]> mLongLinks      = new ArrayList<>();

    // constructors
    public SelectedRegionBean()
    {
    }

    public SelectedRegionBean(String uri, List<SWMainWorldBean> worldList,
            List<SWMainWorldBean> innerWorldList,
            List<SWMainWorldBean[]> shortLinks,
            List<SWMainWorldBean[]> longLinks)
    {
        mURI = uri;
        mWorldList = worldList;
        mInnerWorldList = innerWorldList;
        mShortLinks = shortLinks;
        mLongLinks = longLinks;
    }

    // getters and setters

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public List<SWMainWorldBean> getWorldList()
    {
        return mWorldList;
    }

    public void setWorldList(List<SWMainWorldBean> worldList)
    {
        mWorldList = worldList;
    }

    public List<SWMainWorldBean> getInnerWorldList()
    {
        return mInnerWorldList;
    }

    public void setInnerWorldList(List<SWMainWorldBean> innerWorldList)
    {
        mInnerWorldList = innerWorldList;
    }

    public List<SWMainWorldBean[]> getShortLinks()
    {
        return mShortLinks;
    }

    public void setShortLinks(List<SWMainWorldBean[]> shortLinks)
    {
        mShortLinks = shortLinks;
    }

    public List<SWMainWorldBean[]> getLongLinks()
    {
        return mLongLinks;
    }

    public void setLongLinks(List<SWMainWorldBean[]> longLinks)
    {
        mLongLinks = longLinks;
    }

}
