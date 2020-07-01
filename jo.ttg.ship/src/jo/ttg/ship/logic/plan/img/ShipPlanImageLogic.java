package jo.ttg.ship.logic.plan.img;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipImageSettingsBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipPlanComponentBean;
import jo.ttg.ship.beans.plan.ShipPlanPerimeterBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.ShipPlanUtils;
import jo.util.utils.ArrayUtils;
import jo.util.utils.ThreadHelper;
import jo.vecmath.data.SparseMatrix;

public class ShipPlanImageLogic
{
    // transient variables
    // (must update to be thread safe)
    static ShipData mData;
    
    public synchronized static List<BufferedImage> printShipImage(ShipPlanBean ship, ShipImageSettingsBean settings)
    {
        ThreadHelper.setCanCancel(true);
        mData = new ShipData();
        mData.setShip(ship);
        mData.settings = settings;
        if (mData.settings == null)
            mData.settings = new ShipImageSettingsBean();
        mData.rnd = new Random(mData.settings.getSeed());
        ThreadHelper.setTotalUnits(mData.sUpper.z - mData.sLower.z + 2);
        List<BufferedImage> decks = new ArrayList<>();
        int totalX = 0;
        int totalY = 0;
        for (int z = mData.sLower.z; z <= mData.sUpper.z; z++)
        {
            ThreadHelper.setSubTask("Deck "+z);
            SparseMatrix<ShipSquareBean> level = new SparseMatrix<ShipSquareBean>();
            for (int y = mData.sLower.y; y <= mData.sUpper.y; y++)
                for (int x = mData.sLower.x; x <= mData.sUpper.x; x++)
                {
                    ShipSquareBean sq = ship.getSquare(x, y, z);
                    if (sq != null)
                        level.set(x, y, z, sq);                       
                }
            BufferedImage deck = printLevelImage(level, z);
            decks.add(deck);
            totalX = Math.max(totalX, deck.getWidth());
            totalY += deck.getHeight();
            ThreadHelper.work(1);
            if (ThreadHelper.isCanceled())
                return null;
        }
        BufferedImage everything = new BufferedImage(totalX, totalY, BufferedImage.TYPE_INT_ARGB);
        mData.setG(everything.getGraphics());
        mData.g.setColor(ShipPlanImageLogic.mData.settings.BACKGROUND_COLOR);
        mData.g.fillRect(0, 0, totalX, totalY);
        int y = 0;
        for (BufferedImage deck : decks)
        {
            mData.g.drawImage(deck, (totalX - deck.getWidth())/2, y, null);
            y += deck.getHeight();
        }
        decks.add(0, everything);
        ThreadHelper.work(1);
        return decks;
    }
    
    private static BufferedImage printLevelImage(SparseMatrix<ShipSquareBean> level, int z)
    {
        mData.labels.clear();
        mData.setDeck(z);
        int xSize = (mData.dUpper.x - mData.dLower.x + 1)*mData.ss() + mData.settings.getBoundaryGutter()*2;
        int ySize = (mData.dUpper.y - mData.dLower.y + 1)*mData.ss() + mData.settings.getBoundaryGutter()*2;
        BufferedImage img;
        AffineTransform t = null;
        if (xSize >= ySize)
        {
            img = new BufferedImage(xSize, ySize, BufferedImage.TYPE_INT_ARGB);
            t = null;
            mData.horizontal = true;
        }
        else
        {
            img = new BufferedImage(ySize, xSize, BufferedImage.TYPE_INT_ARGB);
            t = new AffineTransform();
            t.translate(0, xSize);
            t.rotate(-Math.PI/2);
            mData.horizontal = false;
        }
        mData.setG(img.getGraphics());
        if (t != null)
            mData.g.transform(t);
        mData.g.setColor(ShipPlanImageLogic.mData.settings.BACKGROUND_COLOR);
        mData.g.fillRect(0, 0, xSize, ySize);
        addBoundaryLabels(img, z);
        drawGrid(ShipPlanImageLogic.mData.settings.GRID_BACK_COLOR);
        Shape hullOutline = drawHull(level);
        for (int y = mData.dLower.y; y <= mData.dUpper.y; y++)
            for (int x = mData.dLower.x; x <= mData.dUpper.x; x++)
                paintSquare(x, y, z, level);
        mData.g.setClip(hullOutline);
        drawGrid(ShipPlanImageLogic.mData.settings.GRID_SHIP_COLOR);
        mData.g.setClip(null);
        drawOutline(ShipSquareBean.MANEUVER, "Maneuver Drive");
        drawOutline(ShipSquareBean.JUMP, "Jump Drive");
        drawOutline(ShipSquareBean.POWER, "Power Plant");
        drawOutline(ShipSquareBean.CARGO, "Cargo");
        drawComponentOutlines(ShipSquareBean.TURRET, "%s Turret #%d", null);
        drawComponentOutlines(ShipSquareBean.BAY, "%s Bay #%d", null);
        drawComponentOutlines(ShipSquareBean.SPINE, "Spinal %s", null);
        drawComponentOutlines(ShipSquareBean.HANGER, "%s Hanger #%d", null);
        drawComponentOutlines(ShipSquareBean.STATEROOM, "%s", "SR %2$d");
        drawCorridorOutlines();
        drawDoors();
        LabelLogic.printLabels();
        return img;
    }
    
