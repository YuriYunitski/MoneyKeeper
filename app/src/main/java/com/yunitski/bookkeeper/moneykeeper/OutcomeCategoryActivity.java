package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class OutcomeCategoryActivity extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    ListView lvOutcome;
    ArrayAdapter<String> adapter;
    ArrayList<String> list, indexes;
    CategoryOutcome categoryOutcome;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outcome_category);
        toolbar = findViewById(R.id.toolBarOutC);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Расходы");
        lvOutcome = findViewById(R.id.lv_outcome);
        fab = findViewById(R.id.add_out_category);
        fab.setOnClickListener(this);
        categoryOutcome = new CategoryOutcome(this);
        registerForContextMenu(lvOutcome);
        updateUI();
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
        SQLiteDatabase database =categoryOutcome.getWritableDatabase();
        String i = indexes.get(info.position);
        database.delete(CategoryOutcome.CatEntry.TABLEC, CategoryOutcome.CatEntry._ID + "=?", new String[] {i});
        updateUI();
        return true;
    }

    private void updateUI(){

        list = new ArrayList<>();
        indexes = new ArrayList<>();
        SQLiteDatabase db = categoryOutcome.getReadableDatabase();
        Cursor cursor = db.query(CategoryOutcome.CatEntry.TABLEC, new String[]{CategoryOutcome.CatEntry._ID, CategoryOutcome.CatEntry.OUT_CATEGORY}, null, null, null, null, null);
        while (cursor.moveToNext()){
            int idx = cursor.getColumnIndex(CategoryOutcome.CatEntry._ID);
            int idxC = cursor.getColumnIndex(CategoryOutcome.CatEntry.OUT_CATEGORY);
            list.add(cursor.getString(idxC));
            indexes.add(cursor.getString(idx));
        }
        if (adapter == null){
            adapter = new ArrayAdapter<String>(this,  android.R.layout.simple_expandable_list_item_1, list)
//            {
//                @NonNull
//                @Override
//                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                    TextView item = (TextView) super.getView(position,convertView,parent);
//                    item.setTextSize(TypedValue.COMPLEX_UNIT_DIP,22);
//                    item.setBackgroundColor(Color.parseColor("#f7f7f7"));
//                    return super.getView(position, convertView, parent);
//                }
//            }
            ;
            lvOutcome.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(list);
            adapter.notifyDataSetChanged();
        }
        cursor.close();
        db.close();
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
                SQLiteDatabase database = categoryOutcome.getWritableDatabase();
                cv.put(CategoryOutcome.CatEntry.OUT_CATEGORY, added);
                database.insert(CategoryOutcome.CatEntry.TABLEC, null, cv);
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
}