package com.yunitski.bookkeeper.moneykeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;

public class CategoryOutcome extends SQLiteOpenHelper {
    public static final String DB_NAME = "outCat";
    public static final int DB_VERSION = 1;

    public CategoryOutcome(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + CatEntry.TABLEC + " (" + CatEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CatEntry.OUT_CATEGORY + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CatEntry.TABLEC);
        onCreate(db);    }

    static class CatEntry implements BaseColumns {
        public static final String TABLEC = "outTable";

        public static final String OUT_CATEGORY = "out_category";
    }
}
