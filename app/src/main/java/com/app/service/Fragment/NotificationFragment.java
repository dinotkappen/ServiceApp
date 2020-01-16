package com.app.service.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.app.service.Adapter.Notification_Adapter;
import com.app.service.Model.Notification_Model;
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
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;


public class NotificationFragment extends Fragment {
    View rootView;
    String JSON_URL_NOTIFICATIONS;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token;

    ArrayList<Notification_Model> data_Notification_Model = new ArrayList<Notification_Model>();
    private Notification_Adapter notification_adapter;
    private RecyclerView recyclerView_notification;
    LinearLayout linearItemsNotAvail,linearLayoutMain;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        realativeActionBar.setVisibility(View.VISIBLE);
        backArrow.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.GONE);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//        txt_actionbar_Title.setText("Notifications");
//        ImageView img_search_actionbar=(ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//        img_search_actionbar.setVisibility(View.INVISIBLE);
//        img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
        recyclerView_notification = (RecyclerView) rootView.findViewById(R.id.recyclerView_notification);
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int logedIn = preferences.getInt("logedIn", 0);
        linearLayoutMain=(LinearLayout)rootView.findViewById(R.id.linearLayoutMain);
        linearItemsNotAvail=(LinearLayout)rootView.findViewById(R.id.linearItemsNotAvail);
        linearLayoutMain.setVisibility(View.VISIBLE);
        Log.v("logedIn", "" + logedIn);
        if (logedIn == 1) {
            login_token = preferences.getString("login_token", null);
            Log.v("login_token", login_token);

            loadNotifications_api();
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

        return rootView;
    }

    public void set_Notifications() {
        notification_adapter = new Notification_Adapter(getActivity(), data_Notification_Model);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView_notification.setLayoutManager(mLayoutManager);
        recyclerView_notification.setItemAnimator(new DefaultItemAnimator());
        recyclerView_notification.setAdapter(notification_adapter);
    }

    private void loadNotifications_api() {

        JSON_URL_NOTIFICATIONS = utilities.GetUrl() + "accounts/notification";

        Log.v("token_notification", login_token);
        Log.v("JSON_URL_NOTIFICATIONS", JSON_URL_NOTIFICATIONS);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_NOTIFICATIONS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    JSONObject obj_notification = obj.getJSONObject("notification");
                                    JSONArray jsonArray_data = obj_notification.getJSONArray("data");

                                    int count = jsonArray_data.length();
                                    Log.v("jsonArray_data", "" + jsonArray_data.length());
                                    if(jsonArray_data.length()>0)
                                    {
                                        linearLayoutMain.setVisibility(View.VISIBLE);
                                        linearItemsNotAvail.setVisibility(View.GONE);
                                        data_Notification_Model.clear();
                                        for (int i = 0; i < jsonArray_data.length(); i++) {
                                            JSONObject jsondata = jsonArray_data.getJSONObject(i);
                                            String title = jsondata.getString("title");
                                            String message = jsondata.getString("message");
                                            String date = jsondata.getString("date");
                                            try {
                                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm");
                                                final Date dateObj = sdf.parse(date);
                                                //  System.out.println(dateObj);
                                                date = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(dateObj);
                                            } catch (final ParseException e) {
                                                e.printStackTrace();
                                            }
                                            Log.v("date", date);
                                            data_Notification_Model.add(new Notification_Model(jsondata.getString("title"),
                                                    jsondata.getString("message"), date));

                                        }
                                        set_Notifications();
                                    }
                                    else
                                    {
                                        linearLayoutMain.setVisibility(View.GONE);
                                        linearItemsNotAvail.setVisibility(View.VISIBLE);
                                    }



                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Notifications are not available", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            dialog.dismiss();
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
