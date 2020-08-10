package jo.util.table.names.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class NameFrame extends JFrame 
{
    /**
     * 
     */
    private static final long serialVersionUID = 7703781614422379500L;
    
    private NamePanel mClient;

    public NameFrame()
    {
        super("Game Names");
        initInstantiate();
        initLayout();
        initLink();
    }

    private void initLayout()
    {
        setLayout(new BorderLayout());
        add("Center", mClient);
        setSize(400, 640);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent e)
            {
                System.exit(0);
            }            
        });
    }

    private void initLink()
    {
    }

    private void initInstantiate()
    {
        mClient = new NamePanel();
    }
    
    public static void main(String[] argv)
    {
        NameFrame app = new NameFrame();
        app.setVisible(true);
    }
}
