package com.app.service.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class SignUpActivity extends AppCompatActivity {
    TextView txt_SignIn;
    Button btn_Register;
    EditText edt_Name, edt_Email, edt_Phone, edt_Pwd, edtConfirmPassword, edt_customertype,edtLanguage;
    Dialog customerType_Dialog;
    ListView listView_CustomerType;
    List<String> customerType_Values;
    String URL_SIGNUP;
    Utilities utilities;
    String selectedLang;

    Dialog LangDialog;
    ListView listViewLang;
    List<String> langValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (utilities.isOnline(this)) {
            setContentView(R.layout.activity_sign_up);
            selectedLang = Hawk.get("selectedLang", "en");
            URL_SIGNUP = utilities.GetUrl() + "sign-up?";
            edt_Name = (EditText) findViewById(R.id.edt_Name);
            edt_Email = (EditText) findViewById(R.id.edt_Email);
            edt_Phone = (EditText) findViewById(R.id.edt_Phone);
            edt_Pwd = (EditText) findViewById(R.id.edt_Pwd);
            edtConfirmPassword = (EditText) findViewById(R.id.edtConfirmPassword);
            edt_customertype = (EditText) findViewById(R.id.edt_customertype);
            edtLanguage= (EditText) findViewById(R.id.edtLanguage);

            langValues = new ArrayList<>();
            langValues.add(getString(R.string.english));
            langValues.add(getString(R.string.arabic));

            LangDialog = new Dialog(SignUpActivity.this);
            LangDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            LangDialog.setContentView(R.layout.dilg_country);
            LangDialog.setCanceledOnTouchOutside(false);
            listViewLang = (ListView) LangDialog.findViewById(R.id.listViewLang);
            // Create an ArrayAdapter from List
            ArrayAdapter<String> adapterLang = new ArrayAdapter<String>
                    (this, android.R.layout.simple_list_item_1, langValues) {
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

            listViewLang.setAdapter(adapterLang);
            listViewLang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    String selectedLang = (String) listViewLang.getItemAtPosition(position);
                    edtLanguage.setText(selectedLang);

                    LangDialog.dismiss();


                }
            });

            edtLanguage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LangDialog.show();
                }
            });




            customerType_Values = new ArrayList<>();
            customerType_Values.add("Individual");
            customerType_Values.add("Business");

            customerType_Dialog = new Dialog(SignUpActivity.this);
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

            if (selectedLang.equals("en")) {
                edt_Name.setGravity(Gravity.LEFT);
                edt_Email.setGravity(Gravity.LEFT);
                edt_Phone.setGravity(Gravity.LEFT);
                edt_Pwd.setGravity(Gravity.LEFT);
                edt_customertype.setGravity(Gravity.LEFT);
                edtConfirmPassword.setGravity(Gravity.LEFT);


            } else {
                edt_Name.setGravity(Gravity.RIGHT);
                edt_Email.setGravity(Gravity.RIGHT);
                edt_Phone.setGravity(Gravity.RIGHT);
                edt_Pwd.setGravity(Gravity.RIGHT);
                edt_customertype.setGravity(Gravity.RIGHT);
                edtConfirmPassword.setGravity(Gravity.RIGHT);
            }
            btn_Register = (Button) findViewById(R.id.btn_Register);
            txt_SignIn = (TextView) findViewById(R.id.txt_SignIn);

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


            txt_SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(SignUpActivity.this, LogInActivity.class);
                    startActivity(in);
                }
            });
            btn_Register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitForm();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
        } else {
            setContentView(R.layout.no_internet_msg_layout);
        }

    }

    private void submitForm() {

        if (!validateName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }


        if (!validatePhone()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }
        if (!validateCnfrmPassword()) {
            return;
        }
        if (!validateCustomerType()) {
            return;
        }
        if (!validateLang()) {
            return;
        }
        UserRegistration_api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName() {
        if (edt_Name.getText().toString().trim().isEmpty()) {
            edt_Name.setError(getString(R.string.validName));
            requestFocus(edt_Name);
            return false;
        }

        return true;
    }

    private boolean validateEmail() {
        String email = edt_Email.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            edt_Email.setError(getString(R.string.validEmail));
            requestFocus(edt_Email);
            return false;
        }

        return true;
    }

    private boolean validatePhone() {
        if (edt_Phone.getText().toString().trim().isEmpty()) {
            edt_Phone.setError(getString(R.string.validPhone));
            requestFocus(edt_Phone);
            return false;
        }
        if (edt_Phone.getText().toString().length() < 10) {
            edt_Phone.setError("Please enter a valid phone number");
            requestFocus(edt_Phone);
            return false;
        }
        return true;
    }

    private boolean validatePassword() {
        if (edt_Pwd.getText().toString().trim().isEmpty()) {
            edt_Pwd.setError(getString(R.string.validPassword));
            requestFocus(edt_Pwd);
            return false;
        }

        if (TextUtils.isEmpty(edt_Pwd.getText().toString()) || edt_Pwd.getText().toString().length() < 8) {
            edt_Pwd.setError(getString(R.string.validPwdLength));
            requestFocus(edt_Pwd);
            return false;
        }
        return true;
    }

    private boolean validateCnfrmPassword() {
        String password = edt_Pwd.getText().toString();
        String confirmPassword = edtConfirmPassword.getText().toString();
        if (confirmPassword.isEmpty()) {
            edtConfirmPassword.setError(getString(R.string.validConfrmPwd));
            requestFocus(edtConfirmPassword);
            return false;
        }


        if (!password.equals(confirmPassword)) {
            edt_Pwd.setError(getString(R.string.validPwdMissMatch));
            requestFocus(edt_Pwd);
            edt_Pwd.setText("");
            edtConfirmPassword.setText("");


            return false;
        }
        return true;
    }

    private boolean validateCustomerType() {
        if (edt_customertype.getText().toString().trim().isEmpty()) {
            edt_customertype.setError("Select a customer type");
            requestFocus(edt_customertype);
            return false;
        }

        return true;
    }

    private boolean validateLang() {
        if (edtLanguage.getText().toString().trim().isEmpty()) {
            edtLanguage.setError("Select a Language");
            requestFocus(edtLanguage);
            return false;
        }

        return true;
    }


    private void UserRegistration_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        String selectedLang=edtLanguage.getText().toString();
        if(selectedLang.equals(getString(R.string.english)))
        {
            Hawk.put("selectedLang","en");
        }
        else
        {
            Hawk.put("selectedLang","ar");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SIGNUP,
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
                                    JSONObject user_obj = obj.getJSONObject("user");
                                    String user_id = user_obj.getString("id");
                                    String customer_name = user_obj.getString("name");
                                    String user_email = user_obj.getString("email");
                                    String login_token = user_obj.getString("login_token");
                                    String phone = user_obj.getString("phone");
                                    String customertype = user_obj.getString("customertype");
                                    SharedPreferences sharedpreferences = getSharedPreferences("mypreferences", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedpreferences.edit();


                                    editor.putString("user_id", user_id);
                                    editor.putString("customer_name", customer_name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", user_email);
                                    editor.putString("phone", phone);
                                    editor.putString("customertype", customertype);
                                    editor.putInt("logedIn", 1);

                                    editor.commit();

                                    Toast.makeText(getApplicationContext(), "User registered successfully...", Toast.LENGTH_SHORT).show();
//                                    Intent in=new Intent(SignUpActivity.this,MainActivity.class);
//                                    startActivity(in);
                                    finish();
                                } else {


                                    Toast.makeText(getApplicationContext(), "Email already exists. please choose a different one", Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(SignUpActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", edt_Name.getText().toString().trim());
                params.put("email", edt_Email.getText().toString().trim());
                params.put("password", edt_Pwd.getText().toString().trim());
                params.put("phone", edt_Phone.getText().toString().trim());
                params.put("customertype", edt_customertype.getText().toString().trim());


                return params;
            }
        };

        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);

    }
}
