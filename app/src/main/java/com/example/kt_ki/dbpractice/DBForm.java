package com.example.kt_ki.dbpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by kt_ki on 11/16/2016.
 */

class DBForm extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "MyDBName.db";
    private static final String CONTACTS_TABLE_NAME = "contacts";
    private static final String CONTACTS_COLUMN_ID = "id";
    private static final String CONTACTS_COLUMN_NAME = "name";
    private static final String CONTACTS_COLUMN_EMAIL = "email";
    private static final String CONTACTS_COLUMN_PHONE = "phone";
    private static final String CONTACTS_COLUMN_STREET = "street";
    private static final String CONTACTS_COLUMN_CITY = "city";
//    private static final String CONTACTS_COLUMN_INFO = "info";

    DBForm(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table contacts " +
                        "(id integer not null primary key, " +
                        "name text ," +
                        "phone text," +
                        "email text, " +
                        "street text," +
                        "city text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    boolean insertValue(String name, String email, String phone,
                        String street, String city) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_STREET, street);
        contentValues.put(CONTACTS_COLUMN_CITY, city);
//        contentValues.put(CONTACTS_COLUMN_INFO, info);

        sqLiteDatabase.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<String> getID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getName() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select name from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getEmail() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select email from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getPhone() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select phone from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getStreet() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select street from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_STREET)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getCity() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select city from contacts" , null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_CITY)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

//    public ArrayList<String> getInfo() {
//        ArrayList<String> array_list = new ArrayList<>();
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select info from contacts" , null);
//        res.moveToFirst();
//
//        while (!res.isAfterLast()) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_INFO)));
//            res.moveToNext();
//        }
//        res.close();
//        return array_list;
//    }


    ArrayList<String> deleteAll() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("delete from contacts", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}
