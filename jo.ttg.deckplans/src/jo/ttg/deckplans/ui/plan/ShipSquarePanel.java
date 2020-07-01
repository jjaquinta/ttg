package jo.ttg.deckplans.ui.plan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipImageSettingsBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.utils.obj.StringUtils;
import jo.vecmath.data.SparseMatrix;

public class ShipSquarePanel extends JPanel
{
    private int                          SX = 24;
    private int                          SY = 24;

    private ShipImageSettingsBean        mSettings;
    private SparseMatrix<ShipSquareBean> mSquares;
    private int                          mZ;
    
    private Point3i                      mLower = new Point3i();
    private Point3i                      mUpper = new Point3i();

    public ShipSquarePanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
    }

    private void initLink()
    {
        MouseUtils.mouseWheelMoved(this, (ev) -> doWheelMoved(ev));
        MouseUtils.mouseMoved(this, (ev) -> doMouseMoved(ev));
    }

    private void initLayout()
    {
    }
    
    private void doMouseMoved(MouseEvent ev)
    {
        int sy = mLower.y + ev.getX()/SX;
        int sx = mLower.x + ev.getY()/SY;
        ShipSquareBean sq = mSquares.get(sx, sy, mZ);
        if (sq == null)
            setToolTipText("");
        else
        {
            StringBuffer txt = new StringBuffer(ShipSquareBean.NAMES[sq.getType()]);
            if (!StringUtils.isTrivial(sq.getNotes()))
                txt.append(" "+sq.getNotes());
            else
            {
                if (sq.getCompartment() > 0)
                    txt.append(" #"+sq.getCompartment());
            }
            List<String> access = new ArrayList<>();
            if (sq.isMinusXAccess())
                access.add("starboard");
            if (sq.isPlusXAccess())
                access.add("port");
            if (sq.isMinusYAccess())
                access.add("fore");
            if (sq.isPlusYAccess())
                access.add("aft");
            if (sq.isCeilingAccess())
                access.add("dorsal");
            if (sq.isFloorAccess())
                access.add("ventral");
            if (access.size() != 0)
                txt.append(", access="+StringUtils.listize(access));
            if (sq.isNeedsAir())
                txt.append(" (needs air)");
            setToolTipText(txt.toString());
        }
    }
    
    private void doWheelMoved(MouseWheelEvent ev)
    {
        if (ev.getWheelRotation() < 0)
            if (SX > 8)
            {
                SX -= 8;
                SY -= 8;
                getParent().doLayout();
            }
        if (ev.getWheelRotation() > 0)
            if (SX < 64)
            {
                SX += 8;
                SY += 8;
                getParent().doLayout();
            }
    }
    
    @Override
    public Dimension getPreferredSize()
    {
        return new Dimension((mUpper.y - mLower.y + 1)*SY, (mUpper.x - mLower.x + 1)*SX);
    }
    
    @Override
    public void paint(Graphics g)
    {
        Dimension size = getSize();
        g.setColor(mSettings.BACKGROUND_COLOR);
        g.fillRect(0, 0, size.width, size.height);
        for (int x = mLower.x; x <= mUpper.x; x++)
            for (int y = mLower.y; y <= mUpper.y; y++)
                paint(g, mSquares.get(x, y, mZ), mSquares.get(x, y-1, mZ), mSquares.get(x-1, y, mZ), (y - mLower.y)*SY, (x - mLower.x)*SX);
    }

    private void paint(Graphics g, ShipSquareBean sq, ShipSquareBean left, ShipSquareBean top, int x, int y)
    {
        if (sq != null)
        {
            g.setColor(mSettings.squareToColor(sq));
            g.fillRect(x, y, SX, SY);
        }
        g.setColor(getBoundaryColor(sq, left));
        g.drawLine(x, y, x, y + SY);
        g.setColor(getBoundaryColor(sq, top));
        g.drawLine(x, y, x + SX, y);
        if (sq == null)
            return;
        g.setColor(Color.BLACK);
        if (sq.isMinusXAccess())
            g.fillRect(x + SX/4, y, SX/2, SY/8);
        if (sq.isPlusXAccess())
            g.fillRect(x + SX/4, y + SY*7/8, SX/2, SY/8);
        if (sq.isMinusYAccess())
            g.fillRect(x, y + SY/4, SX/8, SY/2);
        if (sq.isPlusYAccess())
            g.fillRect(x + SX*7/8, y + SY/4, SX/8, SY/2);
        if (sq.isFloorAccess())
        {
            int r = SX*5/24;
            g.drawOval(x + SX/2-r, y + SY/2-r, r*2, r*2);
        }
        if (sq.isCeilingAccess())
        {
            int r = SX*7/24;
            g.drawOval(x + SX/2-r, y + SY/2-r, r*2, r*2);
        }
    }
    
    private Color getBoundaryColor(ShipSquareBean sq1, ShipSquareBean sq2)
    {
        if ((sq1 == null) || (sq2 == null))
            return mSettings.GRID_SHIP_COLOR;
        if (sq1.getType() != sq2.getType())
            return Color.BLACK;
        if (sq1.getCompartment() != sq2.getCompartment())
            return Color.BLACK;
        if (StringUtils.compareTo(sq1.getNotes(), sq2.getNotes()) != 0)
            return Color.BLACK;
        return mSettings.GRID_SHIP_COLOR;
    }

    public SparseMatrix<ShipSquareBean> getSquares()
    {
        return mSquares;
    }

    public void setSquares(SparseMatrix<ShipSquareBean> squares)
    {
        mSquares = squares;
        mSquares.getBounds(mLower, mUpper);
        getParent().doLayout();
    }

    public int getZ()
    {
        return mZ;
    }

    public void setZ(int z)
    {
        mZ = z;
        repaint();
    }

    public int getDeck()
    {
        return mZ - mLower.z  + 1;
    }

    public void setDeck(int deck)
    {
        int z = mLower.z + deck - 1;
        if (z < mLower.z)
            z = mLower.z;
        else if (z > mUpper.z)
            z = mUpper.z;
        mZ = z;
        repaint();
    }

    public int getDecks()
    {
        return mUpper.z - mLower.z  + 1;
    }

    public ShipImageSettingsBean getSettings()
    {
        return mSettings;
    }

    public void setSettings(ShipImageSettingsBean settings)
    {
        mSettings = settings;
        repaint();
    }
}
