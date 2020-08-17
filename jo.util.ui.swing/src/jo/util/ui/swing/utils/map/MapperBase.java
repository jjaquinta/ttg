package jo.util.ui.swing.utils.map;

import java.awt.Component;

import jo.util.utils.obj.StringUtils;

public class MapperBase
{
    protected boolean isNameSetting(Component ctrl, String setting)
    {
        String name = ctrl.getName();
        if (StringUtils.isTrivial(name))
            return false;
        return name.indexOf(setting) >= 0;
    }
}
