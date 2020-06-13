/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.LocLogic;
import jo.ttg.logic.OrdLogic;
import jo.ttg.utils.ConvUtils;
import jo.util.html.URIBuilder;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyLogic
{
    public static BodyBean getFromURI(String uri)
    {
        return getFromURI(SchemeLogic.getDefaultScheme(), uri);
    }

	/**
	 * @param scheme
	 * @param uri
	 * @return
	 */
	public static BodyBean getFromURI(IGenScheme scheme, String uri)
	{
		if (!uri.startsWith(BodyBean.SCHEME))
			return null;
		URIBuilder u = new URIBuilder(uri);
		OrdBean o = OrdLogic.parseString(u.getAuthority());
		SystemBean sys = scheme.getGeneratorSystem().generateSystem(o);
		StringTokenizer st = new StringTokenizer(u.getPath(), "/");
		BodyBean b = sys.getSystemRoot();
		if (!b.getName().equals(st.nextToken()))
			return null;
		while (st.hasMoreTokens())
		{
			String name = URIBuilder.decode(st.nextToken());
			BodyBean c = null;
			for (Iterator<BodyBean> i = b.getSatelitesIterator(); i.hasNext(); )
			{
				c = i.next();
				if (c.getName().equals(name))
					break;
			}
			if (c == null)
				return null;
			b = c;
		}
		return b;
	}
	
	public static double getDiametersFromPrimary(BodyBean body)
	{
	    BodyBean primary = body.getPrimary();
	    if (primary == null)
	        return 0;
	    //double rad = Math.max(body.getOrbitalRadius(), 1);
	    double rad = body.getOrbitalRadius();
	    return rad/primary.getDiameter();
	}
	
	public static boolean is100DiametersFromPrimary(BodyBean body)
	{
	    double dist = getDiametersFromPrimary(body);
	    return (dist == 0) || (dist >= 100);
	}
	
	public static boolean is100DiametersFromAllPrimaries(BodyBean body)
	{
	    while (body != null)
	    {
	        double dist = getDiametersFromPrimary(body);
	        if ((dist > 0) && (dist < 100))
	            return false;
	        body = body.getPrimary();
	    }
	    return true;
	}
	
	public static LocBean getLocation(BodyBean body, DateBean date)
	{
	    if (body.getPrimary() == null)
	        return new LocBean();
	    double orbitRadians = getOrbitalRadians(body, date);
	    LocBean ret = new LocBean();
	    ret.setX(body.getOrbitalRadius()*Math.sin(orbitRadians));
	    ret.setY(body.getOrbitalRadius()*Math.cos(orbitRadians));
	    if (body.getPrimary().getPrimary() != null)
	        LocLogic.incr(ret, getLocation(body.getPrimary(), date));
	    return ret;
	}
    
    public static double getOrbitalRadians(BodyBean body, DateBean date)
    {
        if (body.getPrimary() == null)
            return 0;
        double orbitalPeriod = body.getOrbitalPeriod();
        double orbit = date.getDays()/orbitalPeriod;
        double orbitRadians = (orbit - Math.floor(orbit))*Math.PI*2;
        return orbitRadians;
    }

    /**
     * @param outboundBody
     * @param inboundBody
     * @return
     */
    public static BodyBean findCommonParent(BodyBean b1, BodyBean b2)
    {
        List<BodyBean> p1 = new ArrayList<BodyBean>();
        for (BodyBean b = b1; b != null; b = b.getPrimary())
            p1.add(0, b);
        List<BodyBean> p2 = new ArrayList<BodyBean>();
        for (BodyBean b = b2; b != null; b = b.getPrimary())
            p2.add(0, b);
        int l = Math.min(p1.size(), p2.size());
        for (int i = 0; i < l; i++)
        {
            BodyBean pe1 = p1.get(i);
            BodyBean pe2 = p2.get(i);
            if (!pe1.getName().equals(pe2.getName()))
                return p1.get(i-1);
        }
        return p1.get(l - 1);
    }

    public static double calculateGravity(BodyWorldBean body)
    {
        double m = ConvUtils.convSMToKg(body.getMass());
        m /= ConvUtils.EarthMassInKG;
        double r = ConvUtils.convAUToKm(body.getDiameter())/2;
        r /= ConvUtils.EarthRadiusInKM;
        double g = m/(r*r);
        return g;
    }

    public static BodyStarBean findStar(BodyBean b)
    {
        for (BodyBean body = b; body != null; body = body.getPrimary())
        {
            if (body instanceof BodyStarBean)
                return (BodyStarBean)body;
        }
        throw new IllegalArgumentException(b.getName()+" has no star as primary!");
    }
}
