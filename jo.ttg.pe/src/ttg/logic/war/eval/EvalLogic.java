package ttg.logic.war.eval;

import java.util.Hashtable;
import java.util.Iterator;

public class EvalLogic
{
    private static Hashtable mFunctions = new Hashtable(100);
    private static Hashtable mConstants = new Hashtable(20);

    static {
        mConstants.put("PI", new Double(Math.PI));
        mConstants.put("E", new Double(Math.E));

        FSymbols fs;
        int idx = 1;

        // common sense functions
        fs = new FSymbols("abs", 1, idx++);
        mFunctions.put(fs.name, fs); // absolute value
        fs = new FSymbols("floor", 1, idx++);
        mFunctions.put(fs.name, fs); // smaller integer part
        fs = new FSymbols("ceil", 1, idx++);
        mFunctions.put(fs.name, fs); // larger integer part
        fs = new FSymbols("round", 1, idx++);
        mFunctions.put(fs.name, fs); // round

        fs = new FSymbols("min", 2, idx++);
        mFunctions.put(fs.name, fs);
        fs = new FSymbols("max", 2, idx++);
        mFunctions.put(fs.name, fs);
    }

    public static Object evaluate(String expr, Hashtable variables)
        throws IllegalArgumentException
    {
    	// add variables
    	for (Iterator i = variables.keySet().iterator(); i.hasNext(); )
    	{
    		Object key = i.next();
			Object val = variables.get(key);
    		mConstants.put(key, val);
    	}
        expr = cleanSpaces(expr);
        TokenList tokens = separateTokens(expr);

        Object res = null; // error

        try
        {
            res = evaluateTokenizedExpression(tokens);
        }
        catch (IllegalArgumentException e1)
        {
            throw e1;
        }
        catch (Throwable e2)
        {
            System.out.println("Exception: " + e2.getMessage());
        }

        return res;
    }

    public static boolean evaluateBoolean(String sExpression, Hashtable variables)
        throws IllegalArgumentException
    {
        return CalcLogic.makeBoolean(evaluate(sExpression, variables));
    }

	public static int evaluateInteger(String sExpression, Hashtable variables)
		throws IllegalArgumentException
	{
		return CalcLogic.makeInteger(evaluate(sExpression, variables));
	}

    private static String cleanSpaces(String inbuf)
    {
        StringBuffer outbuf = new StringBuffer();
        char sInput[] = inbuf.toCharArray();
        boolean inquote = false;

        for (int i = 0; i < sInput.length; i++)
        {
            if (!inquote)
            {
                if (sInput[i] != ' ')
                {
                    outbuf.append(sInput[i]);
                }
            }
            else
                outbuf.append(sInput[i]);
            if (sInput[i] == '"')
                inquote = !inquote;
        }
        return outbuf.toString();
    }

