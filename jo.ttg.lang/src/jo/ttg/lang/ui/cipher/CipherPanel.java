package jo.ttg.lang.ui.cipher;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.logic.LangLogic;
import jo.ttg.lang.logic.RuntimeLogic;
import jo.ttg.lang.ui.LanguageCtrl;
import jo.util.ui.swing.utils.ListenerUtils;

public class CipherPanel extends JPanel
{
    private LanguageCtrl mLanguage;
    private JButton      mGenerate;
    private JTextArea    mInputText;
    private JTextArea    mOutputText;

    public CipherPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mLanguage = new LanguageCtrl();
        mGenerate = new JButton("Generate");
        mInputText = new JTextArea();
        mInputText.setLineWrap(true);
        mInputText.setEditable(true);
        mOutputText = new JTextArea();
        mOutputText.setLineWrap(true);
        mOutputText.setEditable(false);
    }

    private void initLayout()
    {
        JPanel header = new JPanel();
        header.setLayout(new FlowLayout());
        header.add(mLanguage);
        header.add(mGenerate);
        
        JPanel top = new JPanel();
        top.setLayout(new BorderLayout());
        top.add("North", new JLabel("Input:"));
        top.add("Center", new JScrollPane(mInputText));
        
        JPanel bot = new JPanel();
        bot.setLayout(new BorderLayout());
        bot.add("North", new JLabel("Output:"));
        bot.add("Center", new JScrollPane(mOutputText));
        
        JPanel client = new JPanel();
        client.setLayout(new GridLayout(2, 1));
        client.add(top);
        client.add(bot);
        
        setLayout(new BorderLayout());
        add("North", header);
        add("Center", client);
        
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
        StringBuffer inbuf = new StringBuffer(mInputText.getText());
        StringBuffer outbuf = new StringBuffer();
        while (inbuf.length() > 0)
        {
            String inWord = getNextWord(inbuf);
            if (!Character.isLetter(inWord.charAt(0)))
                outbuf.append(inWord);
            else
            {
                Random rnd = new Random(inWord.hashCode());
                String outWord = LangLogic.generateWord(lang, rnd);
                if (isInitialCaps(inWord))
                    outWord = makeInitialCaps(outWord);
                else if (isAllCaps(inWord))
                    outWord = makeAllCaps(outWord);
                outbuf.append(outWord);
            }
        }
        mOutputText.setText(outbuf.toString().trim());
    }

    private boolean isInitialCaps(String inWord)
    {
        return Character.isUpperCase(inWord.charAt(0));
    }

    private String makeInitialCaps(String outWord)
    {
        return Character.toUpperCase(outWord.charAt(0)) + outWord.substring(1);
    }

    private boolean isAllCaps(String inWord)
    {
        for (char ch : inWord.toCharArray())
            if (!Character.isUpperCase(ch))
                return false;
        return true;
    }

    private String makeAllCaps(String outWord)
    {
        StringBuffer ret = new StringBuffer();
        for (char ch : outWord.toCharArray())
            ret.append(Character.toUpperCase(ch));
        return ret.toString();
    }

    private String getNextWord(StringBuffer inbuf)
    {
        StringBuffer word = new StringBuffer();
        char ch1 = inbuf.charAt(0);
        inbuf.delete(0, 1);
        word.append(ch1);
        for (;;)
        {
            if (inbuf.length() == 0)
                break;
            char ch2 = inbuf.charAt(0);
            if (Character.isLetter(ch1) != Character.isLetter(ch2))
                break;
            inbuf.delete(0, 1);
            word.append(ch2);
        }
        return word.toString();
    }
}
