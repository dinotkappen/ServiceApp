package com.app.service.Fragment;

import android.Manifest;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Activity.LogInActivity;
import com.app.service.Activity.MapsActivity;
import com.app.service.Activity.ThankyouActivity;
import com.app.service.Adapter.AttachmentItemsAdapter;
import com.app.service.Adapter.Receipt_Adapter;
import com.app.service.Model.AttachmentItemsModel;
import com.app.service.Model.Reciept_Model;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cc.cloudist.acplibrary.ACProgressFlower;

import static android.app.Activity.RESULT_OK;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;

public class PlaceOrderFragment extends Fragment {

    View rootView;
    Button btn_placr_order;
    SharedPreferences preferences;
    Utilities utilities;
    String countryID;
    EditText edt_Description, edt_Summary, edt_Attachment;
    String JSON_URL_ATTACHMENT, JSON_URL_CART, JSON_URL_CHECKOUT, JSON_URL_PLACE_ORDER;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    private static final String VIDEO_DIRECTORY = "/demonuts";
    String productId, login_token, servicetime, servicedate;
    String order_id;
    String str_Description, str_Summary, sale_price;
    LinearLayout linear_desc, linear_summary;
    Bitmap photo;
    Uri selectedMediaUri;
    String filePath;
    String selectedVideoPath;
    String subHeading;
    ArrayList<Uri> img_List = new ArrayList<>();
    ArrayList<Uri> OP_List = new ArrayList<>();
    String encodedImage;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 101;
    private static final int MY_VIDEO_REQUEST_CODE = 102;
    private static final int MY_CAMERA_PERMISSION_CODE = 110;
    File uploadFile;
    TextView txt_SubHeading;
    String mediaCode = "";
    String fileName;
    String flagemergency;
    LinearLayout len_team_click;
    ArrayList<AttachmentItemsModel> attachmentItemsModel = new ArrayList<AttachmentItemsModel>();
    private AttachmentItemsAdapter attachmentItemsAdapter;
    private RecyclerView recycleAttachItems;

    int PERMISSION_ALL = 1;
    int countRbTC = 0;
    int perSuccess = 0;
    String[] PERMISSIONS = {

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.SYSTEM_ALERT_WINDOW,
            android.Manifest.permission.CAMERA
    };
    static String full_Adrz, lat_str, long_str;
    static PlaceOrderFragment placeOrderFragment = new PlaceOrderFragment();

    public static PlaceOrderFragment newInstance(String Adrz, String lat, String lon) {
        full_Adrz = Adrz;
        long_str = lat;
        lat_str = lon;
        return placeOrderFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {

            rootView = inflater.inflate(R.layout.fragment_place_order, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
            Log.v("lat_str", lat_str);
            Log.v("long_str", long_str);
            Log.v("full_Adrz", full_Adrz);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayOptions(android.support.v7.app.ActionBar.DISPLAY_SHOW_CUSTOM);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(R.layout.actionbar_layout);
//            ((AppCompatActivity) getActivity()).getSupportActionBar().setCustomView(getLayoutInflater().inflate(R.layout.actionbar_layout, null),
//                    new android.support.v7.app.ActionBar.LayoutParams(android.support.v7.app.ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
//            TextView txt_actionbar_Title = (TextView) ((AppCompatActivity) getActivity()).findViewById(R.id.txt_actionbar_Title);
//            txt_actionbar_Title.setText("SERVICIO");
//            ImageView img_search_actionbar = (ImageView) ((AppCompatActivity) getActivity()).findViewById(R.id.img_search_actionbar);
//            img_search_actionbar.setImageResource(R.drawable.back_btn);
//            img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    getActivity().onBackPressed();
//                }
//            });
            final CheckBox rbTC = rootView.findViewById(R.id.rbTCMaps);

            rbTC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (countRbTC == 0) {
                        rbTC.setChecked(true);
                        rbTC.setError(null);

                        countRbTC = 1;
                    } else {
                        rbTC.setChecked(false);
                        countRbTC = 0;
                    }

                }
            });
            len_team_click =rootView.findViewById(R.id.len_team_click);
            preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            countryID = preferences.getString("countryID", "0");
            edt_Description = (EditText) rootView.findViewById(R.id.edt_Description);
            edt_Attachment = (EditText) rootView.findViewById(R.id.edt_Attachment);
            edt_Summary = (EditText) rootView.findViewById(R.id.edt_Summary);
            linear_summary = (LinearLayout) rootView.findViewById(R.id.linear_summary);
            linear_desc = (LinearLayout) rootView.findViewById(R.id.linear_desc);
            txt_SubHeading = (TextView) rootView.findViewById(R.id.txt_SubHeading);
            JSON_URL_CART = utilities.GetUrl() + "cart/store/";

