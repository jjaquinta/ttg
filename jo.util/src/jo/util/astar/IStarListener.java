package jo.util.astar;

import java.util.List;
import java.util.Set;

public interface IStarListener
{
    public boolean interiumPath(List<AStarNode> path, List<AStarNode> open, Set<AStarNode> closed);
}
