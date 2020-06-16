package jo.ttg.gen.imp.ui.swing.ui;

import jo.ttg.gen.imp.ui.swing.logic.RuntimeLogic;

public class IMP
{
    private String[]    mArgs;
    
    public IMP(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        init();
        IMPFrame frame = new IMPFrame();
        frame.setVisible(true);
    }
    
    private void init()
    {
        RuntimeLogic.init();
    }
    
    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
            ;
    }
    public static void main(String[] argv)
    {
        IMP app = new IMP(argv);
        app.run();
    }
}
