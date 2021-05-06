package com.yunitski.bookkeeper.moneykeeper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class Exchange extends AppCompatActivity {


    private String dollar, euro, belRub, pound;
    TextView t1, t2, t3, t4;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);

        toolbar = findViewById(R.id.toolBar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Курс валют");
        t1 = findViewById(R.id.dollar);
        t2 = findViewById(R.id.euro);
        t3 = findViewById(R.id.bel_rub);
        t4 = findViewById(R.id.phound);

        dollar = getIntent().getStringExtra("dol");
        euro = getIntent().getStringExtra("eur");
        belRub = getIntent().getStringExtra("belr");
        pound = getIntent().getStringExtra("pon");
        t1.setText(dollar);
        t2.setText(euro);
        t3.setText(belRub);
        t4.setText(pound);

    }


}