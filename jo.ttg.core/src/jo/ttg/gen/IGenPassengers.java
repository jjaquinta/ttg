package jo.ttg.gen;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.trade.PassengersBean;

public interface IGenPassengers
{
    public List<PassengersBean> generatePassengers(MainWorldBean origin, MainWorldBean destination, DateBean date);
}