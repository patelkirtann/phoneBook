package com.example.kt_ki.dbpractice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListActivity extends AppCompatActivity {

    ListView mListNames;
    List<String> mNames;
    DBForm dbForm = new DBForm(this);
    EditText mSearch;
    ArrayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = (ListView) findViewById(android.R.id.list);
        mListNames.setSelector(R.color.colorAccent);

        mSearch = (EditText) findViewById(R.id.search_names);
        mSearch.setSingleLine(true);

        mNames = new ArrayList<>();
        for (int i = 0; i < dbForm.getName().size(); i++) {
            mNames.add(i, dbForm.getName().get(i).toUpperCase());
        }
        mAdapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.list_names, mNames);
        mListNames.setAdapter(mAdapter);

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

                        Toast.makeText(ListActivity.this, (CharSequence) mAdapter.getItem(i),
                                Toast.LENGTH_SHORT).show();
                        try{
                            Intent intent = new Intent(ListActivity.this,
                                    UserDetailOperationActivity.class);


                            intent.putExtra("NAME_INTENT", dbForm.getName().get(i));
                            intent.putExtra("ID_INTENT", dbForm.getID().get(i));
                            intent.putExtra("PHONE_NUMBER_INTENT", dbForm.getPhone().get(i));
                            intent.putExtra("EMAIL_ADDRESS_INTENT", dbForm.getEmail().get(i));
                            intent.putExtra("MAP_LOCATION_INTENT", dbForm.getStreet().get(i)
                                    + " , "
                                    + dbForm.getCity().get(i));
                            intent.putExtra("INTRO_INTENT", dbForm.getIntro().get(i));

                            startActivity(intent);
                        }catch (Exception e){
                            Toast.makeText(ListActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finishActivity(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
