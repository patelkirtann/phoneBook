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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {
    private static final int GALLERY_IMAGE = 5;
    private static final int MY_PERMISSIONS_REQUEST_PICTURE_CONTACTS = 1;
    EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;
    String name, phone, email, street, city, intro;
    byte[] picture;
    CircleImageView imageView;

    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        TextView heading = (TextView) findViewById(R.id.tv_heading);
        heading.setText("Edit Contact");

        mName = (EditText) findViewById(R.id.name_field);
        mEmail = (EditText) findViewById(R.id.email_field);
        mPhone = (EditText) findViewById(R.id.phone_field);
        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);
        imageView = (CircleImageView) findViewById(R.id.profile_image);

        getDataToAutoFill();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFromSdCard();
            }
        });

    }

    public void getDataToAutoFill() {
        Bundle extras = getIntent().getExtras();
        RetrieveContactRecord record = dbForm.getContactRow(extras.getString("NAME_INTENT"));

        mName.setText(record.getName());
        mPhone.setText(record.getPhone());
        mEmail.setText(record.getEmail());
        mStreet.setText(record.getStreet());
        mCity.setText(record.getCity());
        mIntro.setText(record.getIntro());

        picture = record.getPicture();
        if (picture != null) {
            ByteArrayInputStream imageStream = new ByteArrayInputStream(picture);
            imageView.setImageBitmap(BitmapFactory.decodeStream(imageStream));
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

    public void updatePermission() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(UpdateActivity.this);
        dialog.setTitle("Confirmation")
                .setIcon(R.drawable.ic_warning)
                .setMessage("Update Contact?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbForm.updateContact( name, email, phone, street, city, intro, picture);
                        Intent intent = new Intent();
                        intent.putExtra("NAME_INTENT",
                                name);
                        setResult(RESULT_OK, intent);
                        finish();
                        Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();

                    }
                })
                .setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UpdateActivity.this, " Canceled ", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .setNeutralButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog alert = dialog.create();
        alert.show();
    }

    public void setImageFromSdCard(Uri selectedImage) {

        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);

        imageView.setImageURI(selectedImage);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (yourSelectedImage != null) {
            yourSelectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        picture = stream.toByteArray();
    }

    public void getImageFromSdCard() {
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
                        if (isExternalStoragePermissionGranted()) {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, GALLERY_IMAGE);
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
