package com.yunitski.bookkeeper.moneykeeper;

import android.provider.BaseColumns;

public class InputData {
    //объявить имя и версию бд
    public static final String DB_NAME = "history";
    public static final int DB_VERSION = 1;


    //статический класс со столбцами бд
    //нужен для упрощенного доступа к элементам
    static class TaskEntry implements BaseColumns {
        public static final String TABLE = "tasks";

        public static final String DATE = "date";

        public static final String TOTAL_VALUE = "total_value";

        public static final String VALUE = "value";

        public static final String OPERATION = "operation";

        public static final String CATEGORY = "category";
    }
}