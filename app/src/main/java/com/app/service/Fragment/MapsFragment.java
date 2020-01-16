package com.app.service.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.service.Activity.MainActivity;
import com.app.service.Activity.MapsActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.drawer;

import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.app.service.Fragment.PlaceOrderFragment.newInstance;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button btnNextMap;
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
    String selectedLat = "0";
    String selectedLong = "0";
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Utilities utilities;
    int PERMISSION_ALL = 1;
    String countryName;
    String[] PERMISSIONS = {
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,

    };
    View rootView;
    ImageView mapMenu, mapBack;

    public MapsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {

            if (utilities.isOnline(getActivity())) {
                rootView = inflater.inflate(R.layout.fragment_maps, container, false);
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                if (!hasPermissions(getActivity().getApplicationContext(), PERMISSIONS)) {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);
                }

                realativeActionBar.setVisibility(View.GONE);
                backArrow.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
                btnNextMap = (Button) rootView.findViewById(R.id.btnNextMap);
                mapMenu = (ImageView) rootView.findViewById(R.id.mapMenu);
                mapBack = (ImageView) rootView.findViewById(R.id.mapBack);
                mapBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getActivity().onBackPressed();
                    }
                });
                mapMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        drawer.openDrawer(GravityCompat.START);
                    }
                });
                btnNextMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (selected_CountryName.equals(country)) {
                            btnNextOnclick();
                        } else {
                            Toast.makeText(getActivity(), "Sorry no service available in selected location...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                preferences = getActivity().getApplicationContext().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                selected_CountryName = preferences.getString("countryName", "");
                selected_CountryName = selected_CountryName.toLowerCase();
                Log.v("selected_CountryName", selected_CountryName);
                selectedLat = preferences.getString("countryLat", "0");
                selectedLong = preferences.getString("countryLon", "0");
                Log.v("selectedLat", selectedLat);
                Log.v("selectedLong", selectedLong);
//                Log.v("selectedLatt", selectedLat);
//                Log.v("selectedLongg", selectedLong);
//                Log.v("countryName", countryName);
                // Obtain the SupportMapFragment and get notified when the map is ready to be used.


                SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                        .findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);


                edt_city = (EditText) rootView.findViewById(R.id.edt_city);
                edt_adrz = (EditText) rootView.findViewById(R.id.edt_adrz);
                edt_location = (EditText) rootView.findViewById(R.id.edt_location);
                edt_pin = (EditText) rootView.findViewById(R.id.edt_pin);

                edt_city.setVisibility(View.VISIBLE);
                edt_adrz.setVisibility(View.VISIBLE);
                edt_location.setVisibility(View.VISIBLE);
                edt_pin.setVisibility(View.VISIBLE);

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
//                btnNextMap.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                        if (selected_CountryName.equals(country)) {
////                            btnNextOnclick();
////                        } else {
////                            Toast.makeText(getActivity(), "Sorry no service available in selected location...", Toast.LENGTH_SHORT).show();
////                        }
//
//
//                    }
//                });

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
                rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
            }
        } catch (Exception ex) {
            Log.v("OnCreateMap", ex.getMessage().toString());
        }
        return rootView;
    }

    public void btnNextOnclick() {
        try {


            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
            if (full_Adrz.equals(null)) {
                Toast.makeText(getActivity(), "Address is required", Toast.LENGTH_SHORT).show();
                Log.v("e1", "Adrz not avail");
                if (lat_dbl.equals(0)) {
                    Toast.makeText(getActivity(), "Address is required", Toast.LENGTH_SHORT).show();
                    Log.v("e2", "Adrz not avail");
                    if (long_dbl.equals(0)) {
                        Toast.makeText(getActivity(), "Address is required", Toast.LENGTH_SHORT).show();
                        Log.v("e3", "Adrz not avail");
                    }
                }
            } else {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("full_Adrz", full_Adrz);
                editor.putString("lat_str", "" + lat_dbl);
                editor.putString("long_str", "" + long_dbl);
                editor.commit();
                Fragment fragment =  PlaceOrderFragment.newInstance(full_Adrz,""+lat_dbl,""+long_dbl);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null).commit();


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
        Log.v("requestCode", "" + requestCode);
        if (requestCode == PERMISSION_ALL) {
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
            lat_dbl = Double.parseDouble(selectedLat);
            long_dbl = Double.parseDouble(selectedLong);
            Log.v("doha_Lat", "" + lat_dbl);
            Log.v("doha_Lon", "" + long_dbl);
            mMap.addMarker(new MarkerOptions().position(doha).title("Doha"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat_dbl, long_dbl), 10.0f));

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


            if (TextUtils.isEmpty(postalCode)) {
                //  edt_pin.setVisibility(View.GONE);
                return; // or break, continue, throw
            } else {
                edt_pin.setVisibility(View.VISIBLE);
                edt_pin.setText(postalCode);
            }


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
