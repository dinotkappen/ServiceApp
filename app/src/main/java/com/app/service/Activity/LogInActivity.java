package com.app.service.Activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.app.service.Utilitiesa.Config;
import com.app.service.Utilitiesa.NotificationUtils;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.app.service.R;
import com.app.service.Utilitiesa.PrefUtil;
import com.app.service.Utilitiesa.Utilities;
import com.app.service.Utilitiesa.VolleySingleton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressFlower;

public class LogInActivity extends AppCompatActivity {
    String name_fb, email_fb;
    TextView txt_SignUp, txt_forgot_pwd;
    Button btn_SignIn;
    SharedPreferences preferences;
    String URL_LOGIN;
    Utilities utilities;
    EditText edt_Email, edt_Pwd;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static String accessToken_str;
    String fbID;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    LoginButton loginButton;
    public PrefUtil prefUtil;
    String TAG;
    String mAccountName;
    String googleName, googleEmail, googleToken, googleID;
    String URL_DEVICE_REG, URL_FB, URL_GOOGLE;
    String login_token;

    LinearLayout linearFB, lineargmail;
    ImageView img_fb, img_gmail;
    GoogleSignInClient mGoogleSignInClient;
    int RC_SIGN_IN = 100;
    private static final String TAGG = "AndroidClarified";

    String firebasseRegID;
    int logedIn = 0;
    String selectedLang;

    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final String TAGS = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (utilities.isOnline(this)) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // Facebook SDK init
            FacebookSdk.sdkInitialize(getApplicationContext());
            callbackManager = CallbackManager.Factory.create();
            setContentView(R.layout.activity_log_in);
            selectedLang = Hawk.get("selectedLang", "en");

            preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
            logedIn = preferences.getInt("logedIn", 0);


            firebasseRegID = preferences.getString("regFireID", "0");
            String j = firebasseRegID;
            img_fb = (ImageView) findViewById(R.id.img_fb);

            img_gmail = (ImageView) findViewById(R.id.img_gmail);
            Glide.with(this)
                    .load(R.drawable.ic_fb)
                    .into(img_fb);
            Glide.with(this)
                    .load(R.drawable.ic_gmail)
                    .into(img_gmail);
            lineargmail = (LinearLayout) findViewById(R.id.lineargmail);
            linearFB = (LinearLayout) findViewById(R.id.linearFB);
            // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


            lineargmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    googleSignIn();
                }
            });

            loginButton = (LoginButton) findViewById(R.id.login_button);
            linearFB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    loginButton.performClick();
                }
            });
            loginButton.setReadPermissions(Arrays.asList(
                    "public_profile", "email"));
            callbackManager = CallbackManager.Factory.create();


            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {

                            try {

                                accessToken_str = loginResult.getAccessToken().getToken();
                                fbID = loginResult.getAccessToken().getUserId();
                                Log.v("fbID", fbID);


//                            startActivityForResult(signInIntent, 101);
//                             save accessToken to SharedPreference
//                            prefUtil.saveAccessToken(accessToken_str);

                                GraphRequest request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject jsonObject,
                                                                    GraphResponse response) {

                                                // Getting FB User Data
                                                Bundle facebookData = getFacebookData(jsonObject);


                                            }
                                        });

                                Bundle parameters = new Bundle();
                                parameters.putString("fields", "id,first_name,last_name,email");
                                request.setParameters(parameters);
                                request.executeAsync();
                            } catch (Exception ex) {
                                String msg = ex.getMessage().toString();
                                String r = msg;
                            }
                        }


                        @Override
                        public void onCancel() {
                            Log.d(TAG, "Login attempt cancelled.");
                        }

                        @Override
                        public void onError(FacebookException e) {
                            e.printStackTrace();
                            Log.d(TAG, "Login attempt failed.");
                            deleteAccessToken();
                        }
                    }
            );

            URL_LOGIN = utilities.GetUrl() + "login?";
            edt_Email = (EditText) findViewById(R.id.edt_Email);
            edt_Pwd = (EditText) findViewById(R.id.edt_Pwd);
            if(selectedLang.equals("en"))
            {
                edt_Email.setGravity(Gravity.LEFT);
                edt_Pwd.setGravity(Gravity.LEFT);

            }
            else
            {
                edt_Email.setGravity(Gravity.RIGHT);
                edt_Pwd.setGravity(Gravity.RIGHT);
            }
            txt_forgot_pwd = (TextView) findViewById(R.id.txt_forgot_pwd);
            txt_SignUp = (TextView) findViewById(R.id.txt_SignUp);
            btn_SignIn = (Button) findViewById(R.id.btn_SignIn);
            txt_SignUp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(LogInActivity.this, SignUpActivity.class);
                    startActivity(in);
                }
            });
            txt_forgot_pwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in = new Intent(LogInActivity.this, ForgotPasswordActivity.class);
                    startActivity(in);
                }
            });
            btn_SignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    submitForm();
                    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

                }
            });
            printKeyHash(this);

            mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    // checking for type intent filter
                    if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                        // gcm successfully registered
                        // now subscribe to `global` topic to receive app wide notifications
                        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
                        String regId = pref.getString("regId", null);

                        Log.e(TAGS, "Firebase reg id: " + regId);
                        Log.v("regIdFIRE", regId);


                    } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                        // new push notification is received

                        String message = intent.getStringExtra("message");

                        Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                        //txtMessage.setText(message);
                    }
                }
            };
        } else {
            setContentView(R.layout.no_internet_msg_layout);
        }


    }


    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    // GOOGLE PLAY APP SIGNING SHA-1 KEY:- 65:5D:66:A1:C9:31:85:AB:92:C6:A2:60:87:5B:1A:DA:45:6E:97:EA
    //SHA1: 6D:BF:BA:62:50:C2:E6:06:10:CA:08:F6:21:A6:7E:AC:98:D5:3F:CE
