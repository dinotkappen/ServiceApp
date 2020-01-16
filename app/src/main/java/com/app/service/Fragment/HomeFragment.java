package com.app.service.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
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
import com.app.service.Adapter.Banner_Img_Adapter;
import com.app.service.Adapter.ViewPagerAdapter;
import com.app.service.Model.Banner_Img_Model;
import com.app.service.Model.Service_List_Model;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.commons.collections4.ListUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.facebook.FacebookSdk.getApplicationContext;


public class HomeFragment extends Fragment {
    View rootView;
    String img_src;
    int totalPageCount;
    int deviceWidth = 0;
    int columnWidth = 0;
    Utilities utilities;
    int deviceHeight = 0;
    int columnHeight = 0;
    String selectedLang;
    private int dotsCount;
    private Toolbar toolbar;
    private ImageView[] dots;
    private ViewPager viewPager;
    FragmentManager childFragMang;
    SharedPreferences preferences;
    private LinearLayout pager_indicator;
    private static ViewPager mPager_Banner;
    private static int NUM_PAGES_BANNER = 0;
    private static int currentPage_Items = 0;
    private static int currentPage_Banner = 0;
    ArrayList service_IDArrayList = new ArrayList();
    ArrayList service_NameArrayList = new ArrayList();

    ArrayList service_ImagesArrayList = new ArrayList();
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ArrayList<Banner_Img_Model> data_Banner_Images = new ArrayList<Banner_Img_Model>();
    private Banner_Img_Adapter obj_item_main_adapter_;
    ArrayList<Service_List_Model> service_List_Model = new ArrayList<Service_List_Model>();
    ArrayList<Service_List_Model> service_List_Arraylist = new ArrayList<Service_List_Model>();

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {
            try {

                rootView = inflater.inflate(R.layout.fragment_home, container, false);
                selectedLang = Hawk.get("selectedLang","en");
                childFragMang = getChildFragmentManager();
                realativeActionBar.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.GONE);
                imgSearch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new SearchFragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//                ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                        new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//                TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//                txt_actionbar_Title.setText("SERVICIO");
//                ImageView img_search_actionbar = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//                img_search_actionbar.setImageResource(R.drawable.search);
//                img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        SearchFragment searchFragment = new SearchFragment();
//                        getActivity().getSupportFragmentManager()
//                                .beginTransaction()
//                                .replace(R.id.fragment_container, searchFragment)
//                                .commit();
//                    }
//                });
                SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                deviceHeight = displayMetrics.heightPixels;
                deviceWidth = displayMetrics.widthPixels;


                editor.putInt("deviceWidth", deviceWidth);
                editor.commit();


                toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);


                viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
                viewPager.setOffscreenPageLimit(3);// no of fragments


