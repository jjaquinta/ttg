package jo.util.utils.js;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Logger;

import javax.script.Bindings;
import javax.script.ScriptContext;

import jo.util.utils.MapUtils;

class JSFunctionOpLogic
{
    private static Logger logger = Logger.getLogger(JSFunctionOpLogic.class.getPackage().getName());
    
    private static Map<String, IJSFunction>   FUNCTIONS = new HashMap<String, IJSFunction>();
    private static Map<String, JavascriptWrapper>   JAVASCRIPT_FUNCTIONS = null;
    
    public static void addFunction(IJSFunction func)
    {
        for (String name : func.getNames())
            FUNCTIONS.put(name.toLowerCase(), func);
    }
    
    static Object evaluateFunction(String name, Object[] args, Map<String,Object> props) throws JSEvaluationException
    {
        IJSFunction func = FUNCTIONS.get(name.toLowerCase());
        if (func != null)
            return func.evaluate(name, args, props);
        if (props.containsKey("exprFunctions"))
        {
            IJSFunction[] funcs = (IJSFunction[])props.get("exprFunctions"); 
            for (IJSFunction f : funcs)
                for (String n : f.getNames())
                    if (n.equalsIgnoreCase(name))
                        return f.evaluate(name, args, props);
        }
        throw new JSEvaluationException("Unknown function "+name);
    }

    static Collection<IJSFunction> getFunctions()
    {
        HashSet<IJSFunction> list = new HashSet<IJSFunction>();
        list.addAll(FUNCTIONS.values());
        return list;
    }

    static void makeJavascriptBindings(Bindings b, Map<String,Object> props)
    {
        if (JAVASCRIPT_FUNCTIONS == null)
        {
            JAVASCRIPT_FUNCTIONS = new HashMap<String, JavascriptWrapper>();
            for (String name : FUNCTIONS.keySet())
            {
                JavascriptWrapper wrapper = new JavascriptWrapper(name);
                JAVASCRIPT_FUNCTIONS.put("func_"+name, wrapper);
            }
        }
        MapUtils.copy(b, JAVASCRIPT_FUNCTIONS);
        if (props.containsKey("exprFunctions"))
        {
            IJSFunction[] funcs = (IJSFunction[])props.get("exprFunctions"); 
            for (IJSFunction f : funcs)
                for (String n : f.getNames())
                    b.put("func_"+n.toLowerCase(), new JavascriptWrapper(n.toLowerCase()));
        }
    }
    
    static String expandJavascriptFunctions(String expr, Map<String,Object> props)
    {
        for (int start = expr.indexOf('@'); start >= 0; start = expr.indexOf('@', start + 1))
        {
            int end = start + 1;
            while ((end < expr.length()) && Character.isJavaIdentifierPart(expr.charAt(end)))
                end++;
            String func = expr.substring(start + 1, end).toLowerCase();
            if (FUNCTIONS.containsKey(func) || extraFunction(func, props))
                expr = expr.substring(0, start) + "func_" + func.toLowerCase() + ".run" + expr.substring(end);
        }
        return expr;
    }

    private static boolean extraFunction(String func, Map<String, Object> props)
    {
        if (!props.containsKey("exprFunctions"))
            return false;
        IJSFunction[] funcs = (IJSFunction[])props.get("exprFunctions"); 
        for (IJSFunction f : funcs)
            for (String n : f.getNames())
                if (n.equalsIgnoreCase(func))
                    return true;
        return false;
    }

    private static Map<Thread, Stack<ScriptContext>> mInstanceProps = new HashMap<Thread, Stack<ScriptContext>>();
    
    static void setInstanceProps(ScriptContext props)
    {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if (stack == null)
        {
            stack = new Stack<ScriptContext>();
            mInstanceProps.put(Thread.currentThread(), stack);
        }
        stack.push(props);
        logger.fine("INSTANCE PROPS: push "+Thread.currentThread().getName()+" size="+stack.size());
    }
    static void clearInstanceProps()
    {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if (stack != null)
        {
            stack.pop();
            if (stack.size() == 0)
                mInstanceProps.remove(Thread.currentThread());
            logger.fine("INSTANCE PROPS: pop "+Thread.currentThread().getName()+" size="+stack.size());
        }
    }
    static ScriptContext getInstanceProps()
    {
        Stack<ScriptContext> stack = mInstanceProps.get(Thread.currentThread());
        if ((stack != null) && (stack.size() > 0))
            return stack.get(stack.size() - 1);
        else
            return null;//throw new IllegalStateException("Tried to get instance props and none set!");
    }
}

class JavascriptWrapper
{
    private String          mName;
    
    public JavascriptWrapper(String name)
    {
        mName = name;
    }
    
    public Object run(Object ... args) throws JSEvaluationException
    {
        ScriptContext context = JSFunctionOpLogic.getInstanceProps();
        int scope = context.getScopes().get(0);
        return JSFunctionOpLogic.evaluateFunction(mName, args, context.getBindings(scope));
    }
    
    public Object run() throws JSEvaluationException
    {
        return run(new Object[] { } );
    }
    
    public Object run(Object arg1) throws JSEvaluationException
    {
        return run(new Object[] { arg1 } );
    }
    
    public Object run(Object arg1, Object arg2) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19, Object arg20) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19, arg20 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19, Object arg20, Object arg21) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19, arg20, arg21 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19, Object arg20, Object arg21, Object arg22) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19, arg20, arg21, arg22 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19, Object arg20, Object arg21, Object arg22, Object arg23) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19, arg20, arg21, arg22, arg23 } );
    }
    
    public Object run(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5, Object arg6, Object arg7, Object arg8,
            Object arg9, Object arg10, Object arg11, Object arg12, Object arg13, Object arg14, Object arg15, Object arg16,
            Object arg17, Object arg18, Object arg19, Object arg20, Object arg21, Object arg22, Object arg23, Object arg24) throws JSEvaluationException
    {
        return run(new Object[] { arg1, arg2, arg3, arg4, arg5, arg6, arg7, arg8, arg9, arg10, arg11, arg12, arg13, arg14, arg15, arg16,
                arg17, arg18, arg19, arg20, arg21, arg22, arg23, arg24 } );
    }
}
