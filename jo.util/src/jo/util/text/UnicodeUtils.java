package jo.util.text;

public class UnicodeUtils 
{
	public static String insertEscapes(String txt)
	{
		StringBuffer ret = new StringBuffer();
		char[] c = txt.toCharArray();
		for (int i = 0; i < c.length; i++)
			if ((c[i] >= ' ') && (c[i] <= '~'))
				ret.append(c[i]);
			else
			{
				ret.append("\\u");
				String hex = "0000"+Integer.toHexString(c[i]);
				hex = hex.substring(hex.length() - 4);
				ret.append(hex);
			}
		return ret.toString();
	}

	public static String removeEscapes(String txt)
	{
		StringBuffer ret = new StringBuffer();
		char[] c = txt.toCharArray();
		for (int i = 0; i < c.length; i++)
			if (c[i] != '\\')
				ret.append(c[i]);
			else
				switch (c[++i])
				{
					case 'r':
						ret.append('\r');
						break;
					case 'n':
						ret.append('\n');
						break;
					case 'b':
						ret.append('\b');
						break;
					case '0':
						ret.append('\0');
						break;
					case 'u':
						int ch = Integer.parseInt(new String(c, i + 1, 4), 16);
						ret.append((char)ch);
						i += 4;
						break;
					default:
						ret.append('\\');
						ret.append(c[i]);
						break;
				}
		return ret.toString();
	}
}
