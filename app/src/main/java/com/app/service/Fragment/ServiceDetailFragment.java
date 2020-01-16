package com.app.service.Fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Activity.MapsActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import cc.cloudist.acplibrary.ACProgressFlower;

import static android.content.Context.MODE_PRIVATE;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.app.service.Adapter.Main_Menu_Adapter.MY_PREFS_NAME;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ServiceDetailFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Button btn_nxt_detail;
    EditText edt_Slct_Time, edt_Slct_Date;

    private int mYear, mMonth, mDay, mHour, mMinute;
    final Calendar myCalendar = Calendar.getInstance();
    String selectedDate_str, selected_Time_str, servicedate_API, servicetime_API;
    String productId_str, countryID;
    String JSON_URL_PRODUCT_DETAILS;
    Utilities utilities;
    TextView txt_Desc, txt_serviceDtl_title, txtPaymentMethod, txtEmergencyServicePrice, txtEmergencyServiceMsg;
    Button btShowmore;
    Boolean expand = false;
    CheckBox checkEmergencyService;
    boolean flagRadio;
    LinearLayout linearRadio;
    String emergency_price, emergencyServiceMessage;
    String selectedLang;
    String selectedPercentage;
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ServiceDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (utilities.isOnline(getActivity())) {
            try {
                // Inflate the layout for this fragment
                rootView = inflater.inflate(R.layout.fragment_service_detail, container, false);
                selectedLang = Hawk.get("selectedLang", "en");
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

                txt_serviceDtl_title = (TextView) rootView.findViewById(R.id.txt_serviceDtl_title);
                txtPaymentMethod = (TextView) rootView.findViewById(R.id.txtPaymentMethod);
                txtEmergencyServicePrice = (TextView) rootView.findViewById(R.id.txtEmergencyServicePrice);
                txtEmergencyServiceMsg = (TextView) rootView.findViewById(R.id.txtEmergencyServiceMsg);
                txtEmergencyServiceMsg.setVisibility(View.GONE);
                txt_Desc = (TextView) rootView.findViewById(R.id.txt_Desc);
                btShowmore = (Button) rootView.findViewById(R.id.btShowmore);
                btShowmore.setText("Read More");
                SharedPreferences prefs = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
                productId_str = prefs.getString("subProductId", "0"); //0 is the default value.
                selectedPercentage=Hawk.get("selectedPercentage","");
                Log.v("selectedPercentageSD", selectedPercentage);
                Log.v("productId_str", productId_str);
                countryID = prefs.getString("countryID", "0");
                Log.v("countryID", countryID);
                JSON_URL_PRODUCT_DETAILS = utilities.GetUrl() + "product/" + productId_str;
                btn_nxt_detail = (Button) rootView.findViewById(R.id.btn_nxt_detail);
                edt_Slct_Time = (EditText) rootView.findViewById(R.id.edt_Slct_Time);
                edt_Slct_Date = (EditText) rootView.findViewById(R.id.edt_Slct_Date);
                checkEmergencyService = (CheckBox) rootView.findViewById(R.id.checkEmergencyService);
                checkEmergencyService.setOnClickListener(this);


//                img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
////                    SharedPreferences.Editor editor =getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
////
////                    editor.putString("productId", productId_str);
////                    editor.apply();
//                        backSubMenu();
//                    }
//                });

                edt_Slct_Date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentDate = Calendar.getInstance();
                        mYear = mcurrentDate.get(Calendar.YEAR);
                        mMonth = mcurrentDate.get(Calendar.MONTH);
                        mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                        DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

                            public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                // TODO Auto-generated method stub
                                /*      Your code   to get date and time    */

                                String selectedyear_str = "" + selectedyear;
                                selectedmonth = selectedmonth + 1;
                                String selectedmonth_str = "" + String.format("%02d", selectedmonth);
                                String selectedday_str = "" + String.format("%02d", selectedday);

                                edt_Slct_Date.setText(selectedday_str + "/" + selectedmonth_str + "/" + selectedyear_str);
                                servicedate_API = selectedyear_str + "/" + selectedmonth_str + "/" + selectedday_str;

                            }
                        }, mYear, mMonth, mDay);
                        mDatePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

                        mDatePicker.setTitle("Select date");
                        mDatePicker.show();

                        edt_Slct_Time.setText("Select Time");

                    }


                });

                edt_Slct_Time.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar mcurrentTime = Calendar.getInstance();
                        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                        int minute = mcurrentTime.get(Calendar.MINUTE);
                        TimePickerDialog mTimePicker;
                        mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {


                                // Selected Time
                                String selected12Time = "" + selectedHour + ":" + selectedMinute;
                                Log.v("selected12Time", selected12Time);
                                String[] splitSelectedTime = selected12Time.split(":");
                                selected12Time = splitSelectedTime[0];
                                String selected12Hour = splitSelectedTime[1];

                                //***Current Time

                                String current12Time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                                String[] current12Split = current12Time.split(":");
                                int currentHour = Integer.parseInt(current12Split[0]);
                                int currentHour12 = currentHour + 3;
                                int currentMinute = Integer.parseInt(current12Split[1]);


                                //Current Date

                                Date c = Calendar.getInstance().getTime();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                String formattedDate = df.format(c);
                                Log.v("formattedDateCR", formattedDate);

                                //Selected Date
                                String SelectedDate = edt_Slct_Date.getText().toString().trim();

                                //Date Comparison

                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                Date strDate = null;
                                try {
                                    strDate = sdf.parse(SelectedDate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                if (System.currentTimeMillis() < strDate.getTime()) {
                                    String am_pm;
                                    if (selectedHour > 12) {
                                        selectedHour = selectedHour - 12;
                                        am_pm = "PM";
                                    } else {
                                        am_pm = "AM";
                                    }

                                    edt_Slct_Time.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                                    edt_Slct_Time.setError(null);
                                    servicetime_API = selectedHour + ":" + selectedMinute + " " + am_pm;
                                    Log.v("servicetime_API_normal", servicetime_API);

                                    SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                                    SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                    Date date = null;
                                    try {
                                        date = parseFormat.parse(servicetime_API);
                                    } catch (ParseException e) {
                                        Log.v("Error", e.getMessage().toString());
                                        e.printStackTrace();
                                    }
                                    servicetime_API = displayFormat.format(date);
                                    Log.v("servicetime_API_Resume", servicetime_API);
                                    Log.v("DateCR", "true");
                                } else {
                                    Log.v("DateCR", "false");
                                    if (Integer.parseInt(selected12Time) != currentHour12) {

                                        if (Integer.parseInt(selected12Time) > currentHour12) {

                                            String am_pm;
                                            if (selectedHour > 12) {
                                                selectedHour = selectedHour - 12;
                                                am_pm = "PM";
                                            } else {
                                                am_pm = "AM";
                                            }

                                            edt_Slct_Time.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                                            edt_Slct_Time.setError(null);
                                            servicetime_API = selectedHour + ":" + selectedMinute + " " + am_pm;
                                            Log.v("servicetime_API_normal", servicetime_API);

                                            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                                            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                            Date date = null;
                                            try {
                                                date = parseFormat.parse(servicetime_API);
                                            } catch (ParseException e) {
                                                Log.v("Error", e.getMessage().toString());
                                                e.printStackTrace();
                                            }
                                            servicetime_API = displayFormat.format(date);
                                            Log.v("servicetime_API_Resume", servicetime_API);
                                        } else {
                                            edt_Slct_Time.setText("Select Time");
                                            Toast.makeText(getApplicationContext(), "Service only available 3 hours later from the current time", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (Integer.parseInt(selected12Hour) > currentMinute) {
                                            String am_pm;
                                            if (selectedHour > 12) {
                                                selectedHour = selectedHour - 12;
                                                am_pm = "PM";
                                            } else {
                                                am_pm = "AM";
                                            }

                                            edt_Slct_Time.setText(selectedHour + ":" + selectedMinute + " " + am_pm);
                                            edt_Slct_Time.setError(null);
                                            servicetime_API = selectedHour + ":" + selectedMinute + " " + am_pm;
                                            Log.v("servicetime_API_normal", servicetime_API);

                                            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
                                            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                                            Date date = null;
                                            try {
                                                date = parseFormat.parse(servicetime_API);
                                            } catch (ParseException e) {
                                                Log.v("Error", e.getMessage().toString());
                                                e.printStackTrace();
                                            }
                                            servicetime_API = displayFormat.format(date);
                                            Log.v("servicetime_API_Resume", servicetime_API);
                                        } else {
                                            edt_Slct_Time.setText("Select Time");
                                            Toast.makeText(getApplicationContext(), "Service only available 3 hours later from the current time", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }


                            }
                        }, hour, minute, false);//Yes 24 hour time
                        mTimePicker.setTitle("Select Time");
                        mTimePicker.show();


                    }
                });

                btn_nxt_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        btnNextClick();
                    }
                });
                if (selectedLang.equals("en")) {
                    loadProductDetails_apiEN();
                } else {
                    loadProductDetails_apiAR();
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

    public void btnNextClick() {
        try {

            selectedDate_str = edt_Slct_Date.getText().toString();
            selected_Time_str = edt_Slct_Time.getText().toString();

            if (checkEmergencyService.isChecked()) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                editor.putString("servicedate", "");
                editor.putString("servicetime", "");
                editor.putString("sale_price", emergency_price);
                editor.putString("flagemergency", "yes");

                editor.apply();
                Fragment fragment =  new MapsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else {
                if (selected_Time_str.equals(getString(R.string.select_time))) {
                    edt_Slct_Time.setError("Please select time");
                    edt_Slct_Time.setText("");
                } else if (selectedDate_str.equals(getString(R.string.select_date))) {
                    edt_Slct_Date.setError("Please select a date");
                    edt_Slct_Date.setText("");

                } else {

                    SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();

                    editor.putString("servicedate", servicedate_API);
                    editor.putString("servicetime", servicetime_API);
                    editor.putString("flagemergency", "no");
                    Log.v("servicedate_API", servicedate_API);
                    Log.v("servicetime_API", servicetime_API);
                    editor.apply();
                    Fragment fragment =  new MapsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("btn_nxt_detail", msg);
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        Log.v("requestCode",""+requestCode);
//        if(requestCode==PERMISSION_ALL)
//        {
//            Toast.makeText(getActivity(),"HIIIIoo",Toast.LENGTH_SHORT).show();
//        }
//    }


    public void loadProductDetails_apiEN() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("PRODUCT_DETAILS", JSON_URL_PRODUCT_DETAILS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);
                            String status = obj.getString("status");

                            if (status.equals("200")) {

                                dialog.dismiss();
                                JSONObject obj_products = obj.getJSONObject("product");
                                String id = obj_products.getString("id");
                                String title = obj_products.getString("name");
                                String description = obj_products.getString("description");
                                String payment = obj_products.getString("payment");
                                String priceAPI = obj_products.getString("price");
                                emergency_price = obj_products.getString("sale_price");
                                emergencyServiceMessage = obj_products.getString("sku");
                                Log.v("priceAPI", priceAPI);
                                Log.v("payment", payment);
                                if (countryID.equals("174")) {


                                    txtPaymentMethod.setText(payment + " : " + priceAPI + " " + getString(R.string.Qar));
                                } else {
                                    try {
                                        Log.v("slctdPercentageDetail", selectedPercentage);
                                        Double qarAPI = Double.parseDouble(priceAPI);
                                     Double   priceFinal =(qarAPI * 0.27);
                                        Log.v("priceFinal", ""+priceFinal);
                                        priceFinal=priceFinal*(Double.parseDouble(selectedPercentage)/100);
                                        String  decimalPrice=decimalFormat.format(priceFinal);
                                        Log.v("decimalPrice", ""+decimalPrice);
                                        txtPaymentMethod.setText(payment + " : " + decimalPrice + " USD");
                                        Log.v("priceDetail", ""+priceFinal);
                                    } catch (Exception ex) {
                                        Log.v("Dollar", ex.getMessage().toString());
                                    }
                                }
                                if (title != null && !title.isEmpty() && !title.equals("null")) {
                                    txt_serviceDtl_title.setText(title);
                                }

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("subService", title);
                                editor.commit();

                                if (description != null && !description.isEmpty() && !description.equals("null")) {
                                    txt_Desc.setText(Html.fromHtml(description));
                                }

                                Log.v("getLineCount", "" + txt_Desc.getLineCount());
                                btShowmore.setVisibility(View.GONE);
                                txt_Desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        Log.v("btnSeeMore", "" + txt_Desc.getLineCount());
                                        if (txt_Desc.getLineCount() > 10) {
                                            btShowmore.setVisibility(View.VISIBLE);


                                        } else {
                                            btShowmore.setVisibility(View.GONE);

                                        }
                                    }
                                });


                                btShowmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (btShowmore.getText().toString().equalsIgnoreCase("Read More")) {
                                            txt_Desc.setMaxLines(Integer.MAX_VALUE);//your TextView
                                            btShowmore.setText("Read Less");
                                        } else {
                                            txt_Desc.setMaxLines(10);//your TextView
                                            btShowmore.setText("Read More");
                                        }


                                    }
                                });


                                JSON_URL_PRODUCT_DETAILS = "";

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

    public void loadProductDetails_apiAR() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Log.v("PRODUCT_DETAILS", JSON_URL_PRODUCT_DETAILS);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_PRODUCT_DETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);
                            String status = obj.getString("status");

                            if (status.equals("200")) {

                                dialog.dismiss();
                                JSONObject obj_products = obj.getJSONObject("product");
                                String id = obj_products.getString("id");
                                String title = obj_products.getString("name_ar");
                                String description = obj_products.getString("description_ar");
                                String payment = obj_products.getString("payment");
                                String price = obj_products.getString("price");
                                emergency_price = obj_products.getString("sale_price");
                                emergencyServiceMessage = obj_products.getString("sku_ar");
                                Log.v("price", price);
                                Log.v("payment", payment);
                                if (countryID.equals("174")) {


                                    txtPaymentMethod.setText(payment + " : " + price + " " + getString(R.string.Qar));
                                } else {
                                    try {
                                        Double qar = Double.parseDouble(price);
                                        price = "" + (qar * 0.27);
                                        Log.v("priceUSD", price);
                                        txtPaymentMethod.setText(payment + " : " + price + " USD");
                                    } catch (Exception ex) {
                                        Log.v("Dollar", ex.getMessage().toString());
                                    }
                                }
                                if (title != null && !title.isEmpty() && !title.equals("null")) {
                                    txt_serviceDtl_title.setText(title);
                                }

                                SharedPreferences.Editor editor = getActivity().getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                                editor.putString("subService", title);
                                editor.commit();

//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    txt_Desc.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
//                                } else {
//                                    txt_Desc.setText(Html.fromHtml(description));
//                                }

                                if (description != null && !description.isEmpty() && !description.equals("null")) {
                                    txt_Desc.setText(Html.fromHtml(description));
                                }

                                Log.v("getLineCount", "" + txt_Desc.getLineCount());
                                btShowmore.setVisibility(View.GONE);
                                txt_Desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                                    @Override
                                    public void onGlobalLayout() {
                                        Log.v("btnSeeMore", "" + txt_Desc.getLineCount());
                                        if (txt_Desc.getLineCount() > 10) {
                                            btShowmore.setVisibility(View.VISIBLE);


                                        } else {
                                            btShowmore.setVisibility(View.GONE);

                                        }
                                    }
                                });


                                btShowmore.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {


                                        if (btShowmore.getText().toString().equalsIgnoreCase("Read More")) {
                                            txt_Desc.setMaxLines(Integer.MAX_VALUE);//your TextView
                                            btShowmore.setText("Read Less");
                                        } else {
                                            txt_Desc.setMaxLines(10);//your TextView
                                            btShowmore.setText("Read More");
                                        }


                                    }
                                });


                                JSON_URL_PRODUCT_DETAILS = "";

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

    @Override
    public void onResume() {
        Log.e("DEBUG", "onResume of Service Detail");
        btn_nxt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnNextClick();
            }
        });
        //loadItems_api();
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.e("DEBUG", "OnPause of Service Detail\"");
        btn_nxt_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnNextClick();
            }
        });
        super.onPause();
    }


    @Override
    public void onClick(View v) {
        if (checkEmergencyService.isChecked()) {

            txtPaymentMethod.setVisibility(View.GONE);
            txtEmergencyServicePrice.setVisibility(View.VISIBLE);
            edt_Slct_Time.setVisibility(View.GONE);
            edt_Slct_Date.setVisibility(View.GONE);
            txtEmergencyServiceMsg.setVisibility(View.VISIBLE);
            if (emergencyServiceMessage != null && !emergencyServiceMessage.isEmpty() && !emergencyServiceMessage.equals("null")) {
                txtEmergencyServiceMsg.setText(emergencyServiceMessage);
            }
            if (emergency_price != null && !emergency_price.isEmpty() && !emergency_price.equals("null")) {
                if (countryID.equals("174")) {

                    txtEmergencyServicePrice.setText(getString(R.string.consultation_charge_emergency)+ " " + emergency_price + " " + "" + getString(R.string.Qar));

                } else {
                    try {
                        Log.v("slctdPercentageDetail", selectedPercentage);
                        Double qarAPI = Double.parseDouble(emergency_price);
                        Double   priceFinal =(qarAPI * 0.27);
                        Log.v("priceFinal", ""+priceFinal);
                        priceFinal=priceFinal*(Double.parseDouble(selectedPercentage)/100);
                        txtPaymentMethod.setText(getString(R.string.consultation_charge_emergency) + " : " + decimalFormat.format(priceFinal) + " USD");
                        Log.v("priceDetail", ""+priceFinal);
                    } catch (Exception ex) {
                        Log.v("Dollar", ex.getMessage().toString());
                    }
                }
                // txtEmergencyServicePrice.setText(getString(R.string.consultation_charge_emergency)+ " " + emergency_price + " " + "" + getString(R.string.Qar));
            }

        } else {
            txtPaymentMethod.setVisibility(View.VISIBLE);
            txtEmergencyServicePrice.setVisibility(View.GONE);
            edt_Slct_Time.setVisibility(View.VISIBLE);
            edt_Slct_Date.setVisibility(View.VISIBLE);
            txtEmergencyServiceMsg.setVisibility(View.GONE);
        }
    }


}

