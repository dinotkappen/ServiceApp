package com.app.service.Fragment;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.app.service.Adapter.All_Service_Adapter;
import com.app.service.Model.All_Service_Model;
import com.app.service.R;
import com.app.service.Utilitiesa.Utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cc.cloudist.acplibrary.ACProgressFlower;

import static com.app.service.Activity.MainActivity.backArrow;
import static com.app.service.Activity.MainActivity.imgSearch;
import static com.app.service.Activity.MainActivity.realativeActionBar;


public class ServiceFragment extends Fragment {
    View rootView;

    private RecyclerView recycler_view_All_Service;
    private All_Service_Adapter obj_all_service_adapter_;

    ArrayList<All_Service_Model> all_service_Model = new ArrayList<All_Service_Model>();
    Utilities utilities;

    public ServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (utilities.isOnline(getActivity())) {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(R.layout.fragment_service, container, false);
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
//            img_search_actionbar.setImageResource(R.drawable.search);
//            img_search_actionbar.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
////                    getActivity().onBackPressed();
//                    Fragment fragment = new SearchFragment();
//                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
//                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.fragment_container, fragment);
//                     fragmentTransaction.addToBackStack(null);
//                    fragmentTransaction.commit();
//                }
//            });
            recycler_view_All_Service = (RecyclerView) rootView.findViewById(R.id.recycler_view_All_Service);
            loadAllServices_api();
        } else {
            rootView = inflater.inflate(R.layout.no_internet_msg_layout, container, false);
        }
        return rootView;
    }

    private void set_All_Services() {
        obj_all_service_adapter_ = new All_Service_Adapter(getActivity(), all_service_Model);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        recycler_view_All_Service.setLayoutManager(mLayoutManager);
        recycler_view_All_Service.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));
        recycler_view_All_Service.setItemAnimator(new DefaultItemAnimator());
        recycler_view_All_Service.setAdapter(obj_all_service_adapter_);
    }


    private void loadAllServices_api() {
        //creating a string request to send request to the url

        final ACProgressFlower dialog = new ACProgressFlower.Builder(getActivity())
                .build();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        String JSON_URL_SERVICES = utilities.GetUrl()+"categories";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL_SERVICES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {
                            //getting the whole json object from the response
                            JSONObject obj = new JSONObject(response);

                            String status = obj.getString("status");
                            if (status.equals("200")) {

                                dialog.dismiss();
                                //so here we are getting that json array
                                JSONArray jsonarray = obj.getJSONArray("categories");


                                all_service_Model.clear();
                                //now looping through all the elements of the json array
                                for (int i = 0; i < jsonarray.length(); i++) {
                                    //getting the json object of the particular index inside the array
                                    JSONObject jsondata = jsonarray.getJSONObject(i);
//                                    String name = heroObject.getString("name");
//                                    String cover = heroObject.getString("cover");
                                    all_service_Model.add(new All_Service_Model(jsondata.getString("id"),
                                            jsondata.getString("name"), jsondata.getString("cover")));

                                }


                                set_All_Services();
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
                        dialog.dismiss();
                        //displaying the error in toast if occurrs
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        //creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //adding the string request to request queue
        requestQueue.add(stringRequest);

    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}