    //
    //  Separates tokens in the expression string with spaces.
    //  Assumption: the input string has no spaces.
    //
    private static TokenList separateTokens(String sIn)
    {
        TokenList tokens = new TokenList();
        char sInput[] = sIn.toCharArray();
        char ch;
        int n = sInput.length;

        int i = 0;
        while (i < n)
        {
            ch = sInput[i];
            if (TokenizeLogic.isControlCharacter(ch))
            {
                // operation.  all operations are 1 character long
                Token t = new Token(Token.OPER, String.valueOf(ch));
                tokens.addTail(t);
            }
            else
            {
                StringBuffer sBuffer = new StringBuffer();
                sBuffer.append(ch);
                i++;

                // now it should be either a constant or function or number.
                // since functions or constants can't begin with numbers -
                // first check if the character can represent a number, and
                // create a number token.  If not - then check if it's a
                // function (must follow by '(') or a constant.
                if (TokenizeLogic.isNumberCharacter(ch))
                {
                    while (i < n)
                    {
                        ch = sInput[i];
                        if (TokenizeLogic.isControlCharacter(ch))
                        {
                            // we already have a control character handler above
                            // let's just back up one character, break out, and
                            // let the above handler to hadle it in the next main
                            // loop iteration
                            i--;
                            break;
                        }
                        else
                        {
                            sBuffer.append(ch);
                        }

                        i++;
                    }

                    Token t = new Token(Token.NUMB, sBuffer.toString());
                    tokens.addTail(t);

                    // end of string - exit
                    if (i == n)
                        break;
                }
                else if (ch == '"')
                {
                    // it's a string. lets find the end
                    sBuffer.setLength(0); // empty it
                    while (i < n)
                    {
                        ch = sInput[i];
                        if (ch == '"')
                        {
                            // end of string
                            break;
                        }
                        else
                        {
                            sBuffer.append(ch);
                        }

                        i++;
                    }

                    Token t = new Token(Token.CONS, sBuffer.toString());
                    tokens.addTail(t);

                    // end of string - exit
                    if (i == n)
                        break;
                }
                else
                {
                    // it's not a number - let's find the next control character.
                    // if it's '(' - the token is a function
                    while (i < n)
                    {
                        ch = sInput[i];
                        if (TokenizeLogic.isControlCharacter(ch))
                        {
                            if (TokenizeLogic.isOpenParenthesis(ch))
                            {
                                // function
                                Token t =
                                    new Token(Token.FUNC, sBuffer.toString());
                                tokens.addTail(t);
                            }
                            else
                            {
                                // constant
                                Token t =
                                    new Token(Token.CONS, sBuffer.toString());
                                tokens.addTail(t);
                            }

                            // let handler above handle it in the next iteration
                            i--;
                            break;
                        }
                        else
                            sBuffer.append(ch);

                        i++;
                    }

                    // end of string - must have been a constant, since
                    // function must have at least a parameter
                    if (i == n)
                    {
                        Token t = new Token(Token.CONS, sBuffer.toString());
                        tokens.addTail(t);
                        break;
                    }
                }
            }

            i++;
        }

        // take care of negative numbers
        fixNegativeNumberTokens(tokens);

        // create output string from the Token List
        String sOutput = new String();
        TokenNode tn = tokens.mHead;
        while (tn != null)
        {
            Token t = tn.mToken;
            if (t.mType == Token.CONS)
                sOutput += "C:";
            else if (t.mType == Token.FUNC)
                sOutput += "F:";
            sOutput += tn.mToken.nameStr() + ' ';

            tn = tn.mNext;
        }

        return tokens;
    }

    private static void fixNegativeNumberTokens(TokenList tokens)
    {
        TokenNode tn = tokens.mHead;

        while (tn != null)
        {
            if (tn.mToken.mType == Token.NUMB)
            {
                TokenNode tn_prev1 = tokens.previous(tn);
                if (tn_prev1 != null
                    && tn_prev1.mToken.mType == Token.OPER
                    && tn_prev1.mToken.nameStr().equals("-"))
                {
                    // only fix if:
                    // 1. no previous tokens
                    // 2. previous token - open parenthesis
                    // 3. previous token - operation
                    // 4. previous token - coma (separating parameters for example)
                    TokenNode tn_prev0 = tokens.previous(tn_prev1);
                    if (tn_prev0 == null
                        || TokenizeLogic.isOpenParenthesis(tn_prev0.mToken)
                        || (tn_prev0.mToken.mType == Token.OPER
                            && tn_prev0.mToken.nameStr().length() == 1
                            && (TokenizeLogic.isOperation(tn_prev0.mToken)
                                || tn_prev0.mToken.nameStr().charAt(0) == ',')))
                    {
                        Token t_backup = tokens.removeAt(tn);
                        Token t_modify = tn_prev1.mToken;
                        t_modify.mType = Token.NUMB;
                        t_modify.mName = new String("-" + t_backup.mName);
                    }
                }
            }

            tn = tn.mNext;
        }
    }

    private static Object evaluateTokenizedExpression(TokenList tl)
        throws IllegalArgumentException
    {
        // 1. Terminate the recoursive chain if only one argument,
        // or go ahead... if more than 1 agument.

        if (tl.mElements < 1)
            throw (new IllegalArgumentException("Zero arguments encountered."));
        else if (tl.mElements == 1)
        {
            // must be a number or a constant
            Token t = tl.mHead.mToken;
            if (t.mType == Token.NUMB || t.mType == Token.CONS)
                return calcNumberConstant(t);
            else
                throw (
                    new IllegalArgumentException(
                        "Expected number or constant: " + t.nameStr()));
        }

        // 1. Evaluate all functions into numbers and rebuild the list (recursive calls)
        // 2. Use stack algorithm to convert new list to postfix expression
        // 3. Evaluate postfix expression

        tl = buildArithmeticList(tl);
        tl = buildPostfixList(tl);

        return evaluatePostfixList(tl);
    }

