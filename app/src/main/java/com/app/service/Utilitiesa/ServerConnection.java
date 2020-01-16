package com.app.service.Utilitiesa;


import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by admin on 04/07/2017.
 */

public class ServerConnection {


    private HttpResponse httpresponse;
    private DefaultHttpClient httpclient;
    private BufferedReader reader;
    private HttpGet request;
    private HttpEntity entity;
    private InputStream inputstream;
    private String line;


    public String Connection(String url) {

        String result = "";
        httpclient = new DefaultHttpClient();
        request = new HttpGet(url);
        try {
            httpresponse = httpclient.execute(request);
            entity = httpresponse.getEntity();
            inputstream = entity.getContent();
        } catch (ClientProtocolException e) {

            return "CLIENT_EXCEPTION";
        } catch (IOException e) {

            return "IO_EXCEPTION";
        }
        if (inputstream != null) {

            try {
                result = convertInputStreamToString_SalesTrnd(inputstream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static String Connection(String url, String json) {
        Log.v("url", "url" + url);
        Log.v("url", "json" + json);
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);


            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.v("result1", "resultkkk");
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            Log.v("result2", "resultkkk");
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            Log.v("result3", "resultkkk");
            httpPost.setHeader("Content-type", "application/json");
            Log.v("result4", "resultkkk");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.v("result5", "resultkkk");
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.v("result6", "resultkkk");
            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString_SalesTrnd(inputStream);

            } else {
                result = "Did not work!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.v("result1", "result1" + result);
        // 11. return result
        return result;
    }

    public static String Connection(String json , HttpPost httpPost) {

        Log.v("url", "json" + json);
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.v("result1", "resultkkk");
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            Log.v("result2", "resultkkk");
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            Log.v("result3", "resultkkk");
//            httpPost.setHeader("Content-type", "application/json");
            Log.v("result4", "resultkkk");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.v("result5", "resultkkk");
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.v("result6", "resultkkk");
            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString_SalesTrnd(inputStream);

            } else {
                result = "Did not work!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.v("result1", "result1" + result);
        // 11. return result
        return result;
    }

    public static String Connection(String json , HttpPut httpPut) {

        Log.v("url", "json" + json);
        InputStream inputStream = null;
        String result = "";
        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.v("result1", "resultkkk");
            // 6. set httpPost Entity
            httpPut.setEntity(se);
            Log.v("result2", "resultkkk");
            // 7. Set some headers to inform server about the type of the content
          //  httpPut.setHeader("Accept", "application/json");
            Log.v("result3", "resultkkk");
//            httpPost.setHeader("Content-type", "application/json");
            Log.v("result4", "resultkkk");
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPut);
            Log.v("result5", "resultkkk");
            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.v("result6", "resultkkk");
            // 10. convert inputstream to string
            if (inputStream != null) {
                result = convertInputStreamToString_SalesTrnd(inputStream);

            } else {
                result = "Did not work!";
            }

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        Log.v("result1", "result1" + result);
        // 11. return result
        return result;
    }

    private static String convertInputStreamToString_SalesTrnd(InputStream
                                                                       inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
}
