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
    int res;

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
            startActivity(new Intent(this, Exchange.class));
        } else if (id == R.id.statistic){

            Toast.makeText(this, "item2", Toast.LENGTH_SHORT).show();
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
        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("b", balance.getText().toString());
        editor.apply();
    }
    void loadBalance(){
        sharedPreferences = getPreferences(MODE_PRIVATE);
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