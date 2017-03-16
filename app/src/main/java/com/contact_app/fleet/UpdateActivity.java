package com.contact_app.fleet;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity {

    EditText mName, mEmail, mPhone, mStreet, mCity, mIntro;
    String name, id, phone, email, street, city, intro;

    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mName = (EditText) findViewById(R.id.name_field);
        mEmail = (EditText) findViewById(R.id.email_field);
        mPhone = (EditText) findViewById(R.id.phone_field);
        mStreet = (EditText) findViewById(R.id.street_field);
        mCity = (EditText) findViewById(R.id.city_field);
        mIntro = (EditText) findViewById(R.id.tv_auto);

        getDataToAutoFill();

    }

    public void getDataToAutoFill() {
        Bundle extras = getIntent().getExtras();

        mName.setText(extras.getString("NAME_INTENT"));
        id = extras.getString("ID_INTENT");
        mPhone.setText(extras.getString("PHONE_NUMBER_INTENT"));
        mEmail.setText(extras.getString("EMAIL_ADDRESS_INTENT"));
        mStreet.setText(extras.getString("STREET_INTENT"));
        mCity.setText(extras.getString("CITY_INTENT"));
        mIntro.setText(extras.getString("INTRO_INTENT"));
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
        dialog.setTitle("Confirmation");
        dialog.setIcon(R.drawable.ic_warning);
        dialog.setMessage("Update Contact?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dbForm.updateContact(id, name, email, phone, street, city, intro);
                Intent intent = new Intent();
                intent.putExtra("NAME_INTENT",
                        name);
                intent.putExtra("ID_INTENT",
                        id);
                intent.putExtra("PHONE_NUMBER_INTENT",
                        phone);
                intent.putExtra("EMAIL_ADDRESS_INTENT",
                        email);
                intent.putExtra("STREET_INTENT",
                        street);
                intent.putExtra("CITY_INTENT",
                        city);
                intent.putExtra("INTRO_INTENT",
                        intro);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(UpdateActivity.this, "Updated", Toast.LENGTH_SHORT).show();

            }
        });
        dialog.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(UpdateActivity.this, " Canceled ", Toast.LENGTH_SHORT).show();
                onBackPressed();
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}
