/*
 * Created on Dec 19, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.LocationURILogic;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.utils.DisplayUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LocationPanel extends JPanel implements MouseListener, MouseMotionListener
{
	private static ImageIcon ORBIT0_IMG = TTGIconUtils.getPlanet("orbit_0.gif");
	private static ImageIcon ORBIT10_IMG = TTGIconUtils.getPlanet("orbit_10.gif");
	private static ImageIcon ORBIT50_IMG = TTGIconUtils.getPlanet("orbit_50.gif");
	private static ImageIcon ORBIT100_IMG = TTGIconUtils.getPlanet("orbit_100.gif");

	private IGenScheme	mScheme;
	private Font		mLabelFont;
	private List<TTGActionListener>	mTTGActionListeners;
	
	private HexPanel	mHex;
	private JPanel		mBodyPanel;
	private JLabel[]	mBody;
	private BodyBean[]	mBodyBean;
	private JLabel		mOrbit;
	private JLabel		mText;
	
	public LocationPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mTTGActionListeners = new ArrayList<>();
	    mLabelFont = new Font("Dialog", Font.PLAIN, 9);
		mHex = new HexPanel();
		mHex.setHexesHigh(1);
		mHex.setHexesWide(1);
		mHex.setForeColor(Color.YELLOW);
		mBodyPanel = new JPanel();
		mBody = new JLabel[0];
		mOrbit = new JLabel();
	    mOrbit.setFont(mLabelFont);
		mText = new JLabel();
	    //mText.setFont(mLabelFont);
	}
	private void initLayout()
	{
		setLayout(new BorderLayout());
		add("Center", mBodyPanel);
		add("South", mText);
	}
	private void initLink()
	{
	}

	public void setLocation(LocationURI location)
	{
	    mHex.setOrigin(location.getOrds());
	    if (location.getType() == LocationURI.BODY)
	    {
	        
	        String uri = LocationURILogic.getURI(location);
		    BodyBean b = (BodyBean)SchemeLogic.getFromURI(mScheme, uri);
		    if (b == null)
		    {
		        System.err.println("Cannot find location "+uri);
		        b = (BodyBean)SchemeLogic.getFromURI(mScheme, uri);
		        return;
		    }
		    mBody = new JLabel[b.getDepth()+2];
		    mBodyBean = new BodyBean[mBody.length];
		    for (int i = mBody.length - 2; b != null; b = b.getPrimary(), i--)
		    {
		        mBodyBean[i] = b;
		        mBody[i] = new JLabel();
		        mBody[i].setIcon(BodyView.getIcon(b));
		        mBody[i].setText(b.getName());
		        mBody[i].setHorizontalTextPosition(JLabel.CENTER);
		        mBody[i].setVerticalTextPosition(JLabel.BOTTOM);
		        mBody[i].setFont(mLabelFont);
		        mBody[i].addMouseListener(this);
		        mBody[i].addMouseMotionListener(this);
		        if ((b.getPrimary() != null))
		            if (b.getPrimary() instanceof BodyStarBean)
			            mBody[i].setToolTipText(
			                    "Orbit #"+
			                    FormatUtils.sNum(b.getOrbit(), 0, 1)
			                    );
		            else if ((b.getPrimary() instanceof BodyWorldBean) || (b.getPrimary() instanceof BodyGiantBean))
		                mBody[i].setToolTipText(
		                    FormatUtils.sNum(b.getOrbitalRadius()/b.getPrimary().getDiameter(), 0, 1)
		                    +" diameters");
		    }
		    mBody[mBody.length-1] = new JLabel();
		    mBody[mBody.length-1].setFont(mLabelFont);
		    try
		    {
		        double o = Double.parseDouble(location.getParam("orbit"));
		        if (o < 1)
		            mBody[mBody.length-1].setIcon(ORBIT0_IMG);
		        else if (o < 20)
		            mBody[mBody.length-1].setIcon(ORBIT10_IMG);
		        else if (o < 90)
		            mBody[mBody.length-1].setIcon(ORBIT50_IMG);
		        else
		            mBody[mBody.length-1].setIcon(ORBIT100_IMG);
		        String desc = DisplayUtils.formatURI(location, mScheme);
		        int off = desc.indexOf(" in the");
		        if (off >= 0)
		            desc = desc.substring(0, off);
			    mBody[mBody.length-1].setToolTipText(desc);
		    }
		    catch (Exception e)
		    {
		        mBody[mBody.length-1].setIcon(ORBIT0_IMG);
		        mBody[mBody.length-1].setToolTipText("Surface");
		    }
	        mBody[mBody.length-1].setHorizontalTextPosition(JLabel.CENTER);
	        mBody[mBody.length-1].setVerticalTextPosition(JLabel.BOTTOM);
		    mBodyPanel.removeAll();
		    mBodyPanel.add(mHex);
		    for (int i = 0; i < mBody.length; i++)
		        mBodyPanel.add(mBody[i]);
	    }
	    else if (location.getType() == LocationURI.SYSTEM)
	    {
	        MainWorldBean sys1 = mScheme.getGeneratorMainWorld().generateMainWorld(location.getOrds());
	        MainWorldBean sys2 = mScheme.getGeneratorMainWorld().generateMainWorld(OrdLogic.parseString(location.getParam("destSys")));
	        double timeLeft = Double.valueOf(location.getParam("timeLeft")).doubleValue();
	        timeLeft /= 60*24;
	        timeLeft = Math.floor(timeLeft*10 + .5)/10;
	        JLabel l1 = new JLabel(BodyView.STAR_IMG);
	        l1.setToolTipText(sys1.getName());
	        l1.setFont(mLabelFont);
	        JLabel i = new JLabel(" "+timeLeft+" days to ");
	        i.setFont(mLabelFont);
	        JLabel l2 = new JLabel(BodyView.STAR_IMG);
	        l2.setToolTipText(sys2.getName());
	        l2.setFont(mLabelFont);
		    mBodyPanel.removeAll();
		    mBodyPanel.add(mHex);
		    mBodyPanel.add(l1);
		    mBodyPanel.add(i);
		    mBodyPanel.add(l2);
	    }
	    mText.setText(DisplayUtils.formatURI(location, mScheme));
	}
	
	public void setLocation(String location)
	{
	    setLocation(LocationURILogic.fromURI(location));
	}
    public IGenScheme getScheme()
    {
        return mScheme;
    }
    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
        mHex.setScheme(scheme);
    }
    public void setAdditionalText(String txt)
    {
        String oldTxt = mText.getText();
        if (!oldTxt.endsWith(txt))
            mText.setText(oldTxt+txt);
    }

	public void addTTGActionListener(TTGActionListener l)
	{
		synchronized (mTTGActionListeners)
		{
			mTTGActionListeners.add(l);
		}
	}

	public void removeTTGActionListener(TTGActionListener l)
	{
		synchronized (mTTGActionListeners)
		{
			mTTGActionListeners.remove(l);
		}
	}
	
	protected void fireTTGActionEvent(TTGActionEvent ev)
	{
		Object[] l = mTTGActionListeners.toArray();
		for (int i = 0; i < l.length; i++)
			((TTGActionListener)l[i]).actionPerformed(ev);
	}

	protected void doProcessMouseEvent(MouseEvent e)
	{
	    BodyBean src = null;
	    String srcURI = null;
	    Object ctrl = e.getSource();
	    for (int i = 0; i < mBody.length; i++)
	        if (ctrl == mBody[i])
	        {
	            src = mBodyBean[i];
	        	srcURI = src.getURI();
			}
	    if (src == null)
	        return;
		switch (e.getID())
		{
			case MouseEvent.MOUSE_CLICKED:
				if (e.getClickCount() == 1)
					fireTTGActionEvent(new TTGActionEvent(this, TTGActionEvent.SELECTED, srcURI, src));				
				else if (e.getClickCount() == 2)
				{
					if (e.getButton() == MouseEvent.BUTTON1)
						fireTTGActionEvent(new TTGActionEvent(this, TTGActionEvent.ACTIVATED, srcURI, src));				
				}
				break;
//			case MouseEvent.MOUSE_DRAGGED:
//				break;
//			case MouseEvent.MOUSE_MOVED:
//				break;
//			case MouseEvent.MOUSE_EXITED:
//				break;
		}
	}

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
     */
    public void mouseDragged(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
     */
    public void mouseMoved(MouseEvent ev)
    {
        doProcessMouseEvent(ev);        
    }
}
