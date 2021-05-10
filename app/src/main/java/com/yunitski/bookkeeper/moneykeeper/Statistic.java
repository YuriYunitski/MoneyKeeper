package com.yunitski.bookkeeper.moneykeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Statistic extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    DBHelper dbHelper;
    DBHelper1 dbHelper1;
    DBHelper2 dbHelper2;
    CategoryIncome categoryIncome;
    CategoryOutcome categoryOutcome;
//    CalendarView calendarView;
//    TextView date;
    TextView textView;
//
//    String ss;
//    boolean isSelectAll, isCategory, isValue, isTotalValue, isOperation;
//    Button param;
//    public static ArrayList<String> currentDate = new ArrayList<>();
    SharedPreferences sharedPreferences;
//
//    String s1, s2, s3, s4;
//
//    TextView totInc, totOutc, perc;
    SQLiteDatabase database;
//    ArrayList<String> totalIncome, totalOutcome, operation, val;
    Spinner spinner;
    ArrayList<String> dateList, valueList, categoryList, incomeList, outcomeList, inOpList, outOpList;
    ArrayList<Integer> currentDayList, currentWeekList, currentMonthList, currentYearList, operationList, categoryInCount, categoryOutCount;
    Set<String> allOps;
    ArrayAdapter<String> spinnerAdapter;
    String[] times = new String[]{"Неделя", "Месяц", "Год"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        toolbar = findViewById(R.id.toolBar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Статистика");
        dbHelper = new DBHelper(this);
        dbHelper1 = new DBHelper1(this);
        dbHelper2 = new DBHelper2(this);
        categoryIncome = new CategoryIncome(this);
        categoryOutcome = new CategoryOutcome(this);
        textView = findViewById(R.id.textView7);
        spinner = findViewById(R.id.time_spinner);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_statistic, R.id.spin_text_stat, times);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        dateList = new ArrayList<>();
                        valueList = new ArrayList<>();
                        operationList = new ArrayList<>();
                        categoryList = new ArrayList<>();
                        currentWeekList = new ArrayList<>();
                        currentMonthList = new ArrayList<>();
                        currentYearList = new ArrayList<>();
                        currentDayList = new ArrayList<>();
                        incomeList = new ArrayList<>();
                        outcomeList = new ArrayList<>();
                        allOps = new HashSet<>();
                        inOpList = new ArrayList<>();
                        outOpList = new ArrayList<>();
                        database = dbHelper.getReadableDatabase();
                        Cursor cursor = database.rawQuery("SELECT " + InputData.TaskEntry.DATE + ", " + InputData.TaskEntry.VALUE + ", " + InputData.TaskEntry.CATEGORY + ", " + InputData.TaskEntry.OPERATION +  " FROM " + InputData.TaskEntry.TABLE + ";", null);
                        if (!(cursor.getCount() <= 0)) {
                            if (cursor.moveToFirst()) {
                                do {
                                    dateList.add(cursor.getString(0));
                                    valueList.add(cursor.getString(1));
                                    categoryList.add(cursor.getString(2));
                                    operationList.add(cursor.getInt(3));
                                } while (cursor.moveToNext());
                            }
                        }
                        cursor.close();
                        database.close();
                        database = categoryIncome.getReadableDatabase();
                        Cursor cursor1 = database.rawQuery("SELECT " + CategoryIncome.CatInEntry.IN_CATEGORY + " FROM " + CategoryIncome.CatInEntry.TABLECI + ";", null);
                        if (!(cursor1.getCount() <= 0)) {
                            if (cursor1.moveToFirst()) {
                                do {
                                    inOpList.add(cursor1.getString(0));
                                } while (cursor1.moveToNext());
                            }
                        }
                        cursor1.close();
                        database.close();
                        database = categoryOutcome.getReadableDatabase();
                        Cursor cursor2 = database.rawQuery("SELECT " + CategoryOutcome.CatEntry.OUT_CATEGORY + " FROM " + CategoryOutcome.CatEntry.TABLEC + ";", null);
                        if (!(cursor2.getCount() <= 0)) {
                            if (cursor2.moveToFirst()) {
                                do {
                                    outOpList.add(cursor2.getString(0));
                                } while (cursor2.moveToNext());
                            }
                        }
                        cursor2.close();
                        database.close();
                        for (int i = 0; i < valueList.size(); i++){
                            if (operationList.get(i) == 2131165295){
                                incomeList.add(valueList.get(i));
                            } else {
                                outcomeList.add(valueList.get(i));
                            }
                        }
                        String cw = "";
                        String[] cxSp = null;
                        for (int i = 0; i < dateList.size(); i++){
                            cw = dateList.get(i);
                            cxSp = cw.split("\\.");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Integer.parseInt(cxSp[2]), Integer.parseInt(cxSp[1]) - 1, Integer.parseInt(cxSp[0]));
                            calendar.setMinimalDaysInFirstWeek(1);
                            int d = calendar.get(Calendar.DAY_OF_MONTH);
                            int wk = calendar.get(Calendar.WEEK_OF_MONTH);
                            int mt = calendar.get(Calendar.MONTH) + 1;
                            int y = calendar.get(Calendar.YEAR);
                            currentDayList.add(d);
                            currentWeekList.add(wk);
                            currentMonthList.add(mt);
                            currentYearList.add(y);
                        }
                        String currentDate = dateC();
                        String[] currentDateSplit = currentDate.split("\\.");
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Integer.parseInt(cxSp[2]), Integer.parseInt(cxSp[1]) - 1, Integer.parseInt(cxSp[0]));
                        calendar.setMinimalDaysInFirstWeek(1);
                        int wk = calendar.get(Calendar.WEEK_OF_MONTH);
                        int incomeSum = 0;
                        int outcomeSum = 0;
                        for (int i = 0; i < currentWeekList.size(); i++){
                            if (currentWeekList.get(i).equals(wk)){
                                if (operationList.get(i) == 2131165295){
                                    incomeSum += Integer.parseInt(valueList.get(i));
                                } else if (operationList.get(i) == 2131165294){
                                    outcomeSum += Integer.parseInt(valueList.get(i));
                                }
                            }
                        }
                        for (int i = 0; i < categoryList.size(); i++){
                            allOps.add(categoryList.get(i));
                        }
                        List<String> allOpsToArray = new ArrayList<>();
                        allOpsToArray.addAll(allOps);
                        categoryInCount = new ArrayList<>();
                        categoryOutCount = new ArrayList<>();
                        for (int i = 0; i < allOps.size(); i++){
                            int c = 0;
                            for (int k = 0; k < inOpList.size(); k++){
                                if (allOpsToArray.get(i).equals(inOpList.get(k))){
                                    c++;
                                }
                            }
                            categoryInCount.add(c);
                        }
                        textView.setText("" + currentDayList + "\n" + currentWeekList + "\n" + currentMonthList + "\n" + currentYearList + "\n" + dateList + "\n" + valueList + "\n" + categoryList + "\n" + operationList + "\n" + incomeList + "\n" + outcomeList + "\n" + incomeSum + "\n" + outcomeSum + "\n" + allOps + "\n" + allOpsToArray + "\n" + categoryInCount + "\n" + inOpList + "\n" + outOpList);
                        break;
                    case 1:
                        break;
                    case 2:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
