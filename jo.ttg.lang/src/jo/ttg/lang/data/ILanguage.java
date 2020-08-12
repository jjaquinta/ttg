package jo.ttg.lang.data;

import org.json.simple.IJSONAble;

public interface ILanguage extends IJSONAble
{
    public String   getName(); // human readable name
    public String   getCode(); // two character symbol
    public boolean  isDefault(); // is it a system supplied language
}
