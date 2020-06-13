package jo.util.utils.obj;

public class CharUtils
{
    public static Object[] toArray(char[] charArray)
    {
        if (charArray == null)
            return null;
        Character[] objArray = new Character[charArray.length];
        for (int i = 0; i < charArray.length; i++)
            objArray[i] = new Character(charArray[i]);
        return objArray;
    }
}