//        totInc = findViewById(R.id.tv_inc);
//        totOutc = findViewById(R.id.tv_outc);
//        perc = findViewById(R.id.tv_perc);
//        param = findViewById(R.id.param);
//        param.setOnClickListener(this);
//        ss = "";
//        isSelectAll = false;
//        isValue = false;
//        isTotalValue = false;
//        isCategory = false;
//        isOperation = false;
//        currentDate.add(dateC());
//        textView = findViewById(R.id.textText);
//        s1 = "";
//        s2 = "";
//        s3 = "";
//        s4 = "";
//        totalIncome = new ArrayList<>();
//        totalOutcome = new ArrayList<>();
//        operation = new ArrayList<>();
//        val = new ArrayList<>();
//        showStat();
//        calendarView = findViewById(R.id.calendarView);
//        date = findViewById(R.id.date);
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String d = dayOfMonth + "." + (month + 1) + "." + year;
//                date.setText(d);
//            }
//        });
    }

    public String dateC(){
        Calendar c = new GregorianCalendar();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        return d + "." + m + "." + y;
    }

    @Override
    public void onClick(View v) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setCancelable(false);
//        builder.setTitle("Параметры");
//        LayoutInflater inflater = getLayoutInflater();
//        View view = inflater.inflate(R.layout.param_list, null);
//        builder.setView(view);
//        CheckBox cbSelectAll = view.findViewById(R.id.cb_sel_all);
//        CheckBox cbOpValue = view.findViewById(R.id.cb_op_val);
//        CheckBox cbBalance = view.findViewById(R.id.cb_bal);
//        CheckBox cbCategory = view.findViewById(R.id.cb_cat);
//        CheckBox cbOperation = view.findViewById(R.id.cb_op);
//        cbSelectAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isSelectAll) {
//                    isSelectAll = true;
//                    isValue = true;
//                    isTotalValue = true;
//                    isCategory = true;
//                    isOperation = true;
//                } else {
//                    isSelectAll = false;
//                    isValue = false;
//                    isTotalValue = false;
//                    isCategory = false;
//                    isOperation = false;
//                }
//                cbOpValue.setChecked(isValue);
//                cbBalance.setChecked(isTotalValue);
//                cbCategory.setChecked(isCategory);
//                cbOperation.setChecked(isOperation);
//            }
//        });
//        cbOpValue.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isValue){
//                    isValue = true;
//                } else {
//                    isValue = false;
//                }
//                cbOpValue.setChecked(isValue);
//            }
//        });
//        cbBalance.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isTotalValue){
//                    isTotalValue = true;
//                } else {
//                    isTotalValue = false;
//                }
//                cbBalance.setChecked(isValue);
//            }
//        });
//        cbCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isCategory){
//                    isCategory = true;
//                } else {
//                    isCategory = false;
//                }
//                cbCategory.setChecked(isCategory);
//            }
//        });
//        cbOperation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isOperation){
//                    isOperation = true;
//                } else {
//                    isOperation = false;
//                }
//                cbOperation.setChecked(isOperation);
//            }
//        });
//        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//                showStatistic(isValue, isTotalValue, isCategory, isOperation);
//                isSelectAll = false;
//                isValue = false;
//                isTotalValue = false;
//                isCategory = false;
//                isOperation = false;
//
//            }
//        });
//        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                isSelectAll = false;
//                isValue = false;
//                isTotalValue = false;
//                isCategory = false;
//                isOperation = false;
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
    }

