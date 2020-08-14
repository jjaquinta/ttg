package jo.ttg.lang.ui.edit;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.logic.ILanguageDriver;
import jo.ttg.lang.logic.LangLogic;
import jo.ttg.lang.logic.RuntimeLogic;
import jo.ttg.lang.ui.ILanguageEditor;
import jo.ttg.lang.ui.LanguageCtrl;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.obj.StringUtils;

public class EditPanel extends JPanel
{
    private LanguageCtrl                 mLanguage;
    private JButton                      mNew;
    private JButton                      mClone;
    private JButton                      mSave;
    private JButton                      mDelete;

    private JPanel                       mClient;
    private Map<String, ILanguageEditor> mEditors = new HashMap<>();
    private ILanguageEditor              mEditor;

    public EditPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
        updateEnablement();
    }

    private void initInstantiate()
    {
        mLanguage = new LanguageCtrl();
        mNew = new JButton("New");
        mClone = new JButton("Clone");
        mSave = new JButton("Save");
        mDelete = new JButton("Delete");

        mClient = new JPanel();
        for (ILanguageDriver driver : LangLogic.getDrivers())
        {
            ILanguageEditor editor = driver.getEditor();
            mEditors.put(driver.getClass().getName(), editor);
        }
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());
        header.add(mLanguage);
        header.add(mNew);
        header.add(mClone);
        header.add(mSave);
        header.add(mDelete);

        mClient.setLayout(new CardLayout());
        for (String key : mEditors.keySet())
            mClient.add(key, (JPanel)mEditors.get(key));
        mClient.add("blank", new JPanel());

        setLayout(new BorderLayout());
        add("North", header);
        add("Center", new JScrollPane(mClient));
    }

    private void initLink()
    {
        ListenerUtils.listen(mNew, (e) -> doNew());
        ListenerUtils.listen(mClone, (e) -> doClone());
        ListenerUtils.listen(mSave, (e) -> doSave());
        ListenerUtils.listen(mDelete, (e) -> doDelete());
        RuntimeLogic.listen("selectedLanguage", (ov, nv) -> updateEnablement());
    }

    private void updateEnablement()
    {
        ILanguage lang = RuntimeLogic.getInstance().getSelectedLanguage();
        mClone.setEnabled(lang != null);
        mSave.setEnabled((lang != null) && !lang.isDefault());
        mDelete.setEnabled((lang != null) && !lang.isDefault());
        if (lang == null)
        {
            ((CardLayout)mClient.getLayout()).show(mClient, "blank");
            mEditor = null;
        }
        else
        {
            ILanguageDriver driver = LangLogic.getDriverFor(lang);
            mEditor = mEditors.get(driver.getClass().getName());
            ((CardLayout)mClient.getLayout()).show(mClient,
                    driver.getClass().getName());
            mEditor.setLanguage(lang);
            ((JPanel)mEditor).setEnabled(!lang.isDefault());
        }
    }

    private void doNew()
    {
        JComboBox<ILanguageDriver> choice = new JComboBox<ILanguageDriver>(LangLogic.getDrivers());
        JOptionPane.showMessageDialog(this, choice, "Choose Type",
                JOptionPane.QUESTION_MESSAGE);
        ILanguageDriver driver = (ILanguageDriver)choice.getSelectedItem();
        if (driver == null)
            return;
        String name = JOptionPane.showInputDialog(this, "What is the name of this language?");
        if (StringUtils.isTrivial(name))
            return;
        String code = JOptionPane.showInputDialog(this, "What is the two letter code of this language?");
        if (StringUtils.isTrivial(code))
            return;
        if ((code.length() != 2))
            return;
        ILanguage lang = driver.newLanguage(name, code, null);
        LangLogic.addLanguage(lang);
        RuntimeLogic.setSelectedLanguage(lang);
    }

    private void doClone()
    {
        ILanguage oldLang = RuntimeLogic.getInstance().getSelectedLanguage();
        ILanguageDriver driver = LangLogic.getDriverFor(oldLang);
        if (driver == null)
            return;
        String name = JOptionPane.showInputDialog(this, "What is the name of this language?");
        if (StringUtils.isTrivial(name))
            return;
        String code = JOptionPane.showInputDialog(this, "What is the two letter code of this language?");
        if (StringUtils.isTrivial(code))
            return;
        if ((code.length() != 2))
            return;
        ILanguage lang = driver.newLanguage(name, code, oldLang);
        LangLogic.addLanguage(lang);
        RuntimeLogic.setSelectedLanguage(lang);
    }

    private void doSave()
    {
        mEditor.doSave();
        RuntimeLogic.save();
    }

    private void doDelete()
    {
        ILanguage oldLang = RuntimeLogic.getInstance().getSelectedLanguage();
        boolean proceed = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete "+oldLang.getName()+"?", "Delete "+oldLang.getName(), 
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION;
        if (!proceed)
            return;
        LangLogic.removeLanguage(oldLang);
    }
}
