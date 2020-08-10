package jo.util.table.beans;


public class TableEntry
{
    private int  mChance;
    private String mText;

    public TableEntry()
    {
        mChance = 0;
        mText = "";
    }
	
	public int getChance()
	{
		return mChance;
	}
	
	public void setChance(int v)
	{
		mChance = v;
	}

	public String getText()
	{
		return mText;
	}
	
	public void setText(String v)
	{
		mText = v;
	}

    public String toString()
    {
        return String.valueOf(mChance)+":"+mText;
    }
}
