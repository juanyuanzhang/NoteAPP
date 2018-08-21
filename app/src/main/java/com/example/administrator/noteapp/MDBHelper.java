package com.example.administrator.noteapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MDBHelper extends SQLiteOpenHelper { //繼承SQLiteOpenHelper

    public MDBHelper(Context context) {
        super(context, "notesource.DB", null, 1); //設定資料庫名稱
    }

    @Override
    public void onCreate(SQLiteDatabase db) {   //建立資料表
        db.execSQL("CREATE TABLE IF NOT EXISTS notesource"+
        "(_id INTEGER PRIMARY KEY autoincrement ,date VARCHAR(10),top VARCHAR(20),cont VARCHAR(100),color VARCHAR(10))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS notesource");
        onCreate(db);
    }
}
