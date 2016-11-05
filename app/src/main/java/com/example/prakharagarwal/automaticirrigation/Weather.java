package com.example.prakharagarwal.automaticirrigation;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Weather extends Fragment {


    public Weather() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
     View   rootview = inflater.inflate(R.layout.fragment_weather, container, false);
        // get the listview




        // setting list adapter

        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7"
        };
        List<String> weekForecast = new ArrayList<>(Arrays.asList(data));
        ArrayAdapter mForecastAdapter;
        mForecastAdapter= new ArrayAdapter<>(getActivity(),R.layout.listitem_weather,R.id.list_item_forcast_textview,weekForecast);
        ListView listview=(ListView)rootview.findViewById(R.id.listview_forecast);
        listview.setAdapter(mForecastAdapter);
        return inflater.inflate(R.layout.fragment_weather, container, false);
    }

}
