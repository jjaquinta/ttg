package jo.ttg.gen.sw.ui.sys;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.core.ui.swing.ctrl.SystemView;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.gen.sw.ui.SWFrame;
import jo.util.ui.swing.logic.ColorUtils;

public class SWSystemViewer extends SystemView
{
    public SWSystemViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdateSystem();
    }

    private void initInstantiate()
    {
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
        setBackground(Color.black);
        setDate(RuntimeLogic.getInstance().getDate());
    }

    private void initLayout()
    {
    }

    private void initLink()
    {
        RuntimeLogic.listen("system", (ov,nv) -> doUpdateSystem());
        RuntimeLogic.listen("focusWorld", (ov,nv) -> setFocus(RuntimeLogic.getInstance().getFocusWorld()));
        RuntimeLogic.listen("cursorWorld", (ov,nv) -> setSelected(RuntimeLogic.getInstance().getCursorWorld()));
        RuntimeLogic.listen("date", (ov,nv) -> setDate(RuntimeLogic.getInstance().getDate()));
    }

    private void doUpdateSystem()
    {
        setSystem(RuntimeLogic.getInstance().getSystem());
        setFocus(RuntimeLogic.getInstance().getFocusWorld());
        setSelected(RuntimeLogic.getInstance().getCursorWorld());
    }
    
    @Override
    protected void paintBackground(Graphics g, Dimension size)
    {
        for (int x = 0; x < size.width; x += SWFrame.STARFIELD.getWidth())
            for (int y = 0; y < size.height; y += SWFrame.STARFIELD.getHeight())
                g.drawImage(SWFrame.STARFIELD, x, y, null);
    }
    
    @Override
    protected void paintBody(Graphics2D g2, int bx, int by, BodyBean b, int px,
            int py, int pr, boolean drawChildren, int dx, int dy)
    {
        super.paintBody(g2, bx, by, b, px, py, pr, drawChildren, dx, dy);
        Rectangle onScreen = mOnScreen.get(b);
        double pop = getPop(b);
        if (pop > 1000)
        {
            if (pop > 1000000)
                g2.setColor(ColorUtils.LimeGreen);
            else if (pop > 1000000)
                g2.setColor(ColorUtils.LightSeaGreen);
            else if (pop > 100000)
                g2.setColor(ColorUtils.SeaGreen);
            else
                g2.setColor(ColorUtils.DarkSeaGreen);
            g2.fillOval(onScreen.x-8-dx, onScreen.y-8-dy, 8, 8);
        }
    }
    
    private double getPop(BodyBean b)
    {
        double pop = 0;
        if (b instanceof BodyPopulated)
            pop += ((BodyPopulated)b).getPopulatedStats().getUPP().getPop().getPopulation();
        if (b != getFocus())
            for (BodyBean c : b.getSatelites())
                pop += getPop(c);
        return pop;
    }
    
    @Override
    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        super.fireTTGActionEvent(ev);
        if (ev.getID() == TTGActionEvent.DEACTIVATED)
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR);
        else
        {
            BodyBean body = (BodyBean)ev.getSource();
            if (ev.getID() == TTGActionEvent.ACTIVATED)
            {
                if (RuntimeLogic.getInstance().getFocusWorld() == body)
                    RuntimeLogic.setZoom(RuntimeBean.ZOOM_SURFACE);
                else
                    RuntimeLogic.setFocusWorld(body);
            }
            else if (ev.getID() == TTGActionEvent.SELECTED)
                RuntimeLogic.setCursorWorld(body);
        }
    }
}
