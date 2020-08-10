package jo.util.table.logic;

import java.util.Random;

import jo.util.table.beans.Table;
import jo.util.table.beans.TableEntry;
import jo.util.table.beans.TableGroup;

public class RollLogic
{
	public static String roll(TableGroup group, String name, Random rnd)
	{
		Table t;
		if ((name == null) || (name.length() == 0))
		{
            t = TableGroupLogic.getDefaultTable(group); // fill from path
        }
		else
			t = TableGroupLogic.getTable(group, name);
		if (t == null)
			return null;
		return roll(group, t, rnd);
	}

	public static String roll(TableGroup group, Table table, Random rnd)
	{
		int r = rnd.nextInt(table.getTotalCount());
		return roll(group, table, r, rnd);
	}

	public static String roll(TableGroup group, Table table, int r, Random rnd)
	{
		TableEntry ent = table.getNthEntry(r);
		return roll(group, table, ent, rnd);
	}

	public static String roll(TableGroup group, Table table, TableEntry ent, Random rnd)
	{
		return expand(group, rnd, ent.getText());
	}

	private static String expand(TableGroup group, Random rnd, String text)
	{
		//System.out.println("RollLogic.expand = "+text);
        StringBuffer sb = new StringBuffer();
        char[] ch = text.toCharArray();
        for (int i = 0; i < ch.length; i++)
        {
            if (ch[i] == '<')
            {
				int j = extract(ch, i);
                sb.append(function(group, rnd, ch[i + 1], new String(ch, i + 2, j - i - 2)));
                i = j;
            }
            else if (ch[i] == '\\')
            {
                ++i;
                switch (ch[i])
                {
                    case 'n':
                        sb.append('\n');
                        break;
                    case 'r':
                        sb.append('\r');
                        break;
                    case 't':
                        sb.append('\t');
                        break;
                    default:
                        sb.append(ch);
                        break;
                }
            }
            else
                sb.append(ch[i]);
        }
        return sb.toString();
	}
	
	private static int extract(char[] str, int start)
	{
		int deep = 0;
		int end;
		for (end = start; end < str.length; end++)
		{
			if (str[end] == '<')
				deep++;
			else if (str[end] == '>')
			{
				deep--;
				if (deep == 0)
					break;
			}
			else if (str[end] == '\\')
				end++;
		}
		return end;
	}

    private static String function(TableGroup group, Random rnd, char func, String arg)
    {
        int off;
//System.out.println("Function "+func+" on '"+arg+"'");
        switch (func)
        {
            case '~':   // lookup subtable <~subtable.t::roll>
                String which = expand(group, rnd, arg);
                off = which.indexOf("::");
                if (off < 0)
                {
                    Table t = TableGroupLogic.getTable(group, which);
					if (t == null)
						System.out.println("Can't find table "+which);
                    return roll(group, t, rnd);
                }
                else
                {
                    int r = Integer.parseInt(which.substring(off + 2));
                    which = which.substring(0, off);
                    Table t = TableGroupLogic.getTable(group, which);
                    return roll(group, t, r, rnd);
                }
            case '#':   // multiple <#3d4+5|subtext>
                off = arg.indexOf('|');
                if (off < 0)
                    return arg;
                int cnt = eval(group, rnd, arg.substring(0, off));
                arg = arg.substring(off + 1);
                StringBuffer sb = new StringBuffer();
                while (cnt-- > 0)
                    sb.append(expand(group, rnd, arg));
                return sb.toString();
            case '=':   // assign <=key|value>
                off = arg.indexOf('|');
                if (off < 0)
                    return arg;
                String value = expand(group, rnd, arg.substring(off + 1));
                String key = expand(group, rnd, arg.substring(0, off));
                System.getProperties().put(key, value);
                return "";
            case '?':   // lookup <?key>
                return System.getProperties().getProperty(arg);
            case 'r':   // roll some dice  <r3d6+3>
                //return String.valueOf(Tables.rnd.roll(arg));
            case '$':   // calc <$1+2*3>
                int v = eval(group, rnd, arg);
                return String.valueOf(v);
            default:
                System.err.println("Unknown function : "+func);
                return arg;
        }
    }

    private static int eval(TableGroup group, Random rnd, String s)
    {
        String calc = expand(group, rnd, s);
        char[] buf = calc.toCharArray();
        int acc = 0;
        int reg = 0;
        char op = '+';
        for (int i = 0; i < buf.length; i++)
            switch (buf[i])
            {
                case '0': case '1': case '2': case '3': case '4':
                case '5': case '6': case '7': case '8': case '9':
                    reg = reg*10 + (buf[i] - '0');
                    break;
                case '+': case '-': case '*': case '/': case '%':
                case 'd': case '=':
                    acc = perform(group, rnd, acc, op, reg);
                    op = buf[i];
                    reg = 0;
                    break;
            }
        acc = perform(group, rnd, acc, op, reg);
        return acc;
    }

    private static int perform(TableGroup group, Random rnd, int x, char op, int y)
    {
		int ret = y;
        switch (op)
        {
            case '+':
                ret = x + y;
				break;
            case '-':
                ret = x - y;
				break;
            case '*':
                ret = x * y;
				break;
            case '/':
                ret = x / y;
				break;
            case '%':
                ret = x % y;
				break;
            case '=':
                ret = (x == y) ? 1 : 0;
				break;
            case 'd':
				ret = x;
				while (x-- > 0)
					ret += rnd.nextInt(y);
				break;
        }
        return ret;
    }

	public static void main(String[] argv)
	{
		try
		{
			TableGroup group = TableGroupLogic.create();
            for (int i = 0; i < argv.length; i++)
                TableGroupLogic.addTablepath(group, argv[i]);
			Random rnd = new Random();
			for (int i = 0; i < 10; i++)
				System.out.println(roll(group, "", rnd));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}	
