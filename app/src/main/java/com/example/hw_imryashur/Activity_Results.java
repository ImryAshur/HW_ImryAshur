package com.example.hw_imryashur;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class Activity_Results extends AppCompatActivity implements CallBack_List {
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        initFragments();

    }

    private void initFragments() {
        fragment_list = new Fragment_List();
        fragment_list.setActivityCallBack(this);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.results_LAY_list, fragment_list);
        transaction.commit();

        fragment_map = new Fragment_Map();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.results_LAY_map, fragment_map);
        transaction1.commit();
    }

    @Override
    public void getLocation(double lat, double lon) {
        fragment_map.setLat(lat);
        fragment_map.setLon(lon);
        fragment_map.moveMap();
    }

}