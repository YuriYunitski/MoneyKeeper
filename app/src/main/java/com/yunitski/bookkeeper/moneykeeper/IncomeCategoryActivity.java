package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class IncomeCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ListView lvIncome;
    ArrayAdapter<String> adapter;
    ArrayList<String> list, indexes;
    CategoryIncome categoryIncome;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_category);

        toolbar = findViewById(R.id.toolBarInC);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Доходы");
        lvIncome = findViewById(R.id.lv_income);
        fab = findViewById(R.id.add_in_category);
        fab.setOnClickListener(this);
        categoryIncome = new CategoryIncome(this);
        registerForContextMenu(lvIncome);
        updateUI();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                break;
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
        int positionOfMenuItem = 0;
        MenuItem item = menu.getItem(positionOfMenuItem);
        SpannableString s = new SpannableString("Удалить");
        s.setSpan(new ForegroundColorSpan(Color.BLACK), 0, s.length(), 0);
        item.setTitle(s);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        SQLiteDatabase database =categoryIncome.getWritableDatabase();
        String i = indexes.get(info.position);
        database.delete(CategoryIncome.CatInEntry.TABLECI, CategoryIncome.CatInEntry._ID + "=?", new String[] {i});
        updateUI();
        return true;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Добавить категорию");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.add_category, null);
        builder.setView(view);
        EditText et = view.findViewById(R.id.add_cat);

        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String added = et.getText().toString();
                ContentValues cv = new ContentValues();
                SQLiteDatabase database = categoryIncome.getWritableDatabase();
                cv.put(CategoryIncome.CatInEntry.IN_CATEGORY, added);
                database.insert(CategoryIncome.CatInEntry.TABLECI, null, cv);
                database.close();
                updateUI();
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

        updateUI();
    }

    public void updateUI(){
        list = new ArrayList<>();
        indexes = new ArrayList<>();
        SQLiteDatabase db = categoryIncome.getReadableDatabase();
        Cursor cursor = db.query(CategoryIncome.CatInEntry.TABLECI, new String[]{CategoryIncome.CatInEntry._ID, CategoryIncome.CatInEntry.IN_CATEGORY}, null, null, null, null, null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(CategoryIncome.CatInEntry._ID);
            int idxC = cursor.getColumnIndex(CategoryIncome.CatInEntry.IN_CATEGORY);
            list.add(cursor.getString(idxC));
            indexes.add(cursor.getString(idx));
        }
        if (adapter == null){
            adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_expandable_list_item_1, list);
            lvIncome.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
    }
}
