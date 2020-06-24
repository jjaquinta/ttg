/*
 * Created on Jan 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import jo.ttg.beans.DateBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.logic.DateLogic;
import ttg.beans.adv.AccountsBean;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.Game;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MoneyLogic
{
    public static boolean debitFromCash(Game game, double amnt, String comment)
    {
        AccountsBean ac = game.getAccounts();
        if (ac.getCash() < amnt)
            return false;
        ac.getAuditTrail().add(FormatUtils.formatDateTime(game.getDate())+" Debit from Cash "+FormatUtils.sCurrency(amnt)+": "+comment);
        ac.setCash(ac.getCash() - amnt);
        game.fireMonotonicPropertyChange("accounts", ac);
        AdvEventLogic.fireEvent(game, AdvEvent.MONEY_DEBIT, new Double(amnt), comment);
        return true;
    }

    public static void creditToCash(Game game, double amnt, String comment)
    {
        AccountsBean ac = game.getAccounts();
        ac.getAuditTrail().add(FormatUtils.formatDateTime(game.getDate())+" Credit to Cash "+FormatUtils.sCurrency(amnt)+": "+comment);
        ac.setCash(ac.getCash() + amnt);
        game.fireMonotonicPropertyChange("accounts", ac);
        AdvEventLogic.fireEvent(game, AdvEvent.MONEY_CREDIT, new Double(amnt), comment);
    }
    
    public static boolean makeLoanPayment(Game game)
    {
        AccountsBean ac = game.getAccounts();
        if (ac.getLoanPaymentsLeft() <= 0)
            return true;
        double amnt = ac.getLoanPaymentAmount();
        String desc = "Loan Payment for "+FormatUtils.formatDate(ac.getLoanPaymentDue());
        if (!debitFromCash(game, amnt, desc))
        	return false;
        DateLogic.incrementMinutes(ac.getLoanPaymentDue(), ac.getLoanPaymentInterval().getMinutes());
        ac.setLoanPaymentsLeft(ac.getLoanPaymentsLeft() - 1);
        ReputationLogic.incrementReputation(game, ReputationLogic.BANK, 1);
        game.fireMonotonicPropertyChange("accounts", ac);
        AdvEventLogic.fireEvent(game, AdvEvent.LOAN_PAY, new Double(amnt));
        return true;
    }

    /**
     * @param game
     * @param oldDate
     * @param newDate
     */
    public static void loanElapsed(Game game, DateBean oldDate, DateBean newDate)
    {
        AccountsBean ac = game.getAccounts();
        DateBean i = new DateBean(ac.getLoanPaymentDue());
        while (DateLogic.earlierThan(i, newDate))
        {
            if (DateLogic.laterThan(i, oldDate))
                ReputationLogic.decrementReputation(game, ReputationLogic.BANK, 10);
            DateLogic.incrementMinutes(i, ac.getLoanPaymentInterval().getMinutes());
        }
    }

    /**
     * @param game
     */
    public static void clearAuditTrail(Game game)
    {
        game.getAccounts().getAuditTrail().clear();        
    }

    /**
     * @param game
     * @param newCost
     * @return
     */
    public static boolean canAfford(Game game, double cost)
    {
        return game.getAccounts().getCash() >= cost;
    }
}
