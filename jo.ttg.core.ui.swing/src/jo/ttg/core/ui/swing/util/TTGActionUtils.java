package jo.ttg.core.ui.swing.util;

import java.util.function.Consumer;

import jo.ttg.core.ui.swing.ctrl.ITTGActioner;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;

public class TTGActionUtils
{
    public static void listen(ITTGActioner ctrl, final Consumer<TTGActionEvent> op)
    {
        ctrl.addTTGActionListener(new TTGActionListener() {
            @Override
            public void actionPerformed(TTGActionEvent e)
            {
                op.accept(e);
            }
        });
    }
}
