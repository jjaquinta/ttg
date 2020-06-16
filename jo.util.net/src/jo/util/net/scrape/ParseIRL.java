package jo.util.net.scrape;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.io.FileUtils;

public class ParseIRL
{
    private File mDir;
    private Map<String, Integer> mTitles = new HashMap<>();
    
    public ParseIRL()
    {
        mDir = new File("d:\\temp\\data\\irl_schools");
    }
    
    public void run()
    {
        try
        {
            File[] files = mDir.listFiles();
            for (File f: files)
                if (f.getName().endsWith(".txt"))
                    parseReport(f);
            List<String> titles = getSortedTitles();
            for (String title : titles)
                System.out.println(mTitles.get(title)+",\""+title+"\"");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<String> getSortedTitles()
    {
        List<String> titles = new ArrayList<>();
        for (String title : mTitles.keySet())
            if (mTitles.get(title) > 1)
                titles.add(title);
        Collections.sort(titles, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2)
            {
                int i1 = mTitles.get(o1);
                int i2 = mTitles.get(o2);
                return i2 - i1;
            }
        });
        return titles;
    }
    
    private int isSection(String line)
    {
        for (int i = 0; i < line.length(); i++)
        {
            char ch = line.charAt(i);
            if (Character.isDigit(ch))
                continue;
            else if (ch == '.')
                continue;
            else if ((ch == ' ') && (i > 0))
                return i;
            else
                return -1;
        }
        return -1;
    }
    
    private void parseReport(File f) throws IOException
    {
        //System.out.println(f.getName());
        String txt = FileUtils.readFileAsString(f.toString(), "utf-8");
        for (StringTokenizer st = new StringTokenizer(txt, "\r\n"); st.hasMoreTokens(); )
        {
            String line = st.nextToken();
            int o = isSection(line);
            if (o < 0)
                continue;
            String title = line.substring(o).trim();
            if (mTitles.containsKey(title))
                mTitles.put(title, mTitles.get(title) + 1);
            else
                mTitles.put(title, 1);
        }
    }
    
    public static void main(String[] argv) throws IOException
    {
        ParseIRL app = new ParseIRL();
        app.run();
    }
}
