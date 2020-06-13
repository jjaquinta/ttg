package jo.util.geom3d;

import java.util.ArrayList;
import java.util.List;

public class Transform3D
{
    public double               xx, xy, xz, xo;
    public double               yx, yy, yz, yo;
    public double               zx, zy, zz, zo;

    /** Create a new unit matrix */
    public Transform3D()
    {
        xx = 1.0f;
        yy = 1.0f;
        zz = 1.0f;
    }
    
    public Transform3D(Transform3D copy)
    {
        xx = copy.xx;
        xy = copy.xy;
        xz = copy.xz;
        xo = copy.xo;
        yx = copy.yx;
        yy = copy.yy;
        yz = copy.yz;
        yo = copy.yo;
        zx = copy.zx;
        zy = copy.zy;
        zz = copy.zz;
        zo = copy.zo;
    }
    
    public String toString()
    {
        return "[["+xx+", "+xy+", "+xz+", "+xo+"], ["+yx+", "+yy+", "+yz+", "+yo+"], ["+zx+", "+zy+", "+zz+", "+zo+"]]";
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

        double Nxx = (double)(xx * ct + zx * st);
        double Nxy = (double)(xy * ct + zy * st);
        double Nxz = (double)(xz * ct + zz * st);
        double Nxo = (double)(xo * ct + zo * st);

        double Nzx = (double)(zx * ct - xx * st);
        double Nzy = (double)(zy * ct - xy * st);
        double Nzz = (double)(zz * ct - xz * st);
        double Nzo = (double)(zo * ct - xo * st);

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

        double Nyx = (double)(yx * ct + zx * st);
        double Nyy = (double)(yy * ct + zy * st);
        double Nyz = (double)(yz * ct + zz * st);
        double Nyo = (double)(yo * ct + zo * st);

        double Nzx = (double)(zx * ct - yx * st);
        double Nzy = (double)(zy * ct - yy * st);
        double Nzz = (double)(zz * ct - yz * st);
        double Nzo = (double)(zo * ct - yo * st);

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

        double Nyx = (double)(yx * ct + xx * st);
        double Nyy = (double)(yy * ct + xy * st);
        double Nyz = (double)(yz * ct + xz * st);
        double Nyo = (double)(yo * ct + xo * st);

        double Nxx = (double)(xx * ct - yx * st);
        double Nxy = (double)(xy * ct - yy * st);
        double Nxz = (double)(xz * ct - yz * st);
        double Nxo = (double)(xo * ct - yo * st);

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
    public void mult(Transform3D rhs)
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
     * in doubleing point. Three successive entries in the array constitute a
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
        scale(v.x, v.y, v.z);
    }
    
    public void translate(Point3D v)
    {
        translate(v.x, v.y, v.z);
    }
    
    public void rotateEuler(Point3D v)
    {
        xrot(v.x);
        yrot(v.y);
        zrot(v.z);
    }
    
    public void rotate(Point3D a1, double angle)
    {
        double mag = Math.sqrt(a1.x * a1.x + a1.y * a1.y + a1.z * a1.z);

        mag = 1.0 / mag;
        double ax = a1.x * mag;
        double ay = a1.y * mag;
        double az = a1.z * mag;

        double sinTheta = Math.sin(angle);
        double cosTheta = Math.cos(angle);
        double t = 1.0 - cosTheta;

        double xz = ax * az;
        double xy = ax * ay;
        double yz = ay * az;

        double[] darray = new double[16];
        darray[0] = t * ax * ax + cosTheta;
        darray[1] = t * xy - sinTheta * az;
        darray[2] = t * xz + sinTheta * ay;
        darray[3] = 0.0;

        darray[4] = t * xy + sinTheta * az;
        darray[5] = t * ay * ay + cosTheta;
        darray[6] = t * yz - sinTheta * ax;
        darray[7] = 0.0;

        darray[8] = t * xz - sinTheta * ay;
        darray[9] = t * yz + sinTheta * ax;
        darray[10] = t * az * az + cosTheta;
        darray[11] = 0.0;

        darray[12] = 0.0;
        darray[13] = 0.0;
        darray[14] = 0.0;
        darray[15] = 1.0;
        Transform3D rot = new Transform3D();
        rot.set(darray);
        mult(rot);
    }

