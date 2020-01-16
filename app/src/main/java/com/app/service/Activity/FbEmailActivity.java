package com.app.service.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class FbEmailActivity extends AppCompatActivity {

    EditText edtFbEmail;
    Button btnFbEmail;
    String strNameFB, strEmailFB, strfbIDFB, straccessTokenFB, firebasseRegID,logInMetod;
    Utilities utilities;
    String login_token;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    LogInActivity logInActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fb_email);
        edtFbEmail = (EditText) findViewById(R.id.edtFbEmail);
        btnFbEmail = (Button) findViewById(R.id.btnFbEmail);


        Intent intent = getIntent();
        logInMetod= intent.getStringExtra("logInMetod");
        strNameFB = intent.getStringExtra("name");
        strfbIDFB = intent.getStringExtra("fb_id");
        straccessTokenFB = intent.getStringExtra("token");
        firebasseRegID = intent.getStringExtra("firebasseRegID");

        btnFbEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });


    }

    private void submitForm() {

        if (!validateEmail()) {
            return;
        }
        if (strNameFB != null && !strNameFB.isEmpty() && !strNameFB.equals("null")) {
            if (strfbIDFB != null && !strfbIDFB.isEmpty() && !strfbIDFB.equals("null")) {
                if (straccessTokenFB != null && !straccessTokenFB.isEmpty() && !straccessTokenFB.equals("null")) {
                    FB_API();
                }
            }

        }


    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateEmail() {
        strEmailFB = edtFbEmail.getText().toString();


        if (strEmailFB.isEmpty() || !isValidEmail(strEmailFB)) {
            edtFbEmail.setError(getString(R.string.validEmail));
            requestFocus(edtFbEmail);
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
       String URL_FB = utilities.GetUrl() + "sign-up-sm";

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



                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {
                                        JSONObject user = obj.getJSONObject("user");
                                        String email = user.getString("email");
                                        if (email == null && email.isEmpty() && email.equals("null")) {
                                            editor.putInt("fb_email", 0);
                                        }
                                        Intent in = new Intent(FbEmailActivity.this, SignUpDetailsSocialLogIn.class);
                                        startActivity(in);
                                        editor.putString("logInMetod", "fb");
                                        editor.commit();
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
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> params = new HashMap<>();
                params.put("name", strNameFB);
                params.put("email", strEmailFB);
                //params.put("phone", "");
                params.put("customertype", "");
                params.put("fb_id", strfbIDFB);
                params.put("token", straccessTokenFB);

                editor.putString("name_fb", strNameFB);
                editor.putString("email_fb", strEmailFB);
                editor.putString("fbID", strfbIDFB);
                editor.putString("accessToken_str", straccessTokenFB);
                editor.commit();


                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }


    public void DeviceChkApi() {
        //creating a string request to send request to the url

        String URL_DEVICE_REG = utilities.GetUrl() + "accounts/registerdevice";

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

                            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgDCFB", msg);
                                    editor.putString("logInMetod", "fb");
                                    editor.commit();
                                    Intent in = new Intent(FbEmailActivity.this, SignUpDetailsSocialLogIn.class);
                                    startActivity(in);
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

    @Override
    public void onBackPressed() {
        LoginManager.getInstance().logOut();
        // your code.
    }
}
