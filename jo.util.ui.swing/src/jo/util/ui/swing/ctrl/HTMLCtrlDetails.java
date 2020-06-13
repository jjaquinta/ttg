package jo.util.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import jo.util.utils.BeanUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;


public class HTMLCtrlDetails
{
    private String mTemplate = "<html></html>";
    private List<Chunk> mChunks = new ArrayList<HTMLCtrlDetails.Chunk>();
    private Map<String, Object> mSubstitutions = new HashMap<>();
    private List<Object> mBeans = new ArrayList<>();
    private static Map<String, BiFunction<Object, String, Object>> mModifierFunctions = new HashMap<>();
    
    // utils
    
    public static void addModifierFunction(String name, BiFunction<Object, String, Object> mf)
    {
        mModifierFunctions.put(name, mf);
    }
    
    private void updateChunks()
    {
        mChunks.clear();
        String html = mTemplate;
        for (;;)
        {
            int o = html.indexOf("[[");
            if (o < 0)
            {
                if (html.length() > 0)
                    mChunks.add(new Chunk(Chunk.TEXT, html));
                break;
            }
            if (o > 0)
                mChunks.add(new Chunk(Chunk.TEXT, html.substring(0, o)));
            int o2 = html.indexOf("]]", o);
            if (o2 < 0)
                break;
            mChunks.add(new Chunk(Chunk.SUB, html.substring(o + 2, o2)));
            html = html.substring(o2 + 2);
        }
        updateText();
    }
    
    void updateValues()
    {
        for (Chunk c : mChunks)
            if (c.mType == Chunk.SUB)
                for (Object b : mBeans)
                {
                    Object val = BeanUtils.get(b, c.mText);
                    if (val != null)
                        mSubstitutions.put(c.mText, val);
                }
    }
    
    String updateText()
    {
        updateValues();
        StringBuffer text = new StringBuffer();
        for (Chunk c : mChunks)
            if (c.mType == Chunk.TEXT)
                text.append(c.mText);
            else if (mSubstitutions.containsKey(c.mText))
            {
                Object val = mSubstitutions.get(c.mText);
                if (!StringUtils.isTrivial(c.mModifier))
                    val = modify(val, c.mModifier, c.mArg);
                text.append(val);
            }
        return text.toString();
    }
    
    void addBean(Object... beans)
    {
        for (Object bean : beans)
            mBeans.add(bean);
    }
    
    void removeBean(Object... beans)
    {
        for (Object bean : beans)
            mBeans.remove(bean);
    }
    
    void clearBeans()
    {
        mBeans.clear();
    }
    
    void replaceBean(Object... beans)
    {
        clearBeans();
        addBean(beans);
    }
    
    void setSubstitution(String key, Object val)
    {
        if (val == null)
            mSubstitutions.remove(key);
        else
            mSubstitutions.put(key, val);
    }
    
    void clearSubstitutions()
    {
        mSubstitutions.clear();
    }
    
    private Object modify(Object val, String modification, String arg)
    {
        if (mModifierFunctions.containsKey(modification))
            val = mModifierFunctions.get(modification).apply(val, arg);
        return val;
    }
    
    // getters and setters

    public String getTemplate()
    {
        return mTemplate;
    }

    public void setTemplate(String template)
    {
        mTemplate = template;
        updateChunks();
    }

    class Chunk
    {
        public static final int TEXT = 0;
        public static final int SUB = 1;
        
        int mType;
        String mText;
        String mModifier;
        String mArg;
        
        public Chunk(int type, String text)
        {
            mType = type;
            mText = text;
            if (mType == SUB)
            {
                int o = mText.indexOf(':');
                if (o > 0)
                {
                    mModifier = mText.substring(o + 1);
                    mText = mText.substring(0, o);
                    o = mModifier.indexOf(':');
                    if (o > 0)
                    {
                        mArg = mModifier.substring(o + 1);
                        mModifier = mModifier.substring(0, o);
                    }
                }
            }
        }
    }
    
    // standard modification functions
    static
    {
        HTMLCtrlDetails.addModifierFunction("zeroPrefix", (val,arg) -> StringUtils.zeroPrefix(val.toString(), IntegerUtils.parseInt(arg)));
        HTMLCtrlDetails.addModifierFunction("spacePrefix", (val,arg) -> StringUtils.prefix(val.toString(), ' ', IntegerUtils.parseInt(arg)));
        HTMLCtrlDetails.addModifierFunction("int", (val,arg) -> IntegerUtils.parseInt(val));
    }
}
