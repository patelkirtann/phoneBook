package com.contact_app.fleet;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    public EditText name, email, phone, street, city, intro;

    public DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        name = (EditText) findViewById(R.id.name_field);
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.phone_field);
        street = (EditText) findViewById(R.id.street_field);
        city = (EditText) findViewById(R.id.city_field);
        intro = (EditText) findViewById(R.id.tv_auto);

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
            if (phone.length() < 10 || phone.length() > 14)
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

        dbForm.insertValue(nameText, emailText, phoneText, streetText, cityText, introText);

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

}
