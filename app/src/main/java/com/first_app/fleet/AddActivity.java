package com.first_app.fleet;

import android.content.DialogInterface;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    Button mSave;

    EditText name, email, phone, street, city, intro;

    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSave = (Button) findViewById(R.id.save);
        name = (EditText) findViewById(R.id.name_field);
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.phone_field);
        street = (EditText) findViewById(R.id.street_field);
        city = (EditText) findViewById(R.id.city_field);
        intro = (EditText) findViewById(R.id.tv_auto);

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onSaveButtonClicked();

            }
        });

    }

    private void onSaveButtonClicked() {
        if (!isEmpty(name) && !isEmpty(email) && contains(email, "@")
                && contains(email, ".")) {
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
                    }else {
                        Toast.makeText(AddActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            dialog.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(AddActivity.this, " Canceled ", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
        } else {
            Toast.makeText(AddActivity.this, " Fill Required Filed(*) ", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isEmpty(EditText edit) {
        if (edit.getText().toString().trim().length() > 0)
            return false;

        return true;
    }

    private boolean contains(EditText edit, String str) {
        if (edit.getText().toString().contains(str)) {
            return true;
        }
        return false;
    }

    private boolean addedToRecords() {

        if (!dbForm.checkName(name.getText().toString().toLowerCase())) {

            String nameText = name.getText().toString().toLowerCase().trim();
            String emailText = email.getText().toString().toLowerCase();
            String phoneText = phone.getText().toString();
            String streetText = street.getText().toString();
            String cityText = city.getText().toString();
            String introText = intro.getText().toString();

            dbForm.insertValue(nameText, emailText, phoneText, streetText, cityText, introText);
            return true;
        } else {
            Toast.makeText(this, "Same Name found.\n Try to give unique Name.",
                    Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_details, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.link_share:
                String mimetype = "text/plain";
                String title = "Share with";
                String text = "Link Here";
                ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(title)
                        .setType(mimetype)
                        .setText(text)
                        .startChooser();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
