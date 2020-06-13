package jo.util.ui.swing.utils;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LayoutUtils
{
    public static JPanel makeBorderPanel(Object... items)
    {
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout());
        for (int i = 0; i < items.length; i += 2)
        {
            if (items[i+1] == null)
                items[i+1] = new JLabel("");
            else if (items[i+1] instanceof String)
                items[i+1] = new JLabel((String)items[i+1]);
            switch (((String)items[i]).toLowerCase())
            {
                case "north":
                case "n":
                case "top":
                case "t":
                case "^":
                    p.add("North", (Component)items[i+1]);
                    break;
                case "south":
                case "s":
                case "bottom":
                case "bot":
                case "b":
                case "v":
                    p.add("South", (Component)items[i+1]);
                    break;
                case "east":
                case "e":
                case "right":
                case "r":
                case ">":
                    p.add("East", (Component)items[i+1]);
                    break;
                case "west":
                case "w":
                case "left":
                case "l":
                case "<":
                    p.add("West", (Component)items[i+1]);
                    break;
                case "center":
                case "c":
                case "middle":
                case "mid":
                case "m":
                case ".":
                    p.add("Center", (Component)items[i+1]);
                    break;
            }
        }
        return p;
    }

    public static JPanel makeGridPanel(int rows, int cols, Object... items)
    {
        JPanel p = new JPanel();
        p.setLayout(new GridLayout(rows, cols));
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] == null)
                items[i] = new JLabel("");
            else if (items[i] instanceof String)
                items[i] = new JLabel((String)items[i]);
            p.add((Component)items[i]);
        }
        return p;
    }
}
