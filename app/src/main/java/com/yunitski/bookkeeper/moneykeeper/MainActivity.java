package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    Toolbar toolbar;

    RecyclerView recyclerView;
    TextView balance, currency, chosenLimit, chosenLimitValue, restType, restTypeValue;
    FloatingActionButton fab;
    ArrayList<Element> elements;
    static boolean outcome, income;
    public static final String ACCOUNT_FILE = "accFile";
    public static final String ACCOUNT_KEY = "acc";
    public static final String ACCOUNT_ONE_FILE = "accountOneFile";
    public static final String ACCOUNT_TWO_FILE = "accountTwoFile";
    public static final String ACCOUNT_THREE_FILE = "accountThreeFile";
    public static final String CURRENCY_KEY = "currency";
    EditText inpValueET, changeBalanceEditText;
    RadioButton radioButtonOut, radioButtonIn;
    RadioGroup radioGroup;
    DBHelper dbHelper;
    DBHelper1 dbHelper1;
    DBHelper2 dbHelper2;
    ArrayList<String> outList;
    ArrayList<String> inList;
    ArrayList<String> indexes;
    ArrayAdapter<String> spinnerOutAdapter;
    ArrayAdapter<String> spinnerInAdapter;
    ElementAdapter adapter;
    SharedPreferences sharedPreferences;
    Spinner spinner;
    ArrayAdapter<String> spinnerAdapter;
    private Thread secThread;
    private Runnable runnable;
    private String dollar, euro, belRub, pound, currentAccount, gryvan, tenge;
    int res;
    final String[] names = new String[] {"Счёт 1", "Счёт 2", "Счёт 3"};

    String spinIn;
    String spinOut;
    Document doc;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.events));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        recyclerView = findViewById(R.id.recycler_list);
        registerForContextMenu(recyclerView);
        balance = findViewById(R.id.balance);
        currency = findViewById(R.id.curremcy);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
        linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setOnClickListener(this);
        chosenLimit = findViewById(R.id.limit_type_choosen);
        chosenLimitValue = findViewById(R.id.limit_value_choosen);
        restType = findViewById(R.id.rest_type);
        restTypeValue = findViewById(R.id.rest_type_value);
        spinIn = "";
        spinOut = "";
        spinner = findViewById(R.id.spinner);
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.spinner_list_item_custom, R.id.text_spinner, names);
        spinner.setAdapter(spinnerAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position){
                    case 0:
                        currentAccount = names[0];
                        editor.putString(ACCOUNT_KEY, currentAccount);
                        editor.apply();
                        updateUI();
                        loadBalance();
                        updBalCur();
                        showLimit();
                        break;
                    case 1:
                        currentAccount = names[1];
                        editor.putString(ACCOUNT_KEY, currentAccount);
                        editor.apply();
                        updateUI();
                        loadBalance();
                        updBalCur();
                        showLimit();
                        break;
                    case 2:
                        currentAccount = names[2];
                        editor.putString(ACCOUNT_KEY, currentAccount);
                        editor.apply();
                        updateUI();
                        loadBalance();
                        updBalCur();
                        showLimit();
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        dbHelper = new DBHelper(this);
        dbHelper1 = new DBHelper1(this);
        dbHelper2 = new DBHelper2(this);
        showLimit();
        init();
        updateAccount();
    }


    private void showLimit(){
        boolean f;
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                SharedPreferences preferences = getSharedPreferences(Limit.ACC1_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(Limit.ACC1_LIMIT_STATUS, false);
                if (f){
                    SharedPreferences limTypePref = getSharedPreferences("acc1TypeFile", Context.MODE_PRIVATE);
                    SharedPreferences limValPref = getSharedPreferences("acc1ValueFile", Context.MODE_PRIVATE);
                    String type = limTypePref.getString("acc1Type", Limit.DAY_LIMIT);
                    SharedPreferences sharedPreferences1 = getSharedPreferences(ACCOUNT_ONE_FILE, Context.MODE_PRIVATE);
                    String cc = sharedPreferences1.getString(CURRENCY_KEY, "rub");
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
                    String typeS = "";
                    switch (type){
                        case Limit.DAY_LIMIT:
                            typeS = "Лимит на день: ";
                            restType.setText("Остаток на день: ");
                            break;
                        case Limit.WEEK_LIMIT:
                            typeS = "Лимит на неделю: ";
                            restType.setText("Остаток на неделю: ");
                            break;
                        case Limit._2_WEEKS_LIMIT:
                            typeS = "Лимит на 2 недели: ";
                            restType.setText("Остаток на 2 недели: ");
                            break;
                        case Limit.MONTH_LIMIT:
                            typeS = "Лимит на месяц: ";
                            restType.setText("Остаток на месяц: ");
                            break;
                    }
                    int val = limValPref.getInt("acc1Value", 0);
                    chosenLimit.setText(typeS);
                    chosenLimit.setTextSize(14);
                    chosenLimitValue.setText("" + val + c1);
                    chosenLimitValue.setTextSize(14);
                    Calendar c = new GregorianCalendar();
                    int y = c.get(Calendar.YEAR);
                    int m = c.get(Calendar.MONTH) + 1;
                    int d = c.get(Calendar.DAY_OF_MONTH);
                    String currentDate = d + "." + m + "." + y;
                    ArrayList<String> values = new ArrayList<>();
                    DBHelper dbHelper = new DBHelper(this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT " + InputData.TaskEntry.VALUE + " FROM " + InputData.TaskEntry.TABLE + " WHERE " + InputData.TaskEntry.OPERATION + " = '2131165294' AND " + InputData.TaskEntry.DATE + " = '" + currentDate + "';", null);
                    if (!(cursor.getCount() <= 0)) {
                        if (cursor.moveToFirst()) {
                            do {
                                values.add(cursor.getString(cursor.getColumnIndex(InputData.TaskEntry.VALUE)));
                            } while (cursor.moveToNext());
                        }
                    }
                    cursor.close();
                    db.close();
                    int restVal = 0;
                    for (int i = 0; i < values.size(); i++){
                        restVal += Integer.parseInt(values.get(i));
                    }
                    int res = val - restVal;
                    restType.setTextSize(14);
                    restTypeValue.setText("" + res + c1);
                    restTypeValue.setTextSize(14);
                } else {
                    chosenLimit.setText("");
                    chosenLimit.setTextSize(1);
                    chosenLimitValue.setText("");
                    chosenLimitValue.setTextSize(1);
                    restType.setText("");
                    restType.setTextSize(1);
                    restTypeValue.setText("");
                    restTypeValue.setTextSize(1);
                }
                break;
            }
            case "Счёт 2": {
                SharedPreferences preferences = getSharedPreferences(Limit.ACC2_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(Limit.ACC2_LIMIT_STATUS, false);
                if (f){
                    SharedPreferences limTypePref = getSharedPreferences("acc2TypeFile", Context.MODE_PRIVATE);
                    SharedPreferences limValPref = getSharedPreferences("acc2ValueFile", Context.MODE_PRIVATE);
                    String type = limTypePref.getString("acc2Type", Limit.DAY_LIMIT);
                    SharedPreferences sharedPreferences1 = getSharedPreferences(ACCOUNT_TWO_FILE, Context.MODE_PRIVATE);
                    String cc = sharedPreferences1.getString(CURRENCY_KEY, "rub");
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
                    String typeS = "";
                    switch (type){
                        case Limit.DAY_LIMIT:
                            typeS = "Лимит на день: ";
                            restType.setText("Остаток на день: ");
                            break;
                        case Limit.WEEK_LIMIT:
                            typeS = "Лимит на неделю: ";
                            restType.setText("Остаток на неделю: ");
                            break;
                        case Limit._2_WEEKS_LIMIT:
                            typeS = "Лимит на 2 недели: ";
                            restType.setText("Остаток на 2 недели: ");
                            break;
                        case Limit.MONTH_LIMIT:
                            typeS = "Лимит на месяц: ";
                            restType.setText("Остаток на месяц: ");
                            break;
                    }
                    int val = limValPref.getInt("acc2Value", 0);
                    chosenLimit.setText(typeS);
                    chosenLimit.setTextSize(14);
                    chosenLimitValue.setText("" + val + c1);
                    chosenLimitValue.setTextSize(14);
                    Calendar c = new GregorianCalendar();
                    int y = c.get(Calendar.YEAR);
                    int m = c.get(Calendar.MONTH) + 1;
                    int d = c.get(Calendar.DAY_OF_MONTH);
                    String currentDate = d + "." + m + "." + y;
                    ArrayList<String> values = new ArrayList<>();
                    DBHelper1 dbHelper1 = new DBHelper1(this);
                    SQLiteDatabase db = dbHelper1.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT " + InputData1.TaskEntry1.VALUE1 + " FROM " + InputData1.TaskEntry1.TABLE1 + " WHERE " + InputData1.TaskEntry1.OPERATION1 + " = '2131165294' AND " + InputData1.TaskEntry1.DATE1 + " = '" + currentDate + "';", null);
                    if (!(cursor.getCount() <= 0)) {
                        if (cursor.moveToFirst()) {
                            do {
                                values.add(cursor.getString(cursor.getColumnIndex(InputData1.TaskEntry1.VALUE1)));
                            } while (cursor.moveToNext());
                        }
                    }
                    cursor.close();
                    db.close();
                    int restVal = 0;
                    for (int i = 0; i < values.size(); i++){
                        restVal += Integer.parseInt(values.get(i));
                    }
                    int res = val - restVal;
                    restType.setTextSize(14);
                    restTypeValue.setText("" + res + c1);
                    restTypeValue.setTextSize(14);
                } else {
                    chosenLimit.setText("");
                    chosenLimit.setTextSize(1);
                    chosenLimitValue.setText("");
                    chosenLimitValue.setTextSize(1);
                    restType.setText("");
                    restType.setTextSize(1);
                    restTypeValue.setText("");
                    restTypeValue.setTextSize(1);
                }
                break;
            }
            case "Счёт 3": {
                SharedPreferences preferences = getSharedPreferences(Limit.ACC3_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(Limit.ACC3_LIMIT_STATUS, false);
                if (f){
                    SharedPreferences limTypePref = getSharedPreferences("acc3TypeFile", Context.MODE_PRIVATE);
                    SharedPreferences limValPref = getSharedPreferences("acc3ValueFile", Context.MODE_PRIVATE);
                    String type = limTypePref.getString("acc3Type", Limit.DAY_LIMIT);
                    SharedPreferences sharedPreferences1 = getSharedPreferences(ACCOUNT_THREE_FILE, Context.MODE_PRIVATE);
                    String cc = sharedPreferences1.getString(CURRENCY_KEY, "rub");
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
                    String typeS = "";
                    switch (type){
                        case Limit.DAY_LIMIT:
                            typeS = "Лимит на день: ";
                            restType.setText("Остаток на день: ");
                            break;
                        case Limit.WEEK_LIMIT:
                            typeS = "Лимит на неделю: ";
                            restType.setText("Остаток на неделю: ");
                            break;
                        case Limit._2_WEEKS_LIMIT:
                            typeS = "Лимит на 2 недели: ";
                            restType.setText("Остаток на 2 недели: ");
                            break;
                        case Limit.MONTH_LIMIT:
                            typeS = "Лимит на месяц: ";
                            restType.setText("Остаток на месяц: ");
                            break;
                    }
                    int val = limValPref.getInt("acc3Value", 0);
                    chosenLimit.setText(typeS);
                    chosenLimit.setTextSize(14);
                    chosenLimitValue.setText("" + val + c1);
                    chosenLimitValue.setTextSize(14);
                    Calendar c = new GregorianCalendar();
                    int y = c.get(Calendar.YEAR);
                    int m = c.get(Calendar.MONTH) + 1;
                    int d = c.get(Calendar.DAY_OF_MONTH);
                    String currentDate = d + "." + m + "." + y;
                    ArrayList<String> values = new ArrayList<>();
                    DBHelper2 dbHelper2 = new DBHelper2(this);
                    SQLiteDatabase db = dbHelper2.getReadableDatabase();
                    Cursor cursor = db.rawQuery("SELECT " + InputData2.TaskEntry2.VALUE2 + " FROM " + InputData2.TaskEntry2.TABLE2 + " WHERE " + InputData2.TaskEntry2.OPERATION2 + " = '2131165294' AND " + InputData2.TaskEntry2.DATE2+ " = '" + currentDate + "';", null);
                    if (!(cursor.getCount() <= 0)) {
                        if (cursor.moveToFirst()) {
                            do {
                                values.add(cursor.getString(cursor.getColumnIndex(InputData2.TaskEntry2.VALUE2)));
                            } while (cursor.moveToNext());
                        }
                    }
                    cursor.close();
                    db.close();
                    int restVal = 0;
                    for (int i = 0; i < values.size(); i++){
                        restVal += Integer.parseInt(values.get(i));
                    }
                    int res = val - restVal;
                    restType.setTextSize(14);
                    restTypeValue.setText("" + res + c1);
                    restTypeValue.setTextSize(14);
                } else {
                    chosenLimit.setText("");
                    chosenLimit.setTextSize(1);
                    chosenLimitValue.setText("");
                    chosenLimitValue.setTextSize(1);
                    restType.setText("");
                    restType.setTextSize(1);
                    restTypeValue.setText("");
                    restTypeValue.setTextSize(1);
                }
                break;
            }
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = adapter.getPosition();
        deleteFromBase(position);
        updateUI();
        return super.onContextItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(ACCOUNT_KEY, names[0]);
        if (curAc.equals(names[0])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_ONE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        } else if (curAc.equals(names[1])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_TWO_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        }  else if (curAc.equals(names[2])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_THREE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        }
        loadBalance();
        updateUI();
    }

    void updBalCur(){
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(ACCOUNT_KEY, names[0]);
        if (curAc.equals(names[0])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_ONE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        } else if (curAc.equals(names[1])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_TWO_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        }  else if (curAc.equals(names[2])) {
            sharedPreferences = getSharedPreferences(ACCOUNT_THREE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "dollar":
                    currency.setText("" + getString(R.string.dollar));
                    break;
                case "rub":
                    currency.setText("" + getString(R.string.ruble));
                    break;
                case "euro":
                    currency.setText("" + getString(R.string.euro));
                    break;
            }
        }
        loadBalance();
        updateUI();
    }

    void updateAccount(){
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        String mPos = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

        int spPos = spinnerAdapter.getPosition(mPos);
        spinner.setSelection(spPos);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.exchange){
            Intent intent = new Intent(this, Exchange.class);
            intent.putExtra("dol", dollar);
            intent.putExtra("eur", euro);
            intent.putExtra("belr", belRub);
            intent.putExtra("pon", pound);
            intent.putExtra("tenge", tenge);
            intent.putExtra("gryvna", gryvan);
            startActivity(intent);
        } else if (id == R.id.settings){

            startActivity(new Intent(this, Settings.class));
        } else if (id == R.id.statistic){

            startActivity(new Intent(this, Statistic.class));
        } else if (id == R.id.limit){
            startActivity(new Intent(this, Limit.class));
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.floatingActionButton) {
            launchDialogAdd();
            updateUI();
        } else if (id == R.id.linearLayout){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Баланс");
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(R.layout.balance_change_dialog, null);
            builder.setView(view);
            changeBalanceEditText = view.findViewById(R.id.et_bal_change);
            changeBalanceEditText.setText("" + balance.getText().toString());
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
                    currentAccount = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

                    if (currentAccount.equals(names[0])) {
                        String fileNameB = "balanceSP";
                        sharedPreferences = getSharedPreferences(fileNameB, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("b", changeBalanceEditText.getText().toString());
                        editor.apply();
                    } else if (currentAccount.equals(names[1])){
                        String fileNameB1 = "balanceSP1";
                        sharedPreferences = getSharedPreferences(fileNameB1, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("b1", changeBalanceEditText.getText().toString());
                        editor.apply();

                    } else if (currentAccount.equals(names[2])){

                        String fileNameB2 = "balanceSP2";
                        sharedPreferences = getSharedPreferences(fileNameB2, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("b2", changeBalanceEditText.getText().toString());
                        editor.apply();
                    }
                    loadBalance();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
            loadBalance();
            updateUI();

        }
    }

    private void init(){
        runnable = new Runnable() {
            @Override
            public void run() {
                getWeb();
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private void getWeb(){
        try {
            doc = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get();

            Elements elements = doc.getElementsByTag("tbody");
            org.jsoup.nodes.Element element = elements.get(0);
            Elements elementsFromTable = element.children();
            String s = elementsFromTable.get(11).toString();
            String[] st = s.split("<td>");
            String[] name = st[4].split("<");
            String[] value = st[5].split("<");
            String s1 = elementsFromTable.get(14).toString();
            String[] st1 = s1.split("<td>");
            String[] name1 = st1[4].split("<");
            String[] value1 = st1[5].split("<");
            String s2 = elementsFromTable.get(12).toString();
            String[] st2 = s2.split("<td>");
            String[] name2 = st2[4].split("<");
            String[] value2 = st2[5].split("<");
            String s3 = elementsFromTable.get(4).toString();
            String[] st3 = s3.split("<td>");
            String[] name3 = st3[4].split("<");
            String[] value3 = st3[5].split("<");
            String s4 = elementsFromTable.get(29).toString();
            String[] st4 = s4.split("<td>");
            String[] name4 = st4[4].split("<");
            String[] value4 = st4[5].split("<");
            String s5 = elementsFromTable.get(28).toString();
            String[] st5 = s5.split("<td>");
            String[] name5 = st5[4].split("<");
            String[] value5 = st5[5].split("<");
            dollar = name[0] + " : " + value[0] + getString(R.string.ruble);
            euro = name2[0]  + " : " + value2[0] + "₽";
            belRub = name3[0] + " : " + value3[0] + "₽";
            pound = name4[0] + " : " + value4[0] + "₽";
            tenge = name1[0] + " : " + value1[0] + "₽" + " на 100₽";
            gryvan = name5[0] + " : " + value5[0] + "₽" + " на 10₽";
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void launchDialogAdd(){
        loadBalance();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Добавить операцию");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_dialog, null);
        builder.setView(view);
        inpValueET = view.findViewById(R.id.input_value);
        radioButtonIn = view.findViewById(R.id.income);
        radioButtonOut = view.findViewById(R.id.outcome);
        radioButtonOut.setChecked(true);
        outcome = true;
        income = false;
        Spinner spinnerIn, spinnerOut;
        spinnerIn = view.findViewById(R.id.spinner_in_cat);
        spinnerOut = view.findViewById(R.id.spinner_out_cat);
        CategoryIncome categoryIncome = new CategoryIncome(this);
        inList = new ArrayList<>();
        SQLiteDatabase database = categoryIncome.getReadableDatabase();
        Cursor cursor = database.query(CategoryIncome.CatInEntry.TABLECI, new String[]{CategoryIncome.CatInEntry._ID, CategoryIncome.CatInEntry.IN_CATEGORY}, null, null, null, null, null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(CategoryIncome.CatInEntry._ID);
            int idxC = cursor.getColumnIndex(CategoryIncome.CatInEntry.IN_CATEGORY);
            inList.add(cursor.getString(idxC));
        }
            spinnerInAdapter = new ArrayAdapter<String>(this, R.layout.spinner_cat, R.id.tv_spin_cat, inList);
            spinnerIn.setAdapter(spinnerInAdapter);

        cursor.close();
        database.close();
        CategoryOutcome categoryOutcome = new CategoryOutcome(this);
        outList = new ArrayList<>();
        SQLiteDatabase database1 = categoryOutcome.getReadableDatabase();
        Cursor cursor1 = database1.query(CategoryOutcome.CatEntry.TABLEC, new String[]{CategoryOutcome.CatEntry._ID, CategoryOutcome.CatEntry.OUT_CATEGORY}, null, null, null, null, null);
        while (cursor1.moveToNext()){
            int idx = cursor1.getColumnIndex(CategoryOutcome.CatEntry._ID);
            int idxC = cursor1.getColumnIndex(CategoryOutcome.CatEntry.OUT_CATEGORY);
            outList.add(cursor1.getString(idxC));
        }
            spinnerOutAdapter = new ArrayAdapter<String>(this, R.layout.spinner_cat, R.id.tv_spin_cat, outList);
            spinnerOut.setAdapter(spinnerOutAdapter);

        cursor1.close();
        database1.close();
        spinnerIn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinIn = inList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerOut.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinOut = outList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        radioGroup = view.findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.income) {
                    income = true;
                    outcome = false;
                } else if (checkedId == R.id.outcome) {
                    income = false;
                    outcome = true;
                }
            }

        });
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
                currentAccount = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

                if (currentAccount.equals(names[0])) {
                    if (income) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_up_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal + i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            cv.put(InputData.TaskEntry.VALUE, i);
                            cv.put(InputData.TaskEntry.TOTAL_VALUE, bal);
                            cv.put(InputData.TaskEntry.DATE, dateC());
                            cv.put(InputData.TaskEntry.OPERATION, res);
                            cv.put(InputData.TaskEntry.CATEGORY, spinIn);
                            db.insert(InputData.TaskEntry.TABLE, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else if (outcome) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_down_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal - i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper.getWritableDatabase();
                            cv.put(InputData.TaskEntry.VALUE, i);
                            cv.put(InputData.TaskEntry.TOTAL_VALUE, bal);
                            cv.put(InputData.TaskEntry.DATE, dateC());
                            cv.put(InputData.TaskEntry.OPERATION, res);
                            cv.put(InputData.TaskEntry.CATEGORY, spinOut);
                            db.insert(InputData.TaskEntry.TABLE, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "no operation selected", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentAccount.equals(names[1])){
                    if (income) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_up_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal + i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper1.getWritableDatabase();
                            cv.put(InputData1.TaskEntry1.VALUE1, i);
                            cv.put(InputData1.TaskEntry1.TOTAL_VALUE1, bal);
                            cv.put(InputData1.TaskEntry1.DATE1, dateC());
                            cv.put(InputData1.TaskEntry1.OPERATION1, res);
                            cv.put(InputData1.TaskEntry1.CATEGORY1, spinIn);
                            db.insert(InputData1.TaskEntry1.TABLE1, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else if (outcome) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_down_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal - i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper1.getWritableDatabase();
                            cv.put(InputData1.TaskEntry1.VALUE1, i);
                            cv.put(InputData1.TaskEntry1.TOTAL_VALUE1, bal);
                            cv.put(InputData1.TaskEntry1.DATE1, dateC());
                            cv.put(InputData1.TaskEntry1.OPERATION1, res);
                            cv.put(InputData1.TaskEntry1.CATEGORY1, spinOut);
                            db.insert(InputData1.TaskEntry1.TABLE1, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "no operation selected", Toast.LENGTH_SHORT).show();
                    }
                } else if (currentAccount.equals(names[2])){
                    if (income) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_up_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal + i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper2.getWritableDatabase();
                            cv.put(InputData2.TaskEntry2.VALUE2, i);
                            cv.put(InputData2.TaskEntry2.TOTAL_VALUE2, bal);
                            cv.put(InputData2.TaskEntry2.DATE2, dateC());
                            cv.put(InputData2.TaskEntry2.OPERATION2, res);
                            cv.put(InputData2.TaskEntry2.CATEGORY2, spinIn);
                            db.insert(InputData2.TaskEntry2.TABLE2, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else if (outcome) {
                        if (!inpValueET.getText().toString().isEmpty()) {
                            res = R.drawable.ic_baseline_arrow_drop_down_24;
                            int i = Integer.parseInt(inpValueET.getText().toString());
                            int bal = Integer.parseInt(balance.getText().toString());
                            int k = bal - i;
                            balance.setText("" + k);
                            ContentValues cv = new ContentValues();
                            SQLiteDatabase db = dbHelper2.getWritableDatabase();
                            cv.put(InputData2.TaskEntry2.VALUE2, i);
                            cv.put(InputData2.TaskEntry2.TOTAL_VALUE2, bal);
                            cv.put(InputData2.TaskEntry2.DATE2, dateC());
                            cv.put(InputData2.TaskEntry2.OPERATION2, res);
                            cv.put(InputData2.TaskEntry2.CATEGORY2, spinOut);
                            db.insert(InputData2.TaskEntry2.TABLE2, null, cv);
                            db.close();
                            saveBalance();
                            updateUI();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "no operation selected", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        updateUI();
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
        updateUI();
    }

    public String dateC(){
        Calendar c = new GregorianCalendar();
        int y = c.get(Calendar.YEAR);
        int m = c.get(Calendar.MONTH) + 1;
        int d = c.get(Calendar.DAY_OF_MONTH);
        return d + "." + m + "." + y;
    }

    private void saveBalance(){
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        currentAccount = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

        if (currentAccount.equals(names[0])) {
            String fileNameB = "balanceSP";
            sharedPreferences = getSharedPreferences(fileNameB, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("b", balance.getText().toString());
            editor.apply();
        } else if (currentAccount.equals(names[1])){
            String fileNameB1 = "balanceSP1";
            sharedPreferences = getSharedPreferences(fileNameB1, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("b1", balance.getText().toString());
            editor.apply();

        } else if (currentAccount.equals(names[2])){

            String fileNameB2 = "balanceSP2";
            sharedPreferences = getSharedPreferences(fileNameB2, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("b2", balance.getText().toString());
            editor.apply();
        }
    }
    void loadBalance(){
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        currentAccount = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

        if (currentAccount.equals(names[0])) {
            String fileNameB = "balanceSP";
            sharedPreferences = getSharedPreferences(fileNameB, Context.MODE_PRIVATE);
            String bb = sharedPreferences.getString("b", "0");
            balance.setText(bb);
        } else if (currentAccount.equals(names[1])){

            String fileNameB1 = "balanceSP1";
            sharedPreferences = getSharedPreferences(fileNameB1, Context.MODE_PRIVATE);
            String bb =sharedPreferences.getString("b1", "0");
            balance.setText(bb);
        } else if (currentAccount.equals(names[2])){
            String fileNameB2 = "balanceSP2";
            sharedPreferences = getSharedPreferences(fileNameB2, Context.MODE_PRIVATE);
            String bb =sharedPreferences.getString("b2", "0");
            balance.setText(bb);
        }
    }



    void updateUI() {
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        String cu = sharedPreferences.getString(ACCOUNT_KEY, names[0]);
        String c = "";
        if (cu.equals(names[0])){
            sharedPreferences = getSharedPreferences(ACCOUNT_ONE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "rub":
                    c = getString(R.string.ruble);
                    break;
                case "euro":
                    c = getString(R.string.euro);
                    break;
                case "dollar":
                    c = getString(R.string.dollar);
                    break;
            }
        } else if (cu.equals(names[1])){
            sharedPreferences = getSharedPreferences(ACCOUNT_TWO_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "rub":
                    c = getString(R.string.ruble);
                    break;
                case "euro":
                    c = getString(R.string.euro);
                    break;
                case "dollar":
                    c = getString(R.string.dollar);
                    break;
            }
        } else if (cu.equals(names[2])){
            sharedPreferences = getSharedPreferences(ACCOUNT_THREE_FILE, Context.MODE_PRIVATE);
            String cc = sharedPreferences.getString(CURRENCY_KEY, "rub");
            switch (cc) {
                case "rub":
                    c = getString(R.string.ruble);
                    break;
                case "euro":
                    c = getString(R.string.euro);
                    break;
                case "dollar":
                    c = getString(R.string.dollar);
                    break;
            }
        }
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        currentAccount = sharedPreferences.getString(ACCOUNT_KEY, names[0]);

        if (currentAccount.equals(names[0])) {
            indexes = new ArrayList<String>();
            elements = new ArrayList<Element>();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor cursor = db.query(InputData.TaskEntry.TABLE, new String[]{InputData.TaskEntry._ID, InputData.TaskEntry.VALUE, InputData.TaskEntry.TOTAL_VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION, InputData.TaskEntry.CATEGORY}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int ii = cursor.getColumnIndex(InputData.TaskEntry._ID);
                int idx = cursor.getColumnIndex(InputData.TaskEntry.DATE);
                int idx1 = cursor.getColumnIndex(InputData.TaskEntry.TOTAL_VALUE);
                int idx2 = cursor.getColumnIndex(InputData.TaskEntry.VALUE);
                int idx3 = cursor.getColumnIndex(InputData.TaskEntry.OPERATION);
                int idx4 = cursor.getColumnIndex(InputData.TaskEntry.CATEGORY);
                String cat = cursor.getString(idx4);
                if (cursor.getString(idx4).equals("")){
                    cat = "Отсутствует";
                }
                indexes.add(0, cursor.getString(ii));
                elements.add(0, new Element("" + cursor.getString(idx2), "" + cursor.getString(idx1), "" + cursor.getString(idx), cursor.getInt(idx3), "" + c,"" + cat));
            }
            if (adapter == null) {
                adapter = new ElementAdapter(getLayoutInflater(), elements);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(elements);
                adapter.notifyDataSetChanged();
            }
            cursor.close();
            db.close();
        } else if (currentAccount.equals(names[1])){
            indexes = new ArrayList<String>();
            elements = new ArrayList<Element>();
            SQLiteDatabase db = dbHelper1.getReadableDatabase();
            Cursor cursor = db.query(InputData1.TaskEntry1.TABLE1, new String[]{InputData1.TaskEntry1._ID, InputData1.TaskEntry1.VALUE1, InputData1.TaskEntry1.TOTAL_VALUE1, InputData1.TaskEntry1.DATE1, InputData1.TaskEntry1.OPERATION1, InputData1.TaskEntry1.CATEGORY1}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int ii = cursor.getColumnIndex(InputData1.TaskEntry1._ID);
                int idx = cursor.getColumnIndex(InputData1.TaskEntry1.DATE1);
                int idx1 = cursor.getColumnIndex(InputData1.TaskEntry1.TOTAL_VALUE1);
                int idx2 = cursor.getColumnIndex(InputData1.TaskEntry1.VALUE1);
                int idx3 = cursor.getColumnIndex(InputData1.TaskEntry1.OPERATION1);
                int idx4 = cursor.getColumnIndex(InputData1.TaskEntry1.CATEGORY1);
                String cat = cursor.getString(idx4);
                if (cursor.getString(idx4).equals("")){
                    cat = "Отсутствует";
                }
                indexes.add(0, cursor.getString(ii));
                elements.add(0, new Element("" + cursor.getString(idx2), "" + cursor.getString(idx1), "" + cursor.getString(idx), cursor.getInt(idx3), "" + c, "" + cat));
            }
            if (adapter == null) {
                adapter = new ElementAdapter(getLayoutInflater(), elements);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(elements);
                adapter.notifyDataSetChanged();
            }
            cursor.close();
            db.close();

        } else if (currentAccount.equals(names[2])){
            indexes = new ArrayList<String>();
            elements = new ArrayList<Element>();
            SQLiteDatabase db = dbHelper2.getReadableDatabase();
            Cursor cursor = db.query(InputData2.TaskEntry2.TABLE2, new String[]{InputData2.TaskEntry2._ID, InputData2.TaskEntry2.VALUE2, InputData2.TaskEntry2.TOTAL_VALUE2, InputData2.TaskEntry2.DATE2, InputData2.TaskEntry2.OPERATION2, InputData2.TaskEntry2.CATEGORY2}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                int ii = cursor.getColumnIndex(InputData2.TaskEntry2._ID);
                int idx = cursor.getColumnIndex(InputData2.TaskEntry2.DATE2);
                int idx1 = cursor.getColumnIndex(InputData2.TaskEntry2.TOTAL_VALUE2);
                int idx2 = cursor.getColumnIndex(InputData2.TaskEntry2.VALUE2);
                int idx3 = cursor.getColumnIndex(InputData2.TaskEntry2.OPERATION2);
                int idx4 = cursor.getColumnIndex(InputData2.TaskEntry2.CATEGORY2);
                String cat = cursor.getString(idx4);
                if (cursor.getString(idx4).equals("")){
                    cat = "Отсутствует";
                }
                indexes.add(0, cursor.getString(ii));
                elements.add(0, new Element("" + cursor.getString(idx2), "" + cursor.getString(idx1), "" + cursor.getString(idx), cursor.getInt(idx3), "" + c, "" + cat));
            }
            if (adapter == null) {
                adapter = new ElementAdapter(getLayoutInflater(), elements);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.clear();
                adapter.addAll(elements);
                adapter.notifyDataSetChanged();
            }
            cursor.close();
            db.close();

        }
        showLimit();
    }

    void deleteFromBase(int id){
        sharedPreferences = getSharedPreferences(ACCOUNT_FILE, Context.MODE_PRIVATE);
        String acc = sharedPreferences.getString(ACCOUNT_KEY, names[0]);
        if (acc.equals(names[0])){
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            String i = indexes.get(id);
            database.delete(InputData.TaskEntry.TABLE, InputData.TaskEntry._ID + "=?", new String[] {i});
        } else if (acc.equals(names[1])){
            SQLiteDatabase database = dbHelper1.getWritableDatabase();
            String i = indexes.get(id);
            database.delete(InputData1.TaskEntry1.TABLE1, InputData1.TaskEntry1._ID + "=?", new String[] {i});
        } else if (acc.equals(names[2])){
            SQLiteDatabase database = dbHelper2.getWritableDatabase();
            String i = indexes.get(id);
            database.delete(InputData2.TaskEntry2.TABLE2, InputData2.TaskEntry2._ID + "=?", new String[] {i});
        }
        updateUI();
    }
}
