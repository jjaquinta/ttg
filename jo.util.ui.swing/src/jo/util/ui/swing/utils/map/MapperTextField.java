package jo.util.ui.swing.utils.map;

import java.awt.Component;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class MapperTextField implements ICtrlMapper
{
    private static final FocusListener mFocusListener = new FocusAdapter() {
        public void focusLost(FocusEvent e) 
        {
            BeanMapUtils.doComponentChange(e);
        };
    };

    @Override
    public boolean isMapperFor(String name)
    {
        return "JTextField".equals(name);
    }

    @Override
    public boolean isMapperFor(Component ctrl)
    {
        return ctrl instanceof JTextField;
    }

    @Override
    public Object getFromCtrl(Component ctrl)
    {
        JTextField c = (JTextField)ctrl;
        return c.getText();
    }

    @Override
    public void setToCtrl(Component ctrl, Object val)
    {
        JTextField c = (JTextField)ctrl;
        if (val == null)
            val = "";
        if (!c.getText().equals(val.toString()))
            c.setText(val.toString());
    }

    @Override
    public void linkToCtrl(Component ctrl)
    {
        ((JTextField)ctrl).addFocusListener(mFocusListener);
    }
}
