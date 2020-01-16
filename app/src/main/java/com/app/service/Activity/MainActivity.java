package com.app.service.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.service.Fragment.ChangeLangFragment;
import com.app.service.Fragment.MapsFragment;
import com.app.service.Fragment.ReceiptDetailsFragment;
import com.app.service.Fragment.SelectCountryFragment;
import com.app.service.Fragment.ServiceDetailFragment;
import com.app.service.Fragment.SubMenuFragment;
import com.app.service.Utilitiesa.Config;
import com.app.service.Utilitiesa.NotificationUtils;
import com.app.service.Utilitiesa.Utilities;
import com.bumptech.glide.Glide;
import com.app.service.Fragment.AboutFragment;
import com.app.service.Fragment.ContactFragment;
import com.app.service.Fragment.HomeFragment;
import com.app.service.Fragment.MyReceiptFragment;
import com.app.service.Fragment.NotificationFragment;
import com.app.service.Fragment.PlaceOrderFragment;
import com.app.service.Fragment.ProfileFragment;
import com.app.service.Fragment.SearchFragment;
import com.app.service.Fragment.ServiceFragment;
import com.app.service.R;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, BottomNavigationView.OnNavigationItemSelectedListener {
    View view;
    LinearLayout nav_head_profile;
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    public static final String PREFS_NAME = "PrefsFile";
    SharedPreferences preferences;
    String str_user_name, str_user_pic;
    TextView prof_name, txt_logout;
    LinearLayout linearLogOut, linearSignIn;
    ImageView profile_image_header;
    String refreshedToken;
    Utilities utilities;
    InputMethodManager imm;
    HomeFragment homeFragment;
  public static DrawerLayout drawer;
    FragmentManager fragmentManager;
   public static MainActivity mainActivity;
    int logedIn;

    public static ImageView imgMenu,backArrow,imgSearch;
    public static RelativeLayout realativeActionBar;

    // private TextView txtRegId, txtMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


            if (utilities.isOnline(this)) {
                Fabric.with(this, new Crashlytics());
                setContentView(R.layout.activity_main);
                imgMenu=(ImageView)findViewById(R.id.imgMenu);
                backArrow=(ImageView)findViewById(R.id.backArrow);
                imgSearch=(ImageView)findViewById(R.id.imgSearch);
                realativeActionBar=(RelativeLayout) findViewById(R.id.realativeActionBar);
                realativeActionBar.setVisibility(View.VISIBLE);

                Hawk.init(this)
                        .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                        .setStorage(HawkBuilder.newSqliteStorage(this))
                        .setLogLevel(LogLevel.FULL)
                        .build();
                mainActivity = MainActivity.this;
                imgMenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggle();
                    }
                });
                backArrow.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
//                Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
//
//                setSupportActionBar(toolbar);
//
//                toolbar.bringToFront();
//                Intent intent = getIntent();
//                String notificationId = intent.getStringExtra("notificationId");
//                String myReceiptFragment = intent.getStringExtra("myReceiptFragment");
//                String myReceiptFragmentID = intent.getStringExtra("myReceiptFragmentID");

                preferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);

                logedIn = preferences.getInt("logedIn", 0);
                str_user_name = preferences.getString("customer_name", "");
                str_user_pic = preferences.getString("profile_pic", "");
                //str_user_pic = "http://serviceapp.whyteapps.com/storage/app/" + str_user_pic;
                Log.v("prof_pic_main", str_user_pic);
                imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                view = getWindow().getDecorView();
                drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//
//                toggle.setDrawerIndicatorEnabled(false);
//                toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        drawer.openDrawer(GravityCompat.START);
//                    }
//                });
//                toggle.setHomeAsUpIndicator(R.mipmap.menu_icon);
//                drawer.addDrawerListener(toggle);
//                toggle.syncState();

