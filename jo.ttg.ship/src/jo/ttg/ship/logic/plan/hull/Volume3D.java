package jo.ttg.ship.logic.plan.hull;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public abstract class Volume3D
{
    protected Point3D   mAspectRatio = new Point3D(1, 1, 1);
    
    public abstract double   getVolume();
    public abstract Block3D getBounds();
    public abstract boolean isInside(Point3D p);
    
    public boolean isInside(double x, double y, double z)
    {
        return isInside(new Point3D(x, y, z));
    }
    
    public double getVolumeDecks(int orientation, List<Point3i> points)
    {
        int[] axis = null;
        double xSize = 1.5;
        double ySize = 1.5;
        double zSize = 1.5;
        switch (orientation)
        {
            case ShipScanBean.XP:
            case ShipScanBean.XM:
                axis = new int[] { 0, 1, 2 };
                zSize = 3;
                break;
            case ShipScanBean.YP:
            case ShipScanBean.YM:
                axis = new int[] { 1, 2, 0 };
                xSize = 3;
                break;
            case ShipScanBean.ZP:
            case ShipScanBean.ZM:
                axis = new int[] { 2, 0, 1 };
                ySize = 3;
                break;
        }
        Block3D bounds = getBounds();
        int xl = (int)Math.floor(bounds.getLower(axis[0])/1.5);
        int xu = (int)Math.ceil(bounds.getUpper(axis[0])/1.5);
        int yl = (int)Math.floor(bounds.getLower(axis[1])/1.5);
        int yu = (int)Math.ceil(bounds.getUpper(axis[1])/1.5);
        int zl = (int)Math.floor(bounds.getLower(axis[2])/3.0);
        int zu = (int)Math.ceil(bounds.getUpper(axis[2])/3.0);
        int chunks = 0;
        int[] p = new int[3];
        for (p[axis[0]] = xl; p[axis[0]] <= xu; p[axis[0]]++)
            for (p[axis[1]] = yl; p[axis[1]] <= yu; p[axis[1]]++)
            {
                Integer start = null;
                for (p[axis[2]] = zl; p[axis[2]] <= zu; p[axis[2]]++)
                    if (isInside(p[0]*xSize, p[1]*ySize, p[2]*zSize))
                    {
                        start = p[axis[2]];
                        break;
                    }
                if (start == null)
                    continue; // empty row
                Integer finish = null;
                for (p[axis[2]] = zu; p[axis[2]] >= zl; p[axis[2]]--)
                    if (isInside(p[0]*xSize, p[1]*ySize, p[2]*zSize))
                    {
                        finish = p[axis[2]];
                        break;
                    }
                chunks += (finish.intValue() - start.intValue()) + 1;
                if (points != null)
                    for (p[axis[2]] = start.intValue(); p[axis[2]] <= finish.intValue(); p[axis[2]]++)
                    points.add(mapPoint(p[0], p[1], p[2], orientation));
            }
        return chunks*1.5*1.5*3.0;
    }
    
    public double getVolumeDecksOld(int orientation, List<Point3i> points)
    {
        double xSize = 1.5;
        double ySize = 1.5;
        double zSize = 1.5;
        switch (orientation)
        {
            case ShipScanBean.XP:
            case ShipScanBean.XM:
                zSize = 3;
                break;
            case ShipScanBean.YP:
            case ShipScanBean.YM:
                xSize = 3;
                break;
            case ShipScanBean.ZP:
            case ShipScanBean.ZM:
                ySize = 3;
                break;
        }
        Block3D bounds = getBounds();
        if (bounds.getWidth()*bounds.getHeight()*bounds.getDepth() < 30*30*30)
            return getVolumeWithinBounds(orientation, points, xSize, ySize, zSize,
                bounds);
        else
            return getVolumeWalk(orientation, points, xSize, ySize, zSize,
                bounds);
    }
    protected double getVolumeWithinBounds(int orientation,
            List<Point3i> points, double xSize, double ySize, double zSize,
            Block3D bounds)
    {
        int xl = (int)Math.floor(bounds.getXlower()/xSize);
        int xu = (int)Math.ceil(bounds.getXupper()/xSize);
        int yl = (int)Math.floor(bounds.getYlower()/ySize);
        int yu = (int)Math.ceil(bounds.getYupper()/ySize);
        int zl = (int)Math.floor(bounds.getZlower()/zSize);
        int zu = (int)Math.ceil(bounds.getZupper()/zSize);
        int chunks = 0;
        for (int x = xl; x <= xu; x++)
            for (int y = yl; y <= yu; y++)
                for (int z = zl; z <= zu; z++)
                    if (isInside(x*1.5, y*1.5, z*3.0))
                    {
                        chunks++;
                        if (points != null)
                            points.add(mapPoint(x, y, z, orientation));
                    }
        return chunks*1.5*1.5*3.0;
    }
    
    private static final int[][] nearby = {
            { -1, 0, 0 },
            {  1, 0, 0 },
            { 0, -1, 0 },
            { 0,  1, 0 },
            { 0, 0, -1 },
            { 0, 0,  1 },
    };

    protected double getVolumeWalk(int orientation,
            List<Point3i> points, double xSize, double ySize, double zSize,
            Block3D bounds)
    {
        System.out.println("Volume by walk");
        Map<String,Point3i> todo = new HashMap<>();
        Set<String> done = new HashSet<>();
        Point3i o = new Point3i(0,0,0);
        todo.put(o.toString(), o);
        int chunks = 0;
        while (todo.size() > 0)
        {
            Point3i doing = todo.values().iterator().next();
            todo.remove(doing.toString());
            done.add(doing.toString());
            System.out.print("  doing "+doing+" of "+todo.size()+", ");
            if (isInside(doing.x*1.5, doing.y*1.5, doing.z*3.0))
            {
                System.out.println("inside "+bounds);
                chunks++;
                if (points != null)
                    points.add(mapPoint(doing.x, doing.y, doing.z, orientation));
                for (int[] d : nearby)
                {
                    Point3i p2 = new Point3i(doing.x + d[0], doing.y + d[1], doing.z + d[2]);
                    if (!done.contains(p2.toString()))
                        todo.put(p2.toString(), p2);
                }
            }
            else
                System.out.println("outside "+bounds);            
        }
        return chunks*1.5*1.5*3.0;
    }

    private Point3i mapPoint(int x, int y, int z, int orientation)
    {
        switch (orientation)
        {
            case ShipScanBean.XP:
                return new Point3i(x, y, z);                
            case ShipScanBean.XM:
                return new Point3i(-x, y, z);                
            case ShipScanBean.YP:
                return new Point3i(y, z, x);                
            case ShipScanBean.YM:
                return new Point3i(-y, z, x);                
            case ShipScanBean.ZP:
                return new Point3i(z, x, y);                
            case ShipScanBean.ZM:
                return new Point3i(-z, x, y);                
        }
        throw new IllegalArgumentException("Unsupported orientation: "+orientation);
    }
    
    public void setVolume(double targetVolume)
    {
        for (;;)
        {
            double actualVolume = getVolume();
            double delta = targetVolume/actualVolume;
            if (delta > 1.01)
                mAspectRatio.scale(Math.cbrt(delta));
            else if (delta < .099)
                mAspectRatio.mult(Math.cbrt(delta));
            else
                break;
        }
    }
    
    public void setVolumeDecks(double targetVolume, int orientation)
    {
        for (int i = 0; i < 8; i++)
        {
            double actualVolume = getVolumeDecks(orientation, null);
            double delta = targetVolume/actualVolume;
            if (delta > 1.01)
                mAspectRatio.scale(Math.cbrt(delta));
            else if (delta < .099)
                mAspectRatio.mult(Math.cbrt(delta));
            else
                break;
        }
    }
    
    public Point3D getAspectRatio()
    {
        return mAspectRatio;
    }
    public void setAspectRatio(Point3D aspectRatio)
    {
        mAspectRatio = aspectRatio;
    }
    
}
