package jo.ttg.beans;

import jo.util.beans.Bean;

public class URIBookmarkBean extends Bean
{
    private String  mCategory;
    private String  mName;
    private String  mURI;
    
    public URIBookmarkBean()
    {
        mCategory = "";
        mName = "";
        mURI = "";
    }
    
    public String getCategory()
    {
        return mCategory;
    }
    public void setCategory(String category)
    {
        mCategory = category;
    }
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public String getURI()
    {
        return mURI;
    }
    public void setURI(String uri)
    {
        mURI = uri;
    }
}
