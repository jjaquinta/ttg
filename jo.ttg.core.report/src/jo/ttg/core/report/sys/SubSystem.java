package jo.ttg.core.report.sys;

import java.util.HashSet;
import java.util.Set;

import jo.ttg.beans.sys.BodyBean;

public class SubSystem
{
    private BodyBean        mRoot;
    private Set<BodyBean>   mContents;
    
    public SubSystem()
    {
        mContents = new HashSet<BodyBean>();
    }
    
    public BodyBean getRoot()
    {
        return mRoot;
    }
    public void setRoot(BodyBean root)
    {
        mRoot = root;
    }
    public Set<BodyBean> getContents()
    {
        return mContents;
    }
    public void setContents(Set<BodyBean> contents)
    {
        mContents = contents;
    }
}
