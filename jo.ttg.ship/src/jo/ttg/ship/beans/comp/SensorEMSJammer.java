package jo.ttg.ship.beans.comp;

public class SensorEMSJammer extends SensorActiveEMS
{

    /**
     *
     */

    public SensorEMSJammer()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "EMS Jammer";
    }

    /**
     *
     */

    public double getWeight()
    {
        return super.getWeight()*2;
    }

}
