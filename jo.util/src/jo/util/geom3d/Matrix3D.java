package jo.util.geom3d;

import java.util.ArrayList;
import java.util.List;

public class Matrix3D
{
    private double               xx, xy, xz, xo;
    private double               yx, yy, yz, yo;
    private double               zx, zy, zz, zo;

    /** Create a new unit matrix */
    public Matrix3D()
    {
        xx = 1.0f;
        yy = 1.0f;
        zz = 1.0f;
    }

    public Matrix3D(Matrix3D m2)
    {
        xx = m2.xx;
        xy = m2.xy;
        xz = m2.xz;
        xo = m2.xo;
        yx = m2.yx;
        yy = m2.yy;
        yz = m2.yz;
        yo = m2.yo;
        zx = m2.zx;
        zy = m2.zy;
        zz = m2.zz;
        zo = m2.zo;
    }

    public Matrix3D(double _xx, double _xy, double _xz, double _xo,
            double _yx, double _yy, double _yz, double _yo,
            double _zx, double _zy, double _zz, double _zo
            )
    {
        xx = _xx;
        xy = _xy;
        xz = _xz;
        xo = _xo;
        yx = _yx;
        yy = _yy;
        yz = _yz;
        yo = _yo;
        zx = _zx;
        zy = _zy;
        zz = _zz;
        zo = _zo;
    }

    public Matrix3D(double _xx, double _xy, double _xz,
            double _yx, double _yy, double _yz,
            double _zx, double _zy, double _zz
            )
    {
        xx = _xx;
        xy = _xy;
        xz = _xz;
        yx = _yx;
        yy = _yy;
        yz = _yz;
        zx = _zx;
        zy = _zy;
        zz = _zz;
    }
    
    public double get(int i, int j)
    {
        switch (i)
        {
            case 0:
                switch (j)
                {
                    case 0:
                        return xx;
                    case 1:
                        return yx;
                    case 2:
                        return zx;
                }
                break;
            case 1:
                switch (j)
                {
                    case 0:
                        return xy;
                    case 1:
                        return yy;
                    case 2:
                        return zy;
                }
                break;
            case 2:
                switch (j)
                {
                    case 0:
                        return xz;
                    case 1:
                        return yz;
                    case 2:
                        return zz;
                }
                break;
            case 3:
                switch (j)
                {
                    case 0:
                        return xo;
                    case 1:
                        return yo;
                    case 2:
                        return zo;
                }
                break;
        }
        throw new IllegalArgumentException();
    }
    
    public void set(int i, int j, double v)
    {
        switch (i)
        {
            case 0:
                switch (j)
                {
                    case 0:
                        xx = v;
                    case 1:
                        yx = v;
                    case 2:
                        zx = v;
                }
                break;
            case 1:
                switch (j)
                {
                    case 0:
                        xy = v;
                    case 1:
                        yy = v;
                    case 2:
                        zy = v;
                }
                break;
            case 2:
                switch (j)
                {
                    case 0:
                        xz = v;
                    case 1:
                        yz = v;
                    case 2:
                        zz = v;
                }
                break;
            case 3:
                switch (j)
                {
                    case 0:
                        xo = v;
                    case 1:
                        yo = v;
                    case 2:
                        zo = v;
                }
                break;
        }
    }

    /** Scale by f in all dimensions */
    public void scale(double f)
    {
        xx *= f;
        xy *= f;
        xz *= f;
        xo *= f;
        yx *= f;
        yy *= f;
        yz *= f;
        yo *= f;
        zx *= f;
        zy *= f;
        zz *= f;
        zo *= f;
    }

    /** Scale along each axis independently */
    public void scale(double xf, double yf, double zf)
    {
        xx *= xf;
        xy *= xf;
        xz *= xf;
        xo *= xf;
        yx *= yf;
        yy *= yf;
        yz *= yf;
        yo *= yf;
        zx *= zf;
        zy *= zf;
        zz *= zf;
        zo *= zf;
    }

    /** Translate the origin */
    public void translate(double x, double y, double z)
    {
        xo += x;
        yo += y;
        zo += z;
    }

