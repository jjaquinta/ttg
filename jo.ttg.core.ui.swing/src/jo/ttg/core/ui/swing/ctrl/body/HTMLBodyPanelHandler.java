package jo.ttg.core.ui.swing.ctrl.body;

import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;

public class HTMLBodyPanelHandler implements IBodyPanelHandler
{

    /*
     * (non-Javadoc)
     * 
     * @see ttg.view.ctrl.BodyPanelHandler#initLayout(ttg.view.ctrl.BodyPanel,
     * java.util.HashMap)
     */
    public void getBodyPanels(Map<String, JPanel> map)
    {
        map.put("world", new HTMLBodyWorldPanel());
        map.put("toids", new HTMLBodyToidsPanel());
        map.put("giant", new HTMLBodyGiantPanel());
        map.put("star", new HTMLBodyStarPanel());
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
            ((HTMLBodyWorldPanel)map.get("world")).setBody((BodyWorldBean)b);
            return "world";
        }
        else if (b instanceof BodyToidsBean)
        {
            ((HTMLBodyToidsPanel)map.get("toids")).setBody((BodyToidsBean)b);
            return "toids";
        }
        else if (b instanceof BodyGiantBean)
        {
            ((HTMLBodyGiantPanel)map.get("giant")).setBody((BodyGiantBean)b);
            return "giant";
        }
        else if (b instanceof BodyStarBean)
        {
            ((HTMLBodyStarPanel)map.get("star")).setBody((BodyStarBean)b);
            return "star";
        }
        return null;
    }
}