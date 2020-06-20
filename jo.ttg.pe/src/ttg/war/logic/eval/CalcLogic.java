package ttg.war.logic.eval;

public class CalcLogic
{

	// it's so good that all operations have exactly 2 arguments !
	public static Object calcOperation(char op, Object arg1, Object arg2)
		throws IllegalArgumentException
	{
		Object ret;

		if (op == '+')
			ret = calcAdd(arg1, arg2);
		else if (op == '-')
			ret = calcSub(arg1, arg2);
		else if (op == '*')
			ret = calcMult(arg1, arg2);
		else if (op == '/')
			ret = calcDiv(arg1, arg2);
		else if (op == '%')
			ret = calcMod(arg1, arg2);
		else if (op == '^')
			ret = calcPow(arg1, arg2);
		else if (op == '|')
			ret = calcOr(arg1, arg2);
		else if (op == '&')
			ret = calcAnd(arg1, arg2);
		else if (op == '=')
			ret = calcEqu(arg1, arg2);
		else if (op == '<')
			ret = calcLT(arg1, arg2);
		else if (op == '>')
			ret = calcGT(arg1, arg2);
		else
			throw (
				new IllegalArgumentException("Unsupported operation: " + op));

//		System.out.println(
//			arg1.toString()
//				+ " "
//				+ op
//				+ " "
//				+ arg2.toString()
//				+ " = "
//				+ ret.toString());

		return ret;
	}

	public static boolean makeBoolean(Object o)
	{
		if (o instanceof Number)
		{
			return ((Number)o).doubleValue() != 0;
		}
		if (o instanceof Boolean)
		{
			return ((Boolean)o).booleanValue();
		}
		if (o == null)
			return false;
		String s = o.toString();
		if (s.equalsIgnoreCase("true"))
			return true;
		if (s.equalsIgnoreCase("false"))
			return false;
		if (s.equals("0"))
			return false;
		return o.toString().length() > 0;
	}

	public static int makeInteger(Object o)
	{
		if (o instanceof Number)
		{
			return ((Number)o).intValue();
		}
		if (o instanceof Boolean)
		{
			return ((Boolean)o).booleanValue() ? 1 : 0;
		}
		if (o == null)
			return 0;
		String s = o.toString();
		try
		{
			return Integer.parseInt(s.trim());
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	public static double makeDouble(Object o)
	{
		if (o instanceof Number)
		{
			return ((Number)o).doubleValue();
		}
		if (o instanceof Boolean)
		{
			return ((Boolean)o).booleanValue() ? 1.0 : 0.0;
		}
		if (o == null)
			return 0;
		String s = o.toString();
		try
		{
			return Double.parseDouble(s.trim());
		}
		catch (NumberFormatException e)
		{
			return 0;
		}
	}

	private static Object calcAdd(Object arg1, Object arg2)
	{
		if (!(arg1 instanceof Double) || !(arg2 instanceof Double))
			return arg1.toString() + arg2.toString();
		return new Double(
			((Double)arg1).doubleValue() + ((Double)arg2).doubleValue());
	}

	private static Object calcSub(Object arg1, Object arg2)
	{
		return new Double(makeDouble(arg1) - makeDouble(arg2));
	}

	private static Object calcMult(Object arg1, Object arg2)
	{
		return new Double(makeDouble(arg1) * makeDouble(arg2));
	}

	private static Object calcDiv(Object arg1, Object arg2)
	{
		return new Double(makeDouble(arg1) / makeDouble(arg2));
	}

	private static Object calcMod(Object arg1, Object arg2)
	{
		return new Double(makeDouble(arg1) % makeDouble(arg2));
	}

	private static Object calcPow(Object arg1, Object arg2)
	{
		return new Double(Math.pow(makeDouble(arg1), makeDouble(arg2)));
	}

	private static Object calcOr(Object arg1, Object arg2)
	{
		return new Boolean(makeBoolean(arg1) || makeBoolean(arg2));
	}

	private static Object calcAnd(Object arg1, Object arg2)
	{
		return new Boolean(makeBoolean(arg1) && makeBoolean(arg2));
	}

	private static Object calcEqu(Object arg1, Object arg2)
	{
		boolean ret;
		if (!(arg1 instanceof Double) || !(arg2 instanceof Double))
			ret = arg1.toString().equalsIgnoreCase(arg2.toString());
		else
			ret = ((Double)arg1).doubleValue() == ((Double)arg2).doubleValue();
		return new Boolean(ret);
	}

	private static Object calcLT(Object arg1, Object arg2)
	{
		boolean ret;
		if (!(arg1 instanceof Double) || !(arg2 instanceof Double))
			ret = arg1.toString().length() < arg2.toString().length();
		else
			ret = ((Double)arg1).doubleValue() < ((Double)arg2).doubleValue();
		return new Boolean(ret);
	}

	private static Object calcGT(Object arg1, Object arg2)
	{
		boolean ret;
		if (!(arg1 instanceof Double) || !(arg2 instanceof Double))
			ret = arg1.toString().length() > arg2.toString().length();
		else
			ret = ((Double)arg1).doubleValue() > ((Double)arg2).doubleValue();
		return new Boolean(ret);
	}

}
