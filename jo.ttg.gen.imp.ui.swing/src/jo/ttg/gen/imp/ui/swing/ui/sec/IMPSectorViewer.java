package jo.ttg.gen.imp.ui.swing.ui.sec;

import java.awt.Dimension;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.core.ui.swing.ctrl.SubSectorField;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.gen.imp.ui.swing.data.RuntimeBean;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;

public class IMPSectorViewer extends SubSectorField
{
    public IMPSectorViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        setOrigin(RuntimeLogic.getInstance().getFocusPoint());
        setSelected(RuntimeLogic.getInstance().getCursorPoint());
    }

    private void initInstantiate()
    {
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
    }

    private void initLayout()
    {
    }

    private void initLink()
    {
        RuntimeLogic.listen("focusPoint", (ov, nv) -> setOrigin((OrdBean)nv));
        RuntimeLogic.listen("cursorPoint", (ov, nv) -> setSelected((OrdBean)nv));
    }
    
    @Override
    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        super.fireTTGActionEvent(ev);
        if (ev.getID() == TTGActionEvent.SELECTED)
        {
            RuntimeLogic.setCursorPoint((OrdBean)ev.getDetail());
        }
        else if (ev.getID() == TTGActionEvent.ACTIVATED)
        {
            RuntimeLogic.setFocusPoint(((SubSectorBean)ev.getObject()).getUpperBound());
            RuntimeLogic.setCursorPoint((OrdBean)ev.getDetail());
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR);
        }
    }
}
