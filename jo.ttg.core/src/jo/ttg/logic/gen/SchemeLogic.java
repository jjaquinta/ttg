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

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.scans.BodyScanBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.LinksBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.XMessageBean;
import jo.ttg.beans.trade.XMessageLotBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.RuntimeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.mw.MainWorldsLogic;
import jo.ttg.logic.scans.BodyScanLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sub.LinksLogic;
import jo.ttg.logic.sub.SubSectorLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.ttg.logic.trade.CargoLotLogic;
import jo.ttg.logic.trade.FreightLotLogic;
import jo.ttg.logic.trade.PassengerLotLogic;
import jo.ttg.logic.trade.XMessageLotLogic;
import jo.ttg.logic.uni.UniverseLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SchemeLogic
{
    private static String[]	mQueueURI = null;
    private static URIBean[]	mQueueObj = null;
    private static IGenScheme   mDefaultScheme = null;
    
    /*
    public static IGenScheme getScheme(String id)
    {
        List<?> choice = ExtensionPointUtils.processExtensions("jo.ttg.core.TTGScheme", new ExecutableExtensionPointProcessor("id", "id", id));
        if (choice.size() == 0)
            return null;
        return (IGenScheme)choice.get(0);
    }
    */
    
    public static void setDefaultScheme(IGenScheme scheme)
    {
        mDefaultScheme = scheme;
    }
    
    public static IGenScheme getDefaultScheme()
    {
        return mDefaultScheme;
    }
    
    public static boolean is2D(IGenScheme scheme)
    {
        return scheme.getSectorSize().getZ() == 0;
    }
    
    public static boolean is3D(IGenScheme scheme)
    {
        return scheme.getSectorSize().getZ() != 0;
    }
    
    public static void setQueueSize(int size)
    {
        if (size <= 0)
        {
            mQueueURI = null;
            mQueueObj = null;
        }
        else
        {
            String[] newQueueURI = new String[size];
            URIBean[] newQueueObj = new URIBean[size];
            if (mQueueURI != null)
            {
                System.arraycopy(mQueueURI, 0, newQueueURI, 0, Math.min(mQueueURI.length, newQueueURI.length));
                System.arraycopy(mQueueObj, 0, newQueueObj, 0, Math.min(mQueueObj.length, newQueueObj.length));
            }
            mQueueURI = newQueueURI;
            mQueueObj = newQueueObj;
        }
    }
    
    private static URIBean findInQueue(String uri)
    {
        if (mQueueURI == null)
            return null;
        URIBean ret = null;
        for (int i = 0; i < mQueueURI.length; i++)
            if (uri.equals(mQueueURI[i]))
            {
                ret = mQueueObj[i];
                System.arraycopy(mQueueURI, 0, mQueueURI, 1, i);
                System.arraycopy(mQueueObj, 0, mQueueObj, 1, i);
                mQueueURI[0] = uri;
                mQueueObj[0] = ret;
                break;
            }
      return ret;
    }
    
    private static void addToQueue(String uri, URIBean obj)
    {
        if (mQueueURI == null)
            return;
        System.arraycopy(mQueueURI, 0, mQueueURI, 1, mQueueURI.length - 1);
        System.arraycopy(mQueueObj, 0, mQueueObj, 1, mQueueObj.length - 1);
        mQueueURI[0] = uri;
        mQueueObj[0] = obj;
    }
    
	public static URIBean getFromURI(IGenScheme scheme, String uri)
	{
	    if ((scheme == null) || (uri == null) || (uri.length() == 0))
	        return null;
	    URIBean ret = findInQueue(uri);
	    if (ret != null)
	        return ret;
		if (uri.startsWith("mw://"))
			ret = MainWorldLogic.getFromURI(scheme, uri);
		else if (uri.startsWith("sub://"))
			ret = SubSectorLogic.getFromURI(scheme, uri);
		else if (uri.startsWith("sec://"))
			ret = SectorLogic.getFromURI(scheme, uri);
		else if (uri.startsWith("uni://"))
			ret = UniverseLogic.getFromURI(scheme, uri);
		else if (uri.startsWith("sys://"))
			ret = SystemLogic.getFromURI(scheme, uri);
		else if (uri.startsWith(BodyBean.SCHEME))
			ret = BodyLogic.getFromURI(scheme, uri);
		else if (uri.startsWith("ord://"))
			ret = OrdLogic.parseString(uri.substring(7));
        else if (uri.startsWith("ords://"))
            ret = OrdLogic.parseString(uri.substring(8));
        else if (uri.startsWith("surface://"))
            ret = (URIBean)SurfaceLogic.getFromURI(scheme, uri);		
        else if (uri.startsWith("cargo://"))
            ret = CargoLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith("cargolot://"))
            ret = CargoLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith("freight://"))
            ret = FreightLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith("passengers://"))
            ret = PassengerLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith("mws://"))
            ret = MainWorldsLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith(XMessageBean.SCHEME))
            ret = XMessageLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith(XMessageLotBean.SCHEME))
            ret = XMessageLotLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith(LinksBean.SCHEME))
            ret = LinksLogic.getFromURI(scheme, uri);     
        else if (uri.startsWith(BodyScanBean.SCHEME))
            ret = BodyScanLogic.getFromURI(scheme, uri);     
		if (ret != null)
		    addToQueue(uri, ret);
		return ret; 
	}
    
    public static List<SectorBean> getSectorsWithin(IGenScheme scheme, OrdBean upperBound, OrdBean lowerBound)
    {
        List<SectorBean> ret = new ArrayList<SectorBean>();
        OrdBean ub = new OrdBean(upperBound);
        scheme.nearestSec(ub);
        OrdBean o = new OrdBean();
        for (long x = ub.getX(); x < lowerBound.getX(); x += scheme.getSectorSize().getX())
        {
            o.setX(x);
            for (long y = ub.getY(); y < lowerBound.getY(); y += scheme.getSectorSize().getY())
            {
                o.setY(y);
                if (scheme.getSectorSize().getZ() > 0)
                    for (long z = ub.getZ(); z < lowerBound.getZ(); z += scheme.getSectorSize().getZ())
                    {
                        o.setZ(z);
                        SectorBean sec = scheme.getGeneratorSector().generateSector(o);
                        ret.add(sec);
                    }
                else
                {
                    SectorBean sec = scheme.getGeneratorSector().generateSector(o);
                    if (sec != null)
                    	ret.add(sec);
                }
            }
        }
        return ret;
    }
    
    public static List<SubSectorBean> getSubSectorsWithin(IGenScheme scheme, OrdBean upperBound, OrdBean lowerBound)
    {
        List<SubSectorBean> ret = new ArrayList<SubSectorBean>();
        for (SectorBean sec : getSectorsWithin(scheme, upperBound, lowerBound))
            for (SubSectorBean sub  :sec.getSubSectors())
                if (OrdLogic.intersects(upperBound, lowerBound, sub.getUpperBound(), sub.getLowerBound()))
                    ret.add(sub);
        return ret;
    }
    
    public static List<MainWorldBean> getWorldsWithin(IGenScheme scheme, OrdBean upperBound, OrdBean lowerBound)
    {
        List<MainWorldBean> ret = new ArrayList<MainWorldBean>();
        for (SubSectorBean sub : getSubSectorsWithin(scheme, upperBound, lowerBound))
            for (MainWorldBean mw : sub.getMainWorlds())
                if (OrdLogic.within(mw.getOrds(), upperBound, lowerBound))
                    ret.add(mw);
        return ret;
    }
	
	public static List<MainWorldBean> getWorldsWithin(IGenScheme scheme, OrdBean o, int rad)
	{
		List<MainWorldBean> ret = new ArrayList<MainWorldBean>();
		OrdBean ub = new OrdBean(o.getX() - rad - 1, o.getY() - rad - 1, o.getZ() - rad - 1);
		OrdBean lb = new OrdBean(o.getX() + rad + 1, o.getY() + rad + 1, o.getZ() + rad + 1);
		List<MainWorldBean> l = getWorldsWithin(scheme, ub, lb);
		for (MainWorldBean mw : l)
		{
			double d = OrdLogic.dist2D(o, mw.getOrds());
			if (d <= rad)
				ret.add(mw);
		}
		return ret;
	}
	
	public static OrdBean getOrds(String uri)
	{
        if (uri == null)
            return null;
	    int o = uri.indexOf("://");
	    if (o >= 0)
	        uri = uri.substring(o+3);
	    o = uri.indexOf("/");
	    if (o >= 0)
	        uri = uri.substring(0, o);
	    return OrdLogic.parseString(uri);
	}

    /**
     * @param origin
     * @param destination
     * @return
     */
    public static boolean isSameOrds(String origin, String destination)
    {
        return getOrds(origin).equals(getOrds(destination));
    }
    
    public static double getUnitLength(IGenScheme scheme) // in parsecs
    {
        return scheme.distance(new OrdBean(0, 0, 0), new OrdBean(1, 0, 0));
    }
    
    public static double getParsecLength(IGenScheme scheme) // in unit
    {
        return 1.0/scheme.distance(new OrdBean(0, 0, 0), new OrdBean(1, 0, 0));
    }

    public static String descFromURI(IGenScheme scheme, URIBean uri)
    {
        if (scheme == null)
            scheme = RuntimeLogic.getScheme();
        if (uri == null)
            return "";
        if (uri instanceof SectorBean)
            return ((SectorBean)uri).getName();
        else if (uri instanceof SubSectorBean)
        {
            SubSectorBean sub = (SubSectorBean)uri;
            SectorBean sec = scheme.getGeneratorSector().generateSector(sub.getUpperBound());
            return sec.getName()+" / "+sub.getName();
        }
        else if (uri instanceof MainWorldBean)
        {
            MainWorldBean mw = (MainWorldBean)uri;
            SectorBean sec = scheme.getGeneratorSector().generateSector(mw.getOrds());
            SubSectorBean sub = scheme.getGeneratorSubSector().generateSubSector(mw.getOrds());
            return sec.getName()+" / "+sub.getName()+" / "+mw.getName();
        }
        else if (uri instanceof BodyBean)
        {
            BodyBean body = (BodyBean)uri;
            SectorBean sec = scheme.getGeneratorSector().generateSector(body.getSystem().getOrds());
            SubSectorBean sub = scheme.getGeneratorSubSector().generateSubSector(body.getSystem().getOrds());
            MainWorldBean mw = scheme.getGeneratorMainWorld().generateMainWorld(body.getSystem().getOrds());
            StringBuffer u = new StringBuffer();
            while (body != null)
            {
                if (u.length() > 0)
                    u.insert(0, " / ");
                u.insert(0, body.getName());
                body = body.getPrimary();
            }
            return sec.getName()+" / "+sub.getName()+" / "+mw.getName()+" / "+u;
        }
        else
        {
            return uri.getURI();
        }
    }
    
    public static PopulatedStatsBean findNearestPopulatedStats(BodyBean body)
    {
        BodyBean nearest = findNearestPopulatedBody(body);
        if (nearest == null)
            return null;
        return ((BodyPopulated)nearest).getPopulatedStats();
    }
    
    public static BodyBean findNearestPopulatedBody(BodyBean body)
    {
        if (body == null)
            return null;
        if (body instanceof BodyPopulated)
            return body;
        if ((body.getPrimary() != null) && (body.getPrimary() instanceof BodyPopulated))
            return body.getPrimary();
        for (Iterator<BodyBean> i = body.getSystem().getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b.isMainworld())
                return b;
        }
        return null;
    }

    public static URIBean getParentOf(IGenScheme scheme, URIBean child)
    {
        if (child instanceof BodyBean)
        {
            BodyBean body = (BodyBean)child;
            if (body.getPrimary() != null)
                return body.getPrimary();
            return MainWorldLogic.getFromOrds(scheme, body.getSystem().getOrds());
        }
        if (child instanceof SystemBean)
            return MainWorldLogic.getFromOrds(scheme, ((SystemBean)child).getOrds());
        if (child instanceof MainWorldBean)
            return SubSectorLogic.getFromOrds(scheme, ((MainWorldBean)child).getOrds());
        if (child instanceof SubSectorBean)
            return SectorLogic.getFromOrds(scheme, ((SubSectorBean)child).getUpperBound());
        return null;
    }}
