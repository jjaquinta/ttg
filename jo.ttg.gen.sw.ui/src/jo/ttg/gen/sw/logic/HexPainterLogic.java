package jo.ttg.gen.sw.logic;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.mw.MainWorldLogic;

public class HexPainterLogic
{
    public static final int M_NORM = 0;
    public static final int M_BACK = 3;
    public static final int M_FOCUSED = 1;
    public static final int M_DISABLED = 2;

    public static Color       mForeColor;
    public static Color       mBackColor;
    public static Color       mDisabledColor;
    public static Color       mFocusedColor;
    public static Color       mUnfocusedBackColor = new Color(32, 32, 32);
    public static Color       mDisabledBackColor = Color.black;
    public static Color       mFocusedBackColor = new Color(128, 128, 128);

    protected static int       mHexSide;
    protected static int       mHexShortSide;
    protected static int       mHexLongSide;
    protected static Font      mNameFont;
    
    public static void paint(Graphics2D g, Dimension oo, Polygon p, Object obj, int mode)
    {
        // correct origin
        Dimension o = new Dimension(oo.width - mHexShortSide - mHexSide/2, oo.height - mHexLongSide);
        MainWorldBean mw = null;
        if (obj instanceof MainWorldBean)
            mw = (MainWorldBean)obj;
        paintHex(g, p, mw, mode);
        if (mw == null)
            return;
        if (mHexSide >= 18)
        {
            paintZone(g, o, mw, mode);
            paintLoc(g, o, mw, mode);
            paintName(g, o, mw, mode);
            paintWorld(g, o, mw, mode);
            paintPort(g, o, mw, mode);
            paintBases(g, o, mw, mode);
            paintGiant(g, o, mw, mode);
        }
        else
        {
            paintWorld(g, o, mw, mode);
        }
    }
    private static void paintBases(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        int w;
        int rad = mHexLongSide/4;

        Font f = new Font("Arial", Font.PLAIN, 8);
        g.setFont(f);
        FontMetrics fm = g.getFontMetrics();
        String m_Bases = mw.getPopulatedStats().getBasesDesc();
        if (m_Bases.indexOf("N") >= 0)
        {
            g.setColor(Color.yellow);
            w = fm.stringWidth("N");
            g.drawString("N", o.width + mHexShortSide + mHexSide/2 - rad - w, o.height + mHexLongSide - fm.getDescent());
        }
        if (m_Bases.indexOf("S") >= 0)
        {
            g.setColor(Color.red);
            w = fm.stringWidth("S");
            g.drawString("S", o.width + mHexShortSide + mHexSide/2 - rad - w, o.height + mHexLongSide + fm.getAscent());
        }
    }
    private static void paintGiant(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        if (mw.getNumGiants() < 1)
            return;
        int rad = mHexLongSide/4;
        int rad2 = rad/2;
        setColorFromMode(g, mode);
        g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad, o.height + mHexLongSide - rad - rad2, rad2, rad2);
    }
    private static void paintHex(Graphics2D g, Polygon p, MainWorldBean mw, int mode)
    {
        if (p == null)
            return;
        Color c = g.getColor();
        setBackColorFromMode(g, mode);
        g.fillPolygon(p);
        g.setColor(c);
        g.drawPolygon(p);
    }
    private static void paintLoc(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        setColorFromMode(g, mode);
        g.setFont(mNameFont);
        FontMetrics fm = g.getFontMetrics();
        String str = OrdLogic.getShortNum(mw.getOrds());
        int w = fm.stringWidth(str);
        g.drawString(str, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + 1 + fm.getAscent());
    }
    private static void paintName(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        int rad = mHexLongSide/4;
        setColorFromMode(g, mode);
        g.setFont(mNameFont);
        FontMetrics fm = g.getFontMetrics();
        String str = MainWorldLogic.getNameDesc(mw);
        int w = fm.stringWidth(str);
        g.drawString(str, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + mHexLongSide + rad + fm.getAscent());
    }
    private static void paintPort(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        int rad = mHexLongSide/4;
        setColorFromMode(g, mode);
        g.setFont(mNameFont);
        FontMetrics fm = g.getFontMetrics();
        String Port = String.valueOf((char)mw.getPopulatedStats().getUPP().getPort().getValue());
        int w = fm.stringWidth(Port);
        g.drawString(Port, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + mHexLongSide - rad - fm.getDescent());
    }
    private static void paintWorld(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        int rad;
        if (mHexSide >= 18)
            rad = mHexLongSide/8;
        else
            rad = mHexLongSide/2;
        int size = mw.getPopulatedStats().getUPP().getSize().getValue();
        if (size == '0')
        {   // asteroid
            int rad2 = rad/3;
            setColorFromMode(g, mode);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide - rad, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2, o.height + mHexLongSide - rad, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad/2, o.height + mHexLongSide - rad/2, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2, o.height + mHexLongSide, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad/2, o.height + mHexLongSide + rad/2, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide + rad, rad2, rad2);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad, o.height + mHexLongSide + rad, rad2, rad2);
        }
        else
        {
            int hydro = mw.getPopulatedStats().getUPP().getHydro().getValue();
            if (hydro == 0)
                g.setColor(Color.orange);
            else
                g.setColor(Color.blue);
            g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide - rad, rad*2, rad*2);
        }
    }
    private static void paintZone(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
    {
        if (mw.getPopulatedStats().getTravelZone() == 'R')
        {
            g.setColor(Color.red);
            g.drawOval(o.width + 6, o.height + 6, mHexShortSide*2 + mHexSide - 12, mHexLongSide*2 - 12);
        }
        else if (mw.getPopulatedStats().getTravelZone() == 'A')
        {
            g.setColor(Color.yellow);
            g.drawOval(o.width + 6, o.height + 6, mHexShortSide*2 + mHexSide - 12, mHexLongSide*2 - 12);
        }
    }
    
    private static void setColorFromMode(Graphics2D g, int mode)
    {
        switch (mode)
        {
            case HexPainterLogic.M_NORM:
                g.setColor(mForeColor);
                break;
            case HexPainterLogic.M_FOCUSED:
                g.setColor(mFocusedColor);
                break;
            case HexPainterLogic.M_DISABLED:
                g.setColor(mDisabledColor);
                break;
            case HexPainterLogic.M_BACK:
                g.setColor(mBackColor);
                break;
        }
    }
    
    private static void setBackColorFromMode(Graphics2D g, int mode)
    {
        switch (mode)
        {
            case HexPainterLogic.M_NORM:
                g.setColor(mUnfocusedBackColor);
                break;
            case HexPainterLogic.M_FOCUSED:
                g.setColor(mFocusedBackColor);
                break;
            case HexPainterLogic.M_DISABLED:
                g.setColor(mDisabledBackColor);
                break;
            case HexPainterLogic.M_BACK:
                g.setColor(mBackColor);
                break;
        }
    }

    public static int getHexSide()
    {
        return mHexSide;
    }

    public static void setHexSide(int hexSide)
    {
        mHexSide = hexSide;
        mHexShortSide = mHexSide/2;
        mHexLongSide = (mHexSide*866)/1000;
        mNameFont = new Font("Arial", Font.PLAIN, mHexSide/3);
    }
}
