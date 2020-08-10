package jo.util.geom3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Obj3D
{
    private List<Point3D> mV = new ArrayList<>();
    private List<Point3D> mVT = new ArrayList<>();
    private List<Point3D> mVN = new ArrayList<>();
    private List<IDirective> mD = new ArrayList<>();
    private Map<String,List<IDirective>> mO = new HashMap<>();
    
    public interface IDirective
    {
        
    }
    
    public class Misc implements IDirective
    {
        String  mCommand;

        public String getCommand()
        {
            return mCommand;
        }

        public void setCommand(String command)
        {
            mCommand = command;
        }
    }

    public class Face implements IDirective
    {
        private List<FacePoint> mPoints = new ArrayList<Obj3D.FacePoint>();

        public List<FacePoint> getPoints()
        {
            return mPoints;
        }

        public void setPoints(List<FacePoint> points)
        {
            mPoints = points;
        }        
    }

    public class FacePoint
    {
        private Point3D mV;
        private Point3D mVT;
        private Point3D mVN;
        
        public Point3D getV()
        {
            return mV;
        }
        public void setV(Point3D v)
        {
            mV = v;
        }
        public Point3D getVT()
        {
            return mVT;
        }
        public void setVT(Point3D vT)
        {
            mVT = vT;
        }
        public Point3D getVN()
        {
            return mVN;
        }
        public void setVN(Point3D vN)
        {
            mVN = vN;
        }
    }

    public List<Point3D> getV()
    {
        return mV;
    }

    public void setV(List<Point3D> v)
    {
        mV = v;
    }

    public List<Point3D> getVT()
    {
        return mVT;
    }

    public void setVT(List<Point3D> vT)
    {
        mVT = vT;
    }

    public List<Point3D> getVN()
    {
        return mVN;
    }

    public void setVN(List<Point3D> vN)
    {
        mVN = vN;
    }

    public List<IDirective> getD()
    {
        return mD;
    }

    public void setD(List<IDirective> d)
    {
        mD = d;
    }

    public Map<String, List<IDirective>> getO()
    {
        return mO;
    }

    public void setO(Map<String, List<IDirective>> o)
    {
        mO = o;
    }
}
