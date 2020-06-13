package jo.util.ui.swing.utils;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.function.Consumer;

public class MouseUtils
{
    public static void mouseClicked(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseListener(new MouseAdapter() {            
            @Override
            public void mouseClicked(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseDragged(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseEntered(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseExited(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseExited(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseMoved(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mousePressed(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseReleased(Component c, final Consumer<MouseEvent> ev)
    {
        c.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e)
            {
                ev.accept(e);
            }
        });
    }
    public static void mouseWheelMoved(Component c, final Consumer<MouseWheelEvent> ev)
    {
        c.addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e)
            {
                ev.accept(e);
            }
        });
    }
}
