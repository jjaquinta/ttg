package jo.ttg.core.ui.swing.surf;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RadialGradientPaint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceAnnotationBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.util.geom2d.Line2D;
import jo.util.geom2d.Line2DLogic;
import jo.util.geom2d.Point2D;
import jo.util.heal.IHEALCoord;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.utils.MathUtils;

public class MapPainterLogic extends CommonPainterLogic
{
    private static final int DESIGN_SIZE = 32;
    
    // globals thread safe because of synchronization
    private static SurfaceBean  mSurface;
    private static Collection<SurfaceAnnotationBean> mNotes;
    private static MapHexBean[][] mGrid;
    private static List<Point> mPerimeter;
    private static List<List<Point>> mOceans;
    private static List<List<Point>> mIcePack;
    private static List<List<Point>> mDepths;
    private static List<List<Point>> mForest;
    private static List<List<Point>> mDesert;
    private static List<Point> mLand = new ArrayList<>();
    private static List<Point> mIceBergs = new ArrayList<>();
    private static List<Point> mMountains = new ArrayList<>();
    private static List<Point> mVolcanos = new ArrayList<>();
    private static List<Point> mHills = new ArrayList<>();
    private static boolean isOceans;
    private static int mSurfaceType;
    private static Random mRND;
    private static int mGridEdge;
    private static Line2D mEquator;
    private static double mLowPoint;
    private static double mHighPoint;
    
    private static Graphics2D mG;
    
