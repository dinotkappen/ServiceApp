package com.app.service.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ReceiptDetailsFragment extends Fragment {

    View rootView;
    SharedPreferences preferences;
    String receiptID,countryID;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    String JSON_URL_RECEIPT_DETAILS;
    Utilities utilities;
    String login_token;
    TextView txtReferenceNo, txStatus, txtServiceName, txtServiceCharge, txtServiceDate, txtServiceTime;

    MapView mMapView;
    private GoogleMap googleMap;
    String location_latitude, location_longitude;
    private static final int MY_LOCATION_CODE = 110;

    public ReceiptDetailsFragment() {
        // Required empty public constructor
    }

    int PERMISSION_ALL = 1;
    int perSuccess = 0;
    String[] PERMISSIONS = {

            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION

    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            // Inflate the layout for this fragment

            rootView = inflater.inflate(R.layout.fragment_receipt_details, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);


//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                    new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//            TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//            txt_actionbar_Title.setText("My Receipt");
//            ImageView img_search_actionbar = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//            img_search_actionbar.setImageResource(R.drawable.back_btn);
//            img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().onBackPressed();
//                }
//            });


            txtReferenceNo = (TextView) rootView.findViewById(R.id.txtReferenceNo);
            txStatus = (TextView) rootView.findViewById(R.id.txStatus);
            txtServiceName = (TextView) rootView.findViewById(R.id.txtServiceName);
            txtServiceCharge = (TextView) rootView.findViewById(R.id.txtServiceCharge);
            txtServiceDate = (TextView) rootView.findViewById(R.id.txtServiceDate);
            txtServiceTime = (TextView) rootView.findViewById(R.id.txtServiceTime);

            preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            receiptID = preferences.getString("receiptId", "0");
            Log.v("receiptID", receiptID);
            login_token = preferences.getString("login_token", null);
            Log.v("login_token", login_token);



            mMapView = (MapView) rootView.findViewById(R.id.mapViewReceipt);
            mMapView.onCreate(savedInstanceState);
            loadReceiptDetails();
            // mMapView.onResume(); // needed to get the map to display immediately
            // mMapView.onResume();

        } catch (Exception ex) {
            Log.v("OnCreate_REDetails", ex.getMessage().toString());
        }

        return rootView;
    }


    private void loadReceiptDetails() {

        JSON_URL_RECEIPT_DETAILS = utilities.GetUrl() + "accounts/order/" + receiptID;


        Log.v("token_receipt", login_token);
        Log.v("URL_RECEIPT_DETAILS", JSON_URL_RECEIPT_DETAILS);

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_RECEIPT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            JSONObject statusObj = obj.getJSONObject("status");

                            String statusName = statusObj.getString("name");

                            JSONObject orderObj = obj.getJSONObject("order");

                            String reference = orderObj.getString("reference");
                            location_latitude = orderObj.getString("location_latitude");
                            location_longitude = orderObj.getString("location_longitude");
                            String servicedate = orderObj.getString("servicedate");
                            String servicetime = orderObj.getString("servicetime");
                            String country_id= orderObj.getString("country_id");
                            Log.v("country_id",country_id);
                            String total = orderObj.getString("total");
                            Log.v("location_latitude", location_latitude);
                            Log.v("location_longitude", location_longitude);

                            if (reference != null && !reference.isEmpty() && !reference.equals("null"))
                            {
                                txtReferenceNo.setText(reference);
                            }

                            if (total != null && !total.isEmpty() && !total.equals("null"))
                            {
                                if(country_id.equals("174"))
                                {
                                    txtServiceCharge.setText("QAR " + total);
                                }
                                else
                                {
                                    Double qar = Double.parseDouble(total);
                                    String price = "" + (qar * 0.27);
                                    Log.v("priceUSD", price);

                                    txtServiceCharge.setText("USD " + price);
                                }
                            }

                            if (statusName != null && !statusName.isEmpty() && !statusName.equals("null"))
                            {
                                txStatus.setText(statusName);
                            }




                            try {
                                final SimpleDateFormat sdf = new SimpleDateFormat("H:mm");
                                final Date dateObj = sdf.parse(servicetime);
                                String dat = "" + new SimpleDateFormat("hh:mm a").format(dateObj);
                                if (dat != null && !dat.isEmpty() && !dat.equals("null"))
                                {
                                    txtServiceTime.setText(dat);
                                }

                            } catch (final ParseException e) {
                                e.printStackTrace();
                            }

                            try {
                                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                final Date dateObj = sdf.parse(servicedate);
                                //  System.out.println(dateObj);
                                servicedate = new SimpleDateFormat("dd-MM-yyyy").format(dateObj);
                            } catch (final ParseException e) {
                                e.printStackTrace();
                            }
                            Log.v("date", servicedate);
                            if (servicedate != null && !servicedate.isEmpty() && !servicedate.equals("null"))
                            {
                                txtServiceDate.setText(servicedate);
                            }



                            JSONArray productsArray = orderObj.getJSONArray("products");
                            JSONObject productObj = productsArray.getJSONObject(0);
                            String serViceName = productObj.getString("name");
                            txtServiceName.setText(serViceName);
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("myReceiptFragment", "0");
                            editor.putString("receiptId", "0");
                            editor.putString("notificationId", "0");
                            editor.commit();
                            loadMap();


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

    public void loadMap() {
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap mMap) {
                    googleMap = mMap;

//             // For showing a move to my location button


                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.


                        return;
                    }
                    googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                    googleMap.setMyLocationEnabled(true);
                    if ((location_latitude != null && !location_latitude.isEmpty() && !location_latitude.equals("null"))) {

                        if (((location_longitude != null && !location_longitude.isEmpty() && !location_longitude.equals("null")))) {
                            LatLng sydney = new LatLng(Double.parseDouble(location_longitude), Double.parseDouble(location_latitude));
                            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker Title").snippet("Marker Description"));
                            Log.v("olk", "oklklklkl");
                            // For zooming automatically to the location of the marker
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(10).build();
                            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        } else {

                            Log.v("location_longitude", location_longitude);
                        }
                    } else {
                        Log.v("location_latitude", location_latitude);
                    }
                    // For dropping a marker at a point on the Map


                }

            });
        } catch (Exception ex) {
            Log.v("loadMap", ex.getMessage().toString());
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
