package jo.ttg.core.ui.swing.ctrl.surf;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.util.heal.GlobeChangeEvent;
import jo.util.heal.IGlobeChangeListener;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;
import jo.util.ui.swing.ctrl.ComponentUtils;
import jo.util.ui.swing.utils.MouseUtils;

public class HEALView<T> extends JPanel implements IGlobeChangeListener<T>
{
    private IHEALGlobe<T>   mGlobe;
    private List<TTGActionListener>   mListeners;
    private HEALCanvasPainter<T>   mPainter;
    
    public HEALView()
    {
        initInstantiate();
        initLink();
        initLayout();
    }
    
    private void initInstantiate()
    {
        mListeners = new ArrayList<TTGActionListener>();
        setPainter(new HEALCanvasPainter<T>());
    }
    
    private void initLink()
    {
        //ComponentUtils.moved(this, (e) -> controlResized(e));
        ComponentUtils.resized(this, (e) -> mPainter.setBounds(new Rectangle(0, 0, getSize().width, getSize().height)));
        MouseUtils.mouseClicked(this, (e) -> doMouseClicked(e));
        MouseUtils.mousePressed(this, (e) -> doMouseDown(e));
    }
    
    private void initLayout()
    {
        
    }
    
    public void setDrawOutline(boolean v)
    {
        mPainter.setDrawOutline(v);
    }
    
    public boolean isDrawOutline()
    {
        return mPainter.isDrawOutline();
    }
    
    public void setGlobe(IHEALGlobe<T> globe)
    {
        if (mGlobe != null)
            mGlobe.removeGlobeChangeListener(this);
        mGlobe = globe;
        mPainter.setGlobe(globe);
        if (mGlobe != null)
            mGlobe.addGlobeChangeListener(this);
        repaint();
    }
    
    public IHEALGlobe<T> getGlobe()
    {
        return mGlobe;
    }

    public void repaint(IHEALCoord ord)
    {
        mPainter.repaint(ord);
        repaint();
    }

    /* (non-Javadoc)
     * @see jo.healpix.data.GlobeChangeListener#globeChanged(jo.healpix.data.GlobeChangeEvent)
     */
    public void globeChanged(GlobeChangeEvent<T> ev)
    {
        if (ev.getID() == GlobeChangeEvent.CHANGE_GLOBE)
        {
            //System.out.println("Globe change");           
            mPainter.setDirtyAll(true);
            repaint();
        }
        else //if (ev.getID() == GlobeChangeEvent.CHANGE_COORD)
        {
            repaint(ev.getCoord());
            //System.out.println("Coord change");           
        }
    }

    private void doMouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            IHEALCoord c = mPainter.getCoordFromPoint(e.getX(), e.getY());
            if (c != null)
                fireTTGActionEvent(new TTGActionEvent(this, (e.getButton() == MouseEvent.BUTTON1) ? TTGActionEvent.ACTIVATED : TTGActionEvent.DEACTIVATED, c.toString(), c));
        }
    }

    public void doMouseDown(MouseEvent e)
    {
        IHEALCoord c = mPainter.getCoordFromPoint(e.getX(), e.getY());
        if (c != null)
            fireTTGActionEvent(new TTGActionEvent(this, TTGActionEvent.SELECTED, c.toString(), c));
    }
    
    public void addTTGActionListener(TTGActionListener l)
    {
        synchronized (mListeners)
        {
            mListeners.add(l);
        }
    }
    
    public void removeTTGActionListener(TTGActionListener l)
    {
        synchronized (mListeners)
        {
            mListeners.remove(l);
        }
    }
    
    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        Object[] listeners = mListeners.toArray();
        for (int i = 0; i < listeners.length; i++)
        {
            TTGActionListener l = (TTGActionListener)listeners[i];
            l.actionPerformed(ev);
        }
    }

    @Override
    public void paint(Graphics g)
    {
        mPainter.paintControl((Graphics2D)g);
    }
    
    public HEALCanvasPainter<T> getPainter()
    {
        return mPainter;
    }

    public void setPainter(HEALCanvasPainter<T> painter)
    {
        mPainter = painter;
        if (mPainter != null)
            mPainter.setBackground(getBackground());
    }

}
