package com.yunitski.bookkeeper.moneykeeper;

import android.provider.BaseColumns;

public class InputData2 {
    public static final String DB_NAME = "history2";
    public static final int DB_VERSION = 1;


    static class TaskEntry2 implements BaseColumns {
        public static final String TABLE2 = "tasks2";

        public static final String DATE2 = "date2";

        public static final String TOTAL_VALUE2 = "total_value2";

        public static final String VALUE2 = "value2";

        public static final String OPERATION2 = "operation2";

        public static final String CATEGORY2 = "category2";
    }
}