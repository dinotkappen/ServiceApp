package com.app.service.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.app.service.Activity.MainActivity;
import com.app.service.R;
import com.orhanobut.hawk.Hawk;

import java.util.Locale;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;

public class ChangeLangFragment extends Fragment implements View.OnClickListener {
    View rootView;
    Button btnChangelang;
    TextView txtEnglish, txtArabic;
    String selectedLang;
    ChangeLangFragment changeLangFragment;

    public ChangeLangFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_change_lang, container, false);
        realativeActionBar.setVisibility(View.VISIBLE);
        changeLangFragment=new ChangeLangFragment();
        backArrow.setVisibility(View.VISIBLE);
        imgSearch.setVisibility(View.GONE);
        btnChangelang =(Button)rootView.findViewById(R.id.btnChangelang);
        txtEnglish =(TextView)rootView.findViewById(R.id.txtEnglish);
        txtArabic = (TextView)rootView.findViewById(R.id.txtArabic);
        txtArabic.setOnClickListener(this);
        txtEnglish.setOnClickListener(this);
        selectedLang= Hawk.get("selectedLang","en");

        if(selectedLang.equals("en"))
        {
            txtEnglish.setTextColor(getResources().getColor(R.color.green));
            txtArabic.setTextColor(getResources().getColor(R.color.colorWhite));
            selectedLang = "en";
        }
        else
        {
            txtEnglish.setTextColor(getResources().getColor(R.color.colorWhite));
            txtArabic.setTextColor(getResources().getColor(R.color.green));
            selectedLang = "ar";
        }



        btnChangelang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.put("selectedLang", selectedLang);
                languageSelection();
            }
        });
        return rootView;
    }

    public void languageSelection() {
        try {
            Locale locale = new Locale(selectedLang);
            Resources resources = getResources();
            Configuration config = resources.getConfiguration();
            config.locale = locale;
            if (Build.VERSION.SDK_INT >= 17) {
                config.setLayoutDirection(locale);
            }

            resources.updateConfiguration(config, resources.getDisplayMetrics());

            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        } catch (Exception ex) {
            Log.v("languageSelection", ex.getMessage().toString());
        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtEnglish: {

                txtEnglish.setTextColor(getResources().getColor(R.color.green));
                txtArabic.setTextColor(getResources().getColor(R.color.colorWhite));
                selectedLang = "en";
                Log.v("selectedLang",selectedLang);

            }
            break;
            case R.id.txtArabic: {
                txtEnglish.setTextColor(getResources().getColor(R.color.colorWhite));
                txtArabic.setTextColor(getResources().getColor(R.color.green));
                selectedLang = "ar";
                Log.v("selectedLang",selectedLang);
            }
            break;

        }
    }
}
