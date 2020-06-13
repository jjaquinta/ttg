package jo.util.utils.js;

import java.util.ArrayList;
import java.util.List;

public class TokenizeLogic
{
    static List<String> tokenizeText(String text, String prefix, String suffix)
    {
        List<String> tokens = new ArrayList<String>();
        for (;;)
        {
            int start = text.indexOf(prefix);
            if (start < 0)
            {
                tokens.add(text);
                break;
            }
            int end = text.indexOf(suffix, start + prefix.length());
            if (end < 0)
                end = text.length();
            tokens.add(text.substring(0, start));
            tokens.add(text.substring(start + prefix.length(), end));
            text = text.substring(end + suffix.length());
        }
        return tokens;
    }

    public static int findLoopEnd(List<String> chunks, int start, int end)
    {
        int stack = 0;
        for (int i = start + 2; i < end; i += 2)
            if (TokenizeLogic.startsWithToken(chunks.get(i), "#loop"))
                stack++;
            else if (TokenizeLogic.startsWithToken(chunks.get(i), "#endloop"))
            {
                stack--;
                if (stack < 0)
                    return i;
            }
        return end;
    }

    public static int findIfEnd(List<String> chunks, int start, int end)
    {
        int stack = 0;
        for (int i = start + 2; i < end; i += 2)
            if (TokenizeLogic.startsWithToken(chunks.get(i), "#if") || TokenizeLogic.startsWithToken(chunks.get(i), "#ifdef"))
                stack++;
            else if (TokenizeLogic.startsWithToken(chunks.get(i), "#end"))
            {
                stack--;
                if (stack < 0)
                    return i;
            }
        return end;
    }

    public static int findIfClose(List<String> chunks, int start, int end)
    {
        int stack = 0;
        for (int i = start + 2; i < end; i += 2)
            if (TokenizeLogic.startsWithToken(chunks.get(i), "#if") || TokenizeLogic.startsWithToken(chunks.get(i), "#ifdef"))
                stack++;
            else if (TokenizeLogic.startsWithToken(chunks.get(i), "#else"))
            {
                if (stack == 0)
                    return i;
            }
            else if (TokenizeLogic.startsWithToken(chunks.get(i), "#end"))
            {
                stack--;
                if (stack < 0)
                    return i;
            }
        return end;
    }

    public static boolean startsWithToken(String txt, String tok)
    {
        if (!txt.startsWith(tok))
            return false;
        if (txt.length() == tok.length())
            return true;
        char c = txt.charAt(tok.length());
        return !Character.isLetter(c);
    }
}
