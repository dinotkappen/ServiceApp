package com.app.service.Fragment;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Adapter.Sub_Menu_Adapter;
import com.app.service.Model.Sub_Menu_Model;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cc.cloudist.acplibrary.ACProgressFlower;

import static android.content.Context.MODE_PRIVATE;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.app.service.Adapter.Main_Menu_Adapter.MY_PREFS_NAME;


public class SubMenuFragment extends Fragment {
    View rootView;
    private RecyclerView recyclerView_submenu;
    private Sub_Menu_Adapter submenu_item_adapter;
    private List<Sub_Menu_Model> submenu_item_ModelList;
    private RecyclerView.LayoutManager mLayoutManager;
    String serviceId_str, serviceName;
    String JSON_URL_SUB_SERVICES;
    String countryID;
    LinearLayout linearItemsNotAvail, linearLayoutMain;
    TextView txt_ServiceTitle;
    String next_page_url;
    String selectedLang;
    String headingMain;
    Boolean exist;
    Utilities utilities;
    ArrayList<Sub_Menu_Model> data_Sub_services = new ArrayList<Sub_Menu_Model>();

    public SubMenuFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (utilities.isOnline(getActivity())) {
            // Inflate the layout for this fragment
            try {
                rootView = inflater.inflate(R.layout.fragment_sub_menu, container, false);
                realativeActionBar.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
                selectedLang = Hawk.get("selectedLang", "en");
                linearLayoutMain = (LinearLayout) rootView.findViewById(R.id.linearLayoutMain);
                linearItemsNotAvail = (LinearLayout) rootView.findViewById(R.id.linearItemsNotAvail);
                linearLayoutMain.setVisibility(View.VISIBLE);
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
////                        HomeFragment homeFragment = new HomeFragment();
////                        getActivity().getSupportFragmentManager()
////                                .beginTransaction()
////                                .replace(R.id.fragment_container, homeFragment)
////                                .commit();
//
//                        backSubMenu();
//                    }
//                });
                txt_ServiceTitle = (TextView) rootView.findViewById(R.id.txt_ServiceTitle);
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);

                serviceId_str = prefs.getString("productId", "0");
                serviceName = prefs.getString("serviceName", "0");
                //0 is the default value.

                countryID = prefs.getString("countryID", "0");
                Log.v("countryID", countryID);//0 is the default value.
                Log.v("serviceName", serviceName);//0 is the default value.
                Log.v("serviceId_str", serviceId_str);
                recyclerView_submenu = (RecyclerView) rootView.findViewById(R.id.recyclerView_submenu);
                txt_ServiceTitle.setText(serviceName);


//Recyclerview setting servic3

                submenu_item_adapter = new Sub_Menu_Adapter(getActivity(), data_Sub_services);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView_submenu.setLayoutManager(mLayoutManager);
                recyclerView_submenu.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
                recyclerView_submenu.setItemAnimator(new DefaultItemAnimator());
                recyclerView_submenu.setAdapter(submenu_item_adapter);

                recyclerView_submenu.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);

                        if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            // Toast.makeText(getContext(), "Lst", Toast.LENGTH_SHORT).show();
                            if (next_page_url != null && !next_page_url.isEmpty() && !next_page_url.equals("null")) {
                                if(selectedLang.equals("en"))
                                {
                                    loadSubServices_apiEn(JSON_URL_SUB_SERVICES);
                                }
                                else
                                {
                                    loadSubServices_apiAR(JSON_URL_SUB_SERVICES);
                                }
                            }

                        }
                    }
                });

                if (serviceId_str != null && !serviceId_str.isEmpty() && !serviceId_str.equals("null")) {
                    if (countryID != null && !countryID.isEmpty() && !countryID.equals("null")) {

                        JSON_URL_SUB_SERVICES = utilities.GetUrl() + "category/" + serviceId_str + "?country_id=" + countryID;
                        if(selectedLang.equals("en"))
                        {
                            loadSubServices_apiEn(JSON_URL_SUB_SERVICES);
                        }
                        else
                        {
                            loadSubServices_apiAR(JSON_URL_SUB_SERVICES);
                        }

                    }
                }


            } catch (Exception ex) {
                String msg = ex.getMessage().toString();
                Log.v("msg_onCreate", msg);
            }

        } else {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }

    public boolean checkAlreadyExist(String id) {
        exist = false;
        for (int i = 0; i < data_Sub_services.size(); i++) {
            if (data_Sub_services.get(i).getSub_item_id().equals(id)) {
                exist = true;
            }


        }
        return exist;
    }

    public void loadSubServices_apiEn(String JSON_URL_SUB_SERVICES) {


        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SUB_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            //data_Sub_services.clear();
                            if (status.equals("200")) {

                                dialog.dismiss();
                                JSONObject obj_products = obj.getJSONObject("products");
                                String current_page = obj_products.getString("current_page");
                                JSONObject obj_dat = obj_products.getJSONObject("data");
                                next_page_url = obj_products.getString("next_page_url");

                                int size = obj_dat.length();
                                Log.v("size", "" + size);
                                if (obj_dat.length() > 0) {
                                    Iterator keys = obj_dat.keys();
                                    while (keys.hasNext()) {
                                        JSONObject obj_details = obj_dat.getJSONObject((String) keys.next());
                                        String id = obj_details.getString("id");
                                        String j = obj_details.getString("cover");

                                        if (checkAlreadyExist(id) == false) {
                                            data_Sub_services.add(new Sub_Menu_Model
                                                    (obj_details.getString("id"), obj_details.getString("cover"), obj_details.getString("name"), ""));

                                        }
                                    }

                                    submenu_item_adapter.notifyDataSetChanged();

                                } else {
                                    linearLayoutMain.setVisibility(View.GONE);
                                    linearItemsNotAvail.setVisibility(View.VISIBLE);
                                }

                                //   set_Main_Items();
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
                        dialog.dismiss();
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public void loadSubServices_apiAR(String JSON_URL_SUB_SERVICES) {


        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SUB_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            //data_Sub_services.clear();
                            if (status.equals("200")) {

                                dialog.dismiss();
                                JSONObject obj_products = obj.getJSONObject("products");
                                String current_page = obj_products.getString("current_page");
                                JSONObject obj_dat = obj_products.getJSONObject("data");
                                next_page_url = obj_products.getString("next_page_url");

                                int size = obj_dat.length();
                                Log.v("size", "" + size);
                                if (obj_dat.length() > 0) {
                                    Iterator keys = obj_dat.keys();
                                    while (keys.hasNext()) {
                                        JSONObject obj_details = obj_dat.getJSONObject((String) keys.next());
                                        String id = obj_details.getString("id");
                                        String j = obj_details.getString("cover");

                                        if (checkAlreadyExist(id) == false) {
                                            data_Sub_services.add(new Sub_Menu_Model
                                                    (obj_details.getString("id"), obj_details.getString("cover"), obj_details.getString("name_ar"), ""));

                                        }
                                    }

                                    submenu_item_adapter.notifyDataSetChanged();

                                } else {
                                    linearLayoutMain.setVisibility(View.GONE);
                                    linearItemsNotAvail.setVisibility(View.VISIBLE);
                                }

                                //   set_Main_Items();
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
                        dialog.dismiss();
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        //go to previous fragemnt
                        //perform your fragment transaction here
                        //pass data as arguments
                        HomeFragment homeFragment = new HomeFragment();
                        getActivity().getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, homeFragment)
                                .commit();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}

