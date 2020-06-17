package ttg.logic.war.eval;

public class TokenList
{
	public int mElements = 0;
	public TokenNode mHead = null;
	public TokenNode mTail = null;

	public TokenList()
	{
	}

	public void addHead(Token t)
	{
		if (t != null)
		{
			TokenNode headReference = mHead;
			TokenNode tn = new TokenNode(t, null, null);
			mHead = tn;

			if (mElements == 0)
			{
				// make tail and head to be separate objects
				mTail = new TokenNode(tn);
			}
			else if (mElements == 1)
			{
				mTail.mPrev = mHead;
				mHead.mNext = mTail;
			}
			else
			{
				headReference.mPrev = mHead;
				tn.mNext = headReference;
			}

			mElements++;
		}
	}

	public void addTail(Token t)
	{
		if (t != null)
		{
			TokenNode tailReference = mTail;
			TokenNode tn = new TokenNode(t, null, null);
			mTail = tn;

			if (mElements == 0)
			{
				// make tail and head to be separate objects
				mHead = new TokenNode(tn);
			}
			else if (mElements == 1)
			{
				mHead.mNext = mTail;
				mTail.mPrev = mHead;
			}
			else
			{
				tailReference.mNext = mTail;
				tn.mPrev = tailReference;
			}

			mElements++;
		}
	}

	public Token removeHead()
	{
		if (mHead == null)
			return null;
		else
		{
			TokenNode tn = new TokenNode(mHead);
			mHead = mHead.mNext;
			if (mHead == null)
				mTail = null;

			mElements--;
			return tn.mToken;
		}
	}

	public Token removeTail()
	{
		if (mTail == null)
			return null;
		else
		{
			TokenNode tn = new TokenNode(mTail);
			mTail = mTail.mPrev;
			if (mTail == null)
				mHead = null;

			mElements--;
			return tn.mToken;
		}
	}

	public Token removeAt(TokenNode tn)
	{
		if (tn.mPrev == null) // it's head
			return removeHead();
		else if (tn.mNext == null) // it's tail
			return removeTail();
		else
		{
			TokenNode tnPrev = tn.mPrev;
			tnPrev.mNext = tn.mNext;
			tnPrev.mNext.mPrev = tnPrev;
			mElements--;
			return tn.mToken;
		}
	}

	public void removeAll()
	{
		mHead = null;
		mTail = null;
		mElements = 0;
	}

	public TokenNode previous(TokenNode tn)
	{
		if (tn == null)
			return null;
		return tn.mPrev;
	}

	// including both "from" and "to"
	public static TokenList copyList(TokenNode from, TokenNode to)
	{
		TokenList tl = new TokenList();
		TokenNode tn = from;

		while (tn != null && tn != to)
		{
			tl.addTail(tn.mToken);
			tn = tn.mNext;
		}

		if (tn == to && to != null)
			tl.addTail(tn.mToken);

		return tl;
	}

}