    private static Object calcNumberConstant(Token t)
        throws IllegalArgumentException
    {
        Object d = null;

        if (t.mType == Token.CONS)
        {
            if (t.mName instanceof String)
            {
                d = mConstants.get(t.nameStr());
                if (d == null)
                {
                    String s = t.nameStr();
                    if (s.startsWith("\""))
                        s = s.substring(1);
                    if (s.endsWith("\""))
                        s = s.substring(0, s.length() - 1);
                    d = s;
                }
            }
            else
                d = t.mName;
            if (d == null)
                throw (
                    new IllegalArgumentException(
                        "Invalid constant name: " + t.nameStr()));
        }
        else
        {
            // number - check if it has only digits,
            // or ###e+### format

            int n = t.nameStr().length();
            if (n < 1)
                throw (
                    new IllegalArgumentException("Empty number encountered"));
            char ch = t.nameStr().charAt(0);
            if (!TokenizeLogic.isNumberCharacter(ch) && ch != '-')
                throw (
                    new IllegalArgumentException("Bad number: " + t.nameStr()));
            if (n < 2 && (ch == '-' || ch == '.'))
                throw (
                    new IllegalArgumentException("Bad number: " + t.nameStr()));

            // if scientific format - don't check "e+" or "e-"
            int indexSkip = t.nameStr().indexOf("e+");
            if (indexSkip < 0)
                indexSkip = t.nameStr().indexOf("e-");
            if (indexSkip < 0)
                indexSkip = -10;

            int nDotCount = 0;
            if (ch == '.')
                nDotCount++;
            for (int i = 1; i < n; i++)
            {
                if (i != indexSkip && i != (indexSkip + 1))
                {
                    ch = t.nameStr().charAt(i);
                    if (!TokenizeLogic.isNumberCharacter(ch))
                        throw (
                            new IllegalArgumentException(
                                "Bad number: " + t.nameStr()));
                    if (ch == '.')
                        nDotCount++;
                }
            }
            if (nDotCount > 1)
                throw (
                    new IllegalArgumentException(
                        "Bad number: " + t.nameStr() + " too many dots?"));

            // evaluate it
            try
            {
                d = new Double(t.nameStr());
            }
            catch (Throwable e)
            {
                d = null;
            }
            if (d == null)
                throw (
                    new IllegalArgumentException(
                        "Invalid number: " + t.nameStr()));
        }

        return d;
    }
    private static TokenNode findMatchingCloseParenthesis(TokenNode tn_input)
    {
        TokenNode tn = tn_input;

        if (!TokenizeLogic.isOpenParenthesis(tn.mToken))
        {
            return null;
        }
        else
        {
            tn = tn.mNext;
            int nOpenParenthesisCount = 0;
            while (tn != null)
            {
                if (TokenizeLogic.isCloseParenthesis(tn.mToken))
                {
                    if (nOpenParenthesisCount == 0)
                        return tn;
                    else
                        nOpenParenthesisCount--;
                }
                else if (TokenizeLogic.isOpenParenthesis(tn.mToken))
                    nOpenParenthesisCount++;

                tn = tn.mNext;
            }

            return tn;
        }
    }

