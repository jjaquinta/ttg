package jo.util.utils.js;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import jo.util.utils.ArrayUtils;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.StringUtils;

public class JSUtils
{
    private static List<IJSListener> mListeners = Collections.synchronizedList(new ArrayList<IJSListener>());
    private static ScriptEngineManager mScriptManager = new ScriptEngineManager();
    
    public static String expandText(String text, Map<String,Object> props) throws JSEvaluationException
    {
        return expandText(text, props, "%", "%");
    }
    public static String expandText(String text, Map<String,Object> props, String prefix, String suffix) throws JSEvaluationException
    {
        ScriptEngine engine = mScriptManager.getEngineByMimeType("text/javascript");
        ScriptContext context = makeContext(engine, props, null);
        JSFunctionOpLogic.setInstanceProps(context);
        List<String> chunks = TokenizeLogic.tokenizeText(text, prefix, suffix);
        String ret = expandTextChunks(chunks, 0, chunks.size(), props);
        JSFunctionOpLogic.clearInstanceProps();
        return ret;
    }
    
    private static String expandTextChunks(List<String> chunks, int start, int end, Map<String,Object> props) throws JSEvaluationException
    {
        StringBuffer expanded = new StringBuffer();
        expanded.append(chunks.get(start));
        for (int o  = start + 1; o < end; o += 2)
        {
            String expr = chunks.get(o);
            if (TokenizeLogic.startsWithToken(expr, "#if") || TokenizeLogic.startsWithToken(expr, "#ifdef"))
            {
                int ifEnd = TokenizeLogic.findIfEnd(chunks, o, end);
                while (o < ifEnd)
                {
                    expr = chunks.get(o);
                    int next = TokenizeLogic.findIfClose(chunks, o, ifEnd);
                    if (TokenizeLogic.startsWithToken(expr, "#if") || TokenizeLogic.startsWithToken(expr, "#ifdef") || TokenizeLogic.startsWithToken(expr, "#elseif"))
                    {
                        boolean evalTrue;
                        if (expr.startsWith("#ifdef"))
                        {
                            String key = expr.substring(6).trim();
                            evalTrue = (props.get(key) != null) && !StringUtils.isTrivial(props.get(key).toString());
                        }
                        else
                        {
                            Object obj = evalObject(expr.substring(3), props);
                            evalTrue = BooleanUtils.parseBoolean(obj);
                        }
                        if (evalTrue)
                        {
                            expanded.append(expandTextChunks(chunks, o + 1, next - 1, props));
                            o = ifEnd;                            
                        }
                        else
                            o = next;
                    }
                    else if (TokenizeLogic.startsWithToken(expr, "#else"))
                    {
                        expanded.append(expandTextChunks(chunks, o + 1, next - 1, props));
                        o = ifEnd;
                    }
                }
            }
            else if (TokenizeLogic.startsWithToken(expr, "#loop"))
            {
                int loopEnd = TokenizeLogic.findLoopEnd(chunks, o, end);
                Object oCollection = evalObject(expr.substring(5), props);
                Object[] collection = null;
                if (oCollection instanceof Object[])
                    collection = (Object[])oCollection;
                else if (oCollection instanceof Collection<?>)
                    collection = ((Collection<?>)oCollection).toArray();
                else if (oCollection instanceof Iterator<?>)
                    collection = ArrayUtils.toList((Iterator<?>)oCollection).toArray();
                if (collection != null)
                    for (Object c : collection)
                    {
                        if (c instanceof Map<?,?>)
                        {
                           Map<String,Object> mergedProps = new HashMap<String, Object>();
                           for (String key : props.keySet())
                               mergedProps.put(key, props.get(key));
                           for (Object key : ((Map<?,?>)c).keySet())
                               mergedProps.put((String)key, ((Map<?,?>)c).get(key));
                           expanded.append(expandTextChunks(chunks, o + 1, loopEnd - 1, mergedProps));
                        }
                        else
                        {
                            props.put("index", c);
                            expanded.append(expandTextChunks(chunks, o + 1, loopEnd - 1, props));
                        }
                    }
                o = loopEnd;
            }
            else
            {
                Object obj = evalObject(expr, props);
                if (obj != null)
                {
                    Object eval = obj.toString();
                    expanded.append(eval);
                }
            }
            expanded.append(chunks.get(o + 1));
        }
        return expanded.toString();
    }
    
    public static Object evalObject(String expr, Map<String,Object> props) throws JSEvaluationException
    {
        JSEvent ev = new JSEvent();
        ev.setStartTime(System.currentTimeMillis());
        ev.setExpression(expr);
        ev.setProps(props);
        
        expr = expr.trim();
        try
        {
            Object ret = evalJavascript(expr, props);
            ev.setStopTime(System.currentTimeMillis());
            ev.setResult(ret);
            fireListeners(ev);
            return ret;
        }
        catch (Throwable t)
        {
            ev.setStopTime(System.currentTimeMillis());
            ev.setError(t);
            fireListeners(ev);
            if (t instanceof JSEvaluationException)
                throw (JSEvaluationException)t;
            else
                throw new IllegalStateException("Error executing macro '"+expr+"'", t);
        }
    }

