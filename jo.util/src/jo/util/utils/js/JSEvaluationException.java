/*
 * Created on Apr 29, 2005 4:37:46 PM by jo
 */
package jo.util.utils.js;

/**
 * @author jo
 * 
 * Apr 29, 2005 4:37:46 PM
 */
public class JSEvaluationException extends Exception
{
    /**
     * 
     */
    private static final long serialVersionUID = -5538589708969672480L;

    public JSEvaluationException(String reason)
    {
        super(reason);
    }

    public JSEvaluationException(String reason, Exception e)
    {
        super(reason, e);
    }
}