    private static TokenList buildArithmeticList(TokenList tl)
        throws IllegalArgumentException
    {
        TokenList tl_out = new TokenList();

        TokenNode tn = tl.mHead;
        while (tn != null)
        {
            if (tn.mToken.mType == Token.FUNC)
            {
                TokenNode tn_start = tn.mNext;

                if (!TokenizeLogic.isOpenParenthesis(tn_start.mToken))
                    throw (
                        new IllegalArgumentException(
                            "Open parenthesis expected: "
                                + tn_start.mToken.nameStr()
                                + " after function "
                                + tn.mToken.nameStr()));

                // search for matching close parenthesis
                TokenNode tn_end = findMatchingCloseParenthesis(tn_start);
                if (tn_end == null)
                    throw (
                        new IllegalArgumentException(
                            "Can't find closing parenthesis for function: "
                                + tn.mToken.nameStr()));

                // function parameters tokens withput parenthesis
                TokenList tl_params =
                    TokenList.copyList(tn_start.mNext, tn_end.mPrev);

                Object d = calcFunction(tn.mToken.nameStr(), tl_params);

                // show argument list
                String sOutput = tn.mToken.nameStr() + " (";
                TokenNode tn_output = tl_params.mHead;
                while (tn_output != null)
                {
                    Token t_output = tn_output.mToken;
                    sOutput += t_output.nameStr();
                    tn_output = tn_output.mNext;
                }
                sOutput += ") = " + d.toString();
                //System.out.println(sOutput);

                // add the number to the new arithmetic list
                if (d instanceof Number)
                    tl_out.addTail(new Token(Token.NUMB, d.toString()));
                else
                    tl_out.addTail(new Token(Token.CONS, d));

                // it will be adjusted to next position at the very end of the loop
                tn = tn_end;
            }
            else
            {
                tl_out.addTail(tn.mToken);
            }

            tn = tn.mNext;
        }

        return tl_out;
    }

	private static Object calcFunction(String name, TokenList tl)
		throws IllegalArgumentException
	{
		FSymbols fs = (FSymbols)mFunctions.get(name);
		if (fs == null)
			throw (
				new IllegalArgumentException("Invalid function name: " + name));
		else
		{
			Object args[] = new Object[fs.params];
			if (fs.params == 1)
			{
				TokenList tl1 = TokenList.copyList(tl.mHead, tl.mTail);
				args[0] = evaluateTokenizedExpression(tl1);
			} // end of 1 parameter functions
			else if (fs.params == 2)
			{
				// find separator ","
				TokenNode tn = tl.mHead;
				int nOpenedParenthesis = 0;
				while (tn != null)
				{
					if (tn.mToken.mType == Token.OPER
						&& tn.mToken.nameStr().length() == 1
						&& tn.mToken.nameStr().charAt(0) == ','
						&& nOpenedParenthesis == 0)
						break;
					else if (TokenizeLogic.isOpenParenthesis(tn.mToken))
						nOpenedParenthesis++;
					else if (TokenizeLogic.isCloseParenthesis(tn.mToken))
						nOpenedParenthesis--;

					tn = tn.mNext;
				}

				if (tn == null)
					throw (
						new IllegalArgumentException(
							"Parameter separator not found for function: "
								+ fs.name));
				else
				{
					TokenList tl1 = TokenList.copyList(tl.mHead, tn.mPrev);
					TokenList tl2 = TokenList.copyList(tn.mNext, tl.mTail);
					args[0] = evaluateTokenizedExpression(tl1);
					args[1] = evaluateTokenizedExpression(tl2);
				}
			}
			// invoke function
			return FunctionLogic.invoke(fs.name, args);
		}
	}

	private static int P_ElementPriority(Token t) throws IllegalArgumentException
	{

		if (TokenizeLogic.isOperation(t))
		{
			char op = t.nameStr().charAt(0);

			if (op == '=')
				return 3;
			else if (op == '|')
				return 3;
			else if (op == '&')
				return 3;
			else if (op == '<')
				return 5;
			else if (op == '>')
				return 5;
			else if (op == '+')
				return 10;
			else if (op == '-')
				return 10;
			else if (op == '*')
				return 20;
			else if (op == '/')
				return 20;
			else if (op == '%')
				return 30;
			else if (op == '^')
				return 30;
			else if (op == ':')
				return 35;
			else
				throw (
					new IllegalArgumentException(
						"Unsupported operation: " + op));
		}
		else if (TokenizeLogic.isOpenParenthesis(t))
		{
			return 40;
		}
		else
			return 0;
	}


