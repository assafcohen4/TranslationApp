package com.cohen.assaf.emptywords.model;

import java.util.List;

/**
 * Created by User on 25/09/2015.
 */
public class User {
    private String mDeviceId;
    private List<WordPair> mStoredWordPairs;

    public String getDeviceId() {
        return mDeviceId;
    }

    public void setDeviceId(String deviceId) {
        mDeviceId = deviceId;
    }

    public List<WordPair> getStoredWordPairs() {
        return mStoredWordPairs;
    }

    public void setStoredWordPairs(List<WordPair> storedWordPairs) {
        mStoredWordPairs = storedWordPairs;
    }
}
