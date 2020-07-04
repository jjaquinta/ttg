package jo.ttg.launcher;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.StringUtils;

public class Launcher implements ActionListener
{
    private String[] mArgs;
    private JSONObject mConfig;
    
    public Launcher(String[] args)
    {
        mArgs = args;
    }
    
    public void run()
    {
        parseArgs();
        loadConfig();
        displayUI();
    }
    
    private void displayUI()
    {
        JFrame frame = new JFrame(mConfig.getString("title"));
        JSONArray options = (JSONArray)mConfig.get("options");
        frame.setLayout(new GridLayout(options.size(), 1));
        for (int i = 0; i < options.size(); i++)
        {
            JSONObject option = (JSONObject)options.get(i);
            String title = option.getString("title");
            String hover = option.getString("hover");
            String main = option.getString("main");
            String icon = option.getString("icon");
            JButton b = new JButton(title);
            b.setToolTipText(hover);
            if (!StringUtils.isTrivial(icon))
                b.setIcon(TTGIconUtils.getIcon(icon));
            b.setActionCommand(main);
            b.addActionListener(this);
            frame.add(b);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        try
        {
            String main = e.getActionCommand();
            Class<?> mainClass = Class.forName(main);
            Method m = mainClass.getMethod("main", String[].class);
            m.invoke(null, (Object)mArgs);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private void loadConfig()
    {
        if (mConfig != null)
            return;
        File home = new File(System.getProperty("user.home"));
        File homeConfig = new File(home, "ttg_launcher.json");
        if (tryToLoad(homeConfig))
            return;
        File ttgDir= new File(home, ".ttg");
        File ttgConfig = new File(ttgDir, "ttg_launcher.json");
        if (tryToLoad(ttgConfig))
            return;
        try
        {
            String json = ResourceUtils.loadSystemResourceString("ttg_launcher.json", Launcher.class);
            mConfig = JSONUtils.readJSONString(json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private void parseArgs()
    {
        for (int i = 0; i < mArgs.length; i++)
            if ("--config".equals(mArgs[i]))
            {
                File f = new File(mArgs[++i]);
                tryToLoad(f);
            }
    }
    
    private boolean tryToLoad(File f)
    {
        if (!f.exists())
            return false;
        try
        {
            mConfig = JSONUtils.readJSON(f);
            return true;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }
    
    public static void main(String[] argv)
    {
        Launcher app = new Launcher(argv);
        app.run();
    }
}
