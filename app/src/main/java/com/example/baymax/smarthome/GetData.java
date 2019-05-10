package com.example.baymax.smarthome;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class GetData extends AppCompatActivity {
    TextView selectCity, cityField, homeH,homeT, cityH, weatherIcon, updatedField,cityT;
    ProgressBar loader;
    Typeface weatherFont;
    String city = "Ha Noi";
    DatabaseReference myData;
    /* Please Put your API KEY here */
    String OPEN_WEATHER_MAP_API = "5619344e16203ce3adac5e61503eaed2";
    /* Please Put your API KEY here */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_data);
        loader = (ProgressBar) findViewById(R.id.loader);
        selectCity = (TextView) findViewById(R.id.selectCity);
        cityField = (TextView) findViewById(R.id.city_field);
        updatedField = (TextView) findViewById(R.id.updated_field);
        weatherIcon = (TextView) findViewById(R.id.weather_icon);
        homeH=findViewById(R.id.textView5);
        homeT=findViewById(R.id.textView6);
        cityH=findViewById(R.id.textView3);
        cityT=findViewById(R.id.textView4);
        weatherFont = Typeface.createFromAsset(getAssets(), "fonts/weathericons-regular-webfont.ttf");
        weatherIcon.setTypeface(weatherFont);

        taskLoadUp(city);
        myData= FirebaseDatabase.getInstance().getReference();
        myData.child("humidity").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model2 temp=dataSnapshot.getValue(Model2.class);
                homeH.setText( temp.value + " % ");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        myData.child("temperature").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Model2 temp=dataSnapshot.getValue(Model2.class);
                homeT.setText( temp.value + "°");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        selectCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(GetData.this);
                alertDialog.setTitle("Change City");
                final EditText input = new EditText(GetData.this);
                input.setText(city);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                city = input.getText().toString();
                                taskLoadUp(city);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });
    }
    public void taskLoadUp(String query) {
        if (Function.isNetworkAvailable(getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }



    class DownloadWeather extends AsyncTask< String, Void, String > {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }
        protected String doInBackground(String...args) {
            String xml = Function.excuteGet("http://api.openweathermap.org/data/2.5/weather?q=" + args[0] +
                    "&units=metric&appid=" + OPEN_WEATHER_MAP_API);
            return xml;
        }
        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    cityT.setText(String.format("%.2f", main.getDouble("temp")) + "°");
                    cityH.setText( main.getString("humidity") + "%");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(Function.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Error, Check City", Toast.LENGTH_SHORT).show();
            }
        }

    }
}