    public void rotate(double x, double y, double z, double angle)
    {
        rotate(new Point3D(x, y, z), angle);
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
        double inX = inV.x;
        double inY = inV.y;
        double inZ = inV.z;
        double outX = inX*xx + inY*xy + inZ*xz + xo;
        double outY = inX*yx + inY*yy + inZ*yz + yo;
        double outZ = inX*zx + inY*zy + inZ*zz + zo;
        outV.x = outX;
        outV.y = outY;
        outV.z = outZ;
    }

    public void transformNoTranslate(Point3D inV, Point3D outV)
    {
        double inX = inV.x;
        double inY = inV.y;
        double inZ = inV.z;
        double outX = inX*xx + inY*xy + inZ*xz;
        double outY = inX*yx + inY*yy + inZ*yz;
        double outZ = inX*zx + inY*zy + inZ*zz;
        outV.x = outX;
        outV.y = outY;
        outV.z = outZ;
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
    
    // return transform that will rotate (1,0,0) to the direction vector given 
    public void  setOrientateTo(Point3D v) 
    {
        boolean xZero = Point3DLogic.equals(v.x, 0);
        boolean yZero = Point3DLogic.equals(v.y, 0);
        boolean zZero = Point3DLogic.equals(v.z, 0);
        // evaluate trivial options
        if (xZero)
        {
            if (yZero)
            {
                if (zZero)
                    ; // no op
                else if (v.z < 0)
                    yrot(90);
                else
                    yrot(-90);
            }
            else if (zZero)
            {
                if (v.y < 0)
                    zrot(-90);
                else
                    zrot(90);                
            }
            else
            {   // YZ plane
                float a = atan2(v.y, v.z);
                xrot(a);
                if (Math.signum(v.y) == Math.signum(v.z))
                    zrot(90);
                else
                    zrot(-90);
            }
        }
        else if (yZero)
        {
            if (zZero)
                if (v.x < 0)
                    yrot(180);
                else
                    ;
            else
            {   // XZ plane
                float a = atan2(v.z, v.x);
                yrot(-a);
            }
        }
        else if (zZero)
        {   // XY plane
            float a = atan2(v.y, v.x);
            zrot(a);
        }
        else
        {
            float xy = (float)Math.sqrt(v.x*v.x + v.y*v.y);
            zrot(atan2(v.y, v.x));
            yrot(-atan2(v.z, xy));
        }
    }

    private static float atan2(double y, double x)
    {
        return (float)(Math.atan2(y, x)/Math.PI*180);
    }

    public void set(double[] arr)
    {
        xx = arr[ 0]; yx = arr[ 1]; zx = arr[ 2];
        xy = arr[ 4]; yy = arr[ 5]; zy = arr[ 6];
        xz = arr[ 8]; yz = arr[ 9]; zz = arr[10];
        xo = arr[11]; yo = arr[12]; zo = arr[13];
    }
    
    public double[] toDoubleArray()
    {
        double[] matrix = new double[] {
                xx, yx, zx, 0,
                xy, yy, zy, 0,
                xz, yz, zz, 0,
                xo, yo, zo, 1,
//                xx, xy, xz, xo,
//                yx, yy, yz, yo,
//                zx, zy, zz, zo,
//                0, 0, 0, 1,
        };
        return matrix;
    }

    public void set(float[] arr)
    {
        xx = arr[ 0]; yx = arr[ 1]; zx = arr[ 2];
        xy = arr[ 4]; yy = arr[ 5]; zy = arr[ 6];
        xz = arr[ 8]; yz = arr[ 9]; zz = arr[10];
        xo = arr[11]; yo = arr[12]; zo = arr[13];
    }
    
    public float[] toFloatArray()
    {
        float[] matrix = new float[] {
                (float)xx, (float)yx, (float)zx, 0,
                (float)xy, (float)yy, (float)zy, 0,
                (float)xz, (float)yz, (float)zz, 0,
                (float)xo, (float)yo, (float)zo, 1,
        };
        return matrix;
    }
}
