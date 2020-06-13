/*
 * Created on Oct 1, 2005
 *
 */
package jo.ttg.core.ui.swing.icons;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.sys.BodyBean;

public class BodyIconLogic
{
    private static List<IBodyViewHandler> mHandlers = null;
    
    private static synchronized void init()
    {
        if (mHandlers != null)
            return;
        mHandlers = new ArrayList<IBodyViewHandler>();
        mHandlers.add(new DefaultBodyViewHandler());
    }

    public static Image getImage(BodyBean b)
    {
        init();
        for (IBodyViewHandler handler : mHandlers)
        {
            Image ret = handler.getImage(b);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public static String getText(BodyBean b)
    {
        init();
        for (IBodyViewHandler handler : mHandlers)
        {
            String ret = handler.getText(b);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public static Image getColumnImage(BodyBean b, int columnIndex)
    {
        init();
        for (IBodyViewHandler handler : mHandlers)
        {
            Image ret = handler.getColumnImage(b, columnIndex);
            if (ret != null)
                return ret;
        }
        return null;
    }

    public static String getColumnText(BodyBean b, int columnIndex)
    {
        init();
        for (IBodyViewHandler handler : mHandlers)
        {
            String ret = handler.getColumnText(b, columnIndex);
            if (ret != null)
                return ret;
        }
        return null;
    }
}
