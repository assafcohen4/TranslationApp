package com.cohen.assaf.emptywords;

import android.app.IntentService;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.ClipboardManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.cohen.assaf.emptywords.model.Database;
import com.cohen.assaf.emptywords.model.Language;
import com.cohen.assaf.emptywords.model.TranslationServiceConnector;
import com.cohen.assaf.emptywords.model.WordPair;
import com.cohen.assaf.emptywords.views.MainActivity;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.List;


public class TranslationService  extends IntentService implements ClipboardManager.OnPrimaryClipChangedListener {

    public static final String WORD_COUPLE = "wordCouple";
    public static final String ORIGINAL = "original";
    public static final String TRANSLATION = "translation";

    private Language mFrom;
    private Language mTo;
    private WordPair mPair;
    private ClipboardManager mClipboardManager;
    private Database mDatabase;


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
        mDatabase = new Database(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onPrimaryClipChanged() {

        ClipData clip = mClipboardManager.getPrimaryClip();
        final String copiedText = clip.getItemAt(0).getText().toString().toLowerCase();
        String apiKey = "trnsl.1.1.20150917T184839Z.e36dbb39d75e6e56.b7811de6c60065b231c28b6fc443be98491ee87c";
        final TranslationServiceConnector connector = new TranslationServiceConnector(copiedText,mFrom,mTo, apiKey);
                try {
                    mPair = connector.getOriginalAndTranslationAsWordPair();
                    while(mPair == null){
                        continue;
                    }
                    if(mDatabase.IsPairAlreadyInDatabase(mPair) == false)
                    {
                        mDatabase.storeWordPair(mPair);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                showTranslationAsToast(mPair.getTranslation());

    }

    private void showTranslationAsToast(final String translation){
       new Handler(Looper.getMainLooper()).post(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(getBaseContext(), translation, Toast.LENGTH_SHORT).show();
           }
       });
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
}
