package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.hull.Box3D;
import jo.ttg.ship.logic.plan.hull.Cone3D;
import jo.ttg.ship.logic.plan.hull.Cylinder3D;
import jo.ttg.ship.logic.plan.hull.Hemisphere3D;
import jo.ttg.ship.logic.plan.hull.Irregular3D;
import jo.ttg.ship.logic.plan.hull.Needle3D;
import jo.ttg.ship.logic.plan.hull.Planetoid3D;
import jo.ttg.ship.logic.plan.hull.Sphere3D;
import jo.ttg.ship.logic.plan.hull.Volume3D;
import jo.util.geom3d.Point3D;

public class ShipPlanHullLogic
{
// volume in cubic meters
    public static void generateHull(ShipPlanBean plan, int volume, int configuration, Point3D aspectRatio, int orientation)
    {
        System.out.println("Generating hull");
        Volume3D config = null;
        switch (configuration)
        {
            case Hull.HULL_NEEDLE:
                config = new Needle3D();
                break;
            case Hull.HULL_CONE:
                config = new Cone3D();
                break;
            case Hull.HULL_SPHERE:
                config = new Sphere3D();
                break;
            case Hull.HULL_DOME:
                config = new Hemisphere3D();
                break;
            case Hull.HULL_CYLINDER:
                config = new Cylinder3D();
                break;
            case Hull.HULL_BOX:
                config = new Box3D();
                break;
            case Hull.HULL_IRREGULAR:
                config = new Irregular3D(true);
                break;
            case Hull.HULL_OPEN_FRAME:
                config = new Irregular3D(false);
                break;
            case Hull.HULL_PLANETOID:
            case Hull.HULL_BUFFERED_PLANETOID:
                config = new Planetoid3D(false);
                break;
            default:
                throw new IllegalArgumentException("Unhandled hull type: "+configuration+" / "+Hull.hullDescription[configuration]);
        }
        config.setAspectRatio(aspectRatio);
        config.setVolumeDecks(volume, orientation);
        List<Point3i> points = new ArrayList<>();
        config.getVolumeDecks(orientation, points);
        for (Point3i p : points)
            plan.setSquare(new ShipSquareBean(p, ShipSquareBean.UNSET));
    }
}
