package com.example.kt_ki.dbpractice;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static com.google.android.gms.internal.zzs.TAG;

public class UserDetailOperationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;
    Button mPhone, mEmail, mMap;
    TextView mPhoneNumber, mEmailAddress, mMapLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_operation);
        mPhone = (Button) findViewById(R.id.bt_gotoCall);
        mEmail = (Button) findViewById(R.id.bt_gotoEmail);
        mMap = (Button) findViewById(R.id.bt_gotoMap);

        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLocation = (TextView) findViewById(R.id.tv_location);


        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserDetailOperationActivity.this, "Calling...",
                        Toast.LENGTH_SHORT).show();
                Intent mCallIntent = new Intent(Intent.ACTION_CALL);
                mCallIntent.setData(Uri.parse("tel:" + "5625416725"));
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
}
