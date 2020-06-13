package jo.util.ui.swing.ctrl;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.function.Consumer;

import javax.swing.JComponent;

public class ComponentUtils
{
    public static void hidden(JComponent ctrl, final Consumer<ComponentEvent> h)
    {
        ctrl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentHidden(ComponentEvent e)
            {
                h.accept(e);
            }
        });
    }
    public static void moved(JComponent ctrl, final Consumer<ComponentEvent> h)
    {
        ctrl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(ComponentEvent e)
            {
                h.accept(e);
            }
        });
    }
    public static void resized(JComponent ctrl, final Consumer<ComponentEvent> h)
    {
        ctrl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e)
            {
                h.accept(e);
            }
        });
    }
    public static void shown(JComponent ctrl, final Consumer<ComponentEvent> h)
    {
        ctrl.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e)
            {
                h.accept(e);
            }
        });
    }
}