    private static void drawCorridorOutlines()
    {
        int ss = mData.ss();
        mData.g.setColor(Color.BLACK);
        mData.g.setStroke(new BasicStroke(mData.ss()/12));
        for (int y = mData.dLower.y; y <= mData.dUpper.y; y++)
        {
            int yy = y(y);
            for (int x = mData.dLower.x; x <= mData.dUpper.x; x++)
                if (mData.ship.isType(x, y, mData.deck, ShipSquareBean.CORRIDOR))
                {
                    int xx = x(x);
                    if (!mData.ship.isType(x+1, y, mData.deck, ShipSquareBean.CORRIDOR))
                        mData.g.drawLine(xx + ss, yy, xx + ss, yy + ss);
                    if (!mData.ship.isType(x-1, y, mData.deck, ShipSquareBean.CORRIDOR))
                        mData.g.drawLine(xx, yy, xx , yy + ss);
                    if (!mData.ship.isType(x, y+1, mData.deck, ShipSquareBean.CORRIDOR))
                        mData.g.drawLine(xx, yy + ss, xx + ss, yy + ss);
                    if (!mData.ship.isType(x, y-1, mData.deck, ShipSquareBean.CORRIDOR))
                        mData.g.drawLine(xx, yy, xx + ss, yy);
                }
        }
    }

    private static void paintSquare(int x, int y, int z,
            SparseMatrix<ShipSquareBean> level)
    {
        ShipSquareBean square = level.get(x, y, z);
        if (square == null)
            return;
        if (square.getType() == ShipSquareBean.HULL)
            return;
        mData.g.setColor(mData.settings.squareToColor(square));
        int ss = mData.ss();
        int xx = x(x);
        int yy = y(y);
        mData.g.fillRect(xx, yy, ss, ss);
        Stroke oldStroke = mData.g.getStroke();
        if (square.isFloorAccess())
        {
            mData.g.setColor(Color.BLACK);
            Stroke solid = new BasicStroke(ss/12, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0);
            mData.g.setStroke(solid);
            int r = ss*5/24;
            mData.g.drawOval(xx + ss/2-r, yy + ss/2-r, r*2, r*2);
        }
        if (square.isCeilingAccess())
        {
            mData.g.setColor(Color.BLACK);
            Stroke dashed = new BasicStroke(ss/12, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);
            mData.g.setStroke(dashed);
            int r = ss*7/24;
            mData.g.drawOval(xx + ss/2-r, yy + ss/2-r, r*2, r*2);
        }
        mData.g.setStroke(oldStroke);
    }
    
