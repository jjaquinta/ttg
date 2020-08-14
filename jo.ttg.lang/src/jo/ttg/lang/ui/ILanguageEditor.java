package jo.ttg.lang.ui;

import jo.ttg.lang.data.ILanguage;

public interface ILanguageEditor
{
    public void setLanguage(ILanguage lang);
    public ILanguage getLanguage();
    public void doSave();
}
