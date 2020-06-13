package jo.util.utils.xml;

public class XMLIOTreeRef
{
    private String mKey;
    private Object  mArg1;
    private Object  mArg2;
    private IXMLIOTreeRefResolver   mResolver;
    
    public XMLIOTreeRef(String key)
    {
        mKey = key;
    }

    public String getKey()
    {
        return mKey;
    }

    public void setKey(String key)
    {
        mKey = key;
    }

    public Object getArg1()
    {
        return mArg1;
    }

    public void setArg1(Object Arg1)
    {
        mArg1 = Arg1;
    }

    public IXMLIOTreeRefResolver getResolver()
    {
        return mResolver;
    }

    public void setResolver(IXMLIOTreeRefResolver resolver)
    {
        mResolver = resolver;
    }

    public Object getArg2()
    {
        return mArg2;
    }

    public void setArg2(Object arg2)
    {
        mArg2 = arg2;
    }
}
