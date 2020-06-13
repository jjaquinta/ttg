package jo.util.ui.swing.ctrl;

import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class HTMLPane extends JEditorPane
{
    private HTMLCtrlDetails mDetails = new HTMLCtrlDetails();
    
    // constructors

    public HTMLPane()
    {
        super();
        init();
    }

    public HTMLPane(String type, String text)
    {
        super(type, text);
        init();
    }


    public HTMLPane(String url) throws IOException
    {
        super(url);
        init();
    }


    public HTMLPane(URL initialPage) throws IOException
    {
        super(initialPage);
        init();
    }

    // utils

    private void init()
    {
        setEditable(false);
        setContentType("text/html");
        addHyperlinkListener(new HyperlinkListener() {
            @Override public void hyperlinkUpdate(final HyperlinkEvent pE) {
                if (HyperlinkEvent.EventType.ACTIVATED == pE.getEventType()) {
                    String reference = pE.getDescription();
                    if (reference != null && reference.startsWith("#")) { // link must start with # to be internal reference
                        reference = reference.substring(1);
                        scrollToReference(reference);
                    }
                }
            }
        });
    }
    
    public void updateValues()
    {
        mDetails.updateValues();
    }
    
    public void updateText()
    {
        String html = mDetails.updateText();
        int o = html.indexOf("<body");
        if(o >= 0)
        {
            o = html.indexOf('>', o);
            if (o >= 0)
                html = html.substring(0, o+1) + "<a name='top'></a>" + html.substring(o + 1);
        }
        setText(html);
        scrollToReference("top");
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
