package com.app.service.Utilitiesa;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Utilities {

    public static boolean isOnline(final Context mcontext)
    {
        boolean response = true;
        try {
            ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();
            if (ni == null) {
                response = false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            response = false;
        }
        Log.v("response","response : "+response);
        return response;
    }

    public static String GetUrl()
    {
        return "http://serviceapp.whyte.company/api/";
    }


}
