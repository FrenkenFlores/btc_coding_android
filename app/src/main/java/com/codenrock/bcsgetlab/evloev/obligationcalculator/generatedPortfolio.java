package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        String t = i.getStringExtra("res");
        Log.d("res", String.valueOf(t));
        et.setText(t);
    }
}