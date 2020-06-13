/*
 * Created on Apr 29, 2005 10:53:48 AM by jo
 */
package jo.util.utils;

/**
 * @author jo
 * 
 * Apr 29, 2005 10:53:48 AM
 */
public class ObjectUtils
{
    public static boolean equals(Object o1, Object o2)
    {
        if (o1 == null)
            if (o2 == null)
                return true;
            else
                return false;
        else
            if (o2 == null)
                return false;
            else if (o1 == o2)
                return true;
            else
                return o1.equals(o2);
    }

    /**
     * @param object
     * @param inventoryItem
     * @return
     */
    public static boolean arrayEquals(Object[] a1, Object[] a2)
    {
        if (a1 == null)
            if (a2 == null)
                return true;
            else
                return false;
        else
            if (a2 == null)
                return false;
            else if (a1 == a2)
                return true;
            else if (a1.length != a2.length)
                return false;
            else
            {
                for (int i = 0; i < a1.length; i++)
                {
                    boolean found = false;
                    for (int j = 0; j < a2.length; j++)
                        if (equals(a1[i], a2[j]))
                        {
                            found = true;
                            break;
                        }
                    if (!found)
                        return false;
                }
                return true;
            }
    }
}
