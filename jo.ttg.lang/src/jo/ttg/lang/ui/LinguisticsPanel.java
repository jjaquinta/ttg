package jo.ttg.lang.ui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import jo.ttg.lang.ui.cipher.CipherPanel;
import jo.ttg.lang.ui.edit.EditPanel;
import jo.ttg.lang.ui.word.WordPanel;

public class LinguisticsPanel extends JPanel
{
    private JTabbedPane mClient;
    private WordPanel   mWord;
    private CipherPanel mCipher;
    private EditPanel   mEditor;

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
        mEditor = new EditPanel();
    }

    private void initLayout()
    {
        mClient.addTab("Word", mWord);
        mClient.addTab("Cipher", mCipher);
        mClient.addTab("Editor", mEditor);
        setLayout(new BorderLayout());
        add("Center", mClient);
    }

    private void initLink()
    {
    }
}
