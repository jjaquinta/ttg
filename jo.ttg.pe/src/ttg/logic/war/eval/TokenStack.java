package ttg.logic.war.eval;

public class TokenStack
{
	TokenList mList = new TokenList();

	public void empty()
	{
		mList.removeAll();
	}

	public void push(Token t)
	{
		mList.addHead(t);
	}

	public Token pop() throws IllegalArgumentException
	{
		if (mList.mElements == 0)
			throw new IllegalArgumentException("Attempt to pop from an empty Postfix stack");

		return mList.removeHead();
	}

	public Token topElement()
	{
		if (mList.mElements == 0)
			throw new IllegalArgumentException("Attempt to access an empty Postfix stack");

		return mList.mHead.mToken;
	}

	public boolean isEmpty()
	{
		return (mList.mElements == 0);
	}
}
