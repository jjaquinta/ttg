package jo.ttg.gen.sw.ui;

import jo.ttg.gen.sw.logic.RuntimeLogic;

public class SW
{
    private String[]    mArgs;
    
    public SW(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        init();
        SWFrame frame = new SWFrame();
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
        SW app = new SW(argv);
        app.run();
    }
}
