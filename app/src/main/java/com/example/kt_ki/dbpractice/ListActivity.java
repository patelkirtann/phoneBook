package com.example.kt_ki.dbpractice;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    ListView mListNames;
    List<String> mNames;
    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = (ListView) findViewById(android.R.id.list);
        mListNames.setSelector(R.color.colorAccent);

        mNames = new ArrayList<>();
//        for (int i = 0; i < dbForm.getName().size(); i++) {
//            mNames.add(i, dbForm.getName().get(i).toUpperCase());
//        }
        mNames.addAll(dbForm.getName());
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.list_head, R.id.head, mNames);
        mListNames.setAdapter(adapter);
//        this.setListAdapter(new ArrayAdapter<>(this, R.layout.list_head, R.id.head , mNames));
    }
}
