package jo.util.utils;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/*
 * Created on Aug 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class StateMachine
{
	private static final Character		DEFAULT_CHAR = new Character((char)0xff);
	private List<Map<Character,String>>   mStates;
	private int				              mState;
	private List<String>	              mResult;
	private StringBuffer	              mAccumulator;
	
	/**
	 * 
	 */
	public StateMachine(String[] stateTable)
	{
		super();
		mStates = new ArrayList<Map<Character,String>>();
		for (int i = 0; i < stateTable.length; i++)
		{
			Map<Character,String> actionTable = new HashMap<Character,String>();
			StringTokenizer directions = new StringTokenizer(stateTable[i], ",");
			while (directions.hasMoreTokens())
			{
				String direction = directions.nextToken();
				int o = direction.indexOf(":");
				String chars = direction.substring(0, o);
				String cmd = direction.substring(o + 1);
				for (int j = 0; j < chars.length(); j++)
				{
					char c = chars.charAt(j);
					if (c == '*')
						actionTable.put(DEFAULT_CHAR, cmd);
					else
						actionTable.put(new Character(c), cmd);
				}
			}
			mStates.add(actionTable);
		}
	}
	
	public void process(Reader rdr) throws IOException
	{
		mState = 0;
		mAccumulator = new StringBuffer();
		mResult = new ArrayList<String>();
		for (;;)
		{
			int ch = rdr.read();
			if (ch < 0)
				break;
			//if (mState > 0) System.out.println("State = "+mState+", input="+(char)ch);
			Map<Character,String> actionTable = mStates.get(mState);
			Character c = new Character((char)ch);
			String cmd = (String)actionTable.get(c);
			if (cmd == null)
				cmd = (String)actionTable.get(DEFAULT_CHAR);
			if (cmd != null)
				execute(cmd, (char)ch);
		}
		rdr.close();
	}
	
	private void execute(String cmd, char ch)
	{
		//System.out.println("Execute "+cmd);
		int newState = 0;
		boolean stateChange = false;
		char[] c = cmd.toCharArray();
		for (int i = 0; i < c.length; i++)
			switch (c[i])
			{
				case '+':
					mAccumulator.append(ch);
					break;
				case '$':
					mResult.add(mAccumulator.toString());
                    //System.out.println("Append "+mAccumulator.toString());
					mAccumulator.setLength(0);
					break;
				case '-':
					mAccumulator.setLength(0);
					break;
				case '#':
					mState = 0;
					break;
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					newState = newState*10 + (c[i] - '0');
					stateChange = true;
					break;
			}
		if (stateChange)
			mState = newState;
	}

	/**
	 * @return
	 */
	public List<String> getResult()
	{
		return mResult;
	}

	/**
	 * @param list
	 */
	public void setResult(List<String> list)
	{
		mResult = list;
	}

}
