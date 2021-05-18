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
    public static final String ACCESS_LIM_FILE = "limFIle";
    public static final String ACCESS_LIM_TYPE = "limType";
    public static final String ACCESS_VAL_LIM_FILE = "valLimFile";
    public static final String ACCESS_VAL_LIM = "valLim";
    public static final String SWITCH_USE = "switchUse";

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
        calculate = findViewById(R.id.calculate);
        calculate.setOnClickListener(this);
        currentLimitValue = findViewById(R.id.val_lim_cur);
        currentLimitType = findViewById(R.id.type_lim_cur);
        updLimit();
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
    private void updLimit(){
        SharedPreferences typePref = getSharedPreferences(ACCESS_LIM_FILE, Context.MODE_PRIVATE);
        SharedPreferences valPref = getSharedPreferences(ACCESS_VAL_LIM_FILE, Context.MODE_PRIVATE);
        String type = typePref.getString(ACCESS_LIM_TYPE, DAY_LIMIT);
        int value = valPref.getInt(ACCESS_VAL_LIM, 0);
        switch (type) {
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
        currentLimitValue.setText("" + value);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.switch1) {
            if (aSwitch.isChecked()) {
                Toast.makeText(this, "oi", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.calculate){
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
            SharedPreferences limTypePref = getSharedPreferences(ACCESS_LIM_FILE, Context.MODE_PRIVATE);
            SharedPreferences limValPref = getSharedPreferences(ACCESS_VAL_LIM_FILE, Context.MODE_PRIVATE);
            String curL = limTypePref.getString(ACCESS_LIM_TYPE, DAY_LIMIT);
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
            int valLimitToSave = limValPref.getInt(ACCESS_VAL_LIM, 111);
            valLimit.setText("" + valLimitToSave);
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences.Editor editor = limTypePref.edit();
                    editor.putString(ACCESS_LIM_TYPE, currentLimChoice[0]);
                    editor.apply();
                    SharedPreferences.Editor editor1 = limValPref.edit();
                    editor1.putInt(ACCESS_VAL_LIM, Integer.parseInt(valLimit.getText().toString()));
                    editor1.apply();
                    updLimit();
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
    }
}