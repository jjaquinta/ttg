package jo.ttg.beans;

import jo.util.beans.Bean;

public class LanguageStatsBean extends Bean
{
    // WordLength
    private int[] mWordLength;
    public int[] getWordLength()
    {
        return mWordLength;
    }
    public void setWordLength(int[] v)
    {
        mWordLength = v;
    }
    public int getWordLength(int index)
    {
        return mWordLength[index];
    }
    public void setWordLength(int index, int v)
    {
        mWordLength[index] = v;
    }

    // InitialSyllableTable
    private int[] mInitialSyllableTable;
    public int[] getInitialSyllableTable()
    {
        return mInitialSyllableTable;
    }
    public void setInitialSyllableTable(int[] v)
    {
        mInitialSyllableTable = v;
    }
    public int getInitialSyllableTable(int index)
    {
        return mInitialSyllableTable[index];
    }
    public void setInitialSyllableTable(int index, int v)
    {
        mInitialSyllableTable[index] = v;
    }

    // FinalSyllableTable
    private int[] mFinalSyllableTable;
    public int[] getFinalSyllableTable()
    {
        return mFinalSyllableTable;
    }
    public void setFinalSyllableTable(int[] v)
    {
        mFinalSyllableTable = v;
    }
    public int getFinalSyllableTable(int index)
    {
        return mFinalSyllableTable[index];
    }
    public void setFinalSyllableTable(int index, int v)
    {
        mFinalSyllableTable[index] = v;
    }

    // FirstConsonant
    private int[] mFirstConsonant;
    public int[] getFirstConsonant()
    {
        return mFirstConsonant;
    }
    public void setFirstConsonant(int[] v)
    {
        mFirstConsonant = v;
    }
    public int getFirstConsonant(int index)
    {
        return mFirstConsonant[index];
    }
    public void setFirstConsonant(int index, int v)
    {
        mFirstConsonant[index] = v;
    }

    // LastConsonant
    private int[] mLastConsonant;
    public int[] getLastConsonant()
    {
        return mLastConsonant;
    }
    public void setLastConsonant(int[] v)
    {
        mLastConsonant = v;
    }
    public int getLastConsonant(int index)
    {
        return mLastConsonant[index];
    }
    public void setLastConsonant(int index, int v)
    {
        mLastConsonant[index] = v;
    }

    // Vowel
    private int[] mVowel;
    public int[] getVowel()
    {
        return mVowel;
    }
    public void setVowel(int[] v)
    {
        mVowel = v;
    }
    public int getVowel(int index)
    {
        return mVowel[index];
    }
    public void setVowel(int index, int v)
    {
        mVowel[index] = v;
    }

    // FirstConsonantText
    private String[] mFirstConsonantText;
    public String[] getFirstConsonantText()
    {
        return mFirstConsonantText;
    }
    public void setFirstConsonantText(String[] v)
    {
        mFirstConsonantText = v;
    }
    public String getFirstConsonantText(int index)
    {
        return mFirstConsonantText[index];
    }
    public void setFirstConsonantText(int index, String v)
    {
        mFirstConsonantText[index] = v;
    }

    // LastConsonantText
    private String[] mLastConsonantText;
    public String[] getLastConsonantText()
    {
        return mLastConsonantText;
    }
    public void setLastConsonantText(String[] v)
    {
        mLastConsonantText = v;
    }
    public String getLastConsonantText(int index)
    {
        return mLastConsonantText[index];
    }
    public void setLastConsonantText(int index, String v)
    {
        mLastConsonantText[index] = v;
    }

    // VowelText
    private String[] mVowelText;
    public String[] getVowelText()
    {
        return mVowelText;
    }
    public void setVowelText(String[] v)
    {
        mVowelText = v;
    }
    public String getVowelText(int index)
    {
        return mVowelText[index];
    }
    public void setVowelText(int index, String v)
    {
        mVowelText[index] = v;
    }


    // constructor
    public LanguageStatsBean()
    {
        mWordLength = new int[0];
        mInitialSyllableTable = new int[0];
        mFinalSyllableTable = new int[0];
        mFirstConsonant = new int[0];
        mLastConsonant = new int[0];
        mVowel = new int[0];
        mFirstConsonantText = new String[0];
        mLastConsonantText = new String[0];
        mVowelText = new String[0];
    }
}
