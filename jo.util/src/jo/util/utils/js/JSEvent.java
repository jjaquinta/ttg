package jo.util.utils.js;

import java.util.Map;

public class JSEvent
{
    private long                mStartTime;
    private long                mStopTime;
    private String              mExpression;
    private Map<String,Object>  mProps;
    private Throwable           mError;
    private Object              mResult;
    
    public String getExpression()
    {
        return mExpression;
    }
    public void setExpression(String expression)
    {
        mExpression = expression;
    }
    public Map<String, Object> getProps()
    {
        return mProps;
    }
    public void setProps(Map<String, Object> props)
    {
        mProps = props;
    }
    public Throwable getError()
    {
        return mError;
    }
    public void setError(Throwable error)
    {
        mError = error;
    }
    public Object getResult()
    {
        return mResult;
    }
    public void setResult(Object result)
    {
        mResult = result;
    }
    public long getStartTime()
    {
        return mStartTime;
    }
    public void setStartTime(long startTime)
    {
        mStartTime = startTime;
    }
    public long getStopTime()
    {
        return mStopTime;
    }
    public void setStopTime(long stopTime)
    {
        mStopTime = stopTime;
    }
}
