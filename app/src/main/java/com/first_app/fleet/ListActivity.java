package com.first_app.fleet;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.first_app.fleet.MainActivity.INTENT_VALUE;

public class ListActivity extends AppCompatActivity {

    ListView mListNames;
    ArrayList<String> mNames;
    DBForm dbForm = new DBForm(this);
    EditText mSearch;
    TextView mInfomationText;
    FloatingActionButton mAddFloatingButton;
    ArrayAdapter<String> mAdapter;
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = (ListView) findViewById(android.R.id.list);
        mListNames.setSelector(R.color.colorAccent);

        mSearch = (EditText) findViewById(R.id.search_names);
        mSearch.setSingleLine(true);

        mInfomationText = (TextView) findViewById(R.id.tv_information);

        mAddFloatingButton = (FloatingActionButton) findViewById(R.id.fab_addContact);

        mProgressbar = (ProgressBar) findViewById(R.id.pb_progress);
        mNames = new ArrayList<>();
        new LoadData().execute("");

        mAdapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.list_names, mNames);

        if (mNames == null){
            mInfomationText.setText("No Contacts");
        }

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
                for (int i = 0; i < mNames.size(); i++) {
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
                                    + " , "
                                    + dbForm.getCity().get(dbPosition));
                            intent.putExtra("INTRO_INTENT", dbForm.getIntro().get(dbPosition));
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(ListActivity.this, "No data found",
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
                intent.putExtra("Add Record", INTENT_VALUE);
                startActivity(intent);
            }
        });
    }

    private class LoadData extends AsyncTask<Object, Object, List<String>> {

        @Override
        protected void onPreExecute() {
            mProgressbar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected List<String> doInBackground(Object... strings) {

            for (int i = 0; i < dbForm.getName().size(); i++) {
                mNames.add(i, dbForm.getName().get(i));
            }
            Collections.sort(mNames);
            return mNames;
        }

        @Override
        protected void onPostExecute(List<String> aVoid) {
            mProgressbar.setVisibility(View.INVISIBLE);
            mListNames.setAdapter(mAdapter);
            super.onPostExecute(aVoid);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
}
