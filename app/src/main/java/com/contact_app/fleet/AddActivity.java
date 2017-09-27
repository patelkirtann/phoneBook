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
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.contact_app.fleet.CallLog.CallLogActivity;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddActivity extends AppCompatActivity {

    private static final int GALLERY_IMAGE = 5;
    private static final int MY_PERMISSIONS_REQUEST_PICTURE_CONTACTS = 1;
    private EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;
    private ImageButton btClockwise;
    private DBForm dbForm;
    private TextInputLayout nameLayout, emailLayout, phoneLayout;
    private byte[] imageByteArray = null;
    private Bitmap yourSelectedImage = null;
    private boolean check = true;
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

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            mPhone.setText(String.valueOf(bundle.getString("LOG_PHONE_NUMBER")));
        }

        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);

        btClockwise = (ImageButton) findViewById(R.id.bt_rotate_clockwise);
        btClockwise.setVisibility(View.GONE);

        btClockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int degree = 90;
                if (yourSelectedImage != null) {
                    rotate(degree);
                } else {
                    Toast.makeText(AddActivity.this,
                            "Select Image first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromSdCard();
            }
        });

    }

    private void rotate(int degree) {
        yourSelectedImage = ImageConverter.rotateImageBy(degree, yourSelectedImage);
        imageByteArray = ImageConverter.convertToByteArray(yourSelectedImage);
        Glide.with(this).load(imageByteArray).into(mPicture);
    }

    private void onSaveClicked() {
        if (!isNameEmpty(mName)
                && mPhone.length() >= 10
                && !dbForm.checkName(mName.getText().toString().toLowerCase().trim())) {
            if (!mEmail.getText().toString().isEmpty()) {
                if (!isEmailValid(mEmail.getText().toString().trim())) {
                    emailLayout.setError("Not a valid Email");
                    check = false;
                } else {
                    emailLayout.setErrorEnabled(false);
                    check = true;
                }
            }
            if (check) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
                dialog.setTitle("Confirmation");
                dialog.setMessage("Save Contact?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (addedToRecords()) {
                            nameLayout.setErrorEnabled(false);
                            phoneLayout.setErrorEnabled(false);
                            emailLayout.setErrorEnabled(false);
                            Toast.makeText(AddActivity.this, " Contact Saved ",
                                    Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
//                            startActivity(new Intent(getApplicationContext(), ListActivity.class));
                            onBackPressed();
                        }
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

                Toast.makeText(AddActivity.this,
                        "Fill all required filed with a valid input",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            if (isNameEmpty(mName)) {
                nameLayout.setError("Put a valid Name(2 or more character)");
            } else if (mPhone.length() < 10) {
                phoneLayout.setError("Put a valid Number(XXX-XXX-XXXX)");
                nameLayout.setErrorEnabled(false);
            } else if (dbForm.checkName(mName.getText().toString().toLowerCase().trim())) {
                phoneLayout.setErrorEnabled(false);
                nameLayout.setError("Duplicate Name found \n Try something Unique");
            }
            Toast.makeText(AddActivity.this,
                    "Fill all required filed with a valid input",
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

            dbForm.insertValue(nameText, emailText, phoneText,
                    streetText, cityText, introText, imageByteArray);
            dbForm.close();

            return true;

        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong while saving the contact",
                    Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_menu, menu);
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
        yourSelectedImage = ImageConverter.transform(BitmapFactory.decodeStream(imageStream));

//        mPicture.setImageBitmap(yourSelectedImage);

        imageByteArray = ImageConverter.convertToByteArray(yourSelectedImage);

        Glide.with(this).load(imageByteArray).asBitmap().into(mPicture);
    }

    public void getImageFromSdCard() {
        if (imageByteArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Source")
                    .setMessage("Select Pictures From Media Library")
                    .setCancelable(false)
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    })
                    .setPositiveButton("Select", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id1) {


                            if (isExternalStoragePermissionGranted()) {
                                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                intent.setType("image/*");
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(intent, GALLERY_IMAGE);
                                }
                            }

                        }
                    })
                    .setNeutralButton("Remove", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            btClockwise.setVisibility(View.GONE);

                            imageByteArray = null;
                            mPicture.setImageResource(R.drawable.ic_add_a_photo);
                        }
                    });

            AlertDialog alert = builder.create();
            alert.show();
        } else if (isExternalStoragePermissionGranted()) {
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
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
                    getImageFromSdCard();

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == GALLERY_IMAGE) {
                btClockwise.setVisibility(View.VISIBLE);

                Uri selectedImage = data.getData();
                setImageFromSdCard(selectedImage);

            }
        }
    }

}
