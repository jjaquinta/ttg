package jo.util.ui.swing.utils.map;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import jo.util.beans.PCSBean;
import jo.util.utils.BeanUtils;

public class BeanMap
{
    private static final Map<Component, BeanMap>            mComponentIndex = new HashMap<>();
    private static final Map<PCSBean, Map<String, BeanMap>> mBeanIndex      = new HashMap<>();

    private Component                                       mCtrl;
    private PCSBean                                         mBean;
    private String                                          mProp;
    private ICtrlMapper                                     mMapper;
    private Function<Object, Object>                        mConvBeanToCtrl;
    private Function<Object, Object>                        mConvCtrlToBean;
    private BiConsumer<Object, Object>                      mBeanSet;
    private Function<Object, Object>                        mBeanGet;

    public BeanMap(Component ctrl, PCSBean bean, String prop)
    {
        mCtrl = ctrl;
        mBean = bean;
        mProp = prop;
        index();
    }

    public BeanMap(Component ctrl, PCSBean bean, String prop, Function<Object, Object> bean2ctrl, Function<Object, Object> ctrl2bean)
    {
        this(ctrl, bean, prop);
        mConvBeanToCtrl = bean2ctrl;
        mConvCtrlToBean = ctrl2bean;
    }

    public BeanMap(Component ctrl, PCSBean bean, String prop, Function<Object, Object> bean2ctrl, Function<Object, Object> ctrl2bean,
            BiConsumer<Object, Object> beanSet, Function<Object,Object> beanGet)
    {
        this(ctrl, bean, prop, bean2ctrl, ctrl2bean);
        mBeanSet = beanSet;
        mBeanGet = beanGet;
    }

    // globals
    public static BeanMap getMap(Component ctrl)
    {
        return mComponentIndex.get(ctrl);
    }

    public static BeanMap getMap(PCSBean bean, String prop)
    {
        return mBeanIndex.get(bean).get(prop);
    }

    // utility

    private void index()
    {
        mComponentIndex.put(getCtrl(), this);
        Map<String, BeanMap> propIndex = mBeanIndex.get(getBean());
        if (propIndex == null)
        {
            propIndex = new HashMap<String, BeanMap>();
            mBeanIndex.put(getBean(), propIndex);
        }
        propIndex.put(getProp(), this);
        mMapper = BeanMapUtils.findMapper(mCtrl);
        if (mMapper == null)
            System.err.println("Cannot find mapper for "+mCtrl.getClass().getName()+"/"+mCtrl.getName());
    }

    public static void unindex(Component ctrl, PCSBean bean, String prop)
    {
        mComponentIndex.remove(ctrl);
        Map<String, BeanMap> propIndex = mBeanIndex.get(bean);
        if (propIndex != null)
        {
            propIndex.remove(prop);
            if (propIndex.size() == 0)
                mBeanIndex.remove(bean);
        }        
    }

    private boolean different(Object o1, Object o2)
    {
        if ((o1 == null) && (o2 == null))
            return false;
        if ((o1 != null) && (o2 != null))
            return !o1.equals(o2);
        return true;
    }

    public void copyBeanToCtrl()
    {
        Object newVal = getFromBean();
        if (mConvBeanToCtrl != null)
            newVal = mConvBeanToCtrl.apply(newVal);
        Object oldVal = getFromCtrl();
        if (different(newVal, oldVal))
            setToCtrl(newVal);
    }

    public void copyCtrlToBean()
    {
        Object newVal = getFromCtrl();
        if (mConvCtrlToBean != null)
            newVal = mConvCtrlToBean.apply(newVal);
        Object oldVal = getFromBean();
        if (different(newVal, oldVal))
            setToBean(newVal);
    }

    public Object getFromBean()
    {
        if (mBeanGet != null)
            return mBeanGet.apply(mBean);
        else
            return BeanUtils.get(getBean(), getProp());
    }

    public void setToBean(Object val)
    {
        if (mBeanSet != null)
            mBeanSet.accept(mBean, val);
        else
            BeanUtils.set(mBean, mProp, val);
    }

    public Object getFromCtrl()
    {
        return mMapper.getFromCtrl(mCtrl);
    }

    public void setToCtrl(Object val)
    {
        mMapper.setToCtrl(mCtrl, val);
    }

    // getters and setters

    public Component getCtrl()
    {
        return mCtrl;
    }

    public void setCtrl(Component ctrl)
    {
        mCtrl = ctrl;
    }

    public PCSBean getBean()
    {
        return mBean;
    }

    public void setBean(PCSBean bean)
    {
        mBean = bean;
    }

    public String getProp()
    {
        return mProp;
    }

    public void setProp(String prop)
    {
        mProp = prop;
    }

    public Function<Object, Object> getConvBeanToCtrl()
    {
        return mConvBeanToCtrl;
    }

    public void setConvBeanToCtrl(Function<Object, Object> convBeanToCtrl)
    {
        mConvBeanToCtrl = convBeanToCtrl;
    }

    public Function<Object, Object> getConvCtrlToBean()
    {
        return mConvCtrlToBean;
    }

    public void setConvCtrlToBean(Function<Object, Object> convCtrlToBean)
    {
        mConvCtrlToBean = convCtrlToBean;
    }

    public ICtrlMapper getMapper()
    {
        return mMapper;
    }

    public void setMapper(ICtrlMapper mapper)
    {
        mMapper = mapper;
    }
}