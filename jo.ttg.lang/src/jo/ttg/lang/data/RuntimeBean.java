package jo.ttg.lang.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import jo.util.beans.PCSBean;

public class RuntimeBean extends PCSBean
{
    private JSONObject      mSettings;
    private String          mError;
    private List<ILanguage> mLanguages = new ArrayList<ILanguage>();
    private ILanguage       mSelectedLanguage;

    // getters and setters

    public JSONObject getSettings()
    {
        return mSettings;
    }

    public void setSettings(JSONObject settings)
    {
        mSettings = settings;
    }

    public String getError()
    {
        return mError;
    }

    public void setError(String error)
    {
        queuePropertyChange("error", mError, error);
        mError = error;
        firePropertyChange();
    }

    public List<ILanguage> getLanguages()
    {
        return mLanguages;
    }

    public void setLanguages(List<ILanguage> languages)
    {
        queuePropertyChange("languages", mLanguages, languages);
        mLanguages = languages;
        firePropertyChange();
    }

    public ILanguage getSelectedLanguage()
    {
        return mSelectedLanguage;
    }

    public void setSelectedLanguage(ILanguage selectedLanguage)
    {
        queuePropertyChange("selectedLanguage", mSelectedLanguage, selectedLanguage);
        mSelectedLanguage = selectedLanguage;
        firePropertyChange();
    }
}
