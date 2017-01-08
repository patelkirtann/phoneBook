package com.example.kt_ki.dbpractice;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class AllRecord extends AppCompatActivity {
    ExpandableListView expandableListView;
    ExpandableAdapter expandableAdapter;
    List<String> listHead;
    HashMap<String, List<String>> listChild;

    DBForm dbForm = new DBForm(AllRecord.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listData();

        expandableAdapter = new ExpandableAdapter(this, listHead, listChild);
        expandableListView.setAdapter(expandableAdapter);
    }

    private void listData() {
        listHead = new ArrayList<>();
        listChild = new HashMap<>();

        for (int i = 0; i < dbForm.getID().size(); i++) {
            listHead.add(i, dbForm.getName().get(i).toUpperCase());

            List<String> data = new ArrayList<>();
            data.add(dbForm.getID().get(i));
            data.add(dbForm.getEmail().get(i));
            data.add(dbForm.getPhone().get(i));
            data.add(dbForm.getStreet().get(i));
            data.add(dbForm.getCity().get(i));

            listChild.put(listHead.get(i), data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.exit(0);
    }
}
