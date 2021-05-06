package com.yunitski.bookkeeper.moneykeeper;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Settings extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    RadioGroup group;
    RadioButton rub, euro, doll;
    FloatingActionButton apply;
    static boolean rubChecked, euroChecked, dollChecked;
    String cur;
    TextView textView, delOp, delBal;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolBar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Настройки");
        rubChecked = true;
        euroChecked = false;
        dollChecked = false;
        cur = "";
        String file = "myFile";
        sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        cur = sharedPreferences.getString("currency", "rub");
        editor.apply();
        rub = findViewById(R.id.radioButtonRuble);
        euro = findViewById(R.id.radioButtonEuro);
        doll = findViewById(R.id.radioButtonDollar);
        group = findViewById(R.id.currencyGroup);
        apply = findViewById(R.id.set_apply);
        textView = findViewById(R.id.textView6);
        delBal = findViewById(R.id.del_balance);
        delOp = findViewById(R.id.del_op);
        delOp.setOnClickListener(this);
        delBal.setOnClickListener(this);
        apply.setOnClickListener(this);
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonRuble){
                    rubChecked = true;
                    euroChecked = false;
                    dollChecked = false;

                } else if (checkedId == R.id.radioButtonEuro){
                    rubChecked = false;
                    euroChecked = true;
                    dollChecked = false;
                } else if (checkedId == R.id.radioButtonDollar){
                    rubChecked = false;
                    euroChecked = false;
                    dollChecked = true;
                }
            }
        });
        if (cur.equals("rub")){
            rub.setChecked(true);
        } else if (cur.equals("euro")){
            euro.setChecked(true);
        } else if (cur.equals("dollar")){
            doll.setChecked(true);
        }
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.set_apply) {

            String file = "myFile";
            sharedPreferences = getSharedPreferences(file, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (rubChecked) {
                editor.putString("currency", "rub");
                editor.apply();
            } else if (euroChecked) {
                editor.putString("currency", "euro");
                editor.apply();
            } else if (dollChecked) {
                editor.putString("currency", "dollar");
                editor.apply();
            }
            finish();
        } else if (id == R.id.del_op){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Удалить операции?");
            builder.setPositiveButton("да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    deleteDatabase(InputData.DB_NAME);
                }
            });
            builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
        } else if (id == R.id.del_balance){


            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("Очистить баланс?");
            builder.setPositiveButton("да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    String fileName = "balanceSP";
                    sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("b", "0");
                    editor.apply();
                }
            });
            builder.setNegativeButton("отмена", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#37334c")));
        }
    }
}