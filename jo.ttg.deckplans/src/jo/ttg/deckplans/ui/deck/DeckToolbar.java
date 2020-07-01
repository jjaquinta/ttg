package jo.ttg.deckplans.ui.deck;

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
import jo.ttg.deckplans.logic.DeckLogic;
import jo.ttg.deckplans.logic.PlanLogic;
import jo.ttg.deckplans.logic.RuntimeLogic;
import jo.util.ui.swing.ctrl.ThreadHelperDlg;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.ThreadHelper;

public class DeckToolbar extends JPanel
{
    private DeckViewer      mViewer;
    
    private JButton         mExport;
    private JButton         mUp;
    private JButton         mDown;
    private JButton         mPrevious;
    private JButton         mRedraw;
    private JLabel          mViewing;
    
    public DeckToolbar(DeckViewer viewer)
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
        mExport = new JButton("Export");
        mPrevious = new JButton("<< Planner");
        mRedraw = new JButton("Redraw");
        mViewing = new JLabel();
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mPrevious);
        left.add(mExport);
        left.add(mUp);
        left.add(mDown);
        left.add(mViewing);
        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));
        right.add(mRedraw);
        setLayout(new BorderLayout());
        add("West", left);
        add("East", right);
    }
    
    private void initLink()
    {
        ListenerUtils.listen(mUp, (ev) -> doUp());
        ListenerUtils.listen(mDown, (ev) -> doDown());
        ListenerUtils.listen(mPrevious, (ev) -> doPrevious());
        ListenerUtils.listen(mExport, (ev) -> doSave());
        ListenerUtils.listen(mRedraw, (ev) -> doRedraw());
        RuntimeLogic.listen("shipDeck", (ov,nv) -> doUpdateViewing());
    }       
    
    private void doSave()
    {
        File dir = RuntimeLogic.getInstance().getLastScan();
        if (dir != null)
            dir = dir.getParentFile();
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle("Save Ship Images");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "PNG files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith(".png");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showSaveDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File deckFile = chooser.getSelectedFile();
        if (deckFile == null)
            return;
        DeckLogic.write(deckFile);
    }
    
    private void doRedraw()
    {
        ThreadHelper t = PlanLogic.process();
        ThreadHelperDlg dlg = new ThreadHelperDlg((JFrame)SwingUtilities.getRoot(this), t, true);
        dlg.setVisible(true);
    }
    
    private void doPrevious()
    {
        RuntimeLogic.setMode(RuntimeBean.PLAN);
    }
    
    private void doUp()
    {
        mViewer.doUp();
        doUpdateViewing();
    }
    
    private void doDown()
    {
        mViewer.doDown();
        doUpdateViewing();
    }
    
    private void doUpdateViewing()
    {
        int decks = RuntimeLogic.getInstance().getShipDeck().getDecks().size() - 1;
        if (mViewer.getDeck() == 0)
            mViewing.setText("Overview of all "+decks+" decks");
        else
            mViewing.setText("Deck "+mViewer.getDeck()+" of "+decks);
    }
}
