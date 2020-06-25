/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.net.URL;

import javax.swing.ImageIcon;

import jo.ttg.beans.sys.BodyBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface BodyViewHandler
{
    public ImageIcon getIcon(BodyBean b);
    public URL getIconURI(BodyBean b);
    public Object[] getView(BodyBean b);
}
