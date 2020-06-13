/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.mw;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldsBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class MainWorldsLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static MainWorldsBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith("mws://"))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String sPoint1 = u.getAuthority();
        String sPoint2 = u.getQuery("lowerBound");
        String sRadius = u.getQuery("radius");
        if (StringUtils.isTrivial(sPoint1))
            return null;
        MainWorldsBean worlds = new MainWorldsBean();
        worlds.setURI(uri);
        OrdBean point1 = OrdLogic.parseString(sPoint1);
        worlds.setPoint1(point1);
        if (!StringUtils.isTrivial(sPoint2))
        {
            OrdBean point2 = OrdLogic.parseString(sPoint2);
            worlds.setPoint2(point2);
            worlds.getMainWorlds().addAll(SchemeLogic.getWorldsWithin(scheme, point1, point2));
            System.out.println(worlds.getMainWorlds().size()+" worlds between "+point1+" and "+point2);
        }
        else if (!StringUtils.isTrivial(sRadius))
        {
            int radius = IntegerUtils.parseInt(sRadius);
            worlds.setRadius(radius);
            worlds.getMainWorlds().addAll(SchemeLogic.getWorldsWithin(scheme, point1, radius));
        }
        else
            return null;
        return worlds;
    }
}
