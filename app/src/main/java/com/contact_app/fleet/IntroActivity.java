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
import android.view.View;
import android.view.ViewGroup;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;
import com.github.paolorotolo.appintro.ISlideBackgroundColorHolder;

public class IntroActivity extends AppIntro implements ISlideBackgroundColorHolder {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            addSlide(AppIntroFragment.newInstance("Add and Save",
                    "Click on round add button to create new contact",
                    R.drawable.add_save,
                    getResources().getColor(R.color.first_screen, null)));

            addSlide(AppIntroFragment.newInstance("Long Click",
                    "Long press on any contact to get call/sms menu",
                    R.drawable.long_click,
                    getResources().getColor(R.color.second_screen, null)));

            addSlide(AppIntroFragment.newInstance("Search By,",
                    "Search contact by their Name or Introduction",
                    R.drawable.name_intro,
                    getResources().getColor(R.color.third_screen, null)));

            addSlide(AppIntroFragment.newInstance("Move To regular Contact",
                    "When you feel the contact is valuable" +
                            " then move them to your regular contact list",
                    R.drawable.detail_move,
                    getResources().getColor(R.color.fourth_screen, null)));

        } else {
            addSlide(AppIntroFragment.newInstance("Add and Save",
                    "Click on round add button to create new contact",
                    R.drawable.add_save,
                    getResources().getColor(R.color.first_screen)));

            addSlide(AppIntroFragment.newInstance("Long Click",
                    "Long press on any contact to get call/sms menu",
                    R.drawable.long_click,
                    getResources().getColor(R.color.second_screen)));

            addSlide(AppIntroFragment.newInstance("Search By,",
                    "Search contact by their Name or Introduction",
                    R.drawable.name_intro,
                    getResources().getColor(R.color.third_screen)));

            addSlide(AppIntroFragment.newInstance("Move To regular Contact",
                    "When you feel the contact is valuable" +
                            " then move them to your regular contact list",
                    R.drawable.detail_move,
                    getResources().getColor(R.color.fourth_screen)));
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
