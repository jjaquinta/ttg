package jo.ttg.ship.logic.plan;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.utils.MathUtils;

public class ShipPlanHullLogic
{
// volume in cubic meters
    public static void generateHull(ShipPlanBean plan, int volume, int configuration)
    {
        switch (configuration)
        {
            case Hull.HULL_NEEDLE:
                generateNeedle(plan, volume);
                break;
            case Hull.HULL_CONE:
                generateCone(plan, volume);
                break;
            case Hull.HULL_SPHERE:
                generateSphere(plan, volume);
                break;
            case Hull.HULL_DOME:
                generateDome(plan, volume);
                break;
            case Hull.HULL_CYLINDER:
                generateCylinder(plan, volume);
                break;
            case Hull.HULL_BOX:
                generateBox(plan, volume);
                break;
            case Hull.HULL_IRREGULAR:
            case Hull.HULL_OPEN_FRAME:
                generateIrregular(plan, volume);
                break;
            default:
                throw new IllegalArgumentException("Unhandled hull type: "+configuration+" / "+Hull.hullDescription[configuration]);
        }
    }
    
    // volume in cubic meters
    private static void generateSphere(ShipPlanBean plan, int volume)
    {
        double r = Math.pow(volume*3/4/Math.PI, .33333);
        int decks = (int)Math.ceil(r*2/3);
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = z*3 + 1.5;
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(deckRadius/1.5);
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
                for (int y = -deckRadiusSquares; y <= deckRadiusSquares; y++)
                {
                    double xm = x*1.5;
                    double ym = y*1.5;
                    double rm = Math.sqrt(xm*xm + ym*ym);
                    if (rm <= deckRadius)
                        plan.setSquare(new ShipSquareBean(x, y, z, ShipSquareBean.UNSET));
                }
        }
    }

    private static void generateDome(ShipPlanBean plan, int volume)
    {
        double r = Math.pow(volume*2*3/4/Math.PI, .33333);
        int decks = (int)Math.ceil(r/3);
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = (z - ventral)*3 + 1.5;
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(deckRadius/1.5);
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
                for (int y = -deckRadiusSquares; y <= deckRadiusSquares; y++)
                {
                    double xm = x*1.5;
                    double ym = y*1.5;
                    double rm = Math.sqrt(xm*xm + ym*ym);
                    if (rm <= deckRadius)
                        plan.setSquare(new ShipSquareBean(x, y, z, ShipSquareBean.UNSET));
                }
        }
    }

    // volume in cubic meters
    private static void generateCylinder(ShipPlanBean plan, int volume)
    {
        double r = Math.pow(volume/8/Math.PI, .33333);
        double h = r*8;
        int decks = (int)Math.ceil(r*2/3);
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int length = (int)Math.ceil(h/1.5);
        int fore = -length/2;
        int aft = fore + length;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = z*3;
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(deckRadius/1.5);
            for (int x = -deckRadiusSquares; x <= deckRadiusSquares; x++)
                for (int y = fore; y < aft; y++)
                    plan.setSquare(new ShipSquareBean(x, y, z, ShipSquareBean.UNSET));
        }
    }

    // volume in cubic meters
    private static void generateNeedle(ShipPlanBean plan, int volume)
    {
        double r = Math.cbrt((3*volume/2)/(Math.PI*8));
        double h = r*8;
        int decks = (int)Math.ceil(r*2/3);
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int length = (int)Math.ceil(h/1.5);
        int fore = -length;
        int aft = fore + length*2;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = z*3;
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(deckRadius/1.5);
            int f = (int)MathUtils.interpolate(Math.abs(z), dorsal + 1, 0, 0, fore);
            int a = (int)MathUtils.interpolate(Math.abs(z), dorsal + 1, 0, 0, aft);
            for (int y = f; y < a; y++)
            {
                int deckWidth;
                if (y < 0)
                    deckWidth = (int)MathUtils.interpolate(y, f, 0, 0, deckRadiusSquares);
                else
                    deckWidth = (int)MathUtils.interpolate(y, a, 0, 0, deckRadiusSquares);
                for (int x = -deckWidth; x <= deckWidth; x++)
                    plan.setSquare(new ShipSquareBean(x, y, z, ShipSquareBean.UNSET));
            }
        }
    }

    // volume in cubic meters
    private static void generateCone(ShipPlanBean plan, int volume)
    {
        double r = Math.cbrt((3*volume)/(Math.PI*8));
        double h = r*8;
        int decks = (int)Math.ceil(r*2/3);
        if (decks == 0)
            decks = 1;
        int ventral = -decks/2;
        int dorsal = ventral + decks;
        int length = (int)Math.ceil(h/1.5);
        int fore = -length/2;
        int aft = fore + length;
        for (int z = ventral; z < dorsal; z++)
        {
            double elevation = z*3;
            double deckRadius = Math.sqrt(r*r - elevation*elevation);
            int deckRadiusSquares = (int)Math.ceil(deckRadius/1.5);
            int f = (int)MathUtils.interpolate(Math.abs(z), dorsal + 1, 0, 0, fore);
            int a = aft;
            for (int y = f; y < a; y++)
            {
                int deckWidth = (int)MathUtils.interpolate(y, f, a, 0, deckRadiusSquares);
                for (int x = -deckWidth; x <= deckWidth; x++)
                    plan.setSquare(new ShipSquareBean(x, y, z, ShipSquareBean.UNSET));
            }
        }
    }

    // volume in cubic meters
    private static void generateBox(ShipPlanBean plan, int volume)
    {
        double t = Math.cbrt(volume/64.0);
        int decks = (int)Math.ceil(2*t/3);
        if (decks == 0)
            decks = 1;
        double deckArea = (volume/decks)/3;
        int width = (int)Math.ceil(Math.sqrt(deckArea/2)/1.5);
        int length = width*2;
        for (int deck = 1; deck <= decks; deck++)
        {
            for (int x = 0; x < width; x++)
                for (int y = 0; y < length; y++)
                    plan.setSquare(new ShipSquareBean(x - width/2, y - length/2, deck, ShipSquareBean.UNSET));
        }
    }
    
    private static void markSphere(Point3i p, double r, int rxy, int rz, ShipPlanBean plan)
    {
        for (int dx = -rxy; dx <= rxy; dx++)
            for (int dy = -rxy; dy <= rxy; dy++)
                for (int dz = -rz; dz <= rz; dz++)
                {
                    double pr = Math.sqrt(dx*1.5*dx*1.5 + dy*1.5*dy*1.5 + dz*3*dz*3);
                    if (pr > r)
                        continue;
                    if (plan.getSquare(p.x + dx, p.y + dy, p.z + dz) == null)
                        plan.setSquare(new ShipSquareBean(p.x + dx, p.y + dy, p.z + dz, ShipSquareBean.UNSET));
                }
    }
    
    private static void addIrregular(Point3i p, Set<Point3i> available, ShipPlanBean plan, double radius)
    {
        available.remove(p);
        //plan.setSquare(p, new ShipSquareBean(ShipSquareBean.UNSET));
        int rxy = (int)Math.max(radius/1.5/2, 1);
        int rz = (int)Math.max(radius/3/2, 1);
        markSphere(p, radius, rxy-1, rz-1, plan);
        if (plan.getSquare(p.x+rxy, p.y, p.z) == null)
            available.add(new Point3i(p.x+rxy, p.y, p.z));
        if (plan.getSquare(p.x-rxy, p.y, p.z) == null)
            available.add(new Point3i(p.x-rxy, p.y, p.z));
        if (plan.getSquare(p.x, p.y+rxy, p.z) == null)
            available.add(new Point3i(p.x, p.y+rxy, p.z));
        if (plan.getSquare(p.x, p.y-rxy, p.z) == null)
            available.add(new Point3i(p.x, p.y-rxy, p.z));
        if (plan.getSquare(p.x, p.y, p.z+rz) == null)
            available.add(new Point3i(p.x, p.y, p.z+rz));
        if (plan.getSquare(p.x, p.y, p.z-rz) == null)
            available.add(new Point3i(p.x, p.y, p.z-rz));
    }

    private static Point3i pickIrregular(Set<Point3i> available, Random rnd)
    {
        Point3i[] points = available.toArray(new Point3i[0]);
        return points[rnd.nextInt(points.length)];
    }
    
    private static final int SMOOTHNESS = 4;
    
    private static boolean smoothIrregular(ShipPlanBean plan)
    {
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        boolean addedAny = false;
        for (Iterator<Point3i> i = plan.getSquares().iterator(); i.hasNext(); )
        {
            Point3i p = i.next();
            ShipSquareBean sq = plan.getSquare(p);
            if (sq != null)
                continue;
            int touching = 0;
            for (Point3i q : ShipPlanUtils.getNeighbors(p, true))
                if (plan.getSquare(q) != null)
                    touching++;
            if (touching >= SMOOTHNESS)
            {
                plan.setSquare(new ShipSquareBean(p, ShipSquareBean.UNSET));
                addedAny = true;
            }
        }
        return addedAny;
    }
    
    private static void generateIrregular(ShipPlanBean plan, int volume)
    {
        plan.println("Irregular gen vol="+volume);
        double r = Math.pow(volume*3/4/Math.PI, .33333);
        r /= 12;
        long tick1 = System.currentTimeMillis();
        Random rnd = new Random();
        Set<Point3i> available = new HashSet<>();
        int squares = (int)(volume/6.75);
        addIrregular(new Point3i(0, 0, 0), available, plan, r);
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        while (plan.getSquares().size() < squares)
        {
            Point3i candidate = pickIrregular(available, rnd);
            plan.getSquares().getBounds(lower, upper);
            if ((candidate.z < lower.z) || (candidate.z > upper.z))
                if (rnd.nextInt(24) > 0)
                    continue;
            addIrregular(candidate, available, plan, r);
        }
        long tick2 = System.currentTimeMillis();
        plan.println("Irregular add - "+((tick2 - tick1)/1000L));
        while (smoothIrregular(plan))
            ;
        long tick3 = System.currentTimeMillis();
        plan.println("Irregular smooth - "+((tick3 - tick2)/1000L));
    }
}
