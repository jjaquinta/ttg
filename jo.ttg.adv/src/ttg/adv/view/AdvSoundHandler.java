/*
 * Created on Jan 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view;

import ttg.adv.beans.AdvEvent;
import ttg.adv.logic.AdvEventHandler;
import ttg.adv.logic.SoundLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvSoundHandler implements AdvEventHandler
{
    private static final String BASE = "sound/";
    
    private static final String MONEY_CREDIT = "kaching.wav";
    private static final String ENTER_JUMP = "jumpin.wav";
    private static final String EXIT_JUMP = "jumpout.wav";
    private static final String BUY_SOMETHING = "purchase.wav";
    private static final String DOCK = "dock.wav";
    private static final String UNDOCK = "undock.wav";

    /* (non-Javadoc)
     * @see ttg.logic.adv.AdvEventHandler#advEvent(ttg.beans.adv.AdvEvent)
     */
    public void advEvent(AdvEvent event)
    {
        switch (event.getID())
        {
            case AdvEvent.MONEY_CREDIT:
                play(MONEY_CREDIT, true);
            	break;
            case AdvEvent.SHIP_JUMP_ENTER:
                play(ENTER_JUMP, false);
            	break;
            case AdvEvent.SHIP_JUMP_EXIT:
                play(EXIT_JUMP, false);
            	break;
            case AdvEvent.SHIP_DOCK:
                play(DOCK, false);
            	break;
            case AdvEvent.SHIP_UNDOCK:
                play(UNDOCK, false);
            	break;
            case AdvEvent.CARGO_BUY:
            case AdvEvent.FREIGHT_BUY:
            case AdvEvent.PASSENGER_CONTRACT:
                play(BUY_SOMETHING, true);
            	break;
        }
    }
    
    private void play(String sound, boolean collapse)
    {
        if (collapse)
            SoundLogic.queueResourceIfNotThere(BASE + sound);
        else
            SoundLogic.queueResource(BASE + sound);
    }

}
