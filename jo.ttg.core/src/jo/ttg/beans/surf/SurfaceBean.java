/*
 * Created on Jan 27, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.beans.surf;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.sys.BodyBean;
import jo.util.heal.IHEALGlobe;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SurfaceBean implements URIBean
{
    private String                      mURI;
    private BodyBean                    mBody;
    private IHEALGlobe<MapHexBean>      mGlobe;
    private List<SurfaceAnnotationBean> mAnnotations = new ArrayList<>();

    public String getURI()
    {
        return mURI;
    }

    public void setURI(String uRI)
    {
        mURI = uRI;
    }

    public BodyBean getBody()
    {
        return mBody;
    }

    public void setBody(BodyBean body)
    {
        mBody = body;
    }

    public IHEALGlobe<MapHexBean> getGlobe()
    {
        return mGlobe;
    }

    public void setGlobe(IHEALGlobe<MapHexBean> globe)
    {
        mGlobe = globe;
    }

    public List<SurfaceAnnotationBean> getAnnotations()
    {
        return mAnnotations;
    }

    public void setAnnotations(List<SurfaceAnnotationBean> annotations)
    {
        mAnnotations = annotations;
    }
}
