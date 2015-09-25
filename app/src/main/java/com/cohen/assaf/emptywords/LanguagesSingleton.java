package com.cohen.assaf.emptywords;

import com.cohen.assaf.emptywords.model.Language;

/**
 * Created by User on 20/09/2015.
 */
public class LanguagesSingleton {

    private static LanguagesSingleton instance = null;
    private static Language[] mLanguagesList;
    protected LanguagesSingleton() {
        mLanguagesList = new Language[]
                {
                        new Language(R.drawable.united_kingdom, "English", "en"),
                        new Language(R.drawable.france, "French", "fr"),
                        new Language(R.drawable.israel, "Hebrew", "he"),
                        new Language(R.drawable.germany, "German", "de")
                };
    }

    public static Language[] getAllLanguagesList() {
        return mLanguagesList;
    }

    public static Language getLanguage(int position){
        return mLanguagesList[position];
    }

    public static LanguagesSingleton getInstance() {
        if(instance == null) {
            instance = new LanguagesSingleton();
        }
        return instance;
    }
}

