package jo.ttg.core.ui.swing.ctrl.body;

import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.ctrl.body.ext.HTMLBodySpecialPanel;

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
        map.put("html.world", new HTMLBodyWorldPanel());
        map.put("html.toids", new HTMLBodyToidsPanel());
        map.put("html.giant", new HTMLBodyGiantPanel());
        map.put("html.star", new HTMLBodyStarPanel());
        map.put("html.special", new HTMLBodySpecialPanel());
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
            ((HTMLBodyWorldPanel)map.get("html.world")).setBody((BodyWorldBean)b);
            return "html.world";
        }
        else if (b instanceof BodyToidsBean)
        {
            ((HTMLBodyToidsPanel)map.get("html.toids")).setBody((BodyToidsBean)b);
            return "html.toids";
        }
        else if (b instanceof BodyGiantBean)
        {
            ((HTMLBodyGiantPanel)map.get("html.giant")).setBody((BodyGiantBean)b);
            return "html.giant";
        }
        else if (b instanceof BodyStarBean)
        {
            ((HTMLBodyStarPanel)map.get("html.star")).setBody((BodyStarBean)b);
            return "html.star";
        }
        else if (b instanceof BodySpecialBean)
        {
            ((HTMLBodySpecialPanel)map.get("html.special")).setBody((BodySpecialBean)b);
            return "html.special";
        }
        return null;
    }
}