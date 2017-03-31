package com.contact_app.fleet;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;

public class UserDetailOperationActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 0;
    private static int UPDATE_TOKEN = -1;

    public String name, id, number, address, street, city, location, intro;
    public Context context = UserDetailOperationActivity.this;
    DBForm db = new DBForm(this);
    private TextView mName, mPhoneNumber, mEmailAddress, mMapLocation, mIntro;
    private ImageView mDisplayPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_operation);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setElevation(0);
        }

        mDisplayPic = (ImageView) findViewById(R.id.iv_photo);
        mName = (TextView) findViewById(R.id.tv_name);
        mPhoneNumber = (TextView) findViewById(R.id.tv_phoneNumber);
        mEmailAddress = (TextView) findViewById(R.id.tv_emailAddress);
        mMapLocation = (TextView) findViewById(R.id.tv_location);
        mIntro = (TextView) findViewById(R.id.tv_info);

        if (savedInstanceState == null) {

            getData();

        } else {
            name = savedInstanceState.getString("NAME_INTENT");

            UserRecord record = db.getContactRow(name);
            setData(record);
        }



        mPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCallOnClick();
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

        mIntro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intro.isEmpty()) {
                    Snackbar.make(findViewById(R.id.sv_scroll), "Intro not found",
                            Snackbar.LENGTH_LONG)
                            .setAction("Add", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    sendDataToUpdate();
                                }
                            })
                            .show();
                }
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
            UserRecord record = db.getContactRow(name);
            setData(record);

        } else {
            Toast.makeText(context, "No Contact to Fetch", Toast.LENGTH_SHORT).show();
        }
    }

    private void setData(UserRecord record) {
        mName.setText(record.getName());
        mEmailAddress.setText(record.getEmail());
        mPhoneNumber.setText(record.getPhone());
        mIntro.setText(record.getIntro());

        street = record.getStreet();
        city = record.getCity();

        location = street + ", " + city;
        if (!location.equals(", ")) {
            mMapLocation.setText(location);
        } else {
            mMapLocation.setText("");
        }

        byte[] imgByte = record.getPicture();
        if (imgByte != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imgByte);
            mDisplayPic.setImageBitmap(BitmapFactory.decodeStream(imageStream));
        } else {
            mDisplayPic.setImageResource(R.drawable.ic_person_pin);
        }
        db.close();
    }


    private void moveToPhone() {
        AlertDialog.Builder dialog = new AlertDialog
                .Builder(UserDetailOperationActivity.this)
                .setTitle("Confirmation")
                .setMessage("Move to your phone contacts?")
                .setPositiveButton("Move to Phone", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent addContactIntent = new Intent(Intent.ACTION_INSERT);
                        addContactIntent.setType(ContactsContract.Contacts.CONTENT_TYPE);
                        addContactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);
                        addContactIntent.putExtra(ContactsContract.Intents.Insert.PHONE, number);
                        addContactIntent.putExtra(ContactsContract.Intents.Insert.EMAIL, address);
                        startActivity(addContactIntent);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    private void onContactDeleted() {

        final AlertDialog.Builder dialog = new AlertDialog.Builder(context)
                .setTitle("Confirmation")
                .setIcon(R.drawable.ic_warning)
                .setMessage("Delete Contact?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Snackbar.make(findViewById(R.id.sv_scroll), "Canceled",
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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

    private void setCallOnClick() {

        final AlertDialog.Builder dialog = new AlertDialog
                .Builder(UserDetailOperationActivity.this)
                .setTitle("Confirmation")
                .setMessage("Call " + name + "?")
                .setPositiveButton("Call", new DialogInterface.OnClickListener() {
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
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();
    }


    private void setEmailOnClick() {

        if (!address.isEmpty()) {
            final AlertDialog.Builder dialog = new AlertDialog
                    .Builder(UserDetailOperationActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("Send Email to " + address + "?")
                    .setPositiveButton("Send Email", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent emailIntent = new Intent(Intent.ACTION_SEND);

                            emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{address});
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Sending from Fleet");
                            emailIntent.setType("plain/text");

                            startActivity(Intent.createChooser(emailIntent, " Send... "));
                        }
                    })
                    .setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alert = dialog.create();
            alert.show();
        } else {
            Snackbar.make(findViewById(R.id.sv_scroll), "Email not found",
                    Snackbar.LENGTH_LONG)
                    .setAction("Add", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendDataToUpdate();
                        }
                    })
                    .show();
        }

    }

    private void setLocationOnClick() {

        final Uri[] uri = new Uri[1];
        if (!location.equals(" ")) {

            final AlertDialog.Builder dialog = new AlertDialog
                    .Builder(UserDetailOperationActivity.this)
                    .setTitle("Confirmation")
                    .setMessage("Find Location in Map?")
                    .setPositiveButton("GOTO Map", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            uri[0] = Uri.parse("geo:0,0?q=" + location);
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, uri[0]);
                            mapIntent.setPackage("com.google.android.apps.maps");
                            startActivity(mapIntent);

                        }
                    })
                    .setNegativeButton("Copy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            copyText();

                        }
                    });

            AlertDialog alert = dialog.create();
            alert.show();
        } else {
            Snackbar.make(findViewById(R.id.sv_scroll), "Location not found",
                    Snackbar.LENGTH_LONG)
                    .setAction("Add", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            sendDataToUpdate();
                        }
                    })
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

    private void shareDetails() {
        String mimeType = "text/plain";
        String title = "Share with,";
        String text = name + "\nPhn:" + number + "\nEmail:" + address;
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(text)
                .startChooser();
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
        } else {
            //permission is automatically granted on sdk<23 upon installation

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
                    Snackbar.make(findViewById(R.id.sv_scroll), "Permission is granted",
                            Snackbar.LENGTH_SHORT)
                            .show();

                } else {

                    Snackbar.make(findViewById(R.id.sv_scroll), "Permission is Denied",
                            Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("NAME_INTENT", name);

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
            case R.id.share_contact:

                shareDetails();

                break;
            case R.id.edit_contact:

                sendDataToUpdate();

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
                UserRecord record = db.getContactRow(name);

                UPDATE_TOKEN = 2;
                setData(record);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (UPDATE_TOKEN == 2) {
            setResult(UPDATE_TOKEN);
            UPDATE_TOKEN = -1;
        }
        super.onBackPressed();
    }
}