    public static synchronized BufferedImage paintMap(SurfaceBean surface, int width, int height, Map<MapHexBean, Shape> positionCache)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        paintMap((Graphics2D)img.getGraphics(), surface, width, height, false, positionCache);
        return img;
    }
    public static synchronized void paintMap(Graphics2D g, SurfaceBean surface, int width, int height, boolean simple, Map<MapHexBean, Shape> positionCache)
    {
        mG = g;
        mSurface = surface;
        Map<SurfaceAnnotationBean,Point> notes = new HashMap<>();
        mGrid = SurfaceLogic.surfaceToGrid(surface, notes);
        mNotes = notes.keySet();
        BodyWorldBean world = (BodyWorldBean)surface.getBody();
        mSurfaceType = getSurfaceType(world);
        mRND = new Random(0);
        mG.setColor(ColorUtils.BlanchedAlmond);
        mG.fillRect(0, 0, width, height);
        calculateGeometry();
        double scale = width/(x(mGridEdge*5)*1.414);
        mG.scale(scale, scale);
        mG.translate(0, y(mGridEdge*2)*.404);
        mG.rotate(-Math.PI/4);
        categorize();
        drawAreas(simple);
        drawItems();
        //drawGrid();
        drawNotes(notes);
        calculatePositions(positionCache);
        mG.dispose();
    }

    private static void calculatePositions(Map<MapHexBean, Shape> positionCache)
    {
        if (positionCache == null)
            return;
        positionCache.clear();
        AffineTransform t = mG.getTransform();
        for (int i = 0; i < mGrid.length; i++)
            for (int j = 0; j < mGrid[i].length; j++)
            {
                MapHexBean hex = mGrid[i][j];
                if (hex == null)
                    continue;
                Rectangle2D in = new Rectangle2D.Double(x(i), y(j), DESIGN_SIZE, DESIGN_SIZE);
                Shape out = t.createTransformedShape(in);
                positionCache.put(hex, out);
            }
    }
    private static void calculateGeometry()
    {
        mGridEdge = mGrid.length/4;
        mEquator = new Line2D(new Point2D(-mGridEdge, 0), new Point2D(mGridEdge*5, mGridEdge*6));
    }
    
    // 1 = north/south pole, 0 = equator
    private static double lattitude(Point p)
    {
        return Math.abs(Line2DLogic.distanceToSegment(p.x, p.y, mEquator)/(mGridEdge*1.414));
    }
    
    private static double altitude(Point p)
    {
        return mGrid[p.x][p.y].getAltitude();
    }
    
    private static void drawNotes(Map<SurfaceAnnotationBean,Point> notes)
    {
        mG.setFont(new Font(Font.DIALOG, Font.ITALIC, DESIGN_SIZE));
        FontMetrics fm = mG.getFontMetrics();
        List<Shape> taken = new ArrayList<>();
        AffineTransform oldTransform = mG.getTransform();
        for (SurfaceAnnotationBean note : notes.keySet())
        {
            String name = getName(note);
            if (name == null)
                continue;
            Point p = notes.get(note);
            double x = x(p.x) + DESIGN_SIZE/2;
            double y = y(p.y) + DESIGN_SIZE/2;
            AffineTransform newTransform = new AffineTransform();
            newTransform.translate(x, y);
            newTransform.rotate(Math.PI/4);
            Rectangle2D r = fm.getStringBounds(name, mG);
            //Rectangle2D text = new Rectangle2D.Double(-r.getWidth()/2, -fm.getHeight()-4, r.getWidth(), r.getHeight());
            Rectangle2D text = findUntakenLocation(taken, r.getWidth(), r.getHeight(), 8, newTransform);
            Rectangle2D dot = new Rectangle2D.Double(x - 4, y - 4, 8, 8);
            mG.transform(newTransform);
            mG.setColor(COLOR_NOTE_BACK);
            mG.fill(text);
            mG.setColor(COLOR_NOTE_FORE);
            mG.draw(text);
            mG.drawString(name, (float)text.getX(), (float)text.getY() + fm.getAscent());
            mG.setTransform(oldTransform);
            mG.fill(dot);
        }
    }
    
    private final static double[] DELTA_X_W = new double[] { -.5,-.5,-1 , 0};
    private final static double[] DELTA_X_G = new double[] { 0  ,0  ,-1 , 1};
    private final static double[] DELTA_Y_H = new double[] {-1  ,0  ,-.5,-.5};
    private final static double[] DELTA_Y_G = new double[] {-1  ,1  ,0  , 0};
    
    private static Rectangle2D findUntakenLocation(List<Shape> taken, double w, double h, double gap,
            AffineTransform trans)
    {
        for (int i = 0; i < DELTA_X_W.length; i++)
        {
            Rectangle2D rect = new Rectangle2D.Double(DELTA_X_W[i]*w + DELTA_X_G[i]*gap, DELTA_Y_H[i]*h + DELTA_Y_G[i]*gap, w, h);
            Shape s = trans.createTransformedShape(rect);
            if (!isIntersection(s, taken) || (i == DELTA_X_W.length - 1))
            {
                taken.add(s);
                return rect;
            }
        }
        return null; // should never get here
    }
    
    private static boolean isIntersection(Shape rect,
            List<Shape> taken)
    {
        Rectangle2D bounds = rect.getBounds();
        for (Shape r : taken)
            if (r.intersects(bounds))
                return true;
        return false;
    }

    private static String getName(SurfaceAnnotationBean note)
    {
        if ((note.getType() == SurfaceAnnotationBean.ISLANDS) || (note.getType() == SurfaceAnnotationBean.LAKES))
            return null;
        return note.getDescription();
    }
    
    private static void drawAreas(boolean simple)
    {
        if (isOceans)
        {
            //mG.setColor(ColorUtils.LawnGreen);
            //mG.fill(toPath(mPerimeter));
            mG.setClip(toPath(mPerimeter));
            drawLand(simple);
            mG.setClip(null);
            mG.setColor(COLOR_OCEAN);
            for (List<Point> path : mOceans)
            {
                mG.fill(toSmoothPath(path));
            }
        }
        else
        {
            mG.setColor(COLOR_OCEAN);
            mG.fill(toPath(mPerimeter));
            //mG.setColor(ColorUtils.LawnGreen);
            //for (List<Point> path : mOceans)
            //    mG.fill(toSmoothPath(path));
            Path2D landClip = new Path2D.Double();
            for (List<Point> path : mOceans)
                landClip.append(toSmoothPath(path), false);
            mG.setClip(landClip);
            drawLand(simple);
            mG.setClip(null);
        }
        mG.setColor(COLOR_DEPTHS);
        for (List<Point> path : mDepths)
            mG.fill(toSmoothPath(path));
        mG.setColor(COLOR_ICEPACK);
        for (List<Point> path : mIcePack)
            mG.fill(toSmoothPath(path));
        mG.setColor(COLOR_DESERT);
        for (List<Point> path : mDesert)
            mG.fill(toSmoothPath(path));
        mG.setColor(COLOR_FOREST);
        for (List<Point> path : mForest)
            mG.fill(toSmoothPath(path));
    }
    
    private static void drawItems()
    {
        for (Point p : mIceBergs)
        {
            double l = lattitude(p);
            if (mRND.nextDouble() < MathUtils.interpolate(l, 0, 1, .25, .75))
                draw(p, ICEBERG);
        }
        for (Point p : mMountains)
        {
            double a = altitude(p);
            if (mRND.nextDouble() < MathUtils.interpolate(a, mLowPoint, mHighPoint, .25, .75))
                draw(p, MOUNTAIN);
        }
        for (Point p : mHills)
        {
            double a = altitude(p);
            if (mRND.nextDouble() < MathUtils.interpolate(a, mLowPoint, mHighPoint, .25, .75))
                draw(p, HILL);
        }
        for (Point p : mVolcanos)
        {
            draw(p, VOLCANO);
        }
    }
    
    private static void draw(Point p, BufferedImage img)
    {
        double x1 = x(p);
        double y1 = y(p);
        double x2 = x(p.x + 1);
        double y2 = y(p.y + 1);
        mG.drawImage(img, (int)x1, (int)y1, (int)x2, (int)y2, 0, 0, img.getWidth(), img.getHeight(), null);
    }
    
    private static Color[] getColorAltitude()
    {
        switch (mSurfaceType)
        {
            case SURFACE_VERDANT:
                return COLOR_ALTITUDE_VERDANT;
            case SURFACE_ICY:
                return COLOR_ALTITUDE_ICY;
            case SURFACE_DESOLATE:
                return COLOR_ALTITUDE_DESOLATE;
            case SURFACE_BARREN:
                return COLOR_ALTITUDE_BARREN;
        }
        throw new IllegalArgumentException("Unhandled surface type "+mSurfaceType);
    }
    
    private static void drawLand(boolean simple)
    {
        Color[] COLOR_ALTITUDE = getColorAltitude();
        Color[][] colors = new Color[mGrid.length+1][mGrid[0].length+1];
        for (Point p : mLand)
            calcColor(p.x, p.y, mGrid[p.x][p.y], colors, COLOR_ALTITUDE);
        for (Point p : mLand)
        {
            double x = x(p);
            double y = y(p);
            double width = x(p.x + 1) - x;
            double height = y(p.y + 1) - y;
            Color upperLeft = colors[p.x][p.y];
            Color upperRight = colors[p.x+1][p.y];
            Color lowerLeft = colors[p.x][p.y+1];
            Color lowerRight = colors[p.x+1][p.y+1];
            if (!simple && isGradient(upperLeft, upperRight, lowerLeft, lowerRight))
                paintGradient(upperLeft, upperRight, lowerLeft, lowerRight, x, y, width, height);
            else
            {
                mG.setColor(upperLeft);
                mG.fill(new Rectangle2D.Double(x, y, width, height));
            }
        }
    }

    private static void calcColor(int x, int y, MapHexBean hex, Color[][] colors,
            Color[] COLOR_ALTITUDE)
    {
        if (colors[x][y] != null)
            return;
        if (isSea(hex))
            return;
        double idx = MathUtils.interpolate(hex.getAltitude(), mLowPoint, mHighPoint, 0, COLOR_ALTITUDE.length-1);
        Color lowColor = COLOR_ALTITUDE[(int)Math.floor(idx)];
        Color highColor = COLOR_ALTITUDE[(int)Math.ceil(idx)];
        Color thisColor = ColorUtils.interpolate(idx, Math.floor(idx), Math.ceil(idx), lowColor, highColor);
        colors[x][y] = thisColor;
        if (get(mGrid, x, y) == null)
            return;
        MapHexBean right = get(mGrid, x+1, y);
        if (right != null)
            calcColor(x+1, y, right, colors, COLOR_ALTITUDE);
        else if (x + 1 < colors.length)
        {
            right = mSurface.getGlobe().get(hex.getLocation().next(IHEALCoord.D_NORTHEAST));
            calcColor(x+1, y, right, colors, COLOR_ALTITUDE);
        }
        MapHexBean down = get(mGrid, x, y+1);
        if (down != null)
            calcColor(x, y+1, down, colors, COLOR_ALTITUDE);
        else if (y + 1 < colors[0].length)
        {
            down = mSurface.getGlobe().get(hex.getLocation().next(IHEALCoord.D_SOUTHEAST));
            calcColor(x, y+1, down, colors, COLOR_ALTITUDE);
        }
    }
    
    private static MapHexBean get(MapHexBean[][] array, int x, int y)
    {
        if (x >= array.length)
            return null;
        if (y >= array[0].length)
            return null;
        return array[x][y];
    }
    
    private static boolean isGradient(Color upperLeft, Color upperRight, Color lowerLeft, Color lowerRight)
    {
        if ((upperLeft == null) || (upperRight == null) || (lowerLeft == null) || (lowerRight == null))
            return false;
        if (upperLeft.equals(upperRight) && upperLeft.equals(lowerLeft) && upperLeft.equals(lowerRight))
            return false;
        return true;
    }
    
    private static void categorize()
    {
        Set<Point> all = new HashSet<>();
        Set<Point> land = new HashSet<>();
        Set<Point> sea = new HashSet<>();
        Set<Point> summerIce = new HashSet<>();
        Set<Point> depths = new HashSet<>();
        Set<Point> forest = new HashSet<>();
        Set<Point> desert = new HashSet<>();
        mLand.clear();
        mIceBergs.clear();
        mMountains.clear();
        mHills.clear();
        mVolcanos.clear();
        boolean namedDeserts = false;
        for (SurfaceAnnotationBean note : mNotes)
            if (note.getType() == SurfaceAnnotationBean.DESERT)
                namedDeserts = true;
        for (int x = 0; x < mGrid.length; x++)
            for (int y = 0; y < mGrid[x].length; y++)
                if (mGrid[x][y] != null)
                {
                    Point p = new Point(x, y);
                    MapHexBean hex = mGrid[x][y];
                    all.add(p);
                    if (isSea(hex))
                        sea.add(p);
                    else
                    {
                        if (land.size() == 0)
                        {
                            mLowPoint = hex.getAltitude();
                            mHighPoint = hex.getAltitude();
                        }
                        else
                        {
                            mLowPoint = Math.min(mLowPoint, hex.getAltitude());
                            mHighPoint = Math.max(mHighPoint, hex.getAltitude());
                        }
                        land.add(p);
                    }
                    switch (hex.getCover())
                    {
                        case MapHexBean.C_SICE:
                            summerIce.add(p);
                            break;
                        case MapHexBean.C_DEEP:
                            depths.add(p);
                            break;
                        case MapHexBean.C_WICE:
                            mIceBergs.add(p);
                            break;
                        case MapHexBean.C_MTNS:
                            mMountains.add(p);
                            break;
                        case MapHexBean.C_VOLC:
                            mVolcanos.add(p);
                            break;
                        case MapHexBean.C_ROUGH:
                            mHills.add(p);
                            break;
                        case MapHexBean.C_FOREST:
                        case MapHexBean.C_JUNGLE:
                            forest.add(p);
                            break;
                        case MapHexBean.C_DESERT:
                            if (namedDeserts)
                                desert.add(p);
                            break;
                    }
                }
        mPerimeter = createOutline(all);
        if (land.size() > sea.size())
        {
            isOceans = true;
            mOceans = createOutlines(sea);
        }
        else
        {
            isOceans = false;
            mOceans = createOutlines(land);
        }
        mLand.addAll(land);
        mIcePack = createOutlines(summerIce);
        mDepths = createOutlines(depths);
        mDesert = createOutlines(desert);
        mForest = createOutlines(forest);
    }

    private static double x(int ix)
    {
        return ix*DESIGN_SIZE;
    }

    private static double y(int iy)
    {
        return iy*DESIGN_SIZE;
    }

    private static double x(Point p)
    {
        return x(p.x);
    }

    private static double y(Point p)
    {
        return y(p.y);
    }
    
    private static void moveTo(Path2D path, Point p)
    {
        //System.out.print("m("+p.x+","+p.y+")");
        path.moveTo(x(p), y(p));
    }
    
    private static void lineTo(Path2D path, Point p)
    {
        //System.out.print("l("+p.x+","+p.y+")");
        path.lineTo(x(p), y(p));
    }
    
    @SuppressWarnings("unused")
    private static void curveTo(Path2D path, Point p1, Point p2, Point p3)
    {
        //System.out.print("c("+p1.x+","+p1.y+" "+p2.x+","+p2.y+" "+p3.x+","+p3.y+")");
        path.curveTo(x(p1), y(p1), x(p2), y(p2), x(p3), y(p3));
    }
    
    private static void moveTo(Path2D path, Point2D p)
    {
        //System.out.print("m("+p.x+","+p.y+")");
        path.moveTo(p.x, p.y);
    }
    
    private static void lineTo(Path2D path, Point2D p)
    {
        //System.out.print("l("+p.x+","+p.y+")");
        path.lineTo(p.x, p.y);
    }
    
    private static void curveTo(Path2D path, Point2D p1, Point2D p2, Point2D p3)
    {
        //System.out.print("c("+p1.x+","+p1.y+" "+p2.x+","+p2.y+" "+p3.x+","+p3.y+")");
        path.curveTo(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    private static Path2D toPath(List<Point> segment)
    {
        Path2D p = new Path2D.Double();
        moveTo(p, segment.get(0));
        for (int j = 1; j < segment.size(); j++)
        {
            Point pp = segment.get(j);
            lineTo(p, pp);
        }
        return p;
    }

    private static Path2D toSmoothPath(List<Point> segment)
    {
        List<Point2D> points = new ArrayList<>();
        for (Point p : segment)
            points.add(make(p));
        Path2D p = new Path2D.Double();
        boolean first = true;
        for (int j = 0; j < points.size(); j++)
        {
            Point s0 = segment.get(j);
            Point s1 = segment.get((j+1)%segment.size());
            Point s2 = segment.get((j+2)%segment.size());
            Point2D p0 = points.get(j);
            Point2D p1 = points.get((j+1)%points.size());
            Point2D p2 = points.get((j+2)%points.size());
            Point2D p01 = tween(p0, p1);
            Point2D p12 = tween(p1, p2);
            if (first)
            {
                moveTo(p, p01);
                first = false;
            }
            if (mPerimeter.contains(s0) || mPerimeter.contains(s1) || mPerimeter.contains(s2) || isCollinear(s0, s1, s2))
            {
                lineTo(p, p1);
                lineTo(p, p12);
            }
            else
            {
                Point2D c1 = tween(p01, p1);
                Point2D c2 = tween(p1, p12);
                curveTo(p, c1, c2, p12);
            }
        }
        return p;
    }
    
    private static Point2D make(Point p)
    {
        return new Point2D(x(p.x), y(p.y));
    }
    
    private static Point2D tween(Point2D p1, Point2D p2)
    {
        return new Point2D((p1.x + p2.x)/2, (p1.y + p2.y)/2);
    }
    
    private static List<Point> createOutline(Set<Point> interior)
    {
        return createOutlines(interior).get(0);
    }
    
    private static List<List<Point>> createOutlines(Set<Point> interior)
    {
        List<int[]> segments = findPerimiterSegments(interior);
        joinSegments(segments);
        List<List<Point>> ret = new ArrayList<>();
        for (int[] segment : segments)
        {
            //segment = simplify(segment);
            List<Point> p = new ArrayList<Point>();
            for (int j = 0; j < segment.length; j += 2)
                p.add(new Point(segment[j], segment[j+1]));
            if (p.get(0).equals(p.get(p.size() - 1)))
                p.remove(p.size() - 1);
            ret.add(p);
        }
        return ret;
    }
    
    private static List<int[]> findPerimiterSegments(Set<Point> interior)
    {
        List<int[]> segments = new ArrayList<>();
        for (Point p : interior)
        {
            if (!interior.contains(new Point(p.x + 1, p.y)))
                segments.add(new int[] { p.x + 1, p.y, p.x + 1, p.y + 1 });
            if (!interior.contains(new Point(p.x - 1, p.y)))
                segments.add(new int[] { p.x, p.y + 1, p.x, p.y });
            if (!interior.contains(new Point(p.x, p.y + 1)))
                segments.add(new int[] { p.x + 1, p.y + 1, p.x, p.y + 1 });
            if (!interior.contains(new Point(p.x, p.y - 1)))
                segments.add(new int[] { p.x, p.y, p.x + 1, p.y });
        }
        return segments;
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
    
    private static boolean isCollinear(int x1, int y1, int x2, int y2, int x3, int y3)
    {
        return ((x1 == x2) && (x2 == x3)) || ((y1 == y2) && (y2 == y3));
    }
    
    private static boolean isCollinear(Point p1, Point p2, Point p3)
    {
        return isCollinear(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y);
    }

    private static boolean isSea(MapHexBean hex)
    {
        switch (hex.getCover())
        {
            case MapHexBean.C_DEEP:
            case MapHexBean.C_SICE:
            case MapHexBean.C_WATER:
            case MapHexBean.C_WICE:
                return true;                
        }
        return false;
    }
    
    private static void paintGradient(Color leftTop, Color rightTop, Color leftBottom, Color rightBottom, double x, double y, double w, double h)
    {
        GradientPaint twoColorGradient = new GradientPaint(
                (float)(x + w), (float)y, rightTop, (float)x, (float)(y + h), leftBottom);

        float radius = (float)(w-(w/4));
        float[] dist = {0f, 1.0f};
        java.awt.geom.Point2D center = new java.awt.geom.Point2D.Double(x, y);
        Color noColor = new Color(leftTop.getRed(), leftTop.getGreen(), leftTop.getBlue(), 0);
        Color[] colors = {leftTop, noColor};
        RadialGradientPaint thirdColor = new RadialGradientPaint(center, radius, dist, colors); 

        center = new java.awt.geom.Point2D.Double(x+w, y+h);
        noColor = new Color(rightBottom.getRed(), rightBottom.getGreen(), rightBottom.getBlue(), 0);
        Color[] colors2 = {rightBottom, noColor};
        RadialGradientPaint fourthColor = new RadialGradientPaint(center, radius, dist, colors2);

        Rectangle2D.Double r = new Rectangle2D.Double(x, y, w, h);
        mG.setPaint(twoColorGradient);
        mG.fill(r);

        mG.setPaint(thirdColor);
        mG.fill(r);

        mG.setPaint(fourthColor);
        mG.fill(r);
    }
}
