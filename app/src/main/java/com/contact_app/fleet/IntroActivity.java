package com.contact_app.fleet;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

/**
 * Created by kt_ki on 3/19/2017.
 */

public class IntroActivity extends AppIntro implements ISlideBackgroundColorHolder {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(AppIntro2Fragment.newInstance("First page", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.first_screen, null)));
            addSlide(AppIntro2Fragment.newInstance("First page", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.second_screen, null)));
            addSlide(AppIntro2Fragment.newInstance("First page", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.third_screen, null)));

        } else {
            addSlide(AppIntro2Fragment.newInstance("", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.first_screen)));
            addSlide(AppIntro2Fragment.newInstance("First page", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.second_screen)));
            addSlide(AppIntro2Fragment.newInstance("First page", "Description", R.mipmap.ic_launcher,
                    getResources().getColor(R.color.third_screen)));
        }

    }

    @Override
    public void onSkipPressed(final Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        // Do something when users tap on Skip button.
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("Skip Introduction?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onDonePressed(currentFragment);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alertDialog = dialog.create();
        alertDialog.show();

    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent i = new Intent(this, ListActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }

    @Override
    public int getDefaultBackgroundColor() {
        return Color.parseColor("#000000");
    }

    @Override
    public void setBackgroundColor(@ColorInt int backgroundColor) {
        ViewGroup container = (ViewGroup) findViewById(android.R.id.content);
        if (container != null)
            container.setBackgroundColor(backgroundColor);

    }
}
