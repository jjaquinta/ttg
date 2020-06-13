/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;

/**
 * @author jjaquinta
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyPanel extends JPanel
{
    private static List<BodyPanelHandler> mHandlers = new ArrayList<BodyPanelHandler>();
    static
    {
        mHandlers.add(new DefaultBodyPanelHandler());
    }

    private CardLayout                    mLayout;
    private JPanel                        mBlankPanel;
    private Map<String, JPanel>           mPanelMap;
    private BodyBean                      mBody;

    public BodyPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mPanelMap = new HashMap<String, JPanel>();
        mBlankPanel = new JPanel();
    }

    private void initLayout()
    {
        mLayout = new CardLayout();
        setLayout(mLayout);
        add("blank", mBlankPanel);
        for (Iterator<BodyPanelHandler> i = mHandlers.iterator(); i.hasNext();)
        {
            BodyPanelHandler h = (BodyPanelHandler)i.next();
            h.getBodyPanels(mPanelMap);
        }
        for (Iterator<String> i = mPanelMap.keySet().iterator(); i.hasNext();)
        {
            String key = (String)i.next();
            add(key, (JPanel)mPanelMap.get(key));
        }
    }

    private void initLink()
    {
    }

    /**
     * @return
     */
    public BodyBean getBody()
    {
        return mBody;
    }

    /**
     * @param bean
     */
    public void setBody(BodyBean bean)
    {
        mBody = bean;
        for (Iterator<BodyPanelHandler> i = mHandlers.iterator(); i.hasNext();)
        {
            BodyPanelHandler h = (BodyPanelHandler)i.next();
            String name = h.handleBody(mPanelMap, mBody);
            if (name != null)
            {
                mLayout.show(this, name);
                return;
            }
        }
        mLayout.show(this, "blank");
    }

    public static void addHandler(BodyPanelHandler h)
    {
        mHandlers.add(0, h);
    }
}

class DefaultBodyPanelHandler implements BodyPanelHandler
{

    /*
     * (non-Javadoc)
     * 
     * @see ttg.view.ctrl.BodyPanelHandler#initLayout(ttg.view.ctrl.BodyPanel,
     * java.util.HashMap)
     */
    public void getBodyPanels(Map<String, JPanel> map)
    {
        map.put("world", new BodyWorldPanel());
        map.put("toids", new BodyToidsPanel());
        map.put("giant", new BodyGiantPanel());
        map.put("star", new BodyStarPanel());
    }

    /*
     * (non-Javadoc)
     * 
     * @see ttg.view.ctrl.BodyPanelHandler#handleBody(ttg.view.ctrl.BodyPanel,
     * java.util.HashMap, ttg.beans.sys.BodyBean)
     */
    public String handleBody(Map<String, JPanel> map, BodyBean b)
    {
        if (b instanceof BodyWorldBean)
        {
            ((BodyWorldPanel)map.get("world")).setBody((BodyWorldBean)b);
            return "world";
        }
        else if (b instanceof BodyToidsBean)
        {
            ((BodyToidsPanel)map.get("toids")).setBody((BodyToidsBean)b);
            return "toids";
        }
        else if (b instanceof BodyGiantBean)
        {
            ((BodyGiantPanel)map.get("giant")).setBody((BodyGiantBean)b);
            return "giant";
        }
        else if (b instanceof BodyStarBean)
        {
            ((BodyStarPanel)map.get("star")).setBody((BodyStarBean)b);
            return "star";
        }
        return null;
    }
}