//            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view_drawer);
//            navigationView.setNavigationItemSelectedListener(this);
//            View headerview = navigationView.getHeaderView(0);

                NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
                navigationView.setNavigationItemSelectedListener(this);


                //getting bottom navigation view and attaching the listener
                final BottomNavigationView navigation = findViewById(R.id.navigation_bottom);
                navigation.setOnNavigationItemSelectedListener(this);

                Intent mIntent = getIntent();
                int intValue = mIntent.getIntExtra("from_maps", 0);
                String notificationId = preferences.getString("notificationId", "0");
                String myReceiptFragment = preferences.getString("myReceiptFragment", "0");
                String myReceiptFragmentID = preferences.getString("receiptId", "0");
                if (intValue == 1) {

                    loadFragment(new PlaceOrderFragment());
                } else if (notificationId.equals("1")) {
                    SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString("notificationId", "0");
                    editor.commit();
                    loadFragment(new ReceiptDetailsFragment());
                } else {
                    loadFragment(new HomeFragment());
                }


                nav_head_profile = (LinearLayout) navigationView.findViewById(R.id.nav_head_profile);
                prof_name = (TextView) navigationView.findViewById(R.id.prof_name);
                txt_logout = (TextView) navigationView.findViewById(R.id.txt_logout);
                linearLogOut = (LinearLayout) navigationView.findViewById(R.id.linearLogOut);
                linearSignIn = (LinearLayout) navigationView.findViewById(R.id.linearSignIn);
                profile_image_header = (ImageView) navigationView.findViewById(R.id.profile_image_header);

                if (logedIn == 1) {
                    linearSignIn.setVisibility(View.GONE);
                    linearLogOut.setVisibility(View.VISIBLE);
                    prof_name.setVisibility(View.VISIBLE);
                } else {
                    linearSignIn.setVisibility(View.VISIBLE);
                    linearLogOut.setVisibility(View.GONE);
                    prof_name.setVisibility(View.GONE);
                }
                linearSignIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);

                    }
                });


                //***To load profile image in navigation header ****
                updateNavHeaderView();


//            Glide.with(this)
//                    .load(str_user_pic)
//                    .placeholder(R.drawable.profile_sample)
//                    .into(profile_image_header);


                Log.v("prof_pic_drawer", str_user_pic);

                nav_head_profile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (drawer.isDrawerOpen(GravityCompat.START)) {
                            drawer.closeDrawer(GravityCompat.START);
                        }
                        Fragment fragment = new ProfileFragment();
                        fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                });
                linearLogOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this);

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
                                        SharedPreferences sharedpreferences = getSharedPreferences(MY_PREFS_NAME, Context.MODE_PRIVATE);
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
                                        Hawk.put("selectedLang","en");

                                        editor.commit();
                                        Intent in = new Intent(MainActivity.this, SplashActivity.class);
                                        startActivity(in);

                                    }
                                });


                        builder.show();
                    }
                });


            } else {
                setContentView(R.layout.no_internet_msg_layout);
            }



    }

    public void updateNavHeaderView() {
        try {
            str_user_name = preferences.getString("customer_name", "");
            str_user_pic = preferences.getString("profile_pic", "");
            Log.v("MainProfile", str_user_pic);
            if (str_user_pic != null && !str_user_pic.isEmpty() && !str_user_pic.equals("")) {
                Glide.with(profile_image_header.getContext()).load(str_user_pic).placeholder(R.drawable.profile_sample)
                        .dontAnimate().into(profile_image_header);

            }
            if (str_user_name != null && !str_user_name.isEmpty() && !str_user_name.equals("")) {
                prof_name.setText(str_user_name);
            }
        } catch (Exception ex) {
            Log.v("updateNavHeaderView", ex.getMessage().toString());
        }
    }
