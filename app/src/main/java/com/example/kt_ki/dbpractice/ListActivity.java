package com.example.kt_ki.dbpractice;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends android.app.ListActivity {
    ListView mListNames;
    List<String> mNames;
    DBForm dbForm = new DBForm(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        mListNames = getListView();
        mListNames.setSelector(R.color.colorAccent);

        mNames = new ArrayList<>();
        for (int i = 0; i < dbForm.getID().size(); i++) {
            mNames.add(i , dbForm.getName().get(i).toUpperCase());
        }
        this.setListAdapter(new ArrayAdapter<>(this, R.layout.list_head, R.id.head , mNames));
    }
}
