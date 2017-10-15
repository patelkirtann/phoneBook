package com.contact_app.fleet.CallLog;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.contact_app.fleet.BuildConfig;
import com.contact_app.fleet.RetrieveContactRecord;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kt_ki on 8/20/2017.
 */

public class CallLogGetter {
    public ArrayList<RetrieveContactRecord> logList = new ArrayList<>();

    public ArrayList<RetrieveContactRecord> getLogList(Context context, int startPos, int endPos) {
        try {
            int permissionCheck = ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_CALL_LOG);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                Cursor c = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null,
                        null,
                        null, CallLog.Calls.DATE + " DESC");

                Log.i("List Size", String.valueOf(logList.size()));
                if (c != null && c.getCount() > startPos) {
                    Log.i("Test1", "cursor listStart " + startPos + " listEnd " + endPos);
                    Log.i("Test1", "cursor position => " + String.valueOf(c.getCount() - endPos));
                    c.moveToPosition(startPos);

                    do {
                        RetrieveContactRecord logModel = new RetrieveContactRecord();

                        // Get Phone Number
                        String number = c.getString(c.getColumnIndex(CallLog.Calls.NUMBER));
                        logModel.setLogNumber(number);

                        // Get Call Duration
                        long time = c.getInt(c.getColumnIndex(CallLog.Calls.DURATION));
                        int minutes = (int) (time / (60));
                        int seconds = (int) (time % 60);
                        String str = String.format(new Locale(String.valueOf(time)), "%02d:%02d",
                                minutes, seconds);
                        if (minutes == 0){
                            logModel.setLogDuration(str + " sec");
                        }else {
                            logModel.setLogDuration(str + " min");
                        }

                        // Get Contact Name
//                        String name = c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME));
                        String name = getContactName(context, number);
                        if (name == null) {
                            logModel.setLogName("Unknown");
                        } else {
                            logModel.setLogName(name);
                        }

                        Log.i("Test2", "cursor listStart " + c.getPosition() + " name " + name);
                        // Get Call Date and Time
                        Date date = new Date(c.getLong(c.getColumnIndex(CallLog.Calls.DATE)));
                        Log.i("Date", date.toString());
                        String dateFormat;
                        if (BuildConfig.VERSION_CODE < 23){
                            dateFormat = new SimpleDateFormat("MM/dd",
                                    new Locale("")).format(date).trim();
                        }else {

                            dateFormat = new SimpleDateFormat("EEE, MMM d",
                                    new Locale("")).format(date).trim();
                        }
                        String timeFormat = new SimpleDateFormat("hh:mm a",
                                new Locale("")).format(date).trim();
                        Log.i("Time", timeFormat);

                        logModel.setLogTime(timeFormat);
                        logModel.setLogDate(dateFormat);
                        // Get Contact ID
                        long contactID = getContactIDFromNumber(number, context);

                        // Get Contact Photo
                        InputStream stream = getContactPhoto(contactID, context);
                        if (stream!=null){
                            logModel.setLogCallImage(BitmapFactory.decodeStream(stream));
                        }

                        // Get Call Type
                        int callType = c.getInt(c.getColumnIndex(CallLog.Calls.TYPE));
                        if (callType == CallLog.Calls.INCOMING_TYPE) {

                            logModel.setLogCallType(callType);

                        } else if (callType == CallLog.Calls.OUTGOING_TYPE) {

                            logModel.setLogCallType(callType);

                        } else if (callType == CallLog.Calls.MISSED_TYPE) {

                            logModel.setLogCallType(callType);

                        }

                        // Add Contacts one by one to the List
                        logList.add(logModel);

                        Log.i("Test1", "listSize " + logList.size());
                    } while (c.moveToNext() && logList.size() <= endPos - startPos);
                    c.close();
                }
            } else {
                Toast.makeText(context, "Grant Phone Permission", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return logList;
    }

    private long getContactIDFromNumber(String contactNumber, Context context) {
        long phoneContactID = 0;
        Cursor contactLookupCursor = context.getContentResolver()
                .query(Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                        Uri.encode(contactNumber)),
                        new String[]{ContactsContract.PhoneLookup._ID}, null, null, null);
        if (contactLookupCursor != null) {
            while (contactLookupCursor.moveToNext()) {
                phoneContactID = contactLookupCursor.getLong(contactLookupCursor
                        .getColumnIndexOrThrow(ContactsContract.PhoneLookup._ID));
            }
            contactLookupCursor.close();
        }
        return phoneContactID;
    }

    private InputStream getContactPhoto(long contactId, Context context) {
        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactId);
        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[]{ContactsContract.Contacts.Photo.PHOTO}, null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                byte[] data = cursor.getBlob(0);
                if (data != null) {
                    return new ByteArrayInputStream(data);
                }
            }
        } finally {
            cursor.close();
        }
        return null;
    }

    private String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME},
                null, null, null);
        if (cursor == null) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(
                        ContactsContract
                                .PhoneLookup
                                .DISPLAY_NAME));
            }
        } finally {
            cursor.close();
        }

        return null;
    }
}
