package com.example.prakharagarwal.automaticirrigation;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PresetTimeFragment extends Fragment {
    DBAdapter dba ;

    public PresetTimeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootview=inflater.inflate(R.layout.fragment_preset_time, container, false);

        dba= new DBAdapter(getActivity().getApplicationContext());


        try {

                dba.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //dba.insert("sdf","asd","SDg","1");
        final List<String> names=dba.getNames();
        final List<String> start = dba.getStart();
        final List<String> stop=dba.getStop();
        final List<String> status=dba.getStatus();
       PresetTime_Adapter adapter=new PresetTime_Adapter(getActivity(),names,start,stop,status);
        ListView list=(ListView)rootview.findViewById(R.id.listview_preset);


        list.setAdapter(adapter);
        //list.setAdapter(sadapter);
        //list.setEmptyView(rootview.findViewById(R.id.preset_textview));

       /* FloatingActionButton fab=(FloatingActionButton)rootview.findViewById(R.id.preset_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent mainIntent = new Intent(getActivity(), AddPresetActivity.class);
                getActivity().startActivity(mainIntent);
            }
        });*/
        return inflater.inflate(R.layout.fragment_preset_time, container, false);
    }

}
