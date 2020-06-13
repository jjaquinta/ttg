package jo.util.jgl.obj;

import java.util.ArrayList;
import java.util.List;

public class JGLGroup extends JGLNode
{
    protected List<JGLNode>   mChildren;
    
    public JGLGroup()
    {
        super();
        mChildren = new ArrayList<JGLNode>();
    }
    
    @Override
    public void recycle()
    {
        super.recycle();
        synchronized (this)
        {
            for (JGLNode n : getChildren())
                n.recycle();
        }
    }
    
    public JGLNode add(JGLNode n)
    {
        synchronized (this)
        {
            getChildren().add(n);
        }
        return n;
    }
    
    public JGLNode remove(JGLNode n)
    {
        synchronized (this)
        {
            getChildren().remove(n);
        }
        return n;
    }

    public List<JGLNode> getChildren()
    {
        return mChildren;
    }

    public void setChildren(List<JGLNode> children)
    {
        mChildren = children;
    }

}
