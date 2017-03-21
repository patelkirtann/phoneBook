package com.contact_app.fleet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.util.ArrayList;
import java.util.Arrays;

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
    private static final String CONTACTS_COLUMN_PICTURE = "picture";

    DBForm(Context context) {
        super(context, DATABASE_NAME, null, 2);
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
                        CONTACTS_COLUMN_INTRO + " text" +
                        CONTACTS_COLUMN_PICTURE + "blob)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if (newVersion > oldVersion) {
                db.execSQL("ALTER TABLE " + CONTACTS_TABLE_NAME + " ADD COLUMN " +
                        CONTACTS_COLUMN_PICTURE + " BLOB");
            }
//        db.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
//        onCreate(db);
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
//        contentValues.put(CONTACTS_COLUMN_PICTURE, picture);

        sqLiteDatabase.insert(CONTACTS_TABLE_NAME, null, contentValues);
        return true;
    }

    ArrayList<String> getID() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_ID + " from " +
                CONTACTS_TABLE_NAME, null);
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
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_NAME + " from " +
                CONTACTS_TABLE_NAME, null);
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

    ArrayList<String> getEmail() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_EMAIL + " from " +
                CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_EMAIL)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    ArrayList<String> getPhone() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_PHONE + " from " +
                CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    ArrayList<String> getStreet() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_STREET + " from " +
                CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_STREET)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    ArrayList<String> getCity() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_CITY + " from " +
                CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_CITY)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    ArrayList<String> getIntro() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select " + CONTACTS_COLUMN_INTRO + " from " +
                CONTACTS_TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_INTRO)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    ArrayList<Bitmap> getImage() {
        ArrayList<Bitmap> array_list = new ArrayList<>();

        String qu = "select " + CONTACTS_COLUMN_PICTURE + " from " + CONTACTS_TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery(qu, null);

        if (cur.moveToFirst()) {
            byte[] imgByte = cur.getBlob(0);
            array_list.add(BitmapFactory.decodeByteArray(imgByte, 100, imgByte.length));
            cur.close();
            return array_list;
        }
        if (!cur.isClosed()) {
            cur.close();
        }
        return null;
    }

    void updateImage(String name, byte[] image) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(name, image);
        sqLiteDatabase.insert(CONTACTS_TABLE_NAME, null, contentValues);

    }

    void deleteContactByID(String id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("delete from contacts where id=" + id, null);
        res.moveToFirst();
        res.close();
    }

    void updateContact(String id, String name, String email, String phone, String street,
                       String city, String intro) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("email", email);
        cv.put("phone", phone);
        cv.put("street", street);
        cv.put("city", city);
        cv.put("intro", intro);

        db.update(CONTACTS_TABLE_NAME, cv, "id=" + id, null);
    }


    void deleteAll() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("delete from contacts", null);
        res.moveToFirst();
        res.close();
    }

    boolean checkName(String name) {
        return getName().contains(name);
    }


}
