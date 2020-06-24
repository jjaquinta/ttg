/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.util.ui.swing.utils;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class FileFilterAdapter extends FileFilter
{
	private String	mExtension;
	private String	mName;
	
	public FileFilterAdapter(String extension, String name)
	{
		mExtension = extension;
		mName = name;
	}
	
	public boolean accept(File f)
	{
		if (f.isDirectory())
			return true;
		return f.getName().toLowerCase().endsWith(mExtension);
	}

	public String getDescription()
	{
		return mName;
	}
}
