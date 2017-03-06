package com.example.kt_ki.dbpractice;

import android.content.Intent;
import android.os.Bundle;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public final static String NAME_INTENT = "sendName";
    public final static String ID_INTENT = "sendID";
    public final static String PHONE_NUMBER_INTENT = "sendPhoneNumber";
    public final static String EMAIL_ADDRESS_INTENT = "sendEmailAddress";
    public final static String MAP_LOCATION_INTENT = "sendMapLocation";
    public final static String INTRO_INTENT = "sendIntro";

    ListView mListNames;
    List<String> mNames;
    DBForm dbForm = new DBForm(this);
    EditText search;
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = (ListView) findViewById(android.R.id.list);
        mListNames.setSelector(R.color.colorAccent);

        search = (EditText) findViewById(R.id.search_names);

        mNames = new ArrayList<>();
        for (int i = 0; i < dbForm.getName().size(); i++) {
            mNames.add(dbForm.getName().get(i).toUpperCase());
        }
        adapter = new ArrayAdapter<>(this, R.layout.list_view, R.id.list_names, mNames);
        mListNames.setAdapter(adapter);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                ListActivity.this.adapter.getFilter().filter(charSequence);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

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
                    if (position == i){
                        Toast.makeText(ListActivity.this , adapter.getItem(i).toString(),
                                Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ListActivity.this , UserDetailOperationActivity.class);
                        intent.putExtra(NAME_INTENT , dbForm.getPhone().get(i));
                        intent.putExtra(ID_INTENT , dbForm.getPhone().get(i));
                        intent.putExtra(PHONE_NUMBER_INTENT , dbForm.getPhone().get(i));
                        intent.putExtra(EMAIL_ADDRESS_INTENT , dbForm.getPhone().get(i));
                        intent.putExtra(MAP_LOCATION_INTENT , dbForm.getPhone().get(i));
                        intent.putExtra(INTRO_INTENT , dbForm.getPhone().get(i));
                        startActivity(intent);
                    }
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
                onRestart();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        Intent intent = new Intent(ListActivity.this , ListActivity.class);
        startActivity(intent);
        this.finish();
        super.onRestart();
    }
}
