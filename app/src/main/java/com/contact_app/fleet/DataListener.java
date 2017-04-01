package com.contact_app.fleet;

import java.util.ArrayList;

/**
 * Created by kt_ki on 3/12/2017.
 */

interface DataListener {
    void onPreExecute();
    void onProgress(int progress);
    void onCompletion(ArrayList<RetrieveContactRecord> userRecords);
}
