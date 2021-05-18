package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class Limit extends AppCompatActivity implements View.OnClickListener {

    Toolbar toolbar;
    Switch aSwitch;
    Button calculate;
    TextView currentLimitValue, currentLimitType;
    public static final String DAY_LIMIT = "dayLim";
    public static final String WEEK_LIMIT = "weekLim";
    public static final String _2_WEEKS_LIMIT = "2weeksLim";
    public static final String MONTH_LIMIT = "monthLim";
    public static final String ACC1_FILE_IS_LIMIT_ACTIVE = "acc1FileIsLimitActive";
    public static final String ACC2_FILE_IS_LIMIT_ACTIVE = "acc2FileIsLimitActive";
    public static final String ACC3_FILE_IS_LIMIT_ACTIVE = "acc3FileIsLimitActive";
    public static final String ACC1_LIMIT_STATUS = "acc1LimitStatus";
    public static final String ACC2_LIMIT_STATUS = "acc2LimitStatus";
    public static final String ACC3_LIMIT_STATUS = "acc3LimitStatus";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_limit);
        toolbar = findViewById(R.id.toolBar4);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Лимит");
        aSwitch = findViewById(R.id.switch1);
        aSwitch.setOnClickListener(this);
        boolean f = false;
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                SharedPreferences preferences = getSharedPreferences(ACC1_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(ACC1_LIMIT_STATUS, false);
                break;
            }
            case "Счёт 2": {
                SharedPreferences preferences = getSharedPreferences(ACC2_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(ACC2_LIMIT_STATUS, false);
                break;
            }
            case "Счёт 3": {
                SharedPreferences preferences = getSharedPreferences(ACC3_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                f = preferences.getBoolean(ACC3_LIMIT_STATUS, false);
                break;
            }
        }
        aSwitch.setChecked(f);
        calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(this);
        currentLimitValue = findViewById(R.id.val_lim_cur);
        currentLimitType = findViewById(R.id.type_lim_cur);
        updLimitMethod();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
        }
        return true;
    }
    private void updLimit(String typeFile, String valueFile, String type, String value){
        SharedPreferences typePref = getSharedPreferences(typeFile, Context.MODE_PRIVATE);
        SharedPreferences valPref = getSharedPreferences(valueFile, Context.MODE_PRIVATE);
        String typeS = typePref.getString(type, DAY_LIMIT);
        int valueS = valPref.getInt(value, 0);
        switch (typeS) {
            case DAY_LIMIT:
                currentLimitType.setText("День");
                break;
            case WEEK_LIMIT:
                currentLimitType.setText("Неделя");
                break;
            case _2_WEEKS_LIMIT:
                currentLimitType.setText("2 недели");
                break;
            case MONTH_LIMIT:
                currentLimitType.setText("Месяц");
                break;
        }
        String c1 = "";
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.ACCOUNT_ONE_FILE, Context.MODE_PRIVATE);
                String cc = sharedPreferences1.getString(MainActivity.CURRENCY_KEY, "rub");
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
                break;
            }
            case "Счёт 2":{
                SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.ACCOUNT_TWO_FILE, Context.MODE_PRIVATE);
                String cc = sharedPreferences1.getString(MainActivity.CURRENCY_KEY, "rub");
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
                break;
            }
            case "Счёт 3": {
                SharedPreferences sharedPreferences1 = getSharedPreferences(MainActivity.ACCOUNT_THREE_FILE, Context.MODE_PRIVATE);
                String cc = sharedPreferences1.getString(MainActivity.CURRENCY_KEY, "rub");
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
                break;
            }
            }
        currentLimitValue.setText("" + valueS + c1);
    }
    private void updLimitMethod(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                updLimit("acc1TypeFile", "acc1ValueFile", "acc1Type", "acc1Value");
                break;
            }
            case "Счёт 2": {
                updLimit("acc2TypeFile", "acc2ValueFile", "acc2Type", "acc2Value");
                break;
            }
            case "Счёт 3": {
                updLimit("acc3TypeFile", "acc3ValueFile", "acc3Type", "acc3Value");
                break;
            }
        }
    }

    private void setLimit(){
        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
        String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
        switch (curAc) {
            case "Счёт 1": {
                launchDialog("acc1TypeFile", "acc1ValueFile", "acc1Type", "acc1Value");
                break;
            }
            case "Счёт 2": {
                launchDialog("acc2TypeFile", "acc2ValueFile", "acc2Type", "acc2Value");
                break;
            }
            case "Счёт 3": {
                launchDialog("acc3TypeFile", "acc3ValueFile", "acc3Type", "acc3Value");
                break;
            }
        }
    }

    private void launchDialog(String typeFile, String valueFile, String type, String value){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Рассчитать");
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.calculate_limit_dialog, null);
        builder.setView(view);
        RadioGroup group = view.findViewById(R.id.radioGroup2);
        RadioButton dayRB = view.findViewById(R.id.rb_day);
        RadioButton weekRB = view.findViewById(R.id.rb_week);
        RadioButton _2weeksRB = view.findViewById(R.id.rb_2_weeks);
        RadioButton monthRB = view.findViewById(R.id.rb_month);
        SharedPreferences limTypePref = getSharedPreferences(typeFile, Context.MODE_PRIVATE);
        SharedPreferences limValPref = getSharedPreferences(valueFile, Context.MODE_PRIVATE);
        String curL = limTypePref.getString(type, DAY_LIMIT);
        switch (curL) {
            case DAY_LIMIT:
                weekRB.setChecked(false);
                _2weeksRB.setChecked(false);
                monthRB.setChecked(false);
                dayRB.setChecked(true);
                break;
            case WEEK_LIMIT:
                _2weeksRB.setChecked(false);
                monthRB.setChecked(false);
                dayRB.setChecked(false);
                weekRB.setChecked(true);
                break;
            case _2_WEEKS_LIMIT:
                monthRB.setChecked(false);
                dayRB.setChecked(false);
                weekRB.setChecked(false);
                _2weeksRB.setChecked(true);
                break;
            case MONTH_LIMIT:
                _2weeksRB.setChecked(false);
                dayRB.setChecked(false);
                weekRB.setChecked(false);
                monthRB.setChecked(true);
                break;
        }
        final String[] currentLimChoice = new String[1];
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_day){
                    currentLimChoice[0] = DAY_LIMIT;
                } else if(checkedId == R.id.rb_week){
                    currentLimChoice[0] = WEEK_LIMIT;
                } else if(checkedId == R.id.rb_2_weeks){
                    currentLimChoice[0] = _2_WEEKS_LIMIT;
                } else if(checkedId == R.id.rb_month){
                    currentLimChoice[0] = MONTH_LIMIT;
                }
            }
        });

        EditText valLimit = view.findViewById(R.id.et_lim_val);
        int valLimitToSave = limValPref.getInt(value, 0);
        valLimit.setText("" + valLimitToSave);
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = limTypePref.edit();
                editor.putString(type, currentLimChoice[0]);
                editor.apply();
                SharedPreferences.Editor editor1 = limValPref.edit();
                editor1.putInt(value, Integer.parseInt(valLimit.getText().toString()));
                editor1.apply();
                updLimitMethod();
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
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.switch1) {
            if (aSwitch.isChecked()) {
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
                String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
                switch (curAc) {
                    case "Счёт 1": {
                        SharedPreferences preferences = getSharedPreferences(ACC1_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC1_LIMIT_STATUS, true);
                        editor.apply();
                        break;
                    }
                    case "Счёт 2": {
                        SharedPreferences preferences = getSharedPreferences(ACC2_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC2_LIMIT_STATUS, true);
                        editor.apply();
                        break;
                    }
                    case "Счёт 3": {
                        SharedPreferences preferences = getSharedPreferences(ACC3_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC3_LIMIT_STATUS, true);
                        editor.apply();
                        break;
                    }
                }
            } else if (!aSwitch.isChecked()){
                SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.ACCOUNT_FILE, Context.MODE_PRIVATE);
                String curAc = sharedPreferences.getString(MainActivity.ACCOUNT_KEY, "Счёт 1");
                switch (curAc) {
                    case "Счёт 1": {
                        SharedPreferences preferences = getSharedPreferences(ACC1_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC1_LIMIT_STATUS, false);
                        editor.apply();
                        break;
                    }
                    case "Счёт 2": {
                        SharedPreferences preferences = getSharedPreferences(ACC2_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC2_LIMIT_STATUS, false);
                        editor.apply();
                        break;
                    }
                    case "Счёт 3": {
                        SharedPreferences preferences = getSharedPreferences(ACC3_FILE_IS_LIMIT_ACTIVE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(ACC2_LIMIT_STATUS, false);
                        editor.apply();
                        break;
                    }
                }
            }
        } else if (id == R.id.calculate){
            setLimit();
        }
    }
}