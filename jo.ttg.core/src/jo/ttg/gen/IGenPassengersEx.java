package jo.ttg.gen;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.PassengersBean;

public interface IGenPassengersEx extends IGenPassengers
{
    public List<PassengersBean> generatePassengers(BodyBean origin, BodyBean destination, DateBean date);
}