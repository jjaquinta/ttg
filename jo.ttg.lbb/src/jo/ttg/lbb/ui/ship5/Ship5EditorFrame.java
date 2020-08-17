package jo.ttg.lbb.ui.ship5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.json.simple.JSONUtils;

import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.ui.swing.utils.ListenerUtils;

public class Ship5EditorFrame extends JFrame
{
    private Ship5EditPanel mClient;
    private JButton mSave;
    private JLabel  mStatus;
    private JLabel  mDirty;

    public Ship5EditorFrame()
    {
        super("TTG High Guard Editor");
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        //setIconImages(IconLogic.getFrameIcons());
        mSave = new JButton("Save");
        mStatus = new JLabel();
        mDirty = new JLabel();
        mClient = new Ship5EditPanel();
    }

    private void initLayout()
    {
        JPanel statusbar = new JPanel();
        statusbar.setLayout(new BorderLayout());
        statusbar.add("East", mSave);
        statusbar.add("West", mDirty);
        statusbar.add("Center", mStatus);

        getContentPane().add("Center", mClient);
        getContentPane().add("South", statusbar);
        Dimension d = new Dimension(1024, 1024);
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
        ComponentUtils.componentResized(this, (ev) -> doResized());
        ListenerUtils.listen(mSave, (e) -> System.out.println(JSONUtils.toFormattedString(mClient.getShip().toJSON())));
    }
    
    private void doResized()
    {
//        Dimension d = getSize();
//        RuntimeLogic.getInstance().getSettings().put("screenWidth", d.width);
//        RuntimeLogic.getInstance().getSettings().put("screenHeight", d.height);
    }

    private void doFrameShut()
    {
        //RuntimeLogic.term();
        setVisible(false);
        System.exit(0);
    }
}
