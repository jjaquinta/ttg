/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic.sys;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SystemLogic
{

	/**
	 * @param scheme
	 * @param uri
	 * @return
	 */
	public static SystemBean getFromURI(IGenScheme scheme, String uri)
	{
		if (!uri.startsWith("sys://"))
			return null;
		OrdBean o = OrdLogic.parseString(uri.substring(6));
		return scheme.getGeneratorSystem().generateSystem(o);
	}

    public static SystemBean getFromOrds(OrdBean o)
    {
        return getFromOrds(null, o);
    }

    public static SystemBean getFromOrds(IGenScheme scheme, OrdBean o)
    {
        if (scheme == null)
            scheme = SchemeLogic.getDefaultScheme();
        return scheme.getGeneratorSystem().generateSystem(o);
    }

    /**
     * @param system
     * @return
     */
    public static BodyBean findMainworld(SystemBean system)
    {
        for (Iterator<BodyBean> i = system.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b.isMainworld())
                return b;
        }
        return null;
    }

    /**
     * @param system
     * @return
     */
    public static BodySpecialBean findStarport(SystemBean system)
    {
        for (Iterator<BodyBean> i = system.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b instanceof BodySpecialBean)
            {
                BodySpecialBean bsb = (BodySpecialBean)b;
                if (bsb.getSubType() == BodySpecialBean.ST_STARPORT)
                    return bsb;
            }
        }
        return null;
    }
    
    public static List<BodyBean> findOfType(SystemBean system, int type)
    {
    	List<BodyBean> ret = new ArrayList<BodyBean>();
    	for (Iterator<BodyBean> i = system.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
    	{
    		BodyBean body = i.next();
    		if (body.getType() == type)
    			ret.add(body);
    	}
    	return ret;
    }

    public static BodyBean findFromURI(SystemBean system, String uri)
    {
        for (Iterator<BodyBean> i = system.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b.getURI().equals(uri))
                return b;
        }
        return null;
    }
}
