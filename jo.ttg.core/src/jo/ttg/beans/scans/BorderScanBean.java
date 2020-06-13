package jo.ttg.beans.scans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.SchemeLogic;

public class BorderScanBean
{
    public static final String CRITERIA_NAVAL_BASE = "base.navy";
    
    private IGenScheme                      mScheme;
    private Object                          mScope;
    private int                             mRadius;
    private String                          mNativeCriteria;
    private String                          mAlienCriteria;
    private Map<String,List<MainWorldBean>> mBorderWorlds;
    private Map<MainWorldBean,Map<String,List<MainWorldBean>>>  mBorderWith;
    
    public BorderScanBean()
    {
        mScheme = SchemeLogic.getDefaultScheme();
        mScope = null;
        mRadius = 4;
        mNativeCriteria = CRITERIA_NAVAL_BASE;
        mAlienCriteria = null;
        mBorderWorlds = new HashMap<String, List<MainWorldBean>>();
        mBorderWith = new HashMap<MainWorldBean, Map<String,List<MainWorldBean>>>();
    }

    public IGenScheme getScheme()
    {
        return mScheme;
    }

    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

    public Object getScope()
    {
        return mScope;
    }

    public void setScope(Object scope)
    {
        mScope = scope;
    }

    public int getRadius()
    {
        return mRadius;
    }

    public void setRadius(int radius)
    {
        mRadius = radius;
    }

    public String getNativeCriteria()
    {
        return mNativeCriteria;
    }

    public void setNativeCriteria(String nativeCriteria)
    {
        mNativeCriteria = nativeCriteria;
    }

    public String getAlienCriteria()
    {
        return mAlienCriteria;
    }

    public void setAlienCriteria(String alienCriteria)
    {
        mAlienCriteria = alienCriteria;
    }

    public Map<String, List<MainWorldBean>> getBorderWorlds()
    {
        return mBorderWorlds;
    }

    public void setBorderWorlds(Map<String, List<MainWorldBean>> borderWorlds)
    {
        mBorderWorlds = borderWorlds;
    }

    public Map<MainWorldBean, Map<String,List<MainWorldBean>>> getBorderWith()
    {
        return mBorderWith;
    }

    public void setBorderWith(Map<MainWorldBean, Map<String,List<MainWorldBean>>> borderWith)
    {
        mBorderWith = borderWith;
    }
}
