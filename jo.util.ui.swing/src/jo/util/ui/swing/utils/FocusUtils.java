package jo.util.ui.swing.utils;

import java.awt.Component;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.function.Consumer;

public class FocusUtils
{
    public static void focusLost(Component c, final Consumer<FocusEvent> op)
    {
        c.addFocusListener(new FocusListener() {            
            @Override
            public void focusLost(FocusEvent e)
            {
                op.accept(e);
            }
            @Override
            public void focusGained(FocusEvent e)
            {
            }
        });
    }
    public static void focusGained(Component c, final Consumer<FocusEvent> op)
    {
        c.addFocusListener(new FocusListener() {            
            @Override
            public void focusLost(FocusEvent e)
            {
            }
            @Override
            public void focusGained(FocusEvent e)
            {
                op.accept(e);
            }
        });
    }
}
