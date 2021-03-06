package com.example.prakharagarwal.automaticirrigation;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MasterControlFragment extends Fragment {

    DBAdapter dba;
    int flag=0;
    static TextView statustext;
    static Switch status_switch;
    //static ImageView masterImage;
    public MasterControlFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_control, container, false);
        dba= new DBAdapter(getActivity().getApplicationContext());
        try {

            dba.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        statustext=(TextView)rootView.findViewById(R.id.mastercontrol_status);
        status_switch=(Switch)rootView.findViewById(R.id.mastercontrol_switch);

        //masterImage=(ImageView)rootView.findViewById(R.id.image_master);
        flag=0;
        int currentStatus= dba.getCurrentStatus();

        int i=getContext().getResources().getIdentifier("ic_add_white_24dp","drawable",getContext().getPackageName());


        if(currentStatus==0){
            status_switch.setChecked(false);
            statustext.setText("OFF");
            //masterImage.setImageDrawable(getResources().getDrawable(R.drawable.personnotwateringaplant));
            flag=1;
        }else{
            status_switch.setChecked(true);
            statustext.setText("ONN");
            //masterImage.setImageDrawable(getResources().getDrawable(R.drawable.personwateringaplant));
            flag=1;
        }



        status_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(flag==0){

                }else{

                if(status_switch.isChecked()==true){
                   new SyncTask_PUT().execute(1);
                }else{
                    new SyncTask_PUT().execute(0);
                }
                }
            }
        });
        

        return rootView;
    }



    public class SyncTask_PUT extends AsyncTask<Integer, Void, String> {

        private final String LOG_TAG = SyncTask_PUT.class.getName();



        @Override
        protected  String doInBackground(Integer... status) {


            String jsonString;
            BufferedReader reader;

            try {

                //String link = "http://homegenie.gear.host/db_put.php";
                String link="http://192.168.43.180:8888/automaticirrigation/db_put.php";

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

                String resmsg=jsonObject.getString("status");
                Log.d("check", resmsg);
                if (resmsg.equals("1")) {

                    Toast.makeText(getActivity(), "Water Pump is now Running", Toast.LENGTH_SHORT).show();
                    MasterControlFragment.statustext.setText("ONN");
                    //masterImage.setImageDrawable(getResources().getDrawable(R.drawable.personwateringaplant));
                }else if(resmsg.equals("0")){
                    Toast.makeText(getActivity(), " Water Pump is now Stopped", Toast.LENGTH_SHORT).show();
                    MasterControlFragment.statustext.setText("OFF");
                   // masterImage.setImageDrawable(getResources().getDrawable(R.drawable.personnotwateringaplant));
                }

            }catch (Exception e){

            }


        }


    }

}
