package com.app.service.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SelectCountry_Activity extends AppCompatActivity {
    Dialog country_Dialog;
    ListView listView_Country;
    List<String> country_Values;
    List<String> country_Values_ID;
    List<String> countryPercentageList;

    List<String> country_lat;
    List<String> country_lon;

    EditText edt_Country;
    Button btn_Next;

    String selected_CountryID = "0";
    String selectedLat="0";
    String selectedLong="0";
    String selectedPercentage="0";
    String selected_CountryName = "";
    String JSON_URL_COUNTRY;
    Utilities utilities;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (utilities.isOnline(this)) {
            setContentView(R.layout.activity_select_country_);
            Hawk.init(this)
                    .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                    .setStorage(HawkBuilder.newSqliteStorage(this))
                    .setLogLevel(LogLevel.FULL)
                    .build();
            edt_Country = (EditText) findViewById(R.id.edt_Country);
            JSON_URL_COUNTRY = utilities.GetUrl() + "countries";
            preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            selected_CountryID = preferences.getString("countryID", "0");
            selected_CountryName = preferences.getString("countryName", "0");
            selectedLat = preferences.getString("countryLat", "0");
            selectedLong = preferences.getString("countryLon", "0");
            btn_Next = (Button) findViewById(R.id.btn_Next);
            btn_Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("countryID", selected_CountryID);
                    editor.putString("countryName", selected_CountryName);
                    editor.putString("countryLat", selectedLat);
                    editor.putString("countryLon", selectedLong);

                    Hawk.put("selectedPercentage",selectedPercentage);
                    Log.v("btnPercent",selectedPercentage);


                    editor.commit();
                    Intent in = new Intent(SelectCountry_Activity.this, MainActivity.class);
                    startActivity(in);
                    finish();
                }
            });


            country_Values = new ArrayList<>();
            country_Values_ID = new ArrayList<>();
            countryPercentageList=new ArrayList<>();
            country_lat = new ArrayList<>();
            country_lon = new ArrayList<>();
            country_Dialog = new Dialog(SelectCountry_Activity.this);
            country_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            country_Dialog.setContentView(R.layout.dialog_country);
            country_Dialog.setCanceledOnTouchOutside(false);
            listView_Country = (ListView) country_Dialog.findViewById(R.id.listView_Country);
            // Create an ArrayAdapter from List
            ArrayAdapter<String> adapter_Country = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, country_Values) {
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
                            selectedLat=country_lat.get(i);
                            selectedLong=country_lon.get(i);
                            selectedPercentage=countryPercentageList.get(i);
                            Log.v("InitialPercentage",selectedPercentage);
                            edt_Country.setText(str_CustomerType);

                        }

                    }


                    country_Dialog.dismiss();


                }
            });
            loadCountryList_api();
        } else {
            setContentView(R.layout.no_internet_msg_layout);
        }
    }

    public void set_Country_initial() {
        if (selected_CountryID.equals("0")) {

            selected_CountryID = country_Values_ID.get(2);
            selected_CountryName=country_Values.get(2);
            selectedLat=country_lat.get(2);
            selectedLong=country_lon.get(2);
            selectedPercentage=countryPercentageList.get(2);
            edt_Country.setText(selected_CountryName);

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
                                        String lat=jsondata.getString("latitude");
                                        String lon=jsondata.getString("longitude");
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
                                Toast.makeText(getApplicationContext(), "Server error, please contact the customer care service...", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