    private static void drawDoors()
    {
        Stroke oldStroke = mData.g.getStroke();
        mData.g.setColor(Color.BLACK);
        Stroke dashed = new BasicStroke(mData.ss()/12);
        mData.g.setStroke(dashed);
        int ss = mData.ss();
        int s = ss/12;
        for (int y = mData.dLower.y; y <= mData.dUpper.y; y++)
            for (int x = mData.dLower.x; x <= mData.dUpper.x; x++)
            {
                ShipSquareBean square = mData.ship.getSquare(x, y, mData.deck);
                if (square == null)
                    continue;
                if (square.getType() != ShipSquareBean.CORRIDOR)
                    continue;
                int xx = x(x);
                int yy = y(y);
                if (square.isPlusXAccess())
                {
                    mData.g.drawLine(xx + ss - s, yy + s, xx + ss + s, yy + s);
                    mData.g.drawLine(xx + ss + s, yy + s, xx + ss - s, yy + ss - s);
                    mData.g.drawLine(xx + ss - s, yy + ss - s, xx + ss + s, yy + ss + s);
                    mData.g.drawLine(xx + ss + s, yy + ss + s, xx + ss - s, yy + s);
                }
                if (square.isMinusXAccess())
                {
                    mData.g.drawLine(xx - s, yy + s, xx + s, yy + s);
                    mData.g.drawLine(xx + s, yy + s, xx - s, yy + ss - s);
                    mData.g.drawLine(xx - s, yy + ss - s, xx + s, yy + ss + s);
                    mData.g.drawLine(xx + s, yy + ss + s, xx - s, yy + s);
                }
                if (square.isPlusYAccess())
                {
                    mData.g.drawLine(xx + s, yy + ss - s, xx + s, yy + ss + s);
                    mData.g.drawLine(xx + s, yy + ss + s, xx + ss - s, yy + ss - s);
                    mData.g.drawLine(xx + ss - s, yy + ss - s, xx + ss + s, yy + ss + s);
                    mData.g.drawLine(xx + ss + s, yy + ss + s, xx + s, yy + ss - s);
                }
                if (square.isMinusYAccess())
                {
                    mData.g.drawLine(xx + s, yy - s, xx + s, yy + s);
                    mData.g.drawLine(xx + s, yy + s, xx + ss - s, yy - s);
                    mData.g.drawLine(xx + ss - s, yy - s, xx + ss + s, yy + s);
                    mData.g.drawLine(xx + ss + s, yy + s, xx + s, yy - s);
                }
            }
        mData.g.setStroke(oldStroke);
    }

    private static void addBoundaryLabels(BufferedImage img, int level)
    {
        if (mData.settings.getBoundaryGutter() <= 0)
            return;
        int gap = mData.g.getFontMetrics(mData.medFont).getHeight();
        int gut = mData.settings.getBoundaryGutter();
        int w = mData.horizontal ? img.getWidth() : img.getHeight();
        int h = mData.horizontal ? img.getHeight() : img.getWidth();
        LabelLogic.addLabel(new Rectangle(gut-gap, 0, gap, h), "Port", mData.medFont, LabelLogic.CENTER);
        LabelLogic.addLabel(new Rectangle(w - gut, 0, gap, h), "Starboard", mData.medFont, LabelLogic.CENTER);
        LabelLogic.addLabel(new Rectangle(0, gut - gap, w, gap), "Fore", mData.medFont, LabelLogic.CENTER);
        LabelLogic.addLabel(new Rectangle(0, h - gut, w, gap), "Aft", mData.medFont, LabelLogic.CENTER);
        LabelLogic.addLabel(new Rectangle(0, 0, w, gap), "Level "+level, mData.medFont, LabelLogic.RIGHT);
        LabelLogic.addLabel(new Rectangle(gap, h - gap, w/2, gap), "Traveller Tools Group Ship Planner", mData.smallFont, LabelLogic.LEFT);
    }
    
    private static void drawOutline(int type, String legend)
    {
        Map<ShipPlanPerimeterBean,Shape> perimeters = createComponentPerimiters(type, false);
        if (perimeters == null)
            return;
        int minArea = mData.ss()*mData.ss();
        for (ShipPlanPerimeterBean comp : perimeters.keySet())
        {
            Shape perimeter = perimeters.get(comp);
            if (mData.settings.isDrawInteriors())
                InteriorLogic.drawInterior(comp, perimeter);
            mData.g.setColor(Color.BLACK);
            mData.g.setStroke(new BasicStroke(mData.ss()/12));
            mData.g.draw(perimeter);
            Rectangle bounds = perimeter.getBounds();
            double area = bounds.getWidth()*bounds.getHeight();
            if (area > minArea)
                LabelLogic.addLabel(perimeter, legend, mData.smallFont, LabelLogic.CENTER);
        }
    }
    
