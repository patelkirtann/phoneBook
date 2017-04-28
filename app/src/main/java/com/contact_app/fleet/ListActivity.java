package com.contact_app.fleet;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SubMenu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements DataListener {

    public static final int DELETE_REQUEST_CODE = 1;
    public static final int UPDATE_RESULT_OK = 2;
    public static final int ADD_REQUEST_CODE = 3;
    private static final int MY_PERMISSIONS_REQUEST_CALL_CONTACTS = 1;

    public ListView mListNames;
    public DBForm dbForm;
    public EditText mSearch;
    public TextView mInformationText;
    public FloatingActionButton mAddFloatingButton;
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

        dbForm = DBForm.getInstance(this);

        mListNames = (ListView) findViewById(android.R.id.list);

        mSearch = (EditText) findViewById(R.id.search_names);
        mSearch.setSingleLine(true);

        mInformationText = (TextView) findViewById(R.id.tv_information);
        mAddFloatingButton = (FloatingActionButton) findViewById(R.id.fab_addContact);
        switcher = (TextView) findViewById(R.id.ts_alphabets);

        mInformationText.setText("No Contacts");
        mListNames.setEmptyView(mInformationText);
        registerForContextMenu(mListNames);

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
                if (scrollState == SCROLL_STATE_IDLE) {
                    switcher.setVisibility(View.GONE);
                    Log.d("OnScrollCHanged", "not scrolling");
                    onScroll = false;
                }
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    onScroll = true;
                }
            }

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

        mAddFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListActivity.this, AddActivity.class);
                startActivityForResult(intent, ADD_REQUEST_CODE);
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        menu.add(Menu.NONE, v.getId(), 0, "Call");
        menu.add(Menu.NONE, v.getId(), 0, "SMS");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        findName = mCustomListAdapter.getItem(info.position).getName();
        number = dbForm.getPhoneByName(findName);

        switch (item.getTitle().toString()) {
            case "Call":
                try {
                    if (isCallPermissionGranted()) {
                        makeCall(number);
                    } else {
                        Snackbar.make(findViewById(R.id.activity_list),
                                "Grant permission to make Call", Snackbar.LENGTH_LONG);
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

    private void sendSms(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    private void makeCall(String number) {
        Intent mCallIntent = new Intent(Intent.ACTION_CALL);
        mCallIntent.setData(Uri.parse("tel:" + number));
        if (mCallIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mCallIntent);
        }

    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        onRestart();
    }

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

    @Override
    public void onCompletion(ArrayList<RetrieveContactRecord> userRecords) {
        onPause();
        mCustomListAdapter = new CustomListAdapter(this, userRecords);

        mListNames.setAdapter(mCustomListAdapter);
    }

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
                Snackbar.make(findViewById(R.id.activity_list), "1 Contact Added",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }
    }

    @Override
    protected void onPause() {
        progressDialog.dismiss();
        super.onPause();
    }

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
                    Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(this, "Permission is Denied", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

}
