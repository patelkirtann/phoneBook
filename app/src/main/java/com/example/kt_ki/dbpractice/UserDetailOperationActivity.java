package com.example.kt_ki.dbpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailOperationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;
    Button mPhone, mEmail, mMap;
    TextView mPhoneNumber, mEmailAddress, mMapLoacation;

    Intent mCallIntent = new Intent(Intent.ACTION_CALL);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_operation);
        mPhone = (Button) findViewById(R.id.bt_gotoCall);
        mEmail = (Button) findViewById(R.id.bt_gotoEmail);
        mMap = (Button) findViewById(R.id.bt_gotoMap);

        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLoacation = (TextView) findViewById(R.id.tv_location);


        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserDetailOperationActivity.this, "PhoneOnClick",
                        Toast.LENGTH_SHORT).show();
                mCallIntent.setData(Uri.parse("tel:"+ "5625416725"));
                if (ActivityCompat.checkSelfPermission(UserDetailOperationActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(
                            UserDetailOperationActivity.this,
                            Manifest.permission.CALL_PHONE)) {

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(UserDetailOperationActivity.this,
                                new String[]{Manifest.permission.CALL_PHONE},
                                MY_PERMISSIONS_REQUEST_CALL_CONTACTS);

                        // MY_PERMISSIONS_REQUEST_CALL_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }
                startActivity(mCallIntent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
