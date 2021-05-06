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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    TextView balance, currency;
    FloatingActionButton fab;
    ArrayList<Element> elements;
    static boolean outcome, income;
    EditText inpValueET;
    RadioButton radioButtonOut, radioButtonIn;
    RadioGroup radioGroup;
    DBHelper dbHelper;
    ElementAdapter adapter;
    SharedPreferences sharedPreferences;

    private Thread secThread;
    private Runnable runnable;
    private String dollar, euro, belRub, pound;
    int res;

    Document doc;

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
        balance = findViewById(R.id.balance);
        currency = findViewById(R.id.curremcy);
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(this);
        dbHelper = new DBHelper(this);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        String file = "myFile";
        sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString("currency", "rub");
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
        loadBalance();
        updateUI();
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
            startActivity(intent);
        } else if (id == R.id.settings){

            startActivity(new Intent(this, Settings.class));
        } else if (id == R.id.statistic){

            startActivity(new Intent(this, Statistic.class));
        }

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        launchDialogAdd();
        updateUI();
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
            dollar = name[0] + " : " + value[0] + getString(R.string.ruble);
            euro = name2[0]  + " : " + value2[0] + "₽";
            belRub = name3[0] + " : " + value3[0] + "₽";
            pound = name4[0] + " : " + value4[0] + "₽";
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
                if (income){
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
                        db.insert(InputData.TaskEntry.TABLE, null, cv);
                        db.close();
                        saveBalance();
                        updateUI();
                    }
                } else if(outcome){
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
                        db.insert(InputData.TaskEntry.TABLE, null, cv);
                        db.close();
                        saveBalance();
                        updateUI();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "no operation selected", Toast.LENGTH_SHORT).show();
                }

            }
        });
        updateUI();
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        updateUI();
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
        String fileName = "balanceSP";
        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("b", balance.getText().toString());
        editor.apply();
    }
    void loadBalance(){
        String fileName = "balanceSP";
        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        String bb =sharedPreferences.getString("b", "0");
        balance.setText(bb);
    }

    void updateUI(){

        String file = "myFile";
        sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
        String cc = sharedPreferences.getString("currency", "rub");
        String c = "";
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
        elements = new ArrayList<Element>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(InputData.TaskEntry.TABLE, new String[]{InputData.TaskEntry._ID, InputData.TaskEntry.VALUE, InputData.TaskEntry.TOTAL_VALUE, InputData.TaskEntry.DATE, InputData.TaskEntry.OPERATION}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(InputData.TaskEntry.DATE);
            int idx1 = cursor.getColumnIndex(InputData.TaskEntry.TOTAL_VALUE);
            int idx2 = cursor.getColumnIndex(InputData.TaskEntry.VALUE);
            int idx3 = cursor.getColumnIndex(InputData.TaskEntry.OPERATION);
            elements.add(0, new Element("" + cursor.getString(idx2), "" + cursor.getString(idx1),  "" + cursor.getString(idx), cursor.getInt(idx3), ""+c ));
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
}