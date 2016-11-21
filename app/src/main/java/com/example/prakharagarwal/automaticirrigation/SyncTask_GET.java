package com.example.prakharagarwal.automaticirrigation;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by prakharagarwal on 15/11/16.
 */
public class SyncTask_GET extends AsyncTask<Void, Void, String> {


    private final String LOG_TAG = SyncTask_GET.class.getName();
    DBAdapter dba;
    public SyncTask_GET(DBAdapter dba){
        this.dba=dba;
    }

    @Override
    protected String doInBackground(Void... s) {


        String jsonString;
        BufferedReader reader;

        try {

            //String link = "http://homegenie.gear.host/db_get1.php";
            //String link="http://192.168.43.180:8888/automaticirrigation/db_get1.php";
            String link="http://10.0.2.2:8888/automaticirrigation/db_get1.php";

            ///     String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
            //  data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


            URL url = new URL(link);
            HttpURLConnection conn = null;


            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");



            //conn.setFixedLengthStreamingMode(message.getBytes().length);

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");



            InputStream inputStream = conn.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                jsonString = null;
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
                jsonString = null;
            }
            jsonString = buffer.toString();
            Log.d("json",jsonString);
            return jsonString;


            //urlConnection.connect();

        } catch (Exception e) {
            Log.e(LOG_TAG, "Error ", e);
            return "";
        } finally {
            // urlConnection.disconnect();
        }

    }
    protected void onPostExecute(String result) {
        Log.d("result", result);
        try {

            try {

                dba.open();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject= new JSONObject(result);

            String resmsg=jsonObject.getString("status");
            Log.d("check", resmsg);
            if (resmsg.equals("status1")) {
               dba.updateCurrentStatus(1);
            }else{
                dba.updateCurrentStatus(0);
            }

        }catch (Exception e){

        }

    }


}

