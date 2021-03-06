package jo.ttg.ship.logic.plan.hull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point3i;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Point3D;

public class IrregularOld3D extends Volume3D
{
    private static final int SIZE = 16;
    
    private List<Point3D>   mCenters = new ArrayList<Point3D>();
    private Block3D         mBounds = new Block3D();
    
    // constructors
    public IrregularOld3D(boolean tight)
    {
        boolean[][][] grid = new boolean[SIZE][SIZE][SIZE];
        int[][][] borders = new int[SIZE][SIZE][SIZE];
        setUsed(grid, borders, SIZE/2, SIZE/2, SIZE/2);
        int todo = SIZE*SIZE*SIZE/4;
        int tot = 6;
        Random rnd = new Random();
        while (--todo > 0)
        {
            if (tight)
                markTight(rnd, grid, borders, tot);
            else
                markLoose(rnd, grid, borders, tot);
            tot += 6;
        }
    }
    
    private void markTight(Random rnd, boolean[][][] grid, int[][][] borders, int tot)
    {
        int best = 0;
        List<Point3i> choices = new ArrayList<>();
        for (int x = 1; x < 15; x++)
            for (int y = 1; y < 15; y++)
                for (int z = 1; z < 15; z++)
                {
                    int mark = borders[x][y][z];
                    if (mark > best)
                    {
                        best = mark;
                        choices.clear();
                        choices.add(new Point3i(x, y, z));
                    }
                    else if (mark == best)
                    {
                        choices.add(new Point3i(x, y, z));
                    }
                }
        Point3i xyz = choices.get(rnd.nextInt(choices.size()));
        setUsed(grid, borders, xyz.x, xyz.y, xyz.z);
    }
    
    private void markLoose(Random rnd, boolean[][][] grid, int[][][] borders, int tot)
    {
        int mark = rnd.nextInt(tot);
        for (int x = 1; x < 15; x++)
            for (int y = 1; y < 15; y++)
                for (int z = 1; z < 15; z++)
                {
                    mark -= borders[x][y][z];
                    if (mark < 0)
                    {
                        setUsed(grid, borders, x, y, z);
                        return;
                    }
                }
    }

    private void setUsed(boolean[][][] grid, int[][][] borders, int x, int y, int z)
    {
        grid[x][y][z] = true;
        borders[x-1][y][z]++;
        borders[x+1][y][z]++;
        borders[x][y-1][z]++;
        borders[x][y+1][z]++;
        borders[x][y][z-1]++;
        borders[x][y][z+1]++;
        Point3D p = new Point3D(x - SIZE/2, y - SIZE/2, z - SIZE/3);
        mCenters.add(p);
        mBounds.extend(new Point3D(p.x - 1, p.y, p.z));
        mBounds.extend(new Point3D(p.x + 1, p.y, p.z));
        mBounds.extend(new Point3D(p.x, p.y - 1, p.z));
        mBounds.extend(new Point3D(p.x, p.y + 1, p.z));
        mBounds.extend(new Point3D(p.x, p.y, p.z - 1));
        mBounds.extend(new Point3D(p.x, p.y, p.z + 1));
    }
    
    // overrides
    @Override
    public double getVolume()
    {        
        double v = 0;
        Point3D p = new Point3D();
        for (p.x = mBounds.getXlower(); p.x <= mBounds.getXupper(); p.x += .1)
            for (p.y = mBounds.getYlower(); p.y <= mBounds.getYupper(); p.y += .1)
                for (p.z = mBounds.getZlower(); p.z <= mBounds.getZupper(); p.z += .1)
                    if (isInside(p))
                        v += .1*.1*.1;
        return v;
    }
    @Override
    public Block3D getBounds()
    {
        Block3D b = new Block3D(mBounds.x*mAspectRatio.x, mBounds.y*mAspectRatio.y, mBounds.z*mAspectRatio.z,
                mBounds.width*mAspectRatio.x, mBounds.height*mAspectRatio.y, mBounds.depth*mAspectRatio.z);
        return b;
    }
    
    @Override
    public boolean isInside(Point3D po)
    {
        Point3D p = new Point3D(po.x/mAspectRatio.x, po.y/mAspectRatio.y, po.z/mAspectRatio.z);
        for (Point3D c : mCenters)
            if (c.dist(p) <= 1)
                return true;
        return false;
    }
    
    // getters and setters
}
