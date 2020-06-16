/*
 * Created on Jul 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import jo.util.net.url.URLLogic;
import jo.util.utils.DebugUtils;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FTPLogic
{
    private static final int RETRY = 3;
    
    private static Map<String,FTPClient> CACHED_CLIENTS = new HashMap<String,FTPClient>();
    
	private static synchronized FTPClient getClient(String url, StringBuffer fileName) throws IOException
	{
        String[] args = URLLogic.splitURL(url);
        String host = args[0];
        String file = args[1];
        String user = args[2];
        String password = args[3];
        FTPClient client = (FTPClient)CACHED_CLIENTS.get(host);
        if ((client == null) || !client.isConnected())
        {
    		client = new FTPClient();
            CACHED_CLIENTS.put(host, client);
    		client.connect(host);
    		if(!FTPReply.isPositiveCompletion(client.getReplyCode()))
    		{
    			client.disconnect();
    			throw new IOException(client.getReplyString());
    		}
    		
    		if (user == null)
    			user = "anonymous";
    		if (password == null)
    			password = "guest";
    		client.login(user, password);
    		if(!FTPReply.isPositiveCompletion(client.getReplyCode()))
    		{
    			client.disconnect();
    			DebugUtils.error("FTP Error:"+client.getReplyString());
    			throw new IOException(client.getReplyString());
    		}
    
    		client.setFileType(FTPClient.ASCII_FILE_TYPE);
        }
        else
            client.changeWorkingDirectory("/"); // reset to root
		// it's ambiguous as to if ftp://host/file wants us to go to
		// /file on the target machine, or ~/file on the target machine.
	    // We choose to interpret ftp://host/file as /file and require
	    // someone to say ftp://host/~/file for ~/file.
	    if (file.startsWith("/~/"))
			file = file.substring(3);
	    String sofar = "";
	    for (;;)
	    {
			int o = file.indexOf("/");
			if (o < 0)
			    break;
			String dir = file.substring(0, o);
			file = file.substring(o+1);
            if (o == 0)
                continue;
			if (!client.changeWorkingDirectory(dir))
			{
			    if (!client.makeDirectory(dir))
			        throw new IOException("Cannot make subdirectory '"+dir+"' in directory '"+sofar+"' preparation for retreiving '"+file+"'");
				if (!client.changeWorkingDirectory(dir))
			        throw new IOException("Cannot change to directory '"+dir+"' in directory '"+sofar+"' preparation for retreiving '"+file+"'");
			}
			sofar += "/"+dir;
	    }
		fileName.append(file);
		return client;
	}
	
	public static InputStream getFromFTP(String urlPath) throws IOException
	{
		DebugUtils.trace("getFromFTP(url="+urlPath+")");
		StringBuffer fileName = new StringBuffer();
		FTPClient client = getClient(urlPath, fileName);
		return client.retrieveFileStream(fileName.toString());
	}
	
	public static void getFromFTP(String urlPath, OutputStream os, boolean deleteSource) throws IOException
	{
		DebugUtils.trace("getFromFTP(url="+urlPath+")");
		StringBuffer fileName = new StringBuffer();
		FTPClient client = getClient(urlPath, fileName);
		for (int i = 0; i < RETRY; i++)
		{
		    if (i > 0)
		        DebugUtils.trace("  try #"+i);
//		    FTPFile[] finfo = client.listFiles();
//		    for (int j = 0; j < finfo.length; j++)
//		        DebugUtils.trace("  "+finfo[j].getSize()+" ("+finfo[j].getRawListing()+")");
//		    finfo = client.listFiles(fileName.toString());
//	        if (finfo.length > 0)
//	            DebugUtils.trace("  "+fileName+"->"+finfo[0].getSize()+" ("+finfo[0].getRawListing()+")");
		    boolean ret = client.retrieveFile(fileName.toString(), os);
		    if (ret)
		        break;
		    DebugUtils.trace("  failure!");
		}
		os.flush();
		os.close();
		if (deleteSource)
		    client.deleteFile(fileName.toString());
		//client.disconnect();
	}

	public static OutputStream sendToFTP(String urlPath) throws IOException
	{
		DebugUtils.trace("sendToFTP(url="+urlPath+")");
		StringBuffer fileName = new StringBuffer();
		FTPClient client = getClient(urlPath, fileName);
		client.deleteFile(fileName.toString());
		return client.storeFileStream(fileName.toString());
	}

	public static void sendToFTP(String urlPath, InputStream is) throws IOException
	{
		DebugUtils.trace("sendToFTP(url="+urlPath+")");
		StringBuffer fileName = new StringBuffer();
		FTPClient client = getClient(urlPath, fileName);
		client.deleteFile(fileName.toString());
		for (int i = 0; i < RETRY; i++)
		{
		    if (i > 0)
		        DebugUtils.trace("  try #"+i);
		    boolean ret = client.storeFile(fileName.toString(), is);
		    if (ret)
		        break;
		    DebugUtils.trace("  failure!");
		}
		is.close();
		//client.disconnect();
	}
    
    private static String mListCacheURL = null;
    private static FTPFile[] mListCacheFiles = null;
    
    public static synchronized FTPFile[] listFilesFTP(String urlPath) throws IOException
    {
        DebugUtils.trace("listFileFTP(url="+urlPath+")");
        if (urlPath.equals(mListCacheURL))
            return mListCacheFiles;
        StringBuffer fileName = new StringBuffer();
        FTPClient client = getClient(urlPath, fileName);
        FTPFile[] files = client.listFiles(fileName.toString());
        //client.disconnect();
        mListCacheURL = urlPath;
        mListCacheFiles = files;
        return files;
    }
    
    public static boolean mkdirsFTP(String urlPath) throws IOException
    {
        DebugUtils.trace("mkdirsFTP(url="+urlPath+")");
        StringBuffer fName = new StringBuffer();
        FTPClient client = getClient(urlPath, fName);
        String fileName = fName.toString();
        int idx = 0;
        boolean ret = true;
        do
        {
            idx = fileName.indexOf("/", idx + 1);
            if (idx < 0)
                idx = fileName.length();
            if (!client.makeDirectory(fileName.substring(0, idx)))
            {
                ret = false;
                break;
            }
        } while (idx < fileName.length());
        //client.disconnect();
        return ret;
    }
}
