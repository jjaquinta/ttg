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
        if (icon.startsWith("tape_"))
            return IconLogic.loadFromResource("images/icons/tape/"+icon, TTGIconUtils.class);
        if (icon.startsWith("tb_"))
            return IconLogic.loadFromResource("images/icons/toolbar/"+icon, TTGIconUtils.class);
        ImageIcon i = IconLogic.loadFromResource("images/"+icon, TTGIconUtils.class);
        return i;
    }
    public static Image getImage(String icon)
    {
        return getIcon(icon).getImage();
    }
	public static ImageIcon getPlanet(String icon)
	{
		return IconLogic.loadFromResource("images/icons/planets/"+icon, TTGIconUtils.class);
	}
    public static ImageIcon getUPP(String icon)
    {
        return IconLogic.loadFromResource("images/icons/upp/"+icon, TTGIconUtils.class);
    }
    public static Image getPlanetImage(String icon)
    {
        return getPlanet(icon).getImage();
    }
    public static ImageIcon getButton(String icon)
    {
        return IconLogic.loadFromResource("images/buttons/"+icon+".png", TTGIconUtils.class);
    }
}
