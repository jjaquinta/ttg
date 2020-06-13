package jo.util.ui.swing.utils;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.AbstractButton;
import javax.swing.JPopupMenu;

public class ActionUtils
{
    private static final ActionListener mMethodActionListener = new ActionListener() {
        
        @Override
        public void actionPerformed(ActionEvent e)
        {
            String ac = e.getActionCommand();
            Object o = e.getSource();
            while (o != null)
            {
                try
                {
                    Method m = o.getClass().getMethod(ac);
                    try
                    {
                        m.invoke(o);
                    }
                    catch (IllegalAccessException | IllegalArgumentException
                            | InvocationTargetException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                catch (NoSuchMethodException | SecurityException e1)
                {
                    // carry on
                }
                if (o instanceof JPopupMenu)
                    o = ((JPopupMenu)o).getInvoker();
                else if (o instanceof Component)
                    o = ((Component)o).getParent();
               else
                {
                    System.err.println("Could not find '"+ac+"' on '"+e.getSource().getClass().getName()+"'");
                    break;
                }
            }
        }
    };
    
    public static void addActionListener(AbstractButton button, String method)
    {
        button.setActionCommand(method);
        button.addActionListener(mMethodActionListener);
    }
}
