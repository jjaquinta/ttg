package jo.ttg.lang.ui.edit;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.LangTrigram;
import jo.ttg.lang.logic.LangLogic;
import jo.ttg.lang.logic.LangTrigramDriver;
import jo.ttg.lang.ui.ILanguageEditor;
import jo.util.ui.swing.utils.FocusUtils;
import jo.util.ui.swing.utils.ListenerUtils;

public class TrigramEditPanel extends JPanel implements ILanguageEditor
{
    private LangTrigram mLanguage;

    private JTextField mName;
    private JTextField mCode;
    private JTextArea  mText;
    private JButton    mReplace;
    private JButton    mAdd;

    public TrigramEditPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mName = new JTextField(24);
        mCode = new JTextField(3);
        mText = new JTextArea();
        mText.setLineWrap(true);
        mText.setBorder(new TitledBorder("Sample Text"));
        mReplace = new JButton("Replace Trigrams");
        mAdd = new JButton("Add Trigrams");
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout(FlowLayout.LEFT));
        header.add(new JLabel("Name:"));
        header.add(mName);
        header.add(new JLabel("Code:"));
        header.add(mCode);

        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout(FlowLayout.CENTER));
        buttonBar.add(mReplace);
        buttonBar.add(mAdd);

        setLayout(new BorderLayout());
        add("North", header);
        add("Center", mText);
        add("South", buttonBar);
    }

    private void initLink()
    {
        ListenerUtils.listen(mReplace, (ev) -> doReplace());
        ListenerUtils.listen(mAdd, (ev) -> doAppend());
        FocusUtils.focusLost(mName, (ev) -> mLanguage.setName(mName.getText()));
        FocusUtils.focusLost(mCode, (ev) -> mLanguage.setCode(mCode.getText()));
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        super.setEnabled(enabled);
        mReplace.setEnabled(enabled);
        mAdd.setEnabled(enabled);
        mText.setEnabled(enabled);
    }

    private void doReplace()
    {
        LangTrigramDriver driver = (LangTrigramDriver)LangLogic.getDriverFor(mLanguage);
        driver.replaceTrigrams(mLanguage, mText.getText().toLowerCase());
        mText.setText("");
    }

    private void doAppend()
    {
        LangTrigramDriver driver = (LangTrigramDriver)LangLogic.getDriverFor(mLanguage);
        driver.appendTrigrams(mLanguage, mText.getText().toLowerCase());
        mText.setText("");
    }

    public void doSave()
    {
    }

    public ILanguage getLanguage()
    {
        return mLanguage;
    }

    public void setLanguage(ILanguage language)
    {
        mLanguage = (LangTrigram)language;
        mName.setText(mLanguage.getName());
        mCode.setText(mLanguage.getCode());
    }
}
