package com.app.service.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiConfiguration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.app.service.Adapter.Main_Menu_Adapter.MY_PREFS_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btn_nxt_map;
    EditText edt_city, edt_adrz, edt_pin, edt_location;
    String city = null;
    String state = null;
    String postalCode = null;
    String country = null;
    String knownName = null;
    String address = null;
    Double lat_dbl, long_dbl;
    String full_Adrz;
    SharedPreferences preferences;
    String selected_CountryName;
    String selectedLat="0";
    String selectedLong="0";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Utilities utilities;
    int PERMISSION_ALL = 1;
    String countryName;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };

    // PlaceAutocompleteFragment placeAutoComplete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            if (utilities.isOnline(this)) {
                setContentView(R.layout.activity_maps);
                SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                if (!hasPermissions(getApplicationContext(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions(MapsActivity.this, PERMISSIONS, PERMISSION_ALL);
                }
                btn_nxt_map = (Button) findViewById(R.id.btn_nxt_map);
                preferences = getApplicationContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                selected_CountryName = preferences.getString("countryName", "");
                selected_CountryName = selected_CountryName.toLowerCase();
                Log.v("selected_CountryName", selected_CountryName);
                selectedLat = preferences.getString("countryLat", "0");
                selectedLong = preferences.getString("countryLon", "0");
                Log.v("selectedLat",selectedLat);
                Log.v("selectedLong",selectedLong);
//                Log.v("selectedLatt", selectedLat);
//                Log.v("selectedLongg", selectedLong);
//                Log.v("countryName", countryName);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);

                edt_city = (EditText) findViewById(R.id.edt_city);
                edt_adrz = (EditText) findViewById(R.id.edt_adrz);
                edt_location = (EditText) findViewById(R.id.edt_location);
              //  edt_pin = (EditText) findViewById(R.id.edt_pin);

                edt_city.setVisibility(View.VISIBLE);
                edt_adrz.setVisibility(View.VISIBLE);
                edt_location.setVisibility(View.VISIBLE);
               // edt_pin.setVisibility(View.VISIBLE);

//            placeAutoComplete = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//            placeAutoComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(Place place) {
//
//                    Log.d("Maps", "Place selected: " + place.getName());
//                }
//
//                @Override
//                public void onError(Status status) {
//                    Log.d("Maps", "An error occurred: " + status);
//                }
//            });
                btn_nxt_map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected_CountryName.equals(country)) {
                            btnNextOnclick();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sorry no service available in selected location...", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

//            PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
//                    getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
//
//
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(Place place) {
//                    mMap.clear();
//                    mMap.addMarker(new MarkerOptions().position(place.getLatLng()).title(place.getName().toString()));
//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(place.getLatLng(), 12.0f));
//                }
//
//                @Override
//                public void onError(Status status) {
//
//                }
//
//
//            });
            } else {
                setContentView(R.layout.no_internet_msg_layout);
            }
        }
        catch (Exception ex)
        {
            Log.v("OnCreateMap",ex.getMessage().toString());
        }
    }

    public void btnNextOnclick() {
        try {


            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            if (full_Adrz.equals(null)) {
                Toast.makeText(getApplicationContext(), "Address is required", Toast.LENGTH_SHORT).show();
                Log.v("e1", "Adrz not avail");
                if (lat_dbl.equals(0)) {
                    Toast.makeText(getApplicationContext(), "Address is required", Toast.LENGTH_SHORT).show();
                    Log.v("e2", "Adrz not avail");
                    if (long_dbl.equals(0)) {
                        Toast.makeText(getApplicationContext(), "Address is required", Toast.LENGTH_SHORT).show();
                        Log.v("e3", "Adrz not avail");
                    }
                }
            } else {
                Intent in = new Intent(MapsActivity.this, MainActivity.class);
                in.putExtra("from_maps", 1);

                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("full_Adrz", full_Adrz);
                editor.putString("lat_str", "" + lat_dbl);
                editor.putString("long_str", "" + long_dbl);
                editor.commit();
                startActivity(in);
                finish();
            }


        } catch (Exception ex) {
            String h = ex.getMessage().toString();
            Toast.makeText(getApplicationContext(), "Address is required", Toast.LENGTH_SHORT).show();
            String k = h;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.v("requestCode",""+requestCode);
        if(requestCode==PERMISSION_ALL)
        {
            //Toast.makeText(getApplicationContext(),"HIIIIoo",Toast.LENGTH_SHORT).show();
        }
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
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;

            // Add a marker in Sydney and move the camera
            LatLng doha = new LatLng(Double.parseDouble(selectedLat), Double.parseDouble(selectedLong));
            lat_dbl=Double.parseDouble(selectedLat);
            long_dbl=Double.parseDouble(selectedLong);
            Log.v("doha_Lat",""+lat_dbl);
            Log.v("doha_Lon",""+long_dbl);
            mMap.addMarker(new MarkerOptions().position(doha).title("Doha"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_dbl,long_dbl), 10.0f));

//            lat_dbl = doha.latitude;
//            long_dbl = doha.longitude;
            get_Adrz();
            try {
                // Setting a click event handler for the map
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

                    @Override
                    public void onMapClick(LatLng latLng) {

                        // Creating a marker
                        MarkerOptions markerOptions = new MarkerOptions();

                        // Setting the position for the marker
                        markerOptions.position(latLng);

                        // Setting the title for the marker.
                        // This will be displayed on taping the marker
                        markerOptions.title(latLng.latitude + " : " + latLng.longitude);
                        lat_dbl = latLng.latitude;
                        long_dbl = latLng.longitude;
                        // Clears the previously touched position
                        mMap.clear();

                        // Animating to the touched position
                        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                        // Placing a marker on the touched position
                        mMap.addMarker(markerOptions);
                        get_Adrz();


                    }
                });
            } catch (Exception ex) {
                Log.v("onMapClick", ex.getMessage().toString());
            }
        } catch (Exception ex) {
            Log.v("onMapReady", ex.getMessage().toString());
        }
    }

    public void get_Adrz() {

        //Getting Adrz
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat_dbl, long_dbl, 5); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state = addresses.get(0).getAdminArea();
            postalCode = addresses.get(0).getPostalCode();
            country = addresses.get(0).getCountryName();
            country = country.toLowerCase();
            Log.v("country", country);
            knownName = addresses.get(0).getFeatureName();
            if (TextUtils.isEmpty(address)) {
                //  edt_adrz.setVisibility(View.GONE);
                return; // or break, continue, throw
            } else {
                edt_adrz.setVisibility(View.VISIBLE);
                edt_adrz.setText(address);
                full_Adrz = address;
            }


            if (TextUtils.isEmpty(city)) {
                // edt_city.setVisibility(View.GONE);
                return; // or break, continue, throw
            } else {
                edt_city.setVisibility(View.VISIBLE);
                edt_city.setText(city);
                full_Adrz = address + "" + city;
            }

            if (TextUtils.isEmpty(state)) {
                //  edt_location.setVisibility(View.GONE);
                return; // or break, continue, throw
            } else {
                edt_location.setVisibility(View.VISIBLE);
                edt_location.setText(state);
                full_Adrz = address + "" + city + "," + state;
            }


//            if (TextUtils.isEmpty(postalCode)) {
//                //  edt_pin.setVisibility(View.GONE);
//                return; // or break, continue, throw
//            } else {
//                edt_pin.setVisibility(View.VISIBLE);
//                edt_pin.setText(postalCode);
//            }


//                    if(postalCode.equals("")||postalCode.equals(null))
//                    {
//                        edt_pin.setVisibility(View.GONE);
//                    }
//                    else
//                    {
//                        edt_pin.setVisibility(View.VISIBLE);
//                        edt_pin.setText(postalCode);
//                    }


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}