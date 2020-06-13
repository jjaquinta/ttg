package jo.util.utils;

import java.util.HashMap;
import java.util.Map;

public class MemoryProfileUtils
{
    private static Map<String,Integer>  mCounts = new HashMap<String,Integer>();
    private static Map<String,Long>  mSummations = new HashMap<String,Long>();
    private static Map<String,Long>  mStarts = new HashMap<String,Long>();
    
    public static void start(String key)
    {
        Runtime.getRuntime().gc();
        mStarts.put(key, new Long(Runtime.getRuntime().freeMemory()));
    }
    
    public static void end(String key)
    {
        Long start = (Long)mStarts.get(key);
        if (start == null)
            return;
        mStarts.remove(key);
        Runtime.getRuntime().gc();
        long used = start.longValue() - Runtime.getRuntime().freeMemory();
        Long sum = (Long)mSummations.get(key);
        if (sum == null)
            sum = new Long(used);
        else
            sum = new Long(sum.longValue() + used);
        mSummations.put(key, sum);
        Integer count = (Integer)mCounts.get(key);
        if (count == null)
            count = new Integer(1);
        else
            count = new Integer(count.intValue() + 1);
        mCounts.put(key, count);
    }
    
    public static double getRate(String key)
    {
        Long totalUsed = (Long)mSummations.get(key);
        Integer totalCount = (Integer)mCounts.get(key);
        if ((totalUsed == null) || (totalCount == null))
            return 0;
        return totalUsed.doubleValue()/totalCount.doubleValue();
    }
    
    public static void printAll(String wrt)
    {
        double total = 0;
        if (wrt != null)
            total = getRate(wrt);
        System.out.println("Memory Profile stats ("+Runtime.getRuntime().freeMemory()+"):");
        for (String key : mSummations.keySet())
        {
            double val = getRate(key);
            if (wrt != null)
            {
                if  (!wrt.equals(key))
                    System.out.println("  "+key+"\t"+((val*100)/total));
            }
            else
            {
                System.out.println("  "+key+"\t"+(int)val);
            }
        }
    }
}
