package jo.util.ui.swing.ctrl;

import java.util.Collection;
import java.util.function.Consumer;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ListUtils
{

    public static <T> void addAll(JList<T> ctrl, T[] lines)
    {
        if (ctrl.getModel() instanceof DefaultListModel)
        {
            DefaultListModel<T> lm = (DefaultListModel<T>)ctrl.getModel();
            for (T line : lines)
                lm.addElement(line);
        }
        else
            throw new IllegalArgumentException("Can't add to this type of list model: "+ctrl.getModel().getClass().getName());
    }

    public static <T> void replaceAll(DefaultListModel<T> model,
            Collection<T> items)
    {
        model.removeAllElements();
        for (T item : items)
            model.addElement(item);
    }

    public static void onSelect(JList<?> scenes, Consumer<ListSelectionEvent> listener)
    {
        scenes.addListSelectionListener(new ListSelectionListener() {            
            @Override
            public void valueChanged(ListSelectionEvent e)
            {
                listener.accept(e);
            }
        });
    }

}
