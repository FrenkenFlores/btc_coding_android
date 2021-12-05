package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private URL url;
    private HttpURLConnection connection;
    private Button button_create_portfolio;
    private final String TAG_C_P="button_create_portfolio";
    private final String TAG_URL="set_connection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        button_create_portfolio = (Button) findViewById(R.id.button_create_portfolio);
        button_create_portfolio.setOnClickListener(this);
    }

    class SetHttpConnection extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                url = new URL(getResources().getString(R.string.url));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);
                if (connection.getResponseCode() == 200) {
                    return connection.getResponseCode() + ": connection failed";
                }
            } catch (MalformedURLException e) {
                Log.e(TAG_URL, e.getMessage());
                e.printStackTrace();
                return "URL: connection failed";
            } catch (IOException e) {
                Log.e(TAG_URL, e.getMessage());
                e.printStackTrace();
                return "openConnection: connection failed";
            }
            return "connection succeeded";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    @Override
    public void onClick(View v) {
        Log.d(TAG_C_P, "Button");
        Log.d(TAG_URL, getResources().getString(R.string.url));
    }
}