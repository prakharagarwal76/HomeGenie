package com.example.prakharagarwal.automaticirrigation;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by prakharagarwal on 03/11/16.
 */
public class PresetTime_Adapter extends ArrayAdapter<String>
{

    private Context context;
    private List<String> name;
    private List<String> starttime;
    private List<String> stoptime;
    private List<String> status;
    DBAdapter dba ;
    int flag;



    public PresetTime_Adapter(Context context, List<String> name,List<String> starttime,List<String> stoptime,List<String> status)
    {
        super(context,0,name);
        this.context = context;
        this.name = new ArrayList<String>();
        this.starttime = new ArrayList<String>();
        this.stoptime = new ArrayList<String>();
        this.status = new ArrayList<String>();

        this.name = name;
        this.starttime=starttime;
        this.stoptime=stoptime;
        this.status=status;


    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview_preset, parent, false);
        }

        dba= new DBAdapter(getContext());
        try {

            dba.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TextView t1 = (TextView) convertView.findViewById(R.id.list_item_preset_text1);
        TextView t2 = (TextView) convertView.findViewById(R.id.list_item_preset_text2);
        TextView t3 = (TextView) convertView.findViewById(R.id.list_item_preset_text3);
        final ImageView t4 = (ImageView) convertView.findViewById(R.id.list_item_preset_image);
        flag=0;



        t1.setText(name.get(position));
        t2.setText(starttime.get(position));
        t3.setText(stoptime.get(position));
        if(status.get(position).equals("1")){
            flag=1;
            t4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_alarm_on_black_24dp));
        }
        t4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==0){

                    t4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_alarm_on_black_24dp));
                    dba.updateStatus(1,name.get(position));
                    Calendar cal1=Calendar.getInstance();
                    Calendar cal2=Calendar.getInstance();
                    SimpleDateFormat sdf=new SimpleDateFormat("d/M/yyyy H:m");
                   try {Date date=sdf.parse(starttime.get(position));
                       cal1.setTime(date);
                       Log.e("date",cal1+"");
                   }catch (ParseException e){

                   }
                    Intent intent = new Intent(getContext(), PresetBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getContext(),position, intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP,cal1.getTimeInMillis(), pendingIntent);
                    flag=1;



                }else{
                    t4.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_alarm_off_black_24dp));
                    dba.updateStatus(0,name.get(position));
                    Intent intent = new Intent(getContext(), PresetBroadcastReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                            getContext(),position, intent, 0);
                    AlarmManager alarmManager = (AlarmManager)getContext().getSystemService(getContext().ALARM_SERVICE);
                    alarmManager.cancel(pendingIntent);
                    flag=0;
                }
            }
        });







        return convertView;
    }

    public void removeAll(int listItemPosition) {
        name.remove(listItemPosition);
        starttime.remove(listItemPosition);
        stoptime.remove(listItemPosition);
        status.remove(listItemPosition);
    }
}