/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.logic;

import java.awt.Image;

import javax.swing.ImageIcon;

import jo.util.ui.swing.logic.IconLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TTGIconUtils
{
    public static ImageIcon getIcon(String icon)
    {
        return IconLogic.loadFromResource("jo/ttg/core/ui/swing/"+icon);
    }
    public static Image getImage(String icon)
    {
        return getIcon(icon).getImage();
    }
	public static ImageIcon getPlanet(String icon)
	{
		return IconLogic.loadFromResource("jo/ttg/core/ui/swing/images/icons/planets/"+icon);
	}
    public static ImageIcon getUPP(String icon)
    {
        return IconLogic.loadFromResource("jo/ttg/core/ui/swing/images/icons/upp/"+icon);
    }
    public static Image getPlanetImage(String icon)
    {
        return getPlanet(icon).getImage();
    }
    public static ImageIcon getButton(String icon)
    {
        return IconLogic.loadFromResource("jo/ttg/core/ui/swing/images/buttons/"+icon+".png");
    }
}
