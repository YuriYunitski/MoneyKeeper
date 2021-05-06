package com.yunitski.bookkeeper.moneykeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class Statistic extends AppCompatActivity {

    Toolbar toolbar;
    DBHelper dbHelper;

    SharedPreferences sharedPreferences;

    String s1, s2, s3, s4;

    TextView totInc, totOutc, perc;
    SQLiteDatabase database;
    ArrayList<String> totalIncome, totalOutcome, operation, val;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        toolbar = findViewById(R.id.toolBar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Статистика");
        totInc = findViewById(R.id.tv_inc);
        totOutc = findViewById(R.id.tv_outc);
        perc = findViewById(R.id.tv_perc);
        s1 = "";
        s2 = "";
        s3 = "";
        s4 = "";
        totalIncome = new ArrayList<>();
        totalOutcome = new ArrayList<>();
        operation = new ArrayList<>();
        val = new ArrayList<>();
        dbHelper = new DBHelper(this);
        showStat();
    }
    private void showStat(){
        database = dbHelper.getWritableDatabase();
        String income = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165295';";
        Cursor c = database.rawQuery(income, null);
        int in = 0;
        int out = 0;
        String ssss = "0";
        if (!(c.getCount() <= 0)) {
            if (c.moveToFirst()) {
                do {
                    totalIncome.add(c.getString(0));
                } while (c.moveToNext());
            }
            String outcome = "SELECT value FROM " + InputData.TaskEntry.TABLE + " WHERE operation = '2131165294';";
            c = database.rawQuery(outcome, null);
            if (c.moveToFirst()) {
                do {
                    totalOutcome.add(c.getString(0));
                } while (c.moveToNext());
            }
            c.close();
            database.close();

            for (int i = 0; i < totalIncome.size(); i++) {
                in += Integer.parseInt(totalIncome.get(i));
            }
            for (int i = 0; i < totalOutcome.size(); i++) {
                out += Integer.parseInt(totalOutcome.get(i));
            }
            double percent = (double) out / in * 100;
            String ss = "" + percent;
            char[] sss = ss.toCharArray();
            if (percent < 100) {
                if (sss.length > 4) {
                    ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4];
                } else {
                    ssss = "";
                    for (char value : sss) {
                        ssss += value;
                    }
                }
            } else if (percent >= 100 && percent < 1000){
                if (sss.length > 5) {
                ssss = "" + sss[0] + sss[1] + sss[2] + sss[3] + sss[4] + sss[5];
            } else {
                ssss = "";
                    for (char value : sss) {
                        ssss += value;
                    }
            }
            } else {
                ssss = "";
                for (char value : sss) {
                    ssss += value;
                }
            }
        }
        String file = "myFile";
        sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString("currency", "rub");
        String c1 = "";
        if (cc.equals("rub")){
            c1 = getString(R.string.ruble);
        } else if (cc.equals("euro")){
            c1 = getString(R.string.euro);
        } else if (cc.equals("dollar")){
            c1 = getString(R.string.dollar);
        }


        totInc.setText("Общий доход: " + in + c1);
        totOutc.setText("Общий расход: " + out + c1);
        perc.setText("Процент расхода от дохода: " + ssss + "%");
    }
}