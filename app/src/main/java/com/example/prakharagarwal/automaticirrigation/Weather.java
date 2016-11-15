package com.example.prakharagarwal.automaticirrigation;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Weather extends Fragment {


    private ImageView mIconView;
    private TextView mFriendlyDateView;
    private TextView mRainView;
    private TextView mCityView;
    private TextView mDescriptionView;
    private TextView mHighTempView;
    private TextView mLowTempView;
    private TextView mHumidityView;
    private TextView mWindView;
    private TextView mPressureView;
    DBAdapter dba;



    public Weather() {
        // Required empty public constructor
    }

    private ArrayAdapter<String> mForecastAdapter;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        dba= new DBAdapter(getActivity().getApplicationContext());

        View rootView = inflater.inflate(R.layout.fragment_weather, container, false);
        mIconView = (ImageView) rootView.findViewById(R.id.detail_icon);
        mRainView = (TextView) rootView.findViewById(R.id.detail_rain_textview);
        mCityView = (TextView) rootView.findViewById(R.id.detail_city_textview);
        mFriendlyDateView = (TextView) rootView.findViewById(R.id.detail_day_textview);
        mDescriptionView = (TextView) rootView.findViewById(R.id.detail_forecast_textview);
        mHighTempView = (TextView) rootView.findViewById(R.id.detail_high_textview);
        mLowTempView = (TextView) rootView.findViewById(R.id.detail_low_textview);
        mHumidityView = (TextView) rootView.findViewById(R.id.detail_humidity_textview);
        mWindView = (TextView) rootView.findViewById(R.id.detail_wind_textview);
        mPressureView = (TextView) rootView.findViewById(R.id.detail_pressure_textview);

        int i=0;
        Cursor c= dba.showWeather();

       // Cursor c=dba.showWeather();
        int count=c.getCount();
        if(count==0)
            updateWeather();

        else if(!compareDate(dba.getDate()))
            updateWeather();

        else {

            WeatherData[] result;

            result = new WeatherData[count];

            while (c.moveToNext()) {
                result[i] = new WeatherData(c.getInt(5), c.getString(0), c.getInt(1), c.getString(2), c.getDouble(3), c.getString(4), c.getString(5), c.getDouble(7), c.getDouble(8), c.getFloat(9), c.getDouble(10), c.getDouble(11), c.getDouble(12));
            /*mCityView.setText(c.getString(1));
            mRainView.setText("Precipitation: "+c.getString(2)+" mm");
            mFriendlyDateView.setText(result[0].friendlyDateText);
            Picasso.with(getContext()).load(result[0].icon).into(mIconView);
            mDescriptionView.setText(result[0].description);
            mHighTempView.setText("Max: "+result[0].high);
            mLowTempView.setText("Min: "+result[0].low);
            mHumidityView.setText("Humidity: "+result[0].humidity+"%");
            mWindView.setText("Wind: "+getFormattedWind(result[0].windSpeedStr,result[0].windDirStr));
            mPressureView.setText("Pressure: "+result[0].pressure);

*/
                i++;
            }




            mCityView.setText("" + result[0].city);
            mRainView.setText("Precipitation: " + result[0].rain + " mm");
            mFriendlyDateView.setText(result[0].friendlyDateText);
            String iconString = "i" + result[0].icon;
            int iconID = getContext().getResources().getIdentifier(iconString, "drawable", getContext().getPackageName());
            mIconView.setImageDrawable(getContext().getResources().getDrawable(iconID));

            //Picasso.with(getContext()).load(result[0].icon).into(mIconView);
            mDescriptionView.setText(result[0].description);
            mHighTempView.setText("Max: " + result[0].high);
            mLowTempView.setText("Min: " + result[0].low);
            mHumidityView.setText("Humidity: " + result[0].humidity + "%");
            mWindView.setText("Wind: " + getFormattedWind(result[0].windSpeedStr, result[0].windDirStr));
            mPressureView.setText("Pressure: " + result[0].pressure);
        }
        return rootView;
    }


    public String getFormattedWind(double windSpeed, double degrees) {

        // From wind direction in degrees, determine compass direction as a string (e.g NW)
        // You know what's fun, writing really long if/else statements with tons of possible
        // conditions.  Seriously, try it!
        String direction = "Unknown";
        if (degrees >= 337.5 || degrees < 22.5) {
            direction = "N";
        } else if (degrees >= 22.5 && degrees < 67.5) {
            direction = "NE";
        } else if (degrees >= 67.5 && degrees < 112.5) {
            direction = "E";
        } else if (degrees >= 112.5 && degrees < 157.5) {
            direction = "SE";
        } else if (degrees >= 157.5 && degrees < 202.5) {
            direction = "S";
        } else if (degrees >= 202.5 && degrees < 247.5) {
            direction = "SW";
        } else if (degrees >= 247.5 && degrees < 292.5) {
            direction = "W";
        } else if (degrees >= 292.5 && degrees < 337.5) {
            direction = "NW";
        }
        return windSpeed+" kmph "+direction;
    }

    public boolean compareDate(long dateInMillis) {
        // If the date is today, return the localized version of "Today" instead of the actual
        // day name.

        Time t = new Time();
        t.setToNow();
        int julianDay = Time.getJulianDay(dateInMillis, t.gmtoff);
        int currentJulianDay = Time.getJulianDay(System.currentTimeMillis(), t.gmtoff);
        if (julianDay == currentJulianDay) {
            return true;
        } else
            return false;

    }


    private void updateWeather() {
        mForecastAdapter =
                new ArrayAdapter<String>(
                        getActivity(), // The current context (this activity)
                        R.layout.listitem_weather, // The name of the layout ID.
                        R.id.list_item_forcast_textview, // The ID of the textview to populate
                        new ArrayList<String>());
        FetchWeatherTask weatherTask = new FetchWeatherTask();
       weatherTask.execute("110088");
    }

    @Override
    public void onStart() {
        super.onStart();
         }

    public class FetchWeatherTask extends AsyncTask<String, Void, WeatherData> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        /* The date/time conversion code is going to be moved outside the asynctask later,
         * so for convenience we're breaking it out into its own method now.
         */


        private String getReadableDateString(long time){
            // Because the API returns a unix timestamp (measured in seconds),
            // it must be converted to milliseconds in order to be converted to valid date.
            SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
            return shortenedDateFormat.format(time);
        }

        public String getFormattedWind(double windSpeed, double degrees) {

            // From wind direction in degrees, determine compass direction as a string (e.g NW)
            // You know what's fun, writing really long if/else statements with tons of possible
            // conditions.  Seriously, try it!
            String direction = "Unknown";
            if (degrees >= 337.5 || degrees < 22.5) {
                direction = "N";
            } else if (degrees >= 22.5 && degrees < 67.5) {
                direction = "NE";
            } else if (degrees >= 67.5 && degrees < 112.5) {
                direction = "E";
            } else if (degrees >= 112.5 && degrees < 157.5) {
                direction = "SE";
            } else if (degrees >= 157.5 && degrees < 202.5) {
                direction = "S";
            } else if (degrees >= 202.5 && degrees < 247.5) {
                direction = "SW";
            } else if (degrees >= 247.5 && degrees < 292.5) {
                direction = "W";
            } else if (degrees >= 292.5 && degrees < 337.5) {
                direction = "NW";
            }
            return windSpeed+" kmph "+direction;
        }


        private WeatherData getWeatherDataFromJson(String forecastJsonStr, int numDays)
                throws JSONException {

            // Location information
            final String OWM_CITY = "city";
            final String OWM_CITY_NAME = "name";
            final String OWM_COORD = "coord";

            // Location coordinate
            final String OWM_LATITUDE = "lat";
            final String OWM_LONGITUDE = "lon";

            // Weather information.  Each day's forecast info is an element of the "list" array.
            final String OWM_LIST = "list";

            final String OWM_PRESSURE = "pressure";
            final String OWM_HUMIDITY = "humidity";
            final String OWM_WINDSPEED = "speed";
            final String OWM_WIND_DIRECTION = "deg";
            final String OWM_RAIN= "rain";

            // All temperatures are children of the "temp" object.
            final String OWM_TEMPERATURE = "temp";
            final String OWM_MAX = "max";
            final String OWM_MIN = "min";

            final String OWM_WEATHER = "weather";
            final String OWM_ICON = "icon";
            final String OWM_DESCRIPTION = "main";
            final String OWM_WEATHER_ID = "id";


            try {
                JSONObject forecastJson = new JSONObject(forecastJsonStr);
                JSONArray weatherArray = forecastJson.getJSONArray(OWM_LIST);

                JSONObject cityJson = forecastJson.getJSONObject(OWM_CITY);
                String cityName = cityJson.getString(OWM_CITY_NAME);

                JSONObject cityCoord = cityJson.getJSONObject(OWM_COORD);
                double cityLatitude = cityCoord.getDouble(OWM_LATITUDE);
                double cityLongitude = cityCoord.getDouble(OWM_LONGITUDE);

                Time dayTime = new Time();
                dayTime.setToNow();

                // we start at the day returned by local time. Otherwise this is a mess.
                int julianStartDay = Time.getJulianDay(System.currentTimeMillis(), dayTime.gmtoff);

                // now we work exclusively in UTC
                dayTime = new Time();

                for(int i = 0; i < weatherArray.length(); i++) {
                    // These are the values that will be collected.
                    long dateTime;
                    String friendlyDate;

                    double pressure;
                    int humidity;
                    double windSpeed;
                    double windDirection;
                    double rain=0.00;

                    double high;
                    double low;

                    String icon;
                    String description;
                    int weatherId;

                    // Get the JSON object representing the day
                    JSONObject dayForecast = weatherArray.getJSONObject(i);

                    // Cheating to convert this to UTC time, which is what we want anyhow
                    dateTime = dayTime.setJulianDay(julianStartDay+i);

                    friendlyDate=getReadableDateString(dateTime);
                    pressure = dayForecast.getDouble(OWM_PRESSURE);
                    humidity = dayForecast.getInt(OWM_HUMIDITY);
                    windSpeed = dayForecast.getDouble(OWM_WINDSPEED);
                    windDirection = dayForecast.getDouble(OWM_WIND_DIRECTION);

                    if(dayForecast.has(OWM_RAIN))
                      rain=dayForecast.getDouble(OWM_RAIN);


                    // Description is in a child array called "weather", which is 1 element long.
                    // That element also contains a weather code.
                    JSONObject weatherObject =
                            dayForecast.getJSONArray(OWM_WEATHER).getJSONObject(0);
                    //icon="http://openweathermap.org/img/w/"+weatherObject.getString(OWM_ICON)+".png";
                    icon=weatherObject.getString(OWM_ICON);//+".png";

                    description = weatherObject.getString(OWM_DESCRIPTION);
                    weatherId = weatherObject.getInt(OWM_WEATHER_ID);

                    // Temperatures are in a child object called "temp".  Try not to name variables
                    // "temp" when working with temperature.  It confuses everybody.
                    JSONObject temperatureObject = dayForecast.getJSONObject(OWM_TEMPERATURE);
                    high = temperatureObject.getDouble(OWM_MAX);
                    low = temperatureObject.getDouble(OWM_MIN);
                    WeatherData weatherValues = new WeatherData(weatherId,cityName,dateTime,friendlyDate,rain,icon,description ,high,low,humidity ,windSpeed,windDirection,pressure);


                    return weatherValues;
                }

                Log.d(LOG_TAG, "FetchWeatherTask Complete.  Inserted");

            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected WeatherData doInBackground(String... params) {

            // If there's no zip code, there's nothing to look up.  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            String format = "json";
            String units = "metric";
            int numDays = 1;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String FORECAST_BASE_URL =
                        "http://api.openweathermap.org/data/2.5/forecast/daily?";
                final String QUERY_PARAM = "q";
                final String FORMAT_PARAM = "mode";
                final String UNITS_PARAM = "units";
                final String DAYS_PARAM = "cnt";
                final String APPID_PARAM = "APPID";

                Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, params[0])
                        .appendQueryParameter(FORMAT_PARAM, format)
                        .appendQueryParameter(UNITS_PARAM, units)
                        .appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
                        .appendQueryParameter(APPID_PARAM, "cb46fcc1d00c4a44ef1f4f163b4a695c")
                        .build();

                URL url = new URL(builtUri.toString());

                Log.v(LOG_TAG, "Built URI " + builtUri.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();

                Log.v(LOG_TAG, "Forecast string: " + forecastJsonStr);
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getWeatherDataFromJson(forecastJsonStr, numDays);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(WeatherData result) {
            if (result != null) {

                dba.update_weatheri(result.weatherId,result.city,result.date,result.friendlyDateText,result.rain,result.icon, result.description ,result.high,result.low,result.humidity ,result.windSpeedStr,result.windDirStr,result.pressure);

                try {

                    mCityView.setText("" + result.city);
                    mRainView.setText("Precipitation: " + result.rain + " mm");
                    mFriendlyDateView.setText(result.friendlyDateText);
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/"+result.icon+".png").into(mIconView);
                    mDescriptionView.setText(result.description);
                    mHighTempView.setText("Max: " + result.high);
                    mLowTempView.setText("Min: " + result.low);
                    mHumidityView.setText("Humidity: " + result.humidity + "%");
                    mWindView.setText("Wind: " + getFormattedWind(result.windSpeedStr, result.windDirStr));
                    mPressureView.setText("Pressure: " + result.pressure);
                }
                catch (Exception e){}
            }
        }
    }

}
