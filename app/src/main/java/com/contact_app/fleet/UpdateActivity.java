package com.contact_app.fleet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.TransactionTooLargeException;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {
    private static final int GALLERY_IMAGE = 5;
    private static final int MY_PERMISSIONS_REQUEST_PICTURE_CONTACTS = 1;
    public EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;
    public ImageButton btClockwise;

    public String id, name, phone, email, street, city, intro;
    public String originalName, originalEmail,
            originalPhone, originalStreet, originalCity, originalIntro;
    public TextInputLayout nameLayout, emailLayout, phoneLayout;

    public byte[] imageByteArray;
    public byte[] originalImageByteArray;
    public CircleImageView imageView;
    public DBForm dbForm;
    public Bitmap yourSelectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_update);

        dbForm = DBForm.getInstance(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView heading = (TextView) findViewById(R.id.tv_heading);
        heading.setText("Edit Contact");

        nameLayout = (TextInputLayout) findViewById(R.id.name_field_layout);
        mName = (EditText) findViewById(R.id.name_field);

        emailLayout = (TextInputLayout) findViewById(R.id.email_field_layout);
        mEmail = (EditText) findViewById(R.id.email_field);

        phoneLayout = (TextInputLayout) findViewById(R.id.phone_field_layout);
        mPhone = (EditText) findViewById(R.id.phone_field);

        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);

        imageView = (CircleImageView) findViewById(R.id.profile_image);
        btClockwise = (ImageButton) findViewById(R.id.bt_rotate_clockwise);
        btClockwise.setVisibility(View.GONE);

        try {
            getDataToAutoFill();
        } catch (TransactionTooLargeException e) {
            Toast.makeText(this, "Too large data to handle", Toast.LENGTH_SHORT).show();
        }

        btClockwise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int degree = 90;
                if (yourSelectedImage != null) {
                    rotate(degree);
                } else {
                    Toast.makeText(UpdateActivity.this,
                            "Select Image first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromSdCard();
            }
        });

    }

    private void rotate(int degree) {
        yourSelectedImage = ImageConverter.rotateImageBy(degree, yourSelectedImage);
        imageView.setImageBitmap(yourSelectedImage);
        imageByteArray = ImageConverter.convertToByteArray(yourSelectedImage);
    }

    private boolean isNameEmpty(EditText edit) {
        String text = edit.getText().toString().replaceAll(" ", "");

        return !(!text.trim().isEmpty() &&
                text.length() >= 2);
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void getDataToAutoFill() throws TransactionTooLargeException {

        Bundle extras = getIntent().getExtras();
        id = extras.getString("ID_INTENT");
        RetrieveContactRecord record = dbForm.getSingleContactById(id);

        originalName = record.getName();
        mName.setText(originalName);

        originalPhone = record.getPhone();
        mPhone.setText(originalPhone);

        originalEmail = record.getEmail();
        mEmail.setText(originalEmail);

        originalStreet = record.getStreet();
        mStreet.setText(originalStreet);

        originalCity = record.getCity();
        mCity.setText(originalCity);

        originalIntro = record.getIntro();
        mIntro.setText(originalIntro);

        imageByteArray = record.getPicture();
        originalImageByteArray = imageByteArray;
        if (imageByteArray != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(imageByteArray);
            yourSelectedImage = BitmapFactory.decodeStream(imageStream);
//            imageView.setImageBitmap(yourSelectedImage);
            btClockwise.setVisibility(View.VISIBLE);
            Glide.with(this).load(imageByteArray).asBitmap().into(imageView);
        }else {
            btClockwise.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.update_contact, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_contact:
                try {
                    getDataToUpdate();
                    updatePermission();

                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getDataToUpdate() {
        name = mName.getText().toString().trim().toLowerCase();
        email = mEmail.getText().toString().toLowerCase();
        phone = mPhone.getText().toString();
        street = mStreet.getText().toString();
        city = mCity.getText().toString();
        intro = mIntro.getText().toString();

    }

    public boolean isValuesOriginal() {
        return name.equals(originalName)
                && email.equals(originalEmail)
                && phone.equals(originalPhone)
                && street.equals(originalStreet)
                && city.equals(originalCity)
                && intro.equals(originalIntro)
                && Arrays.equals(imageByteArray, originalImageByteArray);
    }

    public void updatePermission() {

        if (!isValuesOriginal()) {

            if (!isNameEmpty(mName)
                    && mPhone.length() >= 10) {

                if (isNameOk() && isEmailOk()) {

                    final AlertDialog.Builder dialog =
                            new AlertDialog.Builder(UpdateActivity.this);
                    dialog.setTitle("Confirmation")
                            .setIcon(R.drawable.ic_warning)
                            .setMessage("Update Contact?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dbForm.updateContact(id, name, email,
                                            phone, street, city, intro, imageByteArray);
                                    Intent intent = new Intent();
                                    intent.putExtra("ID_INTENT",
                                            id);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                    Toast.makeText(UpdateActivity.this,
                                            "Updated", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(UpdateActivity.this, " Canceled ",
                                            Toast.LENGTH_SHORT).show();
                                    onBackPressed();
                                }
                            });

                    AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    Toast.makeText(UpdateActivity.this,
                            "Fill all required filed with valid input",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                if (isNameEmpty(mName)) {
                    nameLayout.setError("Put valid name(2 or more character)");
                } else if (mPhone.length() < 10) {
                    phoneLayout.setError("Put valid number(xxx-xxx-xxxx)");
                    nameLayout.setErrorEnabled(false);
                }
                Toast.makeText(UpdateActivity.this,
                        "Fill all required filed with valid input",
                        Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(UpdateActivity.this,
                    "No Data Updated. Please modify data" +
                            " and press Update or Click Back",
                    Toast.LENGTH_LONG).show();

        }
    }

    public boolean isNameOk() {
        boolean duplicateName = dbForm.checkName(
                mName.getText()
                        .toString()
                        .toLowerCase()
                        .trim());

        if (mName.getText().toString().toLowerCase().trim().equals(originalName)){
            return true;
        } else if (duplicateName) {
            phoneLayout.setErrorEnabled(false);
            nameLayout.setError("Duplicate Name Found \nTry to give Same/Unique Name");
            return false;
        }else {
            return true;
        }
    }

    public boolean isEmailOk() {
        if (!mEmail.getText().toString().isEmpty()) {
            if (!isEmailValid(mEmail.getText().toString().trim())) {
                emailLayout.setError("Not a valid email");
                return false;
            } else {
                emailLayout.setErrorEnabled(false);
                return true;
            }
        }
        return true;
    }

    public void setImageFromSdCard(Uri selectedImage) {

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        yourSelectedImage = ImageConverter.transform(BitmapFactory.decodeStream(imageStream));
        try {
            imageStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        imageView.setImageBitmap(yourSelectedImage);

        imageByteArray = ImageConverter.convertToByteArray(yourSelectedImage);

        Glide.with(this).load(imageByteArray).asBitmap().into(imageView);

    }

    public void getImageFromSdCard() {

        if (imageByteArray != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Photo Source")
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
                            imageView.setImageResource(R.drawable.ic_add_a_photo);
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
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();


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
                btClockwise.setVisibility(View.VISIBLE);

                Uri selectedImage = data.getData();
                setImageFromSdCard(selectedImage);

            }
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }



}
