package jo.ttg.logic;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.TTGRuntimeBean;
import jo.ttg.beans.URIBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.SchemeLogic;

public class RuntimeLogic
{
    private static TTGRuntimeBean   mRuntime;
    
    public static synchronized TTGRuntimeBean getInstance()
    {
        if (mRuntime == null)
            mRuntime = new TTGRuntimeBean();
        return mRuntime;
    }
    
    public static void setLocation(String uri)
    {
        getInstance();
        mRuntime.setLocationURI(uri);
        mRuntime.setLocation(SchemeLogic.getFromURI(mRuntime.getScheme(), uri));
    }
    
    public static void setLocation(URIBean obj)
    {
        getInstance();
        mRuntime.setLocation(obj);
        mRuntime.setLocationURI(obj.getURI());
    }
    
    public static void setLocation(URIBean obj, String uri)
    {
        getInstance();
        mRuntime.setLocation(obj);
        mRuntime.setLocationURI(uri);
    }
    
    public static IGenScheme getScheme()
    {
        return getInstance().getScheme();
    }
    
    public static DateBean getDate()
    {
        return getInstance().getDate();
    }
    
    public static void incrementDate(int minutes)
    {
        getInstance();
        mRuntime.getDate().setMinutes(mRuntime.getDate().getMinutes() + minutes);
        mRuntime.fireMonotonicPropertyChange("date");
    }
}
