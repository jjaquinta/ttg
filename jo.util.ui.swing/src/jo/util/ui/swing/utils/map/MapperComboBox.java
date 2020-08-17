package jo.util.ui.swing.utils.map;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

import jo.util.utils.obj.StringUtils;

public class MapperComboBox extends MapperBase implements ICtrlMapper
{
    public static final String ONECHAR = "onechar";
    
    private static final ActionListener mActionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e)
        {
            BeanMapUtils.doComponentChange(e);
        };
    };
    @Override
    public boolean isMapperFor(String name)
    {
        return "JComboBox".equals(name);
    }

    @Override
    public boolean isMapperFor(Component ctrl)
    {
        return ctrl instanceof JComboBox;
    }

    @Override
    public Object getFromCtrl(Component ctrl)
    {
        JComboBox<?> c = (JComboBox<?>)ctrl;
        if (isNameSetting(ctrl, ONECHAR))
        {
            String val = (String)c.getSelectedItem();
            if (StringUtils.isTrivial(val))
                return null;
            return val.charAt(0);
        }
        return c.getSelectedItem();
    }

    @Override
    public void setToCtrl(Component ctrl, Object val)
    {
        JComboBox<?> c = (JComboBox<?>)ctrl;
        if (val == null)
        {
            c.setSelectedIndex(-1);
            return;
        }
        if (isNameSetting(ctrl, ONECHAR))
        {
            for (int i = 0; i < c.getItemCount(); i++)
                if (c.getItemAt(i).toString().startsWith(val.toString()))
                {
                    c.setSelectedIndex(i);
                    return;
                }
        }
        c.setSelectedItem(val);
    }

    @Override
    public void linkToCtrl(Component ctrl)
    {
        ((JComboBox<?>)ctrl).addActionListener(mActionListener);
    }
}
