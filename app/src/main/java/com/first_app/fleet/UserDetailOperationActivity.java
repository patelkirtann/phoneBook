package com.first_app.fleet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailOperationActivity extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;
    Button mPhone, mEmail, mMap;
    TextView mName, mID, mPhoneNumber, mEmailAddress, mMapLocation, mIntro;
    String name, id, number, address, location, intro;
    QuickContactBadge mSavePhone, mSaveEmail;
    Context context = UserDetailOperationActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_operation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mPhone = (Button) findViewById(R.id.bt_gotoCall);
        mEmail = (Button) findViewById(R.id.bt_gotoEmail);
        mMap = (Button) findViewById(R.id.bt_gotoMap);

        mName = (TextView) findViewById(R.id.tv_name);
        mID = (TextView) findViewById(R.id.tv_id);
        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLocation = (TextView) findViewById(R.id.tv_location);
        mIntro = (TextView) findViewById(R.id.tv_info);

        mSavePhone = (QuickContactBadge) findViewById(R.id.cb_SavePhone);
        mSaveEmail = (QuickContactBadge) findViewById(R.id.cb_saveEmail);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                name = "n/a";
                id = "n/a";
                number = "n/a";
                address = "n/a";
                location = "n/a";
                intro = "n/a";

            } else {
                name = extras.getString("NAME_INTENT");
                id = extras.getString("ID_INTENT");
                number = extras.getString("PHONE_NUMBER_INTENT");
                address = extras.getString("EMAIL_ADDRESS_INTENT");
                location = extras.getString("MAP_LOCATION_INTENT");
                intro = extras.getString("INTRO_INTENT");

            }
        } else {
            name = (String) savedInstanceState.getSerializable("NAME_INTENT");
            id = (String) savedInstanceState.getSerializable("ID_INTENT");
            number = (String) savedInstanceState.getSerializable("PHONE_NUMBER_INTENT");
            address = (String) savedInstanceState.getSerializable("EMAIL_ADDRESS_INTENT");
            location = (String) savedInstanceState.getSerializable("MAP_LOCATION_INTENT");
            intro = (String) savedInstanceState.getSerializable("INTRO_INTENT");
        }

        mName.setText(name);
//        mID.setText("#"+id);
        mEmailAddress.setText(address);
        mPhoneNumber.setText(number);
        mMapLocation.setText(location);
        mIntro.setText(intro);

        mSavePhone.assignContactFromPhone(number, true);
        mSavePhone.setMode(ContactsContract.QuickContact.MODE_SMALL);

        mSaveEmail.assignContactFromEmail(address, true);
        mSavePhone.setMode(ContactsContract.QuickContact.MODE_SMALL);


        mPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Calling...",
                        Toast.LENGTH_SHORT).show();

                Intent mCallIntent = new Intent(Intent.ACTION_CALL);
                mCallIntent.setData(Uri.parse("tel:" + number));
                try {
                    if (isCallPermissionGranted()) {
                        startActivity(mCallIntent);
                    } else {
                        Toast.makeText(context, "Grant Permission to call ",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending from Fleet");
                emailIntent.setType("message/rfc822");

                startActivity(Intent.createChooser(emailIntent, " Sending... "));
            }
        });

        mMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                return true;
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

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

        outState.putString("NAME_INTENT", name);
        outState.putString("ID_INTENT", id);
        outState.putString("PHONE_NUMBER_INTENT", number);
        outState.putString("EMAIL_ADDRESS_INTENT", address);
        outState.putString("MAP_LOCATION_INTENT", location);
        outState.putString("INTRO_INTENT", intro);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.delete_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_contact:
                onContactDeleted();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onContactDeleted() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Delete Contact?");
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(context, " Canceled ",
                        Toast.LENGTH_SHORT).show();
            }
        });
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DBForm dbForm = new DBForm(context);
                dbForm.deleteContactByID(id);
                dbForm.close();
                Toast.makeText(context, " Contact Deleted ",
                        Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();

    }
}
