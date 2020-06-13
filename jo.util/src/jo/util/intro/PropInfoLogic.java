package jo.util.intro;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PropInfoLogic
{
    public static List<PropInfo> getProps(Class<?> clazz)
    {
        List<PropInfo> props = new ArrayList<PropInfo>();
        Map<String,Method> getters = new HashMap<String, Method>();
        Map<String,Class<?>> getterType = new HashMap<String, Class<?>>();
        Map<String,Method> setters = new HashMap<String, Method>();        
        Map<String,Class<?>> setterType = new HashMap<String, Class<?>>();        
        for (Method m : clazz.getMethods())
        {
            if (m.getAnnotation(PseudoProp.class) != null)
                continue;
            String name;
            if (m.getName().startsWith("get") && (m.getReturnType() != null))
            {
                name = m.getName().substring(3);
                getters.put(name, m);
                getterType.put(name, m.getReturnType());
            }
            else if (m.getName().startsWith("is") && (m.getReturnType() == boolean.class))
            {
                name = m.getName().substring(2);
                getters.put(name, m);
                getterType.put(name, m.getReturnType());
            }
            else if (m.getName().startsWith("set") && (m.getParameterTypes().length == 1))
            {
                name = m.getName().substring(3);
                setters.put(name, m);
                setterType.put(name, m.getParameterTypes()[0]);
            }
            else
                continue;
            if (setters.containsKey(name) && (getterType.get(name) == setterType.get(name)))
            {
                PropInfo prop = new PropInfo();
                prop.setName(name);
                prop.setType(getterType.get(name));
                prop.setGetter(getters.get(name));
                prop.setSetter(setters.get(name));
                props.add(prop);
            }
            
        }
        return props;
    }
}
