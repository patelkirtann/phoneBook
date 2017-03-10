package com.example.kt_ki.dbpractice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

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
    private static final String CONTACTS_COLUMN_INTRO = "intro";

    DBForm(Context context) {
        super(context, DATABASE_NAME, null, 1);
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
                        CONTACTS_COLUMN_INTRO + " text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        onCreate(db);
    }

    boolean insertValue(String name, String email, String phone,
                        String street, String city, String intro) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(CONTACTS_COLUMN_NAME, name);
        contentValues.put(CONTACTS_COLUMN_EMAIL, email);
        contentValues.put(CONTACTS_COLUMN_PHONE, phone);
        contentValues.put(CONTACTS_COLUMN_STREET, street);
        contentValues.put(CONTACTS_COLUMN_CITY, city);
        contentValues.put(CONTACTS_COLUMN_INTRO, intro);

        sqLiteDatabase.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    public ArrayList<String> getID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_ID + " from " + CONTACTS_TABLE_NAME, null);
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
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_NAME + " from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();
        if (res.moveToFirst()) {
            while (!res.isAfterLast()) {
                array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
                res.moveToNext();
            }
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getEmail() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_EMAIL + " from " + CONTACTS_TABLE_NAME, null);
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
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_PHONE + " from " + CONTACTS_TABLE_NAME, null);
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
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_STREET + " from " + CONTACTS_TABLE_NAME, null);
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
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_CITY + " from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_CITY)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getIntro() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_INTRO + " from " + CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_INTRO)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> deleteContactByName(String name) {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("mDelete from contacts where id=" + name, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }


    public ArrayList<String> deleteContactByID(int id) {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("mDelete from contacts where id=" + id, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

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

    boolean checkName(String name) {
        return getName().contains(name);
    }

    int findPosition(String name) {
        int pos = 0;
            if (getName().contains(name)) {
                pos = getName().indexOf(name);
            }
        return pos;
    }
}