    private static Object evalJavascript(String expr, Map<String,Object> props) throws JSEvaluationException
    {
        ScriptEngine engine = mScriptManager.getEngineByMimeType("text/javascript");
        if (engine == null)
            throw new JSEvaluationException("Cannot find Javascript engine!");
        expr = JSFunctionOpLogic.expandJavascriptFunctions(expr, props);
        ScriptContext context = JSFunctionOpLogic.getInstanceProps();
        try
        {
            if (context == null)
            {
                context = makeContext(engine, props, null);
                JSFunctionOpLogic.setInstanceProps(context);
                Object ret = engine.eval(expr, context);
                JSFunctionOpLogic.clearInstanceProps();
                return ret;
            }
            else
            {
                Map<String, String> nameMap = new HashMap<String, String>();
                updateContext(context, engine, props, nameMap);
                //System.out.println("foo -> "+context.getBindings(0).get("foo"));
                Object ret = engine.eval(expr, context);
                //System.out.println("foo <- "+context.getBindings(0).get("foo"));
                List<Integer> scopes = context.getScopes();
                if (scopes.size() > 0)
                {
                    Bindings b = context.getBindings(scopes.get(0));
                    for (String key : b.keySet())
                    {
                        if (key.startsWith("func_"))
                            continue;
                        if (key.equals("print") || key.equals("println"))
                            continue;
                        Object val = b.get(key);
                        if (nameMap.containsKey(key))
                            key = nameMap.get(key);
                        if (!props.containsKey(key))
                        {
                            //System.out.println("  new key: "+key);
                            props.put(key, val);
                        }
                        else if (props.get(key) != val)
                        {
                            //System.out.println("  new val: "+key+"="+val);
                            props.put(key, val);
                        }
                    }
                }
                return ret;
            }
        }
        catch (ScriptException e)
        {
            e.printStackTrace();
            throw new JSEvaluationException("Script error executing '"+expr+"'", e);
        }
    }

    private static ScriptContext makeContext(ScriptEngine engine, Map<String,Object> props, Map<String, String> nameMap)
    {
        ScriptContext context = engine.getContext();
        Bindings b = engine.createBindings();
        makeBindings(b, props, nameMap);
        int scope = context.getScopes().get(0);
        context.setBindings(b, scope);
        JSFunctionOpLogic.makeJavascriptBindings(b, props);
//        System.out.println("MAKE_CONTEXT: push "+Thread.currentThread().getName());
//        EvaluateFunctionOpLogic.setInstanceProps(context);
        return context;
    }

    private static void updateContext(ScriptContext context, ScriptEngine engine, Map<String,Object> props, Map<String, String> nameMap)
    {
        Bindings b = engine.createBindings();
        makeBindings(b, props, nameMap);
        int scope = context.getScopes().get(0);
        context.setBindings(b, scope);
        JSFunctionOpLogic.makeJavascriptBindings(b, props);
//        System.out.println("UPDATE_CONTEXT: push "+Thread.currentThread().getName());
//        EvaluateFunctionOpLogic.setInstanceProps(context);
    }
    
    private static void makeBindings(Bindings b, Map<String,Object> props, Map<String, String> nameMap)
    {
        for (String k : props.keySet())
        {
            Object v = props.get(k);
            String newk = k.replace('.', '_');
            b.put(newk, v);
            if (nameMap != null)
                nameMap.put(newk, k);
        }
    }

    public static boolean evalBoolean(String expr, Map<String,Object> props) throws JSEvaluationException
    {
        Object obj = evalObject(expr, props);
        boolean ret = BooleanUtils.parseBoolean(obj);
        return ret;
    }
    
    public static List<List<String>> getFunctionLibrary(Map<String,Object> props)
    {
        List<List<String>> library = new ArrayList<List<String>>();
        for (IJSFunction func : JSFunctionOpLogic.getFunctions())
            addLibrary(library, func);
        if ((props != null) && props.containsKey("exprFunctions"))
        {
            IJSFunction[] funcs = (IJSFunction[])props.get("exprFunctions"); 
            for (IJSFunction func : funcs)
                addLibrary(library, func);
        }
        Collections.sort(library, new Comparator<List<String>>(){
            @Override
            public int compare(List<String> o1, List<String> o2)
            {
                return o1.get(0).compareTo(o2.get(0));
            }
        });
        return library;
    }
    private static void addLibrary(List<List<String>> library, IJSFunction func)
    {
        for (String name : func.getNames())
        {
            List<String> lib = new ArrayList<String>();
            lib.add(name);
            if (func instanceof IJSFunction2)
            {
                String[] desc = ((IJSFunction2)func).getDescription(name);
                if (desc != null)
                    ArrayUtils.addAll(lib, desc);
            }
            library.add(lib);
        }
    }
    
    private static void fireListeners(JSEvent ev)
    {
        for (IJSListener listener : mListeners.toArray(new IJSListener[0]))
            listener.expressionEvaluated(ev);
    }
    
    public static void addListener(IJSListener listener)
    {
        mListeners.add(listener);
    }
    
    public static void removeListener(IJSListener listener)
    {
        mListeners.remove(listener);
    }
}
