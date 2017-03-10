package com.first_app.fleet;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AllRecord extends AppCompatActivity {
    ExpandableListView mExpandableListView;
    ExpandableAdapter mExpandableAdapter;
    List<String> mListHead;
    HashMap<String, List<String>> mListChild;

    DBForm dbForm = new DBForm(AllRecord.this);
    ProgressBar mProgressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);

        mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        mProgressbar = (ProgressBar) findViewById(R.id.pb_progress);
        listData();

        mExpandableAdapter = new ExpandableAdapter(AllRecord.this, mListHead, mListChild);
    }

    private void listData() {
        mListHead = new ArrayList<>();
        mListChild = new HashMap<>();
        new LoadData().execute("");

    }

    private class LoadData extends AsyncTask<Object, Object, HashMap<String, List<String>>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressbar.setVisibility(View.VISIBLE);
        }

        @Override
        protected HashMap<String, List<String>> doInBackground(Object... objects) {
            for (int i = 0; i < dbForm.getID().size(); i++) {
                mListHead.add(i, dbForm.getName().get(i).toUpperCase());

                List<String> data = new ArrayList<>();
                data.add("          " + "    ID:      ".toUpperCase() + dbForm.getID().get(i));
                data.add("     " + " Email:      ".toUpperCase() + dbForm.getEmail().get(i));
                data.add("    " + " Phone:      ".toUpperCase() + dbForm.getPhone().get(i));
                data.add("    " + "Street:      ".toUpperCase() + dbForm.getStreet().get(i));
                data.add("        " + "  City:      ".toUpperCase() + dbForm.getCity().get(i));
                data.add("     " + " Intro:      ".toUpperCase() + dbForm.getIntro().get(i));

                mListChild.put(mListHead.get(i), data);
            }
            return mListChild;
        }

        @Override
        protected void onPostExecute(HashMap<String, List<String>> aVoid) {
            super.onPostExecute(aVoid);
            mProgressbar.setVisibility(View.INVISIBLE);
            mExpandableListView.setAdapter(mExpandableAdapter);
        }
    }
}
