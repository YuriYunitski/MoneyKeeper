package com.yunitski.bookkeeper.moneykeeper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Exchange extends AppCompatActivity {


    private String dollar, euro, belRub, pound, tenge, gryvna;
    TextView t1, t2, t3, t4, t5, t6;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        toolbar = findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Курс валют");
        t1 = findViewById(R.id.dollar);
        t2 = findViewById(R.id.euro);
        t3 = findViewById(R.id.bel_rub);
        t4 = findViewById(R.id.phound);
        t5 = findViewById(R.id.tenge);
        t6 = findViewById(R.id.gryvna);

        dollar = getIntent().getStringExtra("dol");
        euro = getIntent().getStringExtra("eur");
        belRub = getIntent().getStringExtra("belr");
        pound = getIntent().getStringExtra("pon");
        tenge = getIntent().getStringExtra("tenge");
        gryvna = getIntent().getStringExtra("gryvna");
        t1.setText(dollar);
        t2.setText(euro);
        t3.setText(belRub);
        t4.setText(pound);
        t5.setText(tenge);
        t6.setText(gryvna);
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
}