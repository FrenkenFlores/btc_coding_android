package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class generatedPortfolio extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText et;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_portfolio);

        Intent i = getIntent();
        et = (EditText) findViewById(R.id.results);
        List<MainActivity.Obligation> top = (List<MainActivity.Obligation>) i.getSerializableExtra("topObligations");
        for (MainActivity.Obligation o: top) {
            et.setText(o.SHORTNAME + " " + o.FACEVALUE + " " + o.YIELDATPREVWAPRICE + " " + o.CouponYeild);
        }
    }
}