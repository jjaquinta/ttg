package jo.util.intro;

import java.lang.reflect.Method;

public class PropInfo
{
    private String      mName;
    private Class<?>    mType;
    private Method      mGetter;
    private Method      mSetter;
    
    public String getName()
    {
        return mName;
    }
    public void setName(String name)
    {
        mName = name;
    }
    public Class<?> getType()
    {
        return mType;
    }
    public void setType(Class<?> type)
    {
        mType = type;
    }
    public Method getGetter()
    {
        return mGetter;
    }
    public void setGetter(Method getter)
    {
        mGetter = getter;
    }
    public Method getSetter()
    {
        return mSetter;
    }
    public void setSetter(Method setter)
    {
        mSetter = setter;
    }
}
