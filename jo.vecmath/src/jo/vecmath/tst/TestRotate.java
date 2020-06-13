package jo.vecmath.tst;

import java.util.Random;

import javax.vecmath.AxisAngle4f;
import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import jo.util.utils.MathUtils;

public class TestRotate
{
    public static void main(String[] args)
    {
        Random r = new Random();
        for (int i = 0; i < 100; i++)
        {
            test(r.nextFloat(), r.nextFloat(), r.nextFloat());
        }
    }
    
    private static void test(float x, float y, float z)
    {        
        Matrix4f m = new Matrix4f();
        AxisAngle4f a = new AxisAngle4f(x, y, z, 30*MathUtils.DEG_TO_RAD);
        m.set(a);
        Matrix3f r = new Matrix3f();
        m.get(r);
        Vector3f v = new Vector3f(x, y, z);
        System.out.println(m.toString());
        for (int i = 0; i < 8; i++)
        {
            System.out.println(v.toString());
            m.transform(v);
        }
        v.x -= x;
        v.y -= y;
        v.z -= z;
        if (v.length() > 1e-4)
            System.out.println("DRIFT!!! "+v.length());
    }
}
