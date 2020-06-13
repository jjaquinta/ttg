/*
 * Created on Apr 18, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.Consumer;

import jo.util.beans.Bean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ArrayUtils
{
    public static boolean isTrivial(Object[] arr)
    {
        return (arr == null) || (arr.length == 0);
    }
    
    public static boolean isTrivial(Collection<?> arr)
    {
        return (arr == null) || (arr.size() == 0);
    }
    
    public static String[] toStringArray(Object[] arr)
    {
        if (arr == null)
            return null;
        String[] ret = new String[arr.length];
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == null)
                ret[i] = null;
            else
                ret[i] = arr[i].toString();
        return ret;
    }
    
    public static String[] toStringArray(Collection<?> arr)
    {
        return toStringArray(arr.toArray());
    }
    
    public static String[] toStringArray(StringTokenizer st)
    {
        ArrayList<String> strings = new ArrayList<String>();
        while (st.hasMoreTokens())
            strings.add(st.nextToken());
        return strings.toArray(new String[0]);
    }

    public static int[] toIntArray(Object[] arr)
    {
        if (arr == null)
            return null;
        int[] ret = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == null)
                ret[i] = 0;
            else
                ret[i] = ((Number)arr[i]).intValue();
        return ret;
    }
    
    public static int[] toIntArray(Collection<?> arr)
    {
        return toIntArray(arr.toArray());
    }

    public static long[] toLongArray(Object[] arr)
    {
        if (arr == null)
            return null;
        long[] ret = new long[arr.length];
        for (int i = 0; i < arr.length; i++)
            if (arr[i] == null)
                ret[i] = 0L;
            else
                ret[i] = ((Number)arr[i]).longValue();
        return ret;
    }
    
    public static long[] toLongArray(Collection<?> arr)
    {
        return toLongArray(arr.toArray());
    }

    /**
     * @param arr
     * @param oid
     * @return
     */
    public static int indexOf(Collection<Bean> arr, long oid)
    {
        int ret = 0;
        for (Iterator<Bean> i = arr.iterator(); i.hasNext(); ret++)
        {
             Bean b = i.next();
             if (b.getOID() == oid)
                 return ret;
        }
        return -1;
    }

    /**
     * @param arr
     * @param oid
     * @return
     */
    public static Object get(Collection<?> arr, int idx)
    {
        if (idx < 0)
            return null;
        for (Iterator<?> i = arr.iterator(); i.hasNext(); idx--)
        {
             Object b = i.next();
             if (idx <= 0)
                 return b;
        }
        return null;
    }

    /**
     * @param ret
     * @param elements
     */
    public static <T> void addAll(Collection<T> ret, T[] elements)
    {
        if (elements != null)
            for (int i = 0; i < elements.length; i++)
                ret.add(elements[i]);
    }

    public static void addAll(Collection<String> ret, String[] elements)
    {
        if (elements != null)
            for (int i = 0; i < elements.length; i++)
                ret.add(elements[i]);
    }
    public static void addAll(Collection<Object> ret, Iterator<Object> elements)
    {
        if (elements != null)
            while (elements.hasNext())
                ret.add(elements.next());
    }
    public static <T> void removeAll(Collection<T> ret, T[] elements)
    {
        if (elements != null)
            for (int i = 0; i < elements.length; i++)
                ret.remove(elements[i]);
    }
    public static void removeAll(Collection<Object> ret, Iterator<Object> elements)
    {
        if (elements != null)
            while (elements.hasNext())
                ret.remove(elements.next());
    }
    // takes an array of arrays and transposes them
    public static List<List<?>> transpose(List<List<?>> input)
    {
        Object[][] matrix = new Object[input.size()][];
        int max = 0;
        for (Iterator<List<?>> i = input.iterator(); i.hasNext(); )
        {
            List<?> a = i.next();
            max = Math.max(max, a.size()); 
        }
        for (int i = 0; i < input.size(); i++)
        {
            List<?> a = input.get(i);
            matrix[i] = new Object[max];
            a.toArray(matrix[i]);
        }
        List<List<?>> output = new ArrayList<List<?>>();
        for (int i = 0; i < matrix[0].length; i++)
        {
            List<Object> a = new ArrayList<Object>();
            for (int j = 0; j < matrix.length; j++)
                if (matrix[j][i] != null)
                    a.add(matrix[j][i]);
            output.add(a);
        }
        return output;
    }

    /**
     * @param selection
     * @return
     */
    public static List<?> toList(Object[] objs)
    {
        ArrayList<Object> ret = new ArrayList<Object>();
        if (objs != null)
            for (int i = 0; i < objs.length; i++)
                ret.add(objs[i]);
        return ret;
    }

    /**
     * @param selection
     * @return
     */
    public static List<Integer> toList(int[] objs)
    {
        List<Integer> ret = new ArrayList<Integer>();
        if (objs != null)
            for (int i = 0; i < objs.length; i++)
                ret.add(objs[i]);
        return ret;
    }

    /**
     * @param selection
     * @return
     */
    public static Set<Object> toSet(Object[] objs)
    {
        Set<Object> ret = new HashSet<Object>();
        if (objs != null)
            for (int i = 0; i < objs.length; i++)
                ret.add(objs[i]);
        return ret;
    }
    
    public static boolean equals(Object[] objs1, Object[] objs2)
    {
        if ((objs1 == null) && (objs2 == null))
            return true;
        if ((objs1 == null) || (objs2 == null))
            return false;
        if (objs1.length != objs2.length)
            return false;
        Set<Object> set1 = toSet(objs1);
        Set<Object> set2 = toSet(objs2);
        set1.removeAll(set2);
        return set1.size() == 0;
    }
    
    public static int indexOf(Object[] objs, Object obj)
    {
        for (int i = 0; i < objs.length; i++)
            if ((objs[i] == obj) || ((objs[i] != null) && objs[i].equals(obj)))
                return i;
        return -1;
    }

    public static int indexOf(char[] objs, char obj)
    {
        for (int i = 0; i < objs.length; i++)
            if (objs[i] == obj)
                return i;
        return -1;
    }
    
    public static Object[] filter(Object[] objs, Class<?> type)
    {
        if (objs == null)
            return null;
        ArrayList<Object> ret = new ArrayList<Object>();
        for (int i = 0; i < objs.length; i++)
            if (type.isAssignableFrom(objs[i].getClass()))
                ret.add(objs[i]);
        return ret.toArray();
    }

    public static Object getRandom(List<?> list, Random random)
    {
        int size = list.size();
        if (size <= 0)
            return null;
        return list.get(random.nextInt(size));
    }
    
    public static void shuffle(Object[] objs, Random random, int start, int len)
    {
        for (int i = 0; i < len; i++)
        {
            int j = random.nextInt(len);
            Object tmp = objs[i];
            objs[i] = objs[j];
            objs[j] = tmp;
        }
    }
    
    public static void shuffle(Object[] objs, Random random)
    {
        shuffle(objs, random, 0, objs.length);
    }
    
    public static long max(long[] arr)
    {
        if (arr.length == 0)
            return 0;
        long ret = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > ret)
                ret = arr[i];
        return ret;
    }
    
    public static long min(long[] arr)
    {
        if (arr.length == 0)
            return 0;
        long ret = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < ret)
                ret = arr[i];
        return ret;
    }
    
    public static int max(int[] arr)
    {
        if (arr.length == 0)
            return 0;
        int ret = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > ret)
                ret = arr[i];
        return ret;
    }
    
    public static int min(int[] arr)
    {
        if (arr.length == 0)
            return 0;
        int ret = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < ret)
                ret = arr[i];
        return ret;
    }

    public static <T> List<T> toList(Iterator<T> iterator) {
        List<T> ret = new ArrayList<T>();
        while (iterator.hasNext())
            ret.add(iterator.next());
        return ret;
    }

    public static void set(boolean[] arr, boolean v) 
    {
        for (int i = 0; i < arr.length; i++)
            arr[i] = v;
    }
    
    public static boolean compareExactOrder(Collection<?> c1, Collection<?> c2)
    {
        if (c1.size() != c2.size())
            return false;
        Iterator<?> i1 = c1.iterator();
        Iterator<?> i2 = c2.iterator();
        while (i1.hasNext())
            if (i1.next() != i2.next())
                return false;
        return true;
    }
    
    public static Object toArray(Object[] arr, String className)
    {
        if (className.equals("[Z"))
        {
            boolean[] ret = new boolean[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Boolean)arr[i]).booleanValue();
            return ret;
        }
        if (className.equals("[S"))
        {
            short[] ret = new short[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Number)arr[i]).shortValue();
            return ret;
        }
        if (className.equals("[I"))
        {
            int[] ret = new int[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Number)arr[i]).intValue();
            return ret;
        }
        if (className.equals("[L"))
        {
            long[] ret = new long[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Number)arr[i]).longValue();
            return ret;
        }
        if (className.equals("[F"))
        {
            float[] ret = new float[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Number)arr[i]).floatValue();
            return ret;
        }
        if (className.equals("[D"))
        {
            double[] ret = new double[arr.length];
            for (int i = 0; i < arr.length; i++)
                ret[i] = ((Number)arr[i]).doubleValue();
            return ret;
        }
        System.err.println("ArrayUtils.toArray, unknown className="+className);
        return arr;
    }

    public static boolean compareAnyOrder(Collection<?> c1, Collection<?> c2)
    {
        if (c1.size() != c2.size())
            return false;
        for (Iterator<?> i1 = c1.iterator(); i1.hasNext(); )
            if (!c2.contains(i1.next()))
                return false;
        return true;
    }

    public static boolean contains(Object[] children,
            Object child)
    {
        if (children == null)
            return false;
        for (int i = 0; i < children.length; i++)
            if (children[i] == child)
                return true;
        return false;
    }

    public static Object[] dup(Object[] objects, int i, int l)
    {
        Object[] d = new Object[l];
        System.arraycopy(objects, i, d, 0, l);
        return d;
    }

    public static Object[] toArray(Object obj)
    {
        if (obj instanceof Object[])
            return (Object[])obj;
        else if (obj instanceof Collection<?>)
            return ((Collection<?>)obj).toArray();
        else if (obj instanceof int[])
        {
            int[] iArray = (int[])obj;
            Integer[] ret = new Integer[iArray.length];
            for (int i = 0; i < iArray.length; i++)
                ret[i] = iArray[i];
            return ret;
        }
        else
            return toStringArray(new StringTokenizer(obj.toString(), ","));
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] add(T[] arr, T elem)
    {
        T[] newArr = (T[])Array.newInstance(arr.getClass().getComponentType(), arr.length + 1);
        System.arraycopy(arr, 0, newArr, 0, arr.length);
        newArr[newArr.length - 1] = elem;
        return newArr;
    }
    
    @SuppressWarnings("unchecked")
    public static <T> T[] remove(T[] arr, T elem)
    {
        int idx = indexOf(arr, elem);
        if (idx < 0)
            return arr;
        T[] newArr = (T[])Array.newInstance(arr.getClass().getComponentType(), arr.length - 1);
        System.arraycopy(arr, 0, newArr, 0, idx);
        System.arraycopy(arr, idx + 1, newArr, idx, arr.length - idx - 1);
        return newArr;
    }

    public static String toString(int[] segs)
    {
        StringBuffer sb = new StringBuffer("[");
        for (int i = 0; i < segs.length; i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(segs[i]);
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static <T> void opCollection(Collection<T> coll, Consumer<T> op)
    {
        opCollection(coll.iterator(), op);
    }
    
    public static <T> void opCollection(Iterator<T> i, Consumer<T> op)
    {
        while (i.hasNext())
            op.accept(i.next());
    }
}
