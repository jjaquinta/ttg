package jo.util.ui.swing.ctrl;

import java.util.Collection;

import javax.swing.DefaultComboBoxModel;

public class ComboUtils
{

    public static <T> void replaceAll(DefaultComboBoxModel<T> model,
            Collection<T> items)
    {
        model.removeAllElements();
        for (T item : items)
            model.addElement(item);
    }
}
