package com.yunitski.bookkeeper.moneykeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper1 extends SQLiteOpenHelper {

    public DBHelper1(Context context) {
        super(context, InputData1.DB_NAME, null, InputData1.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //создать бд
        String createTable = "CREATE TABLE " + InputData1.TaskEntry1.TABLE1 + " (" + InputData1.TaskEntry1._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InputData1.TaskEntry1.VALUE1 + " TEXT, " + InputData1.TaskEntry1.TOTAL_VALUE1 + " TEXT, "  + InputData1.TaskEntry1.DATE1 + " TEXT, " + InputData1.TaskEntry1.OPERATION1 + " INTEGER, " + InputData1.TaskEntry1.CATEGORY1 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputData1.TaskEntry1.TABLE1);
        onCreate(db);
    }
}