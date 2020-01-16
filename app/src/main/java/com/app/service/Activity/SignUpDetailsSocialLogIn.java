package com.app.service.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.app.service.Utilitiesa.VolleySingleton;
import com.facebook.login.LoginManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class SignUpDetailsSocialLogIn extends AppCompatActivity {
    Utilities utilities;
    Dialog customerType_Dialog;
    ListView listView_CustomerType;
    List<String> customerType_Values;
    EditText edt_customertype;
    SharedPreferences preferences;
    String logInMetod;
    String URL_FB, URL_GOOGLE, URL_DEVICE_REG;
    String login_token;
    String googleName, googleEmail, googleID, googleToken;
    String name_fb, email_fb, fbID, accessToken_str;
    String firebasseRegID;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    String customertype_str;
    Button btn_Next_SocaialDetails;
    int fb_email;
    EditText edt_email_fb;
    LinearLayout linear_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (utilities.isOnline(this)) {
            setContentView(R.layout.activity_sign_up_details_social_log_in);
            preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            logInMetod = preferences.getString("logInMetod", "");
            googleName = preferences.getString("googleName", "");
            googleEmail = preferences.getString("googleEmail", "");
            googleID = preferences.getString("googleID", "");
            googleToken = preferences.getString("googleToken", "");


            name_fb = preferences.getString("name_fb", "");
            email_fb = preferences.getString("email_fb", "");
            fbID = preferences.getString("fbID", "");
            accessToken_str = preferences.getString("accessToken_str", "");


            firebasseRegID = preferences.getString("regFireID", "0");

            linear_email = (LinearLayout) findViewById(R.id.linear_email);
            edt_email_fb = (EditText) findViewById(R.id.edt_email_fb);
            edt_customertype = (EditText) findViewById(R.id.edt_customertype);
            btn_Next_SocaialDetails = (Button) findViewById(R.id.btn_Next_SocaialDetails);

            fb_email = preferences.getInt("fb_email", 1);
            if (fb_email == 0) {
                linear_email.setVisibility(View.VISIBLE);
            }

            customerType_Values = new ArrayList<>();
            customerType_Values.add("Individual");
            customerType_Values.add("Business");
            edt_customertype.setText("Individual");

            btn_Next_SocaialDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (logInMetod.equals("gmail")) {
                        customertype_str = edt_customertype.getText().toString();
                        if (customertype_str != null && !customertype_str.isEmpty() && !customertype_str.equals("null")) {
                            googleLogIn();
                            Log.v("gmail", "gmail");
                        } else {
                            edt_customertype.setError("Select a customer type");
                        }

                    } else if (logInMetod.equals("fb")) {
                        if (fb_email == 0) {

                            linear_email.setVisibility(View.VISIBLE);
                            if (!validateEmail()) {
                                return;
                            }

                        }
                        customertype_str = edt_customertype.getText().toString();
                        if (customertype_str != null && !customertype_str.isEmpty() && !customertype_str.equals("null")) {
                            Log.v("fb", "fb");
                            FB_API();
                        } else {
                            edt_customertype.setError("Select a customer type");
                        }
                    }
                }
            });


            customerType_Dialog = new Dialog(SignUpDetailsSocialLogIn.this);
            customerType_Dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            customerType_Dialog.setContentView(R.layout.dialog_customertype);
            customerType_Dialog.setCanceledOnTouchOutside(false);
            listView_CustomerType = (ListView) customerType_Dialog.findViewById(R.id.listView_CustomerType);
            // Create an ArrayAdapter from List
            ArrayAdapter<String> adapter_BodyColor = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, customerType_Values) {
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

            listView_CustomerType.setAdapter(adapter_BodyColor);


//        // Capture ListView item click
            listView_CustomerType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String str_CustomerType = (String) listView_CustomerType.getItemAtPosition(position);
                    edt_customertype.setText(str_CustomerType);

                    customerType_Dialog.dismiss();


                }
            });

            edt_customertype.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerType_Dialog.show();
                }
            });
        } else {
            setContentView(R.layout.no_internet_msg_layout);
        }
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean validateEmail() {
        String email = edt_email_fb.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_email_fb.setError("Please enter a valid email");
            requestFocus(edt_email_fb);
            return false;
        }

        return true;
    }
    private void FB_API() {
        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_FB = utilities.GetUrl() + "sign-up-sm";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    //   String login_token = jsonObj.getString("login_token");
                                    String jh = login_token;
                                    Log.v("login_token_fb", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");
                                    String customertype = jsonObj.getString("customertype");
                                    editor.putString("customer_name", name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putString("customertype", customertype);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();

//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);
                                    LoginManager.getInstance().logOut();
                                    DeviceChkApi();

                                    finish();


                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", name_fb);
                params.put("email", email_fb);
                //params.put("phone", "");
                params.put("customertype", customertype_str);
                params.put("fb_id", fbID);
                params.put("token", accessToken_str);

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void googleLogIn() {
        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_GOOGLE = utilities.GetUrl() + "sign-up-google";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GOOGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    Log.v("login_token_fb", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");
                                    String customertype = jsonObj.getString("customertype");
                                    editor.putString("customer_name", name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putString("customertype", customertype);
                                    editor.putString("logInMetod", "gmail");
                                    Log.v("customertype_GL", customertype);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();
                                    DeviceChkApi();


                                   // Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_SHORT).show();
//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);
                                    finish();


                                } else if (success.equals("0")) {
                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {


                                        Log.v("Ok", "ok");

                                    }

                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String, String> params = new HashMap<>();
                params.put("name", googleName);
                params.put("email", googleEmail);
                // params.put("phone", "");
                params.put("customertype", customertype_str);
                params.put("gmail_id", googleID);
                params.put("token", googleToken);

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void DeviceChkApi() {
        //creating a string request to send request to the url

        URL_DEVICE_REG = utilities.GetUrl() + "accounts/registerdevice";

        //login_token = preferences.getString("login_token", "");


        // Log.v("login_token_LogIn", login_token);
//        Log.v("refreshedToken_LogIn", refreshedToken);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEVICE_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msg", msg);
                                    Intent in = new Intent(SignUpDetailsSocialLogIn.this, MainActivity.class);
                                    startActivity(in);


                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getApplicationContext(), "Server error", Toast.LENGTH_SHORT).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
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

                return headers;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", firebasseRegID);
                params.put("device_type", "Android");
                params.put("device_os", "Android");

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }
}
