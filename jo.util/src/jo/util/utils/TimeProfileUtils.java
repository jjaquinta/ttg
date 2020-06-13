package jo.util.utils;

import java.util.HashMap;
import java.util.Map;

public class TimeProfileUtils
{
    private static Map<String,Long>  mSummations = new HashMap<String,Long>();
    private static Map<String,Long>  mStarts = new HashMap<String,Long>();
    
    public static void clear()
    {
        mSummations.clear();
    }
    
    public static void start(String key)
    {
        mStarts.put(key, new Long(System.currentTimeMillis()));
    }
    
    public static void end(String key)
    {
        Long start = (Long)mStarts.get(key);
        if (start == null)
            return;
        mStarts.remove(key);
        long elapsed = System.currentTimeMillis() - start.longValue();
        Long sum = (Long)mSummations.get(key);
        if (sum == null)
            sum = new Long(elapsed);
        else
            sum = new Long(sum.longValue() + elapsed);
        mSummations.put(key, sum);
    }
    
    public static void printAll(String wrt)
    {
        long total = 0;
        if (wrt != null)
            total = ((Long)mSummations.get(wrt)).longValue();
        System.out.println("Profile stats:");
        for (String key : mSummations.keySet())
        {
            Long val = (Long)mSummations.get(key);
            if (wrt != null)
            {
                if  (!wrt.equals(key))
                    System.out.println("  "+key+"\t"+((val.longValue()*100)/total));
            }
            else
                System.out.println("  "+key+"\t"+val.toString());
        }
    }
}