            recycleAttachItems = (RecyclerView) rootView.findViewById(R.id.recycleAttachItems);
            subHeading = preferences.getString("subService", "0");
            if (subHeading != null && !subHeading.isEmpty() && !subHeading.equals("null")) {
                txt_SubHeading.setText(subHeading);
            }

            len_team_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = new PrivacyPolicyFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });

            edt_Attachment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    selectImage();
//                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                }
            });
            btn_placr_order = (Button) rootView.findViewById(R.id.btn_placr_order);

            btn_placr_order.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int logedIn = preferences.getInt("logedIn", 0);

                    if (logedIn == 1) {
                        if (rbTC.isChecked()) {
                            addTo_Cart_Api();
                        }
                        else
                        {
                            rbTC.setError(getString(R.string.AgreeTC));

                        }
                    } else {
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
            });

            linear_desc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
            });
            linear_summary.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                }
            });
//            } catch (Exception ex) {
//                Log.v("OnCreate", ex.getMessage().toString());

        } else {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }


    public void set_AttachmentItems() {
        try {
            Log.v("totalItems", "" + attachmentItemsModel.size());
            attachmentItemsAdapter = new AttachmentItemsAdapter(getActivity(), attachmentItemsModel);


            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            recycleAttachItems.setLayoutManager(mLayoutManager);
            recycleAttachItems.setItemAnimator(new DefaultItemAnimator());
            recycleAttachItems.setAdapter(attachmentItemsAdapter);
        } catch (Exception ex) {
            Log.v("set_AttachmentItems", ex.getMessage().toString());
        }
    }

    private void selectImage() {
        try {
            if (!hasPermissions(getContext(), PERMISSIONS)) {
                ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSION_ALL);

            }
            final CharSequence[] items = {"Take Photo", "Choose from Library",
                    "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Add Photo!");
            builder.setItems(items, new DialogInterface.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    //  boolean result=Utility.checkPermission(getActivity());
                    if (items[item].equals("Take Photo")) {
                        try {

                            dispatchTakePictureIntent();
                            Log.v("dispatch", "ok");
//                            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//                            startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);


                        } catch (Exception ex) {
                            String msg = ex.getMessage().toString();
                            Log.v("mlk", msg);
                            Intent in = new Intent(getContext(), ThankyouActivity.class);
                            getActivity().startActivity(in);
                            getActivity().finish();
                        }
                    } else if (items[item].equals("Choose from Library")) {

                        try {
                            final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            galleryIntent.setType("image/* video/*");
                            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
                            // startActivityForResult(galleryIntent, MY_GALLERY_REQUEST_CODE);

                        } catch (Exception ex) {
                            Log.v("img_video", ex.getMessage().toString());
                        }

                    } else if (items[item].equals("Choose from Library")) {


                        final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        galleryIntent.setType("video/*");
                        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), MY_GALLERY_REQUEST_CODE);
                        // startActivityForResult(galleryIntent, MY_GALLERY_REQUEST_CODE);


                    } else if (items[item].equals("Cancel")) {
                        dialog.dismiss();
                    }

                }
            });
            builder.show();
        } catch (Exception ex) {
            Log.v("selectImage", ex.getMessage().toString());
        }
    }

    String currentPhotoPath;

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        fileName = "" + image;
        // Save a file: path for use with ACTION_VIEW intents

        currentPhotoPath = image.getAbsolutePath();
        return image;

    }

    private void galleryAddPic() {
        try {

            Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            File f = new File(currentPhotoPath);
            Uri contentUri = Uri.fromFile(f);
            mediaScanIntent.setData(contentUri);
            //img_List.add(contentUri);

            File file = new File(currentPhotoPath);
            fileName = file.getName();
            String[] separated = fileName.split("_");
            fileName = separated[3];
            fileName = "servicio_attachment_image" + fileName;


            attachmentItemsModel.add(new AttachmentItemsModel(contentUri, fileName, ""));
            set_AttachmentItems();

            Log.v("fileNameCamera", "" + fileName);
            Log.v("contentUri", "" + contentUri);
            getActivity().sendBroadcast(mediaScanIntent);
        } catch (Exception ex) {
            Log.v("galleryAddPic", ex.getMessage().toString());
        }
    }

    private void dispatchTakePictureIntent() {
        try {

            Intent takePictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    String ec = ex.getMessage().toString();
                    Log.v("ec", ec);

                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Uri photoURI = FileProvider.getUriForFile(getActivity(),
                            "com.app.service.fileprovider",
                            photoFile);


                    Log.v("photoURI", "ok");

                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, MY_CAMERA_REQUEST_CODE);
                }
            }
        } catch (Exception ex) {
            Log.v("dispatchTakePicture", ex.getMessage().toString());
        }
    }

    public static boolean hasPermissions(Context context, String... permissions) {

        if (context != null && permissions != null) {
            try {
                for (String permission : permissions) {
                    if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                        return false;
                    }
                }
            } catch (Exception ex) {
                Log.v("hasPermissions", ex.getMessage().toString());
            }
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            if (requestCode == MY_CAMERA_PERMISSION_CODE) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "camera permission granted", Toast.LENGTH_LONG).show();
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);
                } else {
                    Toast.makeText(getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception ex) {
            Log.v("onRequestPermissions", ex.getMessage().toString());
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {

//        if(requestCode==MY_CAMERA_REQUEST_CODE)
//        {
//            galleryAddPic();
//            Log.v("galleryAddPicMain","galleryAddPic");
//        }
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            case 100:

                if (requestCode == 100) {
                    img_List.clear();
                    try {
                        if (requestCode == MY_CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
                            Bundle extras = imageReturnedIntent.getExtras();

                            Bitmap imageBitmap = (Bitmap) extras.get("data");
                            Log.v("imageBitmap", "" + imageBitmap);
                            mediaCode = "Image";


                        }

                        Log.v("selectedMediaUri", "" + selectedMediaUri);
                    } catch (Exception ex) {
                        //Intent in = new Intent(getContext(), ThankyouActivity.class);
                        Log.v("cameraError", ex.getMessage().toString());
                        mediaCode = "Image";
                        galleryAddPic();
                        Log.v("galleryAddPicMain", "galleryAddPic");


                    }

                }
                break;
            case 101:
                if (requestCode == 101) {
                    try {

                        ClipData clip = imageReturnedIntent.getClipData();
                        if (clip == null) {
                            selectedMediaUri = imageReturnedIntent.getData();
                            // img_List.add(selectedMediaUri);

                            File file = new File(selectedMediaUri.getPath());
                            fileName = "servicio_attachment" + file.getName();
                            attachmentItemsModel.add(new AttachmentItemsModel(selectedMediaUri, fileName, ""));
                            mediaCode = "Video";
                            set_AttachmentItems();
                            // attachmentItemsAdapter.notifyDataSetChanged();


                            Log.v("nusd", "" + selectedMediaUri);
                            Log.v("fileName", "" + fileName);
                        } else {


                            for (int i = 0; i < clip.getItemCount(); i++) {
                                ClipData.Item item = clip.getItemAt(i);
                                Uri uri = item.getUri();
                                // img_List.add(uri);
                                File file = new File(uri.getPath());
                                fileName = "servicio_attachment" + file.getName();
                                Log.v("fileName", "" + fileName);
                                mediaCode = "Image";
                                Log.v("uri", "" + uri);
                                attachmentItemsModel.add(new AttachmentItemsModel(uri, fileName, ""));
                                set_AttachmentItems();
                                // attachmentItemsAdapter.notifyDataSetChanged();

                            }
                        }
                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("msg_error", msg);
                    }

                }

                break;
        }

    }

    private void addTo_Cart_Api() {
        //creating a string request to send request to the url
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
        btn_placr_order.setEnabled(false);
        productId = preferences.getString("subProductId", null);
        login_token = preferences.getString("login_token", null);
        JSON_URL_CART = utilities.GetUrl() + "cart/store?product=" + productId;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_CART,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    Checkout_Api();


                                } else {


                                    Toast.makeText(getContext(), "Service is not added to the cart", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {

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

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    private void Checkout_Api() {
        //creating a string request to send request to the url
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();
        servicedate = preferences.getString("servicedate", "");
        servicetime = preferences.getString("servicetime", "");
        login_token = preferences.getString("login_token", "");
        flagemergency = preferences.getString("flagemergency", "");
        sale_price = preferences.getString("sale_price", "");
        Log.v("servicedate", servicedate);
        Log.v("servicetime", servicetime);
        Log.v("flagemergency", flagemergency);
        JSON_URL_CHECKOUT = utilities.GetUrl() + "checkout?";
        Log.v("JSON_URL_CHECKOUT", JSON_URL_CHECKOUT);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_CHECKOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//dialog.dismiss();

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                if (success.equals("1")) {

                                    String msg = obj.getString("mag");
                                    order_id = obj.getString("order_id");

                                    editor.putString("order_id", order_id);
                                    editor.commit();
                                    //  Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                                    Log.v("msg", msg);
                                    PlaceOrder_Api();

                                } else {


                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {

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
                    }
                }) {

            /*
             * If you want to add more parameters with the image
             * you can do it here
             * here we have only one parameter with the image
             * which is tags
             * */
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("servicedate", servicedate);
                params.put("servicetime", servicetime);
                params.put("country_id", countryID);
                params.put("flagemergency", flagemergency);
                if (flagemergency.equals("yes")) {
                    params.put("sale_price", sale_price);

                }

                return params;
            }

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

        };


        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }

    private void PlaceOrder_Api() {
        //creating a string request to send request to the url
//        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
//                .build();
//        dialog.setCanceledOnTouchOutside(false);
//        dialog.show();


        Log.v("login_token", login_token);
        JSON_URL_PLACE_ORDER = utilities.GetUrl() + "place-order/" + order_id;
        Log.v("JSON_URL_PLACE_ORDER", JSON_URL_PLACE_ORDER);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, JSON_URL_PLACE_ORDER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

//dialog.dismiss();
                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");
                                SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                if (success.equals("1")) {

                                    String msg = obj.getString("mag");
                                    String order_id = obj.getString("order_id");

                                    editor.putString("order_id", order_id);
                                    editor.commit();
                                    Attachment_Api();

                                    Log.v("msgs", msg);

                                } else {


                                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {

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
                        Log.v("msg_", msg);
                        String n = msg;
                    }
                }) {


            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location_latitude", lat_str);
                params.put("location_longitude", long_str);
                params.put("address", full_Adrz);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userToken", login_token);
                return params;
            }
        };
        queue.add(stringRequest);

    }

    private void Attachment_Api() {
        try {

            final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                    .build();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            //getting the tag from the edittext
            final String tags = "tags";
            JSON_URL_PLACE_ORDER = utilities.GetUrl() + "place-order-update/" + order_id + "?";
            //  JSON_URL_PLACE_ORDER = "http://dohabike.co/test.php";
            str_Summary = edt_Summary.getText().toString();
            str_Description = edt_Description.getText().toString();
            //our custom volley request
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, JSON_URL_PLACE_ORDER,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            dialog.dismiss();
                            try {
                                Log.v("response", new String(response.data));
                                JSONObject obj = new JSONObject(new String(response.data));
                                String msg = obj.getString("mag");

                                String success = obj.getString("success");
                                if (success.equals("1")) {
                                    Intent in = new Intent(getActivity(), ThankyouActivity.class);
                                    startActivity(in);
                                    Log.v("msg_attachment", msg);
                                    getActivity().finish();


                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Intent in = new Intent(getActivity(), ThankyouActivity.class);
                                startActivity(in);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                            Intent in = new Intent(getActivity(), ThankyouActivity.class);
                            startActivity(in);
                            dialog.dismiss();
                        }
                    }) {

                /*
                 * If you want to add more parameters with the image
                 * you can do it here
                 * here we have only one parameter with the image
                 * which is tags
                 * */
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("summary", str_Summary);
                    params.put("description", str_Description);

                    return params;
                }


                @Override
                protected Map<String, DataPart> getByteData() {
                    int l = 1;
                    Map<String, DataPart> params = new HashMap<>();
                    for (int i = 0; i < attachmentItemsModel.size(); i++) {
                        Log.v("attachmentItemsModel", "" + attachmentItemsModel.size());
//                    InputStream iStream = null;
//                    try {
//                        Log.v("msg_att", "");
//                        iStream = getActivity().getContentResolver().openInputStream(attachmentItemsModel.get(i).getFileName());
//                    } catch (FileNotFoundException e) {
//                        e.printStackTrace();
//                    }
                        l++;
                        String attachment_str = "" + attachmentItemsModel.get(i).getAttachmentName();
                        Log.v("attachment_str", attachment_str);
                        if (attachment_str.contains("image")) {
                            params.put("images[" + i + "]", new DataPart("file_avatar" + i + ".jpg",
                                    getFileDataFromDrawable(getActivity(), attachmentItemsModel.get(i).getAttachment())));
                            Log.v("image", "image");
                        } else {
                            params.put("images[" + i + "]", new DataPart("file_avatar" + i + ".mp4",
                                    getFileDataFromDrawable(getActivity(), attachmentItemsModel.get(i).getAttachment())));
                            Log.v("video", "video");
                        }


                        // params.put("images[" + i + "]", new DataPart("image" + i + ".jpg", inputData, "image/jpeg"));
                        Log.v("params", params.toString());
                        Log.v("lCount", "" + l);

                    }


                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("userToken", login_token);

                    return headers;
                }
            };

            //adding the request to volley
            Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
        } catch (Exception ex) {
            Log.v("VolleyAttachment", ex.getMessage().toString());
        }
    }


    public byte[] getFileDataFromDrawable(Context context, Uri uri) {

        String sd = uri.toString();
        Log.v("sd", sd);
        Log.v("mediaCode", "" + mediaCode);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Log.v("image", "" + uri.toString().contains("image"));
        if (uri.toString().contains("image")) {
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();


        } else {
            // Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            try {
                InputStream iStream = context.getContentResolver().openInputStream(uri);
                int bufferSize = 2048;
                byte[] buffer = new byte[bufferSize];

                // we need to know how may bytes were read to write them to the byteBuffer
                int len = 0;
                if (iStream != null) {
                    while ((len = iStream.read(buffer)) != -1) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //  bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        }
        return byteArrayOutputStream.toByteArray();

    }


}
