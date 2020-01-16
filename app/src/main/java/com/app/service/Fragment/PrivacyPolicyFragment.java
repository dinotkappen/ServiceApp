package com.app.service.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.service.Activity.MainActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;

/**
 * A simple {@link Fragment} subclass.
 */
public class PrivacyPolicyFragment extends Fragment {
    View rootView;
    WebView myWebViewPrivacy;
    Utilities utilities;
    MainActivity mainActivity;

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        try {
//            if (utilities.isOnline(getActivity())) {
        // Inflate the layout for this fragment
        Hawk.init(getActivity())
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(getActivity()))
                .setLogLevel(LogLevel.FULL)
                .build();
        Hawk.put("pages", "Privacy");

        rootView = inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        realativeActionBar.setVisibility(View.VISIBLE);
        backArrow.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.GONE);
//
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//        TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//        txt_actionbar_Title.setText(getString(R.string.TermsCoditions));
//        ImageView img_search_actionbar=(ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//        img_search_actionbar.setVisibility(View.VISIBLE);
//        img_search_actionbar.setImageResource(R.drawable.back_btn);
//        img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().onBackPressed();
//            }
//        });



        myWebViewPrivacy = (WebView) rootView.findViewById(R.id.myWebViewAbout);

        myWebViewPrivacy.getSettings().setJavaScriptEnabled(true);
        myWebViewPrivacy.loadUrl("http://serviceapp.whyte.company/page/Privacy-Policy");

//            } else {
//                rootView = inflater.inflate(R.layout.msg_no_internet, container, false);
//            }
//        } catch (Exception ex) {
//            String msg = ex.getMessage().toString();
//            Log.v("msg_Main", msg);
//        }
        return rootView;
    }

    public void onDestroy(){
        super.onDestroy();
        Hawk.put("pages", "home");
    }


}
