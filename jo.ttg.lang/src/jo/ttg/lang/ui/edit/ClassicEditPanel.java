package jo.ttg.lang.ui.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.LangClassic;
import jo.ttg.lang.ui.ILanguageEditor;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.FocusUtils;

public class ClassicEditPanel extends JPanel implements ILanguageEditor
{
    private LangClassic   mLanguage;

    private JTextField      mName;
    private JTextField      mCode;
    private SyllablePanel   mInitialSyllable;
    private FrequencyPanel  mWordLength;
    private SyllablePanel   mFinalSyllable;
    private FrequencyPanel  mFirstConsonants;
    private FrequencyPanel  mVowels;
    private FrequencyPanel  mLastConsonants;
    
    public ClassicEditPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mName = new JTextField(24);
        mCode = new JTextField(3);
        mInitialSyllable = new SyllablePanel();
        mInitialSyllable.setBorder(new TitledBorder("Initial Syllable"));
        mWordLength = new FrequencyPanel();
        mWordLength.setBorder(new TitledBorder("Word Length"));
        mFinalSyllable = new SyllablePanel();
        mFinalSyllable.setBorder(new TitledBorder("Final Syllable"));
        mFirstConsonants = new FrequencyPanel();
        mFirstConsonants.setBorder(new TitledBorder("Initial Consonants"));
        mVowels = new FrequencyPanel();
        mVowels.setBorder(new TitledBorder("Vowels"));
        mLastConsonants = new FrequencyPanel();
        mLastConsonants.setBorder(new TitledBorder("Final Consonants"));
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Name:"));
        header.add(mName);
        header.add(new JLabel("Code:"));
        header.add(mCode);
        
        JPanel client = new JPanel();
        client.setLayout(new TableLayout("anchor=n fill=hv"));
        client.add("1.+ weighty=1", mInitialSyllable);
        client.add("+,. weighty=1", mWordLength);
        client.add("+,. weighty=1", mFinalSyllable);
        client.add("1.+ weighty=3", mFirstConsonants);
        client.add("+,. weighty=3", mVowels);
        client.add("+,. weighty=3", mLastConsonants);
        
        setLayout(new BorderLayout());
        add("North", header);
        add("Center", client);
    }

    private void initLink()
    {
        FocusUtils.focusLost(mName, (ev) -> mLanguage.setName(mName.getText()));
        FocusUtils.focusLost(mCode, (ev) -> mLanguage.setCode(mCode.getText()));
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        mName.setEditable(enabled);
        mCode.setEditable(enabled);
        mInitialSyllable.setEnabled(enabled);
        mWordLength.setEnabled(enabled);
        mFinalSyllable.setEnabled(enabled);
        mFirstConsonants.setEnabled(enabled);
        mVowels.setEnabled(enabled);
        mLastConsonants.setEnabled(enabled);
    }
    
    public ILanguage getLanguage()
    {
        return mLanguage;
    }

    public void setLanguage(ILanguage lang)
    {
        mLanguage = (LangClassic)lang;
        mName.setText(mLanguage.getName());
        mCode.setText(mLanguage.getCode());
        mInitialSyllable.setValues(mLanguage.getInitialSyllableTable());
        mFinalSyllable.setValues(mLanguage.getFinalSyllableTable());
        mWordLength.setValues(mLanguage.getWordLength());
        mWordLength.setValidValues(new String[] { "1", "2", "3", "4", "5", "6", "7", "8" });
        mFirstConsonants.setValues(mLanguage.getFirstConsonant());
        mLastConsonants.setValues(mLanguage.getLastConsonant());
        mVowels.setValues(mLanguage.getVowel());
        revalidate();
    }
    
    public void doSave()
    {        
    }
}