//    byte[] sha1 = {
//            0x6D, (byte) 0xBF, (byte) 0xBA, (byte) 0x62, (byte) 0x50, (byte) 0xC2, (byte) 0xE6, (byte) 0x06, (byte) 0x10, (byte) 0xCA, (byte) 0x08, (byte) 0xF6, (byte) 0x21, (byte) 0xA6, 0x7E, (byte) 0xAC, (byte) 0x98, (byte) 0xD5, (byte) 0x3F, (byte) 0xCE
//    };
//      System.out.println("keyhashGooglePlaySignIn:"+Base64.encodeToString(sha1,Base64.NO_WRAP));
//    String nhj = Base64.encodeToString(sha1, Base64.NO_WRAP);
//    String lk = nhj;

    //******For getting Hash key to give in fb developer console
    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));
                Log.v("key", key);
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
            if (acct != null) {
                googleName = acct.getDisplayName();
                String personGivenName = acct.getGivenName();
                String personFamilyName = acct.getFamilyName();
                googleEmail = acct.getEmail();
                googleID = acct.getId();
                googleToken = acct.getIdToken();
                Uri personPhoto = acct.getPhotoUrl();


                googleLogIn();
            }

            // Signed in successfully, show authenticated UI.
            // updateUI(account);
        } catch (ApiException e) {


            String h = e.getMessage().toString();
            String j = h;
            Log.v("h", h);

        }
    }


    private void onLoggedIn(GoogleSignInAccount googleSignInAccount) {
//        Intent intent = new Intent(this, MainActivity.class);
//        intent.putExtra(ProfileActivity.GOOGLE_ACCOUNT, googleSignInAccount);
//
//        startActivity(intent);
        finish();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        GoogleSignInAccount alreadyloggedAccount = GoogleSignIn.getLastSignedInAccount(this);
//        if (alreadyloggedAccount != null) {
//            Toast.makeText(this, "Already Logged In", Toast.LENGTH_SHORT).show();
//            onLoggedIn(alreadyloggedAccount);
//        } else {
//            Log.d(TAG, "Not logged in");
//        }
//    }

    private void deleteAccessToken() {
        AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(
                    AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

                if (currentAccessToken == null) {
                    //User logged out
                    prefUtil.clearToken();
                    LoginManager.getInstance().logOut();
                }
            }
        };
    }


    private Bundle getFacebookData(JSONObject object) {
        Bundle bundle = new Bundle();

        try {
            String id = object.getString("id");


            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            if (object.has("email")) {
                bundle.putString("email", object.getString("email"));
            }
            else {
                Log.v("firebasseRegID", firebasseRegID);
                name_fb = object.getString("first_name");
                Intent in = new Intent(LogInActivity.this, FbEmailActivity.class);
                in.putExtra("name", name_fb);
                in.putExtra("fb_id", fbID);
                in.putExtra("logInMetod", "fb");
                in.putExtra("token", accessToken_str);
                in.putExtra("firebasseRegID", firebasseRegID);
                startActivity(in);
            }
            try {
                name_fb = object.getString("first_name");
                email_fb = object.getString("email");
                if (email_fb != null && !email_fb.isEmpty() && !email_fb.equals("null")) {

                    FB_API();
                } else {
                    Intent in = new Intent(LogInActivity.this, FbEmailActivity.class);
                    in.putExtra("name", name_fb);
                    in.putExtra("fb_id", fbID);
                    in.putExtra("token", accessToken_str);
                    in.putExtra("logInMetod", "fb");
                    in.putExtra("firebasseRegID", firebasseRegID);
                    startActivity(in);


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            Log.d(TAG, "BUNDLE Exception : " + e.toString());
        }

        return bundle;
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }


        if (!validatePassword()) {
            return;
        }

        LogInCheck_api();
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    private boolean validatePassword() {
        if (edt_Pwd.getText().toString().trim().isEmpty()) {
            edt_Pwd.setError(getString(R.string.validPassword));
            requestFocus(edt_Pwd);
            return false;
        }

        return true;
    }

    private void LogInCheck_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        try {

                            SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

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
                                    login_token = user_obj.getString("login_token");
                                    String lname = user_obj.getString("lname");
                                    String profile_pic = "http://serviceapp.whyteapps.com/storage/app/" + user_obj.getString("profile_pic");
                                    String customertype = user_obj.getString("customertype");


                                    editor.putString("user_id", user_id);
                                    editor.putString("customer_name", customer_name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", user_email);
                                    editor.putString("customertype", customertype);
                                    editor.putString("lname", lname);
                                    editor.putString("logInMetod", "normal");
                                    editor.putString("profile_pic", profile_pic);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();
                                    Log.v("loginPage_token", login_token);
                                    Log.v("profile_pic", profile_pic);
                                    DeviceChkApi("normal");
//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);
                                    finish();

                                } else {

                                    edt_Email.setText("");
                                    requestFocus(edt_Email);
                                    edt_Pwd.setText("");
                                    editor.putInt("logedIn", 0);
                                    editor.commit();
                                    Toast.makeText(getApplicationContext(), "Your username or password is incorrect. please check your credentials and try again", Toast.LENGTH_SHORT).show();

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
                params.put("email", edt_Email.getText().toString().trim());
                params.put("password", edt_Pwd.getText().toString().trim());

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void DeviceChkApi(String from) {
        //creating a string request to send request to the url

        URL_DEVICE_REG = utilities.GetUrl() + "accounts/registerdevice";

        //login_token = preferences.getString("login_token", "");


        // Log.v("login_token_LogIn", login_token);
//        Log.v("refreshedToken_LogIn", refreshedToken);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_DEVICE_REG,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msg", msg);
                                    if(from.equals("normal"))
                                    {
                                        Intent in = new Intent(LogInActivity.this, MainActivity.class);
                                        startActivity(in);
                                        finish();
                                    }
                                    else
                                    {
                                        Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
                                        startActivity(in);
                                        finish();
                                    }

                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", firebasseRegID);
                params.put("device_type", "Android");
                params.put("device_os", "Android");

                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void FB_API() {
        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_FB = utilities.GetUrl() + "sign-up-sm";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FB,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    //   String login_token = jsonObj.getString("login_token");
                                    String jh = login_token;
                                    Log.v("login_token_fb", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");
                                    String customertype = jsonObj.getString("customertype");
                                    editor.putString("customer_name", name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putString("logInMetod", "fb");
                                    editor.putString("customertype", customertype);
                                    editor.putInt("logedIn", 1);
                                    editor.commit();


//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);
                                    LoginManager.getInstance().logOut();
                                    Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
                                    startActivity(in);
                                    finish();
                                    // DeviceChkApi();


                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {
                                        JSONObject user = obj.getJSONObject("user");
                                        String email = user.getString("email");
                                        if (email == null && email.isEmpty() && email.equals("null")) {
                                            editor.putInt("fb_email", 0);
                                        }
                                        Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
                                        startActivity(in);
                                        editor.putString("logInMetod", "fb");
                                        editor.commit();
                                        Log.v("Ok", "ok");

                                    }

                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> params = new HashMap<>();
                params.put("name", name_fb);
                params.put("email", email_fb);
                //params.put("phone", "");
                params.put("customertype", "");
                params.put("fb_id", fbID);
                params.put("token", accessToken_str);

                editor.putString("name_fb", name_fb);
                editor.putString("email_fb", email_fb);
                editor.putString("fbID", fbID);
                editor.putString("accessToken_str", accessToken_str);
                editor.commit();


                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    private void googleLogIn() {
        //creating a string request to send request to the url
        final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        URL_GOOGLE = utilities.GetUrl() + "sign-up-google";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GOOGLE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();
                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        try {


                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            String status = obj.getString("status");
                            if (status.equals("200")) {
                                String success = obj.getString("success");

                                if (success.equals("1")) {

                                    JSONObject jsonObj = obj.getJSONObject("user");

                                    login_token = jsonObj.getString("login_token");
                                    Log.v("login_token_fb", login_token);
                                    String email = jsonObj.getString("email");
                                    String name = jsonObj.getString("name");
                                    String customertype = jsonObj.getString("customertype");
                                    editor.putString("customer_name", name);
                                    editor.putString("login_token", login_token);
                                    editor.putString("user_email", email);
                                    editor.putString("customertype", customertype);
                                    Log.v("customertype_GL", customertype);
                                    editor.putString("logInMetod", "gmail");
                                    editor.putInt("logedIn", 1);
                                    editor.commit();
                                    Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
                                    startActivity(in);
                                    finish();
                                    // DeviceChkApi();


//                                    Intent in = new Intent(LogInActivity.this, MainActivity.class);
//                                    startActivity(in);


                                } else if (success.equals("0")) {

                                    String msg = obj.getString("msg");
                                    Log.v("msgh", msg);
                                    if (msg.equals("-1")) {

                                        Intent in = new Intent(LogInActivity.this, SignUpDetailsSocialLogIn.class);
                                        startActivity(in);
                                        editor.putString("logInMetod", "gmail");
                                        editor.commit();
                                        Log.v("Ok", "ok");

                                    }

                                } else {


                                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();

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
                SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                Map<String, String> params = new HashMap<>();
                params.put("name", googleName);
                params.put("email", googleEmail);
                // params.put("phone", "");
                params.put("customertype", "");
                params.put("gmail_id", googleID);
                params.put("token", googleToken);
                editor.putString("googleName", googleName);
                editor.putString("googleEmail", googleEmail);
                editor.putString("googleID", googleID);
                editor.putString("googleToken", googleToken);
                editor.commit();


                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }


}
