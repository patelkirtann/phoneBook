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

//class LoadData extends AsyncTask<Object, Object, List<String>> {
//
//    private DataListener mListener;
//    private DBForm mDataProvider;
//
//    LoadData(DBForm dataProvider, DataListener listener) {
//        mDataProvider = dataProvider;
//        mListener = listener;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        mListener.onPreExecute();
//        super.onPreExecute();
//    }
//
//    @Override
//    protected List<String> doInBackground(Object... strings) {
//        Log.d("Debug", "doInBackground");
//        List<String> names = new ArrayList<>();
//
//        for (int i = 0; i < mDataProvider.getName().size(); i++) {
//            names.add(mDataProvider.getName().get(i));
//        }
//        Collections.sort(names);
//        return names;
//    }
//
//    @Override
//    protected void onPostExecute(List<String> names) {
//        mListener.onCompletion(names);
//        super.onPostExecute(names);
//    }
//}
