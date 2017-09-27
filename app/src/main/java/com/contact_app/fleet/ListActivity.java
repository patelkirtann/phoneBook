package com.contact_app.fleet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.contact_app.fleet.CallLog.CallLogActivity;

import java.lang.reflect.Method;
import java.util.ArrayList;

// DataListener is an Interface which will Listen for the Data from the DataBase.
public class ListActivity extends AppCompatActivity implements DataListener {

    public static final int DELETE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_OK = 2;
    public static final int ADD_REQUEST_CODE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 1;

    public ListView mListNames;
    public DBForm dbForm;
    public EditText mSearch;
    public TextView mInformationText;
    public com.github.clans.fab.FloatingActionButton mAddFloatingButton;
    public com.github.clans.fab.FloatingActionButton mCallLogFloatingButton;
    public CustomListAdapter mCustomListAdapter;
    public TextView switcher;
    public ProgressDialog progressDialog;
    public String findName;
    public String sendId;
    public String number;
    boolean isDataChanged = true;
    boolean onScroll = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Database class instance to use methods of database class.
        dbForm = DBForm.getInstance(this);

        mListNames = (ListView) findViewById(android.R.id.list);

        mSearch = (EditText) findViewById(R.id.search_names);
        mSearch.setSingleLine(true);

        mInformationText = (TextView) findViewById(R.id.tv_information);
        mAddFloatingButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_addContact);
        mCallLogFloatingButton = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab_CallLogContact);
        switcher = (TextView) findViewById(R.id.ts_alphabets);

        mInformationText.setText("No Contacts");
        mListNames.setEmptyView(mInformationText);

        // To populate the context menu we need to register a view for it which here is a Listview.
        registerForContextMenu(mListNames);

        // The search bar function to search through The list of contacts via Name or Description.
        mSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (mSearch.hasFocus())
                        mCustomListAdapter.filter(charSequence.toString());
                } catch (NullPointerException nullPointer) {
                    Log.e("onTextChanged", nullPointer.getLocalizedMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // The list where click on contact will get you the Details of that contact.
        mListNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                position = mListNames.getPositionForView(view);
                try {
                    Intent intent = new Intent(ListActivity.this,
                            UserDetailOperationActivity.class);
                    findName = mCustomListAdapter.getItem(position).getName();
                    sendId = dbForm.getIdByName(findName);

                    Log.d("Name Value", sendId);
                    intent.putExtra("ID_INTENT", sendId);

//                    Pair<View, String> p1 = Pair.create(view.findViewById(R.id.profile_image), "profile");
//                    Pair<View, String> p2 = Pair.create(view.findViewById(R.id.tv_name), "name");
//                    ActivityOptionsCompat options = ActivityOptionsCompat.
//                            makeSceneTransitionAnimation(ListActivity.this , p1, p2);

//                    startActivityForResult(intent, 1, options.toBundle());

                    startActivityForResult(intent, 1);
                    overridePendingTransition(R.anim.pull_in_right, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(ListActivity.this, "No Contact Found",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

        // get the first visible position for the Alphabets on list scroll.
        mListNames.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    switcher.setVisibility(View.GONE);
                    Log.d("OnScrollCHanged", "not scrolling");
                    onScroll = false;
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    onScroll = true;
                }
            }

            // When user will scroll the list, This function will get the first letter of
            // first visible item in the list
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                Log.d("OnScroll", "scrolling");

                if (mCustomListAdapter != null && !view.isLayoutRequested() && onScroll) {
                    int firstChar = mListNames.getFirstVisiblePosition();
                    if (mListNames.getChildAt(0).getTop() < 0) {
                        firstChar = (mListNames.getFirstVisiblePosition() + 1);
                    }
                    switcher.setText(
                            mCustomListAdapter
                                    .getItem(firstChar)
                                    .getName().subSequence(0, 1));
                    switcher.setVisibility(View.VISIBLE);

                }
            }
        });

        // The floating action button to add contacts.
        mAddFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });

        mCallLogFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListActivity.this, CallLogActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
            }
        });
    }

    // Creates the option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // The menu options and their operations are defined here
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.link_share:
                String mimeType = "text/plain";
                String title = "Share";
                String text =
                        "https://play.google.com/store/apps/details?id=com.contact_app.fleet";
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
                        "-Adding basic contact details with Picture\n" +
                        "-Accepts only Unique names for better understanding of person \n" +
                        "-Editing or Deleting contact with preference\n" +
                        "-Call, Email or Search location with a single click\n" +
                        "-Share information" +
                        "-Search contact by their Name or their small introduction\n" +
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

    // The context menu will open when you long click on any contact to access quick functionality.
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        }
        findName = mCustomListAdapter.getItem(info.position).getName();
        number = dbForm.getPhoneByName(findName);

        menu.setHeaderTitle(findName.toUpperCase());
        menu.add(Menu.NONE, v.getId(), 0, "Call");
        menu.add(Menu.NONE, v.getId(), 0, "SMS");

    }

    // The context menu options and their operations are defined here
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getTitle().toString()) {
            case "Call":
                try {
                    if (isCallPermissionGranted()) {
                        makeCall(number);
                    } else {
                        Snackbar.make(findViewById(R.id.activity_list),
                                "Grant permission to make a Call", Snackbar.LENGTH_LONG);
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Try Again", Toast.LENGTH_SHORT).show();
                }
                break;
            case "SMS":
                sendSms(number);
                break;
        }
        return true;
    }


    // This function let us add the Icons with the text in the Menu.
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    Log.e("Menu Error", e.toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }

    // This will call the intent to open the default SMS service on the phone.
    private void sendSms(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Application found to handle this Action.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // This will directly call the contact apon click.
    private void makeCall(String number) {
        Intent mCallIntent = new Intent(Intent.ACTION_CALL);
        mCallIntent.setData(Uri.parse("tel:" + number));
        if (mCallIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mCallIntent);
        } else {
            Toast.makeText(this, "No Application found to handle this Action.",
                    Toast.LENGTH_SHORT).show();
        }

    }

    // The contact list data is loaded every time if there is a change in the list on onResume method.
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "onResume");
        try {
            if (isDataChanged) {
                new LoadData(dbForm, this).execute("");
                isDataChanged = false;
                mSearch.setText("");
            }
        } catch (NullPointerException nullPointer) {
            Log.e("OnResume", nullPointer.getLocalizedMessage());
        }

    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
