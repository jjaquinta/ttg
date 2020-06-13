/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.icons;

import java.awt.Image;

import jo.ttg.beans.sys.BodyBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface IBodyViewHandler
{
    public Image getImage(BodyBean b);
    public String getText(BodyBean b);
    public Image getColumnImage(BodyBean b, int columnIndex);
    public String getColumnText(BodyBean b, int columnIndex);
}
