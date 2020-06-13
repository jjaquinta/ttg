package jo.util.jgl.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import javax.vecmath.Point3f;

import jo.util.jgl.obj.JGLGroup;
import jo.util.jgl.obj.JGLNode;
import jo.util.jgl.obj.tri.JGLObj;

public class SceneLogic
{
    public static Iterator<JGLNode> sceneIterator(JGLNode root)
    {
        List<JGLNode> items = new ArrayList<JGLNode>();
        addItems(items, root, false);
        return items.iterator();
    }
    
    public static Iterator<JGLObj> objIterator(JGLNode root)
    {
        List<JGLObj> items = new ArrayList<JGLObj>();
        addItems(items, root, true);
        return items.iterator();
    }
    
    @SuppressWarnings("unchecked")
    private static void addItems(@SuppressWarnings("rawtypes") List items, JGLNode root, boolean objOnly)
    {
        if (root instanceof JGLGroup)
        {
            if (!objOnly)
                items.add(root);
            for (JGLNode child : ((JGLGroup)root).getChildren())
                addItems(items, child, objOnly);
        }
        else
            items.add(root);
    }
    
    public static JGLNode find(JGLNode root, String id)
    {
        if (id.equals(root.getID()))
            return root;
        if (root instanceof JGLGroup)
        {
            for (JGLNode n : ((JGLGroup)root).getChildren())
            {
                JGLNode found = find(n, id);
                if (found != null)
                    return found;
            }
        }
        return null;
    }
    
    public static List<JGLObj> intersect(JGLNode root, float x, float y)
    {
        List<JGLObj> hits = new ArrayList<JGLObj>();
        for (Iterator<JGLObj> i = objIterator(root); i.hasNext(); )
        {
            JGLObj o = i.next();
            if (isIntersect(o, x, y))
                hits.add(o);
        }
        Collections.sort(hits, new Comparator<JGLObj>() {
            @Override
            public int compare(JGLObj lhs, JGLObj rhs)
            {
                float lhsz = (float)(lhs.getScreenLowBounds().z + lhs.getScreenHighBounds().z)/2f; 
                float rhsz = (float)(rhs.getScreenLowBounds().z + rhs.getScreenHighBounds().z)/2f; 
                return (int)Math.signum(lhsz - rhsz);
            }
        });
        return hits;
    }
    
    private static boolean isIntersect(JGLObj obj, float x, float y)
    {
        Point3f lowBounds = obj.getScreenLowBounds();
        Point3f highBounds = obj.getScreenHighBounds();
        if ((lowBounds == null) || (highBounds == null))
            return false;
        //System.out.println(obj.getLowBounds()+"--"+obj.getHighBounds()+" -> "+lowBounds+"--"+highBounds);
        float lowX = (float)Math.min(lowBounds.x, highBounds.x);
        float highX = (float)Math.max(lowBounds.x, highBounds.x);
        float lowY = (float)Math.min(lowBounds.y, highBounds.y);
        float highY = (float)Math.max(lowBounds.y, highBounds.y);
        if ((lowX <= x) && (x <= highX) && (lowY <= y) && (y <= highY))
            return true;
        return false;
    }
}
