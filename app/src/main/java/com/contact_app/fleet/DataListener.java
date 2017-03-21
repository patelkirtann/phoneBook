package com.contact_app.fleet;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by kt_ki on 3/12/2017.
 */

interface DataListener {
    void onPreExecute();
    void onProgress(int progress);
    void onCompletion(List<String> data);
}
