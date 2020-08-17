package jo.util.ui.swing.utils.map;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.util.utils.obj.BooleanUtils;

public class MapperCheckBox implements ICtrlMapper
{
    private static final ChangeListener mChangeListener = new ChangeListener() {
        @Override
        public void stateChanged(ChangeEvent e)
        {
            BeanMapUtils.doComponentChange(e);
        }
    };

    @Override
    public boolean isMapperFor(String name)
    {
        return "JCheckBox".equals(name);
    }

    @Override
    public boolean isMapperFor(Component ctrl)
    {
        return ctrl instanceof JCheckBox;
    }

    @Override
    public Object getFromCtrl(Component ctrl)
    {
        JCheckBox c = (JCheckBox)ctrl;
        return c.isSelected();
    }

    @Override
    public void setToCtrl(Component ctrl, Object val)
    {
        JCheckBox c = (JCheckBox)ctrl;
        c.setSelected(BooleanUtils.parseBoolean(val));
    }

    @Override
    public void linkToCtrl(Component ctrl)
    {
        ((JCheckBox)ctrl).addChangeListener(mChangeListener);
    }
}