////        onRestart();
//    }

    // This is DataListener interface method which will show a progress bar until all the data is Loaded.
    @Override
    public void onPreExecute() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    @Override
    public void onProgress(int progress) {

    }

    // This method loads the data in the background in the list from created Database.
    @Override
    public void onCompletion(ArrayList<RetrieveContactRecord> userRecords) {
        onPause();
        mCustomListAdapter = new CustomListAdapter(this, userRecords);

        mListNames.setAdapter(mCustomListAdapter);
    }

    // Here I'm checking every time when this activity calls after the activities which can change the
    // data in the database. And if any changes are made then the list will update otherwise not.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DELETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                isDataChanged = true;
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Deleted",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
        if (resultCode == UPDATE_RESULT_OK) {
            isDataChanged = true;
            Snackbar.make(findViewById(R.id.activity_list), "1 Contact Updated",
                    Snackbar.LENGTH_SHORT)
                    .show();
        }
        if (requestCode == ADD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                isDataChanged = true;
                Snackbar.make(findViewById(R.id.activity_list), "Contact Added",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    // Here i'm stopping the Progress bar after list are loaded successfully.
    @Override
    protected void onPause() {
        progressDialog.dismiss();
        super.onPause();
    }

    // This method will check the call permission which is mandatory to allow by the user if they want
    // to make a direct call from the application.
    public boolean isCallPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation

            return true;
        }
    }

    // If the permission hasn't granted yet, this function will call every time to ask the user to
    // grant the permission if they wanna use the calling function.
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_CONTACTS: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    makeCall(number);
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

}
