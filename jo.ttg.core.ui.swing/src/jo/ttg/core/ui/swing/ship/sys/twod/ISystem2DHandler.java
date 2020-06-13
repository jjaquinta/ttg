package jo.ttg.core.ui.swing.ship.sys.twod;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.util.List;

import jo.ttg.beans.sys.BodyBean;
import jo.util.geom3d.Point3D;

public interface ISystem2DHandler
{
    public Point3D getLocation(BodyBean body);
    public void draw(Graphics2D g2, TransformedBody tbody);
    public void preDraw(Graphics2D g2, List<TransformedBody> bodies, Dimension fov, Dimension viewport);
}
