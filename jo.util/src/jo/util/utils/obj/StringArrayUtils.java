/*
 * Created on Dec 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.obj;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.util.utils.ArrayUtils;

public class StringArrayUtils
{
    
    public static String[] extractArray(String html, String prefix, String suffix)
    {
        List<String> arr = new ArrayList<String>();
        while (html != null)
        {
            int o = html.indexOf(prefix);
            if (o < 0)
                break;
            html = html.substring(o + prefix.length());
            o = html.indexOf(suffix);
            if (o < 0)
                break;
            arr.add(html.substring(0, o));
            html = html.substring(o + suffix.length());
        }
        return ArrayUtils.toStringArray(arr);
    }

    public static int indexOf(String[] arr, String pattern)
    {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equals(pattern))
                return i;
        return -1;
    }

    public static int indexOfSubstring(String[] arr, String pattern)
    {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].indexOf(pattern) >= 0)
                return i;
        return -1;
    }

    public static int indexOfSubstring(String str, String[] patterns)
    {
        for (int i = 0; i < patterns.length; i++)
            if (str.indexOf(patterns[i]) >= 0)
                return i;
        return -1;
    }
    
    public static String[] remove(String[] arr, String pattern)
    {
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++)
            if (!arr[i].equals(pattern))
                ret.add(arr[i]);
        return ArrayUtils.toStringArray(ret);
    }
    
    public static String[] removeSubstring(String[] arr, String pattern)
    {
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++)
            if (arr[i].indexOf(pattern) < 0)
                ret.add(arr[i]);
        return ArrayUtils.toStringArray(ret);
    }
    
    public static String[] removeSubstring(String[] arr, String[] patterns)
    {
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < arr.length; i++)
            if (indexOfSubstring(arr[i], patterns) < 0)
                ret.add(arr[i]);
        return ArrayUtils.toStringArray(ret);
    }
    
    public static String[] unique(String[] arr)
    {
        Set<String> ret = new HashSet<String>();
        for (int i = 0; i < arr.length; i++)
            ret.add(arr[i]);
        return ArrayUtils.toStringArray(ret);
    }
    
    public static Set<String> intersectionIgnoreCase(Set<String> s1, Set<String> s2)
    {
        Set<String> both = new HashSet<String>();
        for (String str1 : s1)
            if (containsIgnoreCase(str1, s2))
                both.add(str1);
        return both;
    }

    public static boolean isIntersectionIgnoreCase(Set<String> s1, Set<String> s2)
    {
        for (String str1 : s1)
            if (containsIgnoreCase(str1, s2))
                return true;
        return false;
    }

    public static boolean containsIgnoreCase(String str1, Set<String> s2)
    {
        for (String str2 : s2)
            if (str2.equalsIgnoreCase(str1))
                return true;
        return false;
    }
}
