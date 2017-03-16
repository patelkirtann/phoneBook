package com.contact_app.fleet;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.List;

public class ListActivity extends AppCompatActivity implements DataListener {

    ListView mListNames;
    DBForm dbForm = new DBForm(this);
    EditText mSearch;
    TextView mInformationText;
    FloatingActionButton mAddFloatingButton;
    ArrayAdapter<String> mAdapter;
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = (ListView) findViewById(android.R.id.list);

        mSearch = (EditText) findViewById(R.id.search_names);
        mSearch.setSingleLine(true);

        mInformationText = (TextView) findViewById(R.id.tv_information);
        mAddFloatingButton = (FloatingActionButton) findViewById(R.id.fab_addContact);
        mProgressbar = (ProgressBar) findViewById(R.id.pb_progress);

        mInformationText.setText("No Contacts");
        mListNames.setEmptyView(mInformationText);

        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ListActivity.this.mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ListActivity.this.mAdapter.getFilter().filter(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mListNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                position = mListNames.getPositionForView(view);
                for (int i = 0; i < mAdapter.getCount(); i++) {
                    if (position == i) {

                        try {
                            Intent intent = new Intent(ListActivity.this,
                                    UserDetailOperationActivity.class);
                            int dbPosition = dbForm.getName().indexOf(mAdapter.getItem(i));

                            intent.putExtra("NAME_INTENT",
                                    dbForm.getName().get(dbPosition).toUpperCase());
                            intent.putExtra("ID_INTENT",
                                    dbForm.getID().get(dbPosition));
                            intent.putExtra("PHONE_NUMBER_INTENT",
                                    dbForm.getPhone().get(dbPosition));
                            intent.putExtra("EMAIL_ADDRESS_INTENT",
                                    dbForm.getEmail().get(dbPosition));
                            intent.putExtra("STREET_INTENT",
                                    dbForm.getStreet().get(dbPosition));
                            intent.putExtra("CITY_INTENT",
                                    dbForm.getCity().get(dbPosition));
                            intent.putExtra("INTRO_INTENT",
                                    dbForm.getIntro().get(dbPosition));
                            startActivityForResult(intent, 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(ListActivity.this, "No Contact Found",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

        mAddFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });
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
                String mimeType = "text/plain";
                String title = "Share";
                String text = "Greetings, Use this application to save your temporary contacts and" +
                        " use it as you fit." +
                        "Download with the given link and share application with friends and family.\n" +
                        "http://play.google.com/store/apps/details?id=com.contact_app.fleet";
                ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(text)
                        .startChooser();
                break;
            case R.id.about:
                final AlertDialog.Builder dialog = new AlertDialog
                        .Builder(ListActivity.this);
                dialog.setTitle("Fleet");
                dialog.setMessage("\n" +
                        "\n" +
                        "\"" +
                        "Why do I have this many unwanted contacts in my phone?\n" +
                        "-Who is this John, Doe? Why is he in my contact list? Where did I meet him? \n" +
                        "-Do I really need his/her contact? Am I ever gonna need his/her Help? \" \n\n" +
                        "You usually get this kind of questions while scrolling your contact list or " +
                        "While adding someone's contact information. \n" +
                        "Well, you never know that you are gonna need His/Her help in future. \n" +
                        "So why not to save those uncertain or temporary contacts in FLEET. \n" +
                        "\n\n" +
                        "Fleet helps those people who travel a lot and love to meet new people, " +
                        "that student who's attending school and meet new students every day," +
                        " those professionals who are interacting with different people on daily basis," +
                        " those people who are not shy and love to make new friends, etc.\n" +
                        "Fleet will store all of your uncertain or temporary contacts and " +
                        "let you use it as a regular contact list," +
                        " But you'll access this contact when you need them in future.\n" +
                        "When you feel comfortable with the person who you thought was temporary," +
                        " but now you are using his contact details frequently," +
                        " you can move all of his details to your regular contact list with a single click.\n" +
                        "\n\n" +
                        "Features:\n" +
                        "-Adding basic contact details\n" +
                        "-Accepts only Unique names for better understanding of person \n" +
                        "-Editing or Deleting contact with preference\n" +
                        "-Call, Email or Search location with a single click\n" +
                        "-Search contact by their name\n" +
                        "\n" +
                        "\n");
                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = dialog.create();
                alertDialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "onResume");
        new LoadData(dbForm, this).execute("");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        onRestart();
    }

    @Override
    public void onPreExecute() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onCompletion(List<String> data) {
        mProgressbar.setVisibility(View.INVISIBLE);
        mAdapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.list_names, data);
        mListNames.setAdapter(mAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Deleted",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
            if (resultCode == 2){
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Updated",
                        Snackbar.LENGTH_SHORT)
                        .show();
        }
    }
}
