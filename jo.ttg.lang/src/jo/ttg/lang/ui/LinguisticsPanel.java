package jo.ttg.lang.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jo.ttg.lang.ui.cipher.CipherPanel;
import jo.ttg.lang.ui.word.WordPanel;

public class LinguisticsPanel extends JPanel
{
    private JTabbedPane mClient;
    private WordPanel    mWord;
    private CipherPanel mCipher;

    public LinguisticsPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mClient = new JTabbedPane();
        mWord = new WordPanel();
        mCipher = new CipherPanel();
    }

    private void initLayout()
    {
        mClient.addTab("Word", mWord);
        mClient.addTab("Cipher", mCipher);
        setLayout(new BorderLayout());
        add("Center", mClient);
    }

    private void initLink()
    {
    }
}
