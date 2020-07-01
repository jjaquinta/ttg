package jo.ttg.deckplans.ui.plan;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.deckplans.logic.PlanLogic;
import jo.ttg.deckplans.logic.RuntimeLogic;
import jo.util.ui.swing.ctrl.ThreadHelperDlg;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.ThreadHelper;

public class PlanToolbar extends JPanel
{
    private PlanViewer      mViewer;

    private JButton         mImportFile;
    private JButton         mExport;
    private JButton         mUp;
    private JButton         mDown;
    private JButton         mPrevious;
    private JButton         mDecks;
    private JLabel          mViewing;
    
    public PlanToolbar(PlanViewer viewer)
    {
        mViewer = viewer;
        initInstantiate();
        initLink();
        initLayout();
        doUpdateViewing();
    }
    
    private void initInstantiate()
    {
        mUp = new JButton("\u2b63");
        mDown = new JButton("\u2b61");
        mImportFile = new JButton("Import (file)");
        mExport = new JButton("Export");
        mPrevious = new JButton("<< Scanner");
        mDecks = new JButton("Decks >>");
        mViewing = new JLabel();
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mPrevious);
        left.add(mImportFile);
        left.add(mExport);
        left.add(mUp);
        left.add(mDown);
        left.add(mViewing);
        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));
        right.add(mDecks);
        setLayout(new BorderLayout());
        add("West", left);
        add("East", right);
    }
    
    private void initLink()
    {
        ListenerUtils.listen(mImportFile, (ev) -> doImportFile());
        ListenerUtils.listen(mPrevious, (ev) -> doPrevious());
        ListenerUtils.listen(mExport, (ev) -> doSave());
        ListenerUtils.listen(mDecks, (ev) -> doDecks());
        ListenerUtils.listen(mUp, (ev) -> doUp());
        ListenerUtils.listen(mDown, (ev) -> doDown());
    }       
    
    private void doImportFile()
    {
        File dir = RuntimeLogic.getInstance().getLastPlan();
        if (dir != null)
            dir = dir.getParentFile();
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle("Open Ship Plan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "JSON files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith("_plan.json");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showOpenDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File scanFile = chooser.getSelectedFile();
        if (scanFile == null)
            return;
        PlanLogic.read(scanFile);
    }
    
    private void doSave()
    {
        File dir = RuntimeLogic.getInstance().getLastPlan();
        if (dir != null)
            dir = dir.getParentFile();
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle("Save Ship Plan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "JSON files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith("_plan.json");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showSaveDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File scanFile = chooser.getSelectedFile();
        if (scanFile == null)
            return;
        PlanLogic.write(scanFile);
    }
    
    private void doDecks()
    {
        ThreadHelper t = PlanLogic.process();
        ThreadHelperDlg dlg = new ThreadHelperDlg((JFrame)SwingUtilities.getRoot(this), t, true);
        dlg.setVisible(true);
    }
    
    private void doPrevious()
    {
        RuntimeLogic.setMode(RuntimeBean.SCAN);
    }
    
    private void doUp()
    {
        mViewer.setDeck(mViewer.getDeck() + 1);
        doUpdateViewing();
    }
    
    private void doDown()
    {
        mViewer.setDeck(mViewer.getDeck() - 1);
        doUpdateViewing();
    }
    
    private void doUpdateViewing()
    {
        int decks = mViewer.getDecks();
        if (mViewer.getDeck() == 0)
            mViewing.setText("Overview of all "+decks+" decks");
        else
            mViewing.setText("Deck "+mViewer.getDeck()+" of "+decks);
    }
}
