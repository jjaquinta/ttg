package jo.ttg.lang.ui.word;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.logic.LangLogic;
import jo.ttg.lang.logic.RuntimeLogic;
import jo.ttg.lang.ui.LanguageCtrl;
import jo.util.ui.swing.utils.ListenerUtils;

public class WordPanel extends JPanel
{
    private LanguageCtrl mLanguage;
    private JButton      mGenerate;
    private JTextArea    mText;
    
    public WordPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mLanguage = new LanguageCtrl();
        mGenerate = new JButton("Generate");
        mText = new JTextArea();
        mText.setLineWrap(true);
        mText.setEditable(false);
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());
        header.add(mLanguage);
        header.add(mGenerate);
        
        setLayout(new BorderLayout());
        add("North", header);
        add("Center", new JScrollPane(mText));
    }

    private void initLink()
    {
        ListenerUtils.listen(mGenerate, (e) -> doGenerate());
    }
    
    private void doGenerate()
    {
        ILanguage lang = RuntimeLogic.getInstance().getSelectedLanguage();
        if (lang == null)
            return;
        StringBuffer text = new StringBuffer();
        while (text.length() < 2000)
        {
            String word = LangLogic.generateWord(lang, null);
            text.append(" ");
            text.append(word);
        }
        mText.setText(text.toString().trim());
    }
}
