package jo.ttg.lang.ui.edit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import jo.util.ui.swing.TableLayout;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;

public class FrequencyPanel extends JPanel
        implements ChangeListener, ActionListener
{
    private JLabel[]             mTitleCtrls;
    private JSpinner[]           mValueCtrls;
    private JButton[]            mDeleteCtrls;
    private JButton              mAddCtrl;

    private Map<String, Integer> mValues;
    private String[]             mValidValues;

    public FrequencyPanel()
    {
    }

    private void reconstruct()
    {
        teardown();
        if ((mValues == null) || (mValues.size() == 0))
            return;
        initInstantiate();
        initLink();
        initLayout();
    }

    protected void teardown()
    {
        if (mValueCtrls != null)
            for (int i = 0; i < mValueCtrls.length; i++)
                mValueCtrls[i].removeChangeListener(this);
        if (mDeleteCtrls != null)
            for (int i = 0; i < mDeleteCtrls.length; i++)
                mDeleteCtrls[i].removeActionListener(this);
        if (mAddCtrl != null)
            mAddCtrl.removeActionListener(this);
        removeAll();
    }

    private void initInstantiate()
    {
        String[] keys = mValues.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        mDeleteCtrls = new JButton[keys.length];
        mTitleCtrls = new JLabel[keys.length];
        mValueCtrls = new JSpinner[keys.length];
        for (int i = 0; i < keys.length; i++)
        {
            mDeleteCtrls[i] = new JButton("-");
            mDeleteCtrls[i].setName(keys[i]);
            mTitleCtrls[i] = new JLabel(keys[i]);
            mValueCtrls[i] = new JSpinner(new SpinnerNumberModel(
                    mValues.get(keys[i]).intValue(), 0, 216, 1));
            mValueCtrls[i].setName(keys[i]);
        }
        mAddCtrl = new JButton("+");
        updateEnablement();
    }

    private void initLayout()
    {
        setLayout(new TableLayout());
        for (int i = 0; i < mTitleCtrls.length; i++)
        {
            add("1,+", mDeleteCtrls[i]);
            add("+,.", mTitleCtrls[i]);
            add("+,. fill=h", mValueCtrls[i]);
        }
        add("1,+", mAddCtrl);
    }

    private void initLink()
    {
        for (int i = 0; i < mValueCtrls.length; i++)
            mValueCtrls[i].addChangeListener(this);
        for (int i = 0; i < mValueCtrls.length; i++)
            mDeleteCtrls[i].addActionListener(this);
        mAddCtrl.addActionListener(this);
    }
    
    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        updateEnablement();
    }

    @Override
    public void stateChanged(ChangeEvent e)
    {
        JSpinner ctrl = (JSpinner)e.getSource();
        String name = ctrl.getName();
        mValues.put(name, IntegerUtils.parseInt(ctrl.getValue()));
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton ctrl = (JButton)e.getSource();
        String name = ctrl.getName();
        if (StringUtils.isTrivial(name))
            doAdd();
        else
        {
            mValues.remove(name);
            reconstruct();
        }
    }
    
    private void doAdd()
    {
        String letter = null;
        int val = 1;
        if (mValidValues == null)
        {
            letter = JOptionPane.showInputDialog(this, "Enter in the letter to add:");
            int v = StringUtils.digitize(letter);
            if (v > 0)
            {
                val = v;
                while ((letter.length() > 0) && Character.isDigit(letter.charAt(letter.length() - 1)))
                    letter = letter.substring(0, letter.length() - 1);
            }
        }
        else
        {
            List<String> candidates = new ArrayList<>();
            for (String l : mValidValues)
                if (!mValues.containsKey(l))
                    candidates.add(l);
            if (candidates.size() == 0)
                return;
            JComboBox<String> choice = new JComboBox<>(candidates.toArray(new String[0]));
            JOptionPane.showMessageDialog(this, choice, "Choose Value",
                    JOptionPane.QUESTION_MESSAGE);
            letter = (String)choice.getSelectedItem();
        }
        if (StringUtils.isTrivial(letter))
            return;
        letter = letter.toLowerCase();
        if (mValues.containsKey(letter))
            return;
        mValues.put(letter, val);
        reconstruct();
    }

    private void updateEnablement()
    {
        boolean enabled = isEnabled();
        for (JSpinner ctrl: mValueCtrls)
            ctrl.setEnabled(enabled);
        for (JButton ctrl: mDeleteCtrls)
            ctrl.setEnabled(enabled);
        mAddCtrl.setEnabled(enabled);
    }
    
    public Map<String, Integer> getValues()
    {
        return mValues;
    }

    public void setValues(Map<String, Integer> values)
    {
        mValues = values;
        reconstruct();
    }

    public String[] getValidValues()
    {
        return mValidValues;
    }

    public void setValidValues(String[] validValues)
    {
        mValidValues = validValues;
    }

}
