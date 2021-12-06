package com.example.weatherapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
{
    private EditText editText;
    private Button submit;
    private TextView currTempText, minTempTxt, maxTempTxt, feelsLikeTxt, pressureTxt, humidityTxt, currWeatherTxt;
    private RequestQueue requestQueue;
    private double currTemp;
    private double minTemp;
    private double maxTemp;
    private double feels_like;
    private int getPressure;
    private int getHumidity;
    private String getWeather;
    private String API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        requestQueue = Volley.newRequestQueue(this);
        editText = findViewById(R.id.etWeather);
        submit = findViewById(R.id.submit);
        currTempText = findViewById(R.id.showTempTxt);
        minTempTxt = findViewById(R.id.showTempMinTxt);
        maxTempTxt = findViewById(R.id.showTempMaxTxt);
        feelsLikeTxt = findViewById(R.id.feels_like);
        pressureTxt = findViewById(R.id.pressureTxt);
        humidityTxt = findViewById(R.id.humidityTxt);
        currWeatherTxt = findViewById(R.id.currWeatherTxt);
        API_KEY = "6fb075d467cec6f9dde520a3383b91ee";


        submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String getCityName = editText.getText().toString();

                if (TextUtils.isEmpty(getCityName))
                {
                    editText.setError("Please enter city name");
                } else
                {
                    String url = "https://api.openweathermap.org/data/2.5/weather?q=" + getCityName + "&appid="+API_KEY;

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>()
                    {
                        @Override
                        public void onResponse(JSONObject response)
                        {
                            try
                            {
                                JSONObject jsonObject = response.getJSONObject("main");

                                // current temp
                                double getCurrWeather = jsonObject.getDouble("temp");
                                currTemp = getCurrWeather - 273.15;
                                currTempText.setTypeface(null, Typeface.BOLD);
                                currTempText.setText(Double.toString(currTemp).substring(0, 4) + " °C");


                                // min. temp
                                double getMinTemp = jsonObject.getDouble("temp_min");
                                minTemp = getMinTemp - 273.15;
                                minTempTxt.setTypeface(null, Typeface.BOLD);
                                minTempTxt.setText(Double.toString(minTemp).substring(0, 4) + " °C");


                                // max temp
                                double getMaxTemp = jsonObject.getDouble("temp_max");
                                maxTemp = getMaxTemp - 273.15;
                                maxTempTxt.setTypeface(null, Typeface.BOLD);
                                maxTempTxt.setText(Double.toString(maxTemp).substring(0, 4) + " °C");

                                // feels like
                                double getFeelsLike = jsonObject.getDouble("feels_like");
                                feels_like = getFeelsLike - 273.15;
                                feelsLikeTxt.setTypeface(null, Typeface.BOLD);
                                feelsLikeTxt.setText(Double.toString(feels_like).substring(0, 4) + " °C");

                                // pressure
                                getPressure = jsonObject.getInt("pressure");
                                pressureTxt.setTypeface(null, Typeface.BOLD);
                                pressureTxt.setText(getPressure + " hPa");                  // hPa = hecto Pascal

                                // humidity
                                getHumidity = jsonObject.getInt("humidity");
                                humidityTxt.setTypeface(null, Typeface.BOLD);
                                humidityTxt.setText(getHumidity + " %");

                                // current weather
                                JSONArray weather = response.getJSONArray("weather");
                                JSONObject jsonObject1 = weather.getJSONObject(0);
                                getWeather = jsonObject1.getString("main");
                                currWeatherTxt.setTypeface(null, Typeface.BOLD);
                                currWeatherTxt.setText(getWeather);
                            } catch (JSONException jsonException)
                            {
                                jsonException.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener()
                    {
                        @Override
                        public void onErrorResponse(VolleyError error)
                        {
                            Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            DefaultRetryPolicy.DEFAULT_TIMEOUT_MS * 2,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
                    ));
                    requestQueue.add(jsonObjectRequest);
                }
            }
        });
    }
}