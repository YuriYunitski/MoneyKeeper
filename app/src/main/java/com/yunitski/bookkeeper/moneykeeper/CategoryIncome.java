package com.yunitski.bookkeeper.moneykeeper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class CategoryIncome extends SQLiteOpenHelper {
    //объявить имя и версию бд
    public static final String DB_NAME = "inCat";
    public static final int DB_VERSION = 1;

    public CategoryIncome(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //создать бд
        String createTable = "CREATE TABLE " + CatInEntry.TABLECI + " (" + CatInEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + CatInEntry.IN_CATEGORY + " TEXT);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + CatInEntry.TABLECI);
        onCreate(db);    }

        //статический класс со столбцами бд
        //нужен для упрощенного доступа к элементам
    static class CatInEntry implements BaseColumns {
        public static final String TABLECI = "outTable";

        public static final String IN_CATEGORY = "out_category";
    }
}
