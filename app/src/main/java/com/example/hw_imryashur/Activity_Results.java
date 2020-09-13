package com.example.hw_imryashur;
/*
    Student - Imry Ashur
*/
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class Activity_Results extends AppCompatActivity implements CallBack_List {
    private Fragment_List fragment_list;
    private Fragment_Map fragment_map;
    private ArrayList<TopTen> topTens;
    public static final String SP_DATA = "DATA";

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

    //get location from view list and move the map to the new location
    @Override
    public void getLocation(int index) {
        fragment_map.setLat(topTens.get(index).getLat());
        fragment_map.setLon(topTens.get(index).getLon());
        fragment_map.moveMap();
    }

    //get the list from SP and set list view
    @Override
    public void getArray(ListView listView) {
        topTens = MySharedPreferencesV4.getInstance().getArray(SP_DATA,new TypeToken<ArrayList<TopTen>>(){});
        if(topTens != null){
            ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,topTens);
            setPositions();
            listView.setAdapter(arrayAdapter);
        }
    }

    // set position numbers
    private void setPositions() {
        for(int i=1;i<=topTens.size();i++){
            topTens.get(i-1).setPosition(i);
        }
    }
}