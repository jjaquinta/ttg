package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.util.astar.AStarNode;
import jo.util.astar.AStarSearch;
import jo.util.utils.DebugUtils;

public class PathLogic
{
    static AStarSearch SEARCH = new AStarSearch();

    
    public static List<Point3i> findPathBetween(ShipPlanBean ship, Point3i from, Point3i to, Set<Point3i> candidates, Set<Point3i> fast)
    {
        DebugUtils.debug = false;
        if (CorridorLogic.dump)
            ship.println("Digging from " + ship.getSquare(from)+" to " + ship.getSquare(to));
        if (from.equals(to))
        {
            List<Point3i> path = new ArrayList<>();
            path.add(from);
            return path;
        }
        List<Point3i> path = findSimplePath(ship, from, to, candidates, fast);
        if (path != null)
            return path;
        return findComplexPath(ship, from, to, candidates, fast);
    }
    
    private static List<Point3i> findSimplePath(ShipPlanBean ship, Point3i from, Point3i to, Set<Point3i> candidates, Set<Point3i> fast)
    {
        List<Point3i> path = new ArrayList<>();
        path.add(from);
        Point3i p = new Point3i(from);
        while (!p.equals(to))
        {
            ShipPlanUtils.moveTowards(p, to);
            if (!candidates.contains(p) && !fast.contains(p))
                return null;
            path.add(new Point3i(p));
        }
        return path;
    }
    
    private static List<Point3i> findComplexPath(ShipPlanBean ship, Point3i from, Point3i to, Set<Point3i> candidates, Set<Point3i> fast)
    {
        DebugUtils.debug = false;
        if (CorridorLogic.dump)
            ship.println("Digging from " + ship.getSquare(from)+" to " + ship.getSquare(to));
        
        AStarNode startNode = new HullNode(candidates, fast,
                to, from);
        AStarNode goalNode = new HullNode(candidates, fast,
                to, to);
        @SuppressWarnings("rawtypes")
        List path = SEARCH.findPath(startNode, goalNode);
        if (path == null)
            return null;
        List<Point3i> ret = new ArrayList<>();
        ret.add(from); // add back in start node
        for (int i = 0; i < path.size(); i++)
        {
            Point3i p = ((HullNode)path.get(i)).getXY();
            ret.add(p);
        }
        HullNode.NODE_CACHE.clear();
        return ret;
    }
    
}

class HullNode extends AStarNode
{
    static public HashMap<Point3i, HullNode> NODE_CACHE   = new HashMap<>();
    private static final float               CORRIDOR_MOD = .5f;

    private Set<Point3i>                     mUnset;
    private Set<Point3i>                     mCorridors;
    private Point3i                          mTarget;
    private Point3i                          mXY;

    public HullNode(Set<Point3i> unset, Set<Point3i> corridors,
            Point3i target, Point3i xy)
    {
        mUnset = unset;
        mCorridors = corridors;
        mTarget = target;
        mXY = xy;
        NODE_CACHE.put(mXY, this);
    }

    public float getCost(AStarNode node)
    {
        float cost = getEstimatedCost(node);
        Point3i targetXY = ((HullNode)node).mXY;
        cost *= mCorridors.contains(targetXY) ? CORRIDOR_MOD : 1.0f;
        cost *= mCorridors.contains(mXY) ? CORRIDOR_MOD : 1.0f;
        return cost;
        /*
        Point3i targetXY = ((HullNode)node).mXY;
        float cost = mCorridors.contains(mXY) ? CORRIDOR_MOD : 1.0f;
        cost += mCorridors.contains(targetXY) ? CORRIDOR_MOD : 1.0f;
        cost /= 2;
        if (targetXY.equals(mTarget) && (mXY.z != targetXY.z))
            cost *= 5;
        return cost;
        */
    }

    public float getEstimatedCost(AStarNode node)
    {
        Point3i targetXY = ((HullNode)node).mXY;
        int dx = mXY.x - targetXY.x;
        int dy = mXY.y - targetXY.y;
        int dz = (mXY.z - targetXY.z)*5;
        float cost = (float)Math.sqrt(dx*dx + dy*dy + dz*dz);
        return cost;
        /*
        float cost = (float)CorridorLogic.dist(mXY, ((HullNode)node).mXY);
        Point3i targetXY = ((HullNode)node).mXY;
        if (targetXY.equals(mTarget) && (mXY.z != targetXY.z))
            cost *= 5;
        return cost;
        */
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public List getNeighbors()
    {
        List<Point3i> near = ShipPlanUtils.getNeighbors(mXY, true);
        List<HullNode> nearNodes = new ArrayList<>();
        for (Point3i n : near)
            if (mUnset.contains(n) || mCorridors.contains(n)
                    || n.equals(mTarget))
            {
                HullNode nn = NODE_CACHE.get(n);
                if (nn == null)
                    nn = new HullNode(mUnset, mCorridors, mTarget, n);
                nearNodes.add(nn);
            }
        return nearNodes;
    }

    public Point3i getXY()
    {
        return mXY;
    }

    public boolean equals(Object obj)
    {
        return mXY.equals(((HullNode)obj).mXY);
    }

    public String toString()
    {
        return "(" + mXY + ") cost=" + ((int)getCost()) + "("
                + ((int)mCostFromStart) + "+" + ((int)mEstimatedCostToGoal)
                + ")";
    }
}
