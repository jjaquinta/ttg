package jo.ttg.gen.imp.ui.swing.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.ui.swing.utils.ListenerUtils;

public class SettingsDlg extends JDialog
{
    private ButtonGroup  mGeneratorGroup;
    private List<JRadioButton> mGeneratorButtons = new ArrayList<>();
    private JCheckBox   mExtended;
    private JButton   mOK;
    private JButton   mCancel;

    private boolean   mAccepted = false;
    private String mOldGenerator;

    public SettingsDlg(JFrame frame)
    {
        super(frame, "Edit", true);
        mAccepted = false;
        initInstantiate();
        initLink();
        initLayout();
    }

    /**
     * 
     */
    private void initInstantiate()
    {        
        mGeneratorGroup = new ButtonGroup();
        mOldGenerator = (String)RuntimeLogic.getInstance().getSettings().get("generator");
        initInstantiateRadio("GEnie Known Space", "jo.ttg.gen.gni.GNIGenSchemeKnownWorld");
        initInstantiateRadio("GEnie Spinward Marches", "jo.ttg.gen.gni.GNIGenSchemeSpinward");
        initInstantiateRadio("GEnie Everything", "jo.ttg.gen.gni.GNIGenSchemeEverything");
        initInstantiateRadio("Traveller Map Known Space", "jo.ttg.gen.tm.TMGenSchemeKnownWorld");
        initInstantiateRadio("Traveller Map Spinward Marches", "jo.ttg.gen.tm.TMGenSchemeSpinward");
        initInstantiateRadio("Traveller Map Everything", "jo.ttg.gen.tm.TMGenSchemeEverything");
        initInstantiateRadio("Generated", "jo.ttg.gen.imp.ImpGenScheme");
        mExtended = new JCheckBox("Extended Generation", RuntimeLogic.getInstance().isExtended());
        mExtended.setToolTipText("Add in bases, ports, labs, and refineries");
        mOK = new JButton("OK");
        mCancel = new JButton("Cancel");
    }
    
    private void initInstantiateRadio(String title, String className)
    {
        JRadioButton radio = new JRadioButton(title);
        radio.setSelected(className.equals(mOldGenerator));
        radio.setActionCommand(className);
        mGeneratorGroup.add(radio);
        mGeneratorButtons.add(radio);
    }

    /**
     * 
     */
    private void initLink()
    {
        ListenerUtils.listen(mOK, (ev) -> doOK());
        ListenerUtils.listen(mCancel, (ev) -> doCancel());
    }

    /**
     * 
     */
    private void initLayout()
    {
        JPanel buttonBar1 = new JPanel();
        buttonBar1.add(mOK);
        buttonBar1.add(mCancel);

        JPanel client = new JPanel();
        client.setLayout(new GridLayout(mGeneratorButtons.size() + 1,1));
        for (Component c : mGeneratorButtons)
            client.add(c);
        client.add(mExtended);
        
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", client);
        getContentPane().add("South", buttonBar1);
        pack();
    }

    private void doOK()
    {
        String newGenerator = null;
        for (JRadioButton g : mGeneratorButtons)
            if (g.isSelected())
            {
                newGenerator = g.getActionCommand();
                break;
            }
        if ((newGenerator != null) && !newGenerator.equals(mOldGenerator))
        {
            try
            {
                SchemeLogic.setDefaultScheme((IGenScheme)(Class.forName(newGenerator).newInstance()));
                JOptionPane.showMessageDialog(this, "You should restart to avoid ambiguous results", "Restart Required", JOptionPane.WARNING_MESSAGE);
            }
            catch (InstantiationException | IllegalAccessException
                    | ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        RuntimeLogic.setExtended(mExtended.isSelected());
        mAccepted = true;
        dispose();
    }

    private void doCancel()
    {
        mAccepted = false;
        dispose();
    }

    /**
     * @return
     */
    public boolean isAccepted()
    {
        return mAccepted;
    }

    /**
     * @param b
     */
    public void setAccepted(boolean b)
    {
        mAccepted = b;
    }
}
