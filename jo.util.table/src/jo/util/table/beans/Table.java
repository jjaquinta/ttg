package jo.util.table.beans;

import java.util.ArrayList;
import java.util.List;

public class Table
{
    private List<TableEntry>   mEntries;
    private String   mComment;
    private String   mName;

    public Table()
    {
        mEntries = new ArrayList<TableEntry>();
		mComment = "";
		mName = "";
    }
	
	public List<TableEntry> getEntries()
	{
		return mEntries;
	}
	
	public void setEntries(List<TableEntry> v)
	{
		mEntries = v;
	}
	
	public String getComment()
	{
		return mComment;
	}
	
	public void setComment(String v)
	{
		mComment = v;
	}

	public String getName()
	{
		return mName;
	}
	
	public void setName(String v)
	{
		mName = v;
	}

    public int getTotalCount()
    {
        int tot = 0;
        for (TableEntry te : getEntries())
            tot += te.getChance();
        return tot;
    }

    public TableEntry getNthEntry(int n)
    {
        for (TableEntry te : getEntries())
        {
            n -= te.getChance();
            if (n < 0)
                return te;
        }
        return null;
    }

    public String toString()
    {
        return mName+": "+mComment;
    }
}
