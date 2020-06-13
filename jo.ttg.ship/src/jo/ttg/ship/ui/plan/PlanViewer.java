package jo.ttg.ship.ui.plan;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.vecmath.Point3i;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.ship.beans.plan.ShipImageSettingsBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.img.ShipPlanImageLogic;
import jo.util.ui.swing.utils.ListenerUtils;

public class PlanViewer extends JPanel
{
    private ShipPlanBean    mPlan;
    private BufferedImage[] mDecks;
    private int             mDeck;
    private Point3i         mPoint;
    private ShipSquareBean  mSquare;
    private ShipImageSettingsBean mSettings;

    private JButton         mOpen;
    private JButton         mSave;
    private JButton         mUp;
    private JButton         mDown;
    private JButton         mRefresh;
    private JScrollPane     mScroller;
    private JLabel          mClient;
    
    private PlanViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    
    private void initInstantiate()
    {
        mOpen = new JButton("Open");
        mSave = new JButton("Save");
        mUp = new JButton("Up");
        mDown = new JButton("Down");
        mRefresh = new JButton("Refresh");
        mClient = new JLabel();
        mScroller = new JScrollPane(mClient);
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(mOpen);
        buttonBar.add(mSave);
        buttonBar.add(mUp);
        buttonBar.add(mDown);
        buttonBar.add(mRefresh);
        
        setLayout(new BorderLayout());
        add("Center", mScroller);
        add("North", buttonBar);
    }
    
    private void initLink()
    {
        ListenerUtils.listen(mOpen, (ev) -> doOpen());
        ListenerUtils.listen(mSave, (ev) -> doSave());
        ListenerUtils.listen(mUp, (ev) -> doUp());
        ListenerUtils.listen(mDown, (ev) -> doDown());
        ListenerUtils.listen(mRefresh, (ev) -> doRefreshImage());
    }       
    
    private void doOpen()
    {
        JFileChooser chooser = new JFileChooser(new File("d:\\temp\\data\\ship\\deanfiles\\data"));
        chooser.setDialogTitle("Select Ship Plan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "JSON files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith("_ship.json");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showOpenDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File planFile = chooser.getSelectedFile();
        if (planFile == null)
            return;
        try
        {
            JSONObject json = JSONUtils.readJSON(planFile);
            mPlan = new ShipPlanBean(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        ((JFrame)SwingUtilities.getRoot(this)).setTitle(planFile.getName());
        doRefreshImage();
    }
    
    private void doSave()
    {
        
    }
    
    private void doUp()
    {
        if (mDeck > 0)
            mDeck--;
        doRefreshLevel();
    }
    
    private void doDown()
    {
        if (mDeck < mDecks.length - 1)
            mDeck++;
        doRefreshLevel();
    }
    
    private void doRefreshImage()
    {
        List<BufferedImage> decks = ShipPlanImageLogic.printShipImage(mPlan, mSettings);
        if (decks == null)
            return;
        decks.remove(0); // remove overview
        mDecks = decks.toArray(new BufferedImage[0]);
        if (mDeck >= mDecks.length)
            mDeck = 0;
        doRefreshLevel();
    }
    
    private void doRefreshLevel()
    {
        mClient.setIcon(new ImageIcon(mDecks[mDeck]));
        mScroller.invalidate();
    }
    
    public static void main(String[] argv)
    {
        JFrame frame = new JFrame("Ship Viewer");
        PlanViewer client = new PlanViewer();
        frame.add("Center", client);
        frame.setSize(1024, 768);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
