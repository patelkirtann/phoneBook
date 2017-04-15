package com.contact_app.fleet;

import android.content.Context;
import android.widget.EditText;

/**
 * Created by kt_ki on 4/13/2017.
 */

public class CommonNamesInUse extends android.support.v7.widget.AppCompatEditText {

    EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;

    public CommonNamesInUse(Context context) {
        super(context);
        mName = (EditText) findViewById(R.id.name_field);
        mEmail = (EditText) findViewById(R.id.email_field);
        mPhone = (EditText) findViewById(R.id.phone_field);
        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);
    }


}
