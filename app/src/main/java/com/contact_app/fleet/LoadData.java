package com.contact_app.fleet;

import android.os.AsyncTask;
import java.util.ArrayList;

class LoadData extends AsyncTask<Object, Object, ArrayList<RetrieveContactRecord>> {
    private DataListener mListener;
    private DBForm dbForm;

    LoadData(DBForm dataProvider, DataListener listener) {
        dbForm = dataProvider;
        mListener = listener;
    }

    @Override
    protected void onPreExecute() {
        mListener.onPreExecute();
        super.onPreExecute();
    }

    @Override
    protected ArrayList<RetrieveContactRecord> doInBackground(Object... params) {

        ArrayList<RetrieveContactRecord> userRecords = dbForm.getListData();

        dbForm.close();
        return userRecords;
    }

    @Override
    protected void onPostExecute(ArrayList<RetrieveContactRecord> userRecords) {
        mListener.onCompletion(userRecords);
        super.onPostExecute(userRecords);
    }
}