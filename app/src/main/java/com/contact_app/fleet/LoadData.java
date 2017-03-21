package com.contact_app.fleet;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by kt_ki on 3/12/2017.
 */

class Wrapper{
    List<String> names;
    List<Bitmap> images;
}


class LoadData extends AsyncTask<Object, Object , Wrapper>{
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
    protected Wrapper doInBackground(Object... params) {

        Wrapper wrapper = new Wrapper();

        List<String> names = dbForm.getName();
        List<Bitmap> images = dbForm.getImage();

//        for (int i = 0; i < dbForm.getName().size(); i++) {
//           // names.add(dbForm.getName().get(i));
//            images.add(dbForm.getImage().get(i));
//        }

        wrapper.names = names;
        wrapper.images = images;

        return wrapper;
    }

    @Override
    protected void onPostExecute(Wrapper wrapper) {
        mListener.onCompletion(wrapper.names , wrapper.images);
        super.onPostExecute(wrapper);
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
