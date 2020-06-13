/*
 * Created on Nov 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import javax.swing.JComponent;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.SchemeLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HexPanel extends JComponent
{
	public static final int M_NORM = 0;
	public static final int M_BACK = 3;
	public static final int M_FOCUSED = 1;
	public static final int M_DISABLED = 2;
	
	private IGenScheme	mScheme;
	private int			mHexesWide;
	private int			mHexesHigh;
	private int			mHexSide;
	private boolean		mCanSelectEmpty;
	private OrdBean		mOrigin;
	private OrdBean		mFocus;
	private HexPanelPainter	mPainter;
	
	// derived
	private int			mHexShortSide;
	private int			mHexLongSide;
	private MainWorldBean[][]	mWorlds;
	private Polygon[][]			mPolygons;
	private Color		mForeColor;
	private Color		mDisabledColor;
	private Color		mFocusedColor;
	private Color		mUnfocusedBackColor;
	private Color		mDisabledBackColor;
	private Color		mFocusedBackColor;
	
	public HexPanel()
	{
		mOrigin = new OrdBean();
		mUnfocusedBackColor = new Color(32, 32, 32);
		mDisabledBackColor = Color.black;
		mFocusedBackColor = new Color(128, 128, 128);
		setHexSide(32);
		clearWorlds();
		clearPolygons();
		mPainter = new HexPanelPainter(this);
		mCanSelectEmpty = false;
	}
	
	public HexPanel(IGenScheme scheme)
	{
		this();
		mScheme = scheme;
	}
	/**
	 * @return
	 */
	public int getHexesHigh()
	{
		return mHexesHigh;
	}

	/**
	 * @return
	 */
	public int getHexesWide()
	{
		return mHexesWide;
	}

	/**
	 * @return
	 */
	public int getHexSide()
	{
		return mHexSide;
	}

	/**
	 * @param i
	 */
	public void setHexesHigh(int i)
	{
		mHexesHigh = i;
		clearWorlds();
		clearPolygons();
	}

	/**
	 * @param i
	 */
	public void setHexesWide(int i)
	{
		mHexesWide = i;
		clearWorlds();
		clearPolygons();
	}

	/**
	 * @param i
	 */
	public void setHexSide(int i)
	{
		mHexSide = i;
		mHexShortSide = mHexSide/2;
		mHexLongSide = (mHexSide*866)/1000;
		clearPolygons();
		setSize(getPreferredSize());
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getPreferredSize()
	 */
	public Dimension getPreferredSize()
	{
		int w = (mHexShortSide + mHexSide)*mHexesWide + mHexShortSide + 2;
		int h;
		if (mHexesWide == 1)
		    h = mHexLongSide*mHexesHigh*2 + 2;
		else
		    h = mHexLongSide*(mHexesHigh*2 + 1) + 2;
		return new Dimension(w, h);
	}
	/* (non-Javadoc)
	 * @see java.awt.Component#getMaximumSize()
	 */
	public Dimension getMaximumSize()
	{
		return getPreferredSize();
	}

	/* (non-Javadoc)
	 * @see java.awt.Component#getMinimumSize()
	 */
	public Dimension getMinimumSize()
	{
		return getPreferredSize();
	}

	/**
	 * @return
	 */
	public OrdBean getOrigin()
	{
		return mOrigin;
	}

	/**
	 * @param bean
	 */
	public void setOrigin(OrdBean bean)
	{
		mOrigin = new OrdBean(bean);
		if ((mOrigin.getX()%2 == 1) && (mHexesWide > 1))
			mOrigin.setX(mOrigin.getX() - 1);
		clearWorlds();
		repaint();
	}
	
	public void setOriginURI(String uri)
	{
		if (uri.startsWith("mw://"))
			setOrigin(((MainWorldBean)(SchemeLogic.getFromURI(mScheme, uri))).getOrds());
	}

	public void refresh()
	{
		clearWorlds();
		repaint();
	}

	private void clearWorlds()
	{
		mWorlds = null;
	}

	/**
	 * 
	 */
	private void fillWorlds()
	{
		if (mWorlds != null)
			return;
		if (mForeColor == null)
		{
			mForeColor = getForeground();
			mFocusedColor = mForeColor.brighter().brighter().brighter();
			mDisabledColor = mForeColor.darker().darker().darker();
		}
		IGenMainWorld gen = null;
		if (mScheme != null)
			gen = mScheme.getGeneratorMainWorld();
		mWorlds = new MainWorldBean[mHexesWide][mHexesHigh];
		for (int i = 0; i < mWorlds.length; i++)
		{
			for (int j = 0; j < mWorlds[i].length; j++)
			{
				OrdBean o = new OrdBean(mOrigin.getX() + i, mOrigin.getY() + j, mOrigin.getZ());
				if (gen != null)
					mWorlds[i][j] = gen.generateMainWorld(o);
				else
					mWorlds[i][j] = null; 
			}
		}
	}

	private void clearPolygons()
	{
		mPolygons = null;
	}

	/**
	 * 
	 */
	private void fillPolygons()
	{
		if (mPolygons != null)
			return;
		mPolygons = new Polygon[mHexesWide][mHexesHigh];
		int oddness = ((mOrigin.getX() % 2 == 0) ? 0 : 1);
		for (int i = 0; i < mPolygons.length; i++)
		{
			for (int j = 0; j < mPolygons[i].length; j++)
			{
				int x[], y[];

				Dimension o = calcHexOrigin(i, j, (oddness + i)%2 == 0);
				x = new int[7];
				y = new int[7];
				x[0] = o.width + mHexShortSide;
				y[0] = o.height;
				x[1] = o.width + mHexShortSide + mHexSide;
				y[1] = o.height;
				x[2] = o.width + mHexShortSide + mHexSide + mHexShortSide;
				y[2] = o.height + mHexLongSide;
				x[3] = o.width + mHexShortSide + mHexSide;
				y[3] = o.height + mHexLongSide*2;
				x[4] = o.width + mHexShortSide;
				y[4] = o.height + mHexLongSide*2;
				x[5] = o.width;
				y[5] = o.height + mHexLongSide;
				x[6] = x[0];
				y[6] = y[0];
				mPolygons[i][j] = new Polygon(x, y, x.length);
			}
		}
	}

	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		fillWorlds();
		fillPolygons();
		Graphics2D g2 = (Graphics2D)g;
		int oddness = ((mOrigin.getX() % 2 == 0) ? 0 : 1);
		Dimension focusOrigin = null;
		Polygon focusPoly = null;
		MainWorldBean focusWorld = null;
	    Dimension o = new Dimension();
		for (int i = 0; i < mWorlds.length; i++)
		{
			for (int j = 0; j < mWorlds[i].length; j++)
			{
			    o.width = mPolygons[i][j].xpoints[5];
			    o.height = mPolygons[i][j].ypoints[0];
				//Dimension o = calcHexOrigin(i, j, (oddness + i)%2 == 0);
				if (mWorlds[i][j] == null)
				{
					OrdBean ords = new OrdBean(mOrigin.getX() + i, mOrigin.getY() + j, mOrigin.getZ());
					int mode;
					if (ords.equals(mFocus))
						mode = M_FOCUSED;
					else
						mode = calcMode(ords);
					mPainter.paint(g2, o, mPolygons[i][j], ords, mode);
				}
				else if (mWorlds[i][j].getOrds().equals(mFocus))
				{
					focusOrigin = new Dimension(o);
					focusPoly = mPolygons[i][j];
					focusWorld = mWorlds[i][j];
				}
				else
					mPainter.paint(g2, o, mPolygons[i][j], mWorlds[i][j], calcMode(mWorlds[i][j].getOrds()));
			}
		}
		if (focusOrigin != null)
			mPainter.paint(g2, focusOrigin, focusPoly, focusWorld, M_FOCUSED);
	}
	
	public int calcMode(OrdBean o)
	{
		return M_NORM;
	}
	
	public MainWorldBean calcWorld(OrdBean o)
	{
		if (o == null)
			return null;
		if (mWorlds == null)
			return null;
		int i = (int)(o.getX() - mOrigin.getX());
		int j = (int)(o.getY() - mOrigin.getY());
		try
		{
			return mWorlds[i][j];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public Polygon calcHexPoly(OrdBean o) 
	{
		if (o == null)
			return null;
		int x = (int)(o.getX() - mOrigin.getX());
		int y = (int)(o.getY() - mOrigin.getY());
		try
		{
			return mPolygons[x][y];
		}
		catch (ArrayIndexOutOfBoundsException e)
		{
			return null;
		}
	}
	
	public Dimension calcHexOrigin(OrdBean o) 
	{
		if (o == null)
			return null;
		int x = (int)(o.getX() - mOrigin.getX());
		int y = (int)(o.getY() - mOrigin.getY());
		boolean odd = (o.getX()%2) != 0; 
		return calcHexOrigin(x, y, odd); 
	}
	public Dimension calcHexOrigin(int x, int y) { return calcHexOrigin(x, y, false); }
	public Dimension calcHexOrigin(int x, int y, boolean odd)
	{
		int ox = x*(mHexShortSide + mHexSide);
		int oy = y*(mHexLongSide*2);
		if (!odd)
			oy += mHexLongSide;
		return new Dimension(ox+1, oy+1);
	}
	public OrdBean calcHexSource(int x, int y)
	{
		fillPolygons();
		int guessx = x/(mHexShortSide + mHexSide);
		int guessy = y/(mHexLongSide*2);
		for (int i = -1; i <= 0; i++)
			for (int j = -1; j <= 0; j++)
			{
				try
				{
					if (mPolygons[guessx+i][guessy+j].contains(x, y))
					{
						//System.out.println(i+","+j);
						return new OrdBean(mOrigin.getX() + guessx+i, mOrigin.getY() + guessy+j, mOrigin.getZ());
					}
				}
				catch (ArrayIndexOutOfBoundsException e)
				{
				}
			}
		return null;
	}
	
	public void makeVisible(OrdBean o)
	{
		if (OrdLogic.within(o, mOrigin, 
			new OrdBean(mOrigin.getX() + mHexesWide, mOrigin.getY() + mHexesHigh, 1)))
			return;
		OrdBean newOrigin = new OrdBean(o.getX() - mHexesWide/2,
			o.getY() - mHexesHigh/2, 0);
		setOrigin(newOrigin);
	}

	/**
	 * @return
	 */
	public OrdBean getFocus()
	{
		return mFocus;
	}

	/**
	 * @param bean
	 */
	public void setFocus(OrdBean bean)
	{
		if ((mFocus != null) && mFocus.equals(bean))
			return;
		fillPolygons();			
		Polygon oldPoly = calcHexPoly(mFocus);
		mFocus = bean;
		Polygon newPoly = calcHexPoly(mFocus);
		if (oldPoly != null)
		{
			repaint(oldPoly.getBounds());
			//System.out.print("oldrepaint("+oldPoly.getBounds()+")");
		}
		if (newPoly != null)
		{
			repaint(newPoly.getBounds());
			//System.out.print("newrepaint("+newPoly.getBounds()+")");
		}
		makeVisible(bean);
	}

	/**
	 * @return
	 */
	public Color getDisabledColor()
	{
		return mDisabledColor;
	}

	/**
	 * @return
	 */
	public Color getFocusedColor()
	{
		return mFocusedColor;
	}

	/**
	 * @return
	 */
	public Color getForeColor()
	{
		return mForeColor;
	}

	/**
	 * @param color
	 */
	public void setDisabledColor(Color color)
	{
		mDisabledColor = color;
	}

	/**
	 * @param color
	 */
	public void setFocusedColor(Color color)
	{
		mFocusedColor = color;
	}

	/**
	 * @param color
	 */
	public void setForeColor(Color color)
	{
		mForeColor = color;
	}

	/**
	 * @return
	 */
	public Color getDisabledBackColor()
	{
		return mDisabledBackColor;
	}

	/**
	 * @return
	 */
	public Color getFocusedBackColor()
	{
		return mFocusedBackColor;
	}

	/**
	 * @return
	 */
	public Color getUnfocusedBackColor()
	{
		return mUnfocusedBackColor;
	}

	/**
	 * @param color
	 */
	public void setDisabledBackColor(Color color)
	{
		mDisabledBackColor = color;
	}

	/**
	 * @param color
	 */
	public void setFocusedBackColor(Color color)
	{
		mFocusedBackColor = color;
	}

	/**
	 * @param color
	 */
	public void setUnfocusedBackColor(Color color)
	{
		mUnfocusedBackColor = color;
	}

	/**
	 * @return
	 */
	public HexPanelPainter getPainter()
	{
		return mPainter;
	}

	/**
	 * @param painter
	 */
	public void setPainter(HexPanelPainter painter)
	{
		mPainter = painter;
		mPainter.setPanel(this);
	}

    public IGenScheme getScheme()
    {
        return mScheme;
    }

    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

	public boolean isCanSelectEmpty()
	{
		return mCanSelectEmpty;
	}

	public void setCanSelectEmpty(boolean b)
	{
		mCanSelectEmpty = b;
	}

}
