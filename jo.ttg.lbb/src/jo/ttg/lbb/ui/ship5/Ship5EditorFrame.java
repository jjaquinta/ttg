package jo.ttg.lbb.ui.ship5;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.json.simple.JSONUtils;

import jo.ttg.lbb.data.ship5.Ship5Design;
import jo.ttg.lbb.data.ship5.Ship5Stats;
import jo.ttg.lbb.logic.ship5.Ship5StatsLogic;
import jo.util.ui.swing.utils.ComponentUtils;
import jo.util.ui.swing.utils.ListenerUtils;

public class Ship5EditorFrame extends JFrame
{
    private Ship5Design mShipDesign;
    private Ship5Stats mShipStats;
    
    private Ship5EditPanel mEditor;
    private Ship5StatsPanel mStats;
    private JButton mSave;
    private JLabel  mStatus;
    private JLabel  mDirty;

    public Ship5EditorFrame()
    {
        super("TTG High Guard Editor");
        initInstantiate();
        initLink();
        initLayout();
        Ship5StatsLogic.updateStats(mShipStats);
    }

    private void initInstantiate()
    {
        mShipDesign = new Ship5Design();
        mShipStats = Ship5StatsLogic.getStats(mShipDesign);
        //setIconImages(IconLogic.getFrameIcons());
        mSave = new JButton("Save");
        mStatus = new JLabel();
        mDirty = new JLabel();
        mEditor = new Ship5EditPanel(mShipDesign);
        mStats = new Ship5StatsPanel(mShipStats);
    }

    private void initLayout()
    {
        JPanel statusbar = new JPanel();
        statusbar.setLayout(new BorderLayout());
        statusbar.add("East", mSave);
        statusbar.add("West", mDirty);
        statusbar.add("Center", mStatus);
        
        JSplitPane client = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, mEditor, mStats);

        getContentPane().add("Center", client);
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
        ListenerUtils.listen(mSave, (e) -> System.out.println(JSONUtils.toFormattedString(mEditor.getShip().toJSON())));
        mShipDesign.addPropertyChangeListener(new PropertyChangeListener() {            
            @Override
            public void propertyChange(PropertyChangeEvent evt)
            {
                Ship5StatsLogic.updateStats(mShipStats);
            }
        });
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
