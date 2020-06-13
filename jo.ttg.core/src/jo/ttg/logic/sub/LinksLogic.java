/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.sub;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.LinksBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.StringUtils;

public class LinksLogic
{

    /**
     * @param scheme
     * @param uri
     * @return
     */
    public static LinksBean getFromURI(IGenScheme scheme, String uri)
    {
        if ((uri == null) || !uri.startsWith(LinksBean.SCHEME))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String sPoint1 = u.getAuthority();
        String sPoint2 = u.getQuery("lowerBound");
        if (StringUtils.isTrivial(sPoint1))
            return null;
        LinksBean links = new LinksBean();
        links.setURI(uri);
        OrdBean point1 = OrdLogic.parseString(sPoint1);
        links.setUpperBound(point1);
        OrdBean point2 = OrdLogic.parseString(sPoint2);
        links.setLowerBound(point2);
        links.getLinks().addAll(SubSectorLogic.getLinks(scheme, point1, point2));
        return links;
    }
}
