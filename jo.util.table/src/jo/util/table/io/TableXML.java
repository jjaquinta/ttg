package jo.util.table.io;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jo.util.table.beans.TableGroup;
import jo.util.table.logic.TableGroupLogic;

public class TableXML
{
	public static TableGroup readFile(String fname)
		throws IOException
	{
		InputStream is = new FileInputStream(fname);
		TableGroup ret = readStream(is);
		is.close();
		return ret;
	}
	
	public static TableGroup readResource(String rname)
		throws IOException
	{
        ClassLoader loader = (new TableFlat()).getClass().getClassLoader();
        InputStream is = loader.getResourceAsStream(rname);
		TableGroup ret =  readStream(is);
		is.close();
		return ret;
	}
	
	public static TableGroup readStream(InputStream is)
		throws IOException
	{
        XMLDecoder dec = new XMLDecoder(is);
		TableGroup ret = (TableGroup)dec.readObject();
		dec.close();
		return ret;
	}

	public static void writeFile(TableGroup group, String fname)
		throws IOException
	{
		OutputStream os = new FileOutputStream(fname);
		writeStream(group, os);
		os.close();
	}
	
	public static void writeStream(TableGroup group, OutputStream os)
		throws IOException
	{
        XMLEncoder enc = new XMLEncoder(os);
		enc.writeObject(group);
		enc.flush();
		enc.close();
	}

	public static void main(String[] argv)
	{
		if (argv.length != 2)
		{
			System.out.println("Usage: cmd <in-flat-file> <out-xml.file>");
			System.exit(0);
		}
		try
		{
			TableGroup group = TableGroupLogic.create(); 
            TableFlat.loadTable(group, argv[0]);
			TableXML.writeFile(group, argv[1]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
