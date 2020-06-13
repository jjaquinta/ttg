/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.chr;

import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.utils.DisplayUtils;
import jo.util.ui.swing.TableLayout;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CharStatsPanel extends JPanel
{
    private CharBean      mChar;

    private JLabel        mName;
    private JLabel        mTitle;
    private JLabel        mSTR;
    private JLabel        mDEX;
    private JLabel        mEND;
    private JLabel        mINT;
    private JLabel        mEDU;
    private JLabel        mSOC;
    private JLabel        mService;
    private JLabel        mTerms;
    private JLabel        mAge;
    private JLabel        mGender;
    private JLabel        mMoney;
    private JLabel        mSalary;
    private JList<String> mSkills;

    public CharStatsPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    /**
     * 
     */
    protected void initInstantiate()
    {
        mName = new JLabel();
        mTitle = new JLabel();
        mSTR = new JLabel();
        mDEX = new JLabel();
        mEND = new JLabel();
        mINT = new JLabel();
        mEDU = new JLabel();
        mSOC = new JLabel();
        mService = new JLabel();
        mTerms = new JLabel();
        mAge = new JLabel();
        mGender = new JLabel();
        mMoney = new JLabel();
        mSalary = new JLabel();
        mSkills = new JList<String>(new DefaultListModel<String>());
    }

    /**
     * 
     */
    protected void initLayout()
    {
        setLayout(new TableLayout());
        add("1,+ anchor=w", new JLabel("Name:"));
        add("+,. fill=h", mName);
        add("+,. anchor=w", new JLabel("Title:"));
        add("+,. fill=h", mTitle);
        add("1,+ anchor=w", new JLabel("STR:"));
        add("+,. fill=h", mSTR);
        add("+,. anchor=w", new JLabel("Service:"));
        add("+,. fill=h", mService);
        add("1,+ anchor=w", new JLabel("DEX:"));
        add("+,. fill=h", mDEX);
        add("+,. anchor=w", new JLabel("Terms:"));
        add("+,. fill=h", mTerms);
        add("1,+ anchor=w", new JLabel("END:"));
        add("+,. fill=h", mEND);
        add("+,. anchor=w", new JLabel("Age:"));
        add("+,. fill=h", mAge);
        add("1,+ anchor=w", new JLabel("INT:"));
        add("+,. fill=h", mINT);
        add("+,. anchor=w", new JLabel("Gender:"));
        add("+,. fill=h", mGender);
        add("1,+ anchor=w", new JLabel("EDU:"));
        add("+,. fill=h", mEDU);
        add("+,. anchor=w", new JLabel("Money:"));
        add("+,. fill=h", mMoney);
        add("1,+ anchor=w", new JLabel("SOC:"));
        add("+,. fill=h", mSOC);
        add("+,. anchor=w", new JLabel("Salary:"));
        add("+,. fill=h", mSalary);

        add("1,+ 4x1 anchor=nw", new JLabel("Skills:"));
        add("1,+ 4x1 fill=hv", new JScrollPane(mSkills));
    }

    /**
     * 
     */
    protected void initLink()
    {
    }

    /**
     * @return
     */
    public CharBean getChar()
    {
        return mChar;
    }

    /**
     * @param stats
     */
    public void setChar(CharBean stats)
    {
        mChar = stats;
        if (mChar == null)
        {
            mName.setText("");
            mTitle.setText("");
            mSTR.setText("");
            mDEX.setText("");
            mEND.setText("");
            mINT.setText("");
            mEDU.setText("");
            mSOC.setText("");
            mService.setText("");
            mTerms.setText("");
            mAge.setText("");
            mGender.setText("");
            mMoney.setText("");
            mSalary.setText("");
            mSkills.removeAll();
        }
        else
        {
            int[] upp = stats.getUpp();
            mName.setText(stats.getName());
            mTitle.setText(stats.getTitle());
            mSTR.setText(String.valueOf(DisplayUtils.int2upp(upp[0])));
            mDEX.setText(String.valueOf(DisplayUtils.int2upp(upp[1])));
            mEND.setText(String.valueOf(DisplayUtils.int2upp(upp[2])));
            mINT.setText(String.valueOf(DisplayUtils.int2upp(upp[3])));
            mEDU.setText(String.valueOf(DisplayUtils.int2upp(upp[4])));
            mSOC.setText(String.valueOf(DisplayUtils.int2upp(upp[5])));
            mService.setText(stats.getService());
            mTerms.setText(String.valueOf(stats.getTerms()));
            mAge.setText(String.valueOf(stats.getAge()));
            mGender.setText(stats.isMale() ? "Male" : "Female");
            mMoney.setText(DisplayUtils.formatCurrency(stats.getMoney()));
            mSalary.setText(DisplayUtils.formatCurrency(stats.getSalary()));
            mSkills.removeAll();
            for (String skill : stats.getSkills().keySet())
            {
                skill += "-" + stats.getSkill(skill);
                ((DefaultListModel<String>)mSkills.getModel())
                        .addElement(skill);
            }
        }
    }
}
