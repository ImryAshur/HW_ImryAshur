package com.example.hw_imryashur;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;

public class Fragment_List extends Fragment {
    protected View view;
    private ListView list_LST_list;
    private ArrayList<TopTen> topTens;
    private CallBack_List callBack_list;
    public final String EXTRA_KEY_DATA = "EXTRA_KEY_DATA";

    public void setActivityCallBack(CallBack_List callBack_list) {
        this.callBack_list = callBack_list;
    }

    public static Fragment_List newInstance() {
        Fragment_List fragment = new Fragment_List();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view==null){
            view = inflater.inflate(R.layout.fragment_list, container, false);
        }
        findViews(view);
        topTens = MySharedPreferencesV4.getInstance().getArray("DATA",new TypeToken<ArrayList<TopTen>>(){});
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,topTens);
        list_LST_list.setAdapter(arrayAdapter);

        list_LST_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> listView, View itemView, int itemPosition, long itemId)
            {
                callBack_list.getLocation(topTens.get(itemPosition).getLat(),topTens.get(itemPosition).getLon());
            }
        });

        return view;
    }

    private void findViews(View view) {
        list_LST_list = view.findViewById(R.id.list_LST_list);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d("pttt", "onSaveInstanceState");
        super.onSaveInstanceState(outState);
    }
}
