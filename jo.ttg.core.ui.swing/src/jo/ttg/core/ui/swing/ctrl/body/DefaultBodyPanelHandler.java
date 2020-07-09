package jo.ttg.core.ui.swing.ctrl.body;

import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.ctrl.body.ext.BodySpecialPanel;

class DefaultBodyPanelHandler implements IBodyPanelHandler
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
        map.put("special", new BodySpecialPanel());
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
        else if (b instanceof BodySpecialBean)
        {
            ((BodySpecialPanel)map.get("special")).setBody((BodySpecialBean)b);
            return "special";
        }
        return null;
    }
}