/*
 * Created on Oct 9, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.cmd;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import jo.util.utils.DebugUtils;
import jo.util.utils.io.OutputLogic;


/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ArgLogic
{
	public static boolean parseArgs(Object bean, String[] argv)
	{
		String key, val;
		boolean plus;
		boolean error = false;
		for (int i = 0; i < argv.length; i++)
		{
			if (argv[i].equals("-"))
			{
				((BaseCmd)bean).knownArg("", "");
				displayPrompt(bean);
				continue;
			}
			if (!argv[i].startsWith("-") && !argv[i].startsWith("+"))
			{
				key = ((BaseCmd)bean).defaultArg();
				if (key == null)
				{
					DebugUtils.warn("Unknown command line parameter: "+argv[i]);
					error = true;
					continue;
				}
				plus = false;
				val = argv[i];
			}
			else
			{
				plus = argv[i].startsWith("+");
				key = argv[i].substring(1);
				int o = key.indexOf("=");
				if (o > 0)
				{
					val = key.substring(o+1);
					key = key.substring(0, o);
				}
				else
				{
					if ((i + 1 == argv.length) || argv[i+1].startsWith("-") || argv[i+1].startsWith("+"))
					{
						if (!plus)
							val = "false";
						else
							val = "true";
					}
					else
						val = argv[++i];
				}
			}
			if (!plus)
			    while (i +1 < argv.length)
			    {
			        if (argv[i+1].startsWith("-") || argv[i+1].startsWith("+"))
			            break;
			        val += " ";
			        val += argv[++i];
			    }
			if (key.equals("?") || key.equalsIgnoreCase("help"))
			{
				((BaseCmd)bean).knownArg(key, val);
				printHelp(bean);
				error = true;
			} // end if
			else
			{
				((BaseCmd)bean).knownArg(key, val);
				if (!set(bean, key, val))
				{
					if (bean instanceof BaseCmd)
					{
						if (!((BaseCmd)bean).unknownArg(key, val))
							error = true;
					}
					else
					{
						DebugUtils.warn("Can't set: "+key+" to "+val);
						error = true;
					}
					continue;
				}  // end if
			}  // end else
		}  // end for
		return error;
	}  // end parseArgs

	/**
	 * Display a prompt interface to the user.
	 * @param o BaseCmd Implementation object
	 * @author christopher_roblee@us.ibm.com
	 */
	static void displayPrompt(Object o)
	{
		String key, val = "";
		BufferedReader stdin = new BufferedReader(
							 new InputStreamReader(System.in));
		String uPrompt = "";
		String line = "";
		Field[] fields = o.getClass().getFields();  // get all the fields of this class
		if(fields.length > 0)       // only display welcome if new execution
		{
			OutputLogic.println("\nWelcome to NeoTools. Enter Parameters (Defaults listed in brackets):");
			OutputLogic.println("******************************************************************");
		}  // end if
		for (int i = 0; i < fields.length; i++)
		{
			if (fields[i].getName().toLowerCase().startsWith("help"))  // get args
			{
				try
				{
					Object v = get(o, fields[i].getName());     // get value
					key = fields[i].getName().substring(4);
					OutputLogic.println("");
					uPrompt= "Choose "+v.toString()+" ["+get(o, key)+"]";
					line = "";
					for(int j = 0; j <uPrompt.length(); j++)
					   line += "-";
					OutputLogic.println(uPrompt);
					OutputLogic.println(line);
					if (v != null)
					{
						if (!displayPromptOptions(o, key, stdin))
						{   // just prompt normally
							val= stdin.readLine();
							((BaseCmd)o).knownArg(key, val);
							val = val.trim();       // remove whitespace
							if (val.length() > 0)       // only call set if new value entered at prompt
								if(!set(o, key, val))
									  OutputLogic.println("Can't set: "+key+" to "+val);
						}  // end else
					} // end if not null
				}  // end try
				catch (Exception e)
				{
				}
			}  // end if arg
		}  // end for
	}  // end displayPrompt

	private static boolean displayPromptOptions(Object o, String key, BufferedReader stdin)
		throws IOException
	{
		// Find and list all available options:
		Method m = getMethod(o, "getOptions"+key);
		if (m == null)
			return false;
		Vector<?> options;
		try
		{
			options = (Vector<?>)m.invoke(o, new Object[0]);
		}
		catch (Exception e)
		{
			return false;
		}
		while (true)
		{
			for (int j = 0; j < options.size(); j++)         // display option list
				OutputLogic.println("["+j+"] "+options.get(j).toString());
			OutputLogic.println("[x] Exit");
			String inbuf = stdin.readLine();
			if (inbuf.equalsIgnoreCase("x"))
				break;
			inbuf = inbuf.trim();
			if (inbuf.length() == 0)
				break;
			int choice = -1;
			try
			{
				 choice = Integer.parseInt(inbuf);        // read user input as integer
			}
			catch(Exception e){}                        // is this an integer?
			if( (choice >= 0) && (choice < options.size()))      // check range
			{
				Object val = options.get(choice);
				((BaseCmd)o).knownArg(key, val.toString());
				if(!set(o, key, val)) // get script name
					DebugUtils.warn("cannot set: " +key +","+val);
				if (!getType(o, key).getName().equals("java.util.Vector"))
					break;   // selection made
			} // end if
			else
				OutputLogic.println("\nInvalid option, re-enter!");   // repeat loop
		}  // end while
		return true;
	}

	private static PropertyDescriptor[] getSupportedProps(Object o)
	{
		BeanInfo info;
		try
		{
			info = Introspector.getBeanInfo(o.getClass());
		}
		catch (IntrospectionException e1)
		{
			return new PropertyDescriptor[0];
		}
		PropertyDescriptor[] props = info.getPropertyDescriptors();
		List<PropertyDescriptor> arr = new ArrayList<PropertyDescriptor>();
		for (int i = 0; i < props.length; i++)
			if ((props[i].getReadMethod() != null) && (props[i].getWriteMethod() != null))
				arr.add(props[i]);
		PropertyDescriptor[] ret = new PropertyDescriptor[arr.size()];
		arr.toArray(ret);
		return ret;
	}

    /*
	private static PropertyDescriptor getSupportedProp(Object o, String key)
	{
		PropertyDescriptor[] props = getSupportedProps(o);
		for (int i = 0; i < props.length; i++)
			if (props[i].getName().equalsIgnoreCase(key))
				return props[i];
		return null;
	}
    */

	public static void printArgs(Object o)
	{
		PropertyDescriptor[] props = getSupportedProps(o);
		for (int i = 0; i < props.length; i++)
			if ((props[i].getReadMethod() != null) && (props[i].getWriteMethod() != null))
			{
				try
				{
					Object val = props[i].getReadMethod().invoke(o, new Object[0]);
					if (val instanceof Vector)
						DebugUtils.info("  -"+props[i].getName()+"="+collateVector((Vector<?>)val));
					else
						DebugUtils.info("  -"+props[i].getName()+"="+val);
				}
				catch (Exception e)
				{
				}
			}
	}

	public static void printHelp(Object o)
	{
		Class<?> c = o.getClass();
		OutputLogic.println("Arguments for "+c.getName());
		PropertyDescriptor[] props = getSupportedProps(o);
		for (int i = 0; i < props.length; i++)
		{
			try
			{
				String fname = props[i].getName();
				OutputLogic.println("  -"+fname+" ["+get(o, fname).toString()+"] "+get(o, "help"+fname));
				printHelpOptions(o, fname);
			}
			catch (Exception e)
			{
			}
		}
	}

	private static void printHelpOptions(Object o, String fname)
	{
		Method m = getMethod(o, "getOptions"+fname);
		if (m == null)
			return;
		try
		{
			Vector<?> v = (Vector<?>)m.invoke(o, new Object[0]);
			if (v != null)
				OutputLogic.println("   options: "+collateVector(v));
		}
		catch (Exception e)
		{
		}
	}

	public static boolean set(Object o, String key, Object val)
	{
		Field f = getField(o, key);
		if ((f != null) && setField(f, o, val))
		{
			return true;
		}
		Method m = getMethod(o, "set"+key);
		if ((m != null) && setMethod(m, o, val))
		{
			return true;
		}
		return false;
	}

	public static Object get(Object o, String key)
	{
		Field f = getField(o, key);
		if (f != null)
		{
			try
			{
				return f.get(o);
			}
			catch (Exception e)
			{
			}
		}
		Method m = getMethod(o, "get"+key);
		if (m != null)
		{
			try
			{
				return m.invoke(o, new Object[0]);
			}
			catch (Exception e)
			{
			}
		}
		return null;
	}

	public static Class<?> getType(Object o, String key)
	{
		Field f = getField(o, key);
		if (f != null)
		{
			return f.getType();
		}
		Method m = getMethod(o, "get"+key);
		if (m != null)
		{
			return m.getReturnType();
		}
		return null;
	}

	public static Field   getField(Object o, String name)
	{
		Field[] fields = o.getClass().getFields();
		for (int i = 0; i < fields.length; i++)
			if (fields[i].getName().equalsIgnoreCase(name))
				return fields[i];
		return null;
	}

	public static Method  getMethod(Object o, String name)
	{
		Method[] methods = o.getClass().getMethods();
		for (int i = 0; i < methods.length; i++)
			if (methods[i].getName().equalsIgnoreCase(name))
				return methods[i];
		return null;
	}

	public static boolean setField(java.lang.reflect.Field f, Object that, Object val)
	{
		if (f == null)
			return false;
		try
		{
			Class<?> ftype = f.getType();
			Class<?> vtype = (val == null) ? ftype : val.getClass();
			if (ftype.isAssignableFrom(vtype))
			{
				f.set(that, val);
				return true;
			}
			else if (ftype.getName().equals("java.util.Vector"))
			{
				@SuppressWarnings("unchecked")
                Vector<Object> v = (Vector<Object>)f.get(that);
				if (v == null)
					return false;
				v.addElement(val);
			}
			if (vtype.getName().equals("java.lang.String"))
			{
				if (ftype.getName().equals("int"))
					f.setInt(that, Integer.parseInt((String)val));
				else if (ftype.getName().equals("long"))
					f.setLong(that, Long.parseLong((String)val));
				else if (ftype.getName().equals("double"))
					f.setDouble(that, Double.valueOf((String)val).doubleValue());
				else if (ftype.getName().equals("boolean"))
					f.setBoolean(that, Boolean.valueOf((String)val).booleanValue());
				else
					return false;
				return true;
			}
			else if (vtype.getName().equals("java.lang.Integer") && ftype.getName().equals("int"))
			{
				f.setInt(that, ((Integer)val).intValue());
				return true;
			}
			else if (vtype.getName().equals("java.lang.Long") && ftype.getName().equals("long"))
			{
				f.setLong(that, ((Long)val).longValue());
				return true;
			}
			else if (vtype.getName().equals("java.lang.Double") && ftype.getName().equals("double"))
			{
				f.setDouble(that, ((Double)val).doubleValue());
				return true;
			}
			else if (vtype.getName().equals("java.lang.Boolean") && ftype.getName().equals("boolean"))
			{
				f.setBoolean(that, ((Boolean)val).booleanValue());
				return true;
			}
			else
			{
				DebugUtils.trace("Unassignable val type "+vtype.getName()+" from "+ftype.getName()+".");
				return false;
			}
		}
		catch (Exception e)
		{
			DebugUtils.trace("setField("+f+", "+val+"): "+e.toString());
		}
		return false;
	}

	public static boolean setMethod(java.lang.reflect.Method m, Object that, Object val)
	{
		if (m == null)
			return false;
		Object[] args = new Object[1];
//Util.DebugLogic.trace("setMethod trying "+that+"."+m.getName()+"("+val+":"+val.getClass()+")");
		try
		{
			Class<?> ftype = m.getParameterTypes()[0];
			Class<?> vtype = (val == null) ? ftype : val.getClass();
			if (ftype.isAssignableFrom(vtype))
			{
				args[0] = val;
			}
			else if (ftype.getName().equals("java.util.Vector"))
			{
				@SuppressWarnings("unchecked")
                Vector<Object> v = (Vector<Object>)get(that, m.getName().substring(3));
				if (v != null)
					v.addElement(val);
				else
					return false;
				return true;
			}
			else if (vtype.getName().equals("java.lang.String"))
			{
				if (ftype.getName().equals("int"))
					args[0] = new Integer((String)val);
				else if (ftype.getName().equals("long"))
					args[0] = new Long((String)val);
				else if (ftype.getName().equals("double"))
					args[0] = new Double ((String)val);
				else if (ftype.getName().equals("boolean"))
					args[0] = new Boolean((String)val);
				else
				{
					DebugUtils.trace("Unassignable val type "+vtype.getName()+".");
					return false;
				}
			}
			else if (vtype.getName().equals("java.lang.Integer") && ftype.getName().equals("int"))
			{
				args[0] = val;
			}
			else if (vtype.getName().equals("java.lang.Long") && ftype.getName().equals("long"))
			{
				args[0] = val;
			}
			else if (vtype.getName().equals("java.lang.Double") && ftype.getName().equals("double"))
			{
				args[0] = val;
			}
			else if (vtype.getName().equals("java.lang.Boolean") && ftype.getName().equals("boolean"))
			{
				args[0] = val;
			}
			else
			{
				DebugUtils.trace("Unassignable val type "+vtype.getName()+" from "+ftype.getName()+".");
				return false;
			}
//Util.DebugLogic.trace("setMethod invoking "+that+"."+m.getName()+"("+args[0]+":"+args[0].getClass()+")");
			m.invoke(that, args);

//Util.DebugLogic.trace("setMethod invoked "+that+"."+m.getName()+"("+args[0]+":"+args[0].getClass()+")");
			return true;
		}
		catch (Exception e)
		{
			DebugUtils.trace(that+"."+m.getName()+"("+args[0]+":"+args[0].getClass()+"): "+e.toString());
		}
		return false;
	}

	public static File    findFile(String fname, String path)
	{
		if (path == null)
			path = ".";
		StringTokenizer st = new StringTokenizer(path, File.pathSeparator);
		while (st.hasMoreTokens())
		{
			String dir = st.nextToken();
			File ret = new File(dir, fname);
			if (ret.exists())
			{
				return ret;
			}
		}
		File ret = new File(".", fname);
		if (ret.exists())
		{
			return ret;
		}
		return null;
	}

	public static String collateArray(String[] arr)
	{
		StringBuffer ret = new StringBuffer();
		for (int i = 0; i < arr.length; i++)
		{
			if (i > 0)
				ret.append(", ");
			ret.append(arr[i]);
		}
		return ret.toString();
	}

	public static String collateVector(Vector<?> v)
	{
		StringBuffer ret = new StringBuffer();
		for (Enumeration<?> e = v.elements(); e.hasMoreElements(); )
		{
			if (ret.length() > 0)
				ret.append(", ");
			ret.append(e.nextElement().toString());
		}
		return ret.toString();
	}

	/**
	 * Look for @<fname> on the command line. Read that file as if it was extra command
	 * line information. Since we want to be compatible with property files, treat
	 * any line without a +/- as if it had a +
	 */

	private static String[] expandArgs(String[] argv)
	{
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < argv.length; i++)
		{
			if (argv[i].startsWith("@"))
			{
				String fname = argv[i].substring(1);
				DebugUtils.info("Reading commands from file "+fname+".");
				try
				{
					BufferedReader r = new BufferedReader(new FileReader(fname));
					for (;;)
					{
						String inbuf = r.readLine();
						if (inbuf == null)
							break;
						inbuf = inbuf.trim();
						if (inbuf.startsWith("#"))
							continue;
						if (!inbuf.startsWith("-") && !inbuf.startsWith("+"))
							inbuf = "+"+inbuf;
						StringTokenizer st = new StringTokenizer(inbuf);
						while (st.hasMoreTokens())
							v.addElement(st.nextToken());
					}
					r.close();
				}
				catch (IOException e)
				{
					DebugUtils.error("Error reading commands from file "+fname+".", e);
				}
			}
			else
				v.addElement(argv[i]);
		}
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}

	/**
	 * Look for arguments split over multiple tokens encased with " marks.
	 * Eg. arg "one two" three should parse 'one two' as a single argument.
	 */

	private static String[] contractArgs(String[] argv)
	{
		Vector<String> v = new Vector<String>();
		for (int i = 0; i < argv.length; i++)
		{
			if (argv[i].startsWith("\""))
			{
				if (argv[i].endsWith("\""))
				{
					v.addElement(argv[i].substring(1, argv[i].length() - 2));
				}
				else
				{
					StringBuffer acc = new StringBuffer(argv[i].substring(1));
					acc.append(" ");
					while (++i < argv.length)
					{
						if (argv[i].endsWith("\""))
						{
							acc.append(argv[i].substring(0, argv[i].length() - 1));
							break;
						}
						else
						{
							acc.append(argv[i]);
							acc.append(" ");
						}
					}
					v.addElement(acc.toString());
				}
			}
			else
				v.addElement(argv[i]);
		}
		String[] ret = new String[v.size()];
		v.copyInto(ret);
		return ret;
	}

	public static String[] normalizeArgs(String[] argv)
	{
		argv = expandArgs(argv);
		argv = contractArgs(argv);
		return argv;
	}

/*
	static public String[][] readCSVFile(String fname) throws IOException
	{
		BufferedReader br = new BufferedReader(new FileReader(fname));
		Vector rows = new Vector();
		Vector cols = new Vector();
		StringBuffer acc = new StringBuffer();
		int state = 0;
		for (;;)
		{
			int ch = br.read();
			if (ch == -1)
				break;
			switch (state)
			{
				case 0: // waiting for field
					if (ch == '"')
						state = 1;
					else if (ch == ',')
					{
						cols.addElement(acc.toString());
						acc.setLength(0);
					}
					else if ((ch == '\r') || (ch == '\n'))
					{
						if (acc.length() > 0)
						{
							cols.addElement(acc.toString());
							acc.setLength(0);
						}
						if (cols.size() > 0)
						{
							String[] c = new String[cols.size()];
							cols.copyInto(c);
							rows.addElement(c);
							cols = new Vector();
						}
					}
					else
						acc.append((char)ch);
					break;
				case 1:
					if (ch == '"')
						state = 0;
					else
						acc.append((char)ch);
					break;
			}
		}
		br.close();
		String[][] ret = new String[rows.size()][];
		rows.copyInto(ret);
		return ret;
	}
*/
}
