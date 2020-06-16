package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;

public interface IHexPanel
{
    public static final int M_NORM = 0;
    public static final int M_BACK = 3;
    public static final int M_FOCUSED = 1;
    public static final int M_DISABLED = 2;
    
    public int getHexSide();
    public Color getForeColor();
    public Color getFocusedColor();
    public Color getDisabledColor();
    public Color getBackground();
    public Color getUnfocusedBackColor();
    public Color getFocusedBackColor();
    public Color getDisabledBackColor();

}
