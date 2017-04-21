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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements DataListener {

    public static final int DELETE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_OK = 2;
    public static final int ADD_REQUEST_CODE = 3;

    public ListView mListNames;
    public DBForm dbForm = new DBForm(this);
    public EditText mSearch;
    public TextView mInformationText;
    public FloatingActionButton mAddFloatingButton;
    public CustomListAdapter mCustomListAdapter;
    public ProgressBar mProgressbar;
    public TextView switcher;



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
        switcher = (TextView) findViewById(R.id.ts_alphabets);
        switcher.setVisibility(View.INVISIBLE);

        mInformationText.setText("No Contacts");
        mListNames.setEmptyView(mInformationText);

        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                mCustomListAdapter.getFilter().filter(charSequence);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String text = mSearch.getText().toString().toLowerCase(Locale.getDefault());
                mCustomListAdapter.filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                String text = mSearch.getText().toString().toLowerCase(Locale.getDefault());
//                mCustomListAdapter.filter(text);
            }
        });

        mListNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                position = mListNames.getPositionForView(view);

                try {
                    Intent intent = new Intent(ListActivity.this,
                            UserDetailOperationActivity.class);
                    String findName = mCustomListAdapter.getItem(position).getName();
                    String sendId = dbForm.getIdByName(findName);
                    Log.d("Name Value", sendId);
                    intent.putExtra("ID_INTENT", sendId);

                    startActivityForResult(intent, 1);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "No Contact Found",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

//         get the first visible position for the Alphabets on list scroll.
        mListNames.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                switcher.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                String firstChar =
                        String.valueOf(mListNames.getChildAt(firstVisibleItem));

                switcher.setText(firstChar.subSequence(0, 1));

                switcher.setVisibility(View.VISIBLE);
            }
        });


        mAddFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivityForResult(intent, 3);
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
                String text = "Greetings, This application will save all of your temporary contacts and" +
                        " gives the functionality to use those contacts as per the convenience." +
                        "Download with the given link and share application with friends and family.\n" +
                        "http://play.google.com/store/apps/details?id=com.contact_app.fleet";
                ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(title)
                        .setType(mimeType)
                        .setText(text)
                        .startChooser();
                break;
            case R.id.about:
                String versionName = BuildConfig.VERSION_NAME;
                final AlertDialog.Builder dialog = new AlertDialog
                        .Builder(ListActivity.this);
                dialog.setIcon(R.mipmap.ic_launcher);
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
                        "-Share information" +
                        "-Search contact by their mName\n" +
                        "\n" +
                        "\n" +
                        "Version: " + versionName +
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
//        onRestart();
    }

    @Override
    public void onPreExecute() {
        mProgressbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgress(int progress) {

    }

    @Override
    public void onCompletion(ArrayList<RetrieveContactRecord> userRecords) {
        mProgressbar.setVisibility(View.INVISIBLE);
        mCustomListAdapter = new CustomListAdapter(this, userRecords);
        mListNames.setAdapter(mCustomListAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Deleted",
                        Snackbar.LENGTH_SHORT)
                        .show();
        }
        if (resultCode == UPDATE_RESULT_OK) {
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Updated",
                        Snackbar.LENGTH_SHORT)
                        .show();
        }
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK)
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Added",
                        Snackbar.LENGTH_SHORT)
                        .show();
        }
    }
}
