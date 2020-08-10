package jo.util.table.names.ui;

import java.awt.BorderLayout;

import javax.swing.JApplet;

public class NameApplet extends JApplet
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = -8936479430284444620L;
    
    private NamePanel mClient;
    
    public NameApplet()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    

    private void initLayout()
    {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", mClient);
    }

    private void initLink()
    {
    }

    private void initInstantiate()
    {
        mClient = new NamePanel();
    }

}
