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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
    public EditText name, email, phone, street, city, intro;
    public DBForm dbForm = new DBForm(this);
    byte[] byteArray;
    String fileManagerString, imagePath;
    String selectedImagePath = "";
    int columnIndex;
    private CircleImageView mPicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mPicture = (CircleImageView) findViewById(R.id.profile_image);
        name = (EditText) findViewById(R.id.name_field);
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.phone_field);
        street = (EditText) findViewById(R.id.street_field);
        city = (EditText) findViewById(R.id.city_field);
        intro = (EditText) findViewById(R.id.tv_auto);

        mPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromSdCard();
            }
        });

    }

    private void onSaveClicked() {
        if (!isEmpty(name) && phone.length() >= 10 &&
                !dbForm.checkName(name.getText().toString().toLowerCase())) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
            dialog.setTitle("Confirmation");
            dialog.setMessage("Save Contact?");
            dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (addedToRecords()) {
                        Toast.makeText(AddActivity.this, " Contact Saved ",
                                Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } else {
                        Toast.makeText(AddActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(AddActivity.this, "Nothing changed", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        } else {
            if (isEmpty(name))
                name.setError("Invalid Name");
            if (phone.length() < 10 && phone.length() > 14)
                phone.setError("Invalid Number");
            if (dbForm.checkName(name.getText().toString().toLowerCase()))
                name.setError("Duplicate Name");
            Toast.makeText(AddActivity.this, " *Enter at least 2 character\n " +
                    "*Enter valid 10 Digit number ", Toast.LENGTH_LONG).show();
        }
    }

    private boolean isEmpty(EditText edit) {
        String text = edit.getText().toString().replaceAll(" ", "");

        return !(!text.trim().isEmpty() &&
                text.length() >= 2);
    }

    private boolean addedToRecords() {

        String nameText = name.getText().toString().toLowerCase().trim();
        String emailText = email.getText().toString().toLowerCase();
        String phoneText = phone.getText().toString();
        String streetText = street.getText().toString();
        String cityText = city.getText().toString();
        String introText = intro.getText().toString();

        dbForm.insertValue(nameText, emailText, phoneText, streetText, cityText, introText, byteArray);

        return true;
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

    private void setImageFromSdCard(Bitmap bmp) {

        mPicture.setImageBitmap(bmp);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byteArray = stream.toByteArray();

    }

    private void getImageFromSdCard() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Photo Source")
                .setMessage("Select Pictures From Media Library")
                .setCancelable(false)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setPositiveButton("Select Picture", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id1) {
                        if (isExternalStoragePermissionGranted()){
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(intent, GALLERY_IMAGE);}else {

                        }

                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
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
//                    Snackbar.make(findViewById(R.id.sv_scroll), "Permission is granted",
//                            Snackbar.LENGTH_SHORT)
//                            .show();

                } else {
                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT).show();
//                    Snackbar.make(findViewById(R.id.sv_scroll), "Permission is Denied",
//                            Snackbar.LENGTH_SHORT)
//                            .show();
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
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

                mPicture.setImageURI(selectedImage);



//                Bitmap bmp = (Bitmap) data.getExtras().get("data");
////                setImageFromSdCard(bmp);
//                mPicture.setImageBitmap(bmp);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (yourSelectedImage != null) {
                    yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byteArray = stream.toByteArray();
            }
        }
    }
}
