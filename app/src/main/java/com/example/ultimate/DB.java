package com.example.ultimate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DB  extends SQLiteOpenHelper {
    public static final String data = "data";

    public DB(@Nullable Context context) {
        super(context, data, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info (id INTEGER primary key autoincrement not null,uname TEXT,upass TEXT)");
        db.execSQL("create table logs (id INTEGER primary key autoincrement not null,uname TEXT,activity INTEGER, times TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getInfo() {
        SQLiteDatabase d = this.getWritableDatabase();
        Cursor r = d.rawQuery("select * from info",null);
        return r;
    }

    public Cursor getLogs() {
        SQLiteDatabase d = this.getWritableDatabase();
        Cursor r = d.rawQuery("select * from logs",null);
        return r;
    }

    public boolean infoInsert(ContentValues c, String table) {
        SQLiteDatabase sd = this.getWritableDatabase();
        long b = sd.insert(table, null, c);
        if (b != -1) {
            return true;
        }
        return false;
    }
}
