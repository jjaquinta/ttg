/*
 * Created on Dec 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.dist;

import java.util.Collection;

import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.dist.DistTransitionJump;
import jo.ttg.beans.dist.DistTransitionOrbit;
import jo.ttg.utils.ConvUtils;
import jo.util.utils.obj.StringUtils;

/**
 * @author jgrant
 *
 * All fuel consumption is in tons of fuel
 * All time taken is in minutes
 */
public class ConsumptionLogic
{
    public static DistConsumption calcConsumption(Collection<DistTransition> traverse, DistCapabilities caps)
    {
        DistConsumption cons = new DistConsumption();
        for (DistTransition trans : traverse)
        {
            //System.out.print(trans.toString()+": ");
            calcConsumption(trans, caps, cons);
            //System.out.println(FormatUtils.formatDateTime(cons.getTime())+", "+Math.floor(cons.getFuel())+"t");
        }
        return cons;
    }
    
    public static boolean findConsumptionLimit(Collection<DistTransition> traverse, DistCapabilities caps, DistConsumption howFar, DistConsumption consumed)
    {
    	boolean complete = true;
        for (DistTransition trans : traverse)
        {
            if (trans instanceof DistTransitionJump)
            {
            	DistTransitionJump transJump = (DistTransitionJump)trans;
            	if ((transJump.getTimeLeft() == 7*24*60) && !caps.isJumpCheckPassed())
            	{
	            	consumed.setJumpCheckNeeded(true);
	            	complete = false;
	            	break;
            	}
            }
            DistConsumption thisJump = new DistConsumption();
            //System.out.print(trans.toString()+": ");
            calcConsumption(trans, caps, thisJump);
            if (StringUtils.isTrivial(consumed.getOrigin()))
            	consumed.setOrigin(thisJump.getOrigin());
            //System.out.println(FormatUtils.formatDateTime(cons.getTime())+", "+Math.floor(cons.getFuel())+"t");
            if ((howFar.getFuel() >= 0) && (thisJump.getFuel() > howFar.getFuel()) && !(trans instanceof DistTransitionJump))
            {
                double pc = howFar.getFuel()/thisJump.getFuel();
                add(consumed, thisJump.getFuel()*pc, (int)(thisJump.getTime().getMinutes()*pc));
                consumed.setDestination(TraverseLogic.getIncrementalLocation(trans, pc));
                complete = false;
                break;
            }
            else if ((howFar.getTime() != null) && thisJump.getTime().getMinutes() > howFar.getTime().getMinutes())
            {
                double pc = (double)howFar.getTime().getMinutes()/(double)thisJump.getTime().getMinutes();
                add(consumed, thisJump.getFuel()*pc, (int)Math.ceil(thisJump.getTime().getMinutes()*pc));
                consumed.setDestination(TraverseLogic.getIncrementalLocation(trans, pc));
                complete = false;
                break;
            }
            else
            {
                add(consumed, thisJump);
                add(howFar, -thisJump.getFuel(), -thisJump.getTime().getMinutes());
                consumed.setDestination(thisJump.getDestination());
            }
        }
        return complete;
    }
    
    public static void calcConsumption(DistTransition trans, DistCapabilities caps, DistConsumption cons)
    {
        if (trans instanceof DistTransitionJump)
            calcConsumption((DistTransitionJump)trans, caps, cons);
        else if (trans instanceof DistTransitionOrbit)
            calcConsumption((DistTransitionOrbit)trans, caps, cons);
    }
    
    private static void calcConsumption(DistTransitionJump trans, DistCapabilities caps, DistConsumption cons)
    {
        if (cons.getOrigin().length() == 0)
            cons.setOrigin(trans.getOriginURI());
        double pc = trans.getTimeLeft()/(double)(7*24*60);
        double fuelUsed =caps.getVolume()*(.05+.05*trans.getDistance())*pc;
        //System.out.println("dist="+trans.getDistance()+", pc="+pc+", used="+fuelUsed);
        cons.setFuel(cons.getFuel()+fuelUsed);
        cons.getTime().setMinutes(cons.getTime().getMinutes()+(int)trans.getTimeLeft());
        cons.setDestination(trans.getDestinationURI());
    }
    
    private static void calcConsumption(DistTransitionOrbit trans, DistCapabilities caps, DistConsumption cons)
    {
        if (cons.getOrigin().length() == 0)
            cons.setOrigin(trans.getBodyURI()+"?orbit="+trans.getOriginOrbit());
        // first calculate consumption from 0 - 100 orbits
        double distAU = 100*trans.getDiameter();
        //System.out.print("("+distAU+"AU) ");
        double distM = ConvUtils.convAUToM(distAU);
        // S = U*T + .5*A*T*T -> S = .5*A*T*T -> 2*S/A = T*T -> T = SQRT(2*S/A)
        double timeS = Math.sqrt(2*distM/(caps.getAcceleration()*9.8));
        double timeM = timeS/60;
        double pc = Math.abs(trans.getDestinationOrbit() - trans.getOriginOrbit())/100.0;
        add(cons, caps.getFuelPerMinute()*timeM*pc, (int)(timeM*pc));
        cons.setDestination(trans.getBodyURI()+"?orbit="+trans.getDestinationOrbit());
    }
    
    private static void add(DistConsumption d1, DistConsumption d2)
    {
        add(d1, d2.getFuel(), d2.getTime().getMinutes());
    }

    private static void add(DistConsumption d1, double fuel, int minutes)
    {
        d1.setFuel(d1.getFuel() + fuel);
        d1.getTime().setMinutes(d1.getTime().getMinutes() + minutes);
    }
}
