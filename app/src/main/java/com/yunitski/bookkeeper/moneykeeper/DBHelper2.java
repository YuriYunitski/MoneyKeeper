package com.yunitski.bookkeeper.moneykeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper2 extends SQLiteOpenHelper {

    public DBHelper2(Context context) {
        super(context, InputData2.DB_NAME, null, InputData2.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + InputData2.TaskEntry2.TABLE2 + " (" + InputData2.TaskEntry2._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + InputData2.TaskEntry2.VALUE2 + " TEXT, " + InputData2.TaskEntry2.TOTAL_VALUE2 + " TEXT, "  + InputData2.TaskEntry2.DATE2 + " TEXT, " + InputData2.TaskEntry2.OPERATION2 + " INTEGER, " + InputData2.TaskEntry2.CATEGORY2 + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + InputData2.TaskEntry2.TABLE2);
        onCreate(db);
    }
}
