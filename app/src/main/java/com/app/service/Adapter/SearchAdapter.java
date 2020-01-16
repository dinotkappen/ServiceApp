package com.app.service.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.service.Fragment.ServiceDetailFragment;
import com.app.service.Fragment.SubMenuFragment;
import com.app.service.Model.SearchModel;
import com.app.service.R;
import com.bumptech.glide.Glide;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

    private Context mContext;
    private List<SearchModel> objSearchModel;
    public static final String MY_PREFS_NAME = "MyPrefsFile";

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView thumbnail;
        public LinearLayout layout_card;
        CardView card_view;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title_search);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail_search);
            layout_card = (LinearLayout) view.findViewById(R.id.layout_card_search);
            card_view = (CardView) view.findViewById(R.id.card_view_search);

        }
    }


    public SearchAdapter(Context mContext, List<SearchModel> objSearchModel) {
        this.mContext = mContext;
        this.objSearchModel = objSearchModel;

    }

    @Override
    public SearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_search_card, parent, false);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        return new SearchAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final SearchAdapter.MyViewHolder holder, final int position) {
        final SearchModel itemMainModel = objSearchModel.get(position);

        // holder.title.setText(objSearchModel.get(position).getName());
//        for (int i = 0; i < objSearchModel.size(); i++) {
//            String h = objSearchModel.get(i).getName().toString();
//            String p = h;
//            holder.title.setText(objSearchModel.get(i).getName());
//        }
        holder.title.setText(itemMainModel.getSearchNameme());

holder.layout_card.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        try {
            SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("subProductId", "" + objSearchModel.get(position).getSearchID());
            editor.apply();
            String h = "" + objSearchModel.get(position).getSearchID();
            Log.v("h",h);
            String l = h;
            Fragment fragment = new ServiceDetailFragment();
            FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } catch (Exception ex) {
            String h = ex.getMessage().toString();
            String k = h;
        }
    }
});
        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
                    editor.putString("subProductId", "" + objSearchModel.get(position).getSearchID());
                    editor.apply();
                    String h = "" + objSearchModel.get(position).getSearchID();
                    Log.v("h",h);
                    String l = h;
                    Fragment fragment = new ServiceDetailFragment();
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } catch (Exception ex) {
                    String h = ex.getMessage().toString();
                    String k = h;
                }
            }
        });
//

        // loading itemMainModel cover using Glide library
        Glide.with(mContext).load(itemMainModel.getSearchImage()).into(holder.thumbnail);

    }


    @Override
    public int getItemCount() {
        return objSearchModel.size();
    }
}
