package com.example.prakharagarwal.automaticirrigation;

/**
 * Created by prakharagarwal on 30/09/16.
 */
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

public class SetMasterControlAsync  extends AsyncTask<String,Void,String> {
    private Context context;


    //flag 0 means get and 1 means post.(By default it is get.)



    protected void onPreExecute() {
        Log.d("connecting","connecting to server");
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {

            
            //String link = context.getString(R.string.url1);

           // URL url = new URL(link);
            HttpURLConnection conn = null;
            JSONObject header = new JSONObject();
            header.put("apisecurityuser","pruapi@kanedu.in");
            header.put("apisecuritypwd","pru@123");
            header.put("apiversion", "1.0");
            JSONObject data = new JSONObject();
            //data.put("userid", username);
            data.put("request_id", 1);
            //data.put("pwd", password);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("header", header);
            jsonObject.put("data", data);
            String message = jsonObject.toString();
            Log.d("json", message);
           // conn=(HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setFixedLengthStreamingMode(message.getBytes().length);

            conn.setDoOutput(true);

            conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");

            conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            // Log.d("hello", "you are here1");
            OutputStream os=new BufferedOutputStream(conn.getOutputStream());

            //Log.d("hello", "you are here2");
            os.write(message.getBytes());

            //  Log.d("hello", "you are here3");
            os.flush();

            os.close();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
            String responsemsg=conn.getResponseMessage();
            //Log.d("hello", "you are here4");
            String line = null;
            StringBuilder sb = new StringBuilder();

            // Read Server Response

            while ((line = reader.readLine()) != null) {
                Log.d("in while:", sb.toString());
                sb.append(line+"\n");

            }
            reader.close();
            //Log.d("line:", responsemsg);
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("result", result);
        try {
            JSONObject jsonObject1 = new JSONObject(result);
            //  Log.d("result", "hello");
            JSONObject jsonObject=jsonObject1.getJSONObject("header");
            int rspcod=jsonObject.getInt("responsecode");
            String resmsg=jsonObject.getString("responsemsg");
            //String errcnt=jsonObject.getString("errordetail");
            //String errdtl=jsonObject.getString("errdtl");

            //int walletid=jsonObject.getInt("id");
            Log.d("resmsg",resmsg);
            //System.out.print(rspcod);
            if(rspcod==0) {


                //} else if (rspcod==1)
            }else
            {


            }
            //else
            //  Log.d("Error in rspcod",result);

        }
        catch (Exception e)
        {
            Log.d("Json Exception",e.toString());

        }
    }
}