package com.example.prakharagarwal.automaticirrigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by prakharagarwal on 15/11/16.
 */
public class PresetBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "yoyo", Toast.LENGTH_SHORT).show();
        new SyncTask_PUT().execute(1);
    }
    public class SyncTask_PUT extends AsyncTask<Integer, Void, String> {

        private final String LOG_TAG = SyncTask_PUT.class.getName();



        @Override
        protected  String doInBackground(Integer... status) {


            String jsonString;
            BufferedReader reader;

            try {

                String link = "http://homegenie.gear.host/db_put.php";
                ///     String data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                //  data += "&" + URLEncoder.encode("pwd", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");


                URL url = new URL(link);
                HttpURLConnection conn = null;

                JSONObject data = new JSONObject();
                data.put("status", status[0]);

                JSONObject jsonObject = new JSONObject();

                jsonObject.put("data", data);
                String message = jsonObject.toString();
                Log.d("json", message);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");



                conn.setFixedLengthStreamingMode(message.getBytes().length);

                conn.setDoOutput(true);

                conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");


                OutputStream os = new BufferedOutputStream(conn.getOutputStream());

                os.write(message.getBytes());


                os.flush();

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
                JSONObject jsonObject= new JSONObject(result);

                String resmsg=jsonObject.getString("status");
                Log.d("check", resmsg);
                if (resmsg.equals("1")) {


                    MasterControlFragment.statustext.setText("ONN");
                }else if(resmsg.equals("0")){

                    MasterControlFragment.statustext.setText("OFF");
                }

            }catch (Exception e){

            }


        }


    }
}
