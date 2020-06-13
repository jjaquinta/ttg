package jo.ttg.gen.sw.ui;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;
import org.json.simple.parser.ParseException;

import jo.util.ui.swing.utils.ListenerUtils;

public class JSONEditDlg extends JDialog
{
    private JSONObject mJSON;
    private JTextArea mClient;
    private JButton   mOK;
    private JButton   mCancel;

    private boolean   mAccepted = false;

    public JSONEditDlg(JFrame frame, JSONObject json)
    {
        super(frame, "Edit", true);
        mJSON = json;
        mAccepted = false;
        initInstantiate();
        initLink();
        initLayout();
    }

    /**
     * 
     */
    private void initInstantiate()
    {
        mClient = new JTextArea(JSONUtils.toFormattedString(mJSON));
        Font oldFont = mClient.getFont();
        Font newFont = new Font(Font.MONOSPACED, oldFont.getStyle(), oldFont.getSize());
        mClient.setFont(newFont);
        mOK = new JButton("OK");
        mCancel = new JButton("Cancel");
    }

    /**
     * 
     */
    private void initLink()
    {
        ListenerUtils.listen(mOK, (ev) -> doOK());
        ListenerUtils.listen(mCancel, (ev) -> doCancel());
        ListenerUtils.keyTyped(mClient, (ev) -> doCheckText());
    }

    /**
     * 
     */
    private void initLayout()
    {
        JPanel buttonBar1 = new JPanel();
        buttonBar1.add(mOK);
        buttonBar1.add(mCancel);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", new JScrollPane(mClient));
        getContentPane().add("South", buttonBar1);
        setSize(512, 720);
    }

    private void doCheckText()
    {
        try
        {
            mJSON = (JSONObject)JSONUtils.PARSER.parse(mClient.getText());
            mOK.setEnabled(true);
        }
        catch (ParseException e)
        {
            mOK.setEnabled(false);
        }
    }

    private void doOK()
    {
        doCheckText();
        mAccepted = true;
        dispose();
    }

    private void doCancel()
    {
        mAccepted = false;
        dispose();
    }

    /**
     * @return
     */
    public boolean isAccepted()
    {
        return mAccepted;
    }

    /**
     * @param b
     */
    public void setAccepted(boolean b)
    {
        mAccepted = b;
    }

    public JSONObject getJSON()
    {
        return mJSON;
    }

    public void setJSON(JSONObject jSON)
    {
        mJSON = jSON;
        if (mClient != null)
            mClient.setText(JSONUtils.toFormattedString(mJSON));
    }

}