    private static TokenList buildPostfixList(TokenList tl)
        throws IllegalArgumentException
    {
        TokenList tl_out = new TokenList();
        TokenStack stack = new TokenStack();

        // show INFIX list
        String sOutput = "Infix: ";
        TokenNode tn_output = tl.mHead;
        while (tn_output != null)
        {
            Token t_output = tn_output.mToken;
            sOutput += t_output.nameStr();
            tn_output = tn_output.mNext;
        }
        //System.out.println(sOutput);

        TokenNode tn = tl.mHead;
        while (tn != null)
        {
            if (TokenizeLogic.isOperation(tn.mToken) || TokenizeLogic.isOpenParenthesis(tn.mToken))
            {
                // pop entries off the stack into tl_out
                // until lower priority operation is found
                // (don't pop parenthesis) then push operation

                while (!stack.isEmpty()
                    && !TokenizeLogic.isOpenParenthesis(stack.topElement())
                    && P_ElementPriority(stack.topElement())
                        >= P_ElementPriority(tn.mToken))
                    tl_out.addTail(stack.pop());

                // push parenthesis on the stack
                stack.push(tn.mToken);
            }
            else if (TokenizeLogic.isCloseParenthesis(tn.mToken))
            {
                // pop entries off the stack into tl_out
                // until left parenthesis found in stack
                // (don't add it to tl_out).

                while (!stack.isEmpty()
                    && !TokenizeLogic.isOpenParenthesis(stack.topElement()))
                    tl_out.addTail(stack.pop());

                // pop parenthesis from the stack
                stack.pop();
            }
            else
            {
                tl_out.addTail(tn.mToken);
            }

            tn = tn.mNext;
        }

        while (!stack.isEmpty())
            tl_out.addTail(stack.pop());

        // show POSTFIX list
        sOutput = "Postfix:";
        tn_output = tl_out.mHead;
        while (tn_output != null)
        {
            Token t_output = tn_output.mToken;
            sOutput += (" | " + t_output.nameStr());
            tn_output = tn_output.mNext;
        }
        //System.out.println(sOutput + " |");

        return tl_out;
    }

    private static Object evaluatePostfixList(TokenList tl)
        throws IllegalArgumentException
    {
        TokenStack stack = new TokenStack();

        TokenNode tn = tl.mHead;
        while (tn != null)
        {
            if (tn.mToken.mType == Token.NUMB || tn.mToken.mType == Token.CONS)
                stack.push(tn.mToken);
            else if (tn.mToken.mType == Token.OPER)
            {
                if (!TokenizeLogic.isOperation(tn.mToken))
                    throw new IllegalArgumentException(
                        "Postfix evaluation: operation expected: "
                            + tn.mToken.nameStr());

                // we remove from the stack SECOND operand first !!!
                Token t2 = stack.pop();
                Token t1 = stack.pop();

                if (t1.mType != Token.NUMB && t1.mType != Token.CONS)
                    throw new IllegalArgumentException(
                        "Postfix evaluation: number or constant expected: "
                            + t1.nameStr());
                if (t2.mType != Token.NUMB && t2.mType != Token.CONS)
                    throw new IllegalArgumentException(
                        "Postfix evaluation: number or constant expected: "
                            + t2.nameStr());

                Object d1 = calcNumberConstant(t1);
                Object d2 = calcNumberConstant(t2);
                Object d =
                    CalcLogic.calcOperation(tn.mToken.nameStr().charAt(0), d1, d2);

                Token t_new;
                if (d instanceof Number)
                    t_new = new Token(Token.NUMB, d.toString());
                else
                    t_new = new Token(Token.CONS, d);
                stack.push(t_new);
            }
            else
                throw new IllegalArgumentException(
                    "Illegal postfix stack symbol: " + tn.mToken.nameStr());

            tn = tn.mNext;
        }

        Token t = stack.pop();

        if (!stack.isEmpty())
            throw new IllegalArgumentException("Empty stack expected at the end of postfix evaluation");

        return calcNumberConstant(t);
    }

}

//	  ///////////////////////////////////////////////
//
//	   class FSymbols - function symbols:
//	   index  (supposed to be unique for each function)
//			  Used internally so that string comparison is
//			  done only once in hashtable, and then later
//			  index used for all comparisons to speed up.
//	   params (number of parameters).
//			  To help us check the parameters passed to
//			  function without having specific code for
//			  groups of functions.
//	   name   (name of the function) - just in case
//
class FSymbols
{
    public String name = null;
    public int index;
    public int params;

    public FSymbols(String sName, int nParams, int nIndex)
    {
        name = sName;
        params = nParams;
        index = nIndex;
    }
}
