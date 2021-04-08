package com.example.nationinfoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private Button btnLoad;
    private List<Country> countries;
    private ListView listView;
    private AdapterCountry adapterCountry;
    private ProgressDialog dialog;

    private String username = "username123321";
    private String path = "http://api.geonames.org/countryInfoJSON?username=" + username;
    private String urlImage = "https://img.geonames.org/flags/m/";

    private StringBuffer response;
    private String responseText;

    private View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addControls();
        addEvents();

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.layoutToast));
    }

    private void addEvents() {
        btnLoad.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                countries.clear();
                adapterCountry.notifyDataSetChanged();
                new getDataService().execute();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Country c = countries.get(position);
                if (layout == null)
                    Log.d("Tag", "Null");

                TextView tv = layout.findViewById(R.id.txtName);
                tv.setText(c.getCountryName());
                ((TextView) layout.findViewById(R.id.txtArea)).setText(c.getArea());
                ((TextView) layout.findViewById(R.id.txtPopulation)).setText(c.getPopulation() + "");

                try {
                    new AsyncTask<String, Void, Bitmap>() {
                        @Override
                        protected Bitmap doInBackground(String... strings) {
                            InputStream in = connectWeb(urlImage + strings[0]);
                            return BitmapFactory.decodeStream(in);
                        }

                        @Override
                        protected void onPostExecute(Bitmap bitmap) {
                            super.onPostExecute(bitmap);
                            ((ImageView) layout.findViewById(R.id.imgFlag)).setImageBitmap(bitmap);
                        }
                    }.execute(c.getCountryCode().toLowerCase() + ".png");


                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
    }

    private void addControls() {
        btnLoad = findViewById(R.id.btnLoad);
        countries = new ArrayList<>();
        listView = findViewById(R.id.listCountry);
        adapterCountry = new AdapterCountry(countries);
        listView.setAdapter(adapterCountry);

        response = new StringBuffer();
    }

    class getDataService extends AsyncTask {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            return getWebServiceResposeData();
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            adapterCountry.notifyDataSetChanged();
            if (dialog.isShowing())
                dialog.dismiss();
        }
    }

    private InputStream connectWeb(String path) {

        try {
            URL url = new URL(path);
            Log.d(ContentValues.TAG, "ServerData: " + path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");

            int responceCode = conn.getResponseCode();
            if (responceCode == HttpURLConnection.HTTP_OK) {
                return conn.getInputStream();
            }
            return null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Object getWebServiceResposeData() {
        try {
            InputStream input = connectWeb(path);
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            String output;

            while ((output = in.readLine()) != null) {
                response.append(output);
            }


            in.close();
            responseText = response.toString();
            JSONObject obj = new JSONObject(responseText);
            JSONArray arr = obj.getJSONArray("geonames");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject json = arr.getJSONObject(i);
                Country c = new Country();
                c.setCountryName(json.getString("countryName"));
                c.setArea(json.getString("areaInSqKm"));
                c.setPopulation(Integer.parseInt(json.getString("population")));
                c.setCountryCode(json.getString("countryCode"));

                countries.add(c);

            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

}