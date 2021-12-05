package com.codenrock.bcsgetlab.evloev.obligationcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private BufferedReader reader;
    private StringBuffer content;

    private URL url;
    private HttpURLConnection connection;

    private Button button_create_portfolio;
    private EditText period;
    private int periodValue;
    private EditText money;
    private int moneyValue;
    private TreeMap<Double, Obligation> obligations;

    private final String TAG_C_P="button_create_portfolio";
    private final String TAG_URL="set_connection";


    protected class Obligation {
        String SHORTNAME;
        Double YIELDATPREVWAPRICE;
        Double FACEVALUE;
        Double CouponYeild;

        public Obligation(String _shortname, Double _YIELDATPREVWAPRICE, Double _FACEVALUE) {
            SHORTNAME = _shortname;
            YIELDATPREVWAPRICE = _YIELDATPREVWAPRICE;
            FACEVALUE = _FACEVALUE;
        }

        public void setCouponYeild(Double couponYeild) {
            CouponYeild = couponYeild;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        content = new StringBuffer();
        obligations = new TreeMap<Double, Obligation>(Collections.reverseOrder());

        period = (EditText) findViewById(R.id.inputPeriodText);
        money = (EditText) findViewById(R.id.inputMoneyText);
        button_create_portfolio = (Button) findViewById(R.id.button_create_portfolio);
        button_create_portfolio.setOnClickListener(this);



        SetHttpConnection setConnection = new SetHttpConnection();
        setConnection.execute();

    }



    public TreeMap<Double, Obligation> parseJson(String responseBody, int periodValue) throws JSONException {
        JSONObject allData = new JSONObject(responseBody);
        JSONObject securities = allData.getJSONObject("securities");
        JSONArray data = securities.getJSONArray("data");

        try {
            for (int i = 0; i < data.length(); i++) {
                String SHORTNAME = data.getJSONArray(i).optString(2, null);
                double YIELDATPREVWAPRICE = data.getJSONArray(i).optDouble(4, 0);
                double COUPONVALUE = data.getJSONArray(i).optDouble(5, 0);
                String NEXTCOUPON = data.getJSONArray(i).optString(6, null);
                double ACCRUEDINT = data.getJSONArray(i).optDouble(7, 0);
                double PREVPRICE = data.getJSONArray(i).optDouble(8, 0);
                int LOTSIZE = data.getJSONArray(i).optInt(9, 0);
                double FACEVALUE = data.getJSONArray(i).optDouble(10, 0);
                String MATDATE = data.getJSONArray(i).optString(13, null);
                int COUPONPERIOD = data.getJSONArray(i).optInt(15, 0);
                if (YIELDATPREVWAPRICE == 0 || FACEVALUE == 0) continue;
                Double CouponYeild = (YIELDATPREVWAPRICE * 365) / (periodValue * FACEVALUE);
                obligations.put(CouponYeild, new Obligation(SHORTNAME, YIELDATPREVWAPRICE, FACEVALUE));

//                Log.d(i + "----" + "SHORTNAME", SHORTNAME);
//                Log.d(i + "----" + "YIELDATPREVWAPRICE", String.valueOf(YIELDATPREVWAPRICE));
//                Log.d(i + "----" + "COUPONVALUE", String.valueOf(COUPONVALUE));
//                Log.d(i + "----" + "NEXTCOUPON", NEXTCOUPON);
//                Log.d(i + "----" + "ACCRUEDINT", String.valueOf(ACCRUEDINT));
//                Log.d(i + "----" + "PREVPRICE", String.valueOf(PREVPRICE));
//                Log.d(i + "----" + "LOTSIZE", String.valueOf(LOTSIZE));
//                Log.d(i + "----" + "FACEVALUE", String.valueOf(FACEVALUE));
//                Log.d(i + "----" + "MATDATE", MATDATE);
//                Log.d(i + "----" + "COUPONPERIOD", String.valueOf(COUPONPERIOD));
            }
        } catch (Exception e) {
            // TODO: handle null data
        }


        return obligations;
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
                }
                reader.close();

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
            } finally {
                connection.disconnect();
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

    public void createProfile(int periodValue, int moneyValue){
        try {
            // TODO: set stable connection befor parsing json
            if (!content.toString().isEmpty())
                parseJson(content.toString(), periodValue);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (Map.Entry<Double, Obligation> o : obligations.entrySet()) {
            Log.d("CouponYeild", String.valueOf(o.getKey()));
            Log.d("SHORTNAME", o.getValue().SHORTNAME);
            Log.d("FACEVALUE", String.valueOf(o.getValue().FACEVALUE));
            Log.d("YIELDATPREVWAPRICE", String.valueOf(o.getValue().YIELDATPREVWAPRICE));
        }
    }

    @Override
    public void onClick(View v) {


        //Create profile
        periodValue = Integer.valueOf(period.getText().toString());
        moneyValue = Integer.valueOf(money.getText().toString());
        if (periodValue == 0 || moneyValue == 0) return;
        createProfile(periodValue, moneyValue);

        Log.d(TAG_C_P, "Button");
        Log.d(TAG_URL, getResources().getString(R.string.url));
        Log.d("Input period", period.getText().toString());
        Log.d("Input money", money.getText().toString());
    }
}