package jo.ttg.lang.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.gen.imp.ui.swing.logic.IconLogic;
import jo.ttg.lang.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.utils.obj.IntegerUtils;

public class LinguisticsFrame extends JFrame
{
    private LinguisticsPanel mClient;
    private JLabel  mStatus;
    private JLabel  mDirty;

    public LinguisticsFrame()
    {
        super("TTG Linguistics");
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setIconImages(IconLogic.getFrameIcons());
        mStatus = new JLabel();
        mDirty = new JLabel();
        mClient = new LinguisticsPanel();
    }

    private void initLayout()
    {
        JPanel statusbar = new JPanel();
        statusbar.setLayout(new BorderLayout());
        statusbar.add("West", mDirty);
        statusbar.add("Center", mStatus);

        getContentPane().add("Center", mClient);
        getContentPane().add("South", statusbar);
        Dimension d = new Dimension(1024, 1024);
        if (RuntimeLogic.getInstance().getSettings().containsKey("screenWidth"))
            d.width = IntegerUtils.parseInt(RuntimeLogic.getInstance().getSettings().get("screenWidth"));
        if (RuntimeLogic.getInstance().getSettings().containsKey("screenHeight"))
            d.height = IntegerUtils.parseInt(RuntimeLogic.getInstance().getSettings().get("screenHeight"));
        setSize(d);
    }

    private void initLink()
    {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                doFrameShut();
            }
        });
        RuntimeLogic.listen("error", (ov, nv) -> doUpdateStatus());
        RuntimeLogic.listen("dirty", (ov, nv) -> doUpdateStatus());
        ComponentUtils.componentResized(this, (ev) -> doResized());
    }
    
    private void doResized()
    {
        Dimension d = getSize();
        RuntimeLogic.getInstance().getSettings().put("screenWidth", d.width);
        RuntimeLogic.getInstance().getSettings().put("screenHeight", d.height);
    }

    private void doUpdateStatus()
    {
        mStatus.setText(RuntimeLogic.getInstance().getError());
        mDirty.setText(" ");
    }

    private void doFrameShut()
    {
        RuntimeLogic.term();
        setVisible(false);
        System.exit(0);
    }
}
