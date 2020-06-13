/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic.gen;

import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.gen.IGenScheme;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SurfaceLogic
{
	/**
	 * @param scheme
	 * @param uri
	 * @return
	 */
    public static SurfaceBean getFromURI(String uri)
    {
        return getFromURI(SchemeLogic.getDefaultScheme(), uri);
    }
	public static SurfaceBean getFromURI(IGenScheme scheme, String uri)
	{
		if (!uri.startsWith("surface://"))
			return null;
		int off = uri.indexOf("?");
		if (off >= 0)
		    uri = uri.substring(0, off);
		String bodyURI = BodyBean.SCHEME + uri.substring(10);
		BodyBean body = BodyLogic.getFromURI(scheme, bodyURI);
		if (!(body instanceof BodyWorldBean))
		    return null;
		SurfaceBean globe = scheme.getGeneratorSurface().generateSurface((BodyWorldBean)body);
		return globe;
	}
    public static SurfaceBean getFromBody(BodyBean body)
    {
        return getFromBody(SchemeLogic.getDefaultScheme(), body);
    }
    public static SurfaceBean getFromBody(IGenScheme scheme, BodyBean body)
    {
        if (!(body instanceof BodyWorldBean))
            return null;
        SurfaceBean globe = scheme.getGeneratorSurface().generateSurface((BodyWorldBean)body);
        return globe;
    }
}
