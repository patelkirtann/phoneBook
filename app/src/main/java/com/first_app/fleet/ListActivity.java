package com.first_app.fleet;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
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
//    ArrayList<String> mNames;
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

                        Toast.makeText(ListActivity.this, mAdapter.getItem(i),
                                Toast.LENGTH_SHORT).show();
                        try {
                            Intent intent = new Intent(ListActivity.this,
                                    UserDetailOperationActivity.class);
                            int dbPosition = dbForm.getName().indexOf(mAdapter.getItem(i));

                            intent.putExtra("NAME_INTENT", dbForm.getName().get(dbPosition));
                            intent.putExtra("ID_INTENT", dbForm.getID().get(dbPosition));
                            intent.putExtra("PHONE_NUMBER_INTENT", dbForm.getPhone().get(dbPosition));
                            intent.putExtra("EMAIL_ADDRESS_INTENT", dbForm.getEmail().get(dbPosition));
                            intent.putExtra("MAP_LOCATION_INTENT", dbForm.getStreet().get(dbPosition)
                                    + "  "
                                    + dbForm.getCity().get(dbPosition));
                            intent.putExtra("INTRO_INTENT", dbForm.getIntro().get(dbPosition));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(ListActivity.this, "No Contact found",
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
                String mimetype = "text/plain";
                String title = "Share with";
                String text = "Link Here";
                ShareCompat.IntentBuilder.from(this)
                        .setChooserTitle(title)
                        .setType(mimetype)
                        .setText(text)
                        .startChooser();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Debug", "OnStop");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("Debug", "OnPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Debug", "onResume");
        new LoadData(dbForm, this).execute("");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("Debug", "onRestart");
    }
    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.d("Debug", "OnPostResume");
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

//    private class LoadData extends AsyncTask<Object, Object, List<String>> {
//
//        public LoadData(DBForm dataprovider, Task onCompletion) {
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            mProgressbar.setVisibility(View.VISIBLE);
//            super.onPreExecute();
//        }
//
//        @Override
//        protected List<String> doInBackground(Object... strings) {
//            Log.d("Debug", "doInBackground");
//            List<String> names = new ArrayList<>(dbForm.getName().size());
//            for (int i = 0; i < dbForm.getName().size(); i++) {
//                names.add(i, dbForm.getName().get(i));
//            }
//            Collections.sort(names);
//            return names;
//        }
//
//        @Override
//        protected void onPostExecute(List<String> names) {
//            mProgressbar.setVisibility(View.INVISIBLE);
//
//            mAdapter = new ArrayAdapter<>(ListActivity.this, R.layout.list_view, R.id.list_names, names);
//            mListNames.setAdapter(mAdapter);
//
//            super.onPostExecute(names);
//        }
//    }
}