//public static void backSubMenu ()
//{
//
//    Fragment fragment_back = mainActivity.getSupportFragmentManager()
//            .findFragmentById(R.id.fragment_container);
//    if (fragment_back instanceof SubMenuFragment) {
//        backMain();
//    }
//    else if(fragment_back instanceof ServiceDetailFragment)
//    {
//        Fragment fragment1 = new SubMenuFragment();
//        mainActivity.getSupportFragmentManager().
//                beginTransaction().
//                add(R.id.fragment_container, fragment1).
//                addToBackStack(null).
//                commit();
//    }
//
//    else if(fragment_back instanceof PlaceOrderFragment)
//    {
//        Fragment fragment1 = new MapsFragment();
//        mainActivity.getSupportFragmentManager().
//                beginTransaction().
//                add(R.id.fragment_container, fragment1).
//                addToBackStack(null).
//                commit();
//    }
//
//}
    private void toggle() {
        if (drawer.isDrawerVisible(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            drawer.openDrawer(GravityCompat.START);
        }
    }
    @Override
    public void onBackPressed() {

        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        Fragment fragment_back = getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (fragment_back instanceof SubMenuFragment) {
               backMain();
            }
            else if (fragment_back instanceof ServiceDetailFragment) {
                getSupportFragmentManager().popBackStack();
            }
          else  if (fragment_back instanceof MapsFragment) {
                getSupportFragmentManager().popBackStack();
            } else if (fragment_back instanceof PlaceOrderFragment) {
                getSupportFragmentManager().popBackStack();
            }  else if (fragment_back instanceof ProfileFragment) {
                backMain();
            } else if (fragment_back instanceof SelectCountryFragment) {
                backMain();
            } else if (fragment_back instanceof ContactFragment) {
                getSupportFragmentManager().popBackStack();
            } else if (fragment_back instanceof ServiceFragment) {
                backMain();
            } else if (fragment_back instanceof NotificationFragment) {
                backMain();
            } else if (fragment_back instanceof MyReceiptFragment) {
                backMain();
            } else if (fragment_back instanceof ChangeLangFragment) {
                backMain();
            } else if (fragment_back instanceof SearchFragment) {
                backMain();
            }
            else if (fragment_back instanceof AboutFragment) {
                backMain();
            }else if (fragment_back instanceof HomeFragment) {

                moveTaskToBack(true);
            } else {
                super.onBackPressed();
            }
        }
    }

    public void backMain(){
        Fragment fragment1 = new HomeFragment();
        getSupportFragmentManager().
                beginTransaction().
                add(R.id.fragment_container,fragment1).
                addToBackStack(null).
                commit();
    }

    public boolean onClick(View view) {

        drawer.closeDrawer(GravityCompat.START);
        Fragment fragment = null;
        try {
            switch (view.getId()) {
                case R.id.txt_home: {
                    drawer.closeDrawers();

                    fragment = new HomeFragment();
                    //your code.

                }
                break;
                case R.id.txt_service: {
                    drawer.closeDrawers();
                    fragment = new ServiceFragment();
                    //your code.

                }
                break;

                case R.id.txt_Notification: {
                    drawer.closeDrawers();
                    if (logedIn == 1) {
                        fragment = new NotificationFragment();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }
                    //your code.

                }
                break;
                case R.id.txt_receipt: {
                    drawer.closeDrawers();
                    if (logedIn == 1) {
                        fragment = new MyReceiptFragment();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }


                    //your code.

                }
                break;
                case R.id.txt_profile: {
                    drawer.closeDrawers();
                    if (logedIn == 1) {
                        fragment = new ProfileFragment();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }

                    //your code.

                }
                break;

                case R.id.txt_changeLang: {
                    drawer.closeDrawers();

                    fragment = new ChangeLangFragment();


                }
                break;
                case R.id.txt_changeCountry: {
                    drawer.closeDrawers();

                    fragment = new SelectCountryFragment();

                    //your code.

                }
                break;

                case R.id.txt_about: {
                    drawer.closeDrawers();
                    fragment = new AboutFragment();

                    //your code.

                }
                break;
                case R.id.txt_contactus: {
                    drawer.closeDrawers();
                    fragment = new ContactFragment();

                    //your code.

                }
                break;


            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            String h = msg;
        }

        return loadFragment(fragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        //   displaySelectedScreen(item.getItemId());
        try {
            switch (item.getItemId()) {

//                //** Navigatin menu **
//
//                case R.id.nav_home:
//                    fragment = new HomeFragment();
//
//                    break;
//
//                case R.id.nav_service:
//                    fragment = new ServiceFragment();
//                    break;
//
//                case R.id.nav_notifications:
//                    fragment = new NotificationFragment();
//
//                    break;
//
//                case R.id.nav_receipt:
//                    fragment = new MyReceiptFragment();
//
//                    break;
//
//                case R.id.nav_about:
//                    fragment = new AboutFragment();
//
//                    break;
//                case R.id.nav_contact:
//                    fragment = new ContactFragment();
//
//                    break;


                //*****Bottom menu**

                case R.id.navigation_home:
                    fragment = new HomeFragment();


                    break;

                case R.id.navigation_search:
                    fragment = new SearchFragment();
                    break;

                case R.id.navigation_profile:
                    if (logedIn == 1) {
                        fragment = new ProfileFragment();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }


                    break;

                case R.id.navigation_notifications:
                    if (logedIn == 1) {
                        fragment = new NotificationFragment();
                    } else {
                        Intent intent = new Intent(MainActivity.this, LogInActivity.class);
                        startActivity(intent);
                    }

                    break;


            }
        } catch (Exception ex) {
            String msg = ex.getMessage().toString();
            String h = msg;
        }

        return loadFragment(fragment);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {

            fragmentManager = mainActivity.getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commitAllowingStateLoss();

            mainActivity.drawer = mainActivity.findViewById(R.id.drawer_layout);
            mainActivity.drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }


}