    private static void drawComponentOutlines(int type, String legendNotes, String legendNoNotes)
    {
        Map<ShipPlanPerimeterBean,Shape> perimeters = createComponentPerimiters(type, false);
        if (perimeters == null)
            return;
        for (ShipPlanPerimeterBean comp : perimeters.keySet())
        {
            String l;
            if (comp.getNotes() != null)
                l = String.format(legendNotes, comp.getNotes(), comp.getCompartment());
            else
                l = String.format(legendNoNotes, comp.getNotes(), comp.getCompartment());
            Shape perimeter = perimeters.get(comp);
            if (mData.settings.isDrawInteriors())
                InteriorLogic.drawInterior(comp, perimeter);
            mData.g.setColor(Color.BLACK);
            mData.g.setStroke(new BasicStroke(mData.ss()/12));
            mData.g.draw(perimeter);
            LabelLogic.addLabel(perimeter, l, mData.smallFont, LabelLogic.CENTER);
        }
    }
    
    private static Shape drawHull(SparseMatrix<ShipSquareBean> level)
    {
        Set<Point3i> interior = new HashSet<>();
        for (Iterator<Point3i> i = mData.ship.getSquares().iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            if (p.z == mData.deck)
                interior.add(p);
        }
        Shape outer = createOutline(interior, true);
        if (outer == null)
            return null;
        mData.g.setColor(ShipPlanImageLogic.mData.settings.BACKGROUND_HULL);
        mData.g.fill(outer);
        return outer;
    }
    
    private static void drawGrid(Color gridColor)
    {
        mData.g.setColor(gridColor);
        Stroke thick = new BasicStroke(Math.max(mData.ss()/12, 1));
        Stroke thin = new BasicStroke(Math.max(mData.ss()/24, 1));
        for (int y = mData.dLower.y; y <= mData.dUpper.y + 1; y++)
        {
            int yy = y(y);
            mData.g.setStroke((y%5 == 0) ? thick : thin);
            mData.g.drawLine(x(mData.dLower.x), yy, x(mData.dUpper.x + 1), yy);            
            for (int x = mData.dLower.x; x <= mData.dUpper.x + 1; x++)
            {
                int xx = x(x);
                if (y == mData.dLower.y)
                {
                    mData.g.setStroke((x%5 == 0) ? thick : thin);
                    mData.g.drawLine(xx, yy, xx, y(mData.dUpper.y + 1));
                }
            }
        }
    }
    
    private static int x(int x)
    {
        return (x - mData.dLower.x)*mData.ss() + mData.settings.getBoundaryGutter();
    }
    
    private static int y(int y)
    {
        return (y - mData.dLower.y)*mData.ss() + mData.settings.getBoundaryGutter();
    }

    private static Path2D createOutline(Set<Point3i> interior, boolean smooth)
    {
        List<int[]> segments = findPerimiterSegments(interior);
        if (segments.size() == 0)
            return null;
        joinSegments(segments);
        if (segments.size() > 1)
        {
            System.err.println("Disconnected perimiter! "+segments.size());
            for (int i = 0; i < segments.size(); i++)
            {
                for (int j : segments.get(i))
                    System.err.print("  "+j);
                System.err.println();
            }
        }
        int[] segment = segments.get(0);
        segment = simplify(segment);
        if (smooth)
            segment = smooth(segment);
        Path2D p = new Path2D.Double();
        p.moveTo(x(segment[0]), y(segment[1]));
        for (int j = 2; j < segment.length; j += 2)
            p.lineTo(x(segment[j]), y(segment[j+1]));
        //p.lineTo(segment[0], segment[1]);
        return p;
    }
    
    private static List<int[]> findPerimiterSegments(Set<Point3i> interior)
    {
        List<int[]> segments = new ArrayList<>();
        for (Point3i p : interior)
        {
            if (!ShipPlanUtils.contains(interior, p.x + 1, p.y, p.z))
                segments.add(new int[] { p.x + 1, p.y, p.x + 1, p.y + 1 });
            if (!ShipPlanUtils.contains(interior, p.x - 1, p.y, p.z))
                segments.add(new int[] { p.x, p.y + 1, p.x, p.y });
            if (!ShipPlanUtils.contains(interior, p.x, p.y + 1, p.z))
                segments.add(new int[] { p.x + 1, p.y + 1, p.x, p.y + 1 });
            if (!ShipPlanUtils.contains(interior, p.x, p.y - 1, p.z))
                segments.add(new int[] { p.x, p.y, p.x + 1, p.y });
        }
        return segments;
    }
    
