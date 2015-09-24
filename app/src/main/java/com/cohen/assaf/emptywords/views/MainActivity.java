package com.cohen.assaf.emptywords.views;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.cohen.assaf.emptywords.model.Language;
import com.cohen.assaf.emptywords.R;
import com.cohen.assaf.emptywords.TranslationService;
import com.parse.Parse;
import com.parse.ParseObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    //TODO nice gui,
    // solve concurrency,
    // proper notification bar,
    // stop service,
    // proper widget,
    // multi user on parse,
    public static final int FROM = 1;
    public static final int TO = 2;
    public static final String FROM_STRING = "from";
    public static final String TO_STRING = "to";

    private Language mLanguageFrom;
    private Language mLanguageTo;

    @Bind(R.id.from_button) Button fromButton;
    @Bind(R.id.to_button) Button toButton;
    @Bind(R.id.start_button) Button startButton;


    @OnClick(R.id.from_button)
    public void choseLanguageFrom(){
        Intent i = new Intent(this, LanguageListActivity.class);
        startActivityForResult(i, 1);

    }

    @OnClick(R.id.to_button)
    public void chooseLanguageTo(){
        Intent i = new Intent(this, LanguageListActivity.class);
        startActivityForResult(i, 2);
    }

    @OnClick(R.id.stop_button)
    public void stopTranslationService(){
        Intent i = new Intent(MainActivity.this, TranslationService.class);
        stopService(i);
    }

    @OnClick(R.id.start_button)
    public void startTranslationService(){
            Intent i = new Intent(MainActivity.this, TranslationService.class);
            i.putExtra(FROM_STRING, mLanguageFrom)
             .putExtra(TO_STRING, mLanguageTo);
            startService(i);
            createNotification();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "MIc9O1nwpQa5lKNuPkdtzXcPRTB8Mg6HxWmoaTbI", "G59SiIMtEsTIA7846icPsapsg8MWafaGRPaJNVZ2");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ImageView flagFrom = (ImageView) findViewById(R.id.flag_from);
        ImageView flagTo = (ImageView) findViewById(R.id.flag_to);

        if(resultCode == RESULT_OK) {
            Language language = (Language) data.getSerializableExtra(LanguageListActivity.RESULT);
            if (requestCode == FROM) {
                mLanguageFrom = language;
                flagFrom.setImageResource(language.icon);
                Toast.makeText(this, "From " + mLanguageFrom, Toast.LENGTH_SHORT).show();

            }
            if (requestCode == TO) {
                mLanguageTo = language;
                flagTo.setImageResource(language.icon);
                Toast.makeText(this, "To " + mLanguageTo, Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void createNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.france)
                .setContentTitle("Translation Service")
                .setContentText("Running");
        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class)
                .addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        notificationManager.notify(1, builder.build());
    }


}
