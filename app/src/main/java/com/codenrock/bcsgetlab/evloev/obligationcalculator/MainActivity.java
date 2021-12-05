package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BufferedReader reader;
    private StringBuffer content;

    private URL url;
    private HttpURLConnection connection;

    private Button button_create_portfolio;

    private final String TAG_C_P="button_create_portfolio";
    private final String TAG_URL="set_connection";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = new StringBuffer();
        button_create_portfolio = (Button) findViewById(R.id.button_create_portfolio);
        button_create_portfolio.setOnClickListener(this);
    }

    class SetHttpConnection extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            try {
                // Set connection
                url = new URL(getResources().getString(R.string.url));
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                // Log processes
                try {
                    int code = connection.getResponseCode();
                    Log.d(TAG_URL, String.valueOf(code));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Check if got errors
//                if (connection.getResponseCode() > 200) {
//                    return connection.getResponseCode() + ": connection failed";
//                }

                // Read content and close connection
                String line;
                reader = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                while ((line = reader.readLine()) != null) {
                    content.append(line);
                    Log.d("line", line);
                }
                reader.close();
                
                Log.d("content", content.toString());

                // Return status to onPostExecute
                return "connection succeeded";
            } catch (MalformedURLException e) {
                Log.e(TAG_URL, e.getMessage());
                e.printStackTrace();
                return "URL: connection failed";
            } catch (IOException e) {
                Log.e(TAG_URL, e.getMessage());
                e.printStackTrace();
                return "openConnection: connection failed";
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        SetHttpConnection setc = new SetHttpConnection();
        setc.execute();
        Log.d(TAG_C_P, "Button");
        Log.d(TAG_URL, getResources().getString(R.string.url));

    }
}