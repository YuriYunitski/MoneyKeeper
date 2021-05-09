package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextClock;
import android.widget.TextView;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Statistic extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    DBHelper dbHelper;
    DBHelper1 dbHelper1;
    DBHelper2 dbHelper2;
//    CalendarView calendarView;
//    TextView date;
    TextView textView;

    String ss;
    boolean isSelectAll, isCategory, isValue, isTotalValue, isOperation;
    Button param;
    public static ArrayList<String> currentDate = new ArrayList<>();
    SharedPreferences sharedPreferences;
//
//    String s1, s2, s3, s4;
//
//    TextView totInc, totOutc, perc;
    SQLiteDatabase database;
//    ArrayList<String> totalIncome, totalOutcome, operation, val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        toolbar = findViewById(R.id.toolBar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Статистика");
//        totInc = findViewById(R.id.tv_inc);
//        totOutc = findViewById(R.id.tv_outc);
//        perc = findViewById(R.id.tv_perc);
        param = findViewById(R.id.param);
        param.setOnClickListener(this);
        ss = "";
        isSelectAll = false;
        isValue = false;
        isTotalValue = false;
        isCategory = false;
        isOperation = false;
        currentDate.add(dateC());
        textView = findViewById(R.id.textText);
//        s1 = "";
//        s2 = "";
//        s3 = "";
//        s4 = "";
//        totalIncome = new ArrayList<>();
//        totalOutcome = new ArrayList<>();
//        operation = new ArrayList<>();
//        val = new ArrayList<>();
        dbHelper = new DBHelper(this);
        dbHelper1 = new DBHelper1(this);
        dbHelper2 = new DBHelper2(this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Параметры");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.param_list, null);
        builder.setView(view);
        CheckBox cbSelectAll = view.findViewById(R.id.cb_sel_all);
        CheckBox cbOpValue = view.findViewById(R.id.cb_op_val);
        CheckBox cbBalance = view.findViewById(R.id.cb_bal);
        CheckBox cbCategory = view.findViewById(R.id.cb_cat);
        CheckBox cbOperation = view.findViewById(R.id.cb_op);
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectAll) {
                    isSelectAll = true;
                    isValue = true;
                    isTotalValue = true;
                    isCategory = true;
                    isOperation = true;
                } else {
                    isSelectAll = false;
                    isValue = false;
                    isTotalValue = false;
                    isCategory = false;
                    isOperation = false;
                }
                cbOpValue.setChecked(isValue);
                cbBalance.setChecked(isTotalValue);
                cbCategory.setChecked(isCategory);
                cbOperation.setChecked(isOperation);
            }
        });
        cbOpValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValue){
                    isValue = true;
                } else {
                    isValue = false;
                }
                cbOpValue.setChecked(isValue);
            }
        });
        cbBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTotalValue){
                    isTotalValue = true;
                } else {
                    isTotalValue = false;
                }
                cbBalance.setChecked(isValue);
            }
        });
        cbCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isCategory){
                    isCategory = true;
                } else {
                    isCategory = false;
                }
                cbCategory.setChecked(isCategory);
            }
        });
        cbOperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isOperation){
                    isOperation = true;
                } else {
                    isOperation = false;
                }
                cbOperation.setChecked(isOperation);
            }
        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                showStatistic(isValue, isTotalValue, isCategory, isOperation);
                isSelectAll = false;
                isValue = false;
                isTotalValue = false;
                isCategory = false;
                isOperation = false;

            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSelectAll = false;
                isValue = false;
                isTotalValue = false;
                isCategory = false;
                isOperation = false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
    }

    void showStatistic(boolean isValue, boolean isTotalValue, boolean isCategory, boolean isOperation){
        if (isValue && isTotalValue && isCategory && isOperation) {
            sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
            String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
            if (curAc.equals("Счёт 1")) {
                database = dbHelper.getWritableDatabase();
                String getAll = "SELECT " + InputData.TaskEntry.VALUE + ", " + InputData.TaskEntry.TOTAL_VALUE + ", " + InputData.TaskEntry.CATEGORY + ", " + InputData.TaskEntry.OPERATION + " FROM " + InputData.TaskEntry.TABLE + " WHERE " + InputData.TaskEntry.DATE + " = '" + currentDate.get(0) + "';";
                Cursor cursor = database.rawQuery(getAll, null);
                if (!(cursor.getCount() <= 0)){
                    if (cursor.moveToFirst()){
                        do {
                            String oi = cursor.getString(2);
                            if (cursor.getString(2).equals("")){
                                oi = "Отсутствует";
                            }
                            ss += "" + cursor.getString(0) + ", " + cursor.getString(1) + ", " + oi + ", " + cursor.getString(3) + "/ ";
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
                database.close();
                textView.setText(ss);
            }
        }
    }
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