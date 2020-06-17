package jo.ttg.gen;

import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;


public interface IGenSurface
{
    public SurfaceBean generateSurface(BodyWorldBean world);
    public SurfaceBean newSurfaceBean();
}