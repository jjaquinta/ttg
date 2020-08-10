package jo.util.table.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TableGroup
{
    private String              mURI;
    private Set<String>         mTablePath;
    private Set<String>         mAlreadyLoaded;
    private Map<String,Table>   mTables;
    private Table               mRoot;
    private ClassLoader         mClassLoader;

	public TableGroup()
	{
		mTables = new HashMap<String,Table>();
        mTablePath = new HashSet<String>();
        mAlreadyLoaded = new HashSet<String>();
	}

    /**
     * @return the uRI
     */
    public String getURI()
    {
        return mURI;
    }

    /**
     * @param uri the uRI to set
     */
    public void setURI(String uri)
    {
        mURI = uri;
    }

    public Set<String> getTablePath()
    {
        return mTablePath;
    }

    public void setTablePath(Set<String> tablePath)
    {
        mTablePath = tablePath;
    }

    public void setTables(Map<String,Table> tables)
    {
        mTables = tables;
    }
    
    public Map<String,Table> getTables()
    {
        return mTables;
    }

    public Table getRoot()
    {
        return mRoot;
    }

    public void setRoot(Table root)
    {
        mRoot = root;
    }

    public Set<String> getAlreadyLoaded()
    {
        return mAlreadyLoaded;
    }

    public void setAlreadyLoaded(Set<String> alreadyLoaded)
    {
        mAlreadyLoaded = alreadyLoaded;
    }

    public ClassLoader getClassLoader()
    {
        return mClassLoader;
    }

    public void setClassLoader(ClassLoader classLoader)
    {
        mClassLoader = classLoader;
    }
}
