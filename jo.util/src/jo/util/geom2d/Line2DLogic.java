package jo.util.geom2d;

public class Line2DLogic
{

    public static Point2D intersects(Line2D l1, Line2D l2)
    {
        double denom = (l2.p2.y - l2.p1.y) * (l1.p2.x - l1.p1.x) - (l2.p2.x - l2.p1.x) * (l1.p2.y - l1.p1.y);
        if (denom == 0.0) // Lines are parallel.
           return null;
        double ua = ((l2.p2.x - l2.p1.x) * (l1.p1.y - l2.p1.y) - (l2.p2.y - l2.p1.y) * (l1.p1.x - l2.p1.x))/denom;
        double ub = ((l1.p2.x - l1.p1.x) * (l1.p1.y - l2.p1.y) - (l1.p2.y - l1.p1.y) * (l1.p1.x - l2.p1.x))/denom;
          if (ua >= 0.0f && ua <= 1.0f && ub >= 0.0f && ub <= 1.0f) {
              // Get the intersection point.
              return new Point2D(l1.p1.x + ua*(l1.p2.x - l1.p1.x), l1.p1.y + ua*(l1.p2.y - l1.p1.y));
          }
        return null;
    }

    public static double distanceToSegment(double x, double y, Line2D seg)
    {
        double px=seg.p2.x-seg.p1.x;
        double py=seg.p2.y-seg.p1.y;
        double temp=(px*px)+(py*py);
        double u=((x - seg.p1.x) * px + (y - seg.p1.y) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        double xx = seg.p1.x + u * px;
        double yy = seg.p1.y + u * py;

        double dx = xx - x;
        double dy = yy - y;
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
    }

    public static void translateLeft(Line2D seg, double amnt)
    {
        Point2D fwd = Point2DLogic.sub(seg.p2, seg.p1);
        fwd.normalize();
        Point2D left = new Point2D(-fwd.y, fwd.x);
        left.scale(amnt);
        seg.p1.incr(left);
        seg.p2.incr(left);
    }

}
