package com.first_app.fleet;

import java.util.List;

/**
 * Created by kt_ki on 3/12/2017.
 */

interface DataListener {
    void onPreExecute();
    void onProgress(int progress);
    void onCompletion(List<String> data);
}
