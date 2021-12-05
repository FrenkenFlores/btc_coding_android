package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_create_portfolio;
    private final String TAG_C_P="button_create_portfolio";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button_create_portfolio = (Button) findViewById(R.id.button_create_portfolio);
        button_create_portfolio.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.d(TAG_C_P, "Button");
    }
}