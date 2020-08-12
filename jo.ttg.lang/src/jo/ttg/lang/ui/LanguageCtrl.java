package jo.ttg.lang.ui;

import java.awt.BorderLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.logic.RuntimeLogic;
import jo.util.ui.swing.utils.ListenerUtils;

public class LanguageCtrl extends JPanel
{
    private DefaultComboBoxModel<ILanguage> mModel;
    private JComboBox<ILanguage>            mCombo;

    public LanguageCtrl()
    {
        initInstantiate();
        updateLanguages();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mModel = new DefaultComboBoxModel<>();
        mCombo = new JComboBox<>(mModel);
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mCombo);
    }

    private void initLink()
    {
        ListenerUtils.listen(mCombo, (ev) -> RuntimeLogic.setSelectedLanguage((ILanguage)mCombo.getSelectedItem()));
        RuntimeLogic.listen("selectedLanguage", (ov,nv) -> {if (mCombo.getSelectedItem() != nv) mCombo.setSelectedItem(nv);});
        RuntimeLogic.listen("languages", (ov,nv) -> updateLanguages());
    }

    private void updateLanguages()
    {
        mModel.removeAllElements();
        for (ILanguage lang : RuntimeLogic.getInstance().getLanguages())
            mModel.addElement(lang);
        mCombo.setSelectedItem(RuntimeLogic.getInstance().getSelectedLanguage());
    }
}
