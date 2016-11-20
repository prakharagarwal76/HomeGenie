package com.example.prakharagarwal.automaticirrigation;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PresetTimeFragment extends Fragment {
    DBAdapter dba ;
    PresetTime_Adapter adapter;
    int listItemPosition;
    ListView list;

    public PresetTimeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dba= new DBAdapter(getActivity().getApplicationContext());


        try {

            dba.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_preset_time, container, false);



        final List<String> names=dba.getNames();
        final List<String> start = dba.getStart();
        final List<String> stop=dba.getStop();
        final List<String> status=dba.getStatus();
        adapter=new PresetTime_Adapter(getActivity(),names,start,stop,status);
        list=(ListView)rootview.findViewById(R.id.listview_preset);


        list.setAdapter(adapter);
       list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               listItemPosition=position;
               AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
               builder.setMessage("Delete this Preset Time?")
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               String item=(String)list.getAdapter().getItem(listItemPosition);
                               dba.deletePreset(item);
                               adapter.removeAll(listItemPosition);
                               adapter.notifyDataSetChanged();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               // User cancelled the dialog
                           }
                       }).show();

               return false;
           }
       });

        FloatingActionButton fab=(FloatingActionButton)rootview.findViewById(R.id.fab_presetfragment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(getActivity(), AddPresetActivity.class);
                getActivity().startActivityForResult(mainIntent,1);
            }
        });
        return rootview;
    }

    @Override
    public void onResume() {
        super.onResume();

        adapter.clear();

        final List<String> names=dba.getNames();
        final List<String> start = dba.getStart();
        final List<String> stop=dba.getStop();
        final List<String> status=dba.getStatus();
        adapter=new PresetTime_Adapter(getActivity(),names,start,stop,status);
       // adapter.notifyDataSetChanged();
        list.setAdapter(adapter);


    }
}
