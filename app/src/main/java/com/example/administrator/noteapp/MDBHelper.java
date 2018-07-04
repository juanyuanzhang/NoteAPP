package com.example.administrator.noteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MDBHelper extends SQLiteOpenHelper {

    public MDBHelper(Context context) {
        super(context, "notesource", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS notesource"+
        "(_id INTEGER PRIMARY KEY autoincrement ,date,top,cont,notify)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notesource");
        onCreate(db);
    }
}