    private static Map<ShipPlanPerimeterBean,Shape> createComponentPerimiters(int type, boolean smooth)
    {
        Map<ShipPlanPerimeterBean, Shape> perimeters = new HashMap<>();
        List<ShipPlanComponentBean> components = mData.ship.getComponents(type);
        for (ShipPlanComponentBean component : components)
        {
            for (ShipPlanPerimeterBean sub : component.findSubComponents(mData.deck))
            {
                Path2D p = createOutline(sub.getSquares(), smooth);
                if (p == null)
                    continue;
                perimeters.put(sub, p);
            }
        }
        return perimeters;
    }

    private static void joinSegments(List<int[]> segments)
    {
        for (;;)
        {
            boolean anyChange = false;
            for (int i = 0; i < segments.size() - 1; i++)
            {
                int[] seg1 = segments.get(i);
                for (int j = i + 1; j < segments.size(); j++)
                {
                    int[] seg2 = segments.get(j);
                    if (follows(seg2, seg1))
                    {
                        seg1 = join(seg2, seg1);
                        segments.remove(j);
                        segments.remove(i);
                        segments.add(i, seg1);
                        j--;
                        anyChange = true;
                    }
                    else if (follows(seg1, seg2))
                    {
                        seg1 = join(seg1, seg2);
                        segments.remove(j);
                        segments.remove(i);
                        segments.add(i, seg1);
                        j--;
                        anyChange = true;
                    }
                }
            }
            if ((segments.size() == 0) || !anyChange)
                break;
        }
    }
    
    private static boolean follows(int[] seg1, int[] seg2)
    {
        return (seg1[seg1.length - 2] == seg2[0]) && (seg1[seg1.length - 1] == seg2[1]); 
    }
    
    private static int[] join(int[] seg1, int[] seg2)
    {
        int[] tmp = new int[seg1.length + seg2.length - 2];
        System.arraycopy(seg1, 0, tmp, 0, seg1.length);
        System.arraycopy(seg2, 2, tmp, seg1.length, seg2.length - 2);
        return tmp;
    }
    
    private static int[] simplify(int[] segIn)
    {
        int end = 4;
        for (int e = end; e < segIn.length; e += 2)
            if (isCollinear(segIn[end-4], segIn[end-3], segIn[end-2], segIn[end-1], segIn[e], segIn[e+1]))
            {
                segIn[end-2] = segIn[e];
                segIn[end-1] = segIn[e+1];
            }
            else
            {
                segIn[end] = segIn[e];
                segIn[end+1] = segIn[e+1];
                end += 2;
            }
        int[] segOut = new int[end];
        System.arraycopy(segIn, 0, segOut, 0, end);
        return segOut;
    }
    
    private static int[] smooth(int[] segIn)
    {
        //System.out.println("Before: "+ArrayUtils.toString(segIn));
        List<Integer> segs = ArrayUtils.toList(segIn);
        segs.remove(0); // get rid of duplicate
        segs.remove(0);
        for (int i = 0; i < segs.size(); i += 2)
        {
            int im = (i - 2 + segs.size())%segs.size();
            int ip = (i + 2)%segs.size();
            if (isLeftTriangle(segs.get(im), segs.get(im+1), segs.get(i), segs.get(i+1), segs.get(ip), segs.get(ip+1)))
            {
                segs.remove(i);
                segs.remove(i);
                i -= 2;
            }
        }
        segs.add(segs.get(0)); // add duplicate back in
        segs.add(segs.get(1));
        int[] segOut = ArrayUtils.toIntArray(segs);
        //System.out.println("After : "+ArrayUtils.toString(segOut));
        return segOut;
    }
    
    // assumes clockwise winding
    private static boolean isLeftTriangle(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        if ((x1 == x2) && (y2 == y3))
        {
            if ((y2 < y1) && (x3 < x2))
                return true;
            if ((y2 > y1) && (x3 > x2))
                return true;
            return false;
        }
        if ((y1 == y2) && (x2 == x3))
        {
            if ((x2 < x1) && (y3 > y2))
                return true;
            if ((x2 > x1) && (y3 < y2))
                return true;
            return false;
        }
        return false;
    }
    
    private static boolean isCollinear(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return ((x1 == x2) && (x2 == x3)) || ((y1 == y2) && (y2 == y3));
    }
}
