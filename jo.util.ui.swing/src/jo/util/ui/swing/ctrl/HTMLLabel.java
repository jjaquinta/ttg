package jo.util.ui.swing.ctrl;

import javax.swing.Icon;
import javax.swing.JLabel;


public class HTMLLabel extends JLabel
{
    private HTMLCtrlDetails mDetails = new HTMLCtrlDetails();
    
    // constructors

    public HTMLLabel()
    {
        super();
    }

    public HTMLLabel(Icon image, int horizontalAlignment)
    {
        super(image, horizontalAlignment);
    }

    public HTMLLabel(Icon image)
    {
        super(image);
    }

    public HTMLLabel(String text, Icon icon, int horizontalAlignment)
    {
        super(text, icon, horizontalAlignment);
    }

    public HTMLLabel(String text, int horizontalAlignment)
    {
        super(text, horizontalAlignment);
    }

    public HTMLLabel(String text)
    {
        super(text);
    }
    
    // utils
    
    public void updateValues()
    {
        mDetails.updateValues();
    }
    
    public void updateText()
    {
        setText(mDetails.updateText());
    }
    
    public void addBean(Object... beans)
    {
        mDetails.addBean(beans);
    }
    
    public void removeBean(Object... beans)
    {
        mDetails.removeBean(beans);
    }
    
    public void clearBeans()
    {
        mDetails.clearBeans();
    }
    
    public void replaceBean(Object... beans)
    {
        mDetails.replaceBean(beans);
    }
    
    public void setSubstitution(String key, Object val)
    {
        mDetails.setSubstitution(key, val);
    }
    
    public void clearSubstitutions()
    {
        mDetails.clearSubstitutions();
    }
    
    // getters and setters

    public String getTemplate()
    {
        return mDetails.getTemplate();
    }

    public void setTemplate(String template)
    {
        mDetails.setTemplate(template);
    }
}
