package com.app.service.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.app.service.R;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.Locale;

public class SplashActivity extends AppCompatActivity {

    /**
     * Duration of wait
     **/
    private final int SPLASH_DISPLAY_LENGTH = 2000;
    int logedIn = 0;
    String countryID = "0",selectedLang;

    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_splash);
            Hawk.init(getApplicationContext())
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                    .setStorage(HawkBuilder.newSqliteStorage(getApplicationContext()))
                    .setLogLevel(LogLevel.FULL)
                    .build();

            /* New Handler to start the Menu-Activity
             * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        selectedLang=Hawk.get("selectedLang","en");
                        languageSelection();

//                        // Start your app main activity
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        String j = msg;
                    }
//
                }
            }, SPLASH_DISPLAY_LENGTH);
        } catch (Exception ex) {
            String ms = ex.getMessage().toString();
            String y = ms;
        }
    }


    public void languageSelection() {
        try {
            Locale locale = new Locale(selectedLang);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLayoutDirection(locale);
            }

            resources.updateConfiguration(config, resources.getDisplayMetrics());

            preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            countryID = preferences.getString("countryID", "0");
            if (countryID.equals("0")) {
                Intent i = new Intent(SplashActivity.this, SelectCountry_Activity.class);
                startActivity(i);
                finish();
            } else {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

        } catch (Exception ex) {
            Log.v("languageSelection", ex.getMessage().toString());
        }
    }
}