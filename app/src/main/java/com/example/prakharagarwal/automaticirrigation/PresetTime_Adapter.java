package com.example.prakharagarwal.automaticirrigation;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
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
    public View getView(int position, View convertView, ViewGroup parent)
    {



        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_listview_preset, parent, false);
        }

        TextView t1 = (TextView) convertView.findViewById(R.id.list_item_preset_text1);
        TextView t2 = (TextView) convertView.findViewById(R.id.list_item_preset_text2);
        TextView t3 = (TextView) convertView.findViewById(R.id.list_item_preset_text3);
        TextView t4 = (TextView) convertView.findViewById(R.id.list_item_preset_text4);

        t1.setText(name.get(position));
        t2.setText(starttime.get(position));
        t3.setText(stoptime.get(position));
        t4.setText(status.get(position));






        return convertView;
    }
}

