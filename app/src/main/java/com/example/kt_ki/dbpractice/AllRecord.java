package com.example.kt_ki.dbpractice;

import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllRecord extends AppCompatActivity {
    ExpandableListView mExpandableListView;
    ExpandableAdapter mExpandableAdapter;
    List<String> mListHead;
    HashMap<String, List<String>> mListChild;

    DBForm dbForm = new DBForm(AllRecord.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        listData();
        mExpandableAdapter = new ExpandableAdapter(AllRecord.this, mListHead, mListChild);
        mExpandableListView.setAdapter(mExpandableAdapter);


    }

    private void listData() {
        mListHead = new ArrayList<>();
        mListChild = new HashMap<>();

        for (int i = 0; i < dbForm.getID().size(); i++) {
            mListHead.add(i, dbForm.getName().get(i).toUpperCase());

            List<String> data = new ArrayList<>();
            data.add("    ID:      ".toUpperCase() + dbForm.getID().get(i));
            data.add(" Email:      ".toUpperCase() + dbForm.getEmail().get(i));
            data.add(" Phone:      ".toUpperCase() + dbForm.getPhone().get(i));
            data.add("Street:      ".toUpperCase() + dbForm.getStreet().get(i));
            data.add("  City:      ".toUpperCase() + dbForm.getCity().get(i));
            data.add(" Intro:      ".toUpperCase() + dbForm.getIntro().get(i));

            mListChild.put(mListHead.get(i), data);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share_link, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String mimetype = "text/plain";
        String title = "Share with";
        String text = "Link Here";
        ShareCompat.IntentBuilder.from(this)
                .setChooserTitle(title)
                .setType(mimetype)
                .setText(text)
                .startChooser();
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        System.exit(0);
//    }
}
