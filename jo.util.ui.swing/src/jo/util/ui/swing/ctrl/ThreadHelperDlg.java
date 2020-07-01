package jo.util.ui.swing.ctrl;

import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.PCSBeanUtils;
import jo.util.utils.ThreadHelper;

public class ThreadHelperDlg extends JDialog
{
    private static final String SPINNER = "\u2b61\u2b67\u2b62\u2b68\u2b63\u2b69\u2b60\u2b66";
    
    private ThreadHelper    mThread;
    
    private JTextField      mSubTask;
    private JProgressBar    mProgress;
    private JButton         mCancel;
    private int             mSpinnerPos = 0;
    
    public ThreadHelperDlg(JFrame owner, ThreadHelper thread)
    {
        super(owner);
        mThread = thread;
        setup();
    }
    
    public ThreadHelperDlg(JFrame owner, ThreadHelper thread, boolean modal)
    {
        super(owner, modal);
        mThread = thread;
        setup();
    }

    private void setup()
    {
        initInstantiate();
        initLink();
        initLayout();
        doUpdateCancel();
        doUpdateProgress();
        doUpdateText();        
        pack();
    }
    
    private void initInstantiate()
    {
        mSubTask = new JTextField(mThread.getDetails().getSubTask());
        mSubTask.setEditable(false);
        mProgress = new JProgressBar(JProgressBar.HORIZONTAL, 0, mThread.getDetails().getTotalUnits());
        mCancel = new JButton("Cancel");
        Thread spinner = new Thread("ProgressSpinner") { public void run() { ThreadHelperDlg.this.doSpinner(); } };
        spinner.start();
    }

    private void initLayout()
    {
        setLayout(new GridLayout(3, 1));
        add(mSubTask);
        add(mProgress);
        add(mCancel);
    }
    
    private void initLink()
    {
        ListenerUtils.listen(mCancel, (ev) -> mThread.getDetails().setCanceled(true));
        PCSBeanUtils.listen(mThread.getDetails(), "subTask", (ov,nv) -> doUpdateText());
        PCSBeanUtils.listen(mThread.getDetails(), "totalUnits,unitsWorked", (ov,nv) -> doUpdateProgress());
        PCSBeanUtils.listen(mThread.getDetails(), "canceled,canCancel", (ov,nv) -> doUpdateCancel());
    }       
    
    private void doUpdateProgress()
    {
        SwingUtilities.invokeLater(new Runnable() {            
            @Override
            public void run()
            {
                mProgress.setIndeterminate(mThread.getDetails().getTotalUnits() <= 0);
                mProgress.setMaximum(mThread.getDetails().getTotalUnits());
                mProgress.setValue(mThread.getDetails().getUnitsWorked());
            }
        });
    }
    
    private void doUpdateCancel()
    {
        SwingUtilities.invokeLater(new Runnable() {            
            @Override
            public void run()
            {
                mCancel.setEnabled(mThread.getDetails().isCanCancel() && !mThread.getDetails().isCanceled());
            }
        });
    }
    
    private void doUpdateText()
    {
        SwingUtilities.invokeLater(new Runnable() {            
            @Override
            public void run()
            {
                setTitle(mThread.getName());
                mSubTask.setText(SPINNER.charAt(mSpinnerPos)+" "+mThread.getDetails().getSubTask());
            }
        });
    }
    
    private void doSpinner()
    {
        while (mThread.isAlive())
        {
            ThreadHelper.snooze(1000);
            SwingUtilities.invokeLater(new Runnable() {            
                @Override
                public void run()
                {
                    mSpinnerPos = (mSpinnerPos + 1)%SPINNER.length();
                    doUpdateText();
                }
            });
        }
        dispose();
    }
}
