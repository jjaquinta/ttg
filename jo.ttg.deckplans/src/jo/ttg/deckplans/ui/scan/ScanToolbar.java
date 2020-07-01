package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;

import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.deckplans.logic.RuntimeLogic;
import jo.ttg.deckplans.logic.ScanLogic;
import jo.util.ui.swing.ctrl.ThreadHelperDlg;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.ThreadHelper;
import jo.util.utils.obj.StringUtils;

public class ScanToolbar extends JPanel
{
    private JButton         mImportFile;
    private JButton         mImportText;
    private JButton         mImportLib;
    private JButton         mExport;
    private JButton         mProcess;
    
    public ScanToolbar()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    
    private void initInstantiate()
    {
        mImportFile = new JButton("Import (file)");
        mImportText = new JButton("Import (text)");
        mImportLib = new JButton("Import (lib)");
        mExport = new JButton("Export");
        mProcess = new JButton("Process >>");
    }

    private void initLayout()
    {
        JPanel left = new JPanel();
        left.setLayout(new FlowLayout(FlowLayout.LEFT));
        left.add(mImportFile);
        left.add(mImportText);
        left.add(mImportLib);
        left.add(mExport);
        JPanel right = new JPanel();
        right.setLayout(new FlowLayout(FlowLayout.RIGHT));
        right.add(mProcess);
        setLayout(new BorderLayout());
        add("West", left);
        add("East", right);
    }
    
    private void initLink()
    {
        ListenerUtils.listen(mImportFile, (ev) -> doImportFile());
        ListenerUtils.listen(mImportText, (ev) -> doImportText());
        ListenerUtils.listen(mImportLib, (ev) -> doImportLib());
        ListenerUtils.listen(mExport, (ev) -> doSave());
        ListenerUtils.listen(mProcess, (ev) -> doProcess());
    }       
    
    private void doImportFile()
    {
        File dir = RuntimeLogic.getInstance().getLastScan();
        if (dir != null)
            dir = dir.getParentFile();
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle("Open Ship Scan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "JSON files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith("_scan.json");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showOpenDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File scanFile = chooser.getSelectedFile();
        if (scanFile == null)
            return;
        ScanLogic.read(scanFile);
    }
    
    private void doSave()
    {
        File dir = RuntimeLogic.getInstance().getLastScan();
        if (dir != null)
            dir = dir.getParentFile();
        JFileChooser chooser = new JFileChooser(dir);
        chooser.setDialogTitle("Save Ship Scan");
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        FileFilter filter = new FileFilter()
        {
            public String getDescription()
            {
                return "JSON files";
            }
            public boolean accept(File pathname)
            {
                return pathname.getName().toLowerCase().endsWith("_scan.json");
            }
        };
        chooser.setFileFilter(filter);
        int rc = chooser.showSaveDialog(this);
        if (rc != JFileChooser.APPROVE_OPTION)
            return;
        File scanFile = chooser.getSelectedFile();
        if (scanFile == null)
            return;
        ScanLogic.write(scanFile);
    }
    
    private void doProcess()
    {
        ThreadHelper t = ScanLogic.process();
        ThreadHelperDlg dlg = new ThreadHelperDlg((JFrame)SwingUtilities.getRoot(this), t, true);
        dlg.setVisible(true);
    }
    
    private void doImportText()
    {
        ImportScanTextDlg dlg = new ImportScanTextDlg((JFrame)SwingUtilities.getRoot(this));
        dlg.setVisible(true);
        if (!StringUtils.isTrivial(dlg.getTextData()))
            ScanLogic.read(dlg.getTextData());
    }
    
    private void doImportLib()
    {
        ImportLibraryDlg dlg = new ImportLibraryDlg((JFrame)SwingUtilities.getRoot(this));
        dlg.setVisible(true);
        LibEntryBean lib = dlg.getLibrary();
        if (lib != null)
            ScanLogic.read(lib);
    }
}
