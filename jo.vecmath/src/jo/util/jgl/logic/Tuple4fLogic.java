package jo.util.jgl.logic;

import javax.vecmath.Tuple4f;

import jo.util.utils.MathUtils;

public class Tuple4fLogic extends MathUtils
{
    public static float[] toFloatArray(Tuple4f v)
    {
        return new float[] { v.x, v.y, v.z, v.w };
    }
}
