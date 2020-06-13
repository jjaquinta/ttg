package jo.ttg.test.sys.twod;

import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.ttg.core.ui.swing.ship.sys.twod.System2DViewer;
import jo.ttg.core.ui.swing.ship.sys.twod.TacticalSystem2DHandler;
import jo.ttg.gen.IGenScheme;
import jo.ttg.gen.gni.GNIGenScheme;

public class TestViewer extends JFrame implements TTGActionListener
{
    private System2DViewer mClient;

    public TestViewer()
    {
        super("Tactical viewer");
        initInstantiate();
        initLink();
        initLayout();        
    }
    
    private void initInstantiate()
    {
        mClient = new System2DViewer();
        IGenScheme scheme = GNIGenScheme.newInstanceSpinward();
        SystemBean sys = scheme.getGeneratorSystem().generateSystem(GNIGenScheme.LUNION);
        mClient.setSystem(sys);
        mClient.setHandler(new TacticalSystem2DHandler());
        mClient.setBackground(Color.BLACK);
    }

    private void initLayout()
    {
        getContentPane().add("Center", mClient);
        setSize(1024, 768);
    }
    
    private void initLink()
    {
        addWindowListener(new WindowAdapter()
          { public void windowClosing(WindowEvent e) { doFrameShut(); }             
            public void windowOpened(WindowEvent e) { doFrameStart(); }
          }
        );
        mClient.addTTGActionListener(this);
    }       
    
    private void doFrameShut()
    {
        setVisible(false);
        System.exit(0);
    }
    
    private void doFrameStart()
    {
    }
    
    public static void main(String[] argv)
    {
        TestViewer f = new TestViewer();
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(TTGActionEvent e)
    {
        // TODO Auto-generated method stub
        
    }

}
