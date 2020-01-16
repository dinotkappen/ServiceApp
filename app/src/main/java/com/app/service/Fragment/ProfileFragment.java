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
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.app.service.Activity.SplashActivity;
import com.app.service.Activity.ThankyouActivity;
import com.bumptech.glide.Glide;
import com.app.service.Activity.LogInActivity;
import com.app.service.Activity.MainActivity;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;
import static com.app.service.Fragment.PlaceOrderFragment.hasPermissions;
import static com.facebook.FacebookSdk.getApplicationContext;


public class ProfileFragment extends Fragment {
    View rootView;
    EditText edt_Name, edt_Email, edt_Phone;
    String name, email, phone;
    String JSON_URL_PROFILE, JSON_URL_UPDATE_PROFILE;
    String login_token;
    Utilities utilities;
    Button btn_Update, btn_Change_Pwd;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    SharedPreferences preferences;
    Bitmap photo = null;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int MY_GALLERY_REQUEST_CODE = 101;
    String encodedImage, str_user_pic;
    ImageView profile_image;
    TextView txt_customer_name, txt_logout;
    LinearLayout lineaeLogOutProfile;
    int logedIn;
    int changeProfileImg = 0;
    ArrayList<Uri> img_List = new ArrayList<>();
    int PERMISSION_ALL = 1;
    int perSuccess = 0;
    Uri selectedMediaUri;
    View view;
    MainActivity mainActivity;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    String[] permissionsRequired = new String[]{Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Button btnCheckPermissions;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (utilities.isOnline(getActivity())) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            realativeActionBar.setVisibility(View.VISIBLE);
            backArrow.setVisibility(View.VISIBLE);
            imgSearch.setVisibility(View.GONE);
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
            JSON_URL_PROFILE = utilities.GetUrl() + "accounts/my-account";
            preferences = getContext().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            profile_image = (ImageView) rootView.findViewById(R.id.profile_image);
            edt_Name = (EditText) rootView.findViewById(R.id.edt_Name);
            edt_Email = (EditText) rootView.findViewById(R.id.edt_Email);
            edt_Phone = (EditText) rootView.findViewById(R.id.edt_Phone);
            btn_Update = (Button) rootView.findViewById(R.id.btn_Update);
            txt_customer_name = (TextView) rootView.findViewById(R.id.txt_customer_name);
            txt_logout = (TextView) rootView.findViewById(R.id.txt_logout);
            lineaeLogOutProfile = (LinearLayout) rootView.findViewById(R.id.lineaeLogOutProfile);
            btn_Change_Pwd = (Button) rootView.findViewById(R.id.btn_Change_Pwd);


            logedIn = preferences.getInt("logedIn", 0);


            if (logedIn == 1) {
                login_token = preferences.getString("login_token", null);
                Log.v("login_token", login_token);
                loadProfile_api();
                // loadProfile_api();
                final Bitmap icon = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.profile_sample);
                btn_Update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Update_Profile_api();

                    }
                });
                permissionStatus = getActivity().getSharedPreferences("permissionStatus", MODE_PRIVATE);
                profile_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
                lineaeLogOutProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                getContext());

                        builder.setTitle("Log Out");
                        builder.setMessage("Are you sure you want to log out?");
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
                                        SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                        editor.putString("user_id", "");
                                        editor.putString("customer_name", "");
                                        editor.putString("login_token", "");
                                        editor.putString("user_email", "");
                                        editor.putString("customertype", "");
                                        editor.putString("lname", "");
                                        editor.putString("profile_pic", "");
                                        editor.putInt("logedIn", 0);
                                        editor.putString("countryID", "0");
                                        editor.commit();
                                        Intent in = new Intent(getActivity(), SplashActivity.class);
                                        startActivity(in);
                                        getActivity().finish();
                                    }
                                });


                        builder.show();


                    }
                });
                btn_Change_Pwd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Fragment fragment = new Change_pwd_Fragment();
                        FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });

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
        } else {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }

        return rootView;
    }

    public void UploadImg() {
        try {
            ByteArrayOutputStream baos1 = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos1);
            byte[] imageBytes = baos1.toByteArray();
            String imagePathStr = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), photo, "title", null);
            //  String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            selectedMediaUri = Uri.parse(imagePathStr);
            Log.v("photo", "" + photo);
           // profile_image.setImageBitmap(photo);
            Log.v("b", "" + selectedMediaUri);
            Log.v("imagePathStr", "" + imagePathStr);
