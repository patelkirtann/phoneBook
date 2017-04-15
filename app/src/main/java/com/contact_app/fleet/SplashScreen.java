package com.contact_app.fleet;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    private static final int SPLASH_TIME_OUT = 1000;
    private static int sOnlyOnce = 0;
    final String welcomeScreenShownPref = "welcomeScreenShown";
    SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Intent i = new Intent(this, ListActivity.class);
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        final Boolean welcomeScreenShown = mPrefs.getBoolean(welcomeScreenShownPref, false);

        if (sOnlyOnce == 0) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    sOnlyOnce = -1;
                    if (!welcomeScreenShown) {

                        Intent intro = new Intent(SplashScreen.this, IntroActivity.class);
                        startActivity(intro);
                        finish();

                        SharedPreferences.Editor editor = mPrefs.edit();
                        editor.putBoolean(welcomeScreenShownPref, true);
                        editor.apply();
                    }else {
                        startActivity(i);
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        } else {
            finish();
            startActivity(i);
        }
    }
}
