package ttg.war.logic.eval;

public class TokenNode
{
	public TokenNode mPrev = null;
	public TokenNode mNext = null;
	public Token mToken = null;

	public TokenNode(Token t, TokenNode prevNode, TokenNode nextNode)
	{
		mToken = t;
		mPrev = prevNode;
		mNext = nextNode;
	}

	public TokenNode(TokenNode nodeCopy)
	{
		if (nodeCopy != null)
		{
			mPrev = nodeCopy.mPrev;
			mNext = nodeCopy.mNext;
			mToken = nodeCopy.mToken;
		}
	}
}
