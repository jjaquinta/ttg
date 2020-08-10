package jo.util.table.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import jo.util.table.beans.Table;
import jo.util.table.beans.TableEntry;
import jo.util.table.beans.TableGroup;
import jo.util.table.logic.TableGroupLogic;

public class TableFlat
{
    public static void loadTable(TableGroup group, String tableName) throws IOException
    {
        //System.out.println("TableFlat.loadTable: "+tableName);
        Object[] path = group.getTablePath().toArray();
        for (int i = 0; i < path.length; i++)
        {
            String elem = (String)path[i];
            try
            {
            if (elem.startsWith("resource://"))
                loadTableFromResource(group, tableName, elem.substring(11));
            else if (elem.startsWith("file:"))
                loadTableFromURL(group, tableName, elem);
            else
                loadTableFromFile(group, tableName, elem);
            }
            catch (Exception e)
            {
                // skip errors, try next path element
            }
            if (group.getTables().containsKey(tableName))
                break;
        }
    }
    
	public static void loadTableFromFile(TableGroup group, String tableName, String where)
		throws IOException
	{
        //System.out.println("TableFlat.loadTableFromFile: where="+where+", tableName="+tableName);
        File current = new File(where);
        if (current.isDirectory())
        {
            if (tableName == null)
                return;
            current = new File(current, tableName);
            if (!current.exists())
                return;
        }
        if (group.getAlreadyLoaded().contains(current.toString()))
        {
            return;
        }
        group.getAlreadyLoaded().add(current.toString());
        InputStream is = new FileInputStream(current);
        readTableFromStream(group, is, current.getParent());
        is.close();
	}
    
    public static void loadTableFromURL(TableGroup group, String tableName, String where)
        throws IOException
    {
        //System.out.println("TableFlat.loadTableFromURL: where="+where+", tableName="+tableName);
        URL url = new URL(where);
        if (tableName != null)
            url = new URL(url, tableName);
        //System.out.println(where+" + "+tableName+" = "+url.toString());
        group.getAlreadyLoaded().add(url.toString());
        InputStream is = url.openStream();
        readTableFromStream(group, is, where);
        is.close();
    }
    
	public static void loadTableFromResource(TableGroup group, String tableName, String where)
		throws IOException
	{
        //System.out.println("TableFlat.loadTableFromResource: where="+where+", tableName="+tableName);
	    if (where.endsWith("/"))
	        where = where + tableName;
        if (group.getAlreadyLoaded().contains(where))
            return;
        group.getAlreadyLoaded().add(where);
        ClassLoader loader = group.getClassLoader();
        if (loader == null)
            loader = TableFlat.class.getClassLoader();
        InputStream is = loader.getResourceAsStream(where);
        if (is == null)
            return;
        readTableFromStream(group, is, where);
        is.close();
	}
    
    public static void readTableFromStream(TableGroup group, InputStream is, String root)
        throws IOException
    {
		Table table = new Table();
        BufferedReader fd = new BufferedReader(new InputStreamReader(is));
        for (;;)
        {
			String inbuf = fd.readLine();
			if (inbuf == null)
				break;
	        if (inbuf.startsWith("#FILE:"))
	        {
				if (table.getEntries().size() > 0)
				{
					TableGroupLogic.add(group, table);
					table = new Table();
				}
				table.setName(inbuf.substring(6).trim());
			}
            else if (inbuf.startsWith("#IMPORT:"))
            {
				if (table.getEntries().size() > 0)
				{
					TableGroupLogic.add(group, table);
					table = new Table();
				}
                if (root.startsWith("file:") || root.startsWith("resource:"))
                {
                    URL rootURL = new URL(root);
                    URL childURL = new URL(rootURL, inbuf.substring(8).trim());
                    //System.out.println(root+" & "+inbuf.substring(8).trim()+" = "+childURL.toString());
                    TableGroupLogic.addTablepath(group, childURL.toString());
                }
                else
                    TableGroupLogic.addTablepath(group, root + "/" + inbuf.substring(8).trim());
            }
			else
			{
                while (inbuf.endsWith("\\"))
                {
                    if (inbuf.endsWith("\\\\"))
                        inbuf = inbuf.substring(0, inbuf.length() - 2)+fd.readLine();
                    else
                        inbuf = inbuf.substring(0, inbuf.length() - 1)+"\r\n"+fd.readLine();
                }
                readLine(table, inbuf);
			}
        }
		if (table.getEntries().size() > 0)
			TableGroupLogic.add(group, table);
    }

    public static void readLine(Table table, String inbuf)
		throws IOException
    {
		int o = inbuf.indexOf(":");
        if (inbuf.startsWith("#") || inbuf.startsWith(":") || (o == -1))
        {
            if (table.getComment().length() == 0)
                table.setComment(inbuf.substring(1));
        }
        else if (o > 0)
        {
			TableEntry ent = new TableEntry();
			ent.setChance(Integer.parseInt(inbuf.substring(0, o)));
			ent.setText(inbuf.substring(o+1));
            table.getEntries().add(ent);
        }
    }
}
