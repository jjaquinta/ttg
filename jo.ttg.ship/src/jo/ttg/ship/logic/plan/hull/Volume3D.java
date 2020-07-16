package jo.ttg.ship.logic.plan.hull;

import java.util.List;

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
        Block3D bounds = getBounds();
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
