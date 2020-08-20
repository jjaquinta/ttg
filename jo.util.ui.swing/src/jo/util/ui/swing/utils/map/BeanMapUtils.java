package jo.util.ui.swing.utils.map;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import jo.util.beans.PCSBean;
import jo.util.utils.obj.StringUtils;

public class BeanMapUtils
{
    private static final List<ICtrlMapper> mMappers = new ArrayList<ICtrlMapper>();
    static
    {
        addMapper(new MapperTextField());
        addMapper(new MapperComboBox());
        addMapper(new MapperSpinner());
        addMapper(new MapperCheckBox());
    }
    private static final PropertyChangeListener mPCL = new PropertyChangeListener() {        
        @Override
        public void propertyChange(PropertyChangeEvent evt)
        {
            BeanMapUtils.doPropertyChange(evt);
        }
    };
    
    public static void addMapper(ICtrlMapper mapper)
    {
        for (ICtrlMapper m : mMappers)
            if (m.getClass() == mapper.getClass())
                return; // already there
        mMappers.add(mapper);
    }
    
    public static ICtrlMapper findMapper(Component ctrl)
    {
        String name = ctrl.getName();
        if (!StringUtils.isTrivial(name))
            for (ICtrlMapper m : mMappers)
                if (m.isMapperFor(name))
                    return m;
        for (ICtrlMapper m : mMappers)
            if (m.isMapperFor(ctrl))
                return m;
        return null;
    }
    
    public static void map(Component ctrl, PCSBean bean, String prop)
    {
        map(ctrl, bean, prop, null, null);
    }
    
    public static void map(Component ctrl, PCSBean bean, String prop, Function<Object, Object> bean2ctrl, Function<Object, Object> ctrl2bean)
    {
        map(ctrl, bean, prop, bean2ctrl, ctrl2bean, null, null);
    }
    
    public static void map(Component ctrl, PCSBean bean, String prop, Function<Object, Object> bean2ctrl, Function<Object, Object> ctrl2bean,
            BiConsumer<Object, Object> beanSetter, Function<Object,Object> beanGetter)
    {
        BeanMap map = new BeanMap(ctrl, bean, prop, bean2ctrl, ctrl2bean,
                beanSetter, beanGetter);
        map.copyBeanToCtrl();
        bean.addPropertyChangeListener(prop, mPCL);
        map.getMapper().linkToCtrl(ctrl);
    }
    
    public static void unmap(Component ctrl, PCSBean bean, String prop)
    {
        bean.removePropertyChangeListener(prop);
        BeanMap.unindex(ctrl, bean, prop);
    }
    
    private static void doPropertyChange(PropertyChangeEvent evt)
    {
        PCSBean bean = (PCSBean)evt.getSource();
        String prop = evt.getPropertyName();
        Object val = evt.getNewValue();
        BeanMap map = BeanMap.getMap(bean, prop);
        map.setToCtrl(val);
    }
    
    public static void doComponentChange(EventObject evt)
    {
        Component ctrl = (Component)evt.getSource();
        BeanMap map = BeanMap.getMap(ctrl);
        map.copyCtrlToBean();
    }
}
