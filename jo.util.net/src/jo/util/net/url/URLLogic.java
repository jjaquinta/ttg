/*
 * Created on Jul 21, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.url;

import java.net.MalformedURLException;
import java.net.URL;

import jo.util.utils.obj.StringUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class URLLogic
{
	public static String[] splitURL(String urlPath) throws MalformedURLException
	{
		URL url = new URL(urlPath);
		String[] ret = new String[4];
		ret[1] = url.getPath();
		ret[0] = url.getHost();
		ret[2] = url.getUserInfo();
		ret[3] = null;
		if (ret[2] != null)
		{
			int i = ret[2].indexOf(":");
			if (i >= 0)
			{
				ret[3] = ret[2].substring(i + 1);
				ret[2] = ret[2].substring(0, i);
			}
		}
		ret[2] = StringUtils.unwebify(ret[2]);
		ret[3] = StringUtils.unwebify(ret[3]);
		return ret;
	}
}
