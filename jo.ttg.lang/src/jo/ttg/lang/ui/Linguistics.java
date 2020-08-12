package jo.ttg.lang.ui;

import jo.ttg.lang.logic.RuntimeLogic;

public class Linguistics
{
    private String[]    mArgs;
    
    public Linguistics(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        init();
        LinguisticsFrame frame = new LinguisticsFrame();
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
        Linguistics app = new Linguistics(argv);
        app.run();
    }
}
