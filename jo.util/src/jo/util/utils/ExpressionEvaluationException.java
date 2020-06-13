/*
 * Created on Apr 29, 2005 4:37:46 PM by jo
 */
package jo.util.utils;

/**
 * @author jo
 * 
 * Apr 29, 2005 4:37:46 PM
 */
public class ExpressionEvaluationException extends Exception
{
	/**
     * 
     */
    private static final long serialVersionUID = -5538589708969672480L;

    public ExpressionEvaluationException(String reason)
	{
		super(reason);
	}
}