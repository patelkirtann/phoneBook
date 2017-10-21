package com.contact_app.fleet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.Blob;
import java.util.ArrayList;

public class DBForm extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String CONTACTS_TABLE_NAME = "contacts";
    private static final String CONTACTS_COLUMN_ID = "id";
    private static final String CONTACTS_COLUMN_NAME = "name";
    private static final String CONTACTS_COLUMN_EMAIL = "email";
    private static final String CONTACTS_COLUMN_PHONE = "phone";
    private static final String CONTACTS_COLUMN_STREET = "street";
    private static final String CONTACTS_COLUMN_CITY = "city";
    private static final String CONTACTS_COLUMN_INTRO = "intro";
    private static final String CONTACTS_COLUMN_PICTURE = "picture";
    private static Blob MY_BLOB = null;

    private static DBForm dbForm = null;

    DBForm(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public static DBForm getInstance(Context context) {
        if (dbForm == null) {
            dbForm = new DBForm(context.getApplicationContext());
        }
        return dbForm;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + CONTACTS_TABLE_NAME +
                        "(" + CONTACTS_COLUMN_ID + " integer not null primary key, " +
                        CONTACTS_COLUMN_NAME + " text," +
                        CONTACTS_COLUMN_PHONE + " text," +
                        CONTACTS_COLUMN_EMAIL + " text, " +
                        CONTACTS_COLUMN_STREET + " text," +
                        CONTACTS_COLUMN_CITY + " text," +
                        CONTACTS_COLUMN_INTRO + " text," +
                        CONTACTS_COLUMN_PICTURE + " blob default " + MY_BLOB + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (newVersion == 3) {
            db.execSQL("ALTER TABLE " + CONTACTS_TABLE_NAME + " ADD COLUMN " +
                    CONTACTS_COLUMN_PICTURE + " BLOB default " + MY_BLOB);
        }
//        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
//        onCreate(db);
    }

    public boolean insertValue(String name, String email, String phone,
                        String street, String city, String intro, byte[] picture) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.getReadableDatabase();

        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_STREET, street);
        contentValues.put(CONTACTS_COLUMN_CITY, city);
        contentValues.put(CONTACTS_COLUMN_INTRO, intro);
        contentValues.put(CONTACTS_COLUMN_PICTURE, picture);

        db.insert(CONTACTS_TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public ArrayList<String> getName() {
        ArrayList<String> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("select " + CONTACTS_COLUMN_NAME + " from " +
                CONTACTS_TABLE_NAME, null);
        cur.moveToFirst();
        if (cur.moveToFirst()) {
            while (!cur.isAfterLast()) {
                array_list.add(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_NAME)));
                cur.moveToNext();
            }
        }
        cur.close();
        db.close();
        return array_list;
    }

    public void deleteContactByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("delete from contacts where name='" + name + "'", null);
        cur.moveToFirst();
        cur.close();
        db.close();
    }

    public void updateContact(String id, String name, String email, String phone, String street,
                       String city, String intro, byte[] picture) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put("name", name);
        cv.put("email", email);
        cv.put("phone", phone);
        cv.put("street", street);
        cv.put("city", city);
        cv.put("intro", intro);
        cv.put("picture", picture);

        db.update(CONTACTS_TABLE_NAME, cv, CONTACTS_COLUMN_ID + " = " + id, null);
        cv.clear();
        db.close();

    }

    public RetrieveContactRecord getSingleContactById(String id) {
        Log.d("DbForm", "Value of Name: " + id);
        RetrieveContactRecord userRecord = new RetrieveContactRecord();
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select * from " + CONTACTS_TABLE_NAME + " where " + CONTACTS_COLUMN_ID
                + " = " + id;
        Cursor cur = db.rawQuery(qu, null);
        cur.moveToFirst();

//        userRecord.setId(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_ID)));
        userRecord.setName(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_NAME)));
        userRecord.setEmail(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_EMAIL)));
        userRecord.setPhone(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_PHONE)));
        userRecord.setStreet(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_STREET)));
        userRecord.setCity(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_CITY)));
        userRecord.setIntro(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_INTRO)));
        userRecord.setPicture(cur.getBlob(cur.getColumnIndex(CONTACTS_COLUMN_PICTURE)));

        cur.close();
        db.close();
        return userRecord;
    }

    public ArrayList<RetrieveContactRecord> getListData() {

        ArrayList<RetrieveContactRecord> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select " + CONTACTS_COLUMN_NAME + ","
                + CONTACTS_COLUMN_INTRO + ","
                + CONTACTS_COLUMN_PICTURE
                + " from " + CONTACTS_TABLE_NAME
                + " order by " + CONTACTS_COLUMN_NAME + " ASC ";
        Cursor cur = db.rawQuery(qu, null);
        if (cur.moveToFirst()) {
            while (!cur.isAfterLast()) {
                RetrieveContactRecord userRecord = new RetrieveContactRecord();
                userRecord.setName(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_NAME)));
                userRecord.setIntro(cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_INTRO)));
                userRecord.setPicture(cur.getBlob(cur.getColumnIndex(CONTACTS_COLUMN_PICTURE)));
                userList.add(userRecord);
                cur.moveToNext();
            }
        }
        cur.close();
        db.close();
        return userList;
    }

    public boolean checkName(String name) {
        return getName().contains(name);
    }

    public String getIdByName(String name) {
        String id;
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select " + CONTACTS_COLUMN_ID + " from " + CONTACTS_TABLE_NAME
                + " where " + CONTACTS_COLUMN_NAME
                + " = '" + name + "'";
        Cursor cur = db.rawQuery(qu, null);
        cur.moveToFirst();
        id = cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_ID));

        cur.close();
        db.close();
        return id;
    }

    public String getPhoneByName(String findName) {
        String phone;
        SQLiteDatabase db = this.getReadableDatabase();
        String qu = "select " + CONTACTS_COLUMN_PHONE + " from " + CONTACTS_TABLE_NAME
                + " where " + CONTACTS_COLUMN_NAME
                + " = '" + findName + "'";
        Cursor cur = db.rawQuery(qu, null);
        cur.moveToFirst();
        phone = cur.getString(cur.getColumnIndex(CONTACTS_COLUMN_PHONE));

        cur.close();
        db.close();
        return phone;
    }

}
