package jo.util.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ExprUtils
{
    public static String expandText(String text, Map<String,Object> props) throws ExpressionEvaluationException
    {
        return expandText(text, props, "%", "%");
    }
    public static String expandText(String text, Map<String,Object> props, String prefix, String suffix) throws ExpressionEvaluationException
    {
        StringBuffer expanded = new StringBuffer();
        int o = 0;
        for (;;)
        {
            int beginPrefix = text.indexOf(prefix, o);
            if (beginPrefix < 0)
            {
                expanded.append(text.substring(o));
                break;
            }
            expanded.append(text.substring(o, beginPrefix));
            int endPrefix = beginPrefix + prefix.length();
            int beginSuffix = text.indexOf(suffix, endPrefix);
            if (beginSuffix < 0)
            {
                expanded.append(text.substring(beginPrefix));
                break;
            }
            int endSuffix = beginSuffix + suffix.length();
            String expr = text.substring(endPrefix, beginSuffix);
            Object eval = evalObject(expr, props).toString();
            expanded.append(eval);
            o = endSuffix;
        }
        return expanded.toString();
    }
    
	public static long eval(String expr, Map<String,Object> props) throws ExpressionEvaluationException
	{
		List<ExprToken> toks = tokenize(expr);
		Double ret = (Double)eval(toks, 0, toks.size(), props);
		//DebugUtils.info(expr + " = "+ret);
		return ret.longValue();
	}

    public static Object evalObject(String expr, Map<String,Object> props) throws ExpressionEvaluationException
    {
        List<ExprToken> toks = tokenize(expr);
        Object ret = eval(toks, 0, toks.size(), props);
        //DebugUtils.info(expr + " = "+ret);
        return ret;
    }

    public static boolean evalBoolean(String expr, Map<String,Object> props) throws ExpressionEvaluationException
    {
        Object ret = evalObject(expr, props);
        if (ret == null)
            return false;
        if (ret instanceof Boolean)
            return ((Boolean)ret).booleanValue();
        if (ret instanceof String)
        {
            if ("true".equalsIgnoreCase((String)ret))
                return true;
            if ("false".equalsIgnoreCase((String)ret))
                return true;
            if ("".equalsIgnoreCase((String)ret))
                return false;
            try
            {
                long v = Long.parseLong((String)ret);
                return v != 0;
            }
            catch (NumberFormatException e)
            {
                return true;
            }
        }
        if (ret instanceof Number)
        {
            return Math.abs(((Number)ret).doubleValue()) < 0.0001;
        }
        return true;
    }

	private static Object eval(List<ExprToken> toks, int start, int end, Map<String,Object> props) throws ExpressionEvaluationException
	{
		//OutputLogic.println("Eval("+start+","+end+")");
		Object[] ret = new Object[1];
		Object[] arg = new Object[1];
		start = evalRVal(toks, start, end, props, ret);
		while (start < end)
		{
			ExprToken tok = (ExprToken)toks.get(start);
			if (tok.mType == ExprToken.T_OPERATOR)
            {
                if (tok.mOVal == '.')
                {   // don't eval arg
                    arg[0] = ((ExprToken)toks.get(start + 1)).mSVal;
                    start += 2; 
                }
                else
                    start = evalRVal(toks, start+1, end, props, arg);
    			ret[0] = evalOperation(tok.mOVal, ret[0], arg[0]);
            }
            else if (tok.mType == ExprToken.T_RELOP)
            {
                start = evalRVal(toks, start+1, end, props, arg);
                ret[0] = evalRelativeOperation(tok.mOVal, ret[0], arg[0]);
            }
            else
                throw new ExpressionEvaluationException("Expected operator");
		}
		return ret[0];
	}
	
	private static Object evalOperation(char op, Object arg1, Object arg2) throws ExpressionEvaluationException
	{
        if (op == '.')
            return evalProperty(arg1, arg2.toString());
        if (arg1 instanceof Boolean)
            arg1 = ((Boolean)arg1).booleanValue() ? new Double(1) : new Double(0);
        if (arg2 instanceof Boolean)
            arg2 = ((Boolean)arg2).booleanValue() ? new Double(1) : new Double(0);
		if (!(arg1 instanceof Double) || !(arg2 instanceof Double))
			throw new ExpressionEvaluationException("Operator must act on numeric");
		Double d1 = (Double)arg1;
		Double d2 = (Double)arg2;
		switch (op)
		{
			case '+':
				return new Double(d1.doubleValue()+d2.doubleValue());
			case '-':
				return new Double(d1.doubleValue()-d2.doubleValue());
			case '*':
				return new Double(d1.doubleValue()*d2.doubleValue());
			case '/':
				return new Double(d1.doubleValue()/d2.doubleValue());
		}
		throw new ExpressionEvaluationException("Unknown operator "+op);
	}
    
    private static Object evalProperty(Object obj, String prop)
    {
        if (obj == null)
            return null;
        try
        {
            BeanInfo info = Introspector.getBeanInfo(obj.getClass());
            PropertyDescriptor[] props = info.getPropertyDescriptors();
            for (int i = 0; i < props.length; i++)
                if (props[i].getName().equalsIgnoreCase(prop))
                {
                    return props[i].getReadMethod().invoke(obj, new Object[0]);
                }
            Field[] fields = obj.getClass().getFields();
            for (int i = 0; i < fields.length; i++)
                if (fields[i].getName().equalsIgnoreCase(prop))
                    return fields[i].get(obj);
        }
        catch (Exception e)
        {
            
        }
        return null;
    }
    
    private static Object evalRelativeOperation(char op, Object arg1, Object arg2) throws ExpressionEvaluationException
    {
        switch (op)
        {
            case ExprToken.O_EQ:
                return new Boolean(evalRelopEquals(arg1, arg2));
            case ExprToken.O_NE:
                return new Boolean(!evalRelopEquals(arg1, arg2));
            case ExprToken.O_LT:
                return new Boolean(evalRelopLessThan(arg1, arg2));
            case ExprToken.O_GT:
                return new Boolean(evalRelopGreaterThan(arg1, arg2));
            case ExprToken.O_LTE:
                return new Boolean(!evalRelopGreaterThan(arg1, arg2));
            case ExprToken.O_GTE:
                return new Boolean(!evalRelopLessThan(arg1, arg2));
        }
        throw new ExpressionEvaluationException("Unknown relative operator "+op);
    }
    
    private static boolean evalRelopEquals(Object arg1, Object arg2)
    {
        if (arg1 == null)
            return arg2 == null;
        if (arg2 == null)
            return false;
        if ((arg1 instanceof Number) && (arg2 instanceof Number))
            return ((Number)arg1).doubleValue() == ((Number)arg2).doubleValue();
        return arg1.toString().equalsIgnoreCase(arg2.toString());
    }
    
    private static boolean evalRelopLessThan(Object arg1, Object arg2)
    {
        if (arg1 == null)
            return arg2 == null;
        if (arg2 == null)
            return false;
        if ((arg1 instanceof Number) && (arg2 instanceof Number))
            return ((Number)arg1).doubleValue() < ((Number)arg2).doubleValue();
        return arg1.toString().compareTo(arg2.toString()) > 0;
    }
    
    private static boolean evalRelopGreaterThan(Object arg1, Object arg2)
    {
        if (arg1 == null)
            return arg2 == null;
        if (arg2 == null)
            return false;
        if ((arg1 instanceof Number) && (arg2 instanceof Number))
            return ((Number)arg1).doubleValue() < ((Number)arg2).doubleValue();
        return arg1.toString().compareTo(arg2.toString()) < 0;
    }
	
	private static int evalRVal(List<ExprToken> toks, int start, int end, Map<String,Object> props, Object[] ret) throws ExpressionEvaluationException
	{
		//OutputLogic.println("EvalRVal("+start+","+end+")");
		ExprToken tok = (ExprToken)toks.get(start);
		if (tok.mType == ExprToken.T_NUMBER)
		{
			ret[0] = new Double(tok.mDVal);
			return start + 1;
		}
		if (tok.mType == ExprToken.T_LPAREN)
		{
			end = findToken(toks, start+1, end, ExprToken.T_RPAREN);
			if (end < 0)
				throw new ExpressionEvaluationException("Unclosed parenthetical expression.");
			ret[0] = eval(toks, start+1, end, props);
			return end + 1;
		}
		if (tok.mType == ExprToken.T_STRING)
		{
			if ((start + 1 < end) && (((ExprToken)toks.get(start+1)).mType == ExprToken.T_LPAREN))
			{	// function
				end = findToken(toks, start+2, end, ExprToken.T_RPAREN);
				if (end < 0)
					throw new ExpressionEvaluationException("Unclosed function call.");
				List<Object> args = new ArrayList<Object>();
				start += 2;
				for (;;)
				{
					int comma = findToken(toks, start, end, ExprToken.T_COMMA);
					if (comma < 0)
					{
						try
                        {
                            args.add(eval(toks, start, end, props));
                        }
                        catch (ExpressionEvaluationException e)
                        {
                            
                        }
						break;
					}
                    try
                    {
    					args.add(eval(toks, start, comma, props));
                    }
                    catch (ExpressionEvaluationException e)
                    {
                        
                    }
					start = comma + 1;
				}
				ret[0] = evalFunction(tok.mSVal, args.toArray());
				return end + 1;
			}
			else
			{	// lookup
				ret[0] = props.get(tok.mSVal);
				if (ret[0] == null)
				{                    
                    //throw new ExpressionEvaluationException("property "+tok.mSVal+" not present");
                    ret[0] = tok.mSVal;
                }
				return start + 1;
			}
		}
		throw new ExpressionEvaluationException("Invalid rval");
	}
	
	private static Object evalFunction(String name, Object[] args) throws ExpressionEvaluationException
	{
		Object ret = null;
		if (name.equalsIgnoreCase("min") || name.equalsIgnoreCase("minimum"))
		{
			for (int i = 0; i < args.length; i++)
			{
				if (args[i] instanceof Double)
				{
					if (ret == null)
						ret = args[i];
					else if (((Double)args[i]).doubleValue() < ((Double)ret).doubleValue()) 
						ret = args[i];
				}
			}
		}
		else if (name.equalsIgnoreCase("max") || name.equalsIgnoreCase("maximum"))
		{
			for (int i = 0; i < args.length; i++)
			{
				if (args[i] instanceof Double)
				{
					if (ret == null)
						ret = args[i];
					else if (((Double)args[i]).doubleValue() > ((Double)ret).doubleValue()) 
						ret = args[i];
				}
			}
		}
        else if (name.equalsIgnoreCase("if"))
        {
            if (args.length == 3)
            {
                boolean v = false;
                if (args[0] == null)
                    v = false;
                else if (args[0] instanceof Number)
                    v = ((Number)args[0]).intValue() != 0;
                else if (args[0] instanceof Boolean)
                    v = ((Boolean)args[0]).booleanValue();
                else if (args[0] instanceof String)
                {
                    String sarg = (String)args[0];
                    if (sarg.equals("true"))
                        v = true;
                    else if (sarg.equals("false"))
                        v = false;
                    else 
                        v = ((String)args[0]).length() != 0;
                }
                ret = v ? args[1] : args[2];
            }
        }
		else
			throw new ExpressionEvaluationException("Unknown function "+name);
		return ret;
	}
	
	private static int findToken(List<ExprToken> toks, int start, int end, int type)
	{
		int stack = 0;
		for (int i = start; i < end; i++)
		{
			ExprToken tok = (ExprToken)toks.get(i);
			if (tok.mType == ExprToken.T_LPAREN)
				stack++;
			else if ((tok.mType == ExprToken.T_RPAREN) && (stack > 0))
				stack--;
			else if ((tok.mType == type) && (stack == 0))
				return i;
		}
		return -1;
	}
	
	private static List<ExprToken> tokenize(String _expr) throws ExpressionEvaluationException
	{
		StringBuffer expr = new StringBuffer(_expr);
		List<ExprToken> ret = new ArrayList<ExprToken>();
		for (;;)
		{
			ExprToken tok = getNextToken(expr);
			if (tok == null)
				break;
			ret.add(tok);
		}
		return ret;
	}
	
	private static ExprToken getNextToken(StringBuffer expr) throws ExpressionEvaluationException
	{
		ExprToken tok = new ExprToken();
		char ch;
		for (;;)
		{
			if (expr.length() == 0)
				return null;
			ch = expr.charAt(0);
			if (!Character.isWhitespace(ch))
				break;
			expr.deleteCharAt(0);
		}
		if (Character.isDigit(ch))
		{
			StringBuffer acc = new StringBuffer();
			do
			{
				acc.append(ch);
				expr.deleteCharAt(0);
				if (expr.length() == 0)
					break;
				ch = expr.charAt(0);
			} while (Character.isDigit(ch) || (ch == '.'));
			tok.mType = ExprToken.T_NUMBER;
			tok.mDVal = Double.parseDouble(acc.toString());
		}
		else if (ch == '(')
		{
			tok.mType = ExprToken.T_LPAREN;
			expr.deleteCharAt(0);
		}
		else if (ch == ')')
		{
			tok.mType = ExprToken.T_RPAREN;
			expr.deleteCharAt(0);
		}
		else if (ch == ',')
		{
			tok.mType = ExprToken.T_COMMA;
			expr.deleteCharAt(0);
		}
		else if ((ch == '+') || (ch == '-') || (ch == '*') || (ch == '/') || (ch == '.'))
		{
			tok.mType = ExprToken.T_OPERATOR;
			tok.mOVal = ch;
			expr.deleteCharAt(0);
		}
        else if (ch == '=')
        {
            tok.mType = ExprToken.T_RELOP;
            expr.deleteCharAt(0);
            ch = expr.charAt(0);
            if (ch == '=')
            {
                tok.mOVal = ExprToken.O_EQ;
                expr.deleteCharAt(0);
            }
            else
                throw new ExpressionEvaluationException("Unknown token: "+ch);
        }
        else if (ch == '<')
        {
            tok.mType = ExprToken.T_RELOP;
            expr.deleteCharAt(0);
            ch = expr.charAt(0);
            if (ch == '=')
            {
                tok.mOVal = ExprToken.O_LTE;
                expr.deleteCharAt(0);
            }
            else if (ch == '>')
            {
                tok.mOVal = ExprToken.O_NE;
                expr.deleteCharAt(0);
            }
            else
            {
                tok.mOVal = ExprToken.O_LT;
            }
        }
        else if (ch == '>')
        {
            tok.mType = ExprToken.T_RELOP;
            expr.deleteCharAt(0);
            ch = expr.charAt(0);
            if (ch == '=')
            {
                tok.mOVal = ExprToken.O_GTE;
                expr.deleteCharAt(0);
            }
            else
            {
                tok.mOVal = ExprToken.O_GT;
            }
        }
        else if (ch == '\"')
        {
            StringBuffer acc = new StringBuffer();
            do
            {
                acc.append(ch);
                expr.deleteCharAt(0);
                if (expr.length() == 0)
                    break;
                ch = expr.charAt(0);
            } while ((ch != '\"'));
            expr.deleteCharAt(0);
            tok.mType = ExprToken.T_STRING;
            tok.mSVal = acc.toString().substring(1);
        }
        else if (ch == '\'')
        {
            StringBuffer acc = new StringBuffer();
            do
            {
                acc.append(ch);
                expr.deleteCharAt(0);
                if (expr.length() == 0)
                    break;
                ch = expr.charAt(0);
            } while ((ch != '\''));
            expr.deleteCharAt(0);
            tok.mType = ExprToken.T_STRING;
            tok.mSVal = acc.toString().substring(1);
        }
		else if (Character.isLetter(ch) || (ch == '_'))
		{
			StringBuffer acc = new StringBuffer();
			do
			{
				acc.append(ch);
				expr.deleteCharAt(0);
				if (expr.length() == 0)
					break;
				ch = expr.charAt(0);
			} while (Character.isLetter(ch) || (ch == '_'));
			tok.mType = ExprToken.T_STRING;
			tok.mSVal = acc.toString();
		}
		else
			throw new ExpressionEvaluationException("Unknown token: "+ch);
		return tok;
	}
}

class ExprToken
{
	public static final int T_NUMBER = 0;
	public static final int T_OPERATOR = 1;
    public static final int T_RELOP = 2;
	public static final int T_STRING = 3;
	public static final int T_LPAREN = 4;
	public static final int T_RPAREN = 5;
	public static final int T_COMMA = 6;
    
    public static final char O_EQ = 0x0101;
    public static final char O_NE = 0x0102;
    public static final char O_LT = 0x0103;
    public static final char O_GT = 0x0104;
    public static final char O_LTE = 0x0105;
    public static final char O_GTE = 0x0106;
	
	public int	mType;
	public double	mDVal;
	public String	mSVal;
	public char		mOVal;
}