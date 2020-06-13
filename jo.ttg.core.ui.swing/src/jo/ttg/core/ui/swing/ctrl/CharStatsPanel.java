/*
 * Created on Jan 29, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.beans.chr.CharBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
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
        mSkills = new JList<String>();
    }

    /**
     * 
     */
    protected void initLink()
    {
    }

    /**
     * 
     */
    protected void initLayout()
    {
        setLayout(new TableLayout());
        add("1,1", new JLabel("Name:"));
        add("2,1 fill=h", mName);
        add("1,2", new JLabel("STR:"));
        add("2,2 fill=h", mSTR);
        add("1,3", new JLabel("DEX:"));
        add("2,3 fill=h", mDEX);
        add("1,4", new JLabel("END:"));
        add("2,4 fill=h", mEND);
        add("1,5", new JLabel("INT:"));
        add("2,5 fill=h", mINT);
        add("1,6", new JLabel("EDU:"));
        add("2,6 fill=h", mEDU);
        add("1,7", new JLabel("SOC:"));
        add("2,7 fill=h", mSOC);

        add("3,1", new JLabel("Title:"));
        add("4,1 fill=h", mTitle);
        add("3,2", new JLabel("Service:"));
        add("4,2 fill=h", mService);
        add("3,3", new JLabel("Terms:"));
        add("4,3 fill=h", mTerms);
        add("3,4", new JLabel("Age:"));
        add("4,4 fill=h", mAge);
        add("3,5", new JLabel("Gender:"));
        add("4,5 fill=h", mGender);
        add("3,6", new JLabel("Money:"));
        add("4,6 fill=h", mMoney);
        add("3,7", new JLabel("Salary:"));
        add("4,7 fill=h", mSalary);

        add("1,8", new JLabel("Skills:"));
        add("2,9 fill=both", new JScrollPane(mSkills));
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
            mSkills.setListData(new String[0]);
        }
        else
        {
            int[] upp = stats.getUpp();
            mName.setText(stats.getName());
            mTitle.setText(stats.getTitle());
            mSTR.setText(String.valueOf(FormatUtils.int2upp(upp[0])));
            mDEX.setText(String.valueOf(FormatUtils.int2upp(upp[1])));
            mEND.setText(String.valueOf(FormatUtils.int2upp(upp[2])));
            mINT.setText(String.valueOf(FormatUtils.int2upp(upp[3])));
            mEDU.setText(String.valueOf(FormatUtils.int2upp(upp[4])));
            mSOC.setText(String.valueOf(FormatUtils.int2upp(upp[5])));
            mService.setText(stats.getService());
            mTerms.setText(String.valueOf(stats.getTerms()));
            mAge.setText(String.valueOf(stats.getAge()));
            mGender.setText(stats.isMale() ? "Male" : "Female");
            mMoney.setText(FormatUtils.sCurrency(stats.getMoney()));
            mSalary.setText(FormatUtils.sCurrency(stats.getSalary()));
            List<String> skills = new ArrayList<>();
            for (Iterator<String> i = stats.getSkills().keySet().iterator(); i
                    .hasNext();)
            {
                String skill = i.next();
                skill += "-" + stats.getSkill(skill);
                skills.add(skill);
            }
            mSkills.setListData(skills.toArray(new String[0]));
        }
    }
}
