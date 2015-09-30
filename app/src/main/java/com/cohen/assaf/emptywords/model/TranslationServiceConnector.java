package com.cohen.assaf.emptywords.model;

import android.util.Log;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by User on 26/09/2015.
 */
public class TranslationServiceConnector {

    private String mText;
    private Language mFrom;
    private Language mTo;
    private String mApiKey;

    public TranslationServiceConnector(String text, Language from, Language to, String apiKey) {
        mText = text;
        mFrom = from;
        mTo = to;
        mApiKey = apiKey;
    }

    public WordPair getOriginalAndTranslationAsWordPair() throws IOException {

        final String[] translation = new String[1];
        String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="+
                mApiKey + "&lang=" +
                mFrom.initials + "-" + mTo.initials +
                "&text="+ mText;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(yandexUrl).build();
        final Call[] call = {okHttpClient.newCall(request)};
        call[0].enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                translation[0] = "Could not connect to network";
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        translation[0] = getTextFromJsonData(response.body().string());
                    } catch (JSONException e) {
                        translation[0] = "could not read understand word";
                    }
//todo connect to parse to store word
                }
            }
        });
        //this loop will give onResponse time to initialise translation[0]
        //if call.enqueue returns onResponse and getTextFromJsonData does not
        // throw an exception the loop will
        //always break within a few seconds
        while(translation[0] == null){
            continue;
        }
        return new WordPair(mText, translation[0]);
    }
    private String getTextFromJsonData(String jsonData) throws JSONException {
        JSONObject data = new JSONObject(jsonData);
        String translation = data.getString("text");
        translation = translation.replace("]", "")
                .replace("[", "")
                .replace("\"", "");
        return translation;
    }


 // region getters & setters
     public String getText() {
         return mText;
     }

    public void setText(String text) {
        mText = text;
    }
    public Language getFrom() {
        return mFrom;
    }

    public void setFrom(Language from) {
        mFrom = from;
    }

    public Language getTo() {
        return mTo;
    }

    public void setTo(Language to) {
        mTo = to;
    }

    public String getApiKey() {
        return mApiKey;
    }

    public void setApiKey(String apiKey) {
        mApiKey = apiKey;
    }
    // endregion
}
