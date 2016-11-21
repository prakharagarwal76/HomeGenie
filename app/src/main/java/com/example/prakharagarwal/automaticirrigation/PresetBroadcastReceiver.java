package com.example.prakharagarwal.automaticirrigation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NotificationCompat;
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
import java.sql.SQLException;

/**
 * Created by prakharagarwal on 15/11/16.
 */
public class PresetBroadcastReceiver extends BroadcastReceiver {
    DBAdapter dba;
    String name;
    String msg;
    int position;
    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context, "yoyo1", Toast.LENGTH_SHORT).show();
        dba= new DBAdapter(context);
        int status=intent.getIntExtra("status",-1);
        name=intent.getStringExtra("name");
        position=intent.getIntExtra("position",-1);
        if(status==1){
            double rainThreshold=dba.getRain();
            if(rainThreshold>=4){
                dba.updateStatus(4,name);
                msg="Heavy rainfall detected, watering cancelled";
                showNotification(context,msg);
            }
            else {
                dba.updateCurrentStatus(1);
                msg="Water pump is going to start";
                showNotification(context,msg);
                MasterControlFragment.status_switch.setChecked(true);
            }

        }else{
            msg="Water pump stopped";
            dba.updateCurrentStatus(0);
            dba.updateStatus(3,name);
            showNotification(context,msg);
            MasterControlFragment.status_switch.setChecked(false);
        }


        //Log.e("intent:",name);
        //int status=Integer.parseInt(intent.getStringExtra("status"));
        //new SyncTask_PUT().execute(status);
    }

    private void showNotification(Context context,String message) {
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), 0);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.home)
                        .setContentTitle("HomeGenie")
                        .setContentText(message);
        mBuilder.setContentIntent(contentIntent);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1, mBuilder.build());
    }
    public class SyncTask_PUT extends AsyncTask<Integer, Void, String> {

        private final String LOG_TAG = SyncTask_PUT.class.getName();



        @Override
        protected  String doInBackground(Integer... status) {


            String jsonString;
            BufferedReader reader;

            try {

                String link = "http://homegenie.gear.host/db_put.php";

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

                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {

                    jsonString = null;
                }
                jsonString = buffer.toString();
                Log.d("json",jsonString);
                return jsonString;

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
                try {

                    dba.open();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                String resmsg=jsonObject.getString("status");
                Log.d("check", resmsg);
                if (resmsg.equals("1")) {
                    dba.updateCurrentStatus(1);
                    MasterControlFragment.status_switch.setChecked(true);
                    MasterControlFragment.statustext.setText("ONN");
                    MasterControlFragment.masterImage.setImageResource(R.drawable.personwateringaplant);
                }else if(resmsg.equals("0")){
                    dba.updateCurrentStatus(0);
                    dba.updateStatus(3,name);
                    MasterControlFragment.status_switch.setChecked(false);
                    MasterControlFragment.statustext.setText("OFF");
                    MasterControlFragment.masterImage.setImageResource(R.drawable.personnotwateringaplant);
                }

            }catch (Exception e){

            }


        }


    }

}
