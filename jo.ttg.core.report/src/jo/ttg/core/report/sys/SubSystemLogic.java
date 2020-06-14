package jo.ttg.core.report.sys;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;

public class SubSystemLogic
{
    
    public static List<SubSystem> getSubSystems(SystemBean sys, int maxWidth)
    {
        List<SubSystem> subs = new ArrayList<SubSystem>();
        addSubSystem(subs, sys.getSystemRoot(), maxWidth);        
        return subs;
    }

    private static void addSubSystem(List<SubSystem> subs, BodyBean root, int maxWidth)
    {
        SubSystem sub = new SubSystem();
        sub.setRoot(root);
        sub.getContents().addAll(root.getAllSatelites());
        subs.add(sub);
        for (;;)
        {
            int w = getOrbitalWidth(root, sub);
            if (w < maxWidth)
                break;
            BodyBean widest = findWidest(root, sub);
            if (widest == null)
                break;
            sub.getContents().removeAll(widest.getAllSatelites());
            sub.getContents().add(widest); // add root back in
            addSubSystem(subs, widest, maxWidth);
        }
    }
    
    private static BodyBean findWidest(BodyBean base, SubSystem sub)
    {
        int v = -1;
        BodyBean best = null;
        for (BodyBean child : base.getSatelites())
        {
            int w = getOrbitalWidth(child, sub);
            if (w == 1)
                continue;
            if ((best == null) || (w > v))
            {
                best = child;
                v = w;
            }
        }
        return best;
    }
    
    public static int getOrbitalWidth(BodyBean b, SubSystem sub)
    {
        int width = 1;
        BodyBean[] sats = b.getSatelites();
        for (BodyBean child : sats)
            if (sub.getContents().contains(child))
                width += getOrbitalWidth(child, sub);
        if (width > 1)
            width += 2;
        return width;
    }
}
