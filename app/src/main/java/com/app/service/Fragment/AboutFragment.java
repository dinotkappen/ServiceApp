package com.app.service.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;


public class AboutFragment extends Fragment {
    View rootView;
    AppBarLayout appBarLayout;
    private String JSON_URL_ABOUTUS;
    Utilities utilities;
    TextView txt_desc,txt_About_Title;
    WebView myWebView;
String title;
    String selectedLang;
    final String mimeType = "text/html";
    final String encoding = "UTF-8";
    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {
        rootView = inflater.inflate(R.layout.fragment_about, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
            selectedLang = Hawk.get("selectedLang", "en");
      //  txt_desc=(TextView)rootView.findViewById(R.id.txt_desc);
        txt_About_Title=(TextView)rootView.findViewById(R.id.txt_About_Title);
        myWebView=(WebView)rootView.findViewById(R.id.myWebView);
        JSON_URL_ABOUTUS = utilities.GetUrl() + "listingPages";
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//        txt_actionbar_Title.setText("");
//        ImageView img_search_actionbar=(ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//            img_search_actionbar.setVisibility(View.INVISIBLE);
//        img_search_actionbar.setImageResource(R.drawable.back_btn);
//        img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });
//        appBarLayout = (AppBarLayout) rootView.findViewById(R.id.app_layout);
//        appBarLayout.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#330000ff")));
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            txt_desc.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>", Html.FROM_HTML_MODE_COMPACT));
//        } else {
//            txt_desc.setText(Html.fromHtml("<h2>Title</h2><br><p>Description here</p>"));
//        }



        myWebView.getSettings().setJavaScriptEnabled(true);
            if(selectedLang.equals("en"))
            {
                myWebView.loadUrl("http://serviceapp.whyte.company/page/about-us");
               // loadAboutUS_api();
            }
            else
            {
               // loadAboutUS_api();
                myWebView.loadUrl("http://serviceapp.whyte.company/page/About-Us-arabic");
            }


       // loadAboutUS_api();
       // myWebView.loadDataWithBaseURL("http://serviceapp.whyteapps.com/page/about-us","",mimeType,encoding,"");
        }
        else
        {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
            return rootView;
    }

    private void loadAboutUS_api() {
//

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //creating a string request to send request to the url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_ABOUTUS,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        //hiding the progressbar after completion
                        // progressBar.setVisibility(View.INVISIBLE);


                        try {
                            dialog.dismiss();
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    JSONObject pages_Obj=obj.getJSONObject("pages");
                                    String description=pages_Obj.getString("description");
                                    title=pages_Obj.getString("name");
                                    myWebView.loadDataWithBaseURL("", description, mimeType, encoding, "");

//                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                        txt_desc.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
//
//                                    } else {
//                                        txt_desc.setText(Html.fromHtml(description));
//                                    }
//                                    txt_About_Title.setText(title);

                                }
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);
    }
}
