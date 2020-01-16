package com.app.service.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Activity.MainActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;

public class SelectCountryFragment extends Fragment {

        View rootView;
    Dialog country_Dialog;
    ListView listView_Country;
    List<String> country_Values;
    List<String> country_Values_ID;
    List<String> country_lat;
    List<String> country_lon;
    List<String> countryPercentageList;
    EditText edt_Country;
    String selectedPercentage="0";
    Button btn_Next;

    String selected_CountryID = "0";
    String selected_CountryName="";
    String selectedLat="0";
    String selectedLong="0";
    String JSON_URL_COUNTRY;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    public SelectCountryFragment() {
            // Required empty public constructor
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            if (utilities.isOnline(getActivity())) {
            rootView = inflater.inflate(R.layout.fragment_select_country, container, false);
                realativeActionBar.setVisibility(View.VISIBLE);
                backArrow.setVisibility(View.VISIBLE);
                imgSearch.setVisibility(View.GONE);
                edt_Country = (EditText)rootView.findViewById(R.id.edt_Country);
                JSON_URL_COUNTRY = utilities.GetUrl() + "countries";
                preferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                selected_CountryID = preferences.getString("countryID", "0");
                selected_CountryName=preferences.getString("countryName","");
                selectedLat=preferences.getString("countryLat","");
                selectedLong=preferences.getString("countryLon","");
                btn_Next = (Button)rootView.findViewById(R.id.btn_Next);
                btn_Next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("countryID", selected_CountryID);
                        editor.putString("countryName", selected_CountryName);
                        editor.putString("countryLat", selectedLat);
                        editor.putString("countryLon", selectedLong);
                        Hawk.put("selectedPercentage",selectedPercentage);
                        Log.v("NextPercent",selectedPercentage);

                        editor.commit();
                        Intent in = new Intent(getActivity(), MainActivity.class);
                        startActivity(in);
                    }
                });


                country_Values = new ArrayList<>();
                country_Values_ID = new ArrayList<>();
                countryPercentageList=new ArrayList<>();
                country_lat = new ArrayList<>();
                country_lon = new ArrayList<>();
                country_Dialog = new Dialog(getActivity());
                country_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                country_Dialog.setContentView(R.layout.dialog_country);
                country_Dialog.setCanceledOnTouchOutside(false);
                listView_Country = (ListView) country_Dialog.findViewById(R.id.listView_Country);
                // Create an ArrayAdapter from List
                ArrayAdapter<String> adapter_Country = new ArrayAdapter<String>
                        (getActivity(), android.R.layout.simple_list_item_1, country_Values) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        // Get the Item from ListView
                        View view = super.getView(position, convertView, parent);

                        // Initialize a TextView for ListView each Item
                        TextView tv = (TextView) view.findViewById(android.R.id.text1);

                        // Set the text color of TextView (ListView Item)
                        tv.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

                        // Generate ListView Item using TextView
                        return view;
                    }
                };

                listView_Country.setAdapter(adapter_Country);
                edt_Country.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        country_Dialog.show();
                    }
                });
                listView_Country.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {

                        String str_CustomerType = (String) listView_Country.getItemAtPosition(position);
                        for (int i = 0; i < country_Values.size(); i++) {
                            if (str_CustomerType.equals(country_Values.get(i))) {
                                selected_CountryID = country_Values_ID.get(i);
                                selected_CountryName=country_Values.get(i);
                                selectedPercentage=countryPercentageList.get(i);
                                selectedLat=country_lat.get(i);
                                selectedLong=country_lon.get(i);
                                edt_Country.setText(str_CustomerType);

                            }

                        }


                        country_Dialog.dismiss();


                    }
                });
                loadCountryList_api();
            } else {
                rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);

            }
            return rootView;
        }
    public void set_Country_initial() {
        if (selected_CountryID.equals("0")) {
            edt_Country.setText(country_Values.get(2));
            selected_CountryID = country_Values_ID.get(2);
            selected_CountryName=country_Values.get(2);
            selectedPercentage=countryPercentageList.get(2);
            selectedLat=country_lat.get(2);
            selectedLong=country_lon.get(2);
            String has = country_Values_ID.get(2);
        } else {
            for (int i = 0; i < country_Values.size(); i++) {
                if (selected_CountryID.equals(country_Values_ID.get(i))) {
                    String country = country_Values.get(i);
                    edt_Country.setText(country);
                }

            }
        }


    }
    private void loadCountryList_api() {


        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_COUNTRY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                //so here we are getting that json array

                                String success = obj.getString("success");


                                if (success.equals("1")) {
                                    String msg = obj.getString("msg");
                                    JSONArray countryArray = obj.getJSONArray("countries");
                                    for (int i = 0; i < countryArray.length(); i++) {
                                        //getting the json object of the particular index inside the array
                                        JSONObject jsondata = countryArray.getJSONObject(i);
                                        String id = jsondata.getString("id");
                                        String name = jsondata.getString("name");
                                        String lat = jsondata.getString("latitude");
                                        String lon = jsondata.getString("longitude");
                                        String percentage=jsondata.getString("percentage");


                                        country_Values.add(name);
                                        country_Values_ID.add(id);
                                        countryPercentageList.add(percentage);
                                        country_lat.add(lat);
                                        country_lon.add(lon);


                                    }
                                }

                                set_Country_initial();

                                //now looping through all the elements of the json array


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
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
    }
