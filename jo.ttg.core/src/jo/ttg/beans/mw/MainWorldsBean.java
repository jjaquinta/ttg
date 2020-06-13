package jo.ttg.beans.mw;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.URIBean;
import jo.util.beans.Bean;

public class MainWorldsBean extends Bean implements URIBean
{
    private String  mURI;
    private OrdBean mPoint1;
    private OrdBean mPoint2;
    private int     mRadius;
    private List<MainWorldBean> mMainWorlds;
    
    public MainWorldsBean()
    {
        mMainWorlds = new ArrayList<MainWorldBean>();
    }

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public OrdBean getPoint1()
    {
        return mPoint1;
    }

    public void setPoint1(OrdBean point1)
    {
        mPoint1 = point1;
    }

    public OrdBean getPoint2()
    {
        return mPoint2;
    }

    public void setPoint2(OrdBean point2)
    {
        mPoint2 = point2;
    }

    public List<MainWorldBean> getMainWorlds()
    {
        return mMainWorlds;
    }

    public void setMainWorlds(List<MainWorldBean> mainWorlds)
    {
        mMainWorlds = mainWorlds;
    }

    public int getRadius()
    {
        return mRadius;
    }

    public void setRadius(int radius)
    {
        mRadius = radius;
    }

}
