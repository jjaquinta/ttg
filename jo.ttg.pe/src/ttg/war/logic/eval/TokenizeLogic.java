package ttg.war.logic.eval;

public class TokenizeLogic
{

    public static boolean isControlCharacter(char ch)
    {
        if (isOpenParenthesis(ch)
            || isCloseParenthesis(ch)
            || isOperation(ch)
            || ch == ',')
            return true;
        else
            return false;
    }

    public static boolean isOpenParenthesis(char ch)
    {
        if (ch == '(' || ch == '[' || ch == '{')
            return true;
        else
            return false;
    }

       public static boolean isOpenParenthesis(Token t)
        {
            if (t.mType == Token.OPER
                && t.nameStr().length() == 1
                && TokenizeLogic.isOpenParenthesis(t.nameStr().charAt(0)))
                return true;
            else
                return false;
        }

    public static boolean isCloseParenthesis(char ch)
    {
        if (ch == ')' || ch == ']' || ch == '}')
            return true;
        else
            return false;
    }

    public static boolean isCloseParenthesis(Token t)
    {
        if (t.mType == Token.OPER
            && t.nameStr().length() == 1
            && TokenizeLogic.isCloseParenthesis(t.nameStr().charAt(0)))
            return true;
        else
            return false;
    }

    public static boolean isOperation(char ch)
    {
        if (ch == '+'
            || ch == '-'
            || ch == '*'
            || ch == '/'
            || ch == '%'
            || ch == '^'
            || ch == '&'
            || ch == '|'
            || ch == '='
            || ch == '<'
            || ch == '>')
            return true;
        else
            return false;
    }

    public static boolean isOperation(Token t)
    {
        if (t.mType == Token.OPER
            && t.nameStr().length() == 1
            && TokenizeLogic.isOperation(t.nameStr().charAt(0)))
            return true;
        else
            return false;
    }

    public static boolean isNumberCharacter(char ch)
    {
        if (ch == '0'
            || ch == '1'
            || ch == '2'
            || ch == '3'
            || ch == '4'
            || ch == '5'
            || ch == '6'
            || ch == '7'
            || ch == '8'
            || ch == '9'
            || ch == '.')
            return true;
        else
            return false;
    }
}
