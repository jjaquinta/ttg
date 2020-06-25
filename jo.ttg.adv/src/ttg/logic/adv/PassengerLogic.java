/*
 * Created on Jan 12, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PassengerLogic
{

    /**
     * @param passengers
     * @return
     */
    public static int totalBerths(List<PassengerBean> passengers)
    {
        int ret = 0;
        for (Iterator i = passengers.iterator(); i.hasNext(); )
        {
            PassengerBean pass = (PassengerBean)i.next();
            if (pass.getPassage() == PassengerBean.PASSAGE_LOW)
                ret++;
        }
        return ret;
    }

    /**
     * @param passengers
     * @return
     */
    public static int totalCabins(List<PassengerBean> passengers)
    {
        int ret = 0;
        for (Iterator i = passengers.iterator(); i.hasNext(); )
        {
            PassengerBean pass = (PassengerBean)i.next();
            if (pass.getPassage() != PassengerBean.PASSAGE_LOW)
                ret++;
        }
        return ret;
    }

    /**
     * @param passengers
     * @return
     */
    public static PassengerBean getYoungestMiddle(List<PassengerBean> passengers)
    {
        PassengerBean youngest = null;
        for (Iterator i = passengers.iterator(); i.hasNext(); )
        {
            PassengerBean pass = (PassengerBean)i.next();
            if (pass.getPassage() != PassengerBean.PASSAGE_MIDDLE)
                continue;
            if ((youngest == null) || DateLogic.earlierThan(youngest.getBoarded(), pass.getBoarded()))
                youngest = pass;
        }
        return youngest;
    }
    
    public static void disembarkPassengers(Game game)
    {
        if (!game.getShip().isDocked())
            return;
        StringBuffer disembark = new StringBuffer();
        StringBuffer leave = new StringBuffer();
        String here = game.getShip().getLocation();
        DateBean limit = new DateBean(game.getDate());
        DateLogic.incrementMinutes(limit, -4*7*24*60);
        int numIntraHigh = 0;
        int numIntraMiddle = 0;
        int numInterHigh = 0;
        int numInterMiddle = 0;
        for (Iterator i = game.getShip().getPassengers().iterator(); i.hasNext(); )
        {
            PassengerBean pass = (PassengerBean)i.next();
            if (pass.getPassage() == PassengerBean.PASSAGE_LOW)
                continue;
            if (pass.getDestination().equals(here))
            {
                if (disembark.length() > 0)
                    disembark.append(", ");
                disembark.append(pass.getName());
                if (SchemeLogic.isSameOrds(pass.getOrigin(), pass.getDestination()))
                    if (pass.getPassage() == PassengerBean.PASSAGE_HIGH)
                        numIntraHigh++;
                	else
                	    numIntraMiddle++;
                else
                    if (pass.getPassage() == PassengerBean.PASSAGE_HIGH)
                        numInterHigh++;
                    else
                        numInterMiddle++;
                i.remove();
                ReputationLogic.incrementLocation(game, pass.getDestination(), 1);
                ReputationLogic.incrementLocation(game, pass.getOrigin(), 1);
            }
            else if (DateLogic.earlierThan(pass.getBoarded(), limit))
            {
                if (leave.length() > 0)
                    leave.append(", ");
                leave.append(pass.getName());
                i.remove();
                ReputationLogic.decrementLocation(game, pass.getDestination(), 10);
                ReputationLogic.decrementLocation(game, pass.getOrigin(), 10);
            }
        }
        if (numIntraHigh > 0)
            MoneyLogic.creditToCash(game, numIntraHigh*500.0, numIntraHigh+" intrasystem high passengers");
        if (numIntraMiddle > 0)
            MoneyLogic.creditToCash(game, numIntraMiddle*400.0, numIntraMiddle+" intrasystem middle passengers");
        if (numInterHigh > 0)
            MoneyLogic.creditToCash(game, numInterHigh*10000.0, numInterHigh+" intersystem high passengers");
        if (numInterMiddle > 0)
            MoneyLogic.creditToCash(game, numInterMiddle*8000.0, numInterMiddle+" intersystem middle passengers");
        if (disembark.length() > 0)
            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_DISEMBARK, disembark.toString());
        if (leave.length() > 0)
            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_DISEMBARK_LEAVE, leave.toString());
		game.getShip().firePassengersChange();
    }
    
    public static void disembarkPassengers(Game game, List<PassengerBean> passengers)
    {
        if (!game.getShip().isDocked())
            return;
        StringBuffer disembark = new StringBuffer();
        StringBuffer leave = new StringBuffer();
        String here = game.getShip().getLocation();
        DateBean limit = new DateBean(game.getDate());
        DateLogic.incrementMinutes(limit, -4*7*24*60);
        int numIntraHigh = 0;
        int numIntraMiddle = 0;
        int numInterHigh = 0;
        int numInterMiddle = 0;
        int numInterLow = 0;
        for (PassengerBean pass : passengers.toArray(new PassengerBean[0]))
        {
            if (pass.getDestination().equals(here))
            {
                if (disembark.length() > 0)
                    disembark.append(", ");
                disembark.append(pass.getName());
                if (!SchemeLogic.isSameOrds(pass.getOrigin(), pass.getDestination()))
                    if (pass.getPassage() == PassengerBean.PASSAGE_HIGH)
                        numIntraHigh++;
                	else
                	    numIntraMiddle++;
                else
                    if (pass.getPassage() == PassengerBean.PASSAGE_HIGH)
                        numInterHigh++;
                    else if (pass.getPassage() == PassengerBean.PASSAGE_MIDDLE)
                        numInterMiddle++;
                    else
                        numInterLow++;
                game.getShip().getPassengers().remove(pass);
                ReputationLogic.incrementLocation(game, pass.getDestination(), 1);
                ReputationLogic.incrementLocation(game, pass.getOrigin(), 1);
            }
            else
            {
                if (leave.length() > 0)
                    leave.append(", ");
                leave.append(pass.getName());
                game.getShip().getPassengers().remove(pass);
                ReputationLogic.decrementLocation(game, pass.getDestination(), 10);
                ReputationLogic.decrementLocation(game, pass.getOrigin(), 10);
            }
        }
        if (numIntraHigh > 0)
            MoneyLogic.creditToCash(game, numIntraHigh*1000.0, numIntraHigh+" intrasystem high passengers");
        if (numIntraMiddle > 0)
            MoneyLogic.creditToCash(game, numIntraMiddle*800.0, numIntraMiddle+" intrasystem middle passengers");
        if (numInterHigh > 0)
            MoneyLogic.creditToCash(game, numInterHigh*10000.0, numInterHigh+" intersystem high passengers");
        if (numInterMiddle > 0)
            MoneyLogic.creditToCash(game, numInterMiddle*8000.0, numInterMiddle+" intersystem middle passengers");
        if (numInterLow > 0)
            MoneyLogic.creditToCash(game, numInterMiddle*1000.0, numInterLow+" intersystem low passengers");
        if (disembark.length() > 0)
            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_DISEMBARK, disembark.toString());
        if (leave.length() > 0)
            AdvEventLogic.fireEvent(game, AdvEvent.PASSENGER_DISEMBARK_ABANDON, leave.toString());
		game.getShip().firePassengersChange();
    }
}
