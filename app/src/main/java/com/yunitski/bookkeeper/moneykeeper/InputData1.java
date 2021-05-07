package com.yunitski.bookkeeper.moneykeeper;

import android.provider.BaseColumns;

public class InputData1 {
    public static final String DB_NAME = "history1";
    public static final int DB_VERSION = 1;


    static class TaskEntry1 implements BaseColumns {
        public static final String TABLE1 = "tasks1";

        public static final String DATE1 = "date1";

        public static final String TOTAL_VALUE1 = "total_value1";

        public static final String VALUE1 = "value1";

        public static final String OPERATION1 = "operation1";
    }
}
