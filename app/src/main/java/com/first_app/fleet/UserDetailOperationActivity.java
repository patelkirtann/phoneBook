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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;

public class UserDetailOperationActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;

    public String name, id, number, address, street, city, location, intro;
    public Context context = UserDetailOperationActivity.this;
    private TextView mName, mPhoneNumber, mEmailAddress, mMapLocation, mIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_operation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        FloatingActionButton mEdit = (FloatingActionButton) findViewById(R.id.fab_edit);

        mName = (TextView) findViewById(R.id.tv_name);
        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLocation = (TextView) findViewById(R.id.tv_location);
        mIntro = (TextView) findViewById(R.id.tv_info);

        if (savedInstanceState == null) {

            getData();

        } else {
            name = (String) savedInstanceState.getSerializable("NAME_INTENT");
            id = (String) savedInstanceState.getSerializable("ID_INTENT");
            number = (String) savedInstanceState.getSerializable("PHONE_NUMBER_INTENT");
            address = (String) savedInstanceState.getSerializable("EMAIL_ADDRESS_INTENT");
            location = savedInstanceState.getSerializable("STREET_INTENT") +
                    " " + savedInstanceState.getSerializable("CITY_INTENT");
            intro = (String) savedInstanceState.getSerializable("INTRO_INTENT");
        }

        setData();

        mPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPhoneOnClick();
            }
        });

        mEmailAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setEmailOnClick();
            }
        });

        mMapLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocationOnClick();
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendDataToUpdate();
            }
        });
    }

    private void sendDataToUpdate() {
        Intent intent = new Intent(this, UpdateActivity.class);
        intent.putExtra("NAME_INTENT", name);
        intent.putExtra("ID_INTENT", id);
        intent.putExtra("PHONE_NUMBER_INTENT", number);
        intent.putExtra("EMAIL_ADDRESS_INTENT", address);
        intent.putExtra("STREET_INTENT", street);
        intent.putExtra("CITY_INTENT", city);
        intent.putExtra("INTRO_INTENT", intro);

        startActivityForResult(intent, 1);
    }

    private void getData() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            name = extras.getString("NAME_INTENT");
            id = extras.getString("ID_INTENT");
            number = extras.getString("PHONE_NUMBER_INTENT");
            address = extras.getString("EMAIL_ADDRESS_INTENT");
            street = extras.getString("STREET_INTENT");
            city = extras.getString("CITY_INTENT");
            location = street + " " + city;

            intro = extras.getString("INTRO_INTENT");
        } else {
            Toast.makeText(context, "No Contact to Fetch", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData() {
        mName.setText(name.toUpperCase().trim());
        mEmailAddress.setText(address.toLowerCase());
        mPhoneNumber.setText(number);
        mIntro.setText(intro);
        if (!location.equals(" ")) {
            mMapLocation.setText(location);
        } else {
            mMapLocation.setText("");
        }

    }


    private void moveToPhone() {
        AlertDialog.Builder dialog = new AlertDialog
                .Builder(UserDetailOperationActivity.this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Move to your phone contacts?");
        dialog.setPositiveButton("Move to Phone", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, address);
                startActivity(addContactIntent);
            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    private void onContactDeleted() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("Confirmation");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setMessage("Delete Contact?");
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                Toast.makeText(context, " Canceled ",
//                        Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(R.id.sv_scroll), "Canceled",
                        Snackbar.LENGTH_SHORT)
                        .show();
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

                setResult(RESULT_OK);
                onBackPressed();
            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    private void setPhoneOnClick() {

        final AlertDialog.Builder dialog = new AlertDialog
                .Builder(UserDetailOperationActivity.this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Call or Copy?");

        dialog.setPositiveButton("Call", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Calling...",
                        Toast.LENGTH_SHORT).show();

                Intent mCallIntent = new Intent(Intent.ACTION_CALL);
                mCallIntent.setData(Uri.parse("tel:" + number));
                try {
                    if (isCallPermissionGranted()) {
                        startActivity(mCallIntent);
                    } else {
                        Snackbar.make(findViewById(R.id.sv_scroll), "Grant Permission to call",
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                } catch (Exception e) {
                    Snackbar.make(findViewById(R.id.sv_scroll), "Try Again",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });

        dialog.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                copyText();

            }
        });

        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = dialog.create();
        alert.show();
    }


    private void setEmailOnClick() {

        final AlertDialog.Builder dialog = new AlertDialog
                .Builder(UserDetailOperationActivity.this);
        dialog.setTitle("Confirmation");
        dialog.setMessage("Send Email to " + address + "?");

        dialog.setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending from Fleet");
                emailIntent.setType("plain/text");

                startActivity(Intent.createChooser(emailIntent, " Send... "));
            }
        });

        dialog.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                copyText();
            }
        });

        dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        AlertDialog alert = dialog.create();
        alert.show();

    }

    private void setLocationOnClick() {

        final Uri[] uri = new Uri[1];
        if (!location.equals(" ")) {

            final AlertDialog.Builder dialog = new AlertDialog
                    .Builder(UserDetailOperationActivity.this);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Send Email to " + address + "?");

            dialog.setPositiveButton("GOTO Map", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    uri[0] = Uri.parse("geo:0,0?q=" + location);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri[0]);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);

                }
            });

            dialog.setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    copyText();
                }
            });

            dialog.setNeutralButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            AlertDialog alert = dialog.create();
            alert.show();
        } else {
            Snackbar.make(findViewById(R.id.sv_scroll), "Location not found",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
    }

    private void copyText() {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip =
                android.content.ClipData.newPlainText("", number);
        clipboard.setPrimaryClip(clip);
        Snackbar.make(findViewById(R.id.sv_scroll), "Text Copied",
                Snackbar.LENGTH_SHORT)
                .show();
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
        outState.putString("STREET_INTENT", street);
        outState.putString("CITY_INTENT", city);
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
            case R.id.export_contact:

                moveToPhone();

                break;
            case android.R.id.home:

                onBackPressed();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                name = data.getStringExtra("NAME_INTENT");
                id = data.getStringExtra("ID_INTENT");
                number = data.getStringExtra("PHONE_NUMBER_INTENT");
                address = data.getStringExtra("EMAIL_ADDRESS_INTENT");
                street = data.getStringExtra("STREET_INTENT");
                city = data.getStringExtra("CITY_INTENT");
                location = street + " " + city;
                intro = data.getStringExtra("INTRO_INTENT");

                setData();
            }
        }
    }
}
