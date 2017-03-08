package com.example.kt_ki.dbpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.google.android.gms.internal.zzs.TAG;

public class UserDetailOperationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;
    Button mPhone, mEmail, mMap;
    TextView mName, mID, mPhoneNumber, mEmailAddress, mMapLocation, mIntro;
    String name , id, number, address, location, intro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_operation);
        mPhone = (Button) findViewById(R.id.bt_gotoCall);
        mEmail = (Button) findViewById(R.id.bt_gotoEmail);
        mMap = (Button) findViewById(R.id.bt_gotoMap);

        mName = (TextView) findViewById(R.id.tv_name);
        mID = (TextView) findViewById(R.id.tv_id);
        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLocation = (TextView) findViewById(R.id.tv_location);
        mIntro = (TextView) findViewById(R.id.tv_info);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                name = "n/a";
                id = "n/a";
                number= "n/a";
                address = "n/a";
                location = "n/a";
                intro= "n/a";

            } else {
                name = extras.getString("NAME_INTENT");
                id = extras.getString("ID_INTENT");
                number= extras.getString("PHONE_NUMBER_INTENT");
                address = extras.getString("EMAIL_ADDRESS_INTENT");
                location = extras.getString("MAP_LOCATION_INTENT");
                intro = extras.getString("INTRO_INTENT");

            }
        } else {
            name= (String) savedInstanceState.getSerializable("NAME_INTENT");
            id= (String) savedInstanceState.getSerializable("ID_INTENT");
            number= (String) savedInstanceState.getSerializable("PHONE_NUMBER_INTENT");
            address= (String) savedInstanceState.getSerializable("EMAIL_ADDRESS_INTENT");
            location= (String) savedInstanceState.getSerializable("MAP_LOCATION_INTENT");
            intro= (String) savedInstanceState.getSerializable("INTRO_INTENT");
        }

        mName.setText(name);
        mID.setText("#"+id);
        mEmailAddress.setText(address);
        mPhoneNumber.setText(number);
        mMapLocation.setText(location);
        mIntro.setText(intro);


        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserDetailOperationActivity.this, "Calling...",
                        Toast.LENGTH_SHORT).show();
                Intent mCallIntent = new Intent(Intent.ACTION_CALL);
                mCallIntent.setData(Uri.parse("tel:" + number));
                try{
                    if (isCallPermissionGranted()){
                        startActivity(mCallIntent);
                    }else {
                        Toast.makeText(UserDetailOperationActivity.this, "Grant Permission to call ",
                                Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(UserDetailOperationActivity.this, "Email Clicked",
//                        Toast.LENGTH_SHORT).show();

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL ,new String[]{address});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending from Fleet");
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent , " Sending... "));
            }
        });

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String addressString = "10705 Rose Ave, LA";

                Uri uri = Uri.parse("geo:0,0?q=" + location);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
    }

    public boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("NAME_INTENT" , name);
        outState.putString("ID_INTENT" , id);
        outState.putString("PHONE_NUMBER_INTENT" , number);
        outState.putString("EMAIL_ADDRESS_INTENT" , address);
        outState.putString("MAP_LOCATION_INTENT" , location);
        outState.putString("INTRO_INTENT" , intro);

    }
}
