package com.contact_app.fleet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {

    private static final int GALLERY_IMAGE = 5;
    private static final int MY_PERMISSIONS_REQUEST_PICTURE_CONTACTS = 1;
    public EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;
    public DBForm dbForm;
    public TextInputLayout nameLayout, emailLayout, phoneLayout;
    byte[] byteArray;
    boolean check = true;
    private CircleImageView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);
        dbForm = DBForm.getInstance(this);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mPicture = (CircleImageView) findViewById(R.id.profile_image);

        nameLayout = (TextInputLayout) findViewById(R.id.name_field_layout);
        mName = (EditText) findViewById(R.id.name_field);

        emailLayout = (TextInputLayout) findViewById(R.id.email_field_layout);
        mEmail = (EditText) findViewById(R.id.email_field);

        phoneLayout = (TextInputLayout) findViewById(R.id.phone_field_layout);
        mPhone = (EditText) findViewById(R.id.phone_field);

        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);

        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromSdCard();
            }
        });

    }

    private void onSaveClicked() {
        if (!isNameEmpty(mName)
                && mPhone.length() >= 10
                && !dbForm.checkName(mName.getText().toString().toLowerCase().trim())) {
            if (!mEmail.getText().toString().isEmpty()) {
                if (!isEmailValid(mEmail.getText().toString().trim())) {
                    emailLayout.setError("Not a valid email");
                    check = false;
                } else {
                    emailLayout.setErrorEnabled(false);
                    check = true;
                }
            }
            if (check) {
                if (addedToRecords()) {
                    nameLayout.setErrorEnabled(false);
                    phoneLayout.setErrorEnabled(false);
                    emailLayout.setErrorEnabled(false);

                    final AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
                    dialog.setTitle("Confirmation");
                    dialog.setMessage("Save Contact?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Toast.makeText(AddActivity.this, " Contact Saved ",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            onBackPressed();

                        }
                    });
                    dialog.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddActivity.this, " Discarded ", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });

                    dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                    Toast.makeText(AddActivity.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    Toast.makeText(AddActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                }
            } else {

                Toast.makeText(AddActivity.this,
                        "Fill all required filed with valid input",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            if (isNameEmpty(mName)) {
                nameLayout.setError("Put valid name(2 or more character)");
            } else if (mPhone.length() < 10) {
                phoneLayout.setError("Put valid number(xxx-xxx-xxxx)");
                nameLayout.setErrorEnabled(false);
            } else if (dbForm.checkName(mName.getText().toString().toLowerCase().trim())) {
                phoneLayout.setErrorEnabled(false);
                nameLayout.setError("Duplicate name found \n Try something unique");
            }
            Toast.makeText(AddActivity.this,
                    "Fill all required filed with valid input",
                    Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNameEmpty(EditText edit) {
        String text = edit.getText().toString().replaceAll(" ", "");

        return !(!text.trim().isEmpty() &&
                text.length() >= 2);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean addedToRecords() {

        try {

            String nameText = mName.getText().toString().toLowerCase().trim();
            String emailText = mEmail.getText().toString().toLowerCase();
            String phoneText = mPhone.getText().toString();
            String streetText = mStreet.getText().toString();
            String cityText = mCity.getText().toString();
            String introText = mIntro.getText().toString();

            for (int i = 0; i < 50; i++) {
                dbForm.insertValue(nameText, emailText, phoneText,
                        streetText, cityText, introText, byteArray);
                dbForm.close();
            }


            return true;

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong while saving contact",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.save:
                onSaveClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setImageFromSdCard(Uri selectedImage) {

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap yourSelectedImage = transform(BitmapFactory.decodeStream(imageStream));

        mPicture.setImageBitmap(yourSelectedImage);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (yourSelectedImage != null) {
            yourSelectedImage.compress(Bitmap.CompressFormat.JPEG, 30, stream);
        }
        byteArray = stream.toByteArray();
    }

    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 4;
        int y = (source.getHeight() - size) / 4;
        Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    public void getImageFromSdCard() {

        if (isExternalStoragePermissionGranted()) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, GALLERY_IMAGE);
        }

    }

    public boolean isExternalStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
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
            case MY_PERMISSIONS_REQUEST_PICTURE_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();
                    getImageFromSdCard();

                } else {
                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_IMAGE) {

                Uri selectedImage = data.getData();
                setImageFromSdCard(selectedImage);

            }
        }
    }

}
