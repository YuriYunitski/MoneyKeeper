package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Statistic extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    DBHelper dbHelper;
    DBHelper1 dbHelper1;
    DBHelper2 dbHelper2;
    TextView tvChoosenDate;
    TextView tvTotalIncome;
    TextView tvTotalOutcomr;
    TextView tvDelPercent;
    TextView tvOutCategSum;
    TextView tvInCategSum;
    SharedPreferences sharedPreferences;
    SQLiteDatabase database;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    String[] times = new String[]{"День", "Неделя", "Месяц", "Год"};
    Button chooseBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        toolbar = findViewById(R.id.toolBar4);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Статистика");
        dbHelper = new DBHelper(this);
        dbHelper1 = new DBHelper1(this);
        dbHelper2 = new DBHelper2(this);
        tvChoosenDate = findViewById(R.id.tv_current_date);
        tvChoosenDate.setText(""+dateC());
        tvTotalIncome = findViewById(R.id.tv_total_income);
        tvTotalOutcomr = findViewById(R.id.tv_total_outcome);
        tvDelPercent = findViewById(R.id.tv_del_percent);
        tvInCategSum = findViewById(R.id.tv_inc_categ_val_sum);
        tvOutCategSum = findViewById(R.id.tv_outc_categ_val_sum);
        chooseBtn = findViewById(R.id.button);
        chooseBtn.setOnClickListener(this);
        spinner = findViewById(R.id.time_spinner);
        spinnerAdapter = new ArrayAdapter<>(this, R.layout.spinner_list_statistic, R.id.spin_text_stat, times);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        getDayStatistic();
                        break;
                    case 1:
                        getWeekStatistic();
                        break;
                    case 2:
                        getMonthStatistic();
                        break;
                    case 3:
                        getYearStatistic();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
    void getDayStatistic(){
        sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                getDayStatisticMethod(dbHelper, InputData.TaskEntry.VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION, InputData.TaskEntry.CATEGORY, InputData.TaskEntry.TABLE, MainActivity.ACCOUNT_ONE_FILE);
                break;
            }
            case "Счёт 2": {
                getDayStatisticMethod(dbHelper1, InputData1.TaskEntry1.VALUE1, InputData1.TaskEntry1.DATE1, InputData1.TaskEntry1.OPERATION1, InputData1.TaskEntry1.CATEGORY1, InputData1.TaskEntry1.TABLE1, MainActivity.ACCOUNT_TWO_FILE);
                break;
            }
            case "Счёт 3": {
                getDayStatisticMethod(dbHelper2, InputData2.TaskEntry2.VALUE2, InputData2.TaskEntry2.DATE2, InputData2.TaskEntry2.OPERATION2, InputData2.TaskEntry2.CATEGORY2, InputData2.TaskEntry2.TABLE2, MainActivity.ACCOUNT_THREE_FILE);
                break;
            }
        }
    }
    void getWeekStatistic(){

        sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                getWeekStatisticMethod(dbHelper, InputData.TaskEntry.VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION, InputData.TaskEntry.CATEGORY, InputData.TaskEntry.TABLE, MainActivity.ACCOUNT_ONE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 2": {
                getWeekStatisticMethod(dbHelper1, InputData1.TaskEntry1.VALUE1, InputData1.TaskEntry1.DATE1, InputData1.TaskEntry1.OPERATION1, InputData1.TaskEntry1.CATEGORY1, InputData1.TaskEntry1.TABLE1, MainActivity.ACCOUNT_TWO_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 3": {
                getWeekStatisticMethod(dbHelper2, InputData2.TaskEntry2.VALUE2, InputData2.TaskEntry2.DATE2, InputData2.TaskEntry2.OPERATION2, InputData2.TaskEntry2.CATEGORY2, InputData2.TaskEntry2.TABLE2, MainActivity.ACCOUNT_THREE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
        }

    }
    void getMonthStatistic(){
        sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                getMonthStatisticMethod(dbHelper, InputData.TaskEntry.VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION, InputData.TaskEntry.CATEGORY, InputData.TaskEntry.TABLE, MainActivity.ACCOUNT_ONE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 2": {
                getMonthStatisticMethod(dbHelper1, InputData1.TaskEntry1.VALUE1, InputData1.TaskEntry1.DATE1, InputData1.TaskEntry1.OPERATION1, InputData1.TaskEntry1.CATEGORY1, InputData1.TaskEntry1.TABLE1, MainActivity.ACCOUNT_TWO_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 3": {
                getMonthStatisticMethod(dbHelper2, InputData2.TaskEntry2.VALUE2, InputData2.TaskEntry2.DATE2, InputData2.TaskEntry2.OPERATION2, InputData2.TaskEntry2.CATEGORY2, InputData2.TaskEntry2.TABLE2, MainActivity.ACCOUNT_THREE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
        }
    }

    void getYearStatistic() {
        sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                getYearStatisticMethod(dbHelper, InputData.TaskEntry.VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION, InputData.TaskEntry.CATEGORY, InputData.TaskEntry.TABLE, MainActivity.ACCOUNT_ONE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 2": {
                getYearStatisticMethod(dbHelper1, InputData1.TaskEntry1.VALUE1, InputData1.TaskEntry1.DATE1, InputData1.TaskEntry1.OPERATION1, InputData1.TaskEntry1.CATEGORY1, InputData1.TaskEntry1.TABLE1, MainActivity.ACCOUNT_TWO_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
            case "Счёт 3": {
                getYearStatisticMethod(dbHelper2, InputData2.TaskEntry2.VALUE2, InputData2.TaskEntry2.DATE2, InputData2.TaskEntry2.OPERATION2, InputData2.TaskEntry2.CATEGORY2, InputData2.TaskEntry2.TABLE2, MainActivity.ACCOUNT_THREE_FILE, MainActivity.CURRENCY_KEY);
                break;
            }
        }
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
        builder.setTitle("Дата");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.statistic_lay, null);
        builder.setView(view);
        CalendarView calendar = view.findViewById(R.id.calendar);
        TextView tvChooseCalendar = view.findViewById(R.id.tv_calendar_choose);
        tvChooseCalendar.setText("" + dateC());
        calendar.setOnDateChangeListener((view1, year, month, dayOfMonth) -> {
            String date = dayOfMonth + "." + (month + 1) + "." + year;
            tvChooseCalendar.setText(date);
        });

        builder.setPositiveButton("ok", (dialog, which) -> {
            tvChoosenDate.setText("" + tvChooseCalendar.getText().toString());
            spinnerAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_list_statistic, R.id.spin_text_stat, times);
            spinner.setAdapter(spinnerAdapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view12, int position, long id) {
                    switch (position) {
                        case 0:
                            getDayStatistic();
                            break;
                        case 1:
                            getWeekStatistic();
                            break;
                        case 2:
                            getMonthStatistic();
                            break;
                        case 3:
                            getYearStatistic();
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

        });

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));

    }

    String isLast(ArrayList<String> list, int pos){
        String s = "";
        if (pos + 1 < list.size())
            s = "\n" + "\n";
        return s;
    }
    void getDayStatisticMethod(SQLiteOpenHelper dbHelperM, String dbValue, String dbDate, String dbOperation, String dbCategory, String dbTable, String accountFile) {
        String currentDate = tvChoosenDate.getText().toString();
        String[] currentDateSplit = currentDate.split("\\.");
        String currentDay = currentDateSplit[0];
        String currentYear = currentDateSplit[2];
        String currentMonth = currentDateSplit[1];
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        ArrayList<String> operationList = new ArrayList<>();
        ArrayList<String> categoryList = new ArrayList<>();
        ArrayList<String> daysInBase = new ArrayList<>();
        ArrayList<String> yearsInBase = new ArrayList<>();
        ArrayList<String> monthsInBase = new ArrayList<>();
        database = dbHelperM.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + dbValue + ", " + dbDate + ", " + dbOperation + ", " + dbCategory + " FROM " + dbTable + ";", null);
        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                do {
                    dateList.add(cursor.getString(1));
                    valueList.add(cursor.getString(0));
                    operationList.add(cursor.getString(2));
                    categoryList.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            daysInBase.add(currentDateToSplit[0]);
        }
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            yearsInBase.add(currentDateToSplit[2]);
        }
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            monthsInBase.add(currentDateToSplit[1]);
        }
        ArrayList<String> incomeValues = new ArrayList<>();
        ArrayList<String> incomeCategories = new ArrayList<>();
        ArrayList<String> outcomeValues = new ArrayList<>();
        ArrayList<String> outcomeCategories = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            if (operationList.get(i).equals("2131165295") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth) && daysInBase.get(i).equals(currentDay)) {
                incomeValues.add(valueList.get(i));
                incomeCategories.add(categoryList.get(i));
            } else if (operationList.get(i).equals("2131165294") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth) && daysInBase.get(i).equals(currentDay)) {
                outcomeValues.add(valueList.get(i));
                outcomeCategories.add(categoryList.get(i));
            }
        }
        int incSum = 0;
        int outcSum = 0;
        if (incomeValues.size() != 0) {
            for (int i = 0; i < incomeValues.size(); i++) {
                incSum += Integer.parseInt(incomeValues.get(i));
            }
        }
        if (outcomeValues.size() != 0){
            for (int i = 0; i < outcomeValues.size(); i++) {
                outcSum += Integer.parseInt(outcomeValues.get(i));
            }
        }
        double percent = 0.0;
        if (incSum != 0) {
            percent = (double) outcSum / incSum * 100.0;
        }
        Set<String> outcomeCatSet = new HashSet<>(outcomeCategories);
        ArrayList<String> outcomeCatSetToArray = new ArrayList<>(outcomeCatSet);
        Set<String> incomeCatSet = new HashSet<>(incomeCategories);
        ArrayList<String> incomeCatSetToArray = new ArrayList<>(incomeCatSet);

        ArrayList<Integer> incCatVal = new ArrayList<>();
        ArrayList<Integer> outcCatVal = new ArrayList<>();

        String allInCatValueSum = "";
        String allOutCatValueSum = "";

        for (int i = 0; i < incomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < incomeCategories.size(); k++) {
                if (incomeCatSetToArray.get(i).equals(incomeCategories.get(k))) {
                    c += Integer.parseInt(incomeValues.get(k));
                }
            }
            incCatVal.add(c);
        }

        for (int i = 0; i < outcomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < outcomeCategories.size(); k++) {
                if (outcomeCatSetToArray.get(i).equals(outcomeCategories.get(k))) {
                    c += Integer.parseInt(outcomeValues.get(k));
                }
            }
            outcCatVal.add(c);
        }
        sharedPreferences = getSharedPreferences(accountFile, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString(MainActivity.CURRENCY_KEY, "rub");
        String c1 = "";
        switch (cc) {
            case "rub":
                c1 = getString(R.string.ruble);
                break;
            case "euro":
                c1 = getString(R.string.euro);
                break;
            case "dollar":
                c1 = getString(R.string.dollar);
                break;
        }
        for (int i = 0; i < incomeCatSetToArray.size(); i++){
            allInCatValueSum += incomeCatSetToArray.get(i) + " " + incCatVal.get(i) + c1 + isLast(incomeCatSetToArray, i);
        }
        for (int i = 0; i < outcomeCatSetToArray.size(); i++){
            allOutCatValueSum += outcomeCatSetToArray.get(i) + " " + outcCatVal.get(i) + c1 + isLast(outcomeCatSetToArray, i);
        }
        tvTotalIncome.setText("" + incSum + c1);
        tvTotalOutcomr.setText("" + outcSum + c1);
        tvDelPercent.setText("" + String.format("%.2f", percent) + "%");
        tvInCategSum.setText("" + allInCatValueSum);
        tvOutCategSum.setText("" + allOutCatValueSum);
    }


    void getWeekStatisticMethod(SQLiteOpenHelper dbHelperM, String dbValue, String dbDate, String dbOperation, String dbCategory, String dbTable, String accountFile, String currencyKey){
        String currentDate = tvChoosenDate.getText().toString();
        //сплитуем чтобы получить отдельно день месяц и год
        //по этим значением из бд будем подбирать соответствующие элементы таблицы
        String[] currentDateSplit = currentDate.split("\\.");
        String currentYear = currentDateSplit[2];
        String currentMonth = currentDateSplit[1];
        Calendar calendar = Calendar.getInstance();
        //месяцы в calendar начинаются с 0, чтобы неделя определялась верно, надо отнять 1 от текущего месяца
        calendar.set(Integer.parseInt(currentDateSplit[2]), Integer.parseInt(currentDateSplit[1]) - 1, Integer.parseInt(currentDateSplit[0]));
        calendar.setMinimalDaysInFirstWeek(1);
        int wk = calendar.get(Calendar.WEEK_OF_MONTH);
        ArrayList<String> dateList = new ArrayList<>(); //список всех дат
        ArrayList<String> valueList = new ArrayList<>(); //список всех значений
        ArrayList<String> operationList = new ArrayList<>(); //список всех операция, т.е. расход и доход
        ArrayList<String> categoryList = new ArrayList<>(); //список всех категорий
        ArrayList<String> yearsInBase = new ArrayList<>();
        ArrayList<String> monthsInBase = new ArrayList<>();
        ArrayList<Integer> weeksInBase = new ArrayList<>();
        database = dbHelperM.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + dbValue + ", " + dbDate + ", " + dbOperation + ", " + dbCategory + " FROM " + dbTable + ";", null);
        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                do {
                    dateList.add(cursor.getString(1));
                    valueList.add(cursor.getString(0));
                    operationList.add(cursor.getString(2));
                    categoryList.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            yearsInBase.add(currentDateToSplit[2]);
        }
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            monthsInBase.add(currentDateToSplit[1]);
        }
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Integer.parseInt(currentDateToSplit[2]), Integer.parseInt(currentDateToSplit[1]) - 1, Integer.parseInt(currentDateToSplit[0]));
            calendar1.setMinimalDaysInFirstWeek(1);
            int wk1 = calendar1.get(Calendar.WEEK_OF_MONTH);
            weeksInBase.add(wk1);
        }
        ArrayList<String> incomeValues = new ArrayList<>(); //значения доходов
        ArrayList<String> incomeCategories = new ArrayList<>(); //категории доходов
        ArrayList<String> outcomeValues = new ArrayList<>(); // значения расходов
        ArrayList<String> outcomeCategories = new ArrayList<>(); //категории расходов
        for (int i = 0; i < dateList.size(); i++) {
            //условие для того, чтобы из всех списков отобрать только нужные значения, разделенные по суммам и категориям
            if (operationList.get(i).equals("2131165295") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth) && weeksInBase.get(i).equals(wk)) {
                incomeValues.add(valueList.get(i));
                incomeCategories.add(categoryList.get(i));
            } else if (operationList.get(i).equals("2131165294") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth) && weeksInBase.get(i).equals(wk)) {
                outcomeValues.add(valueList.get(i));
                outcomeCategories.add(categoryList.get(i));
            }
        }
        int incSum = 0;
        int outcSum = 0;
        //проверка на ноль и nan
        if (incomeValues.size() != 0) {
            for (int i = 0; i < incomeValues.size(); i++) {
                incSum += Integer.parseInt(incomeValues.get(i));
            }
        }
        if (outcomeValues.size() != 0){
            for (int i = 0; i < outcomeValues.size(); i++) {
                outcSum += Integer.parseInt(outcomeValues.get(i));
            }
        }
        double percent = 0.0;
        if (incSum != 0) {
            percent = (double) outcSum / incSum * 100.0;
        }
        Set<String> outcomeCatSet = new HashSet<>(outcomeCategories); //убрать все дубли
        ArrayList<String> outcomeCatSetToArray = new ArrayList<>(outcomeCatSet);
        Set<String> incomeCatSet = new HashSet<>(incomeCategories);
        ArrayList<String> incomeCatSetToArray = new ArrayList<>(incomeCatSet);
        ArrayList<Integer> incCatVal = new ArrayList<>();
        ArrayList<Integer> outcCatVal = new ArrayList<>();
        String allInCatValueSum = "";
        String allOutCatValueSum = "";
        //двойной цикл для нахлждения количества повторяющихся категорий
        //чтобы в дальнейшем рассчитать процент
        for (int i = 0; i < incomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < incomeCategories.size(); k++) {
                if (incomeCatSetToArray.get(i).equals(incomeCategories.get(k))) {
                    c += Integer.parseInt(incomeValues.get(k));
                }
            }
            incCatVal.add(c);
        }
        for (int i = 0; i < outcomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < outcomeCategories.size(); k++) {
                if (outcomeCatSetToArray.get(i).equals(outcomeCategories.get(k))) {
                    c += Integer.parseInt(outcomeValues.get(k));
                }
            }
            outcCatVal.add(c);
        }
        sharedPreferences = getSharedPreferences(accountFile, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString(currencyKey, "rub");
        String c1 = "";
        switch (cc) {
            case "rub":
                c1 = getString(R.string.ruble);
                break;
            case "euro":
                c1 = getString(R.string.euro);
                break;
            case "dollar":
                c1 = getString(R.string.dollar);
                break;
        }
        for (int i = 0; i < incomeCatSetToArray.size(); i++){
            allInCatValueSum += incomeCatSetToArray.get(i) + " " + incCatVal.get(i) + c1 + isLast(incomeCatSetToArray, i);
        }
        for (int i = 0; i < outcomeCatSetToArray.size(); i++){
            allOutCatValueSum += outcomeCatSetToArray.get(i) + " " + outcCatVal.get(i) + c1 + isLast(outcomeCatSetToArray, i);
        }
        tvTotalIncome.setText("" + incSum + c1);
        tvTotalOutcomr.setText("" + outcSum + c1);
        tvDelPercent.setText("" + String.format("%.2f", percent) + "%");
        tvInCategSum.setText("" + allInCatValueSum);
        tvOutCategSum.setText("" + allOutCatValueSum);
    }

    void getMonthStatisticMethod(SQLiteOpenHelper dbHelperM, String dbValue, String dbDate, String dbOperation, String dbCategory, String dbTable, String accountFile, String currencyKey){
        String currentDate = tvChoosenDate.getText().toString();
        String[] currentDateSplit = currentDate.split("\\.");
        String currentYear = currentDateSplit[2];
        String currentMonth = currentDateSplit[1];
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        ArrayList<String> operationList = new ArrayList<>();
        ArrayList<String> categoryList = new ArrayList<>();
        ArrayList<String> yearsInBase = new ArrayList<>();
        ArrayList<String> monthsInBase = new ArrayList<>();
        database = dbHelperM.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + dbValue + ", " + dbDate + ", " + dbOperation + ", " + dbCategory + " FROM " + dbTable + ";", null);
        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                do {
                    dateList.add(cursor.getString(1));
                    valueList.add(cursor.getString(0));
                    operationList.add(cursor.getString(2));
                    categoryList.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            yearsInBase.add(currentDateToSplit[2]);
        }
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            monthsInBase.add(currentDateToSplit[1]);
        }
        ArrayList<String> incomeValues = new ArrayList<>();
        ArrayList<String> incomeCategories = new ArrayList<>();
        ArrayList<String> outcomeValues = new ArrayList<>();
        ArrayList<String> outcomeCategories = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            if (operationList.get(i).equals("2131165295") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth)) {
                incomeValues.add(valueList.get(i));
                incomeCategories.add(categoryList.get(i));
            } else if (operationList.get(i).equals("2131165294") && yearsInBase.get(i).equals(currentYear) && monthsInBase.get(i).equals(currentMonth)) {
                outcomeValues.add(valueList.get(i));
                outcomeCategories.add(categoryList.get(i));
            }
        }
        int incSum = 0;
        int outcSum = 0;
        if (incomeValues.size() != 0) {
            for (int i = 0; i < incomeValues.size(); i++) {
                incSum += Integer.parseInt(incomeValues.get(i));
            }
        }
        if (outcomeValues.size() != 0){
            for (int i = 0; i < outcomeValues.size(); i++) {
                outcSum += Integer.parseInt(outcomeValues.get(i));
            }
        }
        double percent = 0.0;
        if (incSum != 0) {
            percent = (double) outcSum / incSum * 100.0;
        }
        Set<String> outcomeCatSet = new HashSet<>(outcomeCategories);
        ArrayList<String> outcomeCatSetToArray = new ArrayList<>(outcomeCatSet);
        Set<String> incomeCatSet = new HashSet<>(incomeCategories);
        ArrayList<String> incomeCatSetToArray = new ArrayList<>(incomeCatSet);
        ArrayList<Integer> incCatVal = new ArrayList<>();
        ArrayList<Integer> outcCatVal = new ArrayList<>();
        String allInCatValueSum = "";
        String allOutCatValueSum = "";
        //двойной цикл для нахлждения количества повторяющихся категорий
        //чтобы в дальнейшем рассчитать процент
        for (int i = 0; i < incomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < incomeCategories.size(); k++) {
                if (incomeCatSetToArray.get(i).equals(incomeCategories.get(k))) {
                    c += Integer.parseInt(incomeValues.get(k));
                }
            }
            incCatVal.add(c);
        }
        for (int i = 0; i < outcomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < outcomeCategories.size(); k++) {
                if (outcomeCatSetToArray.get(i).equals(outcomeCategories.get(k))) {
                    c += Integer.parseInt(outcomeValues.get(k));
                }
            }
            outcCatVal.add(c);
        }
        sharedPreferences = getSharedPreferences(accountFile, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString(currencyKey, "rub");
        String c1 = "";
        switch (cc) {
            case "rub":
                c1 = getString(R.string.ruble);
                break;
            case "euro":
                c1 = getString(R.string.euro);
                break;
            case "dollar":
                c1 = getString(R.string.dollar);
                break;
        }
        for (int i = 0; i < incomeCatSetToArray.size(); i++){
            allInCatValueSum += incomeCatSetToArray.get(i) + " " + incCatVal.get(i) + c1 + isLast(incomeCatSetToArray, i);
        }
        for (int i = 0; i < outcomeCatSetToArray.size(); i++){
            allOutCatValueSum += outcomeCatSetToArray.get(i) + " " + outcCatVal.get(i) + c1 + isLast(outcomeCatSetToArray, i);
        }
        tvTotalIncome.setText("" + incSum + c1);
        tvTotalOutcomr.setText("" + outcSum + c1);
        tvDelPercent.setText("" + String.format("%.2f", percent) + "%");
        tvInCategSum.setText("" + allInCatValueSum);
        tvOutCategSum.setText("" + allOutCatValueSum);

    }
    void getYearStatisticMethod(SQLiteOpenHelper dbHelperM, String dbValue, String dbDate, String dbOperation, String dbCategory, String dbTable, String accountFile, String currencyKey){
        String currentDate = tvChoosenDate.getText().toString();
        String[] currentDateSplit = currentDate.split("\\.");
        String currentYear = currentDateSplit[2];
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<String> valueList = new ArrayList<>();
        ArrayList<String> operationList = new ArrayList<>();
        ArrayList<String> categoryList = new ArrayList<>();
        ArrayList<String> yearsInBase = new ArrayList<>();
        database = dbHelperM.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT " + dbValue + ", " + dbDate + ", " + dbOperation + ", " + dbCategory + " FROM " + dbTable + ";", null);
        if (!(cursor.getCount() <= 0)) {
            if (cursor.moveToFirst()) {
                do {
                    dateList.add(cursor.getString(1));
                    valueList.add(cursor.getString(0));
                    operationList.add(cursor.getString(2));
                    categoryList.add(cursor.getString(3));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        database.close();
        for (int i = 0; i < dateList.size(); i++) {
            String[] currentDateToSplit = dateList.get(i).split("\\.");
            yearsInBase.add(currentDateToSplit[2]);
        }
        ArrayList<String> incomeValues = new ArrayList<>();
        ArrayList<String> incomeCategories = new ArrayList<>();
        ArrayList<String> outcomeValues = new ArrayList<>();
        ArrayList<String> outcomeCategories = new ArrayList<>();
        for (int i = 0; i < dateList.size(); i++) {
            if (operationList.get(i).equals("2131165295") && yearsInBase.get(i).equals(currentYear)) {
                incomeValues.add(valueList.get(i));
                incomeCategories.add(categoryList.get(i));
            } else if (operationList.get(i).equals("2131165294") && yearsInBase.get(i).equals(currentYear)) {
                outcomeValues.add(valueList.get(i));
                outcomeCategories.add(categoryList.get(i));
            }
        }
        int incSum = 0;
        int outcSum = 0;
        if (incomeValues.size() != 0) {
            for (int i = 0; i < incomeValues.size(); i++) {
                incSum += Integer.parseInt(incomeValues.get(i));
            }
        }
        if (outcomeValues.size() != 0){
            for (int i = 0; i < outcomeValues.size(); i++) {
                outcSum += Integer.parseInt(outcomeValues.get(i));
            }
        }
        double percent = 0.0;
        if (incSum != 0) {
            percent = (double) outcSum / incSum * 100.0;
        }
        Set<String> outcomeCatSet = new HashSet<>(outcomeCategories);
        ArrayList<String> outcomeCatSetToArray = new ArrayList<>(outcomeCatSet);
        Set<String> incomeCatSet = new HashSet<>(incomeCategories);
        ArrayList<String> incomeCatSetToArray = new ArrayList<>(incomeCatSet);
        ArrayList<Integer> incCatVal = new ArrayList<>();
        ArrayList<Integer> outcCatVal = new ArrayList<>();
        String allInCatValueSum = "";
        String allOutCatValueSum = "";
        for (int i = 0; i < incomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < incomeCategories.size(); k++) {
                if (incomeCatSetToArray.get(i).equals(incomeCategories.get(k))) {
                    c += Integer.parseInt(incomeValues.get(k));
                }
            }
            incCatVal.add(c);
        }
        for (int i = 0; i < outcomeCatSet.size(); i++) {
            int c = 0;
            for (int k = 0; k < outcomeCategories.size(); k++) {
                if (outcomeCatSetToArray.get(i).equals(outcomeCategories.get(k))) {
                    c += Integer.parseInt(outcomeValues.get(k));
                }
            }
            outcCatVal.add(c);
        }
        sharedPreferences = getSharedPreferences(accountFile, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString(currencyKey, "rub");
        String c1 = "";
        switch (cc) {
            case "rub":
                c1 = getString(R.string.ruble);
                break;
            case "euro":
                c1 = getString(R.string.euro);
                break;
            case "dollar":
                c1 = getString(R.string.dollar);
                break;
        }
        for (int i = 0; i < incomeCatSetToArray.size(); i++){
            allInCatValueSum += incomeCatSetToArray.get(i) + " " + incCatVal.get(i) + c1 + isLast(incomeCatSetToArray, i);
        }
        for (int i = 0; i < outcomeCatSetToArray.size(); i++){
            allOutCatValueSum += outcomeCatSetToArray.get(i) + " " + outcCatVal.get(i) + c1 + isLast(outcomeCatSetToArray, i);
        }
        tvTotalIncome.setText("" + incSum + c1);
        tvTotalOutcomr.setText("" + outcSum + c1);
        tvDelPercent.setText("" + String.format("%.2f", percent) + "%");
        tvInCategSum.setText("" + allInCatValueSum);
        tvOutCategSum.setText("" + allOutCatValueSum);
    }
}
