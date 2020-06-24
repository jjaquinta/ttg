/*
 * Created on Feb 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jo.util.ui.swing.utils;

import java.awt.Component;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.filechooser.FileFilter;

import jo.util.utils.MapUtils;

/**
 * @author jo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class FileOpenUtils
{
	private static Map<String,File>	mCurrentDir;
	private static Map<String,File>	mLastFile;
	
	private static synchronized void loadProps()
	{
	    if (mCurrentDir != null)
	        return;
	    mCurrentDir = new HashMap<>();
	    File currentFile = new File("currentDir.properties");
        Properties p;
        if (currentFile.exists())
            p = MapUtils.load(currentFile);
        else
            p = null;
	    if (p != null)
	    {
	        for (Object key : p.keySet())
	        {
	            String val = p.getProperty((String)key);
	            mCurrentDir.put((String)key, new File(val));
	        }
	    }
	    mLastFile = new HashMap<>();
	    File lastFile = new File("lastFile.properties");
	    if (lastFile.exists())
	        p = MapUtils.load(lastFile);
	    else
	        p = null;
	    if (p != null)
	    {
            for (Object key : p.keySet())
            {
                String val = p.getProperty((String)key);
	            mLastFile.put((String)key, new File(val));
	        }
	    }
	}
	
	private static synchronized void saveProps()
	{
	    Properties p = new Properties();
	    for (String key : mCurrentDir.keySet())
	    {
	        String val = mCurrentDir.get(key).toString();
	        p.put(key, val);
	    }
	    MapUtils.save(p, new File("currentDir.properties"));
	    p = new Properties();
	    for (String key : mLastFile.keySet())
	    {
	        String val = mLastFile.get(key).toString();
	        p.put(key, val);
	    }
	    MapUtils.save(p, new File("lastFile.properties"));
	}
	
	public static  File selectFile(Component parent, String approveButtonText, FileFilter filter, String extension)
	{
	    loadProps();
	    File currentDir = (File)mCurrentDir.get(extension);
	    if (currentDir == null)
	        currentDir = new File(System.getProperty("user.home"));
	    File lastFile = (File)mLastFile.get(extension);

	    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
		chooser.setApproveButtonText(approveButtonText);
		chooser.setFileFilter(filter);
		chooser.setCurrentDirectory(currentDir);
		if (lastFile != null)
			chooser.setSelectedFile(lastFile);
		int returnVal = chooser.showOpenDialog(parent);
		currentDir = chooser.getCurrentDirectory();
		mCurrentDir.put(extension, currentDir);
		File ret = null;
		if (returnVal == javax.swing.JFileChooser.APPROVE_OPTION)
		{
			lastFile = chooser.getSelectedFile();
			String fname = chooser.getSelectedFile().getName();
			if (fname.indexOf(".") < 0)
				fname += extension;
			mLastFile.put(extension, lastFile);
			ret = new File(currentDir, fname);
		}
	    saveProps();
		return ret;
	}

	
	public static  File selectFile(Component parent, String approveButtonText, String fileType, String extension)
	{
	    return selectFile(parent, approveButtonText, new FileFilterAdapter(extension, fileType), extension);
	}
}
