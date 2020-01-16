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
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.app.service.Activity.LogInActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;


public class Change_pwd_Fragment extends Fragment {
    View rootView;
    EditText edt_Current_Pwd,edt_New_Pwd,edt_Cnfrm_Pwd;
    Button btn_Change_Pwd;
    String str_current_pwd,str_new_pwd;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    String login_token;
    String JSON_URL_UPDATE_PWD;
    Utilities utilities;


    public Change_pwd_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {
        rootView = inflater.inflate(R.layout.fragment_change_pwd_, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
        edt_Current_Pwd=(EditText)rootView.findViewById(R.id.edt_Current_Pwd);
        edt_New_Pwd=(EditText)rootView.findViewById(R.id.edt_New_Pwd);
        edt_Cnfrm_Pwd=(EditText)rootView.findViewById(R.id.edt_Cnfrm_Pwd);
        btn_Change_Pwd=(Button)rootView.findViewById(R.id.btn_Change_Pwd);
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        int logedIn = preferences.getInt("logedIn", 0);
        Log.v("logedIn",""+logedIn);


        if (logedIn == 1) {
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                    new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//            TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//            txt_actionbar_Title.setText("SERVICIO");
//            ImageView img_search_actionbar=(ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//            img_search_actionbar.setImageResource(R.drawable.back_btn);
//            img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().onBackPressed();
//                }
//            });
            login_token = preferences.getString("login_token", null);
            Log.v("login_token", login_token);

            btn_Change_Pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitForm();
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });

        }
        else {
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
        else
        {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }
    private void submitForm() {

        if (!validateCurrentPassword()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }
        if (!validateCnfrmPassword()) {
            return;
        }

        Update_Password_api();
    }

    private boolean validateCurrentPassword() {
        if (edt_Current_Pwd.getText().toString().trim().isEmpty()) {
            edt_Current_Pwd.setError(getString(R.string.validPassword));
            requestFocus(edt_Current_Pwd);
            return false;
        }

        return true;
    }
    private boolean validateNewPassword() {
        if (edt_New_Pwd.getText().toString().trim().isEmpty()) {
            edt_New_Pwd.setError(getString(R.string.validPassword));
            requestFocus(edt_New_Pwd);
            return false;
        }
        if(TextUtils.isEmpty(edt_New_Pwd.getText().toString()) || edt_New_Pwd.getText().toString().length() < 8)
        {
            edt_New_Pwd.setError(getString(R.string.validPwdLength));
            requestFocus(edt_New_Pwd);
            return false;
        }

        return true;
    }
    private boolean validateCnfrmPassword() {
        if (edt_Cnfrm_Pwd.getText().toString().trim().isEmpty()) {
            edt_Cnfrm_Pwd.setError(getString(R.string.validConfrmPwd));
            requestFocus(edt_Cnfrm_Pwd);
            return false;
        }
        else
        {
            if (edt_New_Pwd.getText().toString().trim().toLowerCase().indexOf(edt_Cnfrm_Pwd.getText().toString().trim().toLowerCase()) != 0) {

                edt_Cnfrm_Pwd.setError(getString(R.string.validPwdMissMatch));
                edt_Cnfrm_Pwd.requestFocus();
                return false;
            }
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void Update_Password_api() {
        //creating a string request to send request to the url
        str_current_pwd = edt_Current_Pwd.getText().toString();
        str_new_pwd = edt_New_Pwd.getText().toString();


        JSON_URL_UPDATE_PWD=utilities.GetUrl()+"accounts/update?type=password&current-password="+str_current_pwd+"&password="+str_new_pwd;

        Log.v("login_token1", login_token);
        Log.v("JSON_URL_UPDATE_PWD", JSON_URL_UPDATE_PWD);
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_UPDATE_PWD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response", response);
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            Log.v("response", response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    String msg = obj.getString("msg");
                                    login_token=obj.getString("login_token");
                                    Log.v("new_token", login_token);
                                    editor.putString("login_token", login_token);
                                    editor.commit();
                                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    Log.v("msg", msg);

                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Password is not updated", Toast.LENGTH_SHORT).show();

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
