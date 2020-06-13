package jo.ttg.gen.sw.ui.sys;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import jo.ttg.core.ui.swing.ctrl.SystemTree;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;

public class SWSystemList extends JPanel
{
    private SystemTree mList;

    public SWSystemList()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateDisplay();
    }

    private void initInstantiate()
    {
        mList = new SystemTree();
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mList);
    }

    private void initLink()
    {
        RuntimeLogic.listen("system", (ov,nv) -> updateDisplay());
        ListenerUtils.change(mList, (ev) -> RuntimeLogic.setCursorWorld(mList.getSelectedBody()));
        MouseUtils.mouseClicked(mList, (ev) -> doMouseClicked(ev));
        RuntimeLogic.listen("cursorWorld", (ov,nv) -> mList.setSelectedBody(RuntimeLogic.getInstance().getCursorWorld()));
    }
    
    private void doMouseClicked(MouseEvent ev)
    {
        if (ev.getClickCount() == 2)
            if (ev.getButton() == MouseEvent.BUTTON1)
                RuntimeLogic.setZoom(RuntimeBean.ZOOM_SURFACE);
            else
                RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR);
    }

    private void updateDisplay()
    {
        mList.setSystem(RuntimeLogic.getInstance().getSystem());
        mList.setSelectedBody(RuntimeLogic.getInstance().getCursorWorld());
    }
}
