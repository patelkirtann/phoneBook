package com.example.kt_ki.dbpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_list , menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return false;
            }
        });
        return super.onOptionsItemSelected(item);
    }
}
