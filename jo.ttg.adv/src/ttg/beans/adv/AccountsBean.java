/*
 * Created on Jan 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.beans.adv;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AccountsBean
{
    private double       mCash;
    private double       mLoanPaymentAmount;
    private int          mLoanPaymentsLeft;
    private DateBean     mLoanPaymentDue;
    private DateBean     mLoanPaymentInterval;
    private List<String> mAuditTrail;

    public AccountsBean()
    {
        mAuditTrail = new ArrayList<>();
        mLoanPaymentDue = new DateBean();
        mLoanPaymentInterval = new DateBean();
    }

    public double getCash()
    {
        return mCash;
    }

    public void setCash(double cash)
    {
        mCash = cash;
    }

    public List<String> getAuditTrail()
    {
        return mAuditTrail;
    }

    public void setAuditTrail(List<String> auditTrail)
    {
        mAuditTrail = auditTrail;
    }

    public double getLoanPaymentAmount()
    {
        return mLoanPaymentAmount;
    }

    public void setLoanPaymentAmount(double loanPaymentAmount)
    {
        mLoanPaymentAmount = loanPaymentAmount;
    }

    public DateBean getLoanPaymentDue()
    {
        return mLoanPaymentDue;
    }

    public void setLoanPaymentDue(DateBean loanPaymentDue)
    {
        mLoanPaymentDue = loanPaymentDue;
    }

    public DateBean getLoanPaymentInterval()
    {
        return mLoanPaymentInterval;
    }

    public void setLoanPaymentInterval(DateBean loanPaymentInterval)
    {
        mLoanPaymentInterval = loanPaymentInterval;
    }

    public int getLoanPaymentsLeft()
    {
        return mLoanPaymentsLeft;
    }

    public void setLoanPaymentsLeft(int loanPaymentsLeft)
    {
        mLoanPaymentsLeft = loanPaymentsLeft;
    }
}
