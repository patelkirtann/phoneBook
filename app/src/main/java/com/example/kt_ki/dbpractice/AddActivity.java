package com.example.kt_ki.dbpractice;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {
    final static int INTENT_VALUE = 2;

    Button mSave;
    Button mCancel;

    EditText name, email, phone, street, city, intro;

    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        mSave = (Button) findViewById(R.id.save);
        mCancel = (Button) findViewById(R.id.cancel);

        name = (EditText) findViewById(R.id.name_field);
//        String getName = name.getText().toString();
        email = (EditText) findViewById(R.id.email_field);
        phone = (EditText) findViewById(R.id.phone_field);
        street = (EditText) findViewById(R.id.street_field);
        city = (EditText) findViewById(R.id.city_field);
        intro = (EditText) findViewById(R.id.tv_auto);

        final Context context = AddActivity.this;

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmpty(name) && !isEmpty(email) && contains(email, "@")
                        && contains(email, ".")) {
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(AddActivity.this);
                    dialog.setTitle("Confirmation");
                    dialog.setMessage("Do you wanna Save?");
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            AddedToRecords();
                            Toast.makeText(AddActivity.this, " Contact Saved ", Toast.LENGTH_SHORT).show();
                            Intent intent = getIntent();
                            finish();
                            startActivity(intent);

                        }
                    });
                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(AddActivity.this, " Canceled ", Toast.LENGTH_SHORT).show();

                        }
                    });
                    AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    Toast.makeText(context, " Fill the details ", Toast.LENGTH_SHORT).show();
                }
            }
        });


        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setTitle("Confirmation");
                dialog.setMessage("Do you wanna Cancel ?");
                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(AddActivity.this, " Canceled ", Toast.LENGTH_SHORT).show();
                        AddActivity.this.finish();
                    }
                });
                dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog alert = dialog.create();
                alert.show();
            }
        });
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

    private void AddedToRecords() {

        String nameText = name.getText().toString();
        String emailText = email.getText().toString();
        String phoneText = phone.getText().toString();
        String streetText = street.getText().toString();
        String cityText = city.getText().toString();
        String introText = intro.getText().toString();

        dbForm.insertValue(nameText, emailText, phoneText, streetText, cityText, introText);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = new Intent(AddActivity.this, AddActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
