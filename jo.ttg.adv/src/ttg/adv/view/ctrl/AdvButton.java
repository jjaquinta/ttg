/*
 * Created on Jan 16, 2005
 *
 */
package ttg.adv.view.ctrl;

import java.awt.Insets;

import javax.swing.JButton;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;

/**
 * @author Jo
 *
 */
public class AdvButton extends JButton
{
    public AdvButton(String imageName, String toolTip)
    {
        super();
        if (!imageName.endsWith(".gif"))
            imageName += ".gif";
        setIcon(TTGIconUtils.getIcon(imageName));
        setToolTipText(toolTip);
        setMargin(new Insets(0,0,0,0));
    }
}
