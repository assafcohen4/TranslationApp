package com.cohen.assaf.emptywords;

import android.app.IntentService;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;
import com.cohen.assaf.emptywords.model.Language;
import com.cohen.assaf.emptywords.views.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.List;
import android.provider.Settings.Secure;


public class TranslationService  extends IntentService implements ClipboardManager.OnPrimaryClipChangedListener {

    public static final String WORD_COUPLE = "wordCouple";
    public static final String ORIGINAL = "original";
    public static final String TRANSLATION = "translation";

    private Language mFrom;
    private Language mTo;
    private String mTranslation;
    private ClipboardManager mClipboardManager;
    private String mDeviceId;



    public TranslationService() {
        super("hello");
    }

    public TranslationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mFrom = (Language) intent.getSerializableExtra(MainActivity.FROM_STRING);
        mTo = (Language) intent.getSerializableExtra(MainActivity.TO_STRING);
        mClipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        mClipboardManager.addPrimaryClipChangedListener(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrimaryClipChanged() {

        getDeviceId();
        ClipData clip = mClipboardManager.getPrimaryClip();
        String copiedText = clip.getItemAt(0).getText().toString().toLowerCase();
        connectToTranslationApi(copiedText);

        //TODO solve concurrency problems if app works inconsistently.
        try {
           Thread.sleep(500);
        } catch (InterruptedException e) {
           e.printStackTrace();
        }

            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getBaseContext(), mTranslation, Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void connectToTranslationApi(final String copiedPhrase) {

        String apiKey = "trnsl.1.1.20150917T184839Z.e36dbb39d75e6e56.b7811de6c60065b231c28b6fc443be98491ee87c";
        String fromInitials = mFrom.initials;
        String toInitials = mTo.initials;
        String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key="+
                apiKey + "&lang=" +
                fromInitials + "-" + toInitials +
                "&text="+ copiedPhrase;

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(yandexUrl).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                mTranslation = "Could not connect to network";
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (response.isSuccessful()) {
                    String jsonData = response.body().string();
                    try {
                        mTranslation = getTextTranslation(jsonData);
                        connectToParse(copiedPhrase, mTranslation);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    private String getTextTranslation(String jsonData) throws JSONException {
        JSONObject data = new JSONObject(jsonData);
        String translation = data.getString("text");
       translation = translation.replace("]", "")
                    .replace("[", "")
                    .replace("\"", "");
        return translation;
    }

    private void connectToParse(final String original, String translation){





        final ParseObject wordCouple = new ParseObject(WORD_COUPLE);
        wordCouple.put(ORIGINAL, original);
        wordCouple.put(TRANSLATION, translation);


        ParseQuery<ParseObject> query = ParseQuery.getQuery(WORD_COUPLE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (checkIfWordCoupleAlreadyExists(list, original) == false) {
                    wordCouple.saveInBackground();
                }
            }
        });
    }

    private boolean checkIfWordCoupleAlreadyExists(List<ParseObject> list, String original){
        boolean doesCoupleExist = false;
        for (ParseObject wordCouple : list) {
            if (wordCouple.getString(ORIGINAL).toLowerCase().equals(original)) {
                doesCoupleExist = true;
                break;
            }
        }
        return doesCoupleExist;
    }

    private void getDeviceId(){
        mDeviceId =  Secure.getString(this.getContentResolver(),
                Secure.ANDROID_ID);
    }
}
