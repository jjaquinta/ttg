package jo.util.ui.swing.utils.map;

import java.awt.Component;

public interface ICtrlMapper
{
    public boolean isMapperFor(String name);
    public boolean isMapperFor(Component ctrl);
    public Object getFromCtrl(Component ctrl);
    public void setToCtrl(Component ctrl, Object val);
    public void linkToCtrl(Component ctrl);
}
