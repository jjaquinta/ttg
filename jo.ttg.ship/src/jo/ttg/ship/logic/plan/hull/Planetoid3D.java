package jo.ttg.ship.logic.plan.hull;

import jo.util.geom3d.Block3D;
import jo.util.geom3d.Mesh3D;
import jo.util.geom3d.Mesh3DLogic;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Triangle3D;
import jo.util.geom3d.util.PerturbLogic;
import jo.util.geom3d.util.SphereLogic;

public class Planetoid3D extends Volume3D
{
    private Mesh3D  mMesh;
    private Block3D mBounds;
    
    // constructors
    public Planetoid3D(boolean tight)
    {
        mMesh = SphereLogic.generateGeodesic(new Point3D(0, 0, 0), .1, 4);
        mBounds = new Block3D();
        mMesh = PerturbLogic.perturbOutwards(mMesh, 0, 4, 4, 4);
        for (Triangle3D t : mMesh.getMesh())
        {
            mBounds.extend(t.getP1());
            mBounds.extend(t.getP2());
            mBounds.extend(t.getP3());
        }
    }

    // overrides
    @Override
    public double getVolume()
    {        
        return 4/3*Math.PI*mAspectRatio.x*mAspectRatio.y*mAspectRatio.z;
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
        return Mesh3DLogic.contains(mMesh, p);
    }
    
    // getters and setters
}
