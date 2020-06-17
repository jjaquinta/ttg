package ttg.logic.war.eval;

public class Token
{
	public int mType;

	// possible types
	public final static int CONS = 1;
	public final static int NUMB = 2;
	public final static int FUNC = 3;
	public final static int OPER = 4;

	public Object mName = null;

	public Token(int type, Object name)
	{
		mType = type;
		mName = name;
	}

	public Token(Token t)
	{
		mType = t.mType;
		mName = t.mName;
	}

	public String nameStr()
	{
		return mName.toString();
	}
}
