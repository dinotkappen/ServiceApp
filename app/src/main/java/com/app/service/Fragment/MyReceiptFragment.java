package com.app.service.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Activity.LogInActivity;
import com.app.service.Adapter.Receipt_Adapter;
import com.app.service.Model.Reciept_Model;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MyReceiptFragment extends Fragment {
    View rootView;
    String JSON_URL_ORDERS;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token,countryID;

    ArrayList<Reciept_Model> data_Reciept_Model = new ArrayList<Reciept_Model>();
    private Receipt_Adapter receipt_adapter;
    private RecyclerView recyclerView_reciept;
    LinearLayout linearItemsNotAvail, linearLayoutMain;
    int PERMISSION_ALL = 1;
    int perSuccess = 0;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };

    public MyReceiptFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try{
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reciept, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//        txt_actionbar_Title.setText("My Receipt");
//        ImageView img_search_actionbar = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//        img_search_actionbar.setImageResource(R.drawable.back_btn);
//        img_search_actionbar.setVisibility(View.INVISIBLE);
//        img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
        recyclerView_reciept = (RecyclerView) rootView.findViewById(R.id.recyclerView_reciept);
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int logedIn = preferences.getInt("logedIn", 0);

        Log.v("logedIn", "" + logedIn);
        linearLayoutMain = (LinearLayout) rootView.findViewById(R.id.linearLayoutMain);
        linearItemsNotAvail = (LinearLayout) rootView.findViewById(R.id.linearItemsNotAvail);
        linearLayoutMain.setVisibility(View.VISIBLE);
        if (logedIn == 1) {
            login_token = preferences.getString("login_token", null);
            Log.v("login_token", login_token);
//            if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
//                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
//
//
//            }

            loadReceipt_api();
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(
                    getContext());

            builder.setTitle("Log In");
            builder.setMessage("To continue please log in");
            builder.setCancelable(false);
            builder.setNegativeButton("NO",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            dialog.dismiss();
                        }
                    });
            builder.setPositiveButton("YES",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,
                                            int which) {
                            Intent in = new Intent(getContext(), LogInActivity.class);
                            startActivity(in);
                        }
                    });


            builder.show();


        }
        }
        catch (Exception ex)
        {
            Log.v("OnCreate_RE",ex.getMessage().toString());
        }
        return rootView;
    }
    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    public void set_Receipts() {
        try{
        receipt_adapter = new Receipt_Adapter(getActivity(), data_Reciept_Model);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView_reciept.setLayoutManager(mLayoutManager);
        recyclerView_reciept.setItemAnimator(new DefaultItemAnimator());
        recyclerView_reciept.setAdapter(receipt_adapter);
        if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
        }
        }
        catch (Exception ex)
        {
            Log.v("set_Receipts",ex.getMessage().toString());
        }
    }

    private void loadReceipt_api() {

        JSON_URL_ORDERS = utilities.GetUrl() + "accounts/orders";


        Log.v("token_receipt", login_token);
        Log.v("JSON_URL_ORDERS", JSON_URL_ORDERS);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);
                            data_Reciept_Model.clear();
                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                JSONObject jsonObject_orders = obj.getJSONObject("orders");
                                String cuure = jsonObject_orders.getString("current_page");
                                JSONArray jsonArray_data = jsonObject_orders.getJSONArray("data");
                                Log.v("jsonObject_data", "" + jsonArray_data.length());
                                if (jsonArray_data.length() > 0) {
                                    linearLayoutMain.setVisibility(View.VISIBLE);
                                    linearItemsNotAvail.setVisibility(View.GONE);

                                    for (int i = 0; i < jsonArray_data.length(); i++) {
                                        //getting the json object of the particular index inside the array
                                        JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                        String reference = jsondata.getString("reference");
                                        String total = jsondata.getString("total");
                                        String id = jsondata.getString("id");
                                        String date = jsondata.getString("created_at");
                                        String country_id=jsondata.getString("country_id");
                                        Log.v("country_id",country_id);
                                        try {
                                            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
                                            final Date dateObj = sdf.parse(date);
                                            //  System.out.println(dateObj);
                                            date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(dateObj);
                                        } catch (final ParseException e) {
                                            e.printStackTrace();
                                        }
                                        Log.v("date", date);

                                        JSONObject obj_status = jsondata.getJSONObject("status");

                                        String crnt_status = obj_status.getString("name");

//                                    String k=obj_details.getString("name");
//
                                        data_Reciept_Model.add(new Reciept_Model(id, reference, date, total, crnt_status,country_id));

                                        set_Receipts();
                                        JSON_URL_ORDERS = "";
                                    }


                                    // do smth
                                } else {
                                    linearLayoutMain.setVisibility(View.GONE);
                                    linearItemsNotAvail.setVisibility(View.VISIBLE);
                                }

                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
                            linearLayoutMain.setVisibility(View.GONE);
                            linearItemsNotAvail.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                        String msg = error.getMessage().toString();
                        String n = msg;
                        dialog.dismiss();
                    }
                }) {


            /** Passing some request headers* */
            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("userToken", login_token);
                Log.v("login_token2", login_token);
                return headers;
            }

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);


    }
}