//            try {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
//                // Log.d(TAG, String.valueOf(bitmap));
//
//
//                profile_image.setImageBitmap(bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
            img_List.add(selectedMediaUri);
            String imageString = Arrays.toString(imageBytes);
            if (imageString.equals(null)) {
                encodedImage = "";
            } else {
                encodedImage = imageString;
            }
            //  encodedImage = imageString.replaceAll("\n", "");


//        JSONArray arr = new JSONArray();
//        arr.put("data:image/jpeg;base64," + encodedImage);
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            Log.v("UploadImg", msg);
        }

    }

    private void selectImage() {
        try {
            if (ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[0]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[1]) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(getActivity(), permissionsRequired[2]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[1])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissionsRequired[2])) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Gallery permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                        }
                    });
                    builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(permissionsRequired[0], false)) {
                    //Previously Permission Request was cancelled with 'Dont Ask Again',
                    // Redirect to Settings after showing Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Need Multiple Permissions");
                    builder.setMessage("This app needs Camera and Gallery permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getContext(), "Go to Permissions to Grant  Camera and Gallery", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton(getString(R.string.Cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    //just request the permission
                    ActivityCompat.requestPermissions(getActivity(), permissionsRequired, PERMISSION_CALLBACK_CONSTANT);
                }


                SharedPreferences.Editor editor = permissionStatus.edit();
                editor.putBoolean(permissionsRequired[0], true);
                editor.commit();
            } else {
                //You already have the permission, just go ahead.
                proceedAfterPermission();
            }
        } catch (Exception ex) {
            Log.v("cv", ex.getMessage().toString());
        }
    }
    private void proceedAfterPermission() {



        // Toast.makeText(getContext(), "We got All Permissions", Toast.LENGTH_LONG).show();
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //  boolean result=Utility.checkPermission(getActivity());
                if (items[item].equals("Take Photo")) {

                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, MY_CAMERA_REQUEST_CODE);

                } else if (items[item].equals("Choose from Library")) {

                    final Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setType("image/*");
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

    }
    private void galleryAddPic() {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(currentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        img_List.add(contentUri);
        String url = "" + contentUri;
        Log.v("contentUri", "" + contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    private void dispatchTakePictureIntent() {
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

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {

            case 100:
                if (requestCode == 100) {
                    img_List.clear();
                    try {
                        if (requestCode == MY_CAMERA_REQUEST_CODE) {
                            photo = (Bitmap) imageReturnedIntent.getExtras().get("data");

                            Uri tempUri = getImageUri(getApplicationContext(), photo);

                            // CALL THIS METHOD TO GET THE ACTUAL PATH
                            File finalFile = new File(getRealPathFromURI(tempUri));
                          //  profile_image.setImageBitmap(photo);
                            Log.v("finalFile", "" + finalFile);

                            Glide.with(getContext())
                                    .load(finalFile)
                                    .into(profile_image);
                            UploadImg();
//

                        }

                        Log.v("selectedMediaUri", "" + selectedMediaUri);
                    } catch (Exception ex) {
                        //Intent in = new Intent(getContext(), ThankyouActivity.class);
                        Log.v("cameraError", ex.getMessage().toString());

                        galleryAddPic();
                        Log.v("galleryAddPicMain", "galleryAddPic");


                    }
                }

                break;
            case 101:
                if (requestCode == 101) {
                    try {

                        selectedMediaUri = imageReturnedIntent.getData();
                        img_List.add(selectedMediaUri);
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedMediaUri);
                            Log.e("url_profile", String.valueOf(bitmap));


//                            Glide.with(getContext())
//                                    .load(bitmap)
//                                    .into(profile_image);
                            profile_image.setImageBitmap(bitmap);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception ex) {
                        String msg = ex.getMessage().toString();
                        Log.v("msg_error", msg);
                    }

                }

                break;
        }

    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    private void loadProfile_api() {
        //creating a string request to send request to the url
        login_token = preferences.getString("login_token", null);
        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {
                                    JSONObject objDetails = obj.getJSONObject("customer");
                                    String id = objDetails.getString("id");

                                    name = objDetails.getString("name");
                                    email = objDetails.getString("email");
                                    phone = objDetails.getString("phone");
                                    String j = phone;
                                    String img=objDetails.getString("profile_pic");
                                    str_user_pic = objDetails.getString("profile_pic");
                                    Log.v("laodProfile", str_user_pic);
                                    Log.v("name", name);
                                    edt_Email.setText(email);
                                    txt_customer_name.setText(name);
                                    edt_Name.setText(name);
                                    if (phone.equals("null")) {
                                        edt_Phone.setText("");
                                    } else {


                                        edt_Phone.setText(phone);
                                    }
                                    editor.putString("profile_pic", str_user_pic);
                                    editor.commit();
                                    Log.v("userselectedMediaUri", "" + selectedMediaUri);

                                    Glide.with(profile_image.getContext()).load(str_user_pic).placeholder(R.drawable.profile_sample)
                                            .dontAnimate().into(profile_image);
                                    ((MainActivity) getActivity()).updateNavHeaderView();
//                                    Glide.with(getActivity())
//                                            .load(str_user_pic)
//                                            .placeholder(R.drawable.profile_sample)
//                                            .into(profile_image);

                                    Log.v("prof_pic_main22", str_user_pic);

                                } else {

                                    dialog.dismiss();
                                    Toast.makeText(getContext(), "Service is not added to the cart", Toast.LENGTH_SHORT).show();

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

    private void Update_Profile_api() {

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        //getting the tag from the edittext
        name = edt_Name.getText().toString();
        email = edt_Email.getText().toString();
        phone = edt_Phone.getText().toString();


        JSON_URL_UPDATE_PROFILE = utilities.GetUrl() + "accounts/update?";
        Log.v("login_token1", login_token);
        Log.v("JSON_URL_UPDATE_PROFILE", JSON_URL_UPDATE_PROFILE);
        //our custom volley request
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, JSON_URL_UPDATE_PROFILE,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        dialog.dismiss();
                        try {
                            SharedPreferences sharedpreferences = getActivity().getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            JSONObject obj = new JSONObject(new String(response.data));
                            String msg = obj.getString("msg");

                            String success = obj.getString("success");
                            if (success.equals("1")) {

                                Log.v("msg_attachment", msg);
                                String profile_img_str = obj.getString("profile-img");
                                editor.putString("profile_pic", profile_img_str);
                                editor.commit();
                                Log.v("UpdateResult", profile_img_str);

                                try {
                                    Glide.with(profile_image.getContext()).load(profile_img_str).placeholder(R.drawable.profile_sample)
                                            .dontAnimate().into(profile_image);
                                } catch (Exception e) {
                                    Log.v("GlideCatch", e.getMessage());
                                }

                                ((MainActivity) getActivity()).updateNavHeaderView();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();

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
                params.put("type", "details");
                params.put("name", name);
                params.put("phone", phone);
//                params.put("email", email);
                return params;
            }


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                for (int i = 0; i < img_List.size(); i++) {
                    InputStream iStream = null;
                    try {
                        Log.v("msg_att", "");
                        iStream = getActivity().getContentResolver().openInputStream(img_List.get(i));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Log.v("UploadedImg", ""+img_List.get(i));
                    params.put("profile", new DataPart("file_avatar.jpg",
                            getFileDataFromDrawable(getActivity(), img_List.get(i))));
                    // params.put("images[" + i + "]", new DataPart("image" + i + ".jpg", inputData, "image/jpeg"));


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
    }


    public byte[] getFileDataFromDrawable(Context context, Uri uri) {
        String sd = uri.toString();
        Log.v("sd", sd);

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


        }


        return byteArrayOutputStream.toByteArray();
    }


//    @Override
//    public void onResume() {
//        Log.e("DEBUG_PROFILE", "onResume of HomeFragment");
//        logedIn = preferences.getInt("logedIn", 0);
//        //loadItems_api();
//        if (logedIn == 1) {
//
//            login_token = preferences.getString("login_token", null);
//            loadProfile_api();
//            Log.e("reload", "reload");
//
//
//        }
//        super.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        Log.e("DEBUG_PROFILE", "OnPause of HomeFragment");
//
//        super.onPause();
//    }
}