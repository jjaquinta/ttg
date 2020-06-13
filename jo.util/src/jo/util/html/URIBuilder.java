package jo.util.html;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.StringTokenizer;

import jo.util.utils.obj.StringUtils;

public class URIBuilder
{
    private String  mScheme;
    private String  mAuthority;
    private String  mPath;
    private String  mUser;
    private int     mPort;
    private String  mFragment;
    private Properties mQuery;
    
    public URIBuilder()
    {
        mQuery = new Properties();
    }
    
    public URIBuilder(String uri)
    {
        this();
        try
        {
            if (uri.endsWith("://"))
            {
                mScheme = uri.substring(0, uri.length() - 3);
                mAuthority = "";
                mPath = "";
                mUser = "";
                mPort = 0;
                mFragment = "";
                return;
            }
            URI u = new URI(uri);
            mScheme = u.getScheme();
            mAuthority = u.getAuthority();
            mPath = u.getPath();
            mUser = u.getUserInfo();
            mPort = u.getPort();
            mFragment = u.getFragment();
            String query = u.getRawQuery();
            if (!StringUtils.isTrivial(query))
                for (StringTokenizer st = new StringTokenizer(query, "&"); st.hasMoreTokens(); )
                {
                    String kv = st.nextToken();
                    int o = kv.indexOf('=');
                    if (o < 0)
                        continue;
                    String k = URLLogic.decode(kv.substring(0, o));
                    String v = URLLogic.decode(kv.substring(o+1));
                    mQuery.put(k, v);
                }
        }
        catch (URISyntaxException e)
        {
            e.printStackTrace();
        }
    }
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(mScheme);
        if (!mScheme.endsWith("://"))
            if (mScheme.endsWith(":"))
                sb.append("//");
            else
                sb.append("://");
        if (!StringUtils.isTrivial(mUser) && !mAuthority.startsWith(mUser))
        {
            sb.append(mUser);
            sb.append("@");            
        }
        if (!StringUtils.isTrivial(mAuthority))
            if (mAuthority.startsWith("//"))
                sb.append(mAuthority.substring(2));
            else
                sb.append(mAuthority);
//        if (mPort > 0) // already in authority
//        {
//            sb.append(":");
//            sb.append(mPort);
//        }
        if (!StringUtils.isTrivial(mPath))
        {
            if (!mPath.startsWith("/"))
                sb.append("/");
            sb.append(mPath);
        }
        else if (!sb.toString().endsWith("/"))
            sb.append("/");
        if (mQuery.size() > 0)
        {
            boolean first = true;
            for (Object k : mQuery.keySet())
            {
                Object v = mQuery.get(k);
                if (first)
                {
                    sb.append("?");
                    first = false;
                }
                else
                    sb.append("&");
                sb.append(URLLogic.encode(k.toString()));
                sb.append("=");
                sb.append(URLLogic.encode(v.toString()));
            }
        }
        if (!StringUtils.isTrivial(mFragment))
        {
            if (!mFragment.startsWith("#"))
                sb.append("#");
            sb.append(mFragment);
        }
        return sb.toString();
    }
    
    public String getScheme()
    {
        return mScheme;
    }
    public void setScheme(String scheme)
    {
        if (scheme.endsWith("://"))
            scheme = scheme.substring(0, scheme.length() - 3);
        mScheme = scheme;
    }
    public String getAuthority()
    {
        return mAuthority;
    }
    public void setAuthority(String authority)
    {
        mAuthority = authority;
    }
    public String getProtocol()
    {
        return mScheme;
    }
    public void setProtocol(String scheme)
    {
        mScheme = scheme;
    }
    public String getHost()
    {
        return mAuthority;
    }
    public void setHost(String authority)
    {
        mAuthority = authority;
    }
    public String getPath()
    {
        return mPath;
    }
    public void setPath(String path)
    {
        mPath = path;
    }
    public String getUser()
    {
        return mUser;
    }
    public void setUser(String user)
    {
        mUser = user;
    }
    public int getPort()
    {
        return mPort;
    }
    public void setPort(int port)
    {
        mPort = port;
    }
    public String getFragment()
    {
        return mFragment;
    }
    public void setFragment(String fragment)
    {
        mFragment = fragment;
    }
    public Properties getQuery()
    {
        return mQuery;
    }
    public void setQuery(Properties query)
    {
        mQuery = query;
    }
    public void setQuery(String k, String v)
    {
        mQuery.setProperty(k, v);
    }
    public String getQuery(String k)
    {
        return mQuery.getProperty(k);
    }

    public static String replaceScheme(String uri, String newScheme)
    {
        int o = uri.indexOf("://");
        if (o < 0)
            return newScheme + uri;
        else
            return newScheme + uri.substring(o + 3);
    }
    
    public static String encode(String frag)
    {
        return URLLogic.encode(frag);
    }
    
    public static String decode(String frag)
    {
        return URLLogic.decode(frag);
    }
}
