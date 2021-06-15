package com.example.mytiktek.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HistoryDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "solutionsHistory.db";
    public static final String TABLE_NAME = "data";
    public static final String COL1 = "uID";
    public static final String COL2 = "solutionData";
    public static final String COL3 = "bookImg";


    public HistoryDB(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable1 = "create table " + TABLE_NAME + " (uid text, solutionData text, bookImg text )" ;
        db.execSQL(createTable1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addData(String uid, String solutionData, String bookImg) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, uid);
        contentValues.put(COL2, solutionData);
        contentValues.put(COL3, bookImg);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if date as inserted incorrectly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }

    public Cursor getHistoryOfUser(String uid){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME +" WHERE uid=\'" + uid + "\' ", null);
        return data;
    }
}