//    void showStatistic(boolean isValue, boolean isTotalValue, boolean isCategory, boolean isOperation){
//        if (isValue && isTotalValue && isCategory && isOperation) {
//            sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
//            String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
//            if (curAc.equals("Счёт 1")) {
//                database = dbHelper.getWritableDatabase();
//                String getAll = "SELECT " + InputData.TaskEntry.VALUE + ", " + InputData.TaskEntry.TOTAL_VALUE + ", " + InputData.TaskEntry.CATEGORY + ", " + InputData.TaskEntry.OPERATION + " FROM " + InputData.TaskEntry.TABLE + " WHERE " + InputData.TaskEntry.DATE + " = '" + currentDate.get(0) + "';";
//                Cursor cursor = database.rawQuery(getAll, null);
//                if (!(cursor.getCount() <= 0)){
//                    if (cursor.moveToFirst()){
//                        do {
//                            String oi = cursor.getString(2);
//                            if (cursor.getString(2).equals("")){
//                                oi = "Отсутствует";
//                            }
//                            ss += "" + cursor.getString(0) + ", " + cursor.getString(1) + ", " + oi + ", " + cursor.getString(3) + "/ ";
//                        } while (cursor.moveToNext());
//                    }
//                }
//                cursor.close();
//                database.close();
//                textView.setText(ss);
//            }
//        }
//    }
//    private void showStat() {
//        String f = "accFile";
//        sharedPreferences = getSharedPreferences(f, Context.MODE_PRIVATE);
//        String curAc = sharedPreferences.getString("acc", "Счёт 1");
//        if (curAc.equals("Счёт 1")) {
//            database = dbHelper.getWritableDatabase();
//            String income = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165295';";
//            Cursor c = database.rawQuery(income, null);
//            int in = 0;
//            int out = 0;
//            String ssss = "0";
//            if (!(c.getCount() <= 0)) {
//                if (c.moveToFirst()) {
//                    do {
//                        totalIncome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                String outcome = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165294';";
//                c = database.rawQuery(outcome, null);
//                if (c.moveToFirst()) {
//                    do {
//                        totalOutcome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                c.close();
//                database.close();
//
//                for (int i = 0; i < totalIncome.size(); i++) {
//                    in += Integer.parseInt(totalIncome.get(i));
//                }
//                for (int i = 0; i < totalOutcome.size(); i++) {
//                    out += Integer.parseInt(totalOutcome.get(i));
//                }
//                double percent = (double) out / in * 100;
//                String ss = "" + percent;
//                char[] sss = ss.toCharArray();
//                if (percent < 100) {
//                    if (sss.length > 4) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else if (percent >= 100 && percent < 1000) {
//                    if (sss.length > 5) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4] + sss[5];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else {
//                    ssss = "";
//                    for (char value : sss) {
//                        ssss += value;
//                    }
//                }
//            }
//            String file = "myFile";
//            sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
//            String cc = sharedPreferences.getString("currency", "rub");
//            String c1 = "";
//            if (cc.equals("rub")) {
//                c1 = getString(R.string.ruble);
//            } else if (cc.equals("euro")) {
//                c1 = getString(R.string.euro);
//            } else if (cc.equals("dollar")) {
//                c1 = getString(R.string.dollar);
//            }
//
//
//            totInc.setText("Общий доход: " + in + c1);
//            totOutc.setText("Общий расход: " + out + c1);
//            perc.setText("Процент расхода от дохода: " + ssss + "%");
//        } else if (curAc.equals("Счёт 2")){
//            database = dbHelper1.getWritableDatabase();
//            String income = "SELECT value1 FROM " + InputData1.TaskEntry1.TABLE1 + " WHERE operation1 = '2131165295';";
//            Cursor c = database.rawQuery(income, null);
//            int in = 0;
//            int out = 0;
//            String ssss = "0";
//            if (!(c.getCount() <= 0)) {
//                if (c.moveToFirst()) {
//                    do {
//                        totalIncome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                String outcome = "SELECT value1 FROM " + InputData1.TaskEntry1.TABLE1 + " WHERE operation1 = '2131165294';";
//                c = database.rawQuery(outcome, null);
//                if (c.moveToFirst()) {
//                    do {
//                        totalOutcome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                c.close();
//                database.close();
//
//                for (int i = 0; i < totalIncome.size(); i++) {
//                    in += Integer.parseInt(totalIncome.get(i));
//                }
//                for (int i = 0; i < totalOutcome.size(); i++) {
//                    out += Integer.parseInt(totalOutcome.get(i));
//                }
//                double percent = (double) out / in * 100;
//                String ss = "" + percent;
//                char[] sss = ss.toCharArray();
//                if (percent < 100) {
//                    if (sss.length > 4) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else if (percent >= 100 && percent < 1000) {
//                    if (sss.length > 5) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4] + sss[5];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else {
//                    ssss = "";
//                    for (char value : sss) {
//                        ssss += value;
//                    }
//                }
//            }
//            String file = "myFile1";
//            sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
//            String cc = sharedPreferences.getString("currency", "rub");
//            String c1 = "";
//            if (cc.equals("rub")) {
//                c1 = getString(R.string.ruble);
//            } else if (cc.equals("euro")) {
//                c1 = getString(R.string.euro);
//            } else if (cc.equals("dollar")) {
//                c1 = getString(R.string.dollar);
//            }
//
//
//            totInc.setText("Общий доход: " + in + c1);
//            totOutc.setText("Общий расход: " + out + c1);
//            perc.setText("Процент расхода от дохода: " + ssss + "%");
//        } else if (curAc.equals("Счёт 3")){
//            database = dbHelper2.getWritableDatabase();
//            String income = "SELECT value2 FROM " + InputData2.TaskEntry2.TABLE2 + " WHERE operation2 = '2131165295';";
//            Cursor c = database.rawQuery(income, null);
//            int in = 0;
//            int out = 0;
//            String ssss = "0";
//            if (!(c.getCount() <= 0)) {
//                if (c.moveToFirst()) {
//                    do {
//                        totalIncome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                String outcome = "SELECT value2 FROM " + InputData2.TaskEntry2.TABLE2 + " WHERE operation2 = '2131165294';";
//                c = database.rawQuery(outcome, null);
//                if (c.moveToFirst()) {
//                    do {
//                        totalOutcome.add(c.getString(0));
//                    } while (c.moveToNext());
//                }
//                c.close();
//                database.close();
//
//                for (int i = 0; i < totalIncome.size(); i++) {
//                    in += Integer.parseInt(totalIncome.get(i));
//                }
//                for (int i = 0; i < totalOutcome.size(); i++) {
//                    out += Integer.parseInt(totalOutcome.get(i));
//                }
//                double percent = (double) out / in * 100;
//                String ss = "" + percent;
//                char[] sss = ss.toCharArray();
//                if (percent < 100) {
//                    if (sss.length > 4) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else if (percent >= 100 && percent < 1000) {
//                    if (sss.length > 5) {
//                        ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4] + sss[5];
//                    } else {
//                        ssss = "";
//                        for (char value : sss) {
//                            ssss += value;
//                        }
//                    }
//                } else {
//                    ssss = "";
//                    for (char value : sss) {
//                        ssss += value;
//                    }
//                }
//            }
//            String file = "myFile2";
//            sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
//            String cc = sharedPreferences.getString("currency", "rub");
//            String c1 = "";
//            if (cc.equals("rub")) {
//                c1 = getString(R.string.ruble);
//            } else if (cc.equals("euro")) {
//                c1 = getString(R.string.euro);
//            } else if (cc.equals("dollar")) {
//                c1 = getString(R.string.dollar);
//            }
//
//
//            totInc.setText("Общий доход: " + in + c1);
//            totOutc.setText("Общий расход: " + out + c1);
//            perc.setText("Процент расхода от дохода: " + ssss + "%");
//        }
//    }
}