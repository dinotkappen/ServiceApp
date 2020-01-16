package com.app.service.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;


public class ContactFragment extends Fragment {

    View rootView;
    AppBarLayout appBarLayout;
    EditText edt_Name, edt_Email, edt_Phone,edt_Cmnt;
    private static String JSON_URL_CONTACT;
    Utilities utilities;
    String login_token;
    String name, email, telephone, comment;
    SharedPreferences preferences;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    Button btn_Send;
    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {
        rootView = inflater.inflate(R.layout.fragment_contact, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//        txt_actionbar_Title.setText("SERVICIO");
//        ImageView img_search_actionbar=(ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//            img_search_actionbar.setVisibility(View.INVISIBLE);
        //        img_search_actionbar.setImageResource(R.drawable.back_btn);
//        img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
        JSON_URL_CONTACT = utilities.GetUrl() + "contact?";
        preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
        login_token = preferences.getString("login_token", null);
        edt_Name = (EditText) rootView.findViewById(R.id.edt_Name);
        edt_Email = (EditText) rootView.findViewById(R.id.edt_Email);
        edt_Phone = (EditText) rootView.findViewById(R.id.edt_Phone);
        edt_Cmnt = (EditText) rootView.findViewById(R.id.edt_Cmnt);
        btn_Send=(Button)rootView.findViewById(R.id.btn_Send);



        btn_Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contact_Api();
            }
        });
//        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_layout);
//        appBarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
        }
        else
        {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }

    private void Contact_Api() {
        name = edt_Name.getText().toString();
        email = edt_Email.getText().toString();
        telephone = edt_Phone.getText().toString();
        comment=edt_Cmnt.getText().toString();
        JSON_URL_CONTACT = JSON_URL_CONTACT + "name=" + name + "&email=" + email + "&telephone=" + telephone + "&comment=" + comment;
       Log.v("JSON_URL_CONTACT",JSON_URL_CONTACT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_CONTACT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.v("response",response);

                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            String success = obj.getString("success");
                            String msg = obj.getString("mag");
                            if (status.equals("200")) {

                                if (success.equals("1")) {
                                    Toast.makeText(getContext(), "Thank you for your valid feed back...", Toast.LENGTH_SHORT).show();
                                }



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

                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }
}