package jo.util.table.names.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.util.table.names.logic.NameLogic;

public class NamePanel extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = -521223734338105595L;
    
    private JComboBox<String>   mCategory;
    private JTextArea   mNames;
    private JCheckBox   mMale;
    private JCheckBox   mFemale;
    private JCheckBox   mSurname;
    private JButton     mMore;

    public NamePanel()
    {
        initInstantiate();
        initLayout();
        initLink();
        doNames();
    }
    
    private void initInstantiate()
    {
        mCategory = new JComboBox<String>(NameLogic.CATEGORIES);
        mNames = new JTextArea();
        mNames.setEditable(false);
        mNames.setWrapStyleWord(true);
        mNames.setLineWrap(true);
        mMale = new JCheckBox("Male");
        mFemale = new JCheckBox("Female");
        mSurname = new JCheckBox("Surname");
        mMore = new JButton("More");
    }
    
    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("North", mCategory);
        add("Center", mNames);
        JPanel buttonBar = new JPanel();
        add("South", buttonBar);
        buttonBar.setLayout(new FlowLayout());
        buttonBar.add(mMale);
        buttonBar.add(mFemale);
        buttonBar.add(mSurname);
        buttonBar.add(mMore);
    }
    
    private void initLink()
    {
        ActionListener doNames = new ActionListener(){
            public void actionPerformed(ActionEvent e)
            {
                doNames();
            }
        };
        mCategory.addActionListener(doNames);
        mMale.addActionListener(doNames);
        mFemale.addActionListener(doNames);
        mSurname.addActionListener(doNames);
        mMore.addActionListener(doNames);
    }

    private void doNames()
    {
        String nationality = (String)mCategory.getSelectedItem();
        if (nationality == null)
            nationality = NameLogic.CATEGORY_ANY;
        String mode;
        if (mMale.isSelected())
            if (mFemale.isSelected())
                if (mSurname.isSelected())
                    mode = NameLogic.MODE_MIXED_FULL;
                else
                    mode = NameLogic.MODE_MIXED_FIRST;
            else
                if (mSurname.isSelected())
                    mode = NameLogic.MODE_MALE_FULL;
                else
                    mode = NameLogic.MODE_MALE_FIRST;
        else
            if (mFemale.isSelected())
                if (mSurname.isSelected())
                    mode = NameLogic.MODE_FEMALE_FULL;
                else
                    mode = NameLogic.MODE_FEMALE_FIRST;
            else
                if (mSurname.isSelected())
                    mode = NameLogic.MODE_MIXED_FULL;
                else
                    mode = NameLogic.MODE_MIXED_FIRST;
        String[] names = NameLogic.generatePersonalNames(nationality, mode, 100);
        StringBuffer txt = new StringBuffer();
        for (int i = 0; i < names.length; i++)
        {
            if (i > 0)
                txt.append(", ");
            txt.append(names[i]);
        }
        mNames.setText(txt.toString());
    }
}
