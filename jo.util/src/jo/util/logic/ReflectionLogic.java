package jo.util.logic;

import java.lang.reflect.Method;

public class ReflectionLogic
{
    public static void dumpMethods(Object o)
    {
        Class<?> objClass;
        if (o instanceof Class<?>)
        {
            objClass = (Class<?>)o;
            o = null;            
        }
        else
            objClass = o.getClass();
        Method[] ms = objClass.getMethods();
        System.out.println("Methods on "+objClass.getName());
        for (Method m : ms)
            System.out.println("  "+describe(m));
    }
    
    public static Object call(Object o, String method, Object[] args) throws IllegalArgumentException
    {
        Class<?> objClass;
        if (o instanceof Class<?>)
        {
            objClass = (Class<?>)o;
            o = null;            
        }
        else
            objClass = o.getClass();
        Method m = findMethod(objClass, method, args);
        if (m == null)
        {
            dumpMethods(objClass);
            throw new IllegalArgumentException("Can't find method "+method+" on "+objClass.getName());
        }
        try
        {
            return m.invoke(o, args);
        }
        catch (Exception e)
        {
            dumpMethods(objClass);
            throw new IllegalArgumentException("Can't invoke "+describe(m)+" with "+args, e);
        }
    }

    public static String describe(Method m)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(m.getName());
        sb.append("(");
        for (int i = 0; i < m.getParameterTypes().length; i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(m.getParameterTypes()[i].getName());
        }
        sb.append(")");
        return sb.toString();
    }
    
    private static Method findMethod(Class<?> objClass, String method, Object[] args)
    {
        // pass 1, exact signature
        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++)
        {
            if (args[i] == null)
            {
                argTypes = null;
                break;
            }
            argTypes[i] = args[i].getClass();
        }
        if (argTypes != null)
        {
            try
            {
                Method m = objClass.getMethod(method, argTypes);
                if (m != null)
                    return m;
            }
            catch (SecurityException e)
            {
            }
            catch (NoSuchMethodException e)
            {
            }            
        }
        // pass 2, exact name and # param match
        Method[] mm = objClass.getMethods();
        for (Method m : mm)
            if (m.getName().equals(method) && (m.getParameterTypes().length == args.length))
                return m;
        // pass 3, inexact name and # param match
        for (Method m : mm)
            if (m.getName().equalsIgnoreCase(method) && (m.getParameterTypes().length == args.length))
                return m;
        // pass 4, inexact name
        for (Method m : mm)
            if (m.getName().equalsIgnoreCase(method))
                return m;
        return null;
    }
}
