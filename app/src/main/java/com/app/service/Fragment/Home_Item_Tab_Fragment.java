package com.app.service.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.service.Adapter.Main_Menu_Adapter;
import com.app.service.Model.Service_List_Model;
import com.app.service.R;


import java.util.ArrayList;
import java.util.List;


public class Home_Item_Tab_Fragment extends Fragment {

    View rootView;
    ArrayList service_NameArrayList = new ArrayList();
    ArrayList service_ImagesArrayList = new ArrayList();
    ArrayList service_IDArrayList = new ArrayList();
    ArrayList<Service_List_Model> service_List_Model = new ArrayList<Service_List_Model>();
    private static final String JSON_URL_SERVICES = "http://serviceapp.whyteapps.com/api/categories";

    List<Service_List_Model> dataArray;

    public Home_Item_Tab_Fragment(List<Service_List_Model> dataArray) {
        // Required empty public constructor
        this.dataArray = dataArray;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_one, container, false);
        RecyclerView recyclerView_horizontal = (RecyclerView) rootView.findViewById(R.id.recyclerView_horizontal);


        // set a GridLayoutManager with 3 number of columns , horizontal gravity and false value for reverseLayout to show the items from start to end
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, LinearLayoutManager.VERTICAL, false);
        recyclerView_horizontal.setLayoutManager(gridLayoutManager); // set LayoutManager to RecyclerView
        // recyclerView_horizontal.addItemDecoration(new CirclePagerIndicatorDecoration());

        //  call the constructor of CustomAdapter to send the reference and data to Adapter
        Main_Menu_Adapter main_service_adapter = new Main_Menu_Adapter(getActivity(), dataArray);
        recyclerView_horizontal.setAdapter(main_service_adapter); // set the Adapter to RecyclerView



        return rootView;
    }

}
