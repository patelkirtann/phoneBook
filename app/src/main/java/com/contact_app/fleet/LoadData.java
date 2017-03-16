package com.contact_app.fleet;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kt_ki on 3/12/2017.
 */

class LoadData extends AsyncTask<Object, Object, List<String>> {

    private DataListener mListener;
    private DBForm mDataProvider;

    LoadData(DBForm dataProvider, DataListener listener) {
        mDataProvider = dataProvider;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected List<String> doInBackground(Object... strings) {
        Log.d("Debug", "doInBackground");
        List<String> names = new ArrayList<>(mDataProvider.getName().size());
        for (int i = 0; i < mDataProvider.getName().size(); i++) {
            names.add(i, mDataProvider.getName().get(i));
        }
        Collections.sort(names);
        return names;
    }

    @Override
    protected void onPostExecute(List<String> names) {
        mListener.onCompletion(names);
        super.onPostExecute(names);
    }
}
