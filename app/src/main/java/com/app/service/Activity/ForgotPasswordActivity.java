package com.app.service.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class ForgotPasswordActivity extends AppCompatActivity {

    TextView txt_Register;
    EditText edt_Email_ForgotPwd;
    String URL_RESET_PWD;
    Utilities utilities;
    Button btn_reset_pwd;
    String selectedLang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);
        selectedLang = Hawk.get("selectedLang", "en");
        URL_RESET_PWD=utilities.GetUrl()+"forgetpassword";
        txt_Register=(TextView)findViewById(R.id.txt_Register);
        edt_Email_ForgotPwd=(EditText) findViewById(R.id.edt_Email_ForgotPwd);
        btn_reset_pwd=(Button) findViewById(R.id.btn_reset_pwd);

        if(selectedLang.equals("en"))
        {
            edt_Email_ForgotPwd.setGravity(Gravity.LEFT);

        }
        else
        {
            edt_Email_ForgotPwd.setGravity(Gravity.RIGHT);
        }

        txt_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(ForgotPasswordActivity.this,SignUpActivity.class);
                startActivity(in);
            }
        });

        btn_reset_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
    }

    private void ResetPwd_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_RESET_PWD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    String msg=obj.getString("msg");
                                    Toast.makeText(getApplicationContext(),msg , Toast.LENGTH_SHORT).show();
                                    Log.v("msg",msg);
                                    finish();

                                } else {

                                    edt_Email_ForgotPwd.setText("");
                                    requestFocus(edt_Email_ForgotPwd);
                                   String error=obj.getString("error");
                                    Toast.makeText(getApplicationContext(), error, Toast.LENGTH_SHORT).show();
                                    Log.v("error",error);

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
                params.put("email", edt_Email_ForgotPwd.getText().toString().trim());
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

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
        String email = edt_Email_ForgotPwd.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_Email_ForgotPwd.setError(getString(R.string.validEmail));
            requestFocus(edt_Email_ForgotPwd);
            return false;
        }

        return true;
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }
        ResetPwd_api();
    }
}
