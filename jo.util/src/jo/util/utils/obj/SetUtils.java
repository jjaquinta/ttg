package jo.util.utils.obj;

import java.util.HashSet;
import java.util.Set;

public class SetUtils
{
    @SuppressWarnings("rawtypes")
    public static Set intersection(Set s1, Set s2)
    {
        Set<Object> both = new HashSet<Object>();
        for (Object o : s1)
            if (s2.contains(o))
                both.add(o);
        return both;
    }

    @SuppressWarnings("rawtypes")
    public static boolean isIntersection(Set s1, Set s2)
    {
        for (Object o : s1)
            if (s2.contains(o))
                return true;
        return false;
    }
}
