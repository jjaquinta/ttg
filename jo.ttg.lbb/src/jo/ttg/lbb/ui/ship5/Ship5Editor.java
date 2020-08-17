package jo.ttg.lbb.ui.ship5;

public class Ship5Editor
{
    private String[]    mArgs;
    
    public Ship5Editor(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        init();
        Ship5EditorFrame frame = new Ship5EditorFrame();
        frame.setVisible(true);
    }
    
    private void init()
    {
    }
    
    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
            ;
    }
    public static void main(String[] argv)
    {
        Ship5Editor app = new Ship5Editor(argv);
        app.run();
    }
}