    /** rotate theta degrees about the y axis */
    public void yrot(double theta)
    {
        theta *= (Math.PI / 180);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        double Nxx = xx * ct + zx * st;
        double Nxy = xy * ct + zy * st;
        double Nxz = xz * ct + zz * st;
        double Nxo = xo * ct + zo * st;

        double Nzx = zx * ct - xx * st;
        double Nzy = zy * ct - xy * st;
        double Nzz = zz * ct - xz * st;
        double Nzo = zo * ct - xo * st;

        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /** rotate theta degrees about the x axis */
    public void xrot(double theta)
    {
        theta *= (Math.PI / 180);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        double Nyx = yx * ct + zx * st;
        double Nyy = yy * ct + zy * st;
        double Nyz = yz * ct + zz * st;
        double Nyo = yo * ct + zo * st;

        double Nzx = zx * ct - yx * st;
        double Nzy = zy * ct - yy * st;
        double Nzz = zz * ct - yz * st;
        double Nzo = zo * ct - yo * st;

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        zo = Nzo;
        zx = Nzx;
        zy = Nzy;
        zz = Nzz;
    }

    /** rotate theta degrees about the z axis */
    public void zrot(double theta)
    {
        theta *= (Math.PI / 180);
        double ct = Math.cos(theta);
        double st = Math.sin(theta);

        double Nyx = yx * ct + xx * st;
        double Nyy = yy * ct + xy * st;
        double Nyz = yz * ct + xz * st;
        double Nyo = yo * ct + xo * st;

        double Nxx = xx * ct - yx * st;
        double Nxy = xy * ct - yy * st;
        double Nxz = xz * ct - yz * st;
        double Nxo = xo * ct - yo * st;

        yo = Nyo;
        yx = Nyx;
        yy = Nyy;
        yz = Nyz;
        xo = Nxo;
        xx = Nxx;
        xy = Nxy;
        xz = Nxz;
    }

    /** Multiply this matrix by a second: M = M*R */
    public void mult(Matrix3D rhs)
    {
        double lxx = xx * rhs.xx + yx * rhs.xy + zx * rhs.xz;
        double lxy = xy * rhs.xx + yy * rhs.xy + zy * rhs.xz;
        double lxz = xz * rhs.xx + yz * rhs.xy + zz * rhs.xz;
        double lxo = xo * rhs.xx + yo * rhs.xy + zo * rhs.xz + rhs.xo;

        double lyx = xx * rhs.yx + yx * rhs.yy + zx * rhs.yz;
        double lyy = xy * rhs.yx + yy * rhs.yy + zy * rhs.yz;
        double lyz = xz * rhs.yx + yz * rhs.yy + zz * rhs.yz;
        double lyo = xo * rhs.yx + yo * rhs.yy + zo * rhs.yz + rhs.yo;

        double lzx = xx * rhs.zx + yx * rhs.zy + zx * rhs.zz;
        double lzy = xy * rhs.zx + yy * rhs.zy + zy * rhs.zz;
        double lzz = xz * rhs.zx + yz * rhs.zy + zz * rhs.zz;
        double lzo = xo * rhs.zx + yo * rhs.zy + zo * rhs.zz + rhs.zo;

        xx = lxx;
        xy = lxy;
        xz = lxz;
        xo = lxo;

        yx = lyx;
        yy = lyy;
        yz = lyz;
        yo = lyo;

        zx = lzx;
        zy = lzy;
        zz = lzz;
        zo = lzo;
    }

    /** Add this matrix by a second: M = M+R */
    public void add(Matrix3D rhs)
    {
        xx += rhs.xx;
        xy += rhs.xy;
        xz += rhs.xz;
        xo += rhs.xo;

        yx += rhs.yx;
        yy += rhs.yy;
        yz += rhs.yz;
        yo += rhs.yo;

        zx += rhs.zx;
        zy += rhs.zy;
        zz += rhs.zz;
        zo += rhs.zo;
    }

    /** Reinitialize to the unit matrix */
    public void unit()
    {
        xo = 0;
        xx = 1;
        xy = 0;
        xz = 0;
        yo = 0;
        yx = 0;
        yy = 1;
        yz = 0;
        zo = 0;
        zx = 0;
        zy = 0;
        zz = 1;
    }

    /**
     * Transform nvert points from v into tv. v contains the input coordinates
     * in floating point. Three successive entries in the array constitute a
     * point. tv ends up holding the transformed points as integers; three
     * successive entries per point
     */
    public void transform(double v[], int tv[], int nvert)
    {
        double lxx = xx, lxy = xy, lxz = xz, lxo = xo;
        double lyx = yx, lyy = yy, lyz = yz, lyo = yo;
        double lzx = zx, lzy = zy, lzz = zz, lzo = zo;
        for (int i = nvert * 3; (i -= 3) >= 0;)
        {
            double x = v[i];
            double y = v[i + 1];
            double z = v[i + 2];
            tv[i] = (int)(x * lxx + y * lxy + z * lxz + lxo);
            tv[i + 1] = (int)(x * lyx + y * lyy + z * lyz + lyo);
            tv[i + 2] = (int)(x * lzx + y * lzy + z * lzz + lzo);
        }
    }
    
    public void scale(Point3D v)
    {
        scale(v.getX(), v.getY(), v.getZ());
    }
    
    public void translate(Point3D v)
    {
        translate(v.getX(), v.getY(), v.getZ());
    }
    
    public void rotate(Point3D v)
    {
        xrot(v.getX());
        yrot(v.getY());
        zrot(v.getZ());
    }

    public void transform(Point3D v)
    {
        transform(v, v);
    }    

    public Point3D transformNew(Point3D inV)
    {
        Point3D outV = new Point3D();
        transform(inV, outV);
        return outV;
    }    

    public void transform(Point3D inV, Point3D outV)
    {
        double inX = inV.getX();
        double inY = inV.getY();
        double inZ = inV.getZ();
        double outX = inX*xx + inY*xy + inZ*xz + xo;
        double outY = inX*yx + inY*yy + inZ*yz + yo;
        double outZ = inX*zx + inY*zy + inZ*zz + zo;
        outV.setX(outX);
        outV.setY(outY);
        outV.setZ(outZ);
    }

    public void transformNoTranslate(Point3D inV, Point3D outV)
    {
        double inX = inV.getX();
        double inY = inV.getY();
        double inZ = inV.getZ();
        double outX = inX*xx + inY*xy + inZ*xz;
        double outY = inX*yx + inY*yy + inZ*yz;
        double outZ = inX*zx + inY*zy + inZ*zz;
        outV.setX(outX);
        outV.setY(outY);
        outV.setZ(outZ);
    }
    
    public void transform(List<Point3D> vs)
    {
        for (Point3D v : vs)
            transform(v);
    }
    
    public void transform(List<Point3D> vsIn, List<Point3D> vsOut)
    {
        for (int i = 0; i < vsIn.size(); i++)
            transform(vsIn.get(i), vsOut.get(i));
    }
    
    public List<Point3D> transformNew(List<Point3D> vsIn)
    {
        List<Point3D> vsOut = new ArrayList<Point3D>();
        for (int i = 0; i < vsIn.size(); i++)
            vsOut.add(transformNew(vsIn.get(i)));
        return vsOut;
    }

    public Matrix3D inverse()
    {
        double det = 0;
        for(int i = 0; i < 3; i++)
            det = det + (get(0, i) * (get(1, (i+1)%3) * get(2, (i+2)%3) - get(1, (i+2)%3) * get(2, (i+1)%3)));
        Matrix3D ret = new Matrix3D();
        for (int i = 0; i < 3; ++i)
            for (int j = 0; j < 3; ++j)
                ret.set(i,  j, (((get((j+1)%3, (i+1)%3) * get((j+2)%3, (i+2)%3)) - (get((j+1)%3, (i+2)%3) * get((j+2)%3, (i+1)%3)))/ det));
        return ret;
    }
}
