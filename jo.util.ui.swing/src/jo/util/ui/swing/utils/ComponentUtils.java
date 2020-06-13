package jo.util.ui.swing.utils;

import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

public class ComponentUtils
{
    public static void componentShown(Component c, final Consumer<ComponentEvent> op)
    {
        c.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e)
            {
                op.accept(e);
            }
        });
    }
    public static void componentResized(Component c, final Consumer<ComponentEvent> op)
    {
        c.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                op.accept(e);
            }
        });
    }
    public static void componentMoved(Component c, final Consumer<ComponentEvent> op)
    {
        c.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e)
            {
                op.accept(e);
            }
        });
    }
    public static void componentHidden(Component c, final Consumer<ComponentEvent> op)
    {
        c.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e)
            {
                op.accept(e);
            }
        });
    }
}