                loadBannerList_api();
                if(selectedLang.equals("en"))
                {
                    loadItems_apiEN();
                }
                else
                {
                    loadItems_apiAR();
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

    private void setupViewPager(ViewPager viewPager) {
        try {

            ViewPagerAdapter adapter = new ViewPagerAdapter(childFragMang);
            totalPageCount = service_List_Arraylist.size() / 9;
            int count = service_List_Arraylist.size();
            int reminder = count % 9;
            int quot = count / 9;

            if (reminder > 0) {
                totalPageCount = quot + 1;
            }

            ArrayList<List<Service_List_Model>> arrayOfArray = new ArrayList<>();
            for (List<Service_List_Model> partition : ListUtils.partition(service_List_Arraylist, 9)) {
                arrayOfArray.add(partition);
            }
            for (int i = 0; i < totalPageCount; i++) {
                adapter.addFragment(new Home_Item_Tab_Fragment(arrayOfArray.get(i)), "");

                viewPager.setAdapter(adapter);
            }

            CirclePageIndicator indicator = (CirclePageIndicator)
                    rootView.findViewById(R.id.indicator_items);
            indicator.setViewPager(viewPager);
            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(4 * density);
            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage_Items == totalPageCount) {
                        currentPage_Items = 0;
                    }
                    viewPager.setCurrentItem(currentPage_Items++, true);
                }
            };
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("msg_setupViewPager", msg);
        }

    }

    private void setBannerImage() {

        try {
            obj_item_main_adapter_ = new Banner_Img_Adapter(getActivity(), data_Banner_Images);
            mPager_Banner = (ViewPager) rootView.findViewById(R.id.pager_banner);
            if (getActivity() != null) {
                mPager_Banner.setAdapter(obj_item_main_adapter_);
            }


            CirclePageIndicator indicator = (CirclePageIndicator)
                    rootView.findViewById(R.id.indicator_banner);

            indicator.setViewPager(mPager_Banner);


            final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
            indicator.setRadius(4 * density);

            NUM_PAGES_BANNER = data_Banner_Images.size();

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage_Banner == NUM_PAGES_BANNER) {
                        currentPage_Banner = 0;
                    }
                    mPager_Banner.setCurrentItem(currentPage_Banner++, true);
                }
            };

            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 6000, 6000);

            // Pager listener over indicator
            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageSelected(int position) {
                    currentPage_Banner = position;

                }

                @Override
                public void onPageScrolled(int pos, float arg1, int arg2) {

                }

                @Override
                public void onPageScrollStateChanged(int pos) {

                }
            });
        } catch (Exception ex) {
            String s = ex.getMessage().toString();
            String h = s;
        }

    }

    private void loadBannerList_api() {

        String JSON_URL_BANNERS = utilities.GetUrl() + "banners";
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_BANNERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                //so here we are getting that json array
                                JSONArray bannersArray = obj.getJSONArray("banners");
                                data_Banner_Images.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < bannersArray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = bannersArray.getJSONObject(i);
                                    String banners_id = jsondata.getString("banners_id");
                                    String url = jsondata.getString("banners_image");
                                    Log.v("urlB",url);


                                    data_Banner_Images.add(new Banner_Img_Model("", jsondata.getString("banners_image")));

                                }

                                setBannerImage();
                            } else {
                                Toast.makeText(getContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
                            }


                        } catch (JSONException e) {
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

    private void loadItems_apiEN() {
        //creating a string request to send request to the url
        String JSON_URL_SERVICES = utilities.GetUrl() + "categories";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {


                                //so here we are getting that json array
                                JSONArray jsonarray = obj.getJSONArray("categories");


                                service_List_Arraylist.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = jsonarray.getJSONObject(i);

//                                    String name = heroObject.getString("name");
//                                    String cover = heroObject.getString("cover");
//                                    String h = jsondata.getString("id");
//                                    service_IDArrayList.add(jsondata.getString("id"));
//                                    service_NameArrayList.add(jsondata.getString("name"));
                                    String hj=jsondata.getString("cover");
                                    String kj=hj;


                                    service_List_Arraylist.add(new Service_List_Model(jsondata.getString("id"), jsondata.getString("name"), jsondata.getString("cover")));

                                }
                                setupViewPager(viewPager);
                                // set_Service_Items();
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    private void loadItems_apiAR() {
        //creating a string request to send request to the url
        String JSON_URL_SERVICES = utilities.GetUrl() + "categories";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {


                                //so here we are getting that json array
                                JSONArray jsonarray = obj.getJSONArray("categories");


                                service_List_Arraylist.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = jsonarray.getJSONObject(i);

//                                    String name = heroObject.getString("name");
//                                    String cover = heroObject.getString("cover");
//                                    String h = jsondata.getString("id");
//                                    service_IDArrayList.add(jsondata.getString("id"));
//                                    service_NameArrayList.add(jsondata.getString("name"));
//                                    service_ImagesArrayList.add(jsondata.getString("cover"));


                                    service_List_Arraylist.add(new Service_List_Model(jsondata.getString("id"), jsondata.getString("name_ar"), jsondata.getString("cover")));

                                }
                                setupViewPager(viewPager);
                                // set_Service_Items();
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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

//    @Override
//    public void onResume() {
//        Log.e("DEBUG", "onResume of HomeFragment");
//        //loadItems_api();
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        Log.e("DEBUG", "OnPause of HomeFragment");
//        super.onPause();
//    }


}

