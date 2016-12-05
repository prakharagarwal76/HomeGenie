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
            /*double rainThreshold=dba.getRain();
            if(rainThreshold>=4){
                dba.updateStatus(4,name);
                msg="Heavy rainfall detected, watering cancelled";
                showNotification(context,msg);
            }
            else {*/
                dba.updateCurrentStatus(1);
                msg="Water pump is going to start";
                showNotification(context,msg);
                MasterControlFragment.status_switch.setChecked(true);
            //}

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


}
