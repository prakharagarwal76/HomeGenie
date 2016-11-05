package com.example.prakharagarwal.automaticirrigation;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import android.widget.Button;
import android.widget.TableLayout;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager viewPager;
    TabLayout tabLayout;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        setContentView(R.layout.activity_main);

        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(new MasterControlFragment(),"Master Control");
        viewPagerAdapter.addFragment(new PresetTimeFragment(),"Preset Time");
        viewPagerAdapter.addFragment(new Weather(),"Weather");

        viewPager =(ViewPager)findViewById(R.id.viewpager);
        tabLayout=(TabLayout)findViewById(R.id.tablayout);
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }
}
