package com.example.ultimate;

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
        db.execSQL("create table info (id INTEGER,uname TEXT,upass TEXT)");
        db.execSQL("create table logs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,uname TEXT,activity INTEGER, times TEXT)");
        db.execSQL("insert into info (id,uname,upass) VALUES (1,\"PRIMARY KEY\",\"TEXT\")");
        db.execSQL("insert into logs (id,uname,activity,times) VALUES (1,\"PRIMARY KEY\",1,\"TEXT\")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getData() {
        SQLiteDatabase d = this.getWritableDatabase();
        Cursor r = d.rawQuery("select * from logs",null);
        return r;
    }
}
