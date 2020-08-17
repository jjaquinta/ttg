package jo.util.ui.swing.utils.map;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MapperSpinner implements ICtrlMapper
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
        return "JSpinner".equals(name);
    }

    @Override
    public boolean isMapperFor(Component ctrl)
    {
        return ctrl instanceof JSpinner;
    }

    @Override
    public Object getFromCtrl(Component ctrl)
    {
        JSpinner c = (JSpinner)ctrl;
        return c.getValue();
    }

    @Override
    public void setToCtrl(Component ctrl, Object val)
    {
        JSpinner c = (JSpinner)ctrl;
        if (val == null)
            val = c.getModel().getValue();
        c.setValue(val);
    }

    @Override
    public void linkToCtrl(Component ctrl)
    {
        ((JSpinner)ctrl).addChangeListener(mChangeListener);
    }
}
