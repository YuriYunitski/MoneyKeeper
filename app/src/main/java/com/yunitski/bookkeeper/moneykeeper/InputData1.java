package com.yunitski.bookkeeper.moneykeeper;

import android.provider.BaseColumns;

public class InputData1 {
    //объявить имя и версию бд
    public static final String DB_NAME = "history1";
    public static final int DB_VERSION = 1;


    //статический класс со столбцами бд
    //нужен для упрощенного доступа к элементам
    static class TaskEntry1 implements BaseColumns {
        public static final String TABLE1 = "tasks1";

        public static final String DATE1 = "date1";

        public static final String TOTAL_VALUE1 = "total_value1";

        public static final String VALUE1 = "value1";

        public static final String OPERATION1 = "operation1";

        public static final String CATEGORY1 = "category1";
    }
}
