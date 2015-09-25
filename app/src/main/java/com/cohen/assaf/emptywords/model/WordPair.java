package com.cohen.assaf.emptywords.model;

/**
 * Created by User on 25/09/2015.
 */
public class WordPair {

        private String mOriginal;
        private String mTranslation;

    public String getOriginal() {
        return mOriginal;
    }

    public void setOriginal(String original) {
        mOriginal = original;
    }

    public String getTranslation() {
        return mTranslation;
    }

    public void setTranslation(String translation) {
        mTranslation = translation;
    }

    public WordPair(String original, String translation)
        {
            mOriginal   =  original;
            mTranslation = translation;
        }



}
