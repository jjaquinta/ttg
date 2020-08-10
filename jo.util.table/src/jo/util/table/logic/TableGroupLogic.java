package jo.util.table.logic;

import java.io.File;
import java.io.IOException;
import java.util.StringTokenizer;

import jo.util.table.beans.Table;
import jo.util.table.beans.TableGroup;
import jo.util.table.io.TableFlat;

public class TableGroupLogic
{
    public static TableGroup create()
    {
        return new TableGroup();
    }
    
    public static void addTablepath(TableGroup group, String path)
    {
        //System.out.println("TableGroupLogic.addTablepath, path="+path);
        for (StringTokenizer st = new StringTokenizer(path, ";"); st.hasMoreTokens(); )
        {
            String elem = st.nextToken();
            group.getTablePath().add(elem);
            if (!elem.startsWith("file:") && !elem.startsWith("resource:"))
            {
                File f = new File(elem);
                if (f.exists() && f.isFile())
                {
                	group.getTablePath().add(f.getParent());
                }
            }
        }
    }
    
    public static Table getTable(TableGroup group, String tableName)
    {
        Table ret = (Table)group.getTables().get(tableName);
        if (ret == null)
        {
            try
            {
                TableFlat.loadTable(group, tableName);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            ret = (Table)group.getTables().get(tableName);
            if ((ret == null) && (tableName.length() > 0))
                System.out.println("Can't load table '"+tableName+"'");
        }
        return ret;
    }

    public static void add(TableGroup group, Table table)
    {
        group.getTables().put(table.getName(), table);        
        if (group.getRoot() == null)
            group.setRoot(table);
    }

    public static Table getDefaultTable(TableGroup group)
    {
        if (group.getRoot() == null)
            try
            {
                TableFlat.loadTable(group, null);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        return group.getRoot();
    }
}
