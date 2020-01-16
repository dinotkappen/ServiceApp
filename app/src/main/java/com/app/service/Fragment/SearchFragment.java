package com.app.service.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Activity.MainActivity;
import com.app.service.Adapter.SearchAdapter;
import com.app.service.Model.SearchModel;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.app.service.Adapter.Main_Menu_Adapter.MY_PREFS_NAME;


public class SearchFragment extends Fragment {
    View rootView;

    private RecyclerView recycler_view_Search;
    private SearchAdapter objSearchAdapter;
    Utilities utilities;
    private static String JSON_URL_SEARCH_SERVICES;
    ArrayList<SearchModel> objSeachModel = new ArrayList<SearchModel>();
    EditText edt_Search;
    String keyword=" ";
    String countryID;
 SearchFragment searchFragment;
 FragmentManager fragmentManager;
 TextView txtClose;
    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (utilities.isOnline(getActivity())) {

            try {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_search, container, false);
                realativeActionBar.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                        new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//                txt_actionbar_Title.setText("SERVICIO");
//                ImageView img_search_actionbar = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//                img_search_actionbar.setImageResource(R.drawable.back_btn);
//                img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                       onBackPressed();
//                    }
//                });
                txtClose=(TextView)rootView.findViewById(R.id.txtClose);
                edt_Search = (EditText) rootView.findViewById(R.id.edt_Search);
                recycler_view_Search = (RecyclerView) rootView.findViewById(R.id.recycler_view_Search);
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                countryID = prefs.getString("countryID", "0");
                Log.v("countryID", countryID);
                loadAllServices_api(keyword);
                txtClose.setVisibility(View.INVISIBLE);
                txtClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        edt_Search.setText("");
                        txtClose.setVisibility(View.INVISIBLE);
                    }
                });
                edt_Search.addTextChangedListener(new TextWatcher() {

                    @Override
                    public void afterTextChanged(Editable s) {
                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start,
                                                  int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start,
                                              int before, int count) {
                        String temp = keyword = edt_Search.getText().toString();
                        txtClose.setVisibility(View.VISIBLE);
                        keyword = temp;
                        temp = "";
                        Log.v("q", keyword);
                        loadAllServices_api(keyword);

                    }
                });

            }
            catch (Exception ex)
            {
                String msg=ex.getMessage().toString();
                Log.v("msg_main",msg);
            }

        }
        else
        {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }

    private void set_All_Services() {
        try{
        objSearchAdapter = new SearchAdapter(getActivity(), objSeachModel);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view_Search.setLayoutManager(mLayoutManager);
        //recycler_view_Search.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), false));
        recycler_view_Search.setItemAnimator(new DefaultItemAnimator());
        recycler_view_Search.setAdapter(objSearchAdapter);
        }
        catch (Exception ex)
        {
            String msg=ex.getMessage().toString();
            Log.v("msg_sub",msg);
        }

    }


    private void loadAllServices_api( String keyword) {
        //creating a string request to send request to the url
        JSON_URL_SEARCH_SERVICES = utilities.GetUrl()+"products?country_id="+countryID+"&q="+keyword;
        Log.v("URL_SEARCH_SERVICES",JSON_URL_SEARCH_SERVICES);

        keyword="";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SEARCH_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {


                                //so here we are getting that json array
                                JSONObject obj_product = obj.getJSONObject("products");
                                JSONArray  obj_Data=obj_product.getJSONArray("data");


                                objSeachModel.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < obj_Data.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = obj_Data.getJSONObject(i);
                                    String name = jsondata.getString("name");
                                    String cover = jsondata.getString("cover");
                                    objSeachModel.add(new SearchModel(jsondata.getString("id"),
                                            jsondata.getString("name"), jsondata.getString("cover")));
                                    Log.v("msg",jsondata.getString("name"));

                                }

                                JSON_URL_SEARCH_SERVICES="";

                                set_All_Services();
                                //  item_main_adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(getContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
                            System.out.println("Error " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }



    public void onBackPressed() {

        int backStackEntryCount = getActivity().getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount == 0) {
            Log.v("hi","rtk");
            Intent in=new Intent(getContext(), MainActivity.class);
            startActivity(in);
            //goBack();   // write your code to switch between fragments.
        } else {
           // super.onBackPressed();
            Log.v("kl","lk");
            getActivity().onBackPressed();
        }
    }





    }

