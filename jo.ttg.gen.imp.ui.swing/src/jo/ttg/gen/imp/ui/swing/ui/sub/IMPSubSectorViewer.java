package jo.ttg.gen.imp.ui.swing.ui.sub;

import java.awt.Dimension;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.core.ui.swing.ctrl.HexScroller;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;

public class IMPSubSectorViewer extends HexScroller
{

    public IMPSubSectorViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        setCenter(RuntimeLogic.getInstance().getFocusPoint());
        setFocus(RuntimeLogic.getInstance().getCursorPoint());
    }

    private void initInstantiate()
    {
        setHexSide(RuntimeLogic.getInstance().getHexSide());
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
    }

    private void initLayout()
    {
    }

    private void initLink()
    {
        RuntimeLogic.listen("focusPoint", (ov, nv) -> setCenter((OrdBean)nv));
        RuntimeLogic.listen("cursorPoint", (ov, nv) -> setFocus((OrdBean)nv));
        RuntimeLogic.listen("displayLinks,displayGrid", (ov,nv) -> this.repaint());
    }

    @Override
    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        super.fireTTGActionEvent(ev);
        if (ev.getID() == TTGActionEvent.SELECTED)
        {
            if (ev.getObject() instanceof MainWorldBean)
                RuntimeLogic.setCursorPoint(((MainWorldBean)ev.getObject()).getOrds());
            else if (ev.getObject() instanceof OrdBean)
                RuntimeLogic.setCursorPoint((OrdBean)ev.getObject());
        }
        else if (ev.getID() == TTGActionEvent.ACTIVATED)
        {
            RuntimeLogic.setFocusPoint(((MainWorldBean)ev.getObject()).getOrds());
            RuntimeLogic.setCursorPoint(((MainWorldBean)ev.getObject()).getOrds());
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM);
        }
        else if (ev.getID() == TTGActionEvent.DEACTIVATED)
        {
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SECTOR);
        }
        RuntimeLogic.getInstance().setHexSide(getHexSide());
    }
